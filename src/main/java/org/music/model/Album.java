package org.music.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Album {

    private final String name;
    private Album parentAlbum;
    private final List<Album> childAlbums;
    private final List<SoundClip> soundClips;
    private final List<AlbumObserver> observers;

    /**
     * Constructor for creating a root album.
     */
    public Album(String name) {
        this.name = name;
        this.parentAlbum = null;
        this.childAlbums = new ArrayList<>();
        this.soundClips = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    /**
     * Constructor for creating a child album.
     */
    public Album(String name, Album parentAlbum) {
        this(name);
        this.parentAlbum = parentAlbum;
    }

    public String getName() {
        return name;
    }

    public Album getParentAlbum() {
        return parentAlbum;
    }

    /**
     * @return a list of all child albums.
     */
    public List<Album> getChildAlbums() {
        return Collections.unmodifiableList(childAlbums);
    }

    public boolean isRoot() {
        return parentAlbum == null;
    }

    /**
     * Add an observer to this album.
     */
    public void addObserver(AlbumObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Remove an observer from this album.
     */
    public void removeObserver(AlbumObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notify all observers that the sound clips have changed.
     */
    private void notifySoundClipsChanged() {
        for (AlbumObserver observer : observers) {
            observer.onSoundClipsChanged(this);
        }
    }

    /**
     * Notify all observers that this album has been deleted.
     */
    private void notifyAlbumDeleted() {
        for (AlbumObserver observer : new ArrayList<>(observers)) {
            observer.onAlbumDeleted(this);
        }
    }

    /**
     * Add a child album.
     * Requires album != null and not already a child.
     */
    public void addChildAlbum(Album album) {
        assert album != null;

        if (containsAlbum(album)) {
            throw new IllegalArgumentException("Album already contains child album");
        }

        this.childAlbums.add(album);
        album.parentAlbum = this;
    }

    /**
     * Removes a child album.
     * Requires album != null
     */
    public void removeChildAlbum(Album album) {
        assert album != null;

        if (!containsAlbum(album)) {
            throw new IllegalArgumentException("Album doesn't contain child album");
        }

        this.childAlbums.remove(album);
        album.parentAlbum = null;
        album.notifyAlbumDeleted();
    }

    /**
     * Recursively checks if album contains a specific album.
     */
    public boolean containsAlbum(Album album) {
        if (childAlbums.contains(album)) {
            return true;
        }

        for (Album childAlbum : childAlbums) {
            if (childAlbum.containsAlbum(album)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return a list of all sound clips in this album.
     */
    public List<SoundClip> getSoundClips() {
        return Collections.unmodifiableList(soundClips);
    }

    /**
     * Add a sound clip.
     * Requires soundClip != null
     */
    public void addSoundClip(SoundClip soundClip) {
        assert soundClip != null;

        soundClips.add(soundClip);
        notifySoundClipsChanged();

        if (parentAlbum != null) {
            parentAlbum.addSoundClip(soundClip);
        }
    }

    /**
     * Remove a sound clip.
     * Requires soundClip != null
     */
    public void removeSoundClip(SoundClip soundClip) {
        assert soundClip != null;

        soundClips.remove(soundClip);
        notifySoundClipsChanged();

        for (Album childAlbum : childAlbums) {
            childAlbum.removeSoundClip(soundClip);
        }
    }

    /**
     * Checks if album contains a specific sound clip.
     */
    public boolean containsSoundClip(SoundClip soundClip) {
        return soundClips.contains(soundClip);
    }

    @Override
    public String toString() {
        return name;
    }
}
