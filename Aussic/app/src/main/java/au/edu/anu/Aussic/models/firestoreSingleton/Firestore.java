package au.edu.anu.Aussic.models.firestoreSingleton;

import com.google.firebase.firestore.FirebaseFirestore;

public class Firestore {
    private static FirebaseFirestore firestore;

    private Firestore() {
    }

    public static FirebaseFirestore getInstance() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }
}
