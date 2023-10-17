package au.edu.anu.Aussic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.firebase.firestore.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

import org.robolectric.RobolectricTestRunner;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class FirestoreDaoImplTest {

    private FirestoreDaoImpl firestoreDao;
    private Song testSong;

    @Captor
    private ArgumentCaptor<Song> songCaptor;

    @Before
    public void setUp() {
        //Init mockito anntations
        MockitoAnnotations.initMocks(this);
        //Init firestoreDao
        firestoreDao = new FirestoreDaoImpl();

        // Mock Firebase instances
        firestoreDao.firestore = mock(FirebaseFirestore.class);
        firestoreDao.songsRef = mock(CollectionReference.class);

        testSong = mock(Song.class);
    }

    @Test
    public void testSetSongRealTimeListener() {
        testSong.setId("12345");

        DocumentReference mockDocument = mock(DocumentReference.class);
        when(firestoreDao.songsRef.document(testSong.getId())).thenReturn(mockDocument);

        DocumentSnapshot mockSnapshot = mock(DocumentSnapshot.class);
        Map<String, Object> songData = new HashMap<>();
        songData.put("id", "12345");
        songData.put("name", "Sample Song");
        songData.put("artist", "Sample Artist");
        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.getData()).thenReturn(songData);

        firestoreDao.setSongRealTimeListener(testSong);

        verify(mockDocument, times(1)).addSnapshotListener(any());

        // Now, trigger the snapshot listener manually
        EventListener<DocumentSnapshot> listener = getEventListener(mockDocument);
        listener.onEvent(mockSnapshot, null);

        // Verify the setSong method was called on the Song object
        verify(testSong, times(1)).setSong(songCaptor.capture());

        Song updatedSong = songCaptor.getValue();
        assertEquals("12345", updatedSong.getId());
        assertEquals("Sample Song", updatedSong.getSongName());
        assertEquals("Sample Artist", updatedSong.getArtistName());
    }

    // Utility method to get the listener from the mock DocumentReference
    private EventListener<DocumentSnapshot> getEventListener(DocumentReference mockDocument) {
        ArgumentCaptor<EventListener<DocumentSnapshot>> listenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        verify(mockDocument).addSnapshotListener(listenerCaptor.capture());
        return listenerCaptor.getValue();
    }
}



