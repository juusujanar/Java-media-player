import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Random;


public class Player implements Runnable{

    private Slider time;
    private Slider vol;
    private MediaPlayer player;
    private Label currently_playing;
    private TableView<Music> table;
    private String title;
    private String artist;
    private int song_index;
    private boolean shuffle = false;
    private boolean repeat = false;

    Player(Label currently_playing, TableView<Music> table) {
        this.currently_playing = currently_playing;
        this.table = table;
    }

    public void play(String path, String title, String artist, int song_index) {
        try {
            MediaPlayer.Status status = player.getStatus();
            if (player.getStatus() != MediaPlayer.Status.PAUSED) {
                this.title = title;
                this.artist = artist;
                this.song_index = song_index;
            }
            if (status == MediaPlayer.Status.PAUSED) {
                player.play();
            } else {
                stop();
                player = new MediaPlayer(new Media(path));
                player.play();
            }
        } catch (Exception ex) {       //comes here if there is no status yet
            this.title = title;
            this.artist = artist;
            this.song_index = song_index;
            player = new MediaPlayer(new Media(path));
            player.play();
        }
        set_playing_text();
        player.setOnEndOfMedia(this);


        player.currentTimeProperty().addListener(observable -> {
            updateTimeSlider();
        });

        time.valueProperty().addListener(observable -> {
            if (time.isPressed()){
                player.seek(player.getMedia().getDuration().multiply(time.getValue()/100));
            }
        });

        vol.valueProperty().addListener(observable -> {
            if (vol.isPressed())
                player.setVolume(vol.getValue()/100);
        });
    }


    public void pause(){
        try {
            player.pause();
            if ((artist == null || artist.equals("")))
                currently_playing.setText("Currently paused:   " + title);
            else
                currently_playing.setText("Currently paused:   " + title + " - " + artist);
        } catch (NullPointerException e) {};
    }

    public void stop(){
        try {
            player.stop();
            currently_playing.setText("Currently stopped");
        } catch (NullPointerException e) {};

    }

    private void updateTimeSlider(){
        Platform.runLater(() -> time.setValue(player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100));
    }

    protected Slider getTimeSlider(){
        time = new Slider();
        return time;
    }

    protected Slider getVolSlider(){
        vol = new Slider();
        return vol;
    }

    private void set_playing_text() {
        if ((artist == null || artist.equals("")))
            currently_playing.setText("Currently playing:   " + title);
        else
            currently_playing.setText("Currently playing:   " + title + " - " + artist);
    }

    public void run(){
        Music next_song;
         if (shuffle) {
             int nextindex;
             do {
                 Random random = new Random();
                 nextindex = random.nextInt(table.getItems().size());
             } while (song_index == nextindex);
             next_song = table.getItems().get(nextindex);
             play(next_song.getURI(), next_song.getTitle(), next_song.getArtist(), nextindex);
         } else if (repeat) {
             next_song = table.getItems().get(song_index);
             play(next_song.getURI(), next_song.getTitle(), next_song.getArtist(), song_index);
         } else {
            try {
                next_song = table.getItems().get(song_index + 1);
            } catch (IndexOutOfBoundsException e) {
                next_song = table.getItems().get(0);
                song_index = -1;
            }
            play(next_song.getURI(), next_song.getTitle(), next_song.getArtist(), song_index + 1);
        }
    }

    protected void setShuffle(CheckMenuItem shuffle, CheckMenuItem repeat) {
        if (shuffle.isSelected()) {
            this.shuffle = true;
            this.repeat = false;
            repeat.setSelected(false);
        } else {
            this.shuffle = false;
        }
    }

    protected void setRepeat(CheckMenuItem shuffle, CheckMenuItem repeat) {
        if (repeat.isSelected()) {
            this.repeat = true;
            this.shuffle = false;
            shuffle.setSelected(false);
        } else {
            this.repeat = false;
        }
    }
}