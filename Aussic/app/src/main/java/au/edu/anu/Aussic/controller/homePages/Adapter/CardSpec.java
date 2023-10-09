package au.edu.anu.Aussic.controller.homePages.Adapter;

public class CardSpec {
    private String description;
    private String imageUrl;

    public CardSpec(String description, String imageUrl) {
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
