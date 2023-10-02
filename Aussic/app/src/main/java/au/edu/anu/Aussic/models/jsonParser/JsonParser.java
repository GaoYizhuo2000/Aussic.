package au.edu.anu.Aussic.models.jsonParser;
import com.google.gson.Gson;

import au.edu.anu.Aussic.models.entity.Song;

public class JsonParser {
    private Gson gson;

    public JsonParser() {
        gson = new Gson();
    }

    public Song parseJson(String json) {
        return gson.fromJson(json, Song.class);
    }
}
