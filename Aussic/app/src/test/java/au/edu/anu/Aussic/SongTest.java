package au.edu.anu.Aussic;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import au.edu.anu.Aussic.models.entity.Artwork;
import au.edu.anu.Aussic.models.entity.Preview;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.SongAttributes;


public class SongTest {
    private Song song;
    private SongAttributes songAttributes;

    @Before
    public void setUp() {
        songAttributes = new SongAttributes("Test Song", "Test Artist");
        song = new Song("123", "testType", "testHref", songAttributes);
    }

    @Test
    public void testConstructor() {
        assertEquals("123", song.getId());
        assertEquals("testType", song.getType());
        assertEquals("testHref", song.getHref());
        assertNotNull(song.getAttributes());  // Check if attributes are initialized
    }

    @Test
    public void testSettersAndGetters() {
        song.setId("456");
        assertEquals("456", song.getId());

        song.setType("newType");
        assertEquals("newType", song.getType());

        song.setHref("newHref");
        assertEquals("newHref", song.getHref());
    }

    @Test
    public void testSongAttributesGetters() {
        assertEquals("Test Song", song.getSongName());
        assertEquals("Test Artist", song.getArtistName());
    }

    @Test
    public void testSetAndGetAlbumName() {
        songAttributes.setAlbumName("Test Album");
        assertEquals("Test Album", songAttributes.getAlbumName());
    }

    @Test
    public void testSetAndGetGenreNames() {
        songAttributes.setGenreNames(java.util.Arrays.asList("Pop", "Rock"));
        assertEquals(2, songAttributes.getGenreNames().size());
        assertTrue(songAttributes.getGenreNames().contains("Pop"));
        assertTrue(songAttributes.getGenreNames().contains("Rock"));
    }

    @Test
    public void testSetAndGetTrackNumber() {
        songAttributes.setTrackNumber(5);
        assertEquals(5, songAttributes.getTrackNumber());
    }

    @Test
    public void testSetAndGetDurationInMillis() {
        songAttributes.setDurationInMillis(5000L);
        assertEquals(5000L, songAttributes.getDurationInMillis());
    }

    @Test
    public void testSetAndGetReleaseDate() {
        songAttributes.setReleaseDate("2023-10-17");
        assertEquals("2023-10-17", songAttributes.getReleaseDate());
    }

    @Test
    public void testSetAndGetIsrc() {
        songAttributes.setIsrc("TestISRC");
        assertEquals("TestISRC", songAttributes.getIsrc());
    }

    @Test
    public void testSetAndGetComposerName() {
        songAttributes.setComposerName("Test Composer");
        assertEquals("Test Composer", songAttributes.getComposerName());
    }

    @Test
    public void testSetAndGetUrl() {
        songAttributes.setUrl("http://test.url");
        assertEquals("http://test.url", songAttributes.getUrl());
    }

    @Test
    public void testSetAndGetDiscNumber() {
        songAttributes.setDiscNumber(2);
        assertEquals(2, songAttributes.getDiscNumber());
    }

    @Test
    public void testSetAndGetHasCredits() {
        songAttributes.setHasCredits(true);
        assertTrue(songAttributes.isHasCredits());
    }

    @Test
    public void testSetAndGetHasLyrics() {
        songAttributes.setHasLyrics(true);
        assertTrue(songAttributes.isHasLyrics());
    }

    @Test
    public void testSetAndGetAppleDigitalMaster() {
        songAttributes.setAppleDigitalMaster(true);
        assertTrue(songAttributes.isAppleDigitalMaster());
    }

    @Test
    public void testSetAndGetArtistName() {
        songAttributes.setArtistName("New Artist");
        assertEquals("New Artist", songAttributes.getArtistName());
    }

    @Test
    public void testSetAndGetArtwork() {
        Artwork artwork = new Artwork();

        artwork.setUrl("http://artwork.url");
        songAttributes.setArtwork(artwork);
        assertEquals("http://artwork.url", songAttributes.getArtwork().getUrl());
    }

    @Test
    public void testSetAndGetPreviews() {
        Preview preview1 = new Preview();
        Preview preview2 = new Preview();

        preview1.setUrl("http://preview1.url");
        preview2.setUrl("http://preview2.url");

        songAttributes.setPreviews(Arrays.asList(preview1, preview2));
        assertEquals("http://preview1.url", songAttributes.getPreviews().get(0).getUrl());
        assertEquals("http://preview2.url", songAttributes.getPreviews().get(1).getUrl());
    }


    @Test
    public void testCompareTo() {
        Song anotherSong = new Song("456");
        assertTrue(song.compareTo(anotherSong) < 0);
    }

    @Test
    public void testToString() {
        songAttributes.setAlbumName("Test Album");
        songAttributes.setGenreNames(java.util.Arrays.asList("Pop", "Rock"));
        songAttributes.setTrackNumber(5);
        songAttributes.setDurationInMillis(5000L);
        songAttributes.setReleaseDate("2023-10-17");

        String expected = "Song Attribute {" +
                "albumName='Test Album'" +
                ", genreNames=[Pop, Rock]" +
                ", artistName='Test Artist'" +
                ", name='Test Song'" +
                ", trackNumber=5" +
                ", durationInMillis=5000" +
                ", releaseDate='2023-10-17'" +
                '}';
        assertEquals(expected, songAttributes.toString());
    }

    @Test
    public void testClone() {
        Song clonedSong = song.clone();
        assertEquals(song.getId(), clonedSong.getId());
        assertEquals(song.getType(), clonedSong.getType());
        assertEquals(song.getHref(), clonedSong.getHref());
    }

    @Test
    public void testCloneAdvanced(){
        List<String> genres = Arrays.asList("Pop", "Rock");
        Artwork artwork = new Artwork();
        artwork.setUrl("www.sample-url.com");
        Preview preview = new Preview();
        preview.setUrl("www.sample-url.com");
        List<Preview> previews = Arrays.asList(preview);

        SongAttributes original = new SongAttributes("Sample Album", genres,1, 5000L,"2023-10-17", "ISRC_CODE", artwork, "Composer", "www.sample-url.com", null, 1, true, true, true, "Sample Song", previews, "Sample Artist");
        SongAttributes cloned = original.clone();

        assertNotSame(original, cloned);//The cloned object must not be the same reference
        assertEquals(original.getAlbumName(), cloned.getAlbumName());//Album names should be equal
        assertNotSame(original.getGenreNames(), cloned.getGenreNames());//Genre lists must not be the same reference
        assertEquals(original.getGenreNames(), cloned.getGenreNames());//Genre lists should be equal
        assertNotSame(original.getArtwork(), cloned.getArtwork());//Artworks must not be the same reference
        assertEquals(original.getArtwork().getUrl(), cloned.getArtwork().getUrl());//"Artwork URLs should be equal
        assertNotSame(original.getPreviews(), cloned.getPreviews());//Previews lists must not be the same reference
        assertEquals(original.getPreviews().get(0).getUrl(), cloned.getPreviews().get(0).getUrl());//Previews lists must not be the same reference
    }

    @Test
    public void testSetSong() {
        Song anotherSong = new Song("123", "setType2", "setHref2", new SongAttributes("SetName", "SetArtist"));
        song.setSong(anotherSong);

        assertEquals("123", song.getId());
        assertEquals("setType2", song.getType());
        assertEquals("setHref2", song.getHref());
        assertEquals("SetName", song.getSongName());
        assertEquals("SetArtist", song.getArtistName());
    }
}
