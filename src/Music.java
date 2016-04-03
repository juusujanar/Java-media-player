
public class Music {

    private String song_name;
    private String length;
    protected String path;

    Music(String song_name, String length, String path){
        this.song_name = song_name;
        this.length = length;
        this.path = path;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
