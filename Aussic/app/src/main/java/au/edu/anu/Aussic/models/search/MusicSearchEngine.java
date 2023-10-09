package au.edu.anu.Aussic.models.search;

import au.edu.anu.Aussic.models.avl.AVLTree;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.parserAndTokenizer.ConditionNode;
import au.edu.anu.Aussic.models.parserAndTokenizer.Parser;
import au.edu.anu.Aussic.models.parserAndTokenizer.QueryNode;
import au.edu.anu.Aussic.models.parserAndTokenizer.Token;
import au.edu.anu.Aussic.models.parserAndTokenizer.Tokenizer;


import java.util.ArrayList;
import java.util.List;



public class MusicSearchEngine {
    //private AVLTree musicDataByID;
    private List<Song> songs;

    public MusicSearchEngine(AVLTree<Song> musicDataByID) {
        this.songs = musicDataByID.inorderTraversal();
    }

    public QueryNode parseQuery(String userInput) {
        Tokenizer tokenizer = new Tokenizer();
        List<Token> tokens = tokenizer.tokenize(userInput);
        Parser parser = new Parser(tokens);
        return parser.parse();
    }

    public List<Song> search(QueryNode query) {
        List<Song> result = new ArrayList<>();
        for (ConditionNode condition : query.conditions) {
            String type = condition.type;
            String value = condition.value;
            for (Song song : songs) {
                switch (type) {
                    case "ID":
                        if (String.valueOf(song.getId()).equals(value)) {
                            result.add(song);
                        }
                        //break;
                    case "TYPE":
                        if (song.getType().equals(value)) {
                            result.add(song);
                        }
                        //break;
//                    case "KIND":
//                        if (song.getKind().equals(value)) {
//                            result.add(song);
//                        }
                        //break;
                    case "ARTIST_NAME":
                        if (song.getArtistName().equals(value)) {
                            result.add(song);
                        }
                        //break;
                    case "ALBUM_NAME":
                        if (song.getAlbumName().equals(value)) {
                            result.add(song);
                        }
                        //break;
                    case "TRACK_NUMBER":
                        if (String.valueOf(song.getTrackNumber()).equals(value)) {
                            result.add(song);
                        }
                        //break;
                    case "DISC_NUMBER":
                        if (String.valueOf(song.getDiscNumber()).equals(value)) {
                            result.add(song);
                        }
                        //break;
                    case "SONG_NAME":
                        if (song.getSongName().equals(value)) {
                            result.add(song);
                        }
                        //break;
                    case "GENRE":
                        if (song.getGenre().equals(value)) {
                            result.add(song);
                        }
                        //break;
//                    case "RELEASE_DATE":
//                        if (song.getReleaseDate().equals(value)) {
//                            result.add(song);
//                        }
                        //break;
//                    case "COMPOSER_NAME":
//                        if (song.getComposerName().equals(value)) {
//                            result.add(song);
//                        }
                        //break;
                    default:
                        // Optionally handle unrecognized fields
                        break;
                }

            }
        }
        return result;
    }

    public void displayResults(List<Song> results) {
        if (results == null) {
            System.out.println("Results is null.");
            return;
        }

        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        for (Song songs : results) {
            System.out.println(songs);
        }
    }
}
