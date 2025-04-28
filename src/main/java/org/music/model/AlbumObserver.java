package org.music.model;

public interface AlbumObserver {
    /**
     * Called when sound clips are added to or deleted from the observed album
     */
    void onSoundClipsChanged(Album album);

    /**
     * Called when the observed album is deleted
     */
    void onAlbumDeleted(Album album);
}
