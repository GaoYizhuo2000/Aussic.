package au.edu.anu.Aussic.models.entity;

public class Artwork implements Cloneable{
    private int width;
    private int height;
    private String url;
    private String bgColor;
    private String textColor1;
    private String textColor2;
    private String textColor3;
    private String textColor4;
    public String getUrlToImage() {
        return this.url;
    }

    @Override
    public Artwork clone() {
        try {
            return (Artwork) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getTextColor1() {
        return textColor1;
    }

    public void setTextColor1(String textColor1) {
        this.textColor1 = textColor1;
    }

    public String getTextColor2() {
        return textColor2;
    }

    public void setTextColor2(String textColor2) {
        this.textColor2 = textColor2;
    }

    public String getTextColor3() {
        return textColor3;
    }

    public void setTextColor3(String textColor3) {
        this.textColor3 = textColor3;
    }

    public String getTextColor4() {
        return textColor4;
    }

    public void setTextColor4(String textColor4) {
        this.textColor4 = textColor4;
    }
}
