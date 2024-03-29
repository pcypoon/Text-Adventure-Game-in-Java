package edu.uob;

import java.util.ArrayList;

public class Location extends GameEntity {
    private ArrayList<Artefact> artefacts = new ArrayList<>();
    private ArrayList<Furniture> furnitures = new ArrayList<>();
    private ArrayList<Character> characters = new ArrayList<>();
    private final ArrayList<String> locationRelation = new ArrayList<>();


    public Location(String name, String description) {
        super(name, description);
    }

    public void addArtefacts(Artefact artefact) {
        artefacts.add(artefact);
    }

    public void addFurniture(Furniture furniture) {
        furnitures.add(furniture);
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public void addPath(String locationName) {
        locationRelation.add(locationName);
    }

    public void removeArtefacts(Artefact artefact) {
        artefacts.remove(artefact);
    }

    public void removeFurniture(Furniture furniture) {
        furnitures.remove(furniture);
    }

    public void removeCharacters(Character character) {
        characters.remove(character);
    }

    public void removeLocationRelation(String location) {
        locationRelation.remove(location);
    }

    public Artefact getArtefactByName(String artefactName) {
        for (Artefact artefact : artefacts) {
            if (artefact.getName().equalsIgnoreCase(artefactName)) {
                return artefact;
            }
        }
        return null;
    }

    public Furniture getFurnitureByName(String furnitureName) {
        for (Furniture furniture : furnitures) {
            if (furniture.getName().equalsIgnoreCase(furnitureName)) {
                return furniture;
            }
        }
        return null;
    }

    public Character getCharacterByName(String characterName) {
        for (Character character : characters) {
            if (character.getName().equalsIgnoreCase(characterName)) {
                return character;
            }
        }
        return null;
    }

    public boolean checkIfLocationExists(String locationName) {
        for (String location : locationRelation) {
            if (location.equalsIgnoreCase(locationName)) {
                return true;
            }
        }
        return false;
    }

    public int countNumberOfValidLocation(ArrayList<String> tokens){
        int count = 0;
        for(String location : locationRelation){
            for(String token : tokens){
                if(token.equalsIgnoreCase(location)){
                    count++;
                }
            }
        }
        return count;
    }

    public String getTypeofEntity(String entityName) {
        if (checkIfEntityExistInArtefact(entityName)) {
            return "artefact";
        }
        if (checkIfEntityExistInFurniture(entityName)) {
            return "furniture";
        }
        if (checkIfEntityExistInCharacter(entityName)) {
            return "character";
        }
        return "";
    }

    public boolean checkIfSubjectExistInLocation(String subjectName) {
        if (checkIfEntityExistInArtefact(subjectName) || checkIfEntityExistInFurniture(subjectName) || checkIfEntityExistInCharacter(subjectName)) {
            return true;
        } else
            return false;
    }

    public int countNumberOfValidArtefact(ArrayList<String> tokens){
        int count = 0;
        for(Artefact artefact : artefacts){
            for(String token : tokens){
                if(token.equalsIgnoreCase(artefact.getName())){
                    count++;
                }
            }
        }
        return count;
    }

    public boolean checkIfEntityExistInArtefact(String entityName) {
        for (Artefact artefact : artefacts) {
            if (entityName.equalsIgnoreCase(artefact.getName())) {
                return true;
            }
        }
        return false;
    }


    private boolean checkIfEntityExistInFurniture(String entityName) {
        for (Furniture furniture : furnitures) {
            if (entityName.equalsIgnoreCase(furniture.getName())) {
                return true;
            }
        }
        return false;
    }


    private boolean checkIfEntityExistInCharacter(String entityName) {
        for (Character character : characters) {
            if (entityName.equalsIgnoreCase(character.getName())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Artefact> getArtefactsList() {
        return artefacts;
    }

    public ArrayList<Furniture> getFurnitureList() {
        return furnitures;
    }

    public ArrayList<Character> getCharactersList() {
        return characters;
    }

    public ArrayList<String> getLocationRelation() {
        return locationRelation;
    }

}
