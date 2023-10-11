package au.edu.anu.Aussic.models.search;

import au.edu.anu.Aussic.models.avl.AVLTree;
import au.edu.anu.Aussic.models.avl.NodeArtist;
import au.edu.anu.Aussic.models.avl.NodeID;
import au.edu.anu.Aussic.models.avl.NodeSongName;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.SongAttributes;
import au.edu.anu.Aussic.models.parserAndTokenizer.Token;
import au.edu.anu.Aussic.models.parserAndTokenizer.Tokenizer;



import java.util.*;

public class MusicSearchEngine {
    private AVLTree<NodeID> idTree = null;
    private AVLTree<NodeSongName> songNameTree = null;
    private AVLTree<NodeArtist> artistTree = null;

    //将歌曲插入AVL树的操作不应该在这里,而是应该在建立歌单的类里面,我暂时写在这里,以后再迁移
    public void insertSong(Song song) {
        NodeID nodeId = new NodeID(song);
        NodeSongName nodeSongName = new NodeSongName(song);
        NodeArtist nodeArtist = new NodeArtist(song);

        if (idTree == null) {
            idTree = new AVLTree<>(nodeId);
        } else {
            idTree = idTree.insert(nodeId);
        }

        if (songNameTree == null) {
            songNameTree = new AVLTree<>(nodeSongName);
        } else {
            songNameTree = songNameTree.insert(nodeSongName);
        }

        if (artistTree == null) {
            artistTree = new AVLTree<>(nodeArtist);
        } else {
            artistTree = artistTree.insert(nodeArtist);
        }
    }



    /**
     * 在指定的AVL树中搜索给定的值。
     * @param type 令牌类型
     * @param value 要搜索的值
     * @return 匹配的结果集
     */
    public Set<String> search(String type, String value) {
        Set<String> resultSet = new HashSet<>();

        switch (type) {
            case "ID":
                Set<NodeID> idResults = idTree.searchAll(new NodeID(new Song(value, "", "", null)));
                for (NodeID node : idResults) {
                    resultSet.add(node.song.getId());
                }
                break;
            case "SONG_NAME":
                SongAttributes songAttributes = new SongAttributes(value, new ArrayList<>(), 0, 0, "", "", null, "", "", null, 0, false, false, false, value, new ArrayList<>(), "");
                Set<NodeSongName> songNameResults = songNameTree.searchAll(new NodeSongName(new Song("", "", "", songAttributes)));
                for (NodeSongName node : songNameResults) {
                    resultSet.add(node.songsSavedByName.get(0).getSongName());
                }
                break;
            case "ARTIST_NAME":
                SongAttributes artistAttributes = new SongAttributes("", new ArrayList<>(), 0, 0, "", "", null, "", "", null, 0, false, false, false, "", new ArrayList<>(), value);
                Set<NodeArtist> artistResults = artistTree.searchAll(new NodeArtist(new Song("", "", "", artistAttributes)));
                for (NodeArtist node : artistResults) {
                    resultSet.add(node.songsSavedByArtistName.get(0).getArtistName());
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported search type: " + type);
        }

        return resultSet;
    }
}
