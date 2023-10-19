package au.edu.anu.Aussic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import au.edu.anu.Aussic.models.avl.AVLTree;
import au.edu.anu.Aussic.models.avl.EmptyTree;
import au.edu.anu.Aussic.models.avl.Tree;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.SongAttributes;

public class AVLTreeTest {


    private AVLTree<List<Song>> avlTree;


    /**
     * Checks balance factors of AVL nodes. Prints and asserts if node's balance is incorrect.
     *
     * @author: Oscar Wei
     */
    private void checkBalanceFactors(AVLTree<List<Song>> node) {
        if (node == null) return;
        try {
            int balance = node.getBalanceFactor();
            if (balance < -1 || balance > 1) {
                System.out.println("Balcance of current node: " + balance + "aaa" + node.key + node.value);
            }
            assertTrue(balance >= -1 && balance <= 1);
            checkBalanceFactors((AVLTree<List<Song>>) node.leftNode);
            checkBalanceFactors((AVLTree<List<Song>>) node.rightNode);
        } catch (ClassCastException e) {
            // This node is an EmptyAVL node, so do nothing and return
            return;
        }
    }

    /**
     * Check if a null song throws an appropriate exception.
     * @author: Oscar Wei
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertByNameNullInputTest() {
        AVLTree<List<Song>> testTree = new AVLTree<>(null, new ArrayList<>());
        testTree.insertByName(null);
    }

    /**
     * Validates AVL tree's song insertion by name, ensuring correct key placement
     * and list association for multiple songs.
     * @author: Oscar Wei
     */
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

    /**
     * Tests the {@code insertByName} method of {@code AVLTree} to ensure songs with identical names
     * are aggregated into a single node's list.
     *
     * @author: Oscar Wei
     */
    @Test
    public void insertByNameDuplicateNamesTest() {
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("DUPLICATE", "artist1");
        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("DUPLICATE", "artist2");
        song2.setAttributes(song2attr);

        AVLTree<List<Song>> testTree = new AVLTree<>("DUPLICATE", new ArrayList<>());
        testTree = testTree.insertByName(song1);
        testTree = testTree.insertByName(song2);

        assertEquals("DUPLICATE", testTree.key);
        List<Song> expectedSongs = Arrays.asList(song1, song2);
        assertEquals(expectedSongs, testTree.value);
    }


    /**
     * Tests advanced insertion of songs by name, ensuring AVL tree structure
     * and balance for all 26 alphabet letters.
     *
     * @author: Oscar Wei
     */
    @Test
    public void insertSongsByNameAdvanced() {
        char songName = 'A';
        AVLTree<List<Song>> testTree = new AVLTree<>(Character.toString(songName), new ArrayList<>());
        for (int i = 0; i < 26; i++) {
            Song song = new Song(Integer.toString(i));
            SongAttributes songAttr = new SongAttributes(Character.toString(songName), "artist" + i);
            song.setAttributes(songAttr);
            testTree = testTree.insertByName(song);
            songName++;
        }

        //For every node in the tree, use getBalanceFactor() to check the if the balance is 0, -1, or 1;
        checkBalanceFactors(testTree);
        //assertEquals("", testTree.toString());
        assertEquals("P", testTree.key);
        assertEquals("{key=Pvalue=[Song{id='15', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist15', name='P', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Hvalue=[Song{id='7', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist7', name='H', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Dvalue=[Song{id='3', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist3', name='D', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Bvalue=[Song{id='1', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist1', name='B', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Avalue=[Song{id='0', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist0', name='A', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}, rightNode={key=Cvalue=[Song{id='2', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist2', name='C', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}}, rightNode={key=Fvalue=[Song{id='5', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist5', name='F', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Evalue=[Song{id='4', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist4', name='E', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}, rightNode={key=Gvalue=[Song{id='6', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist6', name='G', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}}}, rightNode={key=Lvalue=[Song{id='11', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist11', name='L', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Jvalue=[Song{id='9', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist9', name='J', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Ivalue=[Song{id='8', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist8', name='I', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}, rightNode={key=Kvalue=[Song{id='10', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist10', name='K', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}}, rightNode={key=Nvalue=[Song{id='13', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist13', name='N', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Mvalue=[Song{id='12', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist12', name='M', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}, rightNode={key=Ovalue=[Song{id='14', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist14', name='O', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}}}}, rightNode={key=Tvalue=[Song{id='19', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist19', name='T', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Rvalue=[Song{id='17', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist17', name='R', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Qvalue=[Song{id='16', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist16', name='Q', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}, rightNode={key=Svalue=[Song{id='18', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist18', name='S', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}}, rightNode={key=Xvalue=[Song{id='23', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist23', name='X', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Vvalue=[Song{id='21', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist21', name='V', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={key=Uvalue=[Song{id='20', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist20', name='U', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}, rightNode={key=Wvalue=[Song{id='22', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist22', name='W', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}}, rightNode={key=Yvalue=[Song{id='24', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist24', name='Y', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={key=Zvalue=[Song{id='25', attributes='Song Attribute {albumName='null', genreNames=null, artistName='artist25', name='Z', trackNumber=0, durationInMillis=0, releaseDate='null'}}], leftNode={}, rightNode={}}}}}}", testTree.toString());
    }

