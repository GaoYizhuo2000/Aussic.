package au.edu.anu.Aussic.models.entity;

import java.util.List;

public class Artist {
    String type;
    String artistName;
    List<String> songs;
    String url; //image url
    public Artist(String artistName, String url){
        this.type = "artists";
        this.artistName = artistName;
        this.url= url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }

    public String getImageUrl() {
        return url;
    }

    public void setImageUrl(String url) {
        this.url = url;
    }
}
