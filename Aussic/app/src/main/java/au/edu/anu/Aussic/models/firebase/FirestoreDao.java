package au.edu.anu.Aussic.models.firebase;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import au.edu.anu.Aussic.models.entity.Session;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.User;

/**
 * @author: u7552399, Yizhuo Gao
 * @author: u7581818, Oscar Wei
 * @author: u7516507, Evan Cheung
 */

public interface FirestoreDao {
    void updateBlockList(String id);
    void removeBlockList(String id);

    CompletableFuture<Map<String, Object>> getRandomSong();
    CompletableFuture<List<Map<String, Object>>> getRandomSongs(int number);
    CompletableFuture<Map<String, Object>> getRandomUseraction();
    CompletableFuture<List<Map<String, Object>>> searchSongs(Map<String, String> terms);
    void addUserdata(User user);
    CompletableFuture<Map<String, Object>> getUserdata(FirebaseUser user);
    CompletableFuture <List<Map<String, Object>>> getUsersData(List<String> IDs);
    CompletableFuture<String> updateUserFavorites(String songId);
    void deleteUserFavorites(String songId);
    CompletableFuture<String> updateUserLikes(String songId);
    CompletableFuture<List<Map<String, Object>>> getSongsByIdList(List<String> songIdList);

    //get all existing users
    CompletableFuture<List<Map<String, Object>>> getAllUsers();
    //get all the sessions that the current user is involved in
    CompletableFuture<List<Map<String, Object>>> getSessions();
    // Get the session by its name
    CompletableFuture<Map<String, Object>> getSession(String sessionName);
    // create new session with the target user, if succeed, return null, else return a message
    void createSession(String targetUserName);
    /**
     * update chatting history
     * @param sessionId the documentId in collection sessions eg.:t1@gmail.com&t2@gmail.com
     * @param message  message the current user has just sent
     * **/
    void updateHistory(String sessionId, String message);
    void setSongRealTimeListener(Song song);
    void setUsrRealTimeListener(User usr);
    void setSessionCollectionRealtimeListener();
    void setSessionRealTimeListener(Session session);
    CompletableFuture<List<Map<String, Object>>> loadRandomGenres(int num);

}
