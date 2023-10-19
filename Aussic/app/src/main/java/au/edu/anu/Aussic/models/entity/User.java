package au.edu.anu.Aussic.models.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: u7516507, Evan Cheung
 * @author: u7552399, Yizhuo Gao
 */

//After user registration, create a new user and upload it to firestore.
//After logging in, pass the user ID to homeactivity, and then pass it when instantiating the
// personal homepage fragment. Then when the fragment is initialized, the user information is
// obtained from firebastore based on the user ID and displayed.
public class User {
    public String username;
    private List<String> favorites = new ArrayList<>(); //放歌曲id
    private List<String> likes = new ArrayList<>();
    private List<String> blockList = new ArrayList<>();
    public String location = "";

    public String iconUrl;
    public String type;

    public User(String username, String iconUrl) {
        this.username = username;
        this.iconUrl = iconUrl;
    }

    public void update(){};

    public void addFavorites(String songID){ this.favorites.add(songID); }
    public void addLikes(String songID){ this.likes.add(songID); }

    public List<String> getFavorites() {
        return favorites;
    }
    public List<String> getLikes() {
        return likes;
    }

    public List<String> getBlockList() {
        return blockList;
    }

    public void setUsr(User usr){
        this.username = usr.username;
        this.favorites = usr.getFavorites();
        this.likes = usr.getLikes();
        this.location = usr.location;
        this.iconUrl = usr.iconUrl;
        this.type = usr.type;
        this.blockList = usr.getBlockList();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param object The object to compare to.
     * @return {@code true} if this object is the same as the object argument; {@code false} otherwise.
     *
     * @author: Evan Cheung
     */
    @Override
    public boolean equals(Object object){
        if(object instanceof User){
            if (((User) object).username.equals(this.username)) return true;
            return false;
        }
        return false;
    }
}
