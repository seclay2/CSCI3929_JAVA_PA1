package edu.cudenver.server;

import edu.cudenver.logging.PrintLog;
import edu.cudenver.lottery.service.LotteryService;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientServer implements Runnable {
    public final static String RSP_OK = "0|Ok";
    public final static String RSP_NOT_IMPLEMENTED = "1|Not Implemented";
    public final static String RSP_NOT_AUTH = "1|Not Authenticated";
    public final static String RSP_INVALID_CMD = "4|Invalid Message Format";

    private ServerSocket socketServer;
    private int connectionCounter;
    private long token;

    private final LotteryService lotteryService;

    public ClientServer(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
        this.connectionCounter = 1; //Total number of connections handled.
        this.token = 1234;
    }

    public void run() {
        try {
            socketServer = new ServerSocket(50000, 100);

            while (true) {// Infinite loop to receive connections.
                Socket connection = waitForConnection();

                ExecutorService executorService = Executors.newCachedThreadPool();

                ClientWorker cw = new ClientWorker(this, connection);

                executorService.execute(cw);
            }
        } catch (Exception e) {
            PrintLog.write("Client Server Terminated.");
            PrintLog.write(e.getMessage());
        }
    }

    /**
     * Waits for a connection, and returns it.
     *
     * @return the Socket object representing the connection.
     * @throws IOException
     */
    private Socket waitForConnection() throws IOException {
        PrintLog.write("Waiting For a Connection");

        Socket connection = socketServer.accept();

        PrintLog.write("Connection " + this.connectionCounter +
                       " received from: " + connection.getInetAddress().getHostName());

        return connection;
    }

    /**
     * Process the client messages.
     *
     * @param msg
     * @return
     */
    protected synchronized String processClientMessage(String msg) {
        String[] arguments = msg.split("\\|");
        int args = arguments.length;

        String action = arguments[0];
        switch (action) {
            case "U": // create user
                if (args != 4) {
                    PrintLog.write("Invalid Create User Command");
                    return RSP_INVALID_CMD;
                }
                lotteryService.createUser(arguments[1], arguments[2], arguments[3]);
                PrintLog.write("User Created: " + arguments[1]);
                return RSP_OK;

            case "L": // user login
                if (args != 3) {
                    PrintLog.write("Invalid Login Command");
                    return RSP_INVALID_CMD;
                }
                if (!lotteryService.validateUser(arguments[1], arguments[2])) {
                    PrintLog.write("User Auth Failed For: " + arguments[1]);
                    return RSP_NOT_AUTH;
                }

                PrintLog.write("User Logged In Successfully: " + arguments[1]);
                return RSP_OK + "|" + token;

            case "T": // get user's tickets
                if (args != 3) {
                    PrintLog.write("Invalid Get User Tickets Command");
                    return RSP_INVALID_CMD;
                }
                if (isNotAuthorized(arguments[1])) {
                    PrintLog.write("User Auth Failed For: " + arguments[1]);
                    return RSP_NOT_AUTH;
                }

                PrintLog.write("Getting tickets for user: " + arguments[1]);
                return lotteryService.getUser(arguments[2]).getTickets().toString();


            case "N": // create ticket for the user
                if (args != 10) {
                    PrintLog.write("Invalid create ticket command");
                    return RSP_INVALID_CMD;
                }
                if (isNotAuthorized(arguments[1])) {
                    PrintLog.write("User Auth Failed For: " + arguments[1]);
                    return RSP_NOT_AUTH;
                }

                int[] whites = {
                    Integer.parseInt(arguments[3]),
                    Integer.parseInt(arguments[4]),
                    Integer.parseInt(arguments[5]),
                    Integer.parseInt(arguments[6]),
                    Integer.parseInt(arguments[7])
                };
                int red = Integer.parseInt(arguments[8]);
                lotteryService.createTicketForUser(arguments[2], whites, red);

                PrintLog.write("Ticket created for user: " + arguments[1]);
                return RSP_OK;

            default: // invalid action
                PrintLog.write("Invalid action given");
                return RSP_NOT_IMPLEMENTED;
        }
    }

    private boolean isNotAuthorized(String token) {
        return Long.parseLong(token) != this.token;
    }
}
