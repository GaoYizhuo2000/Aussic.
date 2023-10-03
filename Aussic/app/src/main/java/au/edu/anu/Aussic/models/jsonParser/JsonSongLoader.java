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

    private Gson gson;

    public JsonSongLoader() {
        this.gson = new Gson();
    }

    public List<Song> loadSongsFromJson(String filename) {
        try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {
            return gson.fromJson(reader, new TypeToken<List<Song>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Or handle the exception accordingly
        }
    }
}
