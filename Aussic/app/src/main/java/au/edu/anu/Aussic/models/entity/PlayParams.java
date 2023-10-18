package au.edu.anu.Aussic.models.entity;

/**
 * @author: u7581818, Oscar Wei
 */

public class PlayParams {
    private String id;
    private String kind;

    public PlayParams(String id, String kind) {
        this.id = id;
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
