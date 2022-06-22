package edu.uob;

import java.util.ArrayList;

public class GameAction {
    private ArrayList<String> subjects = new ArrayList<>();
    private ArrayList<String> consumed = new ArrayList<>();
    private ArrayList<String> produced = new ArrayList<>();
    private String narration;

    public void addSubjects(String subjectEntity) {
        subjects.add(subjectEntity);
    }

    public void addConsumed(String consumedEntity) {
        consumed.add(consumedEntity);
    }

    public void addProduced(String producedEntity) {
        produced.add(producedEntity);
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public ArrayList<String> getConsumed() {
        return consumed;
    }

    public ArrayList<String> getProduced() {
        return produced;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getNarration() {
        return this.narration;
    }

}
