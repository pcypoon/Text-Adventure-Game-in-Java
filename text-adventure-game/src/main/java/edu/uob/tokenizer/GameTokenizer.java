package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameTokenizer {
    String command;
    String[] basicTokenizedCommand;

    public GameTokenizer(String command){
        this.command = command;
    }

    public String getPlayerName() throws GameTokenizeException {
        command = command.replaceAll("\\s+", " ");
        if(command.contains(":")){
            this.basicTokenizedCommand = command.split(":");
            return basicTokenizedCommand[0].trim();
        }
            throw new GameTokenizeException("Fail to locate player's name");
    }

    public ArrayList<String> gameTokens(){
        String gameCommand = basicTokenizedCommand[1];
        gameCommand = gameCommand.toLowerCase();
        gameCommand = gameCommand.replaceAll("[:,;]", " ");
        gameCommand = gameCommand.replaceAll("\\s+", " ");
        String[] tempStorage = gameCommand.split("\\s");
        ArrayList<String> tokens = new ArrayList<>();
        Collections.addAll(tokens, tempStorage);
        tokens.removeAll(Arrays.asList("", null));
        return tokens;
    }
}
