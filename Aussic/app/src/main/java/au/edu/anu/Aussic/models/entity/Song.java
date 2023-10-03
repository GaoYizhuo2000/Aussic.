package au.edu.anu.Aussic.models.entity;
import java.util.List;

public class Song implements Comparable<Song> {
    private String id;
    private String type;
    private String href;
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

//    public Song(String id, String albumName, List<String> genreNames, String artistName, String name) {
//        this.id = id;
//        this.albumName = albumName;
//        this.genreNames = genreNames;
//        this.artistName = artistName;
//        this.name = name;
//    }

    public Song(
            String id,
            String type,
            String href,
            String albumName,
            List<String> genreNames,
            int trackNumber,
            long durationInMillis,
            String releaseDate,
            String isrc,
            Artwork artwork,
            String composerName,
            String url,
            PlayParams playParams,
            int discNumber,
            boolean hasCredits,
            boolean hasLyrics,
            boolean isAppleDigitalMaster,
            String name,
            List<Preview> previews,
            String artistName
    ) {
        this.id = id;
        this.type = type;
        this.href = href;
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

    //getters and setters...
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    @Override
    public int compareTo(Song other) {
        return this.id.compareTo(other.id);  // sort by ID
    }


    public static class Artwork {
        private int width;
        private int height;
        private String url;
        private String bgColor;
        private String textColor1;
        private String textColor2;
        private String textColor3;
        private String textColor4;

        public Artwork(
                int width,
                int height,
                String url,
                String bgColor,
                String textColor1,
                String textColor2,
                String textColor3,
                String textColor4
        ) {
            this.width = width;
            this.height = height;
            this.url = url;
            this.bgColor = bgColor;
            this.textColor1 = textColor1;
            this.textColor2 = textColor2;
            this.textColor3 = textColor3;
            this.textColor4 = textColor4;
        }

        // ... getters and setters
    }

    public static class PlayParams {
        private String id;
        private String kind;

        public PlayParams(String id, String kind) {
            this.id = id;
            this.kind = kind;
        }

        // ... getters and setters
    }

    public static class Preview {
        private String url;

        public Preview(String url) {
            this.url = url;
        }

        // ... getters and setters
    }






}
