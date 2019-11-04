package edu.cudenver.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client class provides client-server communication layer
 */
public class Client {

    private int port;
    private String serverIp;

    private boolean isConnected;

    private Socket clientSocket;
    private BufferedReader input      = null;
    private PrintWriter    output     = null;

    private long token;
    private String userId;


    public Client(){
        this("127.0.0.1",50000);
    }

    public Client(String serverIp, int port){
        this.serverIp=serverIp;
        this.port=port;
        isConnected=false;
        token = 0;
        userId = "";
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
            token = 0;
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

    public String[] processCmd(String msg) throws Exception {
        return new String[]{"hello"};
    }

    public void setToken(long token) {
        this.token = token;
    }

    public long getToken() {
        return token;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

}
