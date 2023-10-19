package au.edu.anu.Aussic.models.parserAndTokenizer;

/**
 * @author: u7581818, Oscar Wei
 */

public class Token {
    public enum Type {STRING, ARTISTNAME, SONGNAME, GENRE, RELEASEDATE, SEMICOLON, USER} // \a, \s, \g, \r
    private final String token; // Token representation in String form.
    private final Type type;    // Type of the token.
    public Token(String token, Type type) {
        this.token = token;
        this.type = type;
    }
    public String getToken() {
        return token;
    }

    public Type getType() {
        return type;
    }
}
