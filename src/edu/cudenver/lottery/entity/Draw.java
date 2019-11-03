package edu.cudenver.lottery.entity;

import java.time.LocalDate;
import java.util.Random;

public class Draw {

    private LocalDate date;
    private int[] white;
    private int red;

    public Draw() {
        date = LocalDate.now();
        white = drawWhites();
        red = drawRed();
    }

    private int[] drawWhites() {
        int[] draw = new int[5];
        for (int i = 0; i < 5; i++) {
            draw[i] = getRand(69);
        }
        return draw;
    }

    private int drawRed() {
        return getRand(26);
    }

    private int getRand(int max) {
        Random rand = new Random();
        return rand.nextInt(max) + 1;
    }

    public LocalDate getDate() {
        return date;
    }

    public int[] getWhite() {
        return white;
    }

    public int getRed() {
        return red;
    }
}
