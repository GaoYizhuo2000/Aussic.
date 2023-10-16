package au.edu.anu.Aussic.controller.Runtime.Adapter;

import au.edu.anu.Aussic.models.entity.Artist;
import au.edu.anu.Aussic.models.entity.Genre;
import au.edu.anu.Aussic.models.entity.Song;

public class ItemSpec {
    private Song song;
    private Artist artist;
    private Genre genre;
    private String type;

    public ItemSpec(Song song) {
        this.song = song;
        this.type = "songs";
    }
    public ItemSpec(Artist artist){
        this.artist = artist;
        this.type = "artists";
    }
    public ItemSpec(Genre genre){
        this.genre = genre;
        this.type = "genres";
    }
    public String getSongName() {
        return Functions.adjustLength(song.getSongName());
    }

    public String getSongImageUrl() {
        return Functions.makeImageUrl(200, 200, song.getUrlToImage());
    }

    public String getType() {
        return type;
    }

    public String getSongArtistName() {return song.getArtistName(); }
    public String getArtistName(){ return artist.getArtistName(); }
    public String getArtistImageUrl(){ return Functions.makeImageUrl(200, 200, artist.getImageUrl()); }
    public String getGenreName(){ return genre.getGenreName(); }
    public String getGenreImageUrl(){ return Functions.makeImageUrl(200, 200, genre.getImageUrl()); }

    public Song getSong() {
        return song;
    }

    public Artist getArtist() {
        return artist;
    }

    public Genre getGenre() {
        return genre;
    }
}
