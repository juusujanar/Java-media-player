import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {

    public void play(String path){
        Media media = new Media(path);
        MediaPlayer player = new MediaPlayer(media);
        player.play();
    }
}
