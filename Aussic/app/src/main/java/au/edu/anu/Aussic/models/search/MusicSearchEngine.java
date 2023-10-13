package au.edu.anu.Aussic.models.search;

import au.edu.anu.Aussic.models.avl.AVLTree;
import au.edu.anu.Aussic.models.avl.EmptyTree;
import au.edu.anu.Aussic.models.avl.Tree;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.SongAttributes;


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
        idTree = new AVLTree<>(song0.getId(), l);
        songNameTree = new AVLTree<>(song0.getSongName(), l);
        artistTree = new AVLTree<>(song0.getArtistName(), l);
        releaseDateTree = new AVLTree<>(song0.getReleaseDate(), l);
        for(Song song: songList){
            idTree = idTree.insertById(song);
            songNameTree.insertByName(song);
            artistTree.insertByArtistName(song);
            releaseDateTree.insertByReleaseDate(song);
        }

        //split songs with multiple genres
        List<Song> songListByGenre = new ArrayList<>();
        for(Song song: songList){
            List<String> genres = song.getGenre();
            if(genres.size() > 1){
                for(String genre : genres){
                    Song songFrag = song.clone();
                    SongAttributes songFragAttr = songFrag.getAttributes();
                    List<String> genrel = new ArrayList<>();
                    genrel.add(genre);
                    songFragAttr.setGenreNames(genrel);
                    songFrag.setAttributes(songFragAttr);
                    songListByGenre.add(songFrag);
                }
            }else{
                songListByGenre.add(song);
            }
        }
        //////////////////
        Song song1 = songListByGenre.remove(0);
        List<Song> l1 = new ArrayList<>();
        l1.add(song1);
        genreTree = new AVLTree<>(song1.getGenre().get(0), l1);
        for(Song song: songListByGenre){
            genreTree.insertByGenre(song);
        }
    }

    public Set<Song> search(Map<String, String> terms){
        Set<Song> idTreeRes =  new HashSet<>();
        Set<Song> songNameTreeRes= new HashSet<>();
        Set<Song> artistTreeRes= new HashSet<>();
        Set<Song> genreTreeRes= new HashSet<>();
        Set<Song> releaseDateTreeRes= new HashSet<>();
        List<Set<Song>> resultSetList = new ArrayList<>();

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