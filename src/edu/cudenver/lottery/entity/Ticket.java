package edu.cudenver.lottery.entity;

import java.util.UUID;

public class Ticket {

    private String ticketId = UUID.randomUUID().toString();
    private int[] whiteNumbers;
    private int powerball;
    private int prize;
    private String type;

    public Ticket() {}

    public Ticket(int[] whiteNumbers, int powerball) {
        this.whiteNumbers = whiteNumbers;
        this.powerball = powerball;
        this.prize = -1;
    }

    public String getTicketNumber() {
        return ticketId;
    }

    public int[] getWhiteNumbers() {
        return whiteNumbers;
    }

    public int getPowerball() {
        return powerball;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Ticket string format csv
     * w1,w2,w3,w4,w5,powerball,prize,type
     * @return
     */
    @Override
    public String toString() {
        String str = "";
        for (int i : whiteNumbers)
            str += i + ",";
        str += powerball + ",";
        str += prize + ",";
        str += type;

        return str;
    }
}
