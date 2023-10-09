//package au.edu.anu.Aussic.models.ParseTest;
//
//import org.junit.Test;
//
//
//import java.util.List;
//
//import static junit.framework.TestCase.assertEquals;
////import static org.junit.Assert.assertTrue;
////import static org.junit.jupiter.api.Assertions.*;
//
//public class ParserAndTokenizerTest {
//
//    @Test
//    public void testTokenizer() {
//        Tokenizer tokenizer = new Tokenizer();
//        List<Token> tokens = tokenizer.tokenize("ARTIST_NAME:Beatles ALBUM_NAME:Abbey Road");
//
//        assertEquals(2, tokens.size());
//
//        //assertTrue(tokens.get(0) instanceof FieldToken);
//        assertEquals("ARTIST_NAME", (tokens.get(0)).getType());
//        assertEquals("Beatles", tokens.get(0).getValue());
//
//        //assertTrue(tokens.get(1) instanceof FieldToken);
//        assertEquals("ALBUM_NAME", (tokens.get(1)).getType());
//        assertEquals("Abbey Road", tokens.get(1).getValue());
//    }
//
//    @Test
//    public void testParser() {
//        Tokenizer tokenizer = new Tokenizer();
//        List<Token> tokens = tokenizer.tokenize("ARTIST_NAME:Beatles ALBUM_NAME:Abbey Road");
//        System.out.println("tokens:" + tokens);
//        Parser parser = new Parser(tokens);
//
//
//        QueryNode queryNode = parser.parse();
//
//        assertEquals(2, queryNode.conditions.size());
//
//        assertEquals("ARTIST_NAME", queryNode.conditions.get(0).type);
//        assertEquals("Beatles", queryNode.conditions.get(0).value);
//
//        assertEquals("ALBUM_NAME", queryNode.conditions.get(1).type);
//        assertEquals("Abbey Road", queryNode.conditions.get(1).value);
//    }
//}
