package edu.cudenver.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private int port;
    private String serverIp;

    private boolean isConnected;

    private Socket clientSocket;
    private BufferedReader input      = null;
    private PrintWriter    output     = null;


    public Client(){
        this("127.0.0.1",50000);
    }

    public Client(String serverIp, int port){
        this.serverIp=serverIp;
        this.port=port;
        isConnected=false;
    }



    public boolean isConnected(){ return isConnected; }


    public void connect() {
        try {
            displayMessage("Attempting connection");

            this.clientSocket = new Socket(this.serverIp, this.port);
            this.isConnected = true;
            this.output = new PrintWriter(this.clientSocket.getOutputStream(), true);
            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

        }
        catch (Exception e){
            this.clientSocket = null;
            this.isConnected = false;
            this.output = null;
            this.input = null;
        }
        finally{
            displayMessage("Connected");
        }
    }


    public void disconnect(){
        displayMessage("\nTerminating connection");
        try {
            output.close();
            input.close();
            clientSocket.close();
        }
        catch (IOException|NullPointerException e){
            e.printStackTrace();
        }
        finally {
            this.isConnected=false;
        }
    }



    public String sendMessage(String msg) throws IOException{
        output.println(msg);
        displayMessage("CLIENT>> "+ msg);

        String srvResponse = this.input.readLine();
        displayMessage("SERVER>> "+ srvResponse);

        return srvResponse;
    }


    private void displayMessage(String message){
        System.out.println(message);
    }

    public static void main(String[] args) {

    }

}
