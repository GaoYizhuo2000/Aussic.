package au.edu.anu.Aussic.models.SongLoader;

        import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;


        import java.io.IOException;
        import java.io.Reader;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.util.List;
        import java.util.Map;

        import au.edu.anu.Aussic.models.entity.Song;

public class GsonSongLoader {

    public static List<Song> loadSongsFromJson(String filename) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
            Gson gson = new Gson();
            return gson.fromJson(reader, new TypeToken<List<Song>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Song loadSong(Map<String, Object> fromFireStore){
        Gson gson = new Gson();
        String jsonData = gson.toJson(fromFireStore);
        return gson.fromJson(jsonData, Song.class);
    }
}
