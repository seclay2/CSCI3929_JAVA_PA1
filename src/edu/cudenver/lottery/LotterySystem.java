package edu.cudenver.lottery;

import java.util.HashMap;

public class LotterySystem {

    private Draw draw;
    private HashMap<String, User> users;

    public LotterySystem() {
        draw = null;
        users = new HashMap<>();
    }

    public void createUser(String phone, String name, String password) {
        users.put(phone, new User(phone, name, password));
    }

    public boolean validateUser(String phone, String password) {
        if (users.containsKey(phone))
            return users.get(phone).pwdMatch(password);
        else
            return false;
    }

    public User getUser(String s) {
        return users.get(s);
    }


}
