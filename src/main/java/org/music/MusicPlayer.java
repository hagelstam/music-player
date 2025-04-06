package org.music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MusicPlayer {
    private final Album rootAlbum;
    private final List<SoundClip> allSoundClips;

    public MusicPlayer() {
        this.rootAlbum = new Album("All Sound Clips");
        this.allSoundClips = new ArrayList<>();
    }

    public Album getRootAlbum() {
        return rootAlbum;
    }

    public void loadSoundClips(File directory) {
        if (directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isDirectory()) {
                    loadSoundClips(file);
                } else if (file.getName().toLowerCase().endsWith(".wav")) {
                    SoundClip soundClip = new SoundClip(file);
                    allSoundClips.add(soundClip);
                    rootAlbum.addSoundClip(soundClip);
                }
            }
        }
    }

    public Album createAlbum(String name) {
        Album newAlbum = new Album(name, rootAlbum);
        rootAlbum.addChildAlbum(newAlbum);
        return newAlbum;
    }

    public void deleteAlbum(Album album) {
        if (album.isRoot()) {
            throw new IllegalArgumentException("Cannot delete the root album");
        }

        Album parent = album.getParentAlbum();
        parent.removeChildAlbum(album);
    }

    public List<SoundClip> getAllSoundClips() {
        return allSoundClips;
    }
}
