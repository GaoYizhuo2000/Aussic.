package au.edu.anu.Aussic.models.GsonLoader;

import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;


        import java.io.IOException;
        import java.io.Reader;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.util.List;
        import java.util.Map;

        import au.edu.anu.Aussic.models.entity.Artist;
        import au.edu.anu.Aussic.models.entity.Genre;
        import au.edu.anu.Aussic.models.entity.Session;
        import au.edu.anu.Aussic.models.entity.Song;
        import au.edu.anu.Aussic.models.entity.User;

/**
 * @author: u7516507, Evan Cheung
 * @author: u7581818, Oscar Wei
 */

public class GsonLoader {

    /**
     * Loads a list of Song objects from a JSON file.
     *
     * @param filename The path to the JSON file.
     * @return A list of Song objects, or null if an error occurs.
     *
     * @author: Oscar Wei
     */

    public static List<Song> loadSongsFromJson(String filename) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
            Gson gson = new Gson();
            return gson.fromJson(reader, new TypeToken<List<Song>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Converts a Map of data from Firestore into a Song object.
     *
     * @param fromFireStore A Map containing song data from Firestore.
     * @return A Song object created from the Firestore data.
     *
     * @author: Evan Cheung
     */

    public static Song loadSong(Map<String, Object> fromFireStore){
        Gson gson = new Gson();
        String jsonData = gson.toJson(fromFireStore);
        return gson.fromJson(jsonData, Song.class);
    }

    /**
     * Converts a Map of data from Firestore into an Artist object.
     *
     * @param fromFireStore A Map containing artist data from Firestore.
     * @return An Artist object created from the Firestore data.
     *
     * @author: Evan Cheung
     */

    public static Artist loadArtist(Map<String, Object> fromFireStore){
        Gson gson = new Gson();
        String jsonData = gson.toJson(fromFireStore);
        return gson.fromJson(jsonData, Artist.class);
    }

    /**
     * Converts a Map of data from Firestore into a Genre object.
     *
     * @param fromFireStore A Map containing genre data from Firestore.
     * @return A Genre object created from the Firestore data.
     *
     * @author: Evan Cheung
     */

    public static Genre loadGenre(Map<String, Object> fromFireStore){
        Gson gson = new Gson();
        String jsonData = gson.toJson(fromFireStore);
        return gson.fromJson(jsonData, Genre.class);
    }

    /**
     * Converts a Map of data from Firestore into a User object.
     *
     * @param fromFireStore A Map containing user data from Firestore.
     * @return A User object created from the Firestore data.
     *
     * @author: Evan Cheung
     */
    public static User loadUser(Map<String, Object> fromFireStore){
        Gson gson = new Gson();
        String jsonData = gson.toJson(fromFireStore);
        return gson.fromJson(jsonData, User.class);
    }

    /**
     * Converts a Map of data from Firestore into a Session object.
     *
     * @param fromFireStore A Map containing session data from Firestore.
     * @return A Session object created from the Firestore data.
     *
     * @author: Evan Cheung
     */

    public static Session loadSession(Map<String, Object> fromFireStore){
        Gson gson = new Gson();
        String jsonData = gson.toJson(fromFireStore);
        return gson.fromJson(jsonData, Session.class);
    }

}
