package au.edu.anu.Aussic.models.search;

import au.edu.anu.Aussic.models.entity.Song;

public class MusicSearchEngine {
    private List<Song> songs;

    public MusicSearchEngine(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> search(Query query) {
        // Implement search logic here
    }
}
