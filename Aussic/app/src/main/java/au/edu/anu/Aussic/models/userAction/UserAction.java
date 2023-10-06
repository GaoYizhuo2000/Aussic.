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
    protected Integer targetSongId;

    public UserAction(String actionType, String username, String targetSong, Integer targetSongId) {
        this.actionType = actionType;
        this.username = username;
        this.targetSong = targetSong;
        this.targetSongId = targetSongId;
    }


    public abstract String getToastMessage();

    //update data based on useraction
    public abstract void update();

}
