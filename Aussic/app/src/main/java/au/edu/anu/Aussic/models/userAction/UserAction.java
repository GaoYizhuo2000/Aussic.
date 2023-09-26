package au.edu.anu.Aussic.models.userAction;

/**
 *
 *
 * @author Yizhuo Gao
 */
public abstract class UserAction {
    protected String actionType;
    protected String username;
    protected String targetSong;

    public UserAction(String username, String targetSong, String actionType) {
        this.username = username;
        this.targetSong = targetSong;
        this.actionType = actionType;
    }
    public abstract String getToastMessage();

}
