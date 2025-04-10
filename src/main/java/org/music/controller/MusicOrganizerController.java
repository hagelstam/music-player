package org.music.controller;

import org.music.model.*;
import org.music.view.MusicOrganizerWindow;

import java.util.List;
import java.util.Set;

public class MusicOrganizerController {

    private MusicOrganizerWindow view;
    private SoundClipBlockingQueue queue;
    private Album root;

    public MusicOrganizerController() {
        // Create the root album for all sound clips
        root = new Album("All Sound Clips");

        // Create the blocking queue
        queue = new SoundClipBlockingQueue();

        // Create a separate thread for the sound clip player and start it
        (new Thread(new SoundClipPlayer(queue))).start();
    }

    /**
     * Load the sound clips found in all subfolders of a path on disk. If path is not
     * an actual folder on disk, has no effect.
     */
    public Set<SoundClip> loadSoundClips(String path) {
        Set<SoundClip> clips = SoundClipLoader.loadSoundClips(path);

        // Add the loaded sound clips to the root album
        for (SoundClip clip : clips) {
            root.addSoundClip(clip);
        }

        return clips;
    }

    public void registerView(MusicOrganizerWindow view) {
        this.view = view;
    }

    /**
     * Returns the root album
     */
    public Album getRootAlbum() {
        return root;
    }

    /**
     * Adds an album to the Music Organizer
     */
    public void addNewAlbum(Album currAlbum, String name) {
        Album newAlbum = new Album(name, currAlbum);
        currAlbum.addChildAlbum(newAlbum);
    }

    /**
     * Removes an album from the Music Organizer
     */
    public void deleteAlbum(Album album) {
        if (album.isRoot()) {
            throw new IllegalArgumentException("Cannot delete the root album");
        }

        Album parent = album.getParentAlbum();
        parent.removeChildAlbum(album);
    }

    /**
     * Adds sound clips to an album
     */
    public void addSoundClips() { //TODO Update parameters if needed
        // TODO: Add your code here

    }

    /**
     * Removes sound clips from an album
     */
    public void removeSoundClips() { //TODO Update parameters if needed
        // TODO: Add your code here

    }

    /**
     * Puts the selected sound clips on the queue and lets
     * the sound clip player thread play them. Essentially, when
     * this method is called, the selected sound clips in the
     * SoundClipTable are played.
     */
    public void playSoundClips() {
        List<SoundClip> l = view.getSelectedSoundClips();
        queue.enqueue(l);
        for (int i = 0; i < l.size(); i++) {
            view.displayMessage("Playing " + l.get(i));
        }
    }
}
