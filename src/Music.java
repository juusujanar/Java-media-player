import javafx.scene.control.TableView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music implements Runnable{

    protected String song_name;
    protected String duration;
    protected String path;
    MediaPlayer player;
    TableView<Music> table;
    String length;

    Music(String song_name, String path, TableView<Music> table) {
        this.song_name = song_name;
        this.path = path;
        this.table = table;
        player = new MediaPlayer(new Media(path));
        player.setOnReady(this);
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getDuration() {
        return duration;
    }

    private void setLength(String length) {
        this.duration = length;
        table.refresh();
    }

    @Override
    public void run() {
        int time = (int) Math.round(player.getTotalDuration().toSeconds()); //time in seconds
        int minutes = time / 60;
        int seconds = time - 3 * 60;
        System.out.println(minutes);
        System.out.println(seconds);
        StringBuilder sb = new StringBuilder();
        sb.append(minutes);
        sb.append(":");
        sb.append(seconds);
        length = sb.toString();
        setLength(length);
    }
}