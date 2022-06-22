package edu.uob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GameParser {
    File entitiesFile;
    File actionsFile;
    GameState state;

    public GameParser(File entitiesFile, File actionsFile, GameState state){
        this.entitiesFile = entitiesFile;
        this.actionsFile = actionsFile;
        this.state = state;
    }

    public void parseEntities() throws FileNotFoundException, ParseException, GameParseException {
        Parser parser = new Parser();
        FileReader reader = new FileReader(entitiesFile);
        parser.parse(reader);
        // getting the whole graph from entity file
        ArrayList<Graph> wholeDocument = parser.getGraphs();
        // getting the two sections of the whole document
        ArrayList<Graph> sections = wholeDocument.get(0).getSubgraphs();
        // getting the location section of the graph
        ArrayList<Graph> locations = sections.get(0).getSubgraphs();

        // Add the first location into game state
        Graph firstLocation = locations.get(0);
        ArrayList<Node> firstLocationNodes = firstLocation.getNodes(false);
        String firstLocationName = firstLocationNodes.get(0).getId().getId();
        state.setFirstLocation(firstLocationName);

        // loop through the sub-graph clusters in location
        for (Graph subGraphCluster : locations) {
            ArrayList<Node> locationNodeList = subGraphCluster.getNodes(false);

            // adding location into game state
            Node locationNode = locationNodeList.get(0);
            String locationName = locationNode.getId().getId();
            String locationDescription = locationNode.getAttribute("description");
            Location newLocation = new Location(locationName, locationDescription);
            state.addLocation(locationName, newLocation);

            // List of clusters in location
            ArrayList<Graph> clusters = subGraphCluster.getSubgraphs();

            // loop through each entity in cluster
            for (Graph subgraphEntities : clusters) {
                ArrayList<Node> entityNodes = subgraphEntities.getNodes(false);
                String entity = subgraphEntities.getId().getId();

                for (Node node : entityNodes) {
                    String name = node.getId().getId();
                    String description = node.getAttribute("description");

                    switch (entity) {
                        case "artefacts" -> addArtefact(newLocation, name, description);
                        case "furniture" -> addFurniture(newLocation, name, description);
                        case "characters" -> addCharacters(newLocation, name, description);
                        default -> System.out.println("Invalid entity" + entity);
                    }
                }
            }
        }
        Graph pathGraph = sections.get(1);
        parsePath(pathGraph);
    }

    public void parsePath(Graph pathGraph) throws GameParseException {
        ArrayList<Edge> edges = pathGraph.getEdges();
        for(Edge edge : edges){
            String source = edge.getSource().getNode().getId().getId();
            String target = edge.getTarget().getNode().getId().getId();
            if(checkIfLocationExist(source)&& checkIfLocationExist(target)){
                state.getLocation(source).addPath(target);
            }else{
                throw new GameParseException("Unable to create path with current source and target");
            }
        }
    }

    private boolean checkIfLocationExist(String locationName){
        return state.getLocationMap().containsKey(locationName);
    }

    public void addArtefact(Location location, String name, String description){
        Artefact newArtefact = new Artefact(name, description);
        state.getLocation(location.name).addArtefacts(newArtefact);
    }

    public void addFurniture(Location location, String name, String description){
        Furniture newFurniture = new Furniture(name, description);
        state.getLocation(location.name).addFurniture(newFurniture);
    }

    public void addCharacters(Location location, String name, String description){
        Character newCharacter = new Character(name, description);
        state.getLocation(location.name).addCharacter(newCharacter);
    }

    public void parseAction() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(actionsFile);
        Element root = document.getDocumentElement();
        NodeList actions = root.getChildNodes();

        // loop through each action
        for (int i = 1; i < actions.getLength(); i += 2) {
            Element currentAction = (Element) actions.item(i);
            GameAction newAction = new GameAction();

            NodeList subjectList = currentAction.getElementsByTagName("subjects");
            parseSubject(subjectList, newAction);

            NodeList consumedList = currentAction.getElementsByTagName("consumed");
            parseConsumed(consumedList, newAction);

            NodeList producedList = currentAction.getElementsByTagName("produced");
            parseProduced(producedList, newAction);

            String currentNarration = currentAction.getElementsByTagName("narration").item(0).getTextContent();
            newAction.setNarration(currentNarration);


            for (int j = 0; j < currentAction.getElementsByTagName("triggers").getLength(); j++) {
                Element trigger = (Element) currentAction.getElementsByTagName("triggers").item(j);
                for (int k = 0; k < trigger.getElementsByTagName("keyword").getLength(); k++) {
                    String currentKeyword = trigger.getElementsByTagName("keyword").item(k).getTextContent();
                    if (state.getTreeActionMap().containsKey(currentKeyword)) {
                        state.getTreeActionMap().get(currentKeyword).add(newAction);
                    } else {
                        HashSet<GameAction> gameActions = new HashSet<>();
                        state.addTriggerWordIntoList(currentKeyword);
                        gameActions.add(newAction);
                        state.addTreeActionMap(currentKeyword, gameActions);
                    }
                }
            }
        }
    }

    private void parseSubject(NodeList subjectList, GameAction newAction){
        for (int l = 0; l < subjectList.getLength(); l++) {
            Element subject = (Element) subjectList.item(l);
            for (int t = 0; t < subject.getElementsByTagName("entity").getLength(); t++) {
                newAction.addSubjects(subject.getElementsByTagName("entity").item(t).getTextContent());
            }
        }
    }

    private void parseConsumed(NodeList consumedList, GameAction newAction){
        for (int l = 0; l < consumedList.getLength(); l++) {
            Element consumed = (Element) consumedList.item(l);
            for (int t = 0; t < consumed.getElementsByTagName("entity").getLength(); t++) {
                newAction.addConsumed(consumed.getElementsByTagName("entity").item(t).getTextContent());
            }
        }
    }

    private void parseProduced(NodeList producedList, GameAction newAction){
        for (int l = 0; l < producedList.getLength(); l++) {
            Element produced = (Element) producedList.item(l);
            for (int t = 0; t < produced.getElementsByTagName("entity").getLength(); t++) {
                newAction.addProduced(produced.getElementsByTagName("entity").item(t).getTextContent());
            }
        }
    }
}
