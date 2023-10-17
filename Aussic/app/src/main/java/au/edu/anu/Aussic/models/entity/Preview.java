package au.edu.anu.Aussic.models.entity;

public class Preview implements Cloneable{
    private String url;
    public String getUrlToListen(){
        return this.url;
    }
    @Override
    public Preview clone() {
        try {
            return (Preview) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
