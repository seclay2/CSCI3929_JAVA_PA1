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

    /**
     * gets 10 most recent tickets
     * @return
     */
    public ArrayList<Ticket> getTickets() {
        ArrayList<Ticket> lastTen = new ArrayList<>();

        if (tickets.size() <= 10) {
            return tickets;
        } else {
            for (int i = tickets.size()-1; i >= tickets.size()-11; i--) {
                lastTen.add(tickets.get(i));
            }
            return lastTen;
        }
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    /**
     * checks password input matches with password stored
     * @param pwd
     * @return
     */
    public boolean pwdMatch(String pwd) {
        return password.equals(pwd);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(phone);
        sb.append("|");
        sb.append(name);
        sb.append("|");
        sb.append(password);
        sb.append("|");
        for (Ticket t : tickets) {
            sb.append(t.toString());
            sb.append("#");
        }
        sb.delete(sb.length()-1, sb.length());

        return sb.toString();
    }
}
