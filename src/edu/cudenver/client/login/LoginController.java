package edu.cudenver.client.login;

import edu.cudenver.client.Client;
import edu.cudenver.client.ClientApp;
import edu.cudenver.client.main.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class LoginController {

    @FXML private ImageView logoImage;

    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;

    @FXML private Button loginButton;
    @FXML private Button createUserButton;


    private Client client;
    private Stage stage;

    public void initialize() {
        client = new Client();

        stage = ClientApp.getPrimaryStage();

        logoImage.setImage(new Image("images/logo3.png"));

        //button event handler to connect to server
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    client.connect();
                    client.setUserId(phoneField.getText());
                    String rsp = client.sendMessage("L|"+ client.getUserId() + "|" + passwordField.getText());
                    String[] arg = rsp.split("\\|");
                    if (Integer.parseInt(arg[0]) > 0) {
                        infoBox(rsp, null, "Failed");
                    } else {
                        client.setToken(Long.parseLong(arg[2]));
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../main/MainView.fxml"));
                        Parent root = loader.load();
                        MainController controller = loader.getController();
                        controller.setClient(client);
                        stage.setScene(new Scene(root));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Cannot Connect to Server.");
                    a.show();
                }
            }
        });

        createUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../createUser/CreateUserView.fxml"));
                    Parent root = loader.load();
                    stage.setScene(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Cannot load Create User Scene");
                    a.show();
                }
            }
        });
    }

    private static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

}