    /**
     * Tests song insertion by genre, ensuring AVL tree maintains structure and songs are
     * correctly associated to genres.
     *
     * @author: Oscar Wei
     */
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

        checkBalanceFactors(testTree);
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


    /**
     * Tests the {@code insertByGenre} method of the {@code AVLTree} class for handling duplicate genres.
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void insertDuplicateGenres() {
        Song song1 = new Song("5");
        SongAttributes song1attr = new SongAttributes("DDD", "artist5");
        song1attr.setGenreNames(Arrays.asList("Country"));
        song1.setAttributes(song1attr);

        Song song2 = new Song("6");
        SongAttributes song2attr = new SongAttributes("EEE", "artist6");
        song2attr.setGenreNames(Arrays.asList("Country"));
        song2.setAttributes(song2attr);

        AVLTree<List<Song>> testTree = new AVLTree<>("Country", new ArrayList<>());
        testTree = testTree.insertByGenre("Country", song1);
        testTree = testTree.insertByGenre("Country", song2);

        assertTrue(testTree.value.contains(song1));
        assertTrue(testTree.value.contains(song2));
//        assertEquals("{}",testTree.leftNode);
//        assertEquals("{}",testTree.rightNode);
    }

    /**
     * Tests the {@code insertByGenre} method of the {@code AVLTree} class for handling songs with a large number of the same genre.
     *
     * <p>This test focuses on the scenario where a single song has a large number (1000) of the same genre ("Jazz").
     * The primary goal is to ensure that the AVLTree can:</p>
     * <ul>
     *     <li>Correctly handle and insert songs with a large number of the same genre.</li>
     *     <li>Maintain data integrity and not lose any genre entries during the insertion process.</li>
     *     <li>Consolidate the song under a single genre node, even if the genre is repeated multiple times for the song.</li>
     * </ul>
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void insertMaxGenreSize() {
        Song song = new Song("8");
        SongAttributes songAttr = new SongAttributes("GGG", "artist8");
        List<String> genres = new ArrayList<>(Collections.nCopies(1000, "Jazz"));
        songAttr.setGenreNames(genres);
        song.setAttributes(songAttr);

        AVLTree<List<Song>> testTree = new AVLTree<>("Jazz", new ArrayList<>());
        for (String genre : song.getGenre()) {
            testTree = testTree.insertByGenre(genre, song);
        }

        assertEquals("Jazz", testTree.key);
        assertTrue(testTree.value.contains(song));
    }

    /**
     *
     * Tests if inserting a null song by genre throws IllegalArgumentException within the expected timeout.
     *
     * @author: Oscar Wei
     */
    @Test(expected = IllegalArgumentException.class, timeout = 1000)
    public void insertNullSong() {
        AVLTree<List<Song>> testTree = new AVLTree<>("Rock", new ArrayList<>());
        testTree.insertByGenre("Rock", null);
    }


