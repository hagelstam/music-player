package org.music.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MusicPlayerTest {
    private MusicPlayer musicPlayer;

    @BeforeEach
    void setUp() {
        musicPlayer = new MusicPlayer();
    }

    @Test
    void getRootAlbum() {
        Album root = musicPlayer.getRootAlbum();
        assertNotNull(root);
        assertEquals("All Sound Clips", root.getName());
    }

    @Test
    void loadSoundClips(@TempDir File tempDir) throws IOException {
        File wavFile = new File(tempDir, "sound1.wav");
        assertTrue(wavFile.createNewFile());

        File subDir = new File(tempDir, "subdir");
        assertTrue(subDir.mkdir());
        File subWav = new File(subDir, "sound2.wav");
        assertTrue(subWav.createNewFile());

        musicPlayer.loadSoundClips(tempDir);
        List<SoundClip> soundClips = musicPlayer.getAllSoundClips();

        assertEquals(2, soundClips.size());
        Album root = musicPlayer.getRootAlbum();
        assertEquals(2, root.getSoundClips().size());
    }

    @Test
    void createAlbum() {
        Album newAlbum = musicPlayer.createAlbum("My Album");

        assertNotNull(newAlbum);
        assertEquals("My Album", newAlbum.getName());
        assertEquals(musicPlayer.getRootAlbum(), newAlbum.getParentAlbum());
        assertTrue(musicPlayer.getRootAlbum().getChildAlbums().contains(newAlbum));
    }

    @Test
    void deleteAlbum() {
        Album newAlbum = musicPlayer.createAlbum("Test1");
        assertTrue(musicPlayer.getRootAlbum().getChildAlbums().contains(newAlbum));

        musicPlayer.deleteAlbum(newAlbum);
        assertFalse(musicPlayer.getRootAlbum().getChildAlbums().contains(newAlbum));
    }

    @Test
    void getAllSoundClips() {
        assertTrue(musicPlayer.getAllSoundClips().isEmpty());
    }
}
