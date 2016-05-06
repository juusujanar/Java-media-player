import javafx.scene.control.TableView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Music implements Runnable {

    protected String title;
    protected String artist;
    protected String duration;
    protected String path;
    MediaPlayer player;
    TableView<Music> table;
    String length;

    Music(String path, TableView<Music> table) {
        this.path = path;
        this.table = table;
        player = new MediaPlayer(new Media(path));
        player.setOnReady(this);

        try {
            InputStream input = new FileInputStream(new File(path.substring(8)));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();
            if (metadata.get("title")==null || metadata.get("title")=="") {
                while (path.indexOf("/") >= 0) {
                    int index = path.indexOf("/");
                    path = path.substring(index + 1);
                }
                this.title = path.substring(0, path.length() - 4);
            } else
                this.title = metadata.get("title");
            this.artist = metadata.get("Author");
        } catch (Exception e)   {  e.printStackTrace();    }
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
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
        StringBuilder sb = new StringBuilder();
        sb.append(minutes);
        sb.append(":");
        sb.append(seconds);
        length = sb.toString();
        setLength(length);
    }
}