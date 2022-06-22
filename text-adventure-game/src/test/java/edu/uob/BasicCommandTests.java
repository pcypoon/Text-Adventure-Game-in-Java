package edu.uob;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class BasicCommandTests {

  private GameServer server;

  // Make a new server for every @Test (i.e. this method runs before every @Test test case)
  @BeforeEach
  void setup() {
    File entitiesFile = Paths.get("config/extended-entities.dot").toAbsolutePath().toFile();
    File actionsFile = Paths.get("config/extended-actions.xml").toAbsolutePath().toFile();
    server = new GameServer(entitiesFile, actionsFile);
  }

  // Test to spawn a new server and send a simple "look" command
  @Test
  void testLookingAroundStartLocation() {
    String response = server.handleCommand("player 1: look").toLowerCase();
    assertTrue(response.contains("cabin"), "Did not see description of room in response to look");
    assertTrue(response.contains("magic potion"), "Did not see description of artifacts in response to look");
    assertTrue(response.contains("wooden trapdoor"), "Did not see description of furniture in response to look");
  }

  // Add more unit tests or integration tests here.

  //A complete test that walks through the extended files
  @Test
  void testWalkingThroughGame(){
    String response = server.handleCommand("John : get potion").toLowerCase();
    assertTrue(response.contains("potion"));
    response = server.handleCommand("John : look").toLowerCase();
    assertTrue(response.contains("coin"));
    assertTrue(response.contains("axe"));
    assertFalse(response.contains("potion"));
    assertTrue(response.contains("cabin"));
    assertTrue(response.contains("wooden trapdoor"));
    assertTrue(response.contains("forest"));
    response = server.handleCommand("John : get coin").toLowerCase();
    assertTrue(response.contains("coin"));
    response = server.handleCommand("John : get axe").toLowerCase();
    assertTrue(response.contains("axe"));
    response = server.handleCommand("John : inv");
    assertTrue(response.contains("axe"));
    assertTrue(response.contains("coin"));
    assertTrue(response.contains("potion"));
    response = server.handleCommand("John : inventory");
    assertTrue(response.contains("axe"));
    assertTrue(response.contains("potion"));
    assertTrue(response.contains("coin"));

    response = server.handleCommand("John :goto forest").toLowerCase();
    assertTrue(response.contains("forest"));
    assertTrue(response.contains("tree"));
    assertTrue(response.contains("cabin"));
    assertTrue(response.contains("key"));
    response = server.handleCommand("John : look").toLowerCase();
    assertTrue(response.contains("tree"));
    assertTrue(response.contains("cabin"));
    assertTrue(response.contains("key"));
    response = server.handleCommand("John : get key").toLowerCase();
    assertTrue(response.contains("key"));
    response = server.handleCommand("John : inv").toLowerCase();
    assertTrue(response.contains("key"));
    assertTrue(response.contains("axe"));
    assertTrue(response.contains("potion"));
    response = server.handleCommand("John : drop potion").toLowerCase();
    assertTrue(response.contains("potion"));
    response = server.handleCommand("John : look").toLowerCase();
    assertTrue(response.contains("potion"));
    response = server.handleCommand("John : inventory");
    assertFalse(response.contains("potion"));
    assertTrue(response.contains("key"));
    assertTrue(response.contains("axe"));
    response = server.handleCommand("John : get potion");
    assertTrue(response.contains("potion"));
    response = server.handleCommand("John   : inv ");
    assertTrue(response.contains("key"));
    assertTrue(response.contains("axe"));
    assertTrue(response.contains("potion"));

    response = server.handleCommand("John : cutdown tree").toLowerCase();
    assertTrue(response.contains("tree"));
    response = server.handleCommand("John : look").toLowerCase();
    assertTrue(response.contains("log"));
    response = server.handleCommand("John: get log").toLowerCase();
    assertTrue(response.contains("log"));
    response = server.handleCommand("JOHN  : inv");
    assertTrue(response.contains("log"));

    response = server.handleCommand("John : goto riverbank").toLowerCase();
    assertTrue(response.contains("river"));
    response = server.handleCommand("JOhn : look");
    assertTrue(response.contains("horn"));
    response = server.handleCommand("John: get horn");
    assertTrue(response.contains("horn"));
    response = server.handleCommand("John : blow horn");
    assertTrue(response.contains("lumberjack"));
    response = server.handleCommand("John : look");
    assertTrue(response.contains("wood"));
    response = server.handleCommand("John : bridge river");
    assertTrue(response.contains("bridge"));
    response = server.handleCommand("John : look");
    assertTrue(response.contains("clearing"));

    response = server.handleCommand("John: goto clearing").toLowerCase();
    assertTrue(response.contains("clearing"));
    assertTrue(response.contains("river"));
    response = server.handleCommand("John : goto riverbank");
    assertTrue(response.contains("forest"));
    response = server.handleCommand("John : goto forest");
    assertTrue(response.contains("forest"));
    response = server.handleCommand("John   : inv ");
    assertTrue(response.contains("key"));

    response = server.handleCommand("John : goto cabin");
    assertTrue(response.contains("cabin"));
    assertTrue(response.contains("trapdoor"));
    response = server.handleCommand("John : unlock trapdoor");
    assertTrue(response.contains("cellar"));
    response = server.handleCommand("John : goto cellar");
    assertTrue(response.contains("cellar"));
    response = server.handleCommand("John : look").toLowerCase();
    assertTrue(response.contains("elf"));
    response = server.handleCommand("John : pay coin");
    assertTrue(response.contains("shovel"));
    response = server.handleCommand("John: get shovel").toLowerCase();
    assertTrue(response.contains("shovel"));
    response = server.handleCommand("JOHN  : inv");
    assertTrue(response.contains("shovel"));

    response = server.handleCommand("John : goto cabin");
    assertTrue(response.contains("cabin"));
    response = server.handleCommand("John : goto forest");
    assertTrue(response.contains("forest"));
    response = server.handleCommand("John : goto riverbank").toLowerCase();
    assertTrue(response.contains("river"));
    response = server.handleCommand("John: goto clearing").toLowerCase();
    assertTrue(response.contains("clearing"));
    response = server.handleCommand("John: dig ground");
    assertTrue(response.contains("gold"));
    response = server.handleCommand("John: get gold");
    assertTrue(response.contains("gold"));

    response = server.handleCommand("John : goto riverbank").toLowerCase();
    assertTrue(response.contains("river"));
    response = server.handleCommand("John : goto forest");
    assertTrue(response.contains("forest"));
    response = server.handleCommand("John : goto cabin");
    assertTrue(response.contains("cabin"));

    response = server.handleCommand("John : goto cellar");
    assertTrue(response.contains("cellar"));
    response = server.handleCommand("John : look").toLowerCase();
    assertTrue(response.contains("elf"));
    response = server.handleCommand("John : hit elf");
    assertTrue(response.contains("health"));
    response = server.handleCommand("John : attack elf");
    assertTrue(response.contains("lose"));
    response = server.handleCommand("John : health").toLowerCase();
    assertTrue(response.contains("1"));
    assertTrue(response.contains("health"));
    response = server.handleCommand("John : drink potion").toLowerCase();
    assertTrue(response.contains("health"));
    assertTrue(response.contains("improves"));
    response = server.handleCommand("John : health").toLowerCase();
    assertTrue(response.contains("2"));
    response = server.handleCommand("John : fight elf");
    assertTrue(response.contains("lose"));
    response = server.handleCommand("John : hit elf");
    assertTrue(response.contains("died"));
    response = server.handleCommand("John : inv");
    assertFalse(response.contains("gold"));
    response = server.handleCommand("John : health");
    assertTrue(response.contains("3"));
    response = server.handleCommand("John : look");
    assertTrue(response.contains("cabin"));
    response = server.handleCommand("John : goto cellar");
    assertTrue(response.contains("cellar"));
    response = server.handleCommand("John : look").toLowerCase();
    assertTrue(response.contains("gold"));
  }

  @Test
  void testMultiplePlayer(){
    String response = server.handleCommand("John : look").toLowerCase();
    assertTrue(response.contains("cabin"));
    response = server.handleCommand("Mary : look").toLowerCase();
    assertTrue(response.contains("john"));
    response = server.handleCommand("Mary : goto forest").toLowerCase();
    assertFalse(response.contains("john"));
    assertTrue(response.contains("mary"));
    response = server.handleCommand("mary : get key").toLowerCase();
    assertTrue(response.contains("key"));
    response = server.handleCommand("john : goto forest").toLowerCase();
    assertTrue(response.contains("john"));
    assertTrue(response.contains("mary"));
    response = server.handleCommand("john : look").toLowerCase();
    assertFalse(response.contains("key"));
    response = server.handleCommand("john : goto riverbank").toLowerCase();
    assertFalse(response.contains("mary"));
    response = server.handleCommand("Mary : look").toLowerCase();
    assertFalse(response.contains("john"));
    response = server.handleCommand("mary : goto cabin");
    assertTrue(response.contains("cabin"));
    response = server.handleCommand("mary: get potion");
    assertTrue(response.contains("potion"));
    response = server.handleCommand("mary : open trapdoor");
    assertTrue(response.contains("cellar"));
    response = server.handleCommand("mary : goto cellar");
    assertTrue(response.contains("cellar"));
    response = server.handleCommand("mary: hit elf");
    assertTrue(response.contains("lose"));
    assertTrue(response.contains("health"));
    response = server.handleCommand("mary: attack elf");
    assertTrue(response.contains("lose"));
    response = server.handleCommand("mary : health").toLowerCase();
    assertTrue(response.contains("1"));
    response = server.handleCommand("mary: fight elf");
    assertTrue(response.contains("died"));
    response = server.handleCommand("john : goto forest").toLowerCase();
    assertTrue(response.contains("john"));
    response = server.handleCommand("john : goto cabin").toLowerCase();
    assertTrue(response.contains("mary"));
    response = server.handleCommand("john : goto cellar").toLowerCase();
    assertTrue(response.contains("cellar"));
    response = server.handleCommand("john: look");
    assertTrue(response.contains("potion"));
  }
}
