import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {

    private Slider time;
    private Slider vol;
    private MediaPlayer player;
    private Label currently_playing;
    private TableView<Music> table;
    private String title;
    private String artist;


    Player(Label currently_playing, TableView<Music> table) {
        this.currently_playing = currently_playing;
        this.table = table;
    }

    public void play(String path, String title, String artist) {
        try {
            MediaPlayer.Status status = player.getStatus();
            if (player.getStatus() != MediaPlayer.Status.PAUSED) {
                this.title = title;
                this.artist = artist;
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
            player = new MediaPlayer(new Media(path));
            player.play();
        }
        set_playing_text();


        player.currentTimeProperty().addListener(new InvalidationListener() {        //For slider movements
            @Override
            public void invalidated(Observable observable) {
                updateTimeSlider();
            }
        });

        time.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (time.isPressed()){
                    player.seek(player.getMedia().getDuration().multiply(time.getValue()/100));
                }
            }
        });

        vol.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable){
                if (vol.isPressed())
                    player.setVolume(vol.getValue()/100);
            }
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
        Platform.runLater(new Runnable(){
            public void run(){
                time.setValue(player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis() * 100);
            }
        });
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
}
