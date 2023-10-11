package au.edu.anu.Aussic.models.parserAndTokenizer;



import java.util.ArrayList;
        import java.util.List;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

public class Tokenizer {


    String regex = "(?<type>ID|TYPE|KIND|ARTIST_NAME|ALBUM_NAME|TRACK_NUMBER|DISC_NUMBER|SONG_NAME|GENRE|RELEASE_DATE|COMPOSER_NAME):(?<value>.+?)(?= (ID|TYPE|KIND|ARTIST_NAME|ALBUM_NAME|TRACK_NUMBER|DISC_NUMBER|SONG_NAME|GENRE|RELEASE_DATE|COMPOSER_NAME):|$)";
    Pattern pattern = Pattern.compile(regex);

    //String input;

    public Tokenizer() {
//        this.input = input;
    }


    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);
        System.out.println("Input: " + input);


        while (matcher.find()) {
            String type = matcher.group("type");
            String value = matcher.group("value");

            Token token = new Token(type, value);
            tokens.add(token);

            //ValueToken valueToken = new ValueToken(value);

            //System.out.println("Type: " + type + ", Value: " + value);
        }

//        for (Token token : tokens) {
////            System.out.println("+++++++++++");
//            System.out.println(token);
//        }
        return tokens;
    }
}

