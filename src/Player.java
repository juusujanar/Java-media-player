import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {

    MediaPlayer player;

    public void play(String path){
        try {
            stop();
        } catch (Exception ex) {};
        Media media = new Media(path);
        player = new MediaPlayer(media);
        player.play();
    }

    public void pause(){
        player.pause();
    }

    public void stop(){
        player.stop();
    }
}
