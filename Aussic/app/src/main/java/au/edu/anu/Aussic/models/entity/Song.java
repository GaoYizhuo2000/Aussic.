package au.edu.anu.Aussic.models.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.controller.homePages.Adapter.CommentItem;
import au.edu.anu.Aussic.controller.observer.RuntimeObserver;

public class Song implements Comparable<Song> {
    private String id;
    private String type;
    private String href;
    private SongAttributes attributes;
    private PlayParams playParams;
    private List<Integer> likeUserId = new ArrayList<>();
    private Map<Integer, String> uidComment = new HashMap<>();
    private Comments comments;

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
//    public Song(Map<String, Object> fromFireStore) {
//        this.id = ((Map<String, Object>)((Map<String, Object>)fromFireStore.get("attributes")).get("playParams")).get("id").toString();
//        this.type = fromFireStore.get("type").toString();
//        this.href = fromFireStore.get("href").toString();
//        this.albumName = ((Map<String, Object>) fromFireStore.get("attributes")).get("albumName").toString();
//        this.genreNames = (List<String>)((Map<String, Object>) fromFireStore.get("attributes")).get("genreName");
//        Object a = fromFireStore.get("attributes");
//        long b = (long)((Map<String, Object>) a).get("trackNumber");
//        this.trackNumber = (int) b;
//        this.durationInMillis = (int) (((Map<String, Object>)fromFireStore.get("attributes")).get("durationInMillis"));
//        this.releaseDate = (((Map<String, Object>)fromFireStore.get("attributes")).get("releaseDate")).toString();
//        this.isrc = (((Map<String, Object>)fromFireStore.get("attributes")).get("isrc")).toString();
//        this.artwork = new Artwork(
//                (int)(((Map<String, Object>)((Map<String, Object>)fromFireStore.get("attributes")).get("artwork")).get("width")),
//                (int)(((Map<String, Object>)((Map<String, Object>)fromFireStore.get("attributes")).get("artwork")).get("height")),
//                (((Map<String, Object>)((Map<String, Object>)fromFireStore.get("attributes")).get("artwork")).get("url")).toString(),
//                (((Map<String, Object>)((Map<String, Object>)fromFireStore.get("attributes")).get("artwork")).get("bgColor")).toString(),
//                (((Map<String, Object>)((Map<String, Object>)fromFireStore.get("attributes")).get("artwork")).get("textColor1")).toString(),
//                (((Map<String, Object>)((Map<String, Object>)fromFireStore.get("attributes")).get("artwork")).get("textColor2")).toString(),
//                (((Map<String, Object>)((Map<String, Object>)fromFireStore.get("attribute")).get("artwork")).get("textColor3")).toString(),
//                (((Map<String, Object>)((Map<String, Object>)fromFireStore.get("attributes")).get("artwork")).get("textColor4")).toString()
//        );
//        this.composerName = ((((Map<String, Object>)fromFireStore.get("attributes")).get("composerName"))).toString();
//        this.url = ((((Map<String, Object>)fromFireStore.get("attributes")).get("url"))).toString();
//        this.playParams = new PlayParams(this.id, (((Map<String, Object>)((Map<String, Object>)fromFireStore.get("attributes")).get("playParams")).get("kind")).toString());
//        this.discNumber = (int)((((Map<String, Object>)fromFireStore.get("attributes")).get("discNumber")));
//        this.hasCredits = (boolean)((((Map<String, Object>)fromFireStore.get("attributes")).get("hasCredits")));
//        this.hasLyrics = (boolean)((((Map<String, Object>)fromFireStore.get("attributes")).get("hasLyrics")));
//        this.isAppleDigitalMaster = (boolean)((((Map<String, Object>)fromFireStore.get("attributes")).get("isAppleDigitalMaster")));
//        this.name = ((((Map<String, Object>)fromFireStore.get("attributes")).get("name"))).toString();
//        this.previews = new ArrayList<>();
//        this.previews.add(new Preview(((((Map<String, Object>)fromFireStore.get("attributes")).get("previews"))).toString()));
//        this.artistName = ((((Map<String, Object>)fromFireStore.get("attributes")).get("artistName"))).toString();
//    }

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

    public void addCommentItem(CommentItem commentItem){
        this.comments.getDetails().add(new Detail(commentItem.getCommentContent(), commentItem.getUserName()));
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

    }
}
