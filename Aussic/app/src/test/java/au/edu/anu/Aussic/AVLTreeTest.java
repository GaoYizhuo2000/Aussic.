package au.edu.anu.Aussic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.models.avl.AVLTree;
import au.edu.anu.Aussic.models.avl.EmptyTree;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.SongAttributes;

public class AVLTreeTest {

    private AVLTree<List<Song>> avlTree;

//    @Before
//    public void setUp() {
//        avlTree = new AVLTree<>("0", new ArrayList<>());
//    }

    @Test(timeout = 1000)
    public void insertByNameTest() {
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        song1.setAttributes(song1attr);
        List<Song> l = new ArrayList<>();
        //l.add(song1);

        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getSongName(), l);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2") ;
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("BBB", "artist2") ;
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("CCC", "artist3") ;
        song4.setAttributes(song4attr);

        testTree = testTree.insertByName(song1);
        testTree = testTree.insertByName(song2);
        testTree = testTree.insertByName(song3);
        testTree = testTree.insertByName(song4);
        assertEquals("BBB", testTree.key);
        List<Song> expectedSongsCalledBBB = new ArrayList<>();
        expectedSongsCalledBBB.add(song3);
        assertEquals(expectedSongsCalledBBB, testTree.value);
        assertEquals("AAA", testTree.leftNode.key);
        List<Song> expectedSongsCalledAAA = new ArrayList<>();
        expectedSongsCalledAAA.add(song1);
        expectedSongsCalledAAA.add(song2);
        List<Song> actualSongs = testTree.leftNode.value;
        assertEquals(expectedSongsCalledAAA, actualSongs);
        assertEquals("CCC", testTree.rightNode.key);
        List<Song> expectedSongsCalledCCC = new ArrayList<>();
        expectedSongsCalledCCC.add(song4);
        assertEquals(expectedSongsCalledCCC, testTree.rightNode.value);

    }
    @Test(timeout = 1000)
    public void insertByGenreTest() {
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        List<String> genres1 = new ArrayList<>();
        genres1.add("Rock");
        genres1.add("R&B");
        genres1.add("Pop");
        song1attr.setGenreNames(genres1); //Song1 has 3 genres, Rock, R&B, Pop.
        song1.setAttributes(song1attr);
        List<Song> l = new ArrayList<>();
        //l.add(song1);
        AVLTree<List<Song>> testTree = new AVLTree<>("Rock", l); //Root Node.

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2") ;
        List<String> genres2 = new ArrayList<>();
        genres2.add("Rock");
        genres2.add("R&B");
        song2attr.setGenreNames(genres2); //Song2 has 2 genres, Rock, R&B.
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("BBB", "artist2") ;
        List<String> genres3 = new ArrayList<>();
        genres3.add("R&B");
        genres3.add("Blue");
        genres3.add("fff");
        song3attr.setGenreNames(genres3);//Song3 has 3 genres, R&B, Blue, fff.
        song3.setAttributes(song3attr);
        for(String genre: song1.getGenre()){
            testTree = testTree.insertByGenre(genre, song1);
        }
        for(String genre: song2.getGenre()){
            testTree =testTree.insertByGenre(genre, song2);
        }
        for(String genre: song3.getGenre()){
            testTree =testTree.insertByGenre(genre, song3);
        }

        assertEquals("R&B", testTree.key);
        List<Song> expectedSongsRB = new ArrayList<>();
        expectedSongsRB.add(song1);
        expectedSongsRB.add(song2);
        expectedSongsRB.add(song3);
        List<Song> expectedSongsPop = new ArrayList<>();
        expectedSongsPop.add(song1);
        List<Song> expectedSongsRock = new ArrayList<>();
        expectedSongsRock.add(song1);
        expectedSongsRock.add(song2);
        List<Song> expectedSongsBlue = new ArrayList<>();
        expectedSongsBlue.add(song3);
        List<Song> expectedSongsFff = new ArrayList<>();
        expectedSongsFff.add(song3);

        assertEquals(expectedSongsRB, testTree.value);
        assertEquals("Pop", testTree.leftNode.key);
        assertEquals(expectedSongsPop, testTree.leftNode.value);
        assertEquals("Rock", testTree.rightNode.key);
        assertEquals(expectedSongsRock, testTree.rightNode.value);
        assertEquals("Blue", testTree.leftNode.leftNode.key);
        assertEquals(expectedSongsBlue, testTree.leftNode.leftNode.value);
        assertEquals("fff", testTree.rightNode.rightNode.key);
        assertEquals(expectedSongsFff, testTree.rightNode.rightNode.value);
    }

    @Test(timeout = 1000)
    public void insertByArtistNameTest() {
        //avlTree = new AVLTree<>("0", new ArrayList<>());
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        song1.setAttributes(song1attr);
        List<Song> listForEachArtist = new ArrayList<>();
        AVLTree<List<Song>> avlTree = new AVLTree<>(song1.getArtistName(), listForEachArtist);

        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2") ;
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("BBB", "artist1") ;
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("CCC", "artist3") ;
        song4.setAttributes(song4attr);

        avlTree = avlTree.insertByArtistName(song1);
        avlTree = avlTree.insertByArtistName(song2);
        avlTree = avlTree.insertByArtistName(song3);
        avlTree = avlTree.insertByArtistName(song4);

        List<Song> expectedArtist1sSongs = new ArrayList<>();
        expectedArtist1sSongs.add(song1);
        expectedArtist1sSongs.add(song3);
        List<Song> expectedArtist2sSongs = new ArrayList<>();
        expectedArtist2sSongs.add(song2);
        List<Song> expectedArtist3sSongs = new ArrayList<>();
        expectedArtist3sSongs.add(song4);

        assertEquals("artist2", avlTree.key);
        assertEquals(expectedArtist2sSongs, avlTree.value);
        assertEquals("artist1", avlTree.leftNode.key);
        assertEquals(expectedArtist1sSongs, avlTree.leftNode.value);
        assertEquals("artist3", avlTree.rightNode.key);
        assertEquals(expectedArtist3sSongs, avlTree.rightNode.value);

        //assertEquals("", avlTree.toString());

    }

    @Test(timeout = 1000)
    public void insertByIdTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("BBB", "artist2") ;
        song2.setAttributes(song2attr);

        avlTree = new AVLTree<>("0", new ArrayList<>());
        avlTree = avlTree.insertById(song1);
        avlTree = avlTree.insertById(song2);
        assertEquals("1", avlTree.key);
        assertEquals("2", avlTree.rightNode.key);
    }

    @Test(timeout = 1000)
    public void insertByReleaseDateTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        song1attr.setReleaseDate("2023-10-14");
        song1.setAttributes(song1attr);
        List<Song> songsReleasedSameDate = new ArrayList<>();
        //songsReleasedSameDate.add(song1);
        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getReleaseDate(), songsReleasedSameDate);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("BBB", "artist2");
        song2attr.setReleaseDate("2023-10-14");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("CCC", "artist3");
        song3attr.setReleaseDate("2023-10-13");
        song3.setAttributes(song3attr);

        testTree = testTree.insertByReleaseDate(song1);
        testTree = testTree.insertByReleaseDate(song2);
        testTree = testTree.insertByReleaseDate(song3);

        List<Song> expectedSongs14 = new ArrayList<>();
        expectedSongs14.add(song1);
        expectedSongs14.add(song2);

        List<Song> expectedSongs13 = new ArrayList<>();
        expectedSongs13.add(song3);

        assertEquals("2023-10-14", testTree.key);
        assertEquals(expectedSongs14, testTree.value);
        assertEquals("2023-10-13", testTree.leftNode.key);
        assertEquals(expectedSongs13,testTree.leftNode.value);

    }

    @Test(timeout = 1000)
    public void immutableTest() {
        avlTree = new AVLTree<>("0", new ArrayList<>());
        Song song = new Song("2");
        avlTree.insertById(song);
        assertNotEquals("2", avlTree.rightNode.key);
    }

    @Test(timeout = 1000)
    public void LeftRotateTest() {
        avlTree = new AVLTree<>("0", new ArrayList<>());
        Song song1 = new Song("2");
        Song song2 = new Song("3");

        avlTree = avlTree.insertById(song1);
        assertEquals("2", avlTree.rightNode.key);
        avlTree = avlTree.insertById(song2);
        assertEquals("2", avlTree.key);
        assertEquals("0", avlTree.leftNode.key);
        assertEquals("3",avlTree.rightNode.key);
    }

    @Test(timeout = 1000)
    public void RightRotateTest() {
        avlTree = new AVLTree<>("0", new ArrayList<>());
        Song song1 = new Song("-1");
        avlTree = avlTree.insertById(song1);
        Song song2 = new Song("-2");
        avlTree = avlTree.insertById(song2);

        assertEquals("-2", avlTree.key);
        assertEquals("-1", avlTree.leftNode.key);
        assertEquals("0", avlTree.rightNode.key);
    }


    @Test(timeout = 1000)
    public void insertDuplicateTest() { //Duplicates are not allowed.
        avlTree = new AVLTree<>("0", new ArrayList<>());
        avlTree = avlTree.insertById(new Song("0"));
        assertEquals("0", avlTree.key);
        assertNotEquals("0", avlTree.leftNode.key);
        assertNotEquals("0", avlTree.rightNode.key);
    }


    @Test(timeout = 1000)
    public void balanceFactorTest() {
        avlTree = new AVLTree<>("0", new ArrayList<>());
        assertEquals(0, avlTree.getBalanceFactor());
        avlTree = avlTree.insertById(new Song("2"));
        assertEquals(-1, avlTree.getBalanceFactor());
        avlTree = avlTree.insertById(new Song("1"));
        assertEquals(0, avlTree.getBalanceFactor());
    }

    @Test(timeout = 1000)
    public void SearchTest() {
        
    }


    @Test(timeout = 1000)
    public void advancedRotationsTest() {
        avlTree = new AVLTree<>("0", new ArrayList<>());
        Song song1 = new Song("1");
        Song song2 = new Song("5");
        Song song3 = new Song("8");
        Song song4 = new Song("2");
        Song song5 = new Song("3");
        Song song6 = new Song("7");
        Song song7 = new Song("6");
        Song song8 = new Song("9");

        avlTree = avlTree.insertById(song1);
        avlTree = avlTree.insertById(song2);
        avlTree = avlTree.insertById(song3);
        avlTree = avlTree.insertById(song4);
        avlTree = avlTree.insertById(song5);
        avlTree = avlTree.insertById(song6);
        avlTree = avlTree.insertById(song7);
        avlTree = avlTree.insertById(song8);

        assertEquals("2", avlTree.key);
        assertEquals("1", avlTree.leftNode.key);
        assertEquals("7", avlTree.rightNode.key);
        assertEquals("0", avlTree.leftNode.leftNode.key);
        assertEquals("5", avlTree.rightNode.leftNode.key);
        assertEquals("8", avlTree.rightNode.rightNode.key);
        assertEquals("3", avlTree.rightNode.leftNode.leftNode.key);
        assertEquals("6", avlTree.rightNode.leftNode.rightNode.key);
        assertEquals("9", avlTree.rightNode.rightNode.rightNode.key);
    }

    //Test for deletion
    //use deleteById to test all possible situation
    @Test(timeout = 1000)
    public void noChildDeletionTest1() {
        Song song1 = new Song("1");
        Song song2 = new Song("2");

        avlTree = new AVLTree<>("0", new ArrayList<>());
        avlTree = avlTree.insertById(song1);
        avlTree = avlTree.insertById(song2);

        avlTree = avlTree.deleteById(song2);
        assertEquals("1", avlTree.key);
        assertEquals("0", avlTree.leftNode.key);
        assertNull(avlTree.rightNode.key);
    }

    @Test(timeout = 1000)
    public void noChildDeletionTest2() {
        Song song1 = new Song("1");
        Song song2 = new Song("3");

        avlTree = new AVLTree<>("2", new ArrayList<>());
        avlTree = avlTree.insertById(song1);
        avlTree = avlTree.insertById(song2);

        avlTree = avlTree.deleteById(song2);
        assertEquals("2", avlTree.key);
        assertEquals("1", avlTree.leftNode.key);
        assertNull(avlTree.rightNode.key);
    }



    @Test(timeout = 1000)
    public void oneChildDeletionTest() {
        Song song1 = new Song("1");
        Song song2 = new Song("2");
        Song song3 = new Song("3");

        avlTree = new AVLTree<>("0", new ArrayList<>());
        avlTree = avlTree.insertById(song1);
        avlTree = avlTree.insertById(song2);
        avlTree = avlTree.insertById(song3);

        avlTree = avlTree.deleteById(song2);

        assertEquals("1", avlTree.key);
        assertEquals("0", avlTree.leftNode.key);
        assertEquals("3", avlTree.rightNode.key);
    }

    @Test(timeout = 1000)
    public void twoChildDeletionTest() {
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        song1.setAttributes(song1attr);
        List<Song> l = new ArrayList<>();
        l.add(song1);
        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getId(), l);
        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2") ;
        song2.setAttributes(song2attr);
        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("CCC", "artist2") ;
        song3.setAttributes(song3attr);
        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("DDD", "artist3") ;
        song4.setAttributes(song4attr);
        testTree = testTree.insertById(song1);
        testTree = testTree.insertById(song2);
        testTree = testTree.insertById(song3);
        testTree = testTree.insertById(song4);


        testTree = testTree.deleteById(song2);


        //assertEquals("", testTree.toString());
        assertEquals("3", testTree.key);
        assertEquals("1", testTree.leftNode.key);
        assertEquals("4", testTree.rightNode.key);

        testTree = testTree.deleteById(song4);
        assertEquals("3", testTree.key);
        assertEquals("1", testTree.leftNode.key);
        assertNull(testTree.rightNode.key);
    }




    @Test(timeout = 1000)
    public void advancedDeletionTest() {

    }


    @Test(timeout = 1000)
    public void deleteByNameTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        song1.setAttributes(song1attr);
        List<Song> l = new ArrayList<>();
        l.add(song1);

        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getSongName(), l);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2") ;
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("BBB", "artist2") ;
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("CCC", "artist3") ;
        song4.setAttributes(song4attr);

        testTree = testTree.insertByName(song1);
        testTree = testTree.insertByName(song2);
        testTree = testTree.insertByName(song3);
        testTree = testTree.insertByName(song4);

        //Todo: test the deleteByName method.
        //testTree = testTree.deleteByName(song4);
        testTree = testTree.deleteByName(song1);
        assertEquals("", testTree.toString());

    }

    @Test(timeout = 1000)
    public void deleteByArtistNameTest() {
        // Initialize songs and their attributes
        Song song1 = new Song("SongA");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1.setAttributes(song1attr);

        Song song2 = new Song("SongB");
        SongAttributes song2attr = new SongAttributes("BBB", "artist1");
        song2.setAttributes(song2attr);

        Song song3 = new Song("SongC");
        SongAttributes song3attr = new SongAttributes("CCC", "artist2");
        song3.setAttributes(song3attr);

        Song song4 = new Song("SongD");
        SongAttributes song4attr = new SongAttributes("DDD", "artist3");
        song4.setAttributes(song4attr);

        // Insert songs into the AVL tree by artist name
        List<Song> l = new ArrayList<>();
        l.add(song1);

        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getAttributes().getArtistName(), l);
        testTree = testTree.insertByArtistName(song2);
        testTree = testTree.insertByArtistName(song3);
        testTree = testTree.insertByArtistName(song4);

        // Delete a song by artist name
        testTree = testTree.deleteByArtistName(song1);
        //testTree = testTree.deleteByArtistName(song2);

        assertEquals("", testTree.toString());
    }



    @Test(timeout = 1000)
    public void deleteByGenreTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        List<String> genres1 = new ArrayList<>();
        genres1.add("Rock");
        genres1.add("R&B");
        genres1.add("Pop");
        song1attr.setGenreNames(genres1); //Song1 has 3 genres, Rock, R&B, Pop.
        song1.setAttributes(song1attr);
        List<Song> l = new ArrayList<>();
        //l.add(song1);
        AVLTree<List<Song>> testTree = new AVLTree<>("Rock", l); //Root Node.

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2") ;
        List<String> genres2 = new ArrayList<>();
        genres2.add("Rock");
        genres2.add("R&B");
        song2attr.setGenreNames(genres2); //Song2 has 2 genres, Rock, R&B.
        song2.setAttributes(song2attr);
        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("BBB", "artist2") ;
        List<String> genres3 = new ArrayList<>();
        genres3.add("R&B");
        genres3.add("Blue");
        genres3.add("fff");
        song3attr.setGenreNames(genres3);//Song3 has 3 genres, R&B, Blue, fff.
        song3.setAttributes(song3attr);
        for(String genre: song1.getGenre()){
            testTree = testTree.insertByGenre(genre, song1);
        }
        for(String genre: song2.getGenre()){
            testTree =testTree.insertByGenre(genre, song2);
        }
        for(String genre: song3.getGenre()){
            testTree =testTree.insertByGenre(genre, song3);
        }

        testTree = testTree.deleteByGenre("Rock", song1);
        testTree = testTree.deleteByGenre("R&B", song3);

    }

    @Test(timeout = 1000)
    public void deleteByReleaseDateTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        song1attr.setReleaseDate("2023-10-14");
        song1.setAttributes(song1attr);
        List<Song> songsReleasedSameDate = new ArrayList<>();
        //songsReleasedSameDate.add(song1);
        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getReleaseDate(), songsReleasedSameDate);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("BBB", "artist2");
        song2attr.setReleaseDate("2023-10-14");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("CCC", "artist3");
        song3attr.setReleaseDate("2023-10-13");
        song3.setAttributes(song3attr);

        testTree = testTree.insertByReleaseDate(song1);
        testTree = testTree.insertByReleaseDate(song2);
        testTree = testTree.insertByReleaseDate(song3);

        testTree = testTree.deleteByReleaseDate(song3);

    }
}