    /**
     * This test verifies the functionality of inserting songs into an AVL tree based on their artist names.
     * The goal is to ensure that after insertion, the AVL tree maintains its balanced structure and songs
     * are correctly grouped under the respective artist names. Through the test, we're also verifying that
     * multiple songs from the same artist are grouped together while maintaining the AVL tree properties.
     *
     * @author: Oscar Wei
     */
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
    }


    /**
     * This test validates the AVL tree's ability to group songs under the same artist name.
     * We're inserting two songs by 'artist1'. After insertion, we check if the tree's key
     * is indeed 'artist1' and if both songs are grouped under this artist name in the tree.
     *
     * @author: Oscar Wei
     */

    @Test(timeout = 1000)
    public void insertSameArtistTest() {
        Song song1 = new Song("SongA");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1.setAttributes(song1attr);

        Song song2 = new Song("SongB");
        SongAttributes song2attr = new SongAttributes("BBB", "artist1");
        song2.setAttributes(song2attr);

        List<Song> l = new ArrayList<>();
        l.add(song1);

        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getAttributes().getArtistName(), l);
        testTree = testTree.insertByArtistName(song2);

        assertEquals("artist1", testTree.key);
        assertTrue(testTree.value.contains(song1) && testTree.value.contains(song2));
    }


    /**
     * Tests AVL tree's insertion using song IDs, ensuring correct node placement by ID.
     *
     * @author: Oscar Wei
     */
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




    /**
     * Tests AVL tree's insertion using song release dates, verifying correct organization by date and song aggregation.
     *
     * @author: Oscar Wei
     */
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

    /**
     * Tests AVL tree's immutability ensuring that original nodes aren't directly modified post insertion.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void immutableTest() {
        avlTree = new AVLTree<>("0", new ArrayList<>());
        Song song = new Song("2");
        avlTree.insertById(song);
        assertNotEquals("2", avlTree.rightNode.key);
    }


    /**
     * Tests AVL tree's left rotation by inserting nodes in ascending
     * order and verifying the tree's structure post rotation.
     *
     * @author: Oscar Wei
     */
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

    /**
     * Tests AVL tree's right rotation by inserting nodes in descending order
     * and verifying the tree's structure post rotation.
     *
     * @author: Oscar Wei
     */
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


    /**
     * Verifies that AVL tree prevents duplicate insertion by ensuring that
     * left and right child nodes are distinct from the parent node.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void insertDuplicateTest() { //Duplicates are not allowed.
        avlTree = new AVLTree<>("0", new ArrayList<>());
        avlTree = avlTree.insertById(new Song("0"));
        assertEquals("0", avlTree.key);
        assertNotEquals("0", avlTree.leftNode.key);
        assertNotEquals("0", avlTree.rightNode.key);
    }



    /**
     * Tests the balance factor of AVL tree after sequential insertions to
     * ensure it maintains proper balance after each operation.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void balanceFactorTest() {
        avlTree = new AVLTree<>("0", new ArrayList<>());
        assertEquals(0, avlTree.getBalanceFactor());
        avlTree = avlTree.insertById(new Song("2"));
        assertEquals(-1, avlTree.getBalanceFactor());
        avlTree = avlTree.insertById(new Song("1"));
        assertEquals(0, avlTree.getBalanceFactor());
    }



    /**
     * Tests the advanced rotations of the AVL tree to verify proper tree restructuring
     * after multiple insertions, ensuring tree remains balanced.
     *
     * @author: Oscar Wei
     */
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


    /**
     * Tests the deletion of a node with no child in the AVL tree,
     * ensuring the tree structure remains correct after deletion.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void noChildDeletionTest1() {
        Song song1 = new Song("1");
        Song song2 = new Song("2");

        avlTree = new AVLTree<>("0", new ArrayList<>());
        avlTree = avlTree.insertById(song1);
        avlTree = avlTree.insertById(song2);

        avlTree = (AVLTree<List<Song>>) avlTree.deleteById(song2);
        assertEquals("1", avlTree.key);
        assertEquals("0", avlTree.leftNode.key);
        assertNull(avlTree.rightNode.key);
    }

    /**
     * Verify AVL tree maintains structure when deleting childless node.
     * Tests insertion, deletion, and tree balance.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void noChildDeletionTest2() {
        Song song1 = new Song("1");
        Song song2 = new Song("3");

        avlTree = new AVLTree<>("2", new ArrayList<>());
        avlTree = avlTree.insertById(song1);
        avlTree = avlTree.insertById(song2);

        avlTree = (AVLTree<List<Song>>) avlTree.deleteById(song2);
        assertEquals("2", avlTree.key);
        assertEquals("1", avlTree.leftNode.key);
        assertNull(avlTree.rightNode.key);
    }



    /**
     * Test AVL tree deletion of node with one child. Checks tree structure after deletion.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void oneChildDeletionTest() {
        Song song1 = new Song("1");
        Song song2 = new Song("2");
        Song song3 = new Song("3");

        avlTree = new AVLTree<>("0", new ArrayList<>());
        avlTree = avlTree.insertById(song1);
        avlTree = avlTree.insertById(song2);
        avlTree = avlTree.insertById(song3);

        avlTree = (AVLTree<List<Song>>) avlTree.deleteById(song2);

        assertEquals("1", avlTree.key);
        assertEquals("0", avlTree.leftNode.key);
        assertEquals("3", avlTree.rightNode.key);
    }

    /**
     * Test AVL tree deletion of node with two children. Validates tree structure post-deletion.
     *
     * @throws Exception if the test fails or times out.
     * @author: Oscar Wei
     */
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

        //assertEquals("", testTree.toString());

        testTree = (AVLTree<List<Song>>) testTree.deleteById(song2);

        checkBalanceFactors(testTree);
        assertEquals("3", testTree.key);
        assertEquals("1", testTree.leftNode.key);
        assertEquals("4", testTree.rightNode.key);

        testTree = (AVLTree<List<Song>>) testTree.deleteById(song4);
        assertEquals("3", testTree.key);
        assertEquals("1", testTree.leftNode.key);
        assertNull(testTree.rightNode.key);
    }




    /**
     * Tests AVL tree deletion by name, validating tree emptiness after multiple
     * deletions by name of nodes with associated attributes.
     *
     * @throws Exception if the test fails or times out.
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void deleteByNameTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1") ;
        song1.setAttributes(song1attr);
        List<Song> l = new ArrayList<>();
        l.add(song1);

        Tree<List<Song>> testTree = new AVLTree<>(song1.getSongName(), l);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2") ;
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("BBB", "artist2") ;
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("CCC", "artist3") ;
        song4.setAttributes(song4attr);

        testTree = testTree.insertByName(song2);
        testTree = testTree.insertByName(song3);
        testTree = testTree.insertByName(song4);

        testTree =  testTree.deleteByName(song3);
        testTree =  testTree.deleteByName(song1);
        testTree =  testTree.deleteByName(song2);
        testTree =  testTree.deleteByName(song4);

        assertNull(testTree.key);
    }

    /**
     * Tests AVL tree deletion of a leaf node by name, verifying tree structure after deletion,
     * and handles nodes with associated attributes.
     *
     * @throws Exception if the test fails or times out.
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void deleteLeafNodeByNameTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1.setAttributes(song1attr);
        List<Song> l = new ArrayList<>();
        l.add(song1);

        Tree<List<Song>> testTree = new AVLTree<>(song1.getSongName(), l);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("BBB", "artist2");
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("CCC", "artist3");
        song4.setAttributes(song4attr);

        testTree = testTree.insertByName(song2);
        testTree = testTree.insertByName(song3);
        testTree = testTree.insertByName(song4);

        testTree = testTree.deleteByName(song4);

        assertEquals("BBB", testTree.key);
        assertEquals("AAA", testTree.leftNode.key);
        assertNull(testTree.rightNode.key);
    }


    /**
     * Tests AVL tree deletion of a node with one child by name,
     * validating tree structure post-deletion, including attributes.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void deleteByNameOneChildNodeTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1.setAttributes(song1attr);
        List<Song> l = new ArrayList<>();
        l.add(song1);

        Tree<List<Song>> testTree = new AVLTree<>(song1.getSongName(), l);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("BBB", "artist2");
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("CCC", "artist3");
        song4.setAttributes(song4attr);

        Song song5 = new Song("5");
        SongAttributes song5atrr = new SongAttributes("DDD", "artist4");
        song5.setAttributes(song5atrr);

        testTree = testTree.insertByName(song2);
        testTree = testTree.insertByName(song3);
        testTree = testTree.insertByName(song4);
        testTree = testTree.insertByName(song5);


        testTree = testTree.deleteByName(song4);

        assertEquals("BBB", testTree.key);
        assertEquals("AAA", testTree.leftNode.key);
        assertEquals("DDD", testTree.rightNode.key);
    }

    /**
     * Tests AVL tree deletion of a node with two children by name, verifying tree structure
     * post-deletion, including attributes.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void deleteByNameTwoChildrenNodeTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1.setAttributes(song1attr);
        List<Song> l = new ArrayList<>();
        l.add(song1);

        Tree<List<Song>> testTree = new AVLTree<>(song1.getSongName(), l);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("AAA", "artist2");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("BBB", "artist2");
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("CCC", "artist3");
        song4.setAttributes(song4attr);

        Song song5 = new Song("5");
        SongAttributes song5atrr = new SongAttributes("DDD", "artist4");
        song5.setAttributes(song5atrr);

        testTree = testTree.insertByName(song2);
        testTree = testTree.insertByName(song3);
        testTree = testTree.insertByName(song4);
        testTree = testTree.insertByName(song5);

        testTree = testTree.deleteByName(song3);

        assertEquals("CCC", testTree.key);
        assertEquals("AAA", testTree.leftNode.key);
        assertEquals("DDD", testTree.rightNode.key);
    }


    /**
     * Tests AVL tree deletion of a song by artist name, verifying tree structure post-deletion.
     * Includes song attributes handling. Handles exceptions and timeouts.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void deleteByArtistNameTest() {
        // Initialize songs and their attributes
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("BBB", "artist1");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("CCC", "artist2");
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
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
        testTree = (AVLTree<List<Song>>) testTree.deleteByArtistName(song1);

        assertEquals("artist2", testTree.key);
        assertEquals("artist1", testTree.leftNode.key);
        assertEquals("artist3", testTree.rightNode.key);
    }

    /**
     * Tests AVL tree deletion of a song by artist name in a leaf node,
     * ensuring proper tree structure post-deletion. Includes song attributes handling.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void deleteByArtistNameLeafNodeTest() {
        // Initialize songs and their attributes
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("BBB", "artist1");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("CCC", "artist2");
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("DDD", "artist3");
        song4.setAttributes(song4attr);

        // Insert songs into the AVL tree by artist name
        List<Song> l = new ArrayList<>();
        l.add(song1);
        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getAttributes().getArtistName(), l);
        testTree = testTree.insertByArtistName(song2);
        testTree = testTree.insertByArtistName(song3);
        testTree = testTree.insertByArtistName(song4);

        // Delete a song that resides in a leaf node
        testTree = (AVLTree<List<Song>>) testTree.deleteByArtistName(song4);

        //assertEquals("", testTree.toString());
        assertEquals("artist2", testTree.key);
        assertEquals("artist1", testTree.leftNode.key);
        assertNull(testTree.leftNode.rightNode.key);
    }

    /**
     * Tests AVL tree deletion of a song by artist name with one child,
     * ensuring proper tree structure post-deletion.
     * Includes song attributes handling. Handles exceptions and timeouts.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void deleteByArtistNameWithOneChildTest() {
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("BBB", "artist1");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("CCC", "artist2");
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("DDD", "artist3");
        song4.setAttributes(song4attr);

        Song song5 = new Song("5");
        SongAttributes song5attr = new SongAttributes("EEE", "artist4");
        song5.setAttributes(song5attr);

        // Insert songs into the AVL tree by artist name
        List<Song> l = new ArrayList<>();
        l.add(song1);
        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getAttributes().getArtistName(), l);
        testTree = testTree.insertByArtistName(song2);
        testTree = testTree.insertByArtistName(song3);
        testTree = testTree.insertByArtistName(song4);
        testTree = testTree.insertByArtistName(song5);

        // Delete a song that resides in a node with one child (song4)
        testTree = (AVLTree<List<Song>>) testTree.deleteByArtistName(song4);

        assertEquals("artist2", testTree.key);
        assertEquals("artist4", testTree.rightNode.key);
    }

    /**
     * Tests AVL tree deletion of a song by artist name with two children,
     * ensuring proper tree structure post-deletion.
     *
     * @author: Oscar Wei
     */

    @Test(timeout = 1000)
    public void deleteByArtistNameWithTwoChildrenTest() {
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("BBB", "artist1");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("CCC", "artist2");
        song3.setAttributes(song3attr);

        Song song4 = new Song("4");
        SongAttributes song4attr = new SongAttributes("DDD", "artist3");
        song4.setAttributes(song4attr);

        Song song5 = new Song("5");
        SongAttributes song5attr = new SongAttributes("EEE", "artist4");
        song5.setAttributes(song5attr);

        // Insert songs into the AVL tree by artist name
        List<Song> l = new ArrayList<>();
        l.add(song1);
        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getAttributes().getArtistName(), l);
        testTree = testTree.insertByArtistName(song2);
        testTree = testTree.insertByArtistName(song3);
        testTree = testTree.insertByArtistName(song4);
        testTree = testTree.insertByArtistName(song5);
        testTree = (AVLTree<List<Song>>) testTree.deleteByArtistName(song3);

        assertEquals("artist3", testTree.key);
        assertEquals("artist1", testTree.leftNode.key);
        assertEquals("artist4", testTree.rightNode.key);
    }


    /**
     * Tests AVL tree song deletion by genre, verifying tree structure post-deletion,
     * including songs with multiple genres. Handles exceptions and timeouts during testing.
     *
     * @author: Oscar Wei
     */
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

        testTree = (AVLTree<List<Song>>) testTree.deleteByGenre("Rock", song1);
        testTree = (AVLTree<List<Song>>) testTree.deleteByGenre("R&B", song3);
    }

    /**
     * Tests AVL tree deletion of a song by release date, focusing on a leaf node.
     *
     * @author: Oscar Wei
     *
     */
    @Test(timeout = 1000)
    public void deleteOneOfSongByReleaseDateTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1attr.setReleaseDate("2023-10-14");
        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("BBB", "artist2");
        song2attr.setReleaseDate("2023-10-14");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("CCC", "artist3");
        song3attr.setReleaseDate("2023-10-15");
        song3.setAttributes(song3attr);

        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getReleaseDate(), new ArrayList<>());

        // The song is inserted making it a leaf node.
        testTree = testTree.insertByReleaseDate(song1);
        testTree = testTree.insertByReleaseDate(song2);
        testTree = testTree.insertByReleaseDate(song3);

        // Deleting the leaf node.
        testTree = (AVLTree<List<Song>>) testTree.deleteByReleaseDate(song1);
        assertEquals("2023-10-14", testTree.key);
        List<Song> resList = new ArrayList<>();
        resList.add(song2);

        assertEquals(resList, testTree.value);
    }

    /**
     * Tests AVL tree deletion of a song by release date from a leaf node.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void deleteLeafByReleaseDateTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1attr.setReleaseDate("2023-10-14");
        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("BBB", "artist2");
        song2attr.setReleaseDate("2023-10-14");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("CCC", "artist3");
        song3attr.setReleaseDate("2023-10-15");
        song3.setAttributes(song3attr);

        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getReleaseDate(), new ArrayList<>());

        // The song is inserted making it a leaf node.
        testTree = testTree.insertByReleaseDate(song1);
        testTree = testTree.insertByReleaseDate(song2);
        testTree = testTree.insertByReleaseDate(song3);


        // Deleting the leaf node.
        testTree = (AVLTree<List<Song>>) testTree.deleteByReleaseDate(song3);

        assertEquals("2023-10-14", testTree.key);
        List<Song> resList = new ArrayList<>();
        resList.add(song1);
        resList.add(song2);
        assertEquals(resList, testTree.value);
        assertNull(testTree.leftNode.key);
        assertNull(testTree.rightNode.key);
    }


    /**
     * Tests AVL tree deletion of a song by release date when the song has one child.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void deleteNodeWithOneChildByReleaseDateTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1attr.setReleaseDate("2023-10-14");
        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("BBB", "artist2");
        song2attr.setReleaseDate("2023-10-15");
        song2.setAttributes(song2attr);

        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getReleaseDate(), new ArrayList<>());

        // Inserting two nodes, making song1 a parent and song2 a child.
        testTree = testTree.insertByReleaseDate(song1);
        testTree = testTree.insertByReleaseDate(song2);

        // Deleting the parent node with one child.
        testTree = (AVLTree<List<Song>>) testTree.deleteByReleaseDate(song1);
        assertEquals("2023-10-15", testTree.key);
        assertNull(testTree.rightNode.key);
        assertNull(testTree.leftNode.key);
    }

    /**
     * Tests AVL tree deletion of a song by release date when the song has two children.
     *
     * @author: Oscar Wei
     */
    @Test(timeout = 1000)
    public void deleteNodeWithTwoChildrenByReleaseDateTest(){
        Song song1 = new Song("1");
        SongAttributes song1attr = new SongAttributes("AAA", "artist1");
        song1attr.setReleaseDate("2023-10-14");
        song1.setAttributes(song1attr);

        Song song2 = new Song("2");
        SongAttributes song2attr = new SongAttributes("BBB", "artist2");
        song2attr.setReleaseDate("2023-10-15");
        song2.setAttributes(song2attr);

        Song song3 = new Song("3");
        SongAttributes song3attr = new SongAttributes("CCC", "artist3");
        song3attr.setReleaseDate("2023-10-13");
        song3.setAttributes(song3attr);

        AVLTree<List<Song>> testTree = new AVLTree<>(song1.getReleaseDate(), new ArrayList<>());

        // Inserting three nodes, making song1 a parent with song2 and song3 as its children.
        testTree = testTree.insertByReleaseDate(song1);
        testTree = testTree.insertByReleaseDate(song2);
        testTree = testTree.insertByReleaseDate(song3);

        // Deleting the parent node with two children.
        testTree = (AVLTree<List<Song>>) testTree.deleteByReleaseDate(song2);

        //assertEquals("", testTree.toString());
        assertEquals("2023-10-14", testTree.key);
        assertEquals("2023-10-13", testTree.leftNode.key);
    }
}
