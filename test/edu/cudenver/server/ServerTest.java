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

        long token = 0;

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
                String[] arg = srvResponse.split("\\|");
                token = Long.parseLong(arg[2]);
                displayMessage("SERVER>> "+ srvResponse);

            } catch (IOException e) {
                displayMessage("IO err");
            }


            /*try {
                String msg = "O|" + token + "|3039635914";
                output.println(msg);
                displayMessage("CLIENT>> "+ msg);

                String srvResponse = input.readLine();
                displayMessage("SERVER>> "+ srvResponse);

            } catch (IOException e) {
                displayMessage("IO err");
            }*/

            try {
                String msg = "N|"+token+"|3039635914|55|7|25|33|45|12|true";
                output.println(msg);
                displayMessage("CLIENT>> "+ msg);

                String srvResponse = input.readLine();
                displayMessage("SERVER>> "+ srvResponse);

            } catch (IOException e) {
                displayMessage("IO err");
            }

            try {
                String msg = "N|"+token+"|3039635914|5|1|7|32|64|23|false";
                output.println(msg);
                displayMessage("CLIENT>> "+ msg);

                String srvResponse = input.readLine();
                displayMessage("SERVER>> "+ srvResponse);

            } catch (IOException e) {
                displayMessage("IO err");
            }

            try {
                String msg = "N|"+token+"|3039635914|60|54|43|32|21|19|false";
                output.println(msg);
                displayMessage("CLIENT>> "+ msg);

                String srvResponse = input.readLine();
                displayMessage("SERVER>> "+ srvResponse);

            } catch (IOException e) {
                displayMessage("IO err");
            }


            try {
                String msg = "T|"+token+"|3039635914";
                output.println(msg);
                displayMessage("CLIENT>> "+ msg);

                String srvResponse = input.readLine();
                displayMessage("SERVER>> "+ srvResponse);

            } catch (IOException e) {
                displayMessage("IO err");
            }

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
                String msg = "U|3038767564|Jeff|password";
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
            //displayMessage("Connected");
        }

    }



}
