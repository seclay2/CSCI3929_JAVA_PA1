package edu.cudenver.lottery.entity;

import java.util.ArrayList;

public class User {

    private String phone;
    private String name;
    private String password;
    private ArrayList<Ticket> tickets;

    public User(String phone, String name, String password) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        tickets = new ArrayList<>();
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getPwd() { return password; }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public boolean pwdMatch(String pwd) {
        return password.equals(pwd);
    }

    @Override
    public java.lang.String toString() {
        return getName();
    }
}
