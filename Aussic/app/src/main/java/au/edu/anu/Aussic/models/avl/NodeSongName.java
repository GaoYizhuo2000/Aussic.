package au.edu.anu.Aussic.models.avl;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.models.entity.Song;

public class NodeSongName implements Comparable<NodeSongName> {
    String name;
    public List<Song> songsSavedByName;

    public NodeSongName(Song song) {
        this.name = song.getSongName();
        this.songsSavedByName = new ArrayList<>();
        this.songsSavedByName.add(song);
    }

    @Override
    public int compareTo(NodeSongName other) {
        return this.name.compareTo(other.name);
    }
}