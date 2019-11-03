package edu.cudenver.lottery.service;

import edu.cudenver.lottery.entity.Draw;
import edu.cudenver.lottery.entity.Ticket;
import edu.cudenver.lottery.entity.User;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class LotteryService {

    private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public LotteryService() {

    }

    public void createUser(String phone, String name, String password) {
        users.put(phone, new User(phone, name, password));
    }

    public boolean validateUser(String phone, String password) {
        if (users.containsKey(phone)) {
            return users.get(phone).pwdMatch(password);
        } else {
            return false;
        }
    }

    public User getUser(String phone) {
        return users.get(phone);
    }

    public void createTicketForUser(String phone, int[] white, int red) {
        getUser(phone).addTicket(new Ticket(white, red));
    }

    public void generateTicketPrizes(Draw draw) {
        for (HashMap.Entry<String, User> entry : users.entrySet()) {
            User user = entry.getValue();
            for (Ticket ticket : user.getTickets()) {
                if (ticket.getPrize() == -1) {
                    ticket.setPrize(generateTicketPrize(draw, ticket));
                }
            }
        }
    }

    // TODO: Add prize logic
    private int generateTicketPrize(Draw draw, Ticket ticket) {
        return new Random().nextInt();
    }
}
