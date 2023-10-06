package au.edu.anu.Aussic.models.userAction;

import java.util.Date;
/**
 *
 *
 * @author Yizhuo Gao
 */
public class Like extends UserAction{
    public Like(String actionType, String username, String targetSong, Integer targetSongId) {
        super(actionType, username, targetSong, targetSongId);
    }



    @Override
    public String getToastMessage() {
        return String.format("User %s liked the song \"%s\"", username, targetSong);
    }

    @Override
    public void update() {

    }
}
