package org.music.view;

import java.util.ArrayList;
import java.util.List;

import org.music.model.Album;
import org.music.model.SoundClip;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class SoundClipListView extends ListView<SoundClip> {

    private List<SoundClip> clips;

    public SoundClipListView() {
        super();
        clips = new ArrayList<>();
    }

    public SoundClipListView(ObservableList<SoundClip> arg0) {
        super(arg0);
        clips = new ArrayList<>();
    }

    /**
     * Displays the contents of the specified albums
     *
     * @param album - the album which contents are to be displayed
     */
    public void display(Album album) {
        this.getItems().clear();
        this.clips.clear();

        this.clips.addAll(album.getSoundClips());

        ObservableList<SoundClip> temp = FXCollections.observableArrayList(clips);
        this.setItems(temp);
    }

    public List<SoundClip> getSelectedClips() {
        ObservableList<SoundClip> items = this.getSelectionModel().getSelectedItems();
        List<SoundClip> clips = new ArrayList<>(items);
        return clips;
    }
}
