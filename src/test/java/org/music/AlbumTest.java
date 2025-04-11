package org.music;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.music.model.Album;
import org.music.model.SoundClip;

class AlbumTest {
    private Album rootAlbum;
    private Album musicAlbum;
    private Album classicalAlbum;
    private SoundClip clip1;
    private SoundClip clip2;
    private SoundClip clip3;

    @BeforeEach
    void setUp() {
        rootAlbum = new Album("All Sound Clips");
        musicAlbum = new Album("Music", rootAlbum);
        classicalAlbum = new Album("Classical Music", musicAlbum);

        rootAlbum.addChildAlbum(musicAlbum);
        musicAlbum.addChildAlbum(classicalAlbum);

        clip1 = new SoundClip(new File("clip1.wav"));
        clip2 = new SoundClip(new File("clip2.wav"));
        clip3 = new SoundClip(new File("clip3.wav"));

        rootAlbum.addSoundClip(clip1);
        musicAlbum.addSoundClip(clip2);
        classicalAlbum.addSoundClip(clip3);
    }

    @Test
    void getName() {
        assertEquals("All Sound Clips", rootAlbum.getName());
        assertEquals("Music", musicAlbum.getName());
        assertEquals("Classical Music", classicalAlbum.getName());
    }

    @Test
    void getParentAlbum() {
        assertNull(rootAlbum.getParentAlbum(), "Root album should have no parent");
        assertEquals(rootAlbum, musicAlbum.getParentAlbum(), "Music album's parent should be root album");
        assertEquals(musicAlbum, classicalAlbum.getParentAlbum(), "Classical album's parent should be music album");
    }

    @Test
    void getChildAlbums() {
        List<Album> rootChildren = rootAlbum.getChildAlbums();
        List<Album> musicChildren = musicAlbum.getChildAlbums();
        List<Album> classicalChildren = classicalAlbum.getChildAlbums();

        assertEquals(1, rootChildren.size(), "Root should have 1 child");
        assertTrue(rootChildren.contains(musicAlbum), "Root should contain music album");

        assertEquals(1, musicChildren.size(), "Music album should have 1 child");
        assertTrue(musicChildren.contains(classicalAlbum), "Music album should contain classical album");

        assertEquals(0, classicalChildren.size(), "Classical album should have no children");
    }

    @Test
    void isRoot() {
        assertTrue(rootAlbum.isRoot(), "Root album should be root");
        assertFalse(musicAlbum.isRoot(), "Music album should not be root");
        assertFalse(classicalAlbum.isRoot(), "Classical album should not be root");
    }

    @Test
    void addChildAlbum() {
        Album jazzAlbum = new Album("Jazz", musicAlbum);

        musicAlbum.addChildAlbum(jazzAlbum);
        assertTrue(musicAlbum.getChildAlbums().contains(jazzAlbum), "Music album should contain jazz album after adding");
        assertEquals(musicAlbum, jazzAlbum.getParentAlbum(), "Jazz album's parent should be music album");
    }

    @Test
    void removeChildAlbum() {
        musicAlbum.removeChildAlbum(classicalAlbum);
        assertFalse(musicAlbum.getChildAlbums().contains(classicalAlbum),
                "Music album should not contain classical album after removal");
        assertNull(classicalAlbum.getParentAlbum(), "Classical album should have null parent after removal");
    }

    @Test
    void containsAlbum() {
        assertTrue(rootAlbum.containsAlbum(musicAlbum), "Root should directly contain music album");
        assertTrue(musicAlbum.containsAlbum(classicalAlbum), "Music should directly contain classical album");

        assertTrue(rootAlbum.containsAlbum(classicalAlbum), "Root should contain classical album recursively");

        assertFalse(classicalAlbum.containsAlbum(rootAlbum), "Classical should not contain root");
        assertFalse(musicAlbum.containsAlbum(rootAlbum), "Music should not contain root");
    }

    @Test
    void getSoundClips() {
        List<SoundClip> rootClips = rootAlbum.getSoundClips();
        List<SoundClip> musicClips = musicAlbum.getSoundClips();
        List<SoundClip> classicalClips = classicalAlbum.getSoundClips();

        assertEquals(3, rootClips.size(), "Root should have 3 clips");
        assertEquals(2, musicClips.size(), "Music should have 2 clips");
        assertEquals(1, classicalClips.size(), "Classical should have 1 clip");
    }

    @Test
    void addSoundClip() {
        SoundClip clip4 = new SoundClip(new File("clip4.wav"));
        classicalAlbum.addSoundClip(clip4);

        assertTrue(classicalAlbum.containsSoundClip(clip4), "Classical should contain clip4");
        assertTrue(musicAlbum.containsSoundClip(clip4), "Music should contain clip4");
        assertTrue(rootAlbum.containsSoundClip(clip4), "Root should contain clip4");
    }

    @Test
    void removeSoundClip() {
        classicalAlbum.removeSoundClip(clip3);
        assertFalse(classicalAlbum.containsSoundClip(clip3), "Classical should not contain clip3 after removal");
    }

    @Test
    void containsSoundClip() {
        assertTrue(rootAlbum.containsSoundClip(clip1), "Root should contain clip1");
        assertTrue(musicAlbum.containsSoundClip(clip2), "Music should contain clip2");
        assertTrue(classicalAlbum.containsSoundClip(clip3), "Classical should contain clip3");

        assertTrue(rootAlbum.containsSoundClip(clip2), "Root should contain inherited clip2");
        assertTrue(rootAlbum.containsSoundClip(clip3), "Root should contain inherited clip3");
        assertTrue(musicAlbum.containsSoundClip(clip3), "Music should contain inherited clip3");

        assertFalse(classicalAlbum.containsSoundClip(clip1), "Classical should not contain clip1");
        assertFalse(classicalAlbum.containsSoundClip(clip2), "Classical should not contain clip2");
    }
}
