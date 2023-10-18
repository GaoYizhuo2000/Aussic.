package au.edu.anu.Aussic;

import org.junit.Test;

import static org.junit.Assert.*;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class p2pTest {
    FirestoreDao firestoreDao = new FirestoreDaoImpl();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Test
    public void testGetAllUsers() {
        firestoreDao.getAllUsers().thenAccept(result -> {
            assert result.get(0).containsKey("username");
        });
    }

    @Test
    public void testGetsessions() {
        firestoreDao.getSessions().thenAccept(result -> {
            assert result.get(0).containsKey("history");
        });
    }
    @Test
    public void testCreateSession() {
        firestoreDao.createSession("t4@gmail.com");


        DocumentReference docRef = db.collection("sessions").document(RuntimeObserver.currentUser.username+"&"+"t4@gmail.com");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                assert task.getResult().exists();
            }
        });
    }
    @Test
    public void testUpdateHistory() {
        firestoreDao.updateHistory(RuntimeObserver.currentUser.username+"&"+"t4@gmail.com", "Testing message");
        db.collection("sessions").document(RuntimeObserver.currentUser.username+"&"+"t4@gmail.com")
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Map<String,String>> history = (List<Map<String,String>>)task.getResult().get("history");
                        assertEquals(history.get(history.size() - 1).get("message"), "Testing message");
                    }
                });

    }


}
