package edu.cudenver.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root =
                FXMLLoader.load(getClass().getResource("ClientApp.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Powerball Lottery");
        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }

    public static void main(String[]args) { launch(args); }
}
