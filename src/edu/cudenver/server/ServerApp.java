package edu.cudenver.server;

import edu.cudenver.lottery.LotteryService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServerApp {

    private ExecutorService clientExecutorService;
    private final LotteryService lotteryService = new LotteryService();

    public ServerApp() {
        this.loadFile();
    }

    /**
     * Starts and Runs the Server.
     */
    public void runServer() {
        try {
            clientExecutorService = Executors.newCachedThreadPool();
            clientExecutorService.execute(new ClientServer(lotteryService));

            Scanner scanner = new Scanner(System.in);
            while (true) {
                try {
                    displayMessage(true, "Actions Available:");
                    displayMessage("  pl  (print client server log)");
                    displayMessage("  s   (save)");
                    displayMessage("  sd  (schedule draw)");
                    displayMessage("  dn  (draw now)");
                    displayMessage("  rcs (restart client server)");
                    displayMessage("  q   (quit)");
                    System.out.println("\n================");
                    System.out.print("$> ");

                    if (scanner.hasNextLine()) {
                        switch (scanner.nextLine()) {
                            case "pl":
                                printClientLog();
                                break;

                            case "s":
                                saveFile();
                                break;

                            case "sd":
                                displayMessage(true, "scheduling draw...");
                                //TODO schedule draw
                                displayMessage("draw scheduled");
                                break;

                            case "dn":
                                displayMessage(true, "drawing now...");
                                displayMessage(lotteryService.runDraw());
                                displayMessage("draw complete");
                                break;

                            case "rcs":
                                displayMessage(true, "Shutting down client server...");
                                clientExecutorService.shutdownNow();
                                clientExecutorService.awaitTermination(180, TimeUnit.SECONDS);
                                if (clientExecutorService.isShutdown()) {
                                    displayMessage("Starting client server...");
                                    clientExecutorService = Executors.newCachedThreadPool();
                                    clientExecutorService.execute(new ClientServer(lotteryService));
                                    displayMessage("Client server is running.");
                                } else {
                                    displayMessage(
                                        "WARNING: Unable to shutdown client server within 180 seconds. Aborting...");
                                }
                                break;

                            case "q":
                                displayMessage(true, "Shutting down client server...");
                                clientExecutorService.shutdown();
                                saveFile();
                                displayMessage("Server shutdown complete.");
                                System.exit(0);
                                break;

                            default:
                                displayMessage("Invalid Option");
                        }
                    }
                } catch (Exception e) {
                    displayMessage(true, "Server encountered an unexpected problem. Shutting down...");
                    e.printStackTrace();
                    clientExecutorService.shutdownNow();
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            displayMessage(true, "Server cannot be started.");
            e.printStackTrace();
            System.exit(2);
        }
    }

    private void printClientLog() {
        try (BufferedReader br = new BufferedReader(new FileReader("clientServer.log"))) {
            displayMessage(true, "Client Server Log:\n\n");
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            displayMessage(true, "Error printing client logs:");
            e.printStackTrace();
        }
    }

    private void loadFile() {
        try {

            //TODO load files
            Scanner file = new Scanner(new File("default.txt"));
            displayMessage("Loading existing Powerball data...");
            while (file.hasNextLine()) {
                String line = file.nextLine();
                // TODO: Logic to load from saved data
            }
            displayMessage("Data loaded.");
        } catch (Exception e) {
            displayMessage("Data was unable to be loaded.");
            e.printStackTrace();
        }

    }

    private void saveFile() {
        displayMessage(true, "saving current state...");
        try {
            lotteryService.toFile();
            displayMessage("save complete.");
        } catch (IOException e) {
            displayMessage(false, "Could not write to file");
        }
    }

    private void displayMessage(String message) {
        displayMessage(false, message);
    }

    private void displayMessage(boolean buffer, String message) {
        if (buffer) {
            System.out.println("\n================");
        }

        System.out.println(message);
    }

    /**
     * Main program that launches the Server.
     *
     * @param args
     */
    public static void main(String[] args) {
        ServerApp server = new ServerApp();
        server.runServer();
    }
}
