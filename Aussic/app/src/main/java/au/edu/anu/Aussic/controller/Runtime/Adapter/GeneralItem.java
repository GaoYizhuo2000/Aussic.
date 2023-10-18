package au.edu.anu.Aussic.controller.Runtime.Adapter;

/**
 * @author: u7516507, Evan Cheung
 */

import au.edu.anu.Aussic.models.entity.Artist;
import au.edu.anu.Aussic.models.entity.Genre;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.User;

public class GeneralItem {
    private Song song;
    private Artist artist;
    private Genre genre;
    private User user;
    private String type;

    public GeneralItem(Song song) {
        this.song = song;
        this.type = "songs";
    }
    public GeneralItem(Artist artist){
        this.artist = artist;
        this.type = "artists";
    }
    public GeneralItem(Genre genre){
        this.genre = genre;
        this.type = "genres";
    }
    public GeneralItem(User user){
        this.user = user;
        this.type = "users";
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
    public String getUserName(){ return user.username; }
    public String getUserImageUrl() { return user.iconUrl; }

    public Song getSong() {
        return song;
    }

    public Artist getArtist() {
        return artist;
    }

    public Genre getGenre() {
        return genre;
    }

    public User getUser() { return user; }
}
