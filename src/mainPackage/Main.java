package mainPackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Load the FXML File
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));

        // Window Options
        primaryStage.setTitle("Tic-Tac-Toe | AMOGUIS & SUN");
        primaryStage.setResizable(false);

        // Launch Window
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    // Launch the JavaFX Application
    public static void main(String[] args) {
        launch(args);
    }
}
