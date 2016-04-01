import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application{

    Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage){
        window = primaryStage;
        window.setTitle("Mp3 player");

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(10, 10, 10, 10));

        Button play = new Button("play");
        Button pause = new Button("pause");
        Button stop = new Button("stop");

        gridpane.setConstraints(play, 0, 0);
        gridpane.setConstraints(pause, 1, 0);
        gridpane.setConstraints(stop, 2, 0);

        TableView<Music> table = new TableView<>();
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
        Scene scene = new Scene(gridpane, 800, 600);
        window.setScene(scene);
        window.show();
    }


    public ObservableList<Music> getMusic(){
        ObservableList<Music> music = FXCollections.observableArrayList();
        music.add(new Music("hitlugu", "2.34"));
        return music;
    }
}
