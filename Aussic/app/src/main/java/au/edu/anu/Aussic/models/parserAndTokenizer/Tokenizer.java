package au.edu.anu.Aussic.models.parserAndTokenizer;

/**
 *
 * Learn from the code of lab6
 *
 * The `Tokenizer` class is responsible for tokenizing input text into a sequence of tokens.
 * It extracts tokens based on specific patterns and returns them one by one.
 *
 * Example usage:
 * ```java
 * Tokenizer tokenizer = new Tokenizer("\\a Adele; \\s Hello, World!; \\g Pop; \\r 2023-10-13; \\u User1;");
 * while (tokenizer.hasNext()) {
 *     Token token = tokenizer.current();
 *     System.out.println("Token: " + token.getToken() + ", Type: " + token.getType());
 *     tokenizer.next();
 * }
 * ```
 *
 * Supported token types:
 * - `ARTISTNAME`: Represents an artist name token (e.g., \a token).
 * - `SONGNAME`: Represents a song name token (e.g., \s token).
 * - `GENRE`: Represents a genre token (e.g., \g token).
 * - `RELEASEDATE`: Represents a release date token (e.g., \r token).
 * - `SEMICOLON`: Represents a semicolon token.
 * - `USER`: Represents a user token (e.g., \\u token).
 * - `STRING`: Represents a generic string token.
 *
 * The `Tokenizer` class provides methods to move to the next token (`next`), retrieve the current token (`current`), and check if there are more tokens available (`hasNext`).
 *
 * @author u7581818, Oscar Wei
 */
public class Tokenizer {

    private String buffer;
    private Token currentToken;

    /**
     * Constructs a `Tokenizer` object with the specified input text.
     *
     * @param text The input text to tokenize.
     */
    public Tokenizer(String text) {
        buffer = text;          // save input text (string)
        next();                 // extracts the first token.
    }


    /**
     * Move to the next token in the input text.
     * This method advances the tokenization process.
     */
    public void next() {
        buffer = buffer.trim();

        if (buffer.isEmpty()) {
            currentToken = null;
            return;
        }

        char firstChar = buffer.charAt(0);
        char secondChar = buffer.charAt(1);
        if (firstChar == '\\' && secondChar== 'a')
            currentToken = new Token("\\a", Token.Type.ARTISTNAME);
        else if (firstChar == '\\' && secondChar== 's')
            currentToken = new Token("\\n", Token.Type.SONGNAME);
        else if (firstChar == '\\' && secondChar== 'g')
            currentToken = new Token("\\g", Token.Type.GENRE);
        else if (firstChar == '\\' && secondChar== 'r')
            currentToken = new Token("\\r", Token.Type.RELEASEDATE);
        else if (firstChar == ';')
            currentToken = new Token(";", Token.Type.SEMICOLON);
        else if (firstChar == '\\' && secondChar== 'u')
            currentToken = new Token("\\u", Token.Type.USER);
        else {
            int pos = 0;
            String value = "";
            while((pos < buffer.length())){

                if(buffer.charAt(pos) != '\\' && buffer.charAt(pos) != ';'){value += buffer.charAt(pos);
                    pos += 1;}
                else{break;}
            }
            currentToken = new Token(value, Token.Type.STRING);
        }
        int tokenLen = currentToken.getToken().length();
        buffer = buffer.substring(tokenLen);
    }
    public Token current() {
        return currentToken;
    }
    public boolean hasNext() {
        return currentToken != null;
    }

}

