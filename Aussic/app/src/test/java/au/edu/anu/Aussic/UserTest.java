package au.edu.anu.Aussic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import au.edu.anu.Aussic.models.entity.Comments;
import au.edu.anu.Aussic.models.entity.Detail;
import au.edu.anu.Aussic.models.entity.MessageContent;
import au.edu.anu.Aussic.models.entity.Session;
import au.edu.anu.Aussic.models.entity.User;
import au.edu.anu.Aussic.models.userAction.Comment;
import au.edu.anu.Aussic.models.userAction.Favorites;
import au.edu.anu.Aussic.models.userAction.Like;
import au.edu.anu.Aussic.models.userAction.UserAction;
import au.edu.anu.Aussic.models.userAction.UserActionFactory;

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

    //Some tests according to the user actions like like, comment, and so on.
    @Test
    public void testGetDetailsInComments() {
        Comments comments = new Comments();
        assertNull(comments.getDetails());
    }

    @Test
    public void testDetail(){
        Detail detail = new Detail("Test Content", "Test Uid");
        assertEquals("Test Content", detail.getContent());
        assertEquals("Test Uid", detail.getUid());
    }

    @Test
    public void testGetMessage() {
        MessageContent messageContent = new MessageContent();
        assertNull(messageContent.getMessage());
    }

    @Test
    public void testGetUserName() {
        MessageContent messageContent = new MessageContent();
        assertNull(messageContent.getUserName());
    }

    @Test
    public void testSessionConstruction() {
        Session session = new Session("TestSession");
        assertEquals("TestSession", session.getName());
        assertNull(session.getUsers());
        assertNull(session.getHistory());
    }

    @Test
    public void testEquals() {
        Session session1 = new Session("Session1");
        Session session2 = new Session("Session1");
        Session session3 = new Session("Session2");

        assertEquals(session1, session2);
        assertNotEquals(session1, session3);
    }

    @Test
    public void testLikeToastMessage() {
        Like like = new Like("like", "user1", "SongA", 1);
        String toastMessage = like.getToastMessage();
        assertEquals("User user1 liked the song \"SongA\"", toastMessage);
    }

    @Test
    public void testFavoritesToastMessage() {
        Favorites favorites = new Favorites("favorite", "user1", "SongB", 2);
        String toastMessage = favorites.getToastMessage();
        assertEquals("User user1 added the song \"SongB\" to favorites", toastMessage);
    }



    @Test
    public void testCommonToastMessage() {
        Comment comment = new Comment("comment", "user1", "SongA", 1, "Great song!");
        String toastMessage = comment.getToastMessage();
        assertEquals("User user1 commented on the song \"SongA\": Great song!", toastMessage);
    }

    @Test
    public void testCreateUserActionForComment() { //Test the factory pattern of user action
        String jsonString = "{ \"actionType\":\"comment\", \"userName\":\"user1\", \"targetSong\":\"SongA\", \"targetSongId\":1, \"content\":\"Great song!\" }";
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        UserAction userAction = UserActionFactory.createUserAction(jsonObject);
        assertTrue(userAction instanceof Comment);
    }

}
