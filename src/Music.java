/*

	AJ Music - a simple music player
    Copyright (C)  2016  Janar Juusu, Andreas Baum

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.


*/

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

    private String title;
    private String artist;
    private String duration;
    private String path;
    private String uri;
    MediaPlayer player;
    TableView<Music> table;
    String length;

    Music(String path, String fullpath, TableView<Music> table) {
        this.path = path;
        this.uri = fullpath;
        this.table = table;
        player = new MediaPlayer(new Media(fullpath));
        player.setOnReady(this);

        try {
            InputStream input = new FileInputStream(new File(path));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();
            if (metadata.get("title")==null || metadata.get("title").equals("")) {
                int index;
                while ((index = path.indexOf("\\")) >= 0) {
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

    public String getPath() {
        return path;
    }

    public String getURI() { return uri; }

    private void setLength(String length) {
        this.duration = length;
        table.refresh();
    }

    @Override
    public void run() {
        int time = (int) Math.round(player.getTotalDuration().toSeconds()); //time in seconds
        int minutes = time / 60;
        int seconds = time - minutes * 60;
        StringBuilder sb = new StringBuilder();
        sb.append(minutes);
        sb.append(":");
        if(seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);
        length = sb.toString();
        setLength(length);
    }
}