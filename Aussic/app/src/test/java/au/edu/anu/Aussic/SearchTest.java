package au.edu.anu.Aussic;



import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

import au.edu.anu.Aussic.models.entity.Song;

import au.edu.anu.Aussic.models.entity.SongAttributes;
import au.edu.anu.Aussic.models.search.MusicSearchEngine;



public class SearchTest {

    private MusicSearchEngine engine;
    private List<Song> testList;

    @Before
    public void setUp() {
        testList = new ArrayList<>();

        Song song1 = new Song("1");
        SongAttributes attributes1 = new SongAttributes("Song1", "Artist1", "2001", Arrays.asList("Pop", "Rock"));
        song1.setAttributes(attributes1);

        Song song2 = new Song("2");
        SongAttributes attributes2 = new SongAttributes("Song2", "Artist2", "2002", Arrays.asList("Classical"));
        song2.setAttributes(attributes2);

        Song song3 = new Song("3");
        SongAttributes attributes3 = new SongAttributes("Song3", "Artist3", "2003", Arrays.asList("Jazz"));
        song3.setAttributes(attributes3);

        Song song4 = new Song("4");
        SongAttributes attributes4 = new SongAttributes("Song4", "Artist4", "2004", Arrays.asList("Rock"));
        song4.setAttributes(attributes4);


        testList.add(song1); //the first object will be deleted after searching.
        testList.add(song1);
        testList.add(song2);
        testList.add(song3);
        testList.add(song4);

        //for(Song e: testList) System.out.println(e);
        engine = new MusicSearchEngine(testList);
        //for(Song e: testList) System.out.println(e);
    }

    @Test
    public void testSearchById() {
        Map<String, String> testTerms = new HashMap<>();
        testTerms.put("id", "2");
        Set<Song> res = engine.search(testTerms);

        //for(Song e: testList) System.out.println(e);

        Song expectedSong = testList.get(1);
        Song actualSong = res.iterator().next();

        assertEquals(1, res.size());
        assertEquals(expectedSong.getId(), actualSong.getId());
        assertEquals(expectedSong.getAttributes(), actualSong.getAttributes());
    }

    @Test
    public void testSearchByGenre() {
        Map<String, String> testTerms = new HashMap<>();
        testTerms.put("genre", "Rock");
        Set<Song> res = engine.search(testTerms);

        assertEquals(2, res.size());
        assertTrue(res.contains(testList.get(0)));
        assertTrue(res.contains(testList.get(3)));
    }

    @Test
    public void testSearchBySongName() {
        Map<String, String> testTerms = new HashMap<>();
        testTerms.put("name", "Song3");
        Set<Song> res = engine.search(testTerms);

        assertEquals(1, res.size());
        assertTrue(res.contains(testList.get(2)));
    }

    @Test
    public void testSearchByArtistName() {
        Map<String, String> testTerms = new HashMap<>();
        testTerms.put("artistName", "Artist1");
        Set<Song> res = engine.search(testTerms);

        assertEquals(1, res.size());
        assertTrue(res.contains(testList.get(0)));
    }

    @Test
    public void testSearchByReleaseDate() {
        Map<String, String> testTerms = new HashMap<>();
        testTerms.put("releaseDate", "2004");
        Set<Song> res = engine.search(testTerms);

        assertEquals(1, res.size());
        assertTrue(res.contains(testList.get(3)));
    }

    @Test
    public void testSearchByMultipleCriteria() {
        Map<String, String> testTerms = new HashMap<>();
        testTerms.put("genre", "Rock");
        testTerms.put("artistName", "Artist1");
        Set<Song> res = engine.search(testTerms);

        assertEquals(1, res.size());
        assertTrue(res.contains(testList.get(0)));
    }

    @Test
    public void testSearchByUndefinedTerm() {
        Map<String, String> testTerms = new HashMap<>();
        testTerms.put("undefinedTerm", "Artist2");
        Set<Song> res = engine.search(testTerms);

        assertEquals(1, res.size());
        assertTrue(res.contains(testList.get(1)));
    }

    @Test
    public void testAddAndSearchSong() {
        Song newSong = new Song("5");
        SongAttributes attributes5 = new SongAttributes("Song5", "Artist5", "2005", Arrays.asList("Blues"));
        newSong.setAttributes(attributes5);
        engine.addSong(newSong);

        Map<String, String> testTerms = new HashMap<>();
        testTerms.put("name", "Song5");
        Set<Song> res = engine.search(testTerms);

        assertEquals(1, res.size());
        assertTrue(res.contains(newSong));
    }
}
