package org.music.view;

import java.util.List;

import org.music.controller.MusicOrganizerController;
import org.music.model.Album;
import org.music.model.AlbumObserver;
import org.music.model.SoundClip;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AlbumWindow extends Stage implements AlbumObserver {

    private final Album album;
    private final MusicOrganizerController controller;
    private final SoundClipListView soundClipListView;

    public AlbumWindow(Album album, MusicOrganizerController controller) {
        this.album = album;
        this.controller = controller;
        
        // Register as observer for the album
        album.addObserver(this);
        
        BorderPane mainPane = new BorderPane();
        
        // Set window title as the album name
        this.setTitle(album.getName());
        
        // Create the sound clip list view
        soundClipListView = new SoundClipListView();
        soundClipListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        soundClipListView.display(album);
        mainPane.setCenter(soundClipListView);
        
        // Setup double-click to play functionality
        soundClipListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    List<SoundClip> selectedClips = soundClipListView.getSelectedClips();
                    if (!selectedClips.isEmpty()) {
                        controller.playSoundClipsFromList(selectedClips);
                    }
                }
            }
        });
        
        Scene scene = new Scene(mainPane, 400, 300);
        this.setScene(scene);
        
        // Handle window close event
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                close();
            }
        });
    }

    @Override
    public void close() {
        album.removeObserver(this);
        super.close();
    }

    @Override
    public void onSoundClipsChanged(Album album) {
        Platform.runLater(() -> soundClipListView.display(album));
    }

    @Override
    public void onAlbumDeleted(Album album) {
        Platform.runLater(this::close);
    }
}