package au.edu.anu.Aussic.models.entity;

import java.util.List;

public class Genre {
    String type;
    String genreName;
    List<String> songs;
    String url;

    public Genre(String genreName, String url){
        this.type = "genres";
        this.genreName = genreName;
        this.url= url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
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
}
