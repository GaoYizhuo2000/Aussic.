package au.edu.anu.Aussic.models.search;

import java.util.List;
import java.util.ArrayList;

import au.edu.anu.Aussic.models.avl.AVLTree;
import au.edu.anu.Aussic.models.entity.Song;

public class MusicSearchEngine {
    private AVLTree<Song> songTree;

    public MusicSearchEngine(AVLTree<Song> songTree) {
        this.songTree = songTree;
    }

    public List<Song> searchByArtist(String artist) {
        List<Song> result = new ArrayList<>();
        searchByArtist(songTree, artist, result);
        return result;
    }

    private void searchByArtist(AVLTree<Song> node, String artist, List<Song> result) {
        if (node != null && node.value != null) {
            if (node.value.getArtistName().equalsIgnoreCase(artist)) {
                result.add(node.value);
            }
            if (node.leftNode instanceof AVLTree) {
                searchByArtist((AVLTree<Song>) node.leftNode, artist, result);
            }
            if (node.rightNode instanceof AVLTree) {
                searchByArtist((AVLTree<Song>) node.rightNode, artist, result);
            }
        }
    }
}
