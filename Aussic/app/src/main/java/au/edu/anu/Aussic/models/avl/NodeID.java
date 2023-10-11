package au.edu.anu.Aussic.models.avl;

import au.edu.anu.Aussic.models.entity.Song;

public class NodeID implements Comparable<NodeID> {
    String id;
    public Song song;

    public NodeID(Song song) {
        this.id = song.getId();
        this.song = song;
    }

    @Override
    public int compareTo(NodeID other) {
        return this.id.compareTo(other.id);
    }
}
