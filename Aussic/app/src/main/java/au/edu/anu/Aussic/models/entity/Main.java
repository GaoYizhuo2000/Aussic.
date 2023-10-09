package au.edu.anu.Aussic.models.entity;



import java.util.List;
import java.util.Scanner;

import au.edu.anu.Aussic.models.avl.AVLTree;
import au.edu.anu.Aussic.models.SongLoader.GsonSongLoader;
import au.edu.anu.Aussic.models.parserAndTokenizer.QueryNode;
import au.edu.anu.Aussic.models.search.MusicSearchEngine;

public class Main {

    public static void main(String[] args) {
        GsonSongLoader loader = new GsonSongLoader();
        List<Song> songs = loader.loadSongsFromJson("./src/entity/songs.json");
        AVLTree<Song> tree = null;


        // Create an AVL tree and insert songs
        if (songs != null) {
            tree = new AVLTree<>(songs.get(0));
            for (int i = 1; i < songs.size(); i++) {
                tree = tree.insert(songs.get(i));
            }

            // Optionally print the tree or song names to check the data
            //tree.inorderTraversal();

//            for(Song song : songs) {
//                System.out.println(song.getAttributes().getName());
//            }
        }

        MusicSearchEngine engine = new MusicSearchEngine(tree);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a search query: ");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("exit")) {
                break;
            }

            QueryNode query = engine.parseQuery(userInput);
            List<Song> results = engine.search(query);

            engine.displayResults(results);
        }
        scanner.close();
    }

}
