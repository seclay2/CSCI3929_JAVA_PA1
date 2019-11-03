package edu.cudenver.server;

//import edu.cudenver.exception.AuthorNotFoundException;
//import edu.cudenver.exception.PublisherNotFoundException;

import edu.cudenver.lottery.LotterySystem;

import javax.swing.event.InternalFrameEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerApp {
    public final static String RSP_OK = "0|Ok";
    public final static String RSP_NOT_IMPLEMENTED = "1|Not Implemented";
    public final static String RSP_NOT_AUTH = "1|Not Authenticated";
    public final static String RSP_INVALID_CMD = "4|Invalid Message Format";

    private int port;
    private int backlog;
    private int connectionCounter;

    private ServerSocket socketServer;

    private long token;
    private LotterySystem lottery;

    public ServerApp(int port, int backlog) {
        this.port    = port;
        this.backlog = backlog; //Incoming Connection Queue size

        this.connectionCounter = 1; //Total number of connections handled.

        this.token = 1234;

        lottery = new LotterySystem();
        this.loadFile();
    }

    /**
     * Starts and Runs the Server.
     */
    public void runServer(){
        try {
            socketServer = new ServerSocket(this.port, this.backlog);
            Socket connection = null;

            Scanner scanner = new Scanner(System.in);

            while(true){// Infinite loop to receive connections.
                try{
                    connection = waitForConnection();

                    displayMessage(">> ");
                    if (scanner.hasNextLine()) {
                        String input = scanner.nextLine();
                        switch(input) {
                            case "save":
                                saveFile();
                                break;
                            case "exit":
                            case "quit":
                            case "terminate":
                                socketServer.close();
                                System.out.println("Server Exiting...");
                                break;
                            default:
                                System.out.println("Invalid command ( \"save\" or \"quit\"");
                        }
                    }

                    ExecutorService executorService = Executors.newCachedThreadPool();

                    ClientWorker cw = new ClientWorker(this,connection);

                    executorService.execute(cw);

                }
                catch (IOException e){
                    displayMessage("\n================\nServer Terminated.");
                }
                finally{

                }

            }
        }
        catch (IOException e){
            displayMessage("\n================\nServer cannot be opened.");
            e.printStackTrace();
        }
    }


    /**
     * Waits for a connection, and returns it.
     * @return the Socket object representing the connection.
     * @throws IOException
     */
    private Socket waitForConnection() throws IOException {
        displayMessage("Waiting For a Connection");

        Socket connection = socketServer.accept();

        displayMessage( "Connection "+ this.connectionCounter +
                " received from: " + connection.getInetAddress().getHostName());

        return connection;
    }


    private void displayMessage(String message){
        System.out.println(message);
    }


    private void loadFile(){
        try{
            Scanner file = new Scanner(new File("default.txt"));
            System.out.println("Reading File");
            while (file.hasNextLine()){
                String line = file.nextLine();
                System.out.println(processClientMessage(line));
            }
            System.out.println("Done");
        }
        catch (FileNotFoundException e){
            System.out.println("File does not exists.");
        }

    }

    private void saveFile(){

    }

    /**
     * Process the client messages.
     * @param msg
     * @return
     */
    protected synchronized String processClientMessage(String msg){
        String[] arguments = msg.split("\\|");
        String response;
        int args=arguments.length;

        //create user
        if (arguments[0].equals("U")) {
            if (args == 4){ //check the message has the correct number of arguments for the command
                lottery.createUser(arguments[1], arguments[2], arguments[3]);
                return RSP_OK;
            }
            else { return RSP_INVALID_CMD;}
        }
        //login
        else if (arguments[0].equals("L")) {
            if (args == 3){ //check the message has the correct number of arguments for the command
                if (lottery.validateUser(arguments[1], arguments[2]))
                    return RSP_OK + "|" + token;
                else
                    return RSP_NOT_AUTH;
            }
            else { return RSP_INVALID_CMD; }
        }
        else {
            if (Long.parseLong(arguments[0]) != token)
                return RSP_NOT_AUTH;
        }

        switch (arguments[1]){
            //get tickets
            case "T":
                if (args == 3){ //check the message has the correct number of arguments for the command
                    return lottery.getUser(arguments[2]).getTickets().toString();
                }
                else { return RSP_INVALID_CMD; }
            //create new ticket
            case "N":
                if (args == 10) {
                    int[] whites = { Integer.parseInt(arguments[3]),
                                    Integer.parseInt(arguments[4]),
                                    Integer.parseInt(arguments[5]),
                                    Integer.parseInt(arguments[6]),
                                    Integer.parseInt(arguments[7])
                    };
                    int red = Integer.parseInt(arguments[8]);

                    lottery.getUser(arguments[2]).createTicket(1, whites, red);
                }
            default:
                return RSP_NOT_IMPLEMENTED;
        }


    }


    /**
     * Main program that launches the Server.
     * Can and should be moved out to another class.
     * @param args
     */
    public static void main(String[] args){
        ServerApp server = new ServerApp(50000, 100);
        server.runServer();
    }

    class listener implements Runnable {
        @Override
        public void run() {

        }
    }
}
