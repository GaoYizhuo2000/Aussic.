package au.edu.anu.Aussic.models.parserAndTokenizer;

public class FieldToken extends Token {
    private String field;
    private String value;

    public FieldToken(String field, String value) {
        super("Field", value);
        this.field = field;
        this.value = value;
    }

    // ... getters and other methods
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FieldToken{" +
                "field='" + field + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
