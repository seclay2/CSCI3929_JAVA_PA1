package edu.cudenver.client.main;

import edu.cudenver.client.Client;
import edu.cudenver.client.ClientApp;
import edu.cudenver.client.createTicket.CreateTicketController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

public class MainController {
    // instance variables for interacting with GUI
    @FXML private ListView<Ticket> ticketListView;
    @FXML private ListView<String> ticketInfoView;

    @FXML private Button logoutButton;
    @FXML private Button newTicketButton;
    @FXML private Button refreshButton;

    // stores the list of Book Objects
    private ObservableList<Ticket> tickets =
            FXCollections.observableArrayList();

    private Client client;


    public MainController(){
    }


    public void initialize(){
        // when ListView selection changes, show large cover in ImageView
        ticketListView.getSelectionModel().selectedItemProperty().
                addListener(
                        new ChangeListener<Ticket>() {
                            @Override
                            public void changed(ObservableValue<? extends Ticket> ov,
                                                Ticket oldValue, Ticket newValue) {
                                ObservableList<String> list = FXCollections.observableArrayList();
                                list.addAll(newValue.toStrArray());
                                ticketInfoView.setItems(list);
                            }
                        }
                );

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String rsp = "";
                try {
                    rsp = client.sendMessage("O|" + client.getToken() + "|" + client.getUserId());
                    String[] arg = rsp.split("\\|");
                    if (Integer.parseInt(arg[0]) > 0) {
                        infoBox(rsp, null, null);
                    }
                    else {
                        client.disconnect();
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/LoginView.fxml"));
                            Parent root = loader.load();
                            ClientApp.getPrimaryStage().setScene(new Scene(root));
                        } catch (IOException e) {
                            e.printStackTrace();
                            Alert a = new Alert(Alert.AlertType.ERROR);
                            a.setContentText("Cannot load Create User Scene");
                            a.show();
                        }
                    }
                } catch (IOException e) {
                    infoBox("Unable to logout of server...\n"+rsp, null, "Error");
                }
            }
        });

        refreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                refresh();
            }
        });

        newTicketButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../createTicket/CreateTicketView.fxml"));
                    Parent root = loader.load();
                    CreateTicketController controller = loader.getController();
                    controller.setClient(client);
                    ClientApp.getPrimaryStage().setScene(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Cannot load Create User Scene");
                    a.show();
                }
            }
        });
    }



    //////////////////////////////  Actions //////////////////////////////

    /**
     * Disconnects from the server.
     * @param actionEvent
     */
    public void disconnect(ActionEvent actionEvent) {
        if (this.client.isConnected()) {
            this.client.disconnect();
        }
        this.client = null;
    }

    /**
     * Updates content in list view with up-to-date data from server
     */
    public void refresh() {
        tickets = FXCollections.observableArrayList();
        String rsp = "";
        try {
            rsp = client.sendMessage("T|" + client.getToken() + "|" + client.getUserId());
            String[] arg = rsp.split("\\|");
            for (String s : arg) {
                if (s.split(",").length > 2)
                    tickets.add(new Ticket(s));
            }
            ticketListView.setItems(tickets);
        } catch (IOException e) {
            infoBox(rsp, null, "Failure");
        }
    }

    public void setClient(Client client) {
        this.client = client;
        refresh();
    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    /**
     * Inner class Ticket provides structure for ticket data
     */
    class Ticket {
        String whiteNumbers;
        String powerball;
        String prize;
        String type;

        Ticket(String ticketStr) {
            whiteNumbers = "";
            String[] infoStr = ticketStr.split(",");
            for (int i = 0; i < 5; i++) {
                whiteNumbers += infoStr[i] + "  ";
            }
            powerball = infoStr[5];
            if (infoStr[6].equals("-1"))
                prize = "TBA";
            else
                prize = infoStr[6];
            type = infoStr[7];
        }

        /**
         * Outputs array of ticket data as strings
         * for use with list view
         * @return array of ticket info strings
         */
        public String[] toStrArray() {
            return new String[] {
                "White: " + whiteNumbers,
                    "Powerball: " + powerball,
                    "Prize: " + prize,
                    "Type: " + type
            };
        }

        @Override
        public String toString() {
            return whiteNumbers + "  " + powerball;
        }
    }
}