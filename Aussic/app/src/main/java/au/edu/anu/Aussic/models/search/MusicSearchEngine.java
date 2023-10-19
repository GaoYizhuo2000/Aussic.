package au.edu.anu.Aussic.models.search;

import au.edu.anu.Aussic.models.avl.AVLTree;
import au.edu.anu.Aussic.models.avl.Tree;
import au.edu.anu.Aussic.models.entity.Song;


import java.util.*;

/**
 * The `MusicSearchEngine` class provides a music search engine that allows searching for songs based on various criteria.
 * It uses AVL trees to efficiently index and search songs by different attributes such as ID, song name, artist name, genre, and release date.
 *
 * Example usage:
 * ```java
 * List<Song> songList = // Load songs from a data source
 * MusicSearchEngine searchEngine = new MusicSearchEngine(songList);
 * Map<String, String> searchTerms = new HashMap<>();
 * searchTerms.put("artistName", "Adele");
 * searchTerms.put("genre", "Pop");
 * Set<Song> searchResults = searchEngine.search(searchTerms);
 * ```
 *
 * Supported search criteria:
 * - `id`: Search by song ID.
 * - `name`: Search by song name.
 * - `artistName`: Search by artist name.
 * - `genre`: Search by genre.
 * - `releaseDate`: Search by release date.
 * - `undefinedTerm`: Search for a generic string term in song name, artist name, and genre.
 *
 * The `MusicSearchEngine` class allows adding songs, deleting songs, and searching for songs based on the provided criteria.
 *
 * @author u7581818, Oscar Wei
 * @author u7552399, Yizhuo Gao
 * @author u7603590, Jiawei Niu
 */
public class MusicSearchEngine {

    List<Song> songList = new ArrayList<>();
    private Tree<List<Song>> idTree;
    private Tree<List<Song>> songNameTree;
    private Tree<List<Song>> artistTree;
    private Tree<List<Song>> genreTree;
    private Tree<List<Song>> releaseDateTree;

    public MusicSearchEngine(List<Song> songList) {
        this.songList = songList;
        initialise();
    }

    /**
     * Initializes the AVL trees by indexing the songs based on various attributes.
     */
    public void initialise(){
        Song song0 = songList.remove(0);
        List<Song> l = new ArrayList<>();
        l.add(song0);

        idTree = new AVLTree<>(song0.getId(), new ArrayList<>(l));
        songNameTree = new AVLTree<>(song0.getSongName(), new ArrayList<>(l));
        artistTree = new AVLTree<>(song0.getArtistName(), new ArrayList<>(l));
        releaseDateTree = new AVLTree<>(song0.getReleaseDate(), new ArrayList<>(l));

        // Initialise genreTree
        genreTree = new AVLTree<>(song0.getGenre().get(0), new ArrayList<>(l));
        for(int i = 1; i < song0.getGenre().size() ;i ++){
            genreTree = genreTree.insertByGenre(song0.getGenre().get(i), song0);
        }

        for(Song song: songList){
            idTree = idTree.insertById(song);
            songNameTree = songNameTree.insertByName(song);
            artistTree = artistTree.insertByArtistName(song);
            releaseDateTree = releaseDateTree.insertByReleaseDate(song);
            for(String genre: song.getGenre()){
                genreTree = genreTree.insertByGenre(genre, song);
            }
        }


    }


    /**
     * Deletes a song from all indexed AVL trees.
     *
     * @param song The song to be deleted.
     */
    public void deleteSong(Song song){
        idTree =  idTree.deleteById(song);
        songNameTree =  songNameTree.deleteByName(song);
        artistTree =  artistTree.deleteByArtistName(song);
        releaseDateTree =  releaseDateTree.deleteByReleaseDate(song);
        for(String genre: song.getGenre()){
            genreTree = genreTree.deleteByGenre(genre, song);
        }
    }


    /**
     * Adds a song to all indexed AVL trees.
     *
     * @param song The song to be added.
     */
    public void addSong(Song song){
        idTree = idTree.insertById(song);
        songNameTree = songNameTree.insertByName(song);
        artistTree = artistTree.insertByArtistName(song);
        releaseDateTree = releaseDateTree.insertByReleaseDate(song);
        for(String genre: song.getGenre()){
            genreTree = genreTree.insertByGenre(genre, song);
        }
    }

    /**
     * Searches for songs based on the provided search terms.
     *
     * @param terms A map of search terms and their corresponding values.
     * @return A set of songs that match the search criteria.
     */
    public Set<Song> search(Map<String, String> terms){
        Set<Song> idTreeRes =  new HashSet<>();
        Set<Song> songNameTreeRes= new HashSet<>();
        Set<Song> artistTreeRes= new HashSet<>();
        Set<Song> genreTreeRes= new HashSet<>();
        Set<Song> releaseDateTreeRes= new HashSet<>();
        List<Set<Song>> resultSetList = new ArrayList<>();

        if(terms.containsKey("undefinedTerm")){
            Tree<List<Song>> result0 = songNameTree.find(terms.get("undefinedTerm"));
            Tree<List<Song>> result1 = artistTree.find(terms.get("undefinedTerm"));
            Tree<List<Song>> result2 = genreTree.find(terms.get("undefinedTerm"));
            List<Song> res = new ArrayList<>();
            if(result0!= null){res.addAll(result0.value);}
            if(result1 != null){res.addAll(result1.value);}
            if(result2 != null){res.addAll(result2.value);}

            return new HashSet<>(res);
        }
        if(terms.containsKey("id")){
            Tree<List<Song>> result = idTree.find(terms.get("id"));
            if(result != null){
                idTreeRes = new HashSet<>(result.value);
            }
            resultSetList.add(idTreeRes);
        }
        if(terms.containsKey("name")){
            Tree<List<Song>> result = songNameTree.find(terms.get("name"));
            if(result != null){
                songNameTreeRes = new HashSet<>(result.value);
            }
            resultSetList.add(songNameTreeRes);
        }
        if(terms.containsKey("artistName")){
            Tree<List<Song>> result = artistTree.find(terms.get("artistName"));
            if(result != null){
                artistTreeRes = new HashSet<>(result.value);
            }
            resultSetList.add(artistTreeRes);
        }
        if(terms.containsKey("genre")){
            Tree<List<Song>> result = genreTree.find(terms.get("genre"));
            if(result != null){
                genreTreeRes = new HashSet<>(result.value);
            }
            resultSetList.add(genreTreeRes);
        }
        if(terms.containsKey("releaseDate")){
            Tree<List<Song>> result = releaseDateTree.find(terms.get("releaseDate"));
            if(result != null){
                releaseDateTreeRes = new HashSet<>(result.value);
            }
            resultSetList.add(releaseDateTreeRes);
        }

        Set<Song> result = new HashSet<>(resultSetList.get(0));
        for (Set<Song> set : resultSetList) {
            result.retainAll(set);
        }
        return result;
    }
}