package com.company;

import java.util.HashMap;
import java.util.Random;

public class TokenHandler {

    public static final int TOKEN_LENGTH = 50;
    private static final long EXPIRATION_TIME_MILLISECONDS = 3600000000L;
    private static final String CHARACTER_POOL = "abcdefghijklmnopqrstuvwxyz1234567890";

    private static HashMap<String, String> usernameToToken = new HashMap<String, String>();
    private static HashMap<String, String> tokenToUsername = new HashMap<String, String>();
    private static HashMap<String, Long> tokenToLastTimeUsed = new HashMap<String, Long>();

    public static boolean validateToken(String token) {
        if (!tokenToLastTimeUsed.containsKey(token)) {
            return false;
        }
        long lastTimeUsed = tokenToLastTimeUsed.get(token);
        if (System.currentTimeMillis() - lastTimeUsed > EXPIRATION_TIME_MILLISECONDS) {
            removeEntryByToken(token);
            return false;
        }
        updateLastTimeOfToken(token);
        return true;
    }

    public static void createToken(String username) {
        if (usernameToToken.containsKey(username)) {
            return;
        }
        String token = generateToken();
        usernameToToken.put(username, token);
        tokenToUsername.put(token, username);
        tokenToLastTimeUsed.put(token, System.currentTimeMillis());
    }

    public static void removeEntryByToken(String token) {
        String username = tokenToUsername.get(token);
        tokenToLastTimeUsed.remove(token);
        tokenToUsername.remove(token);
        usernameToToken.remove(username);
    }

    public static String getTokenOfUsername(String username) {
        return usernameToToken.get(username);
    }

    public static String getUsernameOfToken(String token) {
        return tokenToUsername.get(token);
    }

    public static void updateLastTimeOfToken(String token) {
        tokenToLastTimeUsed.replace(token, System.currentTimeMillis());
    }

    public static boolean checkUsernameAndToken(String username, String token) {
        if (!usernameToToken.containsKey(username) || !usernameToToken.get(username).equals(token)) {
            return false;
        }
        if (!validateToken(token)) {
            return false;
        }
        updateLastTimeOfToken(token);
        return true;
    }

    private static String generateToken() {
        Random characterRandomizer = new Random();
        String result;
        do {
            result = "";
            for (int characterIndex = 0; characterIndex < TOKEN_LENGTH; characterIndex++) {
                result = result.concat("" + CHARACTER_POOL.charAt(characterRandomizer.nextInt(36)));
            }
        } while (tokenToLastTimeUsed.containsKey(result));
        return result;
    }





    public static void addEntry(String username, String token) {
        usernameToToken.put(username, token);
        tokenToUsername.put(token, username);
        tokenToLastTimeUsed.put(token, System.currentTimeMillis());
    }
}
