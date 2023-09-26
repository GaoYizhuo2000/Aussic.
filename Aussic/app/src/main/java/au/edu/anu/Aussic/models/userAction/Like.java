package au.edu.anu.Aussic.models.userAction;

import java.util.Date;
/**
 *
 *
 * @author Yizhuo Gao
 */
public class Like extends UserAction{
    public Like(String username, String targetSong, String actionType) {
        super(username, targetSong, actionType);
    }

    @Override
    public String getToastMessage() {
        return String.format("User %s liked the song \"%s\"", username, targetSong);
    }
}
