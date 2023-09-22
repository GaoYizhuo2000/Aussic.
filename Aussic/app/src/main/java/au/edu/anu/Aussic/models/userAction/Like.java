package au.edu.anu.Aussic.models.userAction;

import java.util.Date;

public class Like extends UserAction{
    public Like(String username, String targetSong, Date actionTime) {
        super(username, targetSong);
    }

    @Override
    String getToastMessage() {
        return String.format("User %s liked the song \"%s\"", username, targetSong);
    }
}
