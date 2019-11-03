package edu.cudenver.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class Controller {
    @FXML public TextField txtIP;
    @FXML public TextField txtPort;
    @FXML public TextField txtCmd;
    @FXML public ListView lstBook;
    @FXML public ListView lstAuthor;
    @FXML public ListView lstPublisher;
    @FXML public Button btnConnect;
    @FXML public Button btnDisconnect;
    @FXML public Button btnSend;
    @FXML public Button btnHelp;

    private Client libraryClient;

    private ObservableList<String> bookList;
    private ObservableList<String> authorList;
    private ObservableList<String> publisherList;

    public Controller(){
        bookList= FXCollections.observableArrayList();
        authorList= FXCollections.observableArrayList();
        publisherList= FXCollections.observableArrayList();
    }


    public void initialize(){
        setConnectionBoxEnabled(true);

        this.lstBook.setItems(bookList);
        this.lstAuthor.setItems(authorList);
        this.lstPublisher.setItems(publisherList);
    }



    //////////////////////////////  Actions //////////////////////////////

    /**
     * Executes the Help
     * @param actionEvent
     */
    public void help(ActionEvent actionEvent) {
        String help="Commands:\n";
        help += "A|author name\n";
        help += "P|name|address\n";
        help += "B|title|location|pub year|author name|publisher name\n";
        help += "L| -> List Books\n";
        help += "T| -> List Authors\n";
        help += "U| -> List Publishers";
        Alert a = new Alert(Alert.AlertType.INFORMATION,help,ButtonType.OK);
        a.showAndWait();
    }

    /**
     * Send the command to the server and process the response.
     * @param actionEvent
     */
    public void sendCommand(ActionEvent actionEvent) {
        String cmd = this.txtCmd.getText();
        sendCommand(cmd);
    }

    /**
     * Sends the command (text line) to the server through the client object.
     * Based on the action requested by the user, process the response different.
     * For the Add something commands, it will automatically refresh the lists.
     * @param cmd
     */
    private void sendCommand(String cmd){
        String response;
        System.out.println("Sending CMD:::"+cmd);
        if (this.libraryClient.isConnected()){
            try {
                response = this.libraryClient.sendMessage(cmd);

                String[] cmdArray = cmd.split("\\|");
                switch (cmdArray[0]){
                    case "A":
                    case "P":
                    case "B":
                        processAddAction(response,cmdArray[0]);
                        break;

                    case "L": //books
                    case "U": // Publishers
                    case "T": // authors
                        processListAction(response, cmdArray[0]);
                        break;
                    default:
                        Alert a = new Alert(Alert.AlertType.ERROR,"Not Implemented", ButtonType.OK);
                        a.showAndWait();
                }
            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR,"Unexpected Error", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    /**
     * Connects to the server
     * @param actionEvent
     */
    public void connect(ActionEvent actionEvent) {
        String ip = this.txtIP.getText();
        int port = Integer.parseInt(this.txtPort.getText());

        this.libraryClient = new Client(ip,port);

        this.libraryClient.connect();
        if (this.libraryClient.isConnected()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION,"Connected!", ButtonType.OK);
            a.showAndWait();
            setConnectionBoxEnabled(false);
            sendCommand("L|");
            sendCommand("U|");
            sendCommand("T|");

        }
    }


    /**
     * Disconnects from the server.
     * @param actionEvent
     */
    public void disconnect(ActionEvent actionEvent) {
        if (this.libraryClient.isConnected()) {
            this.libraryClient.disconnect();
        }
        setConnectionBoxEnabled(true);
        this.libraryClient = null;
    }


    /**
     * Process a response for an add something action.
     * Sends automatically the list action.
     * Refactored from "processAddAuthor","processAddPublisher","processAddBook"
     * @param response
     * @param event
     */
    private void processAddAction(String response, String event){
        String sendCmd;
        String alertMsg;

        switch (event){
            case "A": sendCmd="T|"; alertMsg="Author Successfully Added"; break;
            case "P": sendCmd="U|"; alertMsg="Publisher Successfully Added"; break;
            case "B": sendCmd="L|"; alertMsg="Book Successfully Added"; break;
            default:
                Alert a = new Alert(Alert.AlertType.ERROR,"Unknown action.",ButtonType.OK);
                a.showAndWait();
                return;
        }

        String[] responses = response.split("\\|");

        if (Integer.parseInt(responses[0])==0 ){
            Alert a = new Alert(Alert.AlertType.INFORMATION,alertMsg,ButtonType.OK);
            a.showAndWait();
            sendCommand(sendCmd);
        }
        else{
            Alert a = new Alert(Alert.AlertType.ERROR,responses[1],ButtonType.OK);
            a.showAndWait();
        }
    }



    /**
     * Process a response for an List command.
     * Refactored from "processListAuthor","processListPublisher","processListBooks"
     * @param response
     * @param event
     */
    private void processListAction(String response, String event){
        ObservableList<String> list;
        switch (event){
            case "L": list=bookList;        break;
            case "U": list=publisherList;   break;
            case "T": list=authorList;      break;
            default:
                Alert a = new Alert(Alert.AlertType.ERROR,"Unknown action.",ButtonType.OK);
                a.showAndWait();
                return;
        }

        String[] responses = response.split("\\|");

        if (Integer.parseInt(responses[0])==0 ){
            list.remove(0,list.size());
            for (int i=1; i< responses.length; i++){
                list.add(responses[i]);
            }
        }
        else{
            Alert a = new Alert(Alert.AlertType.ERROR,responses[1],ButtonType.OK);
            a.showAndWait();
        }

    }


    /**
     * Sets the status of the GUI controls.
     * If true, will enable the connection box and disable the rest.
     * @param connected
     */
    private void setConnectionBoxEnabled(boolean connected){
        //if true will enable this:
        this.btnConnect.setDisable(!connected);
        this.txtIP.setDisable(!connected);
        this.txtPort.setDisable(!connected);

        //if true will disable this:
        this.btnSend.setDisable(connected);
        this.btnHelp.setDisable(connected);
        this.btnDisconnect.setDisable(connected);
        this.txtCmd.setDisable(connected);

    }
}