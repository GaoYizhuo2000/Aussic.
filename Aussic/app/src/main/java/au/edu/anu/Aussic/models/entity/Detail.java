package au.edu.anu.Aussic.models.entity;

public class Detail {
    private String content;
    private String uid;
    public Detail(String content, String uid){
        this.content = content;
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public String getUid() {
        return uid;
    }
}
