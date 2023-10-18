package au.edu.anu.Aussic.models.firebase;

/**
 * @author: u7552399, Yizhuo Gao
 */

import com.google.firebase.firestore.FirebaseFirestore;

public class SingletonFirestoreDbConnection {
    private static FirebaseFirestore firestore;

    private SingletonFirestoreDbConnection() {
    }

    public static FirebaseFirestore getInstance() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }
}
