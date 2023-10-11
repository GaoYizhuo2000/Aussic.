package au.edu.anu.Aussic.controller.homePages.Adapter;

public class CommentItem {
    private int userAvatarResId;  // Let's assume it's a drawable resource ID for simplicity
    private String userName;
    private String commentContent;

    public CommentItem(int userAvatarResId, String userName, String commentContent) {
        this.userAvatarResId = userAvatarResId;
        this.userName = userName;
        this.commentContent = commentContent;
    }

    // Getters
    public int getUserAvatarResId() {
        return userAvatarResId;
    }

    public String getUserName() {
        return userName;
    }

    public String getCommentContent() {
        return commentContent;
    }
}
