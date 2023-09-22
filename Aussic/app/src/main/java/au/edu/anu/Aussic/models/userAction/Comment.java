package au.edu.anu.Aussic.models.userAction;

/**
 *
 *
 * @author Yizhuo Gao
 */
public class Comment extends UserAction{
    protected String content;

    public Comment(String username, String targetSong, String content, String actionType) {
        super(username, targetSong, actionType);
        this.content = content;
    }

    @Override
    String getToastMessage() {
        return String.format("User %s commented on the song \"%s\": %s", username, targetSong, content);
    }
}
