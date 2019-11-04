package edu.cudenver.server;

import edu.cudenver.logging.PrintLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 * Single thread worker for server-client communication
 */
public class ClientWorker implements Runnable {

    private ClientServer server;
    private Socket connection;
    private long token;


    public ClientWorker(ClientServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        this.token = new Random().nextLong();
    }

    @Override
    public void run() {
        try {
            PrintLog.write("Getting Data Streams");

            BufferedReader input = getInputStream(connection);
            PrintWriter output = getOutputStream(connection);

            PrintLog.write("Processing Connection");
            try {
                String message = "";
                do {
                    message = input.readLine();
                    PrintLog.write(message);

                    String response = server.processClientMessage(message, this);

                    sendMessage(response, output);
                } while (!message.equals("TERMINATE"));
            } catch (Exception e) {
                PrintLog.write("Client terminated CW.");
                closeConnection(connection, input, output);
            }

        } catch (IOException e) {
            PrintLog.write("\n================\nCannot get the Streams.");
            e.printStackTrace();
        }
    }


    private PrintWriter getOutputStream(Socket connection) throws IOException {

        return new PrintWriter(connection.getOutputStream(), true);

    }


    private BufferedReader getInputStream(Socket connection) throws IOException {

        InputStreamReader stream = new InputStreamReader(connection.getInputStream());
        return new BufferedReader(stream);

    }


    private void closeConnection(Socket connection, BufferedReader input, PrintWriter output) {
        PrintLog.write("\nTerminating connection");
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendMessage(String msg, PrintWriter output) {
        output.println(msg);
        PrintLog.write("SERVER>> " + msg.replace("\n", ""));
    }

    public long getToken() {
        return token;
    }

    public void invalidateToken() {
        token = new Random().nextLong();
    }
}