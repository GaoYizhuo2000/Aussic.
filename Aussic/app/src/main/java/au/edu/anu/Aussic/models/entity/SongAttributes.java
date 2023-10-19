package au.edu.anu.Aussic.models.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: u7581818, Oscar Wei
 */
public class SongAttributes implements Cloneable{
    private String albumName;
    private List<String> genreNames;
    private int trackNumber;
    private long durationInMillis;
    private String releaseDate;
    private String isrc;
    private Artwork artwork;
    private String composerName;
    private String url;
    private PlayParams playParams;
    private int discNumber;
    private boolean hasCredits;
    private boolean hasLyrics;
    private boolean isAppleDigitalMaster;
    private String name;
    private List<Preview> previews;
    private String artistName;

    public SongAttributes(String name, String artistName, String releaseDate, List<String> genreNames){
        this.name = name;
        this.artistName = artistName;
        this.releaseDate = releaseDate;
        this.genreNames = genreNames;
    }

    public SongAttributes(String name, String artistName) {
        this.name = name;
        this.artistName = artistName;
    }

    public SongAttributes(String albumName, List<String> genreNames, int trackNumber,
                          long durationInMillis, String releaseDate, String isrc, Artwork artwork,
                          String composerName, String url, PlayParams playParams, int discNumber,
                          boolean hasCredits, boolean hasLyrics, boolean isAppleDigitalMaster,
                          String name, List<Preview> previews, String artistName) {
        this.albumName = albumName;
        this.genreNames = genreNames;
        this.trackNumber = trackNumber;
        this.durationInMillis = durationInMillis;
        this.releaseDate = releaseDate;
        this.isrc = isrc;
        this.artwork = artwork;
        this.composerName = composerName;
        this.url = url;
        this.playParams = playParams;
        this.discNumber = discNumber;
        this.hasCredits = hasCredits;
        this.hasLyrics = hasLyrics;
        this.isAppleDigitalMaster = isAppleDigitalMaster;
        this.name = name;
        this.previews = previews;
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<String> getGenreNames() {
        return genreNames;
    }

    public void setGenreNames(List<String> genreNames) {
        this.genreNames = genreNames;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public long getDurationInMillis() {
        return durationInMillis;
    }

    public void setDurationInMillis(long durationInMillis) {
        this.durationInMillis = durationInMillis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getIsrc() {
        return isrc;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    public String getComposerName() {
        return composerName;
    }

    public void setComposerName(String composerName) {
        this.composerName = composerName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PlayParams getPlayParams() {
        return playParams;
    }

    public void setPlayParams(PlayParams playParams) {
        this.playParams = playParams;
    }

    public int getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(int discNumber) {
        this.discNumber = discNumber;
    }

    public boolean isHasCredits() {
        return hasCredits;
    }

    public void setHasCredits(boolean hasCredits) {
        this.hasCredits = hasCredits;
    }

    public boolean isHasLyrics() {
        return hasLyrics;
    }

    public void setHasLyrics(boolean hasLyrics) {
        this.hasLyrics = hasLyrics;
    }

    public boolean isAppleDigitalMaster() {
        return isAppleDigitalMaster;
    }

    public void setAppleDigitalMaster(boolean appleDigitalMaster) {
        isAppleDigitalMaster = appleDigitalMaster;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Preview> getPreviews() {
        return previews;
    }

    public void setPreviews(List<Preview> previews) {
        this.previews = previews;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    /**
     * Creates a deep clone of this SongAttributes object.
     *
     * @return A new SongAttributes object that is a deep copy of this instance.
     * @author: Oscar Wei
     */
    @Override
    public SongAttributes clone() {
        try {
            SongAttributes cloned = (SongAttributes) super.clone();

            if (this.genreNames != null) {
                cloned.genreNames = new ArrayList<>(this.genreNames);
            }
            if (this.artwork != null) {
                cloned.artwork = this.artwork.clone();
            }
            if (this.previews != null) {
                cloned.previews = new ArrayList<>();
                for (Preview preview : this.previews) {
                    cloned.previews.add(preview.clone());
                }
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Song Attribute {" +
                "albumName='" + albumName + '\'' +
                ", genreNames=" + genreNames +
                ", artistName='" + artistName + '\'' +
                ", name='" + name + '\'' +
                ", trackNumber=" + trackNumber +
                ", durationInMillis=" + durationInMillis +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}