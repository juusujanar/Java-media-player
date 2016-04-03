import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Main extends Application{

    Stage window;
    Player player;
    TableView<Music> table;

    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage){
        window = primaryStage;
        window.setTitle("Mp3 player");
        player = new Player();

        BorderPane layout = new BorderPane();
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(10, 10, 10, 10));

        Menu filemenu = new Menu("File");
        MenuItem addsongItem = new MenuItem("Add song");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> window.close());
        filemenu.getItems().addAll(addsongItem, exitItem);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(filemenu);
        layout.setTop(menuBar);
        layout.setCenter(gridpane);

        Button play = new Button("play");
        play.setOnAction(event -> mängi());
        Button pause = new Button("pause");
        Button stop = new Button("stop");

        gridpane.setConstraints(play, 0, 0);
        gridpane.setConstraints(pause, 1, 0);
        gridpane.setConstraints(stop, 2, 0);

        table = new TableView<>();
        table.setItems(getMusic());

        TableColumn<Music, String> namecolumn = new TableColumn<>("Name");
        namecolumn.setMinWidth(200);
        namecolumn.setCellValueFactory(new PropertyValueFactory<>("song_name"));

        TableColumn<Music, String> lengthcolumn = new TableColumn<>("Length");
        lengthcolumn.setMinWidth(50);
        lengthcolumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        table.getColumns().addAll(namecolumn, lengthcolumn);
        gridpane.setConstraints(table, 0, 1, 10, 1);

        gridpane.getChildren().addAll(play, pause, stop, table);
        Scene scene = new Scene(layout, 800, 600);
        window.setScene(scene);
        window.show();
    }


    public ObservableList<Music> getMusic(){
        ObservableList<Music> music = FXCollections.observableArrayList();
        music.add(new Music("hitlugu", "2.34", "file:///C:/Music/laul.mp3"));
        music.add(new Music("MyHumps", "3.32", "file:///C:/Music/MyHumps.mp3"));
        return music;
    }

    public void mängi() {
        ObservableList<Music> musicSelected = table.getSelectionModel().getSelectedItems();
        player.play(musicSelected.get(0).path);
    }
}
