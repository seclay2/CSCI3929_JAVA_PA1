package edu.cudenver.lottery.entity;

import java.time.LocalDate;
import java.util.Random;

public class Draw {

    private LocalDate date;
    private int[] white;
    private int powerball;
    private int multiplier;

    public Draw(LocalDate date) {
        this.date = date;
    }

    public Draw() {
        date = LocalDate.now();
        white = drawWhites();
        powerball = drawPowerball();
    }

    // random draw for white numbers
    private int[] drawWhites() {
        int[] draw = new int[5];
        for (int i = 0; i < 5; i++) {
            draw[i] = getRand(69);
        }
        return draw;
    }

    // random draw for powerball
    private int drawPowerball() {
        return getRand(26);
    }

    // random draw for multiplier
    private void drawMultiplier() {
        multiplier = getRand(4) + 1;
        //TODO 10x mult
    }

    private int getRand(int max) {
        return new Random().nextInt(max) + 1;
    }

    public LocalDate getDate() {
        return date;
    }

    public int[] getWhiteNumbers() {
        return white;
    }

    public int getPowerball() {
        return powerball;
    }

    public int getMultiplier() {
        return multiplier;
    }

    /**
     * Returns file friendly string for saving
     * @return
     */
    public String toFileString() {
        StringBuilder sb = new StringBuilder(date.toString());
        sb.append("|");
        for (int i : white) {
            sb.append(i);
            sb.append(",");
        }
        sb.delete(sb.length()-1, sb.length());
        sb.append("|");
        sb.append(powerball);
        sb.append("|");
        sb.append(multiplier);

        return sb.toString();
    }

    @Override
    public String toString() {
        String out = "";
        for (int i : white)
            out = out + i + " ";
        return  out + "(" + getPowerball() + ")";
    }
}
