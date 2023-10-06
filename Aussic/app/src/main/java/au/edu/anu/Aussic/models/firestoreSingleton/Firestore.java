package au.edu.anu.Aussic.models.firestoreSingleton;

import com.google.firebase.firestore.FirebaseFirestore;

public class Firestore {
    private static FirebaseFirestore firestore;

    private Firestore() {
        // 私有构造函数
    }

    public static FirebaseFirestore getInstance() {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore;
    }
}
