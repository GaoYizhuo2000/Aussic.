package au.edu.anu.Aussic.controller.Runtime.Adapter;

/**
 * @author: u7516507, Evan Cheung
 */

public class CommentItem {
    private String iconUrl;  // Let's assume it's a drawable resource ID for simplicity
    private String userName;
    private String commentContent;

    public CommentItem(String iconUrl, String userName, String commentContent) {
        this.iconUrl = iconUrl;
        this.userName = userName;
        this.commentContent = commentContent;
    }

    // Getters
    public String getIconUrl() {
        return iconUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getCommentContent() {
        return commentContent;
    }
}
