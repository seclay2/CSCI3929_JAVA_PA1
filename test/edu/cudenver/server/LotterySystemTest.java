package edu.cudenver.server;

import edu.cudenver.lottery.LotteryService;

public class LotterySystemTest {

    public static void main (String[] args) {

        LotteryService lotteryService = new LotteryService();

        lotteryService.createUser("3039635170", "Scott", "tigers07");

        if (lotteryService.validateUser("3039635170", "tigers07")) {
            System.out.println("valid");
        }

        lotteryService.getUser("3039635170");

    }
}
