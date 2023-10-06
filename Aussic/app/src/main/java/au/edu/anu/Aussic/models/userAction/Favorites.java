package au.edu.anu.Aussic.models.userAction;

public class Favorites extends UserAction{


    public Favorites(String actionType, String username, String targetSong, Integer targetSongId) {
        super(actionType, username, targetSong, targetSongId);
    }

    @Override
    public String getToastMessage() {
        return String.format("User %s added the song \"%s\" to favorites", username, targetSong);
    }

    @Override
    public void update() {

    }
}
