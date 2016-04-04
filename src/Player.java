import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {

    MediaPlayer player;
    String path;

    public void play(String path) {
        try {
            MediaPlayer.Status status = player.getStatus();
            if ((status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.STOPPED) &&  path == this.path) {
                player.play();
            } else {
                this.path = path;
                stop();
                Media media = new Media(path);
                player = new MediaPlayer(media);
                player.play();
            }
        } catch (Exception ex) {
            this.path = path;
            Media media = new Media(path);
            player = new MediaPlayer(media);
            player.play();
        }
    }

    public void pause(){
        player.pause();
    }

    public void stop(){
        player.stop();
    }
}
