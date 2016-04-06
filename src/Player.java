import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {

    Slider time;
    Slider vol;
    MediaPlayer player;

    public void play(String path) {
        try {
            MediaPlayer.Status status = player.getStatus();
            if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.STOPPED) {
                player.play();
            } else {
                stop();
                player = new MediaPlayer(new Media(path));
                player.play();
            }
        } catch (Exception ex) {
            player = new MediaPlayer(new Media(path));
            player.play();
        }

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
        player.pause();
    }

    public void stop(){
        player.stop();
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
}
