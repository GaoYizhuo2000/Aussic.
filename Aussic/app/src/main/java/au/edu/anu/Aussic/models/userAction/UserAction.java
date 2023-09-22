package au.edu.anu.Aussic.models.userAction;

import java.util.Date;

public abstract class UserAction {
    protected String actionType;
    protected String username;
    protected String targetSong;

    public UserAction(String username, String targetSong, String actionType) {
        this.username = username;
        this.targetSong = targetSong;
        this.actionType = actionType;
    }
    abstract String getToastMessage();

}
