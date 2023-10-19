package au.edu.anu.Aussic.controller.homePages.P2P;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.Adapter.CommentItem;
import au.edu.anu.Aussic.controller.Runtime.Adapter.MessagesAdapter;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataChangeListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.controller.entityPages.SongActivity;
import au.edu.anu.Aussic.models.entity.MessageContent;
import au.edu.anu.Aussic.models.entity.User;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;
import au.edu.anu.Aussic.models.userAction.Comment;

/**
 * @author: u7516507, Evan Cheung
 */

public class MessageActivity extends AppCompatActivity implements OnDataChangeListener {
    /** The current peer user for messaging. */
    private User userPeer;

    /** Image representation for blocking actions. */
    private ImageView blockSign;

    /** Text representation for block status. */
    private TextView blockMessage;

    /** Input field for entering messages. */
    private EditText input;

    /** Button to trigger message sending. */
    private Button sendBtn;

    /** Recycler view to display messages. */
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        RuntimeObserver.addOnDataChangeListener(this);

        this.userPeer = RuntimeObserver.currentMessagingSession.getPeerUsr();
        this.blockSign = findViewById(R.id.block_sign);
        this.blockMessage = findViewById(R.id.block_message);
        this.input = findViewById(R.id.message_input);
        this.sendBtn = findViewById(R.id.the_message_btn_send);
        this.recyclerView = findViewById(R.id.messages_list);

        // Set up click listener for the block button
        this.blockSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreDao firestoreDao = new FirestoreDaoImpl();
                if(RuntimeObserver.currentUser.getBlockList().contains(userPeer.username))
                    firestoreDao.removeBlockList(userPeer.username);
                else
                    firestoreDao.updateBlockList(userPeer.username);
            }
        });

        // Set up click listener for the sendBtn
        this.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the message from the EditText
                String messageText = input.getText().toString().trim();

                if (!messageText.isEmpty()) {
                    // Update message in firestore
                    FirestoreDao firestoreDao = new FirestoreDaoImpl();
                    firestoreDao.updateHistory(RuntimeObserver.currentMessagingSession.getName(), messageText);
                    // Clear the EditText after sending the comment
                    input.setText("");

                } else {
                    Toast.makeText(MessageActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // React to any data change when created
        onDataChangeResponse();

    }

    /**
     * Responds to changes in data, updates UI elements
     * like block indicators and message list.
     */
    @Override
    public void onDataChangeResponse() {
        if(this.userPeer == null) {
            this.userPeer = RuntimeObserver.currentMessagingSession.getPeerUsr();
            if (this.userPeer != null) onDataChangeResponse();
        }
        else{
            if(userPeer.getBlockList().contains(RuntimeObserver.currentUser.username))
                this.blockMessage.setText("This user has blocked you...");

            if(RuntimeObserver.currentUser.getBlockList().contains(userPeer.username)) {
                this.blockSign.setImageResource(R.drawable.ic_block_open);
                this.blockMessage.setText("You have blocked this user...");
            }
            else this.blockSign.setImageResource(R.drawable.ic_block_block);

            List<CommentItem> messages = new ArrayList<>();

            if(!(RuntimeObserver.currentUser.getBlockList().contains(userPeer.username) || userPeer.getBlockList().contains(RuntimeObserver.currentUser.username))){
                this.blockMessage.setText("");
                for (MessageContent messageContent : RuntimeObserver.currentMessagingSession.getHistory()){
                    CommentItem message;

                    if(messageContent.getUserName().equals(RuntimeObserver.currentUser.username)) message = new CommentItem(RuntimeObserver.currentUser.iconUrl, RuntimeObserver.currentUser.username, messageContent.getMessage());
                    else message = new CommentItem(this.userPeer.iconUrl, this.userPeer.username, messageContent.getMessage());

                    messages.add(message);
                }
            }

            if(recyclerView.getAdapter() == null) this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            if(recyclerView.getAdapter() == null) recyclerView.setAdapter(new MessagesAdapter(messages));
            else if(recyclerView.getAdapter().getItemCount() != messages.size()) recyclerView.setAdapter(new MessagesAdapter(messages));
        }
    }
}