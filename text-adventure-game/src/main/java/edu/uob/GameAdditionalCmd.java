package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;

public class GameAdditionalCmd {
    GameState gameState;
    GameAction gameAction;
    Player currentPlayer;
    String currentPlayerName;
    ArrayList<String> subjects;
    ArrayList<String> possibleInventoryArtefacts = new ArrayList<>();
    ArrayList<String> possibleLocationEntities = new ArrayList<>();

    public GameAdditionalCmd(GameState gameState,GameAction gameAction){
        this.gameState = gameState;
        this.gameAction = gameAction;
        this.currentPlayerName = gameState.getCurrentPlayer();
        this.currentPlayer = gameState.getPlayerFromMap(currentPlayerName);
        this.subjects = gameAction.getSubjects();
    }

    public String executeAdditionalCmd() throws GameAdditionalCmdException {
        StringBuilder returnCommand = new StringBuilder();
        checkIfEntitiesExist();

        ArrayList<String> actionConsumedList = gameAction.getConsumed();
        ArrayList<String> actionProducedList = gameAction.getProduced();

        for (String consumedEntity : actionConsumedList) {
            consumedEntityCommand(consumedEntity);
        }
        for (String producedEntity : actionProducedList) {
            producedEntityCommand(producedEntity);
        }
        String narration = gameAction.getNarration();
        returnCommand.append(narration);

        if (currentPlayer.getHealthLevel() == 0) {
            removePlayerFromGame();
            returnCommand.append("\n").append("You died and lost all of your items, you must return to the start of the game");
        }
        return returnCommand.toString();
    }

    private void removePlayerFromGame() {
        ArrayList<Artefact> currentPlayerInventory = currentPlayer.getPlayerInventory();
        Location currentLocation = getCurrentLocation();
        String firstLocation = gameState.getFirstLocation();

        for (Artefact artefactToAdd : currentPlayerInventory) {
            currentLocation.addArtefacts(artefactToAdd);
        }
        currentPlayer.dropAllInventory();
        currentPlayer.restoreHealth();
        currentPlayer.setPlayerLocation(firstLocation);
    }

    private void checkIfEntitiesExist() throws GameAdditionalCmdException {
        possibleLocationEntities.addAll(possibleLocationArtefact());
        possibleLocationEntities.addAll(possibleLocationFurniture());
        possibleLocationEntities.addAll(possibleLocationCharacter());
        possibleLocationEntities.addAll(possibleArtefactInInventory());
        if (!possibleLocationEntities.containsAll(subjects)) {
            throw new GameAdditionalCmdException("Failed to locate needed subject in location or inventory");
        }
    }

    private Location getCurrentLocation() {
        String playerLocation = currentPlayer.getPlayerLocation();
        return gameState.getLocation(playerLocation);
    }

    private ArrayList<String> possibleArtefactInInventory() {
        ArrayList<Artefact> artefactsInInventory = currentPlayer.getPlayerInventory();
        for (String subject : subjects) {
            for (Artefact artefact : artefactsInInventory) {
                if (artefact.getName().equalsIgnoreCase(subject)) {
                    possibleInventoryArtefacts.add(subject);
                }
            }
        }
        return possibleInventoryArtefacts;
    }

    private ArrayList<String> possibleLocationArtefact() {
        Location currentLocation = getCurrentLocation();
        ArrayList<Artefact> artefactsInLocation = currentLocation.getArtefactsList();
        ArrayList<String> possibleLocationArtefacts = new ArrayList<>();
        for (String subject : subjects) {
            for (Artefact artefact : artefactsInLocation) {
                if (artefact.getName().equalsIgnoreCase(subject)) {
                    possibleLocationArtefacts.add(subject);
                }
            }
        }
        return possibleLocationArtefacts;
    }

    private ArrayList<String> possibleLocationFurniture() {
        Location currentLocation = getCurrentLocation();
        ArrayList<Furniture> furnitureInLocation = currentLocation.getFurnitureList();
        ArrayList<String> possibleLocationFurniture = new ArrayList<>();
        for (String subject : subjects) {
            for (Furniture furniture : furnitureInLocation) {
                if (furniture.getName().equalsIgnoreCase(subject)) {
                    possibleLocationFurniture.add(subject);
                }
            }
        }
        return possibleLocationFurniture;
    }

