package edu.cudenver.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return ClientApp.primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root =
                FXMLLoader.load(getClass().getResource("login/LoginView.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Powerball Lottery");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[]args) { launch(args); }
}
