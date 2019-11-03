package edu.cudenver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerTest {

    public static void displayMessage(String msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) {

        try {
            displayMessage("Attempting connection");

            Socket clientSocket = new Socket("127.0.0.1", 50000);
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            try {
                String msg = "U|3039635914|Scott|tigers07";
                output.println(msg);
                displayMessage("CLIENT>> "+ msg);

                String srvResponse = input.readLine();
                displayMessage("SERVER>> "+ srvResponse);

            } catch (IOException e) {
                displayMessage("IO err");
            }

            try {
                String msg = "L|3039635914|tigers07";
                output.println(msg);
                displayMessage("CLIENT>> "+ msg);

                String srvResponse = input.readLine();
                displayMessage("SERVER>> "+ srvResponse);

            } catch (IOException e) {
                displayMessage("IO err");
            }

            try {
                String msg = "1234|T";
                output.println(msg);
                displayMessage("CLIENT>> "+ msg);

                String srvResponse = input.readLine();
                displayMessage("SERVER>> "+ srvResponse);

            } catch (IOException e) {
                displayMessage("IO err");
            }

        }
        catch (Exception e){

        }
        finally{
            displayMessage("Connected");
        }

    }



}