    private ArrayList<String> possibleLocationCharacter() {
        Location currentLocation = getCurrentLocation();
        ArrayList<Character> characterInLocation = currentLocation.getCharactersList();
        ArrayList<String> possibleLocationCharacter = new ArrayList<>();
        for (String subject : subjects) {
            for (Character character : characterInLocation) {
                if (character.getName().equalsIgnoreCase(subject)) {
                    possibleLocationCharacter.add(subject);
                }
            }
        }
        return possibleLocationCharacter;
    }


    private void consumedEntityCommand(String consumedEntity){
        consumedArtefactFromInventory(consumedEntity);
        consumedEntityFromLocation(consumedEntity);
        consumedPath(consumedEntity);
        consumedHealth(consumedEntity);
    }

    private void consumedArtefactFromInventory(String consumedEntity) {
        Artefact artefactToConsumed = currentPlayer.getArtefactFromInventoryByName(consumedEntity);
        if (artefactToConsumed != null) {
            gameState.addArtefactIntoStoreroom(artefactToConsumed);
            currentPlayer.removeArtefactFromInventory(artefactToConsumed);
        }
    }

    private void consumedEntityFromLocation(String consumedEntity) {
        Location currentLocation = getCurrentLocation();
        String entity = currentLocation.getTypeofEntity(consumedEntity);

        switch (entity) {
            case "artefact" -> {
                Artefact artefactToRemove = currentLocation.getArtefactByName(consumedEntity);
                gameState.addArtefactIntoStoreroom(artefactToRemove);
                currentLocation.removeArtefacts(artefactToRemove);
            }
            case "furniture" -> {
                Furniture furnitureToRemove = currentLocation.getFurnitureByName(consumedEntity);
                gameState.addFurnitureIntoStoreroom(furnitureToRemove);
                currentLocation.removeFurniture(furnitureToRemove);
            }
            case "character" -> {
                Character characterToRemove = currentLocation.getCharacterByName(consumedEntity);
                gameState.addCharacterIntoStoreroom(characterToRemove);
                currentLocation.removeCharacters(characterToRemove);
            }
            default -> {
            }
        }
    }

    private void consumedPath(String consumedEntity) {
        Location currentLocation = getCurrentLocation();
        if (currentLocation.checkIfLocationExists(consumedEntity)) {
            currentLocation.removeLocationRelation(consumedEntity);
        }
    }

    private void consumedHealth(String consumedEntity) {
        if (consumedEntity.equalsIgnoreCase("health")) {
            currentPlayer.decreaseHealth();
        }
    }

    private void producedEntityCommand(String producedEntity) {
        producedHealth(producedEntity);
        producedPath(producedEntity);
        producedEntityInLocation(producedEntity);
    }

    private void producedEntityInLocation(String producedEntity) {
        HashMap<String, Location> locationHashMap = gameState.getLocationMap();
        Location currentLocation = getCurrentLocation();
        for (String location : locationHashMap.keySet()) {
            Location sourceLocation = gameState.getLocation(location);
            if (sourceLocation.checkIfSubjectExistInLocation(producedEntity)) {
                alterSubjectInLocation(producedEntity, sourceLocation, currentLocation);
            }
        }
    }

    private void alterSubjectInLocation(String producedEntity, Location sourceLocation, Location currentLocation) {
        String entity = sourceLocation.getTypeofEntity(producedEntity);

        switch (entity) {
            case "artefact" -> {
                Artefact alterArtefact = sourceLocation.getArtefactByName(producedEntity);
                sourceLocation.removeArtefacts(alterArtefact);
                currentLocation.addArtefacts(alterArtefact);
            }
            case "furniture" -> {
                Furniture alterFurniture = sourceLocation.getFurnitureByName(producedEntity);
                sourceLocation.removeFurniture(alterFurniture);
                currentLocation.addFurniture(alterFurniture);
            }
            case "character" -> {
                Character alterCharacter = sourceLocation.getCharacterByName(producedEntity);
                sourceLocation.removeCharacters(alterCharacter);
                currentLocation.addCharacter(alterCharacter);
            }
            default -> {
            }
        }
    }

    private void producedHealth(String producedEntity) {
        if (producedEntity.equalsIgnoreCase("health")) {
            currentPlayer.increaseHealth();
        }
    }

    private void producedPath(String producedEntity) {
        Location currentLocation = getCurrentLocation();
        if (gameState.getLocationMap().containsKey(producedEntity)) {
            currentLocation.addPath(producedEntity);
        }
    }
}
