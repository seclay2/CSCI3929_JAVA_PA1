package edu.cudenver.lottery.entity;

public class PowerPlayTicket extends Ticket {

    public PowerPlayTicket(int[] white, int red) {
        super(white, red);
        setType("powerplay");
    }

}
