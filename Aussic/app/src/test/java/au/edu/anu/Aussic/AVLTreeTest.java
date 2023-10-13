package au.edu.anu.Aussic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.models.avl.AVLTree;
import au.edu.anu.Aussic.models.avl.EmptyTree;
import au.edu.anu.Aussic.models.entity.Song;

public class AVLTreeTest {

    private AVLTree<List<Song>> avlTree;

    @Before
    public void setUp() {
        avlTree = new AVLTree<>("1", new ArrayList<>());
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
