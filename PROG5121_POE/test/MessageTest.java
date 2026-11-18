package prog5121poe;

import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

@Test
public void testSentMessagesArrayPopulated() {
Message msgHandler = new Message();
msgHandler.storeMessage("0123456789", msgHandler.createMessageHash("0123456789", 1, "Did you get the cake?"), "+27834557896", "Did you get the cake?", "Sent");
msgHandler.storeMessage("0838884567", msgHandler.createMessageHash("0838884567", 2, "It is dinner time!"), "0838884567", "It is dinner time!", "Sent");

assertEquals(2, msgHandler.getSentMessages().size());
assertEquals("Did you get the cake?", msgHandler.getSentMessages().get(0));
assertEquals("It is dinner time!", msgHandler.getSentMessages().get(1));
}

@Test
public void testDisplayLongestMessage() {
Message msgHandler = new Message();
msgHandler.storeMessage("ID1", msgHandler.createMessageHash("ID1", 1, "Short"), "+27838884567", "Short", "Stored");
msgHandler.storeMessage("ID2", msgHandler.createMessageHash("ID2", 2, "Where are you? You are late! I have asked you to be on time."), "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");

String longest = msgHandler.displayLongestMessage();
assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
}

@Test
public void testSearchMessageID() {
Message msgHandler = new Message();
String hash = msgHandler.createMessageHash("0838884567", 1, "It is dinner time!");
msgHandler.storeMessage("0838884567", hash, "+27838884567", "It is dinner time!", "Sent");

String result = msgHandler.searchMessageID("0838884567");
assertTrue(result.contains("It is dinner time!"));
}

@Test
public void testSearchRecipientMessages() {
Message msgHandler = new Message();
String hash1 = msgHandler.createMessageHash("ID1", 1, "Where are you? You are late! I have asked you to be on time.");
String hash2 = msgHandler.createMessageHash("ID2", 2, "Ok, I am leaving without you.");

msgHandler.storeMessage("ID1", hash1, "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
msgHandler.storeMessage("ID2", hash2, "+27838884567", "Ok, I am leaving without you.", "Stored");

String results = msgHandler.searchRecipientMessages("+27838884567");
assertTrue(results.contains("Where are you?"));
assertTrue(results.contains("Ok, I am leaving without you."));
}

@Test
public void testDeleteMessageByHash() {
Message msgHandler = new Message();
String hash = msgHandler.createMessageHash("ID2", 1, "Where are you? You are late! I have asked you to be on time.");
msgHandler.storeMessage("ID2", hash, "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");

String deleteResult = msgHandler.deleteMessageByHash(hash);
assertTrue(deleteResult.contains("successfully deleted"));
}
}
