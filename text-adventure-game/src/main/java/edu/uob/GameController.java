package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameController {
    GameState gameState;
    ArrayList<String> tokens;
    Player player;
    String playerLocationName;

    public GameController(GameState gameState, Player player, ArrayList<String> tokens) {
        this.gameState = gameState;
        this.player = player;
        this.tokens = tokens;
        this.playerLocationName = player.getPlayerLocation();
    }

    public String interpretCommand() throws GameInterpretException, GameAdditionalCmdException {
        GameCommandInterpreter commandInterpreter = new GameCommandInterpreter(tokens, gameState);
        String TriggerCommand = commandInterpreter.getBuiltInCommand();
        switch (TriggerCommand) {
            case "inventory":
            case "inv":
                return executeInventoryCommand();
            case "get":
                if (commandIsValidCheck()) {
                    throw new GameInterpretException("Artefact to get is not stated");
                }
                return executeGetCommand();

            case "drop":
                if (commandIsValidCheck()) {
                    throw new GameInterpretException("Artefact to drop is not stated");
                }
                return executeDropCommand();
            case "goto":
                if (commandIsValidCheck()) {
                    throw new GameInterpretException("Location to reach is not stated");
                }
                return executeGotoCommand();
            case "look":
                return executeLookCommand();
            case "health":
                return executeHealthCommand();
            default:
                return executeGameSpecificCommand();
        }
    }

    private boolean commandIsValidCheck() {
        return tokens.size() <= 1;
    }

    private Location retrieveCurrentLocation() {
        return gameState.getLocation(playerLocationName);
    }

    private Location retrieveLocation(String newLocationName) {
        return gameState.getLocation(newLocationName);
    }

    private String executeGameSpecificCommand() throws GameInterpretException, GameAdditionalCmdException {
        GameCommandInterpreter commandInterpreter = new GameCommandInterpreter(tokens, gameState);
        if (commandInterpreter.countTriggerWordMatches() == 1) {
            StringBuilder returnCommand = new StringBuilder();

            String triggerCommand = commandInterpreter.getMatchTriggerWord();
            HashSet<GameAction> triggerGameActionSet = commandInterpreter.getActionSetFromTriggerWord(triggerCommand);
            GameAction triggerGameAction = commandInterpreter.matchSubjectFromSet(triggerGameActionSet, tokens);
            GameAdditionalCmd gameAdditionalCmd = new GameAdditionalCmd(gameState, triggerGameAction);

            String actionCommandString = gameAdditionalCmd.executeAdditionalCmd();
            returnCommand.append(actionCommandString);
            returnCommand.append("\n");
            return returnCommand.toString();

        } else if (commandInterpreter.countTriggerWordMatches() > 1) {
            throw new GameInterpretException("Command is ambiguous");
        }
        throw new GameInterpretException("Cannot locate trigger words");
    }

    private String executeInventoryCommand() {
        ArrayList<Artefact> inventoryArtefacts = player.getPlayerInventory();
        StringBuilder returnCommand = new StringBuilder();
        returnCommand.append("You have these items in inventory: ").append("\n");
        for (Artefact artefact : inventoryArtefacts) {
            returnCommand.append(artefact.getDescription());
            returnCommand.append("\n");
        }
        return returnCommand.toString();
    }

    private String executeGetCommand() throws GameInterpretException {
        Location currentLocation = retrieveCurrentLocation();
        StringBuilder returnCommand = new StringBuilder();

        if(currentLocation.countNumberOfValidArtefact(tokens) > 1){
            throw new GameInterpretException("Command is ambiguous as it has two or more valid artefacts");
        }

        for (String artefact : tokens) {
            if (currentLocation.checkIfEntityExistInArtefact(artefact)) {
                Artefact artefactToBeChanged = currentLocation.getArtefactByName(artefact);
                currentLocation.removeArtefacts(artefactToBeChanged);
                player.putArtefactInInventory(artefactToBeChanged);
                returnCommand.append("You picked up a ");
                returnCommand.append(artefact);
                return returnCommand.toString();
            }
        }
        throw new GameInterpretException("Failed to find the artefact in current location");
    }

    private String executeDropCommand() throws GameInterpretException {
        Location currentLocation = retrieveCurrentLocation();
        StringBuilder returnCommand = new StringBuilder();

        if(player.countNumberOfValidArtefactInInventory(tokens) > 1){
            throw new GameInterpretException("Command is ambiguous as it has two or more valid artefacts");
        }

        for (String artefact : tokens) {
            if (player.checkIfContainsArtefactInInventory(artefact)) {
                Artefact artefactToBeChanged = player.getArtefactFromInventoryByName(artefact);
                currentLocation.addArtefacts(artefactToBeChanged);
                player.removeArtefactFromInventory(artefactToBeChanged);
                returnCommand.append("You dropped a ");
                returnCommand.append(artefact);
                return returnCommand.toString();
            }
        }
        throw new GameInterpretException("Failed to find the artefact in player's inventory");
    }

    private String executeGotoCommand() throws GameInterpretException {
        Location currentLocation = retrieveCurrentLocation();
        StringBuilder returnCommand = new StringBuilder();

        if(currentLocation.countNumberOfValidLocation(tokens)> 1){
            throw new GameInterpretException("Command is ambiguous as it has two or more valid locations");
        }

        for (String locationName : tokens) {
            if (currentLocation.checkIfLocationExists(locationName)) {
                player.setPlayerLocation(locationName);
                Location newLocation = retrieveLocation(locationName);
                returnCommand.append("You are in ");
                returnCommand.append(newLocation.getDescription()).append(".").append(" ");
                returnCommand.append("You can see:");
                returnCommand.append("\n");
                appendAllCurrentEntity(newLocation, returnCommand);
                return returnCommand.toString();
            }
        }
        throw new GameInterpretException("Failed to locate new location in accessible path");
    }

    private String executeLookCommand() {
        Location currentLocation = retrieveCurrentLocation();
        StringBuilder returnCommand = new StringBuilder();
        returnCommand.append("You are in ");
        returnCommand.append(currentLocation.getDescription()).append(".").append(" ");
        returnCommand.append("You can see:");
        returnCommand.append("\n");

        appendAllCurrentEntity(currentLocation, returnCommand);

        return returnCommand.toString();
    }

    private void appendAllCurrentEntity(Location currentLocation, StringBuilder returnCommand) {
        ArrayList<Artefact> currentLocationArtefact = currentLocation.getArtefactsList();
        appendCurrentArtefact(currentLocationArtefact, returnCommand);

        ArrayList<Furniture> currentLocationFurniture = currentLocation.getFurnitureList();
        appendCurrentFurniture(currentLocationFurniture, returnCommand);

        ArrayList<Character> currentLocationCharacter = currentLocation.getCharactersList();
        appendCurrentLocationCharacter(currentLocationCharacter, returnCommand);

        ArrayList<String> accessibleLocation = currentLocation.getLocationRelation();
        appendCurrentAccessibleLocation(accessibleLocation, returnCommand);

        HashMap<String, Player> playerHashMap = gameState.getPlayerHashMap();
        appendCurrentPlayer(playerHashMap, returnCommand, currentLocation);
    }

    private void appendCurrentArtefact(ArrayList<Artefact> currentLocationArtefact, StringBuilder returnCommand) {
        if (!currentLocationArtefact.isEmpty()) {
            for (Artefact artefact : currentLocationArtefact) {
                String artefactNameDescription = artefact.getDescription();
                returnCommand.append(artefactNameDescription);
                returnCommand.append("\n");
            }
        }
    }

    private void appendCurrentFurniture(ArrayList<Furniture> currentLocationFurniture, StringBuilder returnCommand) {
        if (!currentLocationFurniture.isEmpty()) {
            for (Furniture furniture : currentLocationFurniture) {
                String furnitureNameDescription = furniture.getDescription();
                returnCommand.append(furnitureNameDescription);
                returnCommand.append("\n");
            }
        }
    }

    private void appendCurrentLocationCharacter(ArrayList<Character> currentLocationCharacter, StringBuilder returnCommand) {
        if (!currentLocationCharacter.isEmpty()) {
            for (Character character : currentLocationCharacter) {
                String characterNameDescription = character.getDescription();
                returnCommand.append(characterNameDescription);
                returnCommand.append("\n");
            }
        }
    }

    private void appendCurrentAccessibleLocation(ArrayList<String> accessibleLocation, StringBuilder returnCommand) {
        if (!accessibleLocation.isEmpty()) {
            returnCommand.append("You can access from here:");
            returnCommand.append("\n");
            for (String location : accessibleLocation) {
                returnCommand.append(location);
                returnCommand.append("\n");
            }
        }
    }

    private void appendCurrentPlayer(HashMap<String, Player> playerHashMap, StringBuilder returnCommand, Location currentLocation) {
        String locationName = currentLocation.getName();
        if (!playerHashMap.isEmpty()) {
            returnCommand.append("You can see players include:");
            returnCommand.append("\n");
            for (Player otherPlayer : playerHashMap.values()) {
                if (otherPlayer.getPlayerLocation().equalsIgnoreCase(locationName)) {
                    returnCommand.append(otherPlayer.getName());
                    returnCommand.append("\n");
                }
            }
        }
    }

    private String executeHealthCommand() {
        StringBuilder returnCommand = new StringBuilder();
        returnCommand.append("Current health level: ");
        int healthLevel = player.getHealthLevel();
        returnCommand.append(healthLevel);
        return returnCommand.toString();
    }
}

