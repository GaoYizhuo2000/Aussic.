package au.edu.anu.Aussic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import au.edu.anu.Aussic.models.entity.User;

public class UserTest {
    private User user;


    @Before
    public void setUp() {
        user = new User("TestUsername", "http://example.com/icon.png");
    }

    @Test
    public void testUserInitialState() { //Empty input test
        assertTrue(user.getFavorites().isEmpty());
        assertTrue(user.getLikes().isEmpty());
        assertTrue(user.getBlockList().isEmpty());
        assertEquals("", user.location);
    }


    @Test
    public void testAddFavorites() {
        user.addFavorites("song1");
        assertTrue(user.getFavorites().contains("song1"));
    }

    @Test
    public void testAddLikes() {
        user.addLikes("song2");
        assertTrue(user.getLikes().contains("song2"));
    }

    @Test
    public void testMultipleAddFavorites() {
        user.addFavorites("song1");
        user.addFavorites("song1");
        assertEquals(2, user.getFavorites().size());
    }

    @Test
    public void testSetUsrNullValues() {
        User newUser = new User(null, null);
        user.setUsr(newUser);

        assertNull(user.username);
        assertNull(user.iconUrl);
    }

    @Test
    public void testUserWithDefaultLocation() {
        User newUser = new User("Test", "http://example.com/icon2.png");
        assertEquals("", newUser.location);
    }

    @Test
    public void testSetUsr() {
        User newUser = new User("NewUsername", "http://example.com/newicon.png");
        newUser.addFavorites("song3");
        newUser.addLikes("song4");

        user.setUsr(newUser);

        assertEquals("NewUsername", user.username);
        assertEquals("http://example.com/newicon.png", user.iconUrl);
        assertTrue(user.getFavorites().contains("song3"));
        assertTrue(user.getLikes().contains("song4"));
    }


}
