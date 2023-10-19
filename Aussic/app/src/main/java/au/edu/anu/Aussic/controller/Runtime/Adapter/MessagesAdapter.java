package au.edu.anu.Aussic.controller.Runtime.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;

/**
 * @author: u7516507, Evan Cheung
 * An adapter for P2P messages list, include owner and peer user
 */

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CommentItem> messages;

    public MessagesAdapter(List<CommentItem> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getUserName().equals(RuntimeObserver.currentUser.username) ? 1 : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new PeerMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            return new OwnerMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentItem message = messages.get(position);
        if (holder.getItemViewType() == 0) {
            ((PeerMessageViewHolder) holder).bind(message);
        } else {
            ((OwnerMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class PeerMessageViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userName;
        TextView commentContent;

        public PeerMessageViewHolder(View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            userName = itemView.findViewById(R.id.user_name);
            commentContent = itemView.findViewById(R.id.user_comment);
        }

        void bind(CommentItem message) {
            Glide.with(userAvatar.getContext())
                    .load(message.getIconUrl())
                    .into(userAvatar);
            userName.setText(message.getUserName());
            commentContent.setText(message.getCommentContent());
        }
    }

    class OwnerMessageViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userName;
        TextView commentContent;

        public OwnerMessageViewHolder(View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.my_avatar);
            userName = itemView.findViewById(R.id.my_name);
            commentContent = itemView.findViewById(R.id.my_comment);
        }

        void bind(CommentItem message) {
            Glide.with(userAvatar.getContext())
                    .load(message.getIconUrl())
                    .into(userAvatar);
            userName.setText(message.getUserName());
            commentContent.setText(message.getCommentContent());
        }
    }
}

