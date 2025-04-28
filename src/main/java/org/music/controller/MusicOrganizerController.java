package org.music.controller;

import java.util.List;
import java.util.Set;

import org.music.model.Album;
import org.music.model.SoundClip;
import org.music.model.SoundClipBlockingQueue;
import org.music.model.SoundClipLoader;
import org.music.model.SoundClipPlayer;
import org.music.view.AlbumWindow;
import org.music.view.MusicOrganizerWindow;

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
    public void addNewAlbum() {
        Album selectedAlbum = view.getSelectedAlbum();
        if (selectedAlbum == null) {
            view.displayMessage("Please select an album");
            return;
        }

        String albumName = view.promptForAlbumName();
        if (albumName == null || albumName.trim().isEmpty()) {
            view.displayMessage("Album name cannot be empty");
            return;
        }

        Album newAlbum = new Album(albumName, selectedAlbum);
        selectedAlbum.addChildAlbum(newAlbum);

        view.onAlbumAdded(newAlbum);
        view.displayMessage("Album " + albumName + " added");
    }

    /**
     * Removes an album from the Music Organizer
     */
    public void deleteAlbum() {
        Album selectedAlbum = view.getSelectedAlbum();
        if (selectedAlbum == null) {
            view.displayMessage("Please select an album");
            return;
        }

        if (selectedAlbum.isRoot()) {
            view.displayMessage("Cannot delete the root album");
            return;
        }

        Album parent = selectedAlbum.getParentAlbum();
        parent.removeChildAlbum(selectedAlbum);

        view.onAlbumRemoved();
        view.displayMessage("Album " + selectedAlbum.getName() + " removed");
    }

    /**
     * Adds sound clips to an album
     */
    public void addSoundClips() {
        Album selectedAlbum = view.getSelectedAlbum();
        if (selectedAlbum == null) {
            view.displayMessage("Select an album");
            return;
        }

        List<SoundClip> selectedClips = view.getSelectedSoundClips();
        if (selectedClips.isEmpty()) {
            view.displayMessage("Select sound clips");
            return;
        }

        for (SoundClip clip : selectedClips) {
            selectedAlbum.addSoundClip(clip);
        }

        view.onClipsUpdated();
        view.displayMessage("Added sound clip(s) to album " + selectedAlbum.getName());
    }

    /**
     * Removes sound clips from an album
     */
    public void removeSoundClips() {
        Album selectedAlbum = view.getSelectedAlbum();
        if (selectedAlbum == null) {
            view.displayMessage("Select an album");
            return;
        }

        List<SoundClip> selectedClips = view.getSelectedSoundClips();
        if (selectedClips.isEmpty()) {
            view.displayMessage("Select sound clips");
            return;
        }

        for (SoundClip clip : selectedClips) {
            if (selectedAlbum.containsSoundClip(clip)) {
                selectedAlbum.removeSoundClip(clip);
            }
        }

        view.onClipsUpdated();
        view.displayMessage("Removed sound clip(s) from album " + selectedAlbum.getName());
    }

    /**
     * Puts the selected sound clips on the queue and lets
     * the sound clip player thread play them. Essentially, when
     * this method is called, the selected sound clips in the
     * SoundClipTable are played.
     */
    public void playSoundClips() {
        List<SoundClip> l = view.getSelectedSoundClips();
        playSoundClipsFromList(l);
    }
    
    /**
     * Plays a list of sound clips.
     */
    public void playSoundClipsFromList(List<SoundClip> soundClips) {
        if (soundClips.isEmpty()) {
            return;
        }
        
        queue.enqueue(soundClips);

        for (SoundClip soundClip : soundClips) {
            if (view != null) {
                view.displayMessage("Playing " + soundClip);
            }
        }
    }
    
    /**
     * Opens a new window for the selected album.
     */
    public void openAlbumWindow() {
        Album selectedAlbum = view.getSelectedAlbum();
        if (selectedAlbum == null) {
            view.displayMessage("Please select an album");
            return;
        }
        
        AlbumWindow albumWindow = new AlbumWindow(selectedAlbum, this);
        albumWindow.show();
        view.displayMessage("Opened new window for album: " + selectedAlbum.getName());
    }
}
