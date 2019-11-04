package edu.cudenver.lottery.entity;

public class BasicTicket extends Ticket {

    public BasicTicket(int[] white, int red) {
        super(white, red);
        setType("basic");
    }

}
