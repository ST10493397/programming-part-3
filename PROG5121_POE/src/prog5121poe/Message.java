package prog5121poe;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Message {
private ArrayList<String> sentMessages = new ArrayList<>();
private ArrayList<String> disregardedMessages = new ArrayList<>();
private ArrayList<String> storedMessages = new ArrayList<>();
private ArrayList<String> messageHashes = new ArrayList<>();
private ArrayList<String> messageIDs = new ArrayList<>();
private ArrayList<String> recipients = new ArrayList<>();

private int totalMessagesSent = 0;
private StringBuilder allMessagesLog = new StringBuilder();

public boolean checkMessageID(String messageID) {
if (messageID == null) return false;
return messageID.length() <= 10;
}

public String checkRecipientCell(String cellNumber) {
if (cellNumber != null && cellNumber.startsWith("+") && cellNumber.length() <= 12) {
return "Cell phone number successfully captured.";
} else {
return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
}
}

public String createMessageHash(String messageID, int messageNum, String messageText) {
if (messageID == null || messageID.length() < 2) {
return "00:" + messageNum + ":ERROR";
}
String prefix = messageID.substring(0, 2);
String[] words = messageText.trim().split("\\s+");
String firstWord = words.length > 0 ? words[0].toUpperCase() : "";
String lastWord = words.length > 1 ? words[words.length - 1].toUpperCase() : firstWord;

return prefix + ":" + messageNum + ":" + firstWord + lastWord;
}

public String printMessages() {
return allMessagesLog.toString();
}

public int returnTotalMessagess() {
return totalMessagesSent;
}

public void incrementTotalMessages() {
this.totalMessagesSent++;
}

public void appendMessageLog(String logEntry) {
this.allMessagesLog.append(logEntry);
}

@SuppressWarnings("unchecked")
public void storeMessage(String id, String hash, String recipient, String message, String flag) {
JSONObject jsonObject = new JSONObject();
jsonObject.put("messageID", id);
jsonObject.put("messageHash", hash);
jsonObject.put("recipient", recipient);
jsonObject.put("message", message);
jsonObject.put("flag", flag);

messageIDs.add(id);
messageHashes.add(hash);
recipients.add(recipient);

if (flag.equalsIgnoreCase("Sent")) {
sentMessages.add(message);
} else if (flag.equalsIgnoreCase("Disregard") || flag.equalsIgnoreCase("Disregarded")) {
disregardedMessages.add(message);
} else if (flag.equalsIgnoreCase("Stored")) {
storedMessages.add(message);
}

try (FileWriter file = new FileWriter("messages.json", true)) {
file.write(jsonObject.toJSONString() + "\n");
System.out.println("Message successfully stored to JSON file.");
} catch (IOException e) {
System.out.println("Error writing to JSON file: " + e.getMessage());
}
}

public void readJsonFileIntoArrays() {
sentMessages.clear();
disregardedMessages.clear();
storedMessages.clear();
messageHashes.clear();
messageIDs.clear();
recipients.clear();

JSONParser parser = new JSONParser();
try (FileReader reader = new FileReader("messages.json")) {
java.util.Scanner scanner = new java.util.Scanner(reader);
while (scanner.hasNextLine()) {
String line = scanner.nextLine();
if (!line.trim().isEmpty()) {
JSONObject obj = (JSONObject) parser.parse(line);
String id = (String) obj.get("messageID");
String hash = (String) obj.get("messageHash");
String rec = (String) obj.get("recipient");
String msg = (String) obj.get("message");
String flag = (String) obj.get("flag");

messageIDs.add(id);
messageHashes.add(hash);
recipients.add(rec);

if ("Sent".equalsIgnoreCase(flag)) {
sentMessages.add(msg);
} else if ("Disregard".equalsIgnoreCase(flag) || "Disregarded".equalsIgnoreCase(flag)) {
disregardedMessages.add(msg);
} else if ("Stored".equalsIgnoreCase(flag)) {
storedMessages.add(msg);
}
}
}
scanner.close();
System.out.println("JSON file successfully read and arrays populated.");
} catch (IOException | ParseException e) {
System.out.println("Error reading JSON file: " + e.getMessage());
}
}

public String displaySendersAndRecipients() {
StringBuilder sb = new StringBuilder();
for (int i = 0; i < messageIDs.size(); i++) {
sb.append("Recipient: ").append(recipients.get(i))
.append(" | Message ID: ").append(messageIDs.get(i)).append("\n");
}
return sb.length() > 0 ? sb.toString() : "No messages found.";
}

public String displayLongestMessage() {
if (messageIDs.isEmpty()) {
readJsonFileIntoArrays();
}
String longest = "";
for (String msg : storedMessages) {
if (msg.length() > longest.length()) {
longest = msg;
}
}
if (longest.isEmpty()) {
for (String msg : sentMessages) {
if (msg.length() > longest.length()) {
longest = msg;
}
}
}
return longest.isEmpty() ? "No messages available." : longest;
}

public String searchMessageID(String targetID) {
if (messageIDs.isEmpty()) {
readJsonFileIntoArrays();
}
for (int i = 0; i < messageIDs.size(); i++) {
if (messageIDs.get(i).equals(targetID)) {
String msgContent = "";
if (i < storedMessages.size()) msgContent = storedMessages.get(i);
else if (i < sentMessages.size()) msgContent = sentMessages.get(i);
else if (i < disregardedMessages.size()) msgContent = disregardedMessages.get(i);

return "Recipient: " + recipients.get(i) + "\nMessage: " + msgContent;
}
}
return "Message ID not found.";
}

public String searchRecipientMessages(String targetRecipient) {
if (messageIDs.isEmpty()) {
readJsonFileIntoArrays();
}
StringBuilder sb = new StringBuilder();
for (int i = 0; i < recipients.size(); i++) {
if (recipients.get(i).equals(targetRecipient)) {
String msg = "";
if (i < storedMessages.size()) msg = storedMessages.get(i);
else if (i < sentMessages.size()) msg = sentMessages.get(i);
sb.append(msg).append("\n");
}
}
return sb.length() > 0 ? sb.toString().trim() : "No messages found for this recipient.";
}

public String deleteMessageByHash(String targetHash) {
if (messageHashes.isEmpty()) {
readJsonFileIntoArrays();
}
for (int i = 0; i < messageHashes.size(); i++) {
if (messageHashes.get(i).equals(targetHash)) {
String removedMsg = "";
if (i < storedMessages.size()) removedMsg = storedMessages.get(i);
else if (i < sentMessages.size()) removedMsg = sentMessages.get(i);

messageHashes.remove(i);
messageIDs.remove(i);
recipients.remove(i);
if (i < storedMessages.size()) storedMessages.remove(i);
else if (i < sentMessages.size()) sentMessages.remove(i);

return "Message: \"" + removedMsg + "\" successfully deleted.";
}
}
return "Message hash not found.";
}

public String displayMessageReport() {
if (messageIDs.isEmpty()) {
readJsonFileIntoArrays();
}
StringBuilder sb = new StringBuilder("=== FULL MESSAGE REPORT ===\n");
for (int i = 0; i < messageIDs.size(); i++) {
sb.append("Hash: ").append(messageHashes.get(i)).append("\n")
.append("Recipient: ").append(recipients.get(i)).append("\n")
.append("ID: ").append(messageIDs.get(i)).append("\n---------------------------\n");
}
return sb.toString();
}

public ArrayList<String> getSentMessages() { return sentMessages; }
public ArrayList<String> getStoredMessages() { return storedMessages; }
}
