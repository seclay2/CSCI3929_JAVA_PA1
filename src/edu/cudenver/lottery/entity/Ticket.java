package edu.cudenver.lottery.entity;

import java.util.UUID;

public class Ticket {

    protected String ticketId = UUID.randomUUID().toString();
    protected int[] whiteNumbers;
    protected int redNumber;
    protected int prize;

    public Ticket() {

    }

    public Ticket(int[] whiteNumbers, int redNumber) {
        this.whiteNumbers = whiteNumbers;
        this.redNumber = redNumber;
        this.prize = -1;
    }

    public String getTicketId() {
        return ticketId;
    }

    public int[] getWhiteNumbers() {
        return whiteNumbers;
    }

    public int getRedNumber() {
        return redNumber;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prizeMoney) {
        this.prize = prizeMoney;
    }
}
