package au.edu.anu.Aussic.models.userAction;

import java.util.Date;

public class Comment extends UserAction{
    protected String content;

    public Comment(String username, String targetSong, Date actionTime, String content) {
        super(username, targetSong);
        this.content = content;
    }

    @Override
    String getToastMessage() {
        return String.format("User %s commented on the song \"%s\": %s", username, targetSong, content);
    }
}
