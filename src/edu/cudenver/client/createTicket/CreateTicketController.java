package edu.cudenver.client.createTicket;

import edu.cudenver.client.Client;
import edu.cudenver.client.ClientApp;
import edu.cudenver.client.main.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;

public class CreateTicketController {

    @FXML private Text note;
    @FXML private TextField w1Field;
    @FXML private TextField w2Field;
    @FXML private TextField w3Field;
    @FXML private TextField w4Field;
    @FXML private TextField w5Field;
    @FXML private TextField powerballField;
    @FXML private RadioButton powerplayToggle;

    @FXML private Button backButton;
    @FXML private Button submitButton;

    private Client client;

    public void initialize(){

        note.setFill(Color.RED);

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (sendTicket()) {
                    backToMain();
                }
            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                backToMain();
            }
        });


        // Text Field Listeners

        w1Field.textProperty().addListener((observable, oldValue, newValue) ->{
            errorHandleWhiteTextFields(w1Field, oldValue, newValue);

        });

        w2Field.textProperty().addListener((observable, oldValue, newValue) ->{
            errorHandleWhiteTextFields(w2Field, oldValue, newValue);

        });

        w3Field.textProperty().addListener((observable, oldValue, newValue) ->{
            errorHandleWhiteTextFields(w3Field, oldValue, newValue);

        });

        w4Field.textProperty().addListener((observable, oldValue, newValue) ->{
            errorHandleWhiteTextFields(w4Field, oldValue, newValue);
        });

        w5Field.textProperty().addListener((observable, oldValue, newValue) ->{
            errorHandleWhiteTextFields(w5Field, oldValue, newValue);
        });

        powerballField.textProperty().addListener((observable, oldValue, newValue) ->{
            //prevent non-numerical text
            if (!newValue.matches("\\d*")) {
                powerballField.setText(oldValue);
            }
            //check range
            if (powerballField.getText() != "") {
                int num = Integer.parseInt(powerballField.getText());
                if (num > 26 || num < 1) {
                    powerballField.setStyle("-fx-text-inner-color: red;");
                    submitButton.setDisable(true);
                } else {
                    powerballField.setStyle("-fx-text-inner-color: black;");
                    submitButton.setDisable(false);
                }
            }
        });

    }

    /**
     * Ensures values in TextField are numeric only and within range
     * @param tf TextField to check
     * @param oldValue from listener, previous valid value
     * @param newValue from listener, new value to check
     */
    private void errorHandleWhiteTextFields(TextField tf, String oldValue, String newValue) {
        //prevent non-numerical text
        if (!newValue.matches("\\d*")) {
            tf.setText(oldValue);
        }
        //check range
        if (tf.getText() != "") {
            int num = Integer.parseInt(tf.getText());
            if (num > 69 || num < 1) {
                tf.setStyle("-fx-text-inner-color: red;");
                submitButton.setDisable(true);
            } else {
                tf.setStyle("-fx-text-inner-color: black;");
                submitButton.setDisable(false);
            }
        }
    }

    /**
     * Sets scene to MainView
     */
    private void backToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../main/MainView.fxml"));
            Parent root = loader.load();
            MainController controller = loader.getController();
            controller.setClient(client);
            ClientApp.getPrimaryStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Cannot load Create User Scene");
            a.show();
        }
    }

    /**
     * Sends new ticket to server
     * @return true if successful, false otherwise
     */
    private boolean sendTicket() {
        String rsp = "";
        String powerplay = "false";
        if (powerplayToggle.isSelected()) {
            powerplay = "true";
        }
        try {
            rsp = client.sendMessage("N|" + client.getToken() + "|" + client.getUserId() +
                    "|" + w1Field.getText() + "|" + w2Field.getText() + "|" + w3Field.getText() +
                    "|" + w4Field.getText() + "|" + w5Field.getText() + "|" + powerballField.getText() +
                    "|" + powerplay);
            String[] arg = rsp.split("\\|");
            if (Integer.parseInt(arg[0]) > 0) {
                infoBox("Could not create ticket: " + rsp, null, "Failure");
            }
            else {
                return true;
            }
        } catch (IOException e) {
            infoBox("Unable to logout of server...\n"+rsp, null, "Error");

        }
        return false;
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
