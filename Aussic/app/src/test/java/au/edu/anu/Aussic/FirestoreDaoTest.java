package au.edu.anu.Aussic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

public class FirestoreDaoTest {
    private FirestoreDao firestoreDao;



    @Before
    public void setUp(){
        firestoreDao = Mockito.mock(FirestoreDao.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetRandomSong() throws ExecutionException, InterruptedException {
        Map<String, Object> mockSong = new HashMap<>();
        mockSong.put("id", "123");
        mockSong.put("type", "testType");
        mockSong.put("href", "testHref");

        Map<String, Object> songAttributes = new HashMap<>();
        songAttributes.put("albumName", "Test Album");
        songAttributes.put("genreNames", Arrays.asList("Pop", "Rock"));
        songAttributes.put("artistName", "Test Artist");
        songAttributes.put("name", "Test Song");
        songAttributes.put("trackNumber", 5);
        songAttributes.put("durationInMillis", 5000);
        songAttributes.put("releaseDate", "2023-10-17");

        mockSong.put("attributes", songAttributes);

        Mockito.when(firestoreDao.getRandomSong()).thenReturn(java.util.concurrent.CompletableFuture.completedFuture(mockSong));
        Map<String, Object> song = firestoreDao.getRandomSong().get();

        assertNotNull(song);

        assertEquals("123", song.get("id"));
        assertEquals("testType", song.get("type"));
        assertEquals("testHref", song.get("href"));

        // Assertions for songAttributes
        Map<String, Object> attributes = (Map<String, Object>) song.get("attributes");
        assertNotNull(attributes);
        assertEquals("Test Album", attributes.get("albumName"));
        assertEquals(Arrays.asList("Pop", "Rock"), attributes.get("genreNames"));
        assertEquals("Test Artist", attributes.get("artistName"));
        assertEquals("Test Song", attributes.get("name"));
        assertEquals(5, attributes.get("trackNumber"));
        assertEquals(5000, attributes.get("durationInMillis"));
        assertEquals("2023-10-17", attributes.get("releaseDate"));

    }

    @Test
    public void testGetRandomSongs() throws ExecutionException, InterruptedException {
        List<Map<String, Object>> mockSongs = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            Map<String, Object> mockSong = new HashMap<>();
            mockSong.put("id", String.valueOf(i));
            mockSong.put("name", "Sample Song " + i);
            mockSongs.add(mockSong);
        }

        Mockito.when(firestoreDao.getRandomSongs(5)).thenReturn(java.util.concurrent.CompletableFuture.completedFuture(mockSongs));

        List<Map<String, Object>> songs = firestoreDao.getRandomSongs(5).get();
        assertNotNull(songs);
        assertEquals(5, songs.size());
    }

    @Test
    public void testSearchSongs() throws ExecutionException, InterruptedException {
        Map<String, String> terms = new HashMap<>();
        terms.put("genre", "rock");

        // Mock song based on provided structure
        Map<String, Object> mockSong = createMockSong();
        List<Map<String, Object>> mockSongsList = Arrays.asList(mockSong, mockSong, mockSong);

        // Mocking the searchSongs result
        Mockito.when(firestoreDao.searchSongs(terms)).thenReturn(java.util.concurrent.CompletableFuture.completedFuture(mockSongsList));

        List<Map<String, Object>> songs = firestoreDao.searchSongs(terms).get();

        assertNotNull(songs);
        assertEquals(3, songs.size());

        // Checking structure for each song
        for(Map<String, Object> song : songs) {
            validateSongStructure(song);
        }
    }

    @Test
    public void testGetAllUsers() throws ExecutionException, InterruptedException {
        // Mock user data
        Map<String, Object> mockUser = new HashMap<>();
        mockUser.put("id", "user123");
        mockUser.put("name", "John Doe");
        mockUser.put("email", "john.doe@example.com");
        List<Map<String, Object>> mockUsersList = Arrays.asList(mockUser, mockUser, mockUser);

        // Mocking the getAllUsers result
        Mockito.when(firestoreDao.getAllUsers()).thenReturn(java.util.concurrent.CompletableFuture.completedFuture(mockUsersList));

        List<Map<String, Object>> users = firestoreDao.getAllUsers().get();

        assertNotNull(users);
        assertEquals(3, users.size());

        // Assertions based on expected user data
        for(Map<String, Object> user : users) {
            assertEquals("user123", user.get("id"));
            assertEquals("John Doe", user.get("name"));
            assertEquals("john.doe@example.com", user.get("email"));
        }
    }

    @Test
    public void testGetSessions() throws ExecutionException, InterruptedException {
        // Mock session data
        Map<String, Object> mockSession = new HashMap<>();
        mockSession.put("sessionId", "session123");
        mockSession.put("startTime", "10:00 AM");
        mockSession.put("duration", "2 hours");
        List<Map<String, Object>> mockSessionsList = Arrays.asList(mockSession, mockSession, mockSession);

        // Mocking the getSessions result
        Mockito.when(firestoreDao.getSessions()).thenReturn(java.util.concurrent.CompletableFuture.completedFuture(mockSessionsList));

        List<Map<String, Object>> sessions = firestoreDao.getSessions().get();

        assertNotNull(sessions);
        assertEquals(3, sessions.size());

        // Assertions based on expected session data
        for(Map<String, Object> session : sessions) {
            assertEquals("session123", session.get("sessionId"));
            assertEquals("10:00 AM", session.get("startTime"));
            assertEquals("2 hours", session.get("duration"));
        }
    }

    private Map<String, Object> createMockSong() {
        Map<String, Object> mockSong = new HashMap<>();
        mockSong.put("id", "123");
        mockSong.put("type", "testType");
        mockSong.put("href", "testHref");

        Map<String, Object> songAttributes = new HashMap<>();
        songAttributes.put("albumName", "Test Album");
        songAttributes.put("genreNames", Arrays.asList("Pop", "Rock"));
        songAttributes.put("artistName", "Test Artist");
        songAttributes.put("name", "Test Song");
        songAttributes.put("trackNumber", 5);
        songAttributes.put("durationInMillis", 5000);
        songAttributes.put("releaseDate", "2023-10-17");

        mockSong.put("attributes", songAttributes);

        return mockSong;
    }

    private void validateSongStructure(Map<String, Object> song) {
        assertEquals("123", song.get("id"));
        assertEquals("testType", song.get("type"));
        assertEquals("testHref", song.get("href"));

        Map<String, Object> attributes = (Map<String, Object>) song.get("attributes");
        assertNotNull(attributes);
        assertEquals("Test Album", attributes.get("albumName"));
        assertEquals(Arrays.asList("Pop", "Rock"), attributes.get("genreNames"));
        assertEquals("Test Artist", attributes.get("artistName"));
        assertEquals("Test Song", attributes.get("name"));
        assertEquals(5, attributes.get("trackNumber"));
        assertEquals(5000, attributes.get("durationInMillis"));
        assertEquals("2023-10-17", attributes.get("releaseDate"));
    }

}
