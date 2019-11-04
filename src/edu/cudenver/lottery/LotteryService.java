package edu.cudenver.lottery;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import edu.cudenver.lottery.entity.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LotteryService {

    private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private ArrayList<Draw> draws = new ArrayList<>();
    private int jackpot;

    public LotteryService() {
        jackpot = 40000000;
    }

    public void createUser(String phone, String name, String password) {
        users.put(phone, new User(phone, name, password));
    }

    /**
     * Compares user password and input password
     * @param phone user phone #
     * @param password input password
     * @return true if valid, false otherwise
     */
    public boolean validateUser(String phone, String password) {
        if (users.containsKey(phone))
            return users.get(phone).pwdMatch(password);
        else
            return false;
    }

    /**
     * Check if user already exists
     * @param phone user phone #
     * @return true if exists, false if not
     */
    public boolean userExist(String phone) {
        return users.containsKey(phone);
    }

    public User getUser(String s) {
        return users.get(s);
    }

    /**
     * Creates new ticket
     * @param phone user phone #
     * @param white white ball numebers
     * @param powerball powerball number
     * @param pp true if powerplay, false if basic
     */
    public void createTicket(String phone, int[] white, int powerball, boolean pp) {
        if (pp)
            getUser(phone).addTicket(new PowerPlayTicket(white, powerball));
        else
            getUser(phone).addTicket(new BasicTicket(white, powerball));
    }

    /**
     * Updates prize on all tickets
     * @param draw
     */
    public void generateTicketPrizes(Draw draw) {
        for (HashMap.Entry<String, User> entry : users.entrySet()) {
            User user = entry.getValue();
            generateUserTicketPrizes(user, draw);
        }
    }

    /**
     * Updates prizes of one Users tickets
     * @param user
     * @param draw
     */
    private void generateUserTicketPrizes(User user, Draw draw) {
        for (Ticket ticket : user.getTickets()) {
            if (ticket.getPrize() < 0) {
                int numWhiteMatch = whiteMatch(draw.getWhiteNumbers(), ticket.getWhiteNumbers());
                boolean powerball = powerballMatch(draw.getPowerball(), ticket.getPowerball());
                if (ticket.getType().equals("basic")) {
                    ticket.setPrize(generateBasicTicketPrize(numWhiteMatch, powerball));
                }
                else {
                    ticket.setPrize(generatePPTicketPrize(numWhiteMatch, powerball, draw.getMultiplier()));
                }
            }
        }
    }

    /**
     * Gets number of white ball matches between ticket and draw
     * @param draw
     * @param ticket
     * @return
     */
    private int whiteMatch(int[] draw, int[] ticket) {
        int matchCounter = 0;

        for (int i : ticket) {
            for (int j : draw) {
                if ( i == j )
                    matchCounter++;
            }
        }

        return matchCounter;
    }

    /**
     * Checks powerball match between ticket and draw
     * @param draw
     * @param ticket
     * @return
     */
    private boolean powerballMatch(int draw, int ticket) {
        return draw == ticket;
    }

    /**
     * Basic payout logic
     * @param numWhiteMatch
     * @param powerball
     * @return
     */
    private int generateBasicTicketPrize(int numWhiteMatch, boolean powerball) {

        switch (numWhiteMatch) {
            case 0:
            case 1:
                if (powerball)
                    return 4;
                else
                    return 0;
            case 2:
                if (powerball)
                    return 7;
                else
                    return 0;
            case 3:
                if (powerball)
                    return 100;
                else
                    return 7;
            case 4:
                if (powerball)
                    return 10000;
                else
                    return 100;
            case 5:
                if (powerball)
                    return jackpot;
                else
                    return 1000000;
        }
        return -1;
    }

    /**
     * Powerplay payout logic
     * @param numWhiteMatch
     * @param powerball
     * @param multiplier
     * @return
     */
    private int generatePPTicketPrize(int numWhiteMatch, boolean powerball, int multiplier) {

        switch (numWhiteMatch) {
            case 0:
            case 1:
                if (powerball)
                    return 8 * multiplier;
                else
                    return 0;
            case 2:
                if (powerball)
                    return 14 * multiplier;
                else
                    return 0;
            case 3:
                if (powerball)
                    return 200 * multiplier;
                else
                    return 14 * multiplier;
            case 4:
                if (powerball)
                    return 20000 * multiplier;
                else
                    return 200 * multiplier;
            case 5:
                if (powerball)
                    return jackpot;
                else
                    return 2000000;
        }
        return -1;
    }

    /*
    public void scheduleDraw(LocalDate date) {
        draws.add(new Draw(date));
    }*/

    /**
     * Runs new draw, adds to draw list
     * @return draw numbers string
     */
    public String runDraw() {
        Draw draw = new Draw();
        draws.add(draw);
        generateTicketPrizes(draw);

        return draw.toString();
    }

    /**
     * Saves User data and tickets to users.txt
     * draw data to draws.txt
     * @throws IOException
     */
    public void toFile() throws IOException {
        FileWriter fw = new FileWriter("users.txt");
        for (User user : users.values()) {
            fw.write(user.toString());
            fw.write('\n');
        }
        fw.close();

        fw = new FileWriter("draws.txt");
        for (Draw draw : draws) {
            fw.write(draw.toFileString());
            fw.write('\n');
        }
        fw.close();
    }
}
