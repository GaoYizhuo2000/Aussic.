package au.edu.anu.Aussic.models.parserAndTokenizer;

public class ValueToken extends Token {
    private String value;

    public ValueToken(String value) {
        super("Value", value);
        this.value = value;
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
        return "ValueToken{" +
                "value='" + value + '\'' +
                '}';
    }
}
