//package au.edu.anu.Aussic.models.ParseTest;
//
//
//
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//import java.util.List;
//
//import static junit.framework.TestCase.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import au.edu.anu.Aussic.models.avl.AVLTree;
//import au.edu.anu.Aussic.models.entity.Song;
//import au.edu.anu.Aussic.models.jsonParser.JsonSongLoader;
//import au.edu.anu.Aussic.models.search.MusicSearchEngine;
//
//
//public class SearchTest {
//
//    private MusicSearchEngine engine;
//
//    @Before
//    public void setUp() {
//        JsonSongLoader loader = new JsonSongLoader();
//        List<Song> songs = loader.loadSongsFromJson("./src/entity/songs.json");
//        AVLTree<Song> tree = null;
//
//
//        // Create an AVL tree and insert songs
//        if (songs != null) {
//            tree = new AVLTree<>(songs.get(0));
//            for (int i = 1; i < songs.size(); i++) {
//                tree = tree.insert(songs.get(i));
//            }
//        }
//
//        engine = new MusicSearchEngine(tree);
//    }
//
//    @Test
//    public void searchID() {
//        QueryNode query = engine.parseQuery("ID:1440825121");
//        List<Song> results = engine.search(query);
//        Song resultSong = results.get(0);
//        assertEquals("Walk the Line", resultSong.getSongName());
//    }
//
//    @Test
//    public void searchSongName() {
//        QueryNode query = engine.parseQuery("SONG_NAME:Rolex");
//        List<Song> results = engine.search(query);
//        assertEquals(4, results.size());// there are 4 songs called Rolex
//        Song song1 = results.get(0);
//        Song song2 = results.get(1);
//        Song song3 = results.get(2);
//        Song song4 = results.get(3);
//        assertEquals("1440826058",song1.getId());
//        assertEquals("1443725350",song2.getId());
//        assertEquals("1443825087",song3.getId());
//        assertEquals("1444002939",song4.getId());
//    }
//
//    @Test
//    public void searchMultiVar() {
//        QueryNode query = engine.parseQuery("SONG_NAME:Rolex");
//        List<Song> results = engine.search(query);
//
//    }
//}
