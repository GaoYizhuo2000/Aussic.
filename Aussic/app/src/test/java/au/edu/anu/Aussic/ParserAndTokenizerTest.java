package au.edu.anu.Aussic;

import org.junit.Test;
import au.edu.anu.Aussic.models.parserAndTokenizer.Token;
import au.edu.anu.Aussic.models.parserAndTokenizer.Tokenizer;
import au.edu.anu.Aussic.models.parserAndTokenizer.Parser;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class ParserAndTokenizerTest {

    @Test
    public void testTokenCreation() {
        Token token = new Token("\\a",Token.Type.ARTISTNAME);
        assertEquals("\\a", token.getToken());
        assertEquals(Token.Type.ARTISTNAME, token.getType());
    }

    @Test
    public void testTokenization() {
        Tokenizer tokenizer = new Tokenizer("\\a Tina Arena; \\n I Want to Know What Love Is (Single Edit)");

        assertTrue(tokenizer.hasNext());
        assertEquals(Token.Type.ARTISTNAME, tokenizer.current().getType());
        tokenizer.next();

        assertTrue(tokenizer.hasNext());
        assertEquals("Tina Arena",tokenizer.current().getToken());
        tokenizer.next();

        assertTrue(tokenizer.hasNext());
        assertEquals(Token.Type.SEMICOLON, tokenizer.current().getType());
        tokenizer.next();

        assertTrue(tokenizer.hasNext());
        assertEquals(Token.Type.NAME, tokenizer.current().getType());
        tokenizer.next();

        assertTrue(tokenizer.hasNext());
        assertEquals("I Want to Know What Love Is (Single Edit)",tokenizer.current().getToken());
        tokenizer.next();

        assertFalse(tokenizer.hasNext());
    }

    @Test
    public void testSimpleParsing() {
        Parser parser = new Parser(new Tokenizer("\\a Tina Arena; \\n I Want to Know What Love Is (Single Edit)"));
        Map<String, String> result = parser.Parse();
        assertEquals("Tina Arena", result.get("artistName"));
        assertEquals("I Want to Know What Love Is (Single Edit)", result.get("name"));
    }

    @Test
    public void testAdvancedParsing() {
        Parser parser = new Parser(new Tokenizer("\\n Walk the Line; \\a Iggy Azalea; \\r 2014-01-01; \\g [\"Hip-Hop/Rap\", \"Music\",  \"Alternative Rap\",  \"Rap\", \"Underground Rap\"]"));
        Map<String, String> result = parser.Parse();
        assertEquals("Iggy Azalea", result.get("artistName"));
        assertEquals("Walk the Line", result.get("name"));
        assertEquals("2014-01-01", result.get("releaseDate"));
        assertEquals("[\"Hip-Hop/Rap\", \"Music\",  \"Alternative Rap\",  \"Rap\", \"Underground Rap\"]", result.get("genre"));
    }

}




//            "genreNames": [
//            "Hip-Hop/Rap",
//            "Music",
//            "Alternative Rap",
//            "Rap",
//            "Underground Rap"
//            ],
//            "releaseDate": "2014-01-01",
//            "name": "Walk the Line",
//            "artistName": "Iggy Azalea"

