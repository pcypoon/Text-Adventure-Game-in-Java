package edu.uob;
import java.util.ArrayList;

public class Player extends GameEntity {
    private ArrayList<Artefact> inventory = new ArrayList<>();
    private String location;
    private int healthStatus;

    public Player(String name, String firstLocation) {
        super(name, firstLocation);
        this.location = firstLocation;
        healthStatus = 3;
    }

    public ArrayList<Artefact> getPlayerInventory(){return inventory;}

    public void putArtefactInInventory(Artefact newArtefact){
        inventory.add(newArtefact);
    }

    public void removeArtefactFromInventory(Artefact existingArtefact){
        inventory.remove(existingArtefact);
    }

    public void setPlayerLocation(String locationName){
        location = locationName;
    }

    public String getPlayerLocation(){
        return location;
    }

    public Artefact getArtefactFromInventoryByName(String artefactName){
        for(Artefact artefact: inventory){
            if(artefact.getName().equalsIgnoreCase(artefactName)){
                return artefact;
            }
        }
        return null;
    }
    public boolean checkIfContainsArtefactInInventory(String artefactName){
        for(Artefact artefact: inventory){
            return artefact.getName().equalsIgnoreCase(artefactName);
        }
        return false;
    }

    public void increaseHealth() {
        if (healthStatus < 3) {
            healthStatus++;
        }
    }

    public int countNumberOfValidArtefactInInventory(ArrayList<String> tokens){
        int count = 0;
        for(Artefact artefact : inventory){
            for(String token : tokens){
                if(token.equalsIgnoreCase(artefact.getName())){
                    count++;
                }
            }
        }
        return count;
    }

    public void decreaseHealth(){
        healthStatus--;
    }

    public void restoreHealth(){
        healthStatus = 3;
    }

    public int getHealthLevel(){
        return healthStatus;
    }

    public void dropAllInventory(){
        inventory.clear();
    }
}
