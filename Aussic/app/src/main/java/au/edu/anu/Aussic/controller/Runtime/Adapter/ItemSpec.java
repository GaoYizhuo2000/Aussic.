package au.edu.anu.Aussic.controller.Runtime.Adapter;

import au.edu.anu.Aussic.models.entity.Song;

public class ItemSpec {
    private String name;
    private String artistName;
    private String imageUrl;
    private Song song;

    public ItemSpec(String name, String imageUrl, String artistName, Song song) {
        this.name = name;
        this.artistName = artistName;
        this.imageUrl = imageUrl;
        this.song = song;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getArtistName() {return artistName; }

    public Song getSong() {return song; }
}
