package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class GameState {
    private String firstLocation;
    private HashMap<String, Location> locationHashMap = new HashMap<>();
    private HashMap<String, Player> playerHashMap = new HashMap<>();
    private TreeMap<String, HashSet<GameAction>> actions = new TreeMap<>();
    private ArrayList<String> triggerWordsList = new ArrayList<>();
    private String currentPlayer;

    public GameState() {
    }

    public void addLocation(String name, Location location) {
        locationHashMap.put(name, location);
    }

    public Location getLocation(String name) {
        return locationHashMap.get(name);
    }

    public void setFirstLocation(String name) {
        this.firstLocation = name;
    }

    public String getFirstLocation() {
        return firstLocation;
    }

    public HashMap<String, Location> getLocationMap() {
        return locationHashMap;
    }

    public void addTreeActionMap(String triggerWord, HashSet<GameAction> gameActionHashSet) {
        actions.put(triggerWord, gameActionHashSet);
    }

    public TreeMap<String, HashSet<GameAction>> getTreeActionMap() {
        return actions;
    }

    public boolean checkIfPlayerExist(String playerName) {
        return playerHashMap.containsKey(playerName);
    }

    public void addPlayerIntoMap(String playerName) {
        Player newPlayer = new Player(playerName, firstLocation);
        playerHashMap.put(playerName, newPlayer);
    }

    public Player getPlayerFromMap(String playerName) {
        return playerHashMap.get(playerName);
    }

    public void setPlayerIntoMap(Player player) {
        this.playerHashMap.put(player.name, player);
    }

    public void addTriggerWordIntoList(String triggerWord) {
        triggerWordsList.add(triggerWord);
    }

    public ArrayList<String> getTriggerWordsList() {
        return triggerWordsList;
    }

    public void setCurrentPlayer(String playerName) {
        this.currentPlayer = playerName;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void addArtefactIntoStoreroom(Artefact artefactToAdd) {
        Location storeroom = locationHashMap.get("storeroom");
        storeroom.addArtefacts(artefactToAdd);
    }

    public void addFurnitureIntoStoreroom(Furniture furnitureToAdd) {
        Location storeroom = locationHashMap.get("storeroom");
        storeroom.addFurniture(furnitureToAdd);
    }

    public void addCharacterIntoStoreroom(Character characterToAdd) {
        Location storeroom = locationHashMap.get("storeroom");
        storeroom.addCharacter(characterToAdd);
    }

    public HashMap<String, Player> getPlayerHashMap() {
        return playerHashMap;
    }
}

