package au.edu.anu.Aussic.models.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import au.edu.anu.Aussic.models.avl.AVLTree;

public class Main {
    public static void main(String[] args) {
        // Load songs from JSON file
        List<Song> songs = loadSongsFromJson("songs.json");

        // Create an AVL tree and insert songs
        AVLTree<Song> tree = new AVLTree<>(songs.get(0));
        for (int i = 1; i < songs.size(); i++) {
            tree.insert(songs.get(i));
        }

        tree.inorderTraversal();

    }

    public static List<Song> loadSongsFromJson(String filename) {
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
