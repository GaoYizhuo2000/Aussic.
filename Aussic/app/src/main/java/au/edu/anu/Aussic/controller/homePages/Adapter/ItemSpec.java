package au.edu.anu.Aussic.controller.homePages.Adapter;

public class ItemSpec {
    private String name;
    private String artistName;
    private String imageUrl;

    public ItemSpec(String name, String imageUrl, String artistName) {
        this.name = name;
        this.artistName = artistName;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getArtistName() {return artistName; }
}
