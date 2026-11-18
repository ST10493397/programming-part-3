package prog5121poe;

import java.util.Scanner;

public class Main {
public static void main(String[] args) {
Scanner input = new Scanner(System.in);
Login authSystem = new Login();
Message messageHandler = new Message();

System.out.println("=== PART 1: LOGIN & REGISTRATION ===");

System.out.print("Create Username (Must have '_' and be max 5 chars): ");
String regUser = input.nextLine();
System.out.print("Create Password (Min 8 chars, 1 Capital, 1 Number, 1 Special): ");
String regPass = input.nextLine();
System.out.print("Enter first name: ");
String fName = input.nextLine();
System.out.print("Enter last name: ");
String lName = input.nextLine();
System.out.print("Enter cell phone number (+27 format): ");
String regCell = input.nextLine();

String regResult = authSystem.registerUser(regUser, regPass, fName, lName, regCell);
System.out.println("\nSystem Status: " + regResult);

// Login Loop
System.out.println("\n--- WELCOME TO LOGIN ---");
while (true) {
System.out.print("Enter Username: ");
String loginUser = input.nextLine();
System.out.print("Enter Password: ");
String loginPass = input.nextLine();

boolean isSuccess = authSystem.loginUser(loginUser, loginPass);
String finalOutput = authSystem.returnLoginStatus(loginUser, loginPass);
System.out.println("\nResult: " + finalOutput);

if (isSuccess) {
break;
}
System.out.println("Please check your entries and try logging in again.\n");
}

// Part 2 & 3: QuickChat Messaging Loop
System.out.println("\n=== PART 2 & 3: QUICKCHAT MESSAGING ===");
boolean quit = false;

while (!quit) {
System.out.println("\nChoose an option:");
System.out.println("1. Send a message");
System.out.println("2. Show recently sent messages");
System.out.println("3. Stored Messages (Part 3 Menu)");
System.out.println("4. Quit");
System.out.print("Enter choice (1-4): ");

String choiceStr = input.nextLine();
int choice;
try {
choice = Integer.parseInt(choiceStr);
} catch (NumberFormatException e) {
System.out.println("Invalid input. Please enter a number between 1 and 4.");
continue;
}

switch (choice) {
case 1:
String msgID = "";
while (true) {
System.out.print("Enter Message ID (max 10 characters): ");
msgID = input.nextLine();
if (messageHandler.checkMessageID(msgID)) {
System.out.println("Message ID successfully captured.");
break;
} else {
System.out.println("Message ID is invalid or exceeds 10 characters. Please re-enter.");
}
}

String recipientCell = "";
while (true) {
System.out.print("Enter Recipient Cell Number (must start with + and be <= 12 chars): ");
recipientCell = input.nextLine();
String cellCheckResult = messageHandler.checkRecipientCell(recipientCell);
System.out.println(cellCheckResult);
if (cellCheckResult.contains("successfully captured")) {
break;
}
}

System.out.print("Enter message content: ");
String messageText = input.nextLine();

System.out.print("Enter message flag (Sent / Stored / Disregard): ");
String flag = input.nextLine();

messageHandler.incrementTotalMessages();
int msgNum = messageHandler.returnTotalMessagess();
String hash = messageHandler.createMessageHash(msgID, msgNum, messageText);

String logEntry = "ID: " + msgID + " | Hash: " + hash + " | To: " + recipientCell + " | Msg: " + messageText + "\n";
messageHandler.appendMessageLog(logEntry);

messageHandler.storeMessage(msgID, hash, recipientCell, messageText, flag);
System.out.println("\nGenerated Message Hash: " + hash);
break;

case 2:
System.out.println("\n--- All Sent Messages Log ---");
String logs = messageHandler.printMessages();
if (logs.isEmpty()) {
System.out.println("No messages sent yet.");
} else {
System.out.print(logs);
}
break;

case 3:
boolean back = false;
while (!back) {
System.out.println("\n--- STORED MESSAGES MENU (PART 3) ---");
System.out.println("a. Display sender and recipient of all stored messages");
System.out.println("b. Display the longest stored message");
System.out.println("c. Search for a message ID");
System.out.println("d. Search messages for a particular recipient");
System.out.println("e. Delete a message using message hash");
System.out.println("f. Display full message report");
System.out.println("g. Back to main menu");
System.out.print("Choose an option (a-g): ");

String subChoice = input.nextLine().toLowerCase();
switch (subChoice) {
case "a":
System.out.println("\n" + messageHandler.displaySendersAndRecipients());
break;
case "b":
System.out.println("\nLongest Message: " + messageHandler.displayLongestMessage());
break;
case "c":
System.out.print("Enter Message ID to search: ");
String searchID = input.nextLine();
System.out.println("\n" + messageHandler.searchMessageID(searchID));
break;
case "d":
System.out.print("Enter Recipient cell to search: ");
String searchRec = input.nextLine();
System.out.println("\n" + messageHandler.searchRecipientMessages(searchRec));
break;
case "e":
System.out.print("Enter Message Hash to delete: ");
String delHash = input.nextLine();
System.out.println("\n" + messageHandler.deleteMessageByHash(delHash));
break;
case "f":
System.out.println("\n" + messageHandler.displayMessageReport());
break;
case "g":
back = true;
break;
default:
System.out.println("Invalid sub-option. Please choose a-g.");
}
}
break;

case 4:
quit = true;
System.out.println("Exiting application. Goodbye!");
break;

default:
System.out.println("Invalid choice. Please select 1 to 4.");
}
}
input.close();
}
}
