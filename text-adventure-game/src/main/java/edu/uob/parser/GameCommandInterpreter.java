package edu.uob;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

// A class for interpreting unique game commands
public class GameCommandInterpreter {
    ArrayList<String> tokens;
    GameState gameState;

    public GameCommandInterpreter(ArrayList<String> tokens, GameState gameState) {
        this.tokens = tokens;
        this.gameState = gameState;
    }

    public String getBuiltInCommand() {
        String currentString = "";
        for (String token : tokens) {
            switch (token) {
                case "inventory" -> {
                    currentString = "inventory";
                    return currentString;
                }
                case "inv" -> {
                    currentString = "inv";
                    return currentString;
                }
                case "get" -> {
                    currentString = "get";
                    return currentString;
                }
                case "goto" -> {
                    currentString = "goto";
                    return currentString;
                }
                case "drop" -> {
                    currentString = "drop";
                    return currentString;
                }
                case "look" -> {
                    currentString = "look";
                    return currentString;
                }
                case "health" -> {
                    currentString = "health";
                    return currentString;
                }
                default -> currentString = "";
            }
        }
        return currentString;
    }

    public String getMatchTriggerWord() {
        ArrayList<String> triggerWordList = gameState.getTriggerWordsList();
        for (String s : triggerWordList) {
            if (tokens.contains(s.toLowerCase())) {
                return s;
            }
        }
        return "";
    }

    public int countTriggerWordMatches() {
        ArrayList<String> triggerWordList = gameState.getTriggerWordsList();
        int count = 0;
        for (String s : triggerWordList) {
            for (String token : tokens) {
                if (token.equalsIgnoreCase(s)) {
                    count++;
                }
            }
        }
        return count;
    }

    public HashSet<GameAction> getActionSetFromTriggerWord(String triggerName) {
        TreeMap<String, HashSet<GameAction>> actions = gameState.getTreeActionMap();

        for (String s : actions.keySet()) {
            if (s.equalsIgnoreCase(triggerName)) {
                return actions.get(s);
            }
        }
        return null;
    }

    public GameAction matchSubjectFromSet(HashSet<GameAction> gameActionSet, ArrayList<String> tokens) throws GameAdditionalCmdException {
        for (GameAction gameAction : gameActionSet) {
            for (String token : tokens) {
                if (checkIfContainsSubject(gameAction, token)) {
                    return gameAction;
                }
            }
        }
        throw new GameAdditionalCmdException("Command does not include a feasible subject");
    }

    public boolean checkIfContainsSubject(GameAction gameAction, String subjectName) {
        ArrayList<String> subjects = gameAction.getSubjects();
        for (String s : subjects) {
            if (subjectName.equals(s.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
