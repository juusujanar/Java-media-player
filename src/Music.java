import javafx.scene.media.Media;
import javafx.util.Duration;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.Map;


public class Music {

    private String song_name;
    private String duration;
    protected String path;

    Music(String song_name, String path) {
        this.song_name = song_name;
        this.path = path;
        this.duration = getAudioDuration(path);
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

    public void setLength(String length) {
        this.duration = length;
    }

    private String getAudioDuration(String path) {
        File file = new File(path);
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            Map<?,?> properties = fileFormat.properties();
            System.out.println(properties);
            System.out.println(properties.get("duration"));
        } catch (Exception ex){System.out.println(ex);};
        return "2";
    }
}
