package au.edu.anu.Aussic.controller.Runtime.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import au.edu.anu.Aussic.R;

/**
 * @author: u7516507, Evan Cheung
 * the adapter for the comments in a song
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<CommentItem> commentList;

    public CommentAdapter(List<CommentItem> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentItem currentItem = commentList.get(position);
        holder.userName.setText(currentItem.getUserName());
        holder.commentContent.setText(currentItem.getCommentContent());
        //holder.userAvatar.setImageResource(currentItem.getUserAvatarResId());
        Glide.with(holder.userAvatar.getContext())
                .load(currentItem.getIconUrl())
                .into(holder.userAvatar);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userName;
        TextView commentContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            userName = itemView.findViewById(R.id.user_name);
            commentContent = itemView.findViewById(R.id.user_comment);
        }
    }
}

