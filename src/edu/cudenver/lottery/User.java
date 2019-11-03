package edu.cudenver.lottery;

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

    public void createTicket(int flag, int[] white, int red) {
        if (flag == 1) {
            tickets.add(new BasicTicket(white, red));
        }
    }

    public boolean pwdMatch(String pwd) {
        return password.equals(pwd);
    }


    @Override
    public java.lang.String toString() {
        return getName();
    }
}
