package au.edu.anu.Aussic.models.search;

import au.edu.anu.Aussic.models.avl.AVLTree;
import au.edu.anu.Aussic.models.avl.Tree;
import au.edu.anu.Aussic.models.entity.Song;


import java.util.*;

public class MusicSearchEngine {


    List<Song> songList = new ArrayList<>();
    private AVLTree<List<Song>> idTree;
    private AVLTree<List<Song>> songNameTree;
    private AVLTree<List<Song>> artistTree;
    private AVLTree<List<Song>> genreTree;
    private AVLTree<List<Song>> releaseDateTree;

    public MusicSearchEngine(List<Song> songList) {
        this.songList = songList;
        initialise();
    }

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
    public void deleteSong(Song song){
        idTree = (AVLTree<List<Song>>) idTree.deleteById(song);
        songNameTree = (AVLTree<List<Song>>) songNameTree.deleteByName(song);
        artistTree = (AVLTree<List<Song>>) artistTree.deleteByArtistName(song);
        releaseDateTree = (AVLTree<List<Song>>) releaseDateTree.deleteByReleaseDate(song);
        for(String genre: song.getGenre()){
            genreTree = (AVLTree<List<Song>>) genreTree.deleteByGenre(genre, song);
        }
    }

    public void addSong(Song song){
        idTree = idTree.insertById(song);
        songNameTree = songNameTree.insertByName(song);
        artistTree = artistTree.insertByArtistName(song);
        releaseDateTree = releaseDateTree.insertByReleaseDate(song);
        for(String genre: song.getGenre()){
            genreTree = genreTree.insertByGenre(genre, song);
        }
    }

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
    //search engine test
   // List<Song> testList = new ArrayList<>();
   //     testList.add(new Song("1"));
   //             testList.add(new Song("2"));
   //             testList.add(new Song("3"));
   //             testList.add(new Song("4"));
   //             Map<String, String> testTerms = new HashMap<>();
   //     testTerms.put("id", "2");
   //     MusicSearchEngine searchEngine = new MusicSearchEngine(testList);
   //     Set<Song> res = searchEngine.search(testTerms);
   //     System.out.println(res);