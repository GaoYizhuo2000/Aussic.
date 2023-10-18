package au.edu.anu.Aussic.models.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.controller.Runtime.Adapter.CommentItem;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;

/**
 * @author: u7581818, Oscar Wei
 * @author: u7552399, Yizhuo Gao
 * @author: u7516507, Evan Cheung
 */

public class Song implements Cloneable, Comparable<Song> {
    private String id;
    private String type;
    private String href;
    private SongAttributes attributes;
    private PlayParams playParams;
    private List<Integer> likeUserId = new ArrayList<>();
    private Map<Integer, String> uidComment = new HashMap<>();
    private Comments comments;
    private int favorites;
    private int likes;


    //private String

    // Constructor
    public Song(String id, String type, String href, SongAttributes attributes) {
        this.id = id;
        this.type = type;
        this.href = href;
        this.attributes = attributes;
    }
    public Song(String id) {
        this.id = id;
    }

    @Override
    public Song clone() {
        Song cloned = null;
        try {
            cloned = (Song) super.clone();
            if (this.attributes != null) {
                cloned.attributes = this.attributes.clone();
            }
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return cloned;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public SongAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(SongAttributes attributes) {
        this.attributes = attributes;
    }

    public PlayParams getPlayParams() {
        return playParams;
    }

    public List<Integer> getLikeUserId() {
        return likeUserId;
    }

    public Map<Integer, String> getUidComment() {
        return uidComment;
    }


    @Override
    public int compareTo(Song other) {
        return this.id.compareTo(other.id);  // 按照ID排序
    }

    // Other methods as needed
    // ...
    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", attributes='" + attributes.toString() +
                '}';
    }


    public String getArtistName() {
        return attributes.getArtistName();
    }

    public String getAlbumName() {
        return attributes.getAlbumName();
        // ... getters and setters
    }



    public String getKind() {
        return playParams.getKind();
    }

    public int getTrackNumber() {
        return attributes.getTrackNumber();
    }

    public int getDiscNumber() {
        return attributes.getDiscNumber();
    }

    public String getSongName() {
        return attributes.getName();
    }

        // ... getters and setters
    public List<String> getGenre() {
        return attributes.getGenreNames();
    }

    public String getReleaseDate() {
        return attributes.getReleaseDate();
    }

    public String getComposerName() {
        return attributes.getComposerName();
    }
    public String getUrlToListen() {
        return this.attributes.getPreviews().get(0).getUrlToListen();
    }
    public String getUrlToImage() {
        return this.attributes.getArtwork().getUrlToImage();
    }

    public Comments getComments() {
        return comments;
    }

    public List<CommentItem> getCommentItems() {
        List<CommentItem> out = new ArrayList<>();
        for (Detail detail : this.comments.getDetails()){
            out.add(new CommentItem(RuntimeObserver.currentUser.iconUrl, detail.getUid(), detail.getContent()));
        }
        return out;
    }

    public int getFavorites() {
        return favorites;
    }

    public int getLikes() {
        return likes;
    }

    public void setSong(Song song){
        this.id = song.getId();
        this.type = song.getType();
        this.href = song.getHref();
        this.attributes = song.getAttributes();
        this.playParams = song.getPlayParams();
        this.likeUserId = song.getLikeUserId();
        this.uidComment = song.getUidComment();
        this.comments = song.getComments();
        this.favorites = song.getFavorites();
        this.likes = song.getLikes();

    }
}
