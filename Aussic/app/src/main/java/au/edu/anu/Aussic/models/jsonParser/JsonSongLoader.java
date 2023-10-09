package au.edu.anu.Aussic.models.jsonParser;

        import com.google.gson.Gson;
        import com.google.gson.reflect.TypeToken;


        import java.io.IOException;
        import java.io.Reader;
        import java.nio.file.Files;
        import java.nio.file.Paths;
        import java.util.List;

        import au.edu.anu.Aussic.models.entity.Song;

public class JsonSongLoader {

    public List<Song> loadSongsFromJson(String filename) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
            Gson gson = new Gson();
            // Type token pattern to handle generic array deserialization
            return gson.fromJson(reader, new TypeToken<List<Song>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Or handle exception accordingly
        }
    }
}
