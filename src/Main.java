import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;


public class Main extends Application{

    Stage window;
    Player player;
    TableView<Music> table;
    public ObservableList<Music> music;

    ArrayList<File> watchFolders = new ArrayList<>();

    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Mp3 player");
        player = new Player();

        VBox layout = new VBox();
        HBox MediaHbox = new HBox();
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(20, 10, 10, 10));


        Menu filemenu = new Menu("File");                         //Making menus
        MenuItem addsongItem = new MenuItem("Add song");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> window.close());
        filemenu.getItems().addAll(addsongItem, exitItem);

        // Folder menu for adding/editing/scanning watch folders
        Menu foldermenu = new Menu("Folders");
        MenuItem editFolder = new MenuItem("Edit watch folders");
        MenuItem scan = new MenuItem("Scan folders");
        //editFolder.setOnAction(event -> folderedit());
        //scan.setOnAction(event -> scanFolders());
        foldermenu.getItems().addAll(editFolder, scan);

        // MENUBAR CREATION
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(filemenu, foldermenu);
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


        table = new TableView<>();                        //making table
        table.setItems(getMusic());
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
        gridpane.setConstraints(table, 0, 1);
        gridpane.setConstraints(MediaHbox, 0, 0);


        gridpane.getChildren().addAll(MediaHbox, table);   //showing window
        Scene scene = new Scene(layout, 800, 600);
        window.setScene(scene);
        window.show();

        //watchFolders.add(new File("C:\\Users\\Janar\\Desktop"));
        ArrayList<String> files = scanFolder();
        System.out.println(files);
    }


    protected ObservableList<Music> getMusic(){
        music = FXCollections.observableArrayList();
        music.add(new Music("file:///C:/Music/years.mp3", table));
        music.add(new Music("file:///C:/Music/laul.mp3", table));
        music.add(new Music("file:///C:/Music/MyHumps.mp3", table));
        return music;
    }

    private void start_playing() {
        ObservableList<Music> musicSelected = table.getSelectionModel().getSelectedItems();
        player.play(musicSelected.get(0).path);
    }

    public ArrayList<String> scanFolder(){
        ArrayList<String> music = new ArrayList<>();
        for(File dir : watchFolders) {
            try {
                for (File file : dir.listFiles()) {
                    if (file.getName().endsWith((".mp3"))) {
                        music.add(file.getAbsolutePath());
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return music;
    }

}
