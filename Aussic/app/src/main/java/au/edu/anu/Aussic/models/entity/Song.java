package au.edu.anu.Aussic.models.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Song implements Comparable<Song> {
    private String id;
    private String type;
    private String href;
    private SongAttributes attributes;
    private PlayParams playParams;
    private List<Integer> likeUserId = new ArrayList<>();
    private Map<Integer, String> uidComment = new HashMap<>();

    //private String

    // Constructor
    public Song(String id, String type, String href, SongAttributes attributes) {
        this.id = id;
        this.type = type;
        this.href = href;
        this.attributes = attributes;
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

    public List<String> getGenre() {
        return attributes.getGenreNames();
    }

    public String getReleaseDate() {
        return attributes.getReleaseDate();
    }

    public String getComposerName() {
        return attributes.getComposerName();
    }
}
