package edu.uob;

import com.alexmerz.graphviz.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;

/** This class implements the STAG server. */
public final class GameServer {
    GameState gameState = new GameState();
    private static final char END_OF_TRANSMISSION = 4;


    public static void main(String[] args) throws IOException {
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        server.blockingListenOn(8888);
    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.GameServer(File, File)}) otherwise we won't be able to mark
    * your submission correctly.
    *
    * <p>You MUST use the supplied {@code entitiesFile} and {@code actionsFile}
    *
    * @param entitiesFile The game configuration file containing all game entities to use in your game
    * @param actionsFile The game configuration file containing all game actions to use in your game
    *
    */
    public GameServer(File entitiesFile, File actionsFile) {
        // TODO implement your server logic here

        GameParser parser = new GameParser(entitiesFile, actionsFile, gameState);
        try {
            parser.parseEntities();
            parser.parseAction();
        } catch (ParseException | GameParseException | ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }

    /**
    * KEEP this signature (i.e. {@code edu.uob.GameServer.handleCommand(String)}) otherwise we won't be
    * able to mark your submission correctly.
    *
    * <p>This method handles all incoming game commands and carries out the corresponding actions.
    */
    public String handleCommand(String command) {
        // TODO implement your server logic here
        if (command.isEmpty()) {
            try {
                throw new GameCommandException("Failed to read command from player");
            } catch (GameCommandException e) {
                return "ERROR: " + e;
            }
        }
        String returnStatement;
        command = command.toLowerCase();
        GameTokenizer gameTokenizer = new GameTokenizer(command);

        try {
            Player player;
            String playerName = gameTokenizer.getPlayerName();
            if (!gameState.checkIfPlayerExist(playerName)) {
                player = new Player(playerName, gameState.getFirstLocation());
                player.setPlayerLocation(gameState.getFirstLocation());
                gameState.addPlayerIntoMap(playerName);
                gameState.setCurrentPlayer(playerName);
            } else {
                player = gameState.getPlayerFromMap(playerName);
                gameState.setCurrentPlayer(playerName);
            }

            ArrayList<String> tokens = gameTokenizer.gameTokens();
            GameController gameController = new GameController(gameState, player, tokens);

            returnStatement = gameController.interpretCommand();
            gameState.setPlayerIntoMap(player);
        } catch (GameTokenizeException | GameInterpretException | GameAdditionalCmdException e) {
            return "ERROR: " + e;
        }

        return returnStatement;
    }

    /**
     * Starts a *blocking* socket server listening for new connections. This method blocks until the
     * current thread is interrupted.
     *
     * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
     * you want to.
     *
     * @param portNumber The port to listen on.
     * @throws IOException If any IO related operation fails.
     */
    public void blockingListenOn(int portNumber) throws IOException {
        try (ServerSocket s = new ServerSocket(portNumber)) {
            System.out.println("Server listening on port " + portNumber);
            while (!Thread.interrupted()) {
                try {
                    blockingHandleConnection(s);
                } catch (IOException e) {
                    System.out.println("Connection closed");
                }
            }
        }
    }

    /**
     * Handles an incoming connection from the socket server.
     *
     * <p>This method isn't used for marking. You shouldn't have to modify this method, but you can if
     * * you want to.
     *
     * @param serverSocket The client socket to read/write from.
     * @throws IOException If any IO related operation fails.
     */
    private void blockingHandleConnection(ServerSocket serverSocket) throws IOException {
        try (Socket s = serverSocket.accept();
             BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
            System.out.println("Connection established");
            String incomingCommand = reader.readLine();
            if(incomingCommand != null) {
                System.out.println("Received message from " + incomingCommand);
                String result = handleCommand(incomingCommand);
                writer.write(result);
                writer.write("\n" + END_OF_TRANSMISSION + "\n");
                writer.flush();

            }
        }
    }
}