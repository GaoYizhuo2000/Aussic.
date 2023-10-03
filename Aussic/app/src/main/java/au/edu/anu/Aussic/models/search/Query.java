package au.edu.anu.Aussic.models.search;

public class Query {
    private String artistName;


    public Query(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
