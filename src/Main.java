import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
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
        HBox buttonHbox = new HBox();
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(20, 10, 10, 10));

        Menu filemenu = new Menu("File");
        MenuItem addsongItem = new MenuItem("Add song");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> window.close());
        filemenu.getItems().addAll(addsongItem, exitItem);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(filemenu);
        layout.setTop(menuBar);
        layout.setCenter(gridpane);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setFillWidth(true);
        columnConstraints.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().add(columnConstraints);

        Button play = new Button("play");
        play.setOnAction(event -> start_playing());
        Button pause = new Button("pause");
        pause.setOnAction(event -> player.pause());
        Button stop = new Button("stop");
        stop.setOnAction(event -> player.stop());
        Slider time = new Slider();
        Label volume = new Label("Volume:");
        Slider vol = new Slider();

        vol.setPrefWidth(100);
        vol.setMinWidth(30);
        vol.setValue(100);
        buttonHbox.setHgrow(time, Priority.ALWAYS);
        play.setMinWidth(30);
        pause.setMinWidth(30);
        stop.setMinWidth(30);

        buttonHbox.getChildren().addAll(play, pause, stop, time, volume, vol);;

        table = new TableView<>();
        table.setItems(getMusic());

        TableColumn<Music, String> namecolumn = new TableColumn<>("Name");
        namecolumn.setMinWidth(200);
        namecolumn.setCellValueFactory(new PropertyValueFactory<>("song_name"));

        TableColumn<Music, String> lengthcolumn = new TableColumn<>("Length");
        lengthcolumn.setMinWidth(50);
        lengthcolumn.setCellValueFactory(new PropertyValueFactory<>("length"));

        table.getColumns().addAll(namecolumn, lengthcolumn);
        gridpane.setConstraints(table, 0, 1);
        gridpane.setConstraints(buttonHbox, 0, 0);

        gridpane.getChildren().addAll(buttonHbox, table);
        Scene scene = new Scene(layout, 800, 600);
        window.setScene(scene);
        window.show();
    }


    public ObservableList<Music> getMusic(){
        ObservableList<Music> music = FXCollections.observableArrayList();
        music.add(new Music("Love story", "2.34", "file:///C:/Music/laul.mp3"));
        music.add(new Music("MyHumps", "3.32", "file:///C:/Music/MyHumps.mp3"));
        return music;
    }

    private void start_playing() {
        ObservableList<Music> musicSelected = table.getSelectionModel().getSelectedItems();
        player.play(musicSelected.get(0).path);
    }


}
