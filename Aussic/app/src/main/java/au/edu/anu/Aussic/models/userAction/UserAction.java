package au.edu.anu.Aussic.models.userAction;

import java.util.Date;

public abstract class UserAction {
    protected String username;
    protected String targetSong;

    public UserAction(String username, String targetSong) {
        this.username = username;
        this.targetSong = targetSong;
    }
    abstract String getToastMessage();

}
