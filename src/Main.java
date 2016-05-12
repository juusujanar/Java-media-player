import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class Main extends Application{

    Stage window;
    Player player;
    TableView<Music> table;
    public ObservableList<Music> music = FXCollections.observableArrayList();

    private ArrayList<String> watchFolders = new ArrayList<>();
    private ArrayList<String> musicFolders = new ArrayList<String>();

    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage) {
        window = primaryStage;

        HBox infoHbox = new HBox();
        Label currently_playing = new Label("Enjoy your music");
        currently_playing.setFont(new Font(20));
        infoHbox.getChildren().add(currently_playing);
        table = new TableView<>();
        player = new Player(currently_playing, table);

        window.setTitle("Music player");
        window.getIcons().add(new Image("file:mylogo.jpg"));

        VBox layout = new VBox();
        HBox MediaHbox = new HBox();
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(20, 10, 10, 10));
        gridpane.setVgap(5);
        gridpane.setHgap(20);

        Menu filemenu = new Menu("File");                         // Making menus
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> window.close());
        filemenu.getItems().add(exitItem);

        Menu playbackmenu = new Menu("Playback");
        CheckMenuItem shuffle = new CheckMenuItem("Shuffle");
        CheckMenuItem repeat = new CheckMenuItem("Repeat");
        shuffle.setOnAction(event1 -> player.setShuffle(shuffle, repeat));
        repeat.setOnAction(event1 -> player.setRepeat(shuffle, repeat));
        playbackmenu.getItems().addAll(shuffle, repeat);

        // Folder menu for adding/editing/scanning watch folders
        Menu foldermenu = new Menu("Folders");
        MenuItem editFolder = new MenuItem("Add music folders");
        MenuItem scan = new MenuItem("Scan current folders");
        editFolder.setOnAction(event -> addFolder());
        scan.setOnAction(event -> scanFolder());
        foldermenu.getItems().addAll(editFolder, scan);

        // MENUBAR CREATION
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(filemenu, foldermenu, playbackmenu);
        layout.getChildren().addAll(menuBar, gridpane);
        ColumnConstraints columnConstraints = new ColumnConstraints();  // Making table scalable
        columnConstraints.setFillWidth(true);
        columnConstraints.setHgrow(Priority.ALWAYS);;
        gridpane.getColumnConstraints().add(columnConstraints);


        Button play = new Button("play");                       // Making MediaBar
        play.setOnAction(event -> start_playing());
        Button pause = new Button("pause");
        pause.setOnAction(event -> player.pause());
        Button stop = new Button("stop");
        stop.setOnAction(event -> player.stop());
        Slider time = player.getTimeSlider();
        Label volume = new Label("Volume: ");
        Slider vol = player.getVolSlider();

        vol.setPrefWidth(100);
        vol.setMinWidth(30);
        vol.setValue(100);
        MediaHbox.setHgrow(time, Priority.ALWAYS);
        play.setMinWidth(30);
        pause.setMinWidth(30);
        stop.setMinWidth(30);

        MediaHbox.getChildren().addAll(play, pause, stop, time, volume, vol);


        table.setItems(getMusic());        // making the table
        table.setPrefSize(600, 2000);

        TableColumn<Music, String> namecolumn = new TableColumn<>("Title");
        namecolumn.setMinWidth(300);
        namecolumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Music, String> artistcolumn = new TableColumn<>("Artist");
        artistcolumn.setMinWidth(300);
        artistcolumn.setCellValueFactory(new PropertyValueFactory<>("artist"));

        TableColumn<Music, String> lengthcolumn = new TableColumn<>("Duration");
        lengthcolumn.setMinWidth(50);
        lengthcolumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        table.getColumns().addAll(namecolumn, artistcolumn, lengthcolumn);
        gridpane.setConstraints(table, 0, 2);
        gridpane.setConstraints(MediaHbox, 0, 1);
        gridpane.setConstraints(infoHbox, 0, 0);

        gridpane.getChildren().addAll(MediaHbox, table, infoHbox);   //showing window

        Scene scene = new Scene(layout, 800, 600);
        window.setScene(scene);
        window.show();

        }


    protected ObservableList<Music> getMusic(){

        try {
            File musicfile = new File("AddedFolders.txt");
            Scanner sc = new Scanner(musicfile, "UTF-8");
            while (sc.hasNextLine()) {
                musicFolders.add(sc.nextLine());
            }
        } catch (IOException e) {}
        scanFolder(musicFolders);
        return music;
    }

    private void start_playing() {
        try {
            Music musicSelected = table.getSelectionModel().getSelectedItems().get(0);
            int index = 0;
            for (Object item : table.getItems()){
                if (item == musicSelected)
                    break;
                else
                    index++;
            }
            player.play(musicSelected.getURI(), musicSelected.getTitle(), musicSelected.getArtist(), index);
        }
        catch (NullPointerException e) {
            System.out.println("No music file is selected.");
        }
        catch (Exception e) {
            System.out.println("Something else went wrong. Printing stack trace.");
            e.printStackTrace();
        }

    }

    public void scanFolder() {
        try {
            PrintWriter writer = new PrintWriter("AddedFolders.txt", "UTF-8");
            for (String path : watchFolders) {
                writer.println(path);
                try {
                    File directory = new File(path);
                    for (File file : directory.listFiles()) {
                        if (file.getName().endsWith((".mp3"))) {
                            music.add(new Music(file.getAbsolutePath(), file.toURI().toURL().toExternalForm(), table));
                        }
                    }
                } catch (NullPointerException e) {
                    System.out.println("One of the folders was not found: " + watchFolders.toString());
                } catch (Exception e) {
                    System.out.println("Application just threw up. Better find someone to clean it up.");
                    e.printStackTrace();
                }
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanFolder(ArrayList<String> folders){
        for (String path : folders) {
            try {
                File directory = new File(path);
                for (File file : directory.listFiles()) {
                    if (file.getName().endsWith((".mp3"))) {
                        music.add(new Music(file.getAbsolutePath(), file.toURI().toURL().toExternalForm(), table));
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("One of the folders was not found: " + watchFolders.toString());
            } catch (Exception e) {
                System.out.println("Application just threw up. Better find someone to clean it up.");
                e.printStackTrace();
            }
        }
    }

    public void addFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Adding music folder");
        File file = directoryChooser.showDialog(null);
        if (file != null) {
            watchFolders.add(file.toString());
            scanFolder();
        }
    }

}
