package au.edu.anu.Aussic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.models.avl.AVLTree;
import au.edu.anu.Aussic.models.avl.EmptyTree;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.SongAttributes;

public class AVLTreeTest {

    private AVLTree<List<Song>> avlTree;

    @Before
    public void setUp() {
        avlTree = new AVLTree<>("0", new ArrayList<>());
    }
    @Test(timeout = 1000)
    public void insertByNametest() {
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
        assertEquals("BBB", testTree.key);
    }
    @Test(timeout = 1000)
    public void insertByGenretest() {
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        List<String> genres1 = new ArrayList<>();
        genres1.add("Rock");
        genres1.add("R&B");
        genres1.add("Pop");
        song1attr.setGenreNames(genres1);
        song1.setAttributes(song1attr);
        List<Song> l = new ArrayList<>();
        l.add(song1);
        AVLTree<List<Song>> testTree = new AVLTree<>("Rock", l);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2") ;
        List<String> genres2 = new ArrayList<>();
        genres2.add("Rock");
        genres2.add("R&B");
        song2attr.setGenreNames(genres2);
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("BBB", "artist2") ;
        List<String> genres3 = new ArrayList<>();
        genres3.add("R&B");
        genres3.add("Blue");
        genres3.add("fff");
        song3attr.setGenreNames(genres3);
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
    }
    @Test(timeout = 1000)
    public void insertByArtistNametest() {
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2") ;
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("BBB", "artist2") ;
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("CCC", "artist3") ;
        song4.setAttributes(song4attr);

        avlTree = avlTree.insertByName(song1);
        avlTree = avlTree.insertByName(song2);
        avlTree = avlTree.insertByName(song3);
        avlTree = avlTree.insertByName(song4);
        assertEquals("AAA", avlTree.key);
    }

    @Test(timeout = 1000)
    public void immutableTest() {
        Song song = new Song("2");
        avlTree.insertById(song);
        assertNotEquals("2", avlTree.rightNode.key);
    }

    @Test(timeout = 1000)
    public void testLeftRotate() {
        Song song1 = new Song("2");
        Song song2 = new Song("3");

        avlTree = avlTree.insertById(song1);
        assertEquals("2", avlTree.rightNode.key);
        avlTree = avlTree.insertById(song2);
        assertEquals("2", avlTree.key);
        assertEquals("1", avlTree.leftNode.key);
        assertEquals("3",avlTree.rightNode.key);
    }

    @Test(timeout = 1000)
    public void testRightRotate() {
        Song song1 = new Song("0");
        avlTree = avlTree.insertById(song1);
        Song song2 = new Song("-1");
        avlTree = avlTree.insertById(song2);

        assertEquals("0", avlTree.key);
        assertEquals("-1", avlTree.leftNode.key);
        assertEquals("1", avlTree.rightNode.key);
    }


    @Test(timeout = 1000)
    public void insertDuplicateTest() { //Duplicates are not allowed.
        avlTree = avlTree.insertById(new Song("1"));
        assertEquals("1", avlTree.key);
        assertNotEquals("1", avlTree.leftNode.key);
        assertNotEquals("1", avlTree.rightNode.key);
    }


    @Test(timeout = 1000)
    public void balanceFactorTest() {
        assertEquals(0, avlTree.getBalanceFactor());
        avlTree = avlTree.insertById(new Song("2"));
        assertEquals(-1, avlTree.getBalanceFactor());
        avlTree = avlTree.insertById(new Song("0"));
        assertEquals(0, avlTree.getBalanceFactor());
    }

    @Test(timeout = 1000)
    public void SearchTest() {
        
    }


    @Test(timeout = 1000)
    public void advancedRotationsTest() {
        Song song1 = new Song("3");
        Song song2 = new Song("2");
        Song song3 = new Song("1");
        avlTree = avlTree.insertById(song1);
        avlTree = avlTree.insertById(song2);
        avlTree = avlTree.insertById(song3);
        assertEquals("2", avlTree.key);
        assertEquals("1", avlTree.leftNode.key);
        assertEquals("3", avlTree.rightNode.key);
    }

    //Test for deletion
    @Test(timeout = 1000)
    public void noChildDeletionTest() {
        Song song = new Song("2");
        avlTree = avlTree.insertById(song);
        //avlTree = avlTree.delete(song);
    }


    @Test(timeout = 1000)
    public void oneChildDeletionTest1() {


    }

    @Test(timeout = 1000)
    public void oneChildDeletionTest2() {


    }


    @Test(timeout = 1000)
    public void oneChildDeletionTest3() {


    }

    @Test(timeout = 1000)
    public void oneChildDeletionTest4() {

    }

    @Test(timeout = 1000)
    public void twoChildDeletionTest1() {

    }

    @Test(timeout = 1000)
    public void twoChildDeletionTest2() {

    }

    @Test(timeout = 1000)
    public void advancedDeletionTest1() {



    }


    @Test(timeout = 1000)
    public void advancedDeletionTest2() {


    }

    @Test(timeout = 1000)
    public void advancedDeletionTest3() {

    }

    @Test(timeout = 1000)
    public void advancedDeletionTest4() { //From the lecture slides



    }
}
