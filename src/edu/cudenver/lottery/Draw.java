package edu.cudenver.lottery;

import java.util.Date;
import java.util.Random;

public class Draw {

    private Date date;
    private int[] white;
    private int red;

    public Draw() {
        date = new Date();
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

    public int[] getPicks() {
        int[] picks = new int[6];
        picks = white;
        picks[5] = red;
        return picks;
    }


}
