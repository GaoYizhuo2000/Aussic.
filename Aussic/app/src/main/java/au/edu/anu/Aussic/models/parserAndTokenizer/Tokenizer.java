package au.edu.anu.Aussic.models.parserAndTokenizer;

/**
 * @author: u7581818, Oscar Wei
 */
public class Tokenizer {

    private String buffer;
    private Token currentToken;

    public Tokenizer(String text) {
        buffer = text;          // save input text (string)
        next();                 // extracts the first token.
    }

    public void next() {
        buffer = buffer.trim();

        if (buffer.isEmpty()) {
            currentToken = null;
            return;
        }

        /*
        To help you, we have already written the first few steps in the tokenization process.
        The rest will follow a similar format.
         */
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

