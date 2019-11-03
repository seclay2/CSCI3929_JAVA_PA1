package edu.cudenver.lottery;

import java.io.Serializable;
import java.util.Random;

public class Ticket {

    protected long ticketNumber;
    protected int[] white;
    protected int red;

    public Ticket() {
        Random rand = new Random();
        ticketNumber = rand.nextLong();
    }

    public Ticket(int[] white, int red) {
        ticketNumber = 0;
        this.white = white;
        this.red = red;
    }

    public long getTicketNumber() {
        return ticketNumber;
    }

}
