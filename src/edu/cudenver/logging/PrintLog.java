package edu.cudenver.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * PrintLog class provides logging layer for server
 */
public class PrintLog {
    public static synchronized void write(String message) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("clientServer.log", true));
            writer.println(message);
            writer.close();
        } catch (IOException e) {
            System.out.print("logging error");
            e.printStackTrace();
        }
    }
}
