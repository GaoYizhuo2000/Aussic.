package au.edu.anu.Aussic.models.avl;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.models.entity.Song;

public class NodeArtist implements Comparable<NodeArtist> {
    String artistName;
    public List<Song> songsSavedByArtistName;

    public NodeArtist(Song song) {
        this.artistName = song.getArtistName();
        this.songsSavedByArtistName = new ArrayList<>();
        this.songsSavedByArtistName.add(song);
    }

    @Override
    public int compareTo(NodeArtist other) {
        return this.artistName.compareTo(other.artistName);
    }
}
