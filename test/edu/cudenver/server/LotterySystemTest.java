package edu.cudenver.server;

import edu.cudenver.lottery.LotterySystem;

public class LotterySystemTest {

    public static void main (String[] args) {

        LotterySystem lotterySystem = new LotterySystem();

        lotterySystem.createUser("3039635170", "Scott", "tigers07");

        if (lotterySystem.validateUser("3039635170", "tigers07")) {
            System.out.println("valid");
        }

        lotterySystem.getUser("3039635170");

    }
}
