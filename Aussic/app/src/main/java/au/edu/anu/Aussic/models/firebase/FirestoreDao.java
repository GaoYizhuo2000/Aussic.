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

    /**
     * get a random song data from firebase
     */
    CompletableFuture<Map<String, Object>> getRandomSong();
    /**
     * *get a number of random songs from firebase
     * @param number the number of random songs
     * **/
    CompletableFuture<List<Map<String, Object>>> getRandomSongs(int number);
    /**
     * get a random user action to feed the app to simulate other user's action
     * **/
    CompletableFuture<Map<String, Object>> getRandomUseraction();
    /**
     * search songs from firebase according to searching terms
     * @param terms searching terms in form of Map derived from the parser
     **/
    CompletableFuture<List<Map<String, Object>>> searchSongs(Map<String, String> terms);
    /**
     * add the user data to firestore when signing up a new account
     * @param user current user which is instance of User
     **/
    void addUserdata(User user);
    /**
     * get user data from firebase
     * @param user current user
     **/
    CompletableFuture<Map<String, Object>> getUserdata(FirebaseUser user);
    /**
     * get a number of users datas from firebase
     * @param IDs List of users Ids
     **/
    CompletableFuture <List<Map<String, Object>>> getUsersData(List<String> IDs);
    /**
     * update User Image
     * @param imageUrl of the firebase storage in which the image is store
     **/
    void updateUserImage(String imageUrl);
    /**
     * update User favorites in firestore after the user added a new favorite song
     * @param songId of the newly added song
     **/
    CompletableFuture<String> updateUserFavorites(String songId);
    /**
     * delete User favorites in firestore after the user deleted a favorite song
     * @param songId of the song to be deleted
     **/
    void deleteUserFavorites(String songId);
    /**
     * update User likes in firestore after the user liked a song
     * @param songId of the song to be deleted
     **/
    CompletableFuture<String> updateUserLikes(String songId);
    /**
     * get a number of songs data
     * @param songIdList of the song to be fetched
     **/
    CompletableFuture<List<Map<String, Object>>> getSongsByIdList(List<String> songIdList);

    /**
     * get all current users data
     **/
    CompletableFuture<List<Map<String, Object>>> getAllUsers();
    /**
     * get all the sessions that the current user is involved in
     **/
    CompletableFuture<List<Map<String, Object>>> getSessions();
    /**
     * get the session by its name
     * @param sessionName the sessionsName ie.:documentId
     **/
    CompletableFuture<Map<String, Object>> getSession(String sessionName);
    /**
     * create new session with the target user
     **/
    void createSession(String targetUserName);
    /**
     * update chatting history
     * @param sessionId the documentId in collection sessions eg.:t1@gmail.com&t2@gmail.com
     * @param message  which the current user has just sent
     * **/
    void updateHistory(String sessionId, String message);
    void updateBlockList(String id);
    void removeBlockList(String id);
    void setSongRealTimeListener(Song song);
    void setUsrRealTimeListener(User usr);
    void setSessionCollectionRealtimeListener();
    void setSessionRealTimeListener(Session session);
    /**
     * get a number of random genres from firestore
     * @param num the number of random genres
     * **/
    CompletableFuture<List<Map<String, Object>>> loadRandomGenres(int num);

}
