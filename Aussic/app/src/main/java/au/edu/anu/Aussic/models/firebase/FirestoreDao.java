package au.edu.anu.Aussic.models.firebase;

import java.util.List;
import java.util.Map;

public interface FirestoreDao {
    void updateSongs();
    List<Map<String, Object>> searchSongs(Map<String, Object> terms);
}
