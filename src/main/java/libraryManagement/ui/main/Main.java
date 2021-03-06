package libraryManagement.ui.main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import libraryManagement.database.DatabaseHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
        DatabaseHandler.getInstance();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
                shutdown();
            }

        });
    }
    public void shutdown() {
        try {
            DriverManager.getConnection("jdbc:sqlite:./Biblioteka/database.db");
        } catch (
                SQLException se) {
            if (!(se.getErrorCode() == 50000) && (se.getSQLState().equals("XJ015")))
                se.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
