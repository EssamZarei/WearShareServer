import wearshareserver.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class WearShareClient {

    public static void main(String[] args) {
        Scanner inUser = new Scanner(System.in);
        System.out.println("Hi from Client side");

        int typeOfUser = validateUserType(inUser); // Validate and get the user type
        int logOrCreate = validateChoice(inUser); // Validate and get the login or create account choice
        int ID = 0;
        String name = "";
        String password = "";
        String location = "";
        String phoneNumber = "";

        if (logOrCreate == 4) { // If logging in
            ID = validateID(inUser); // Validate and get the ID
            password = validatePassword(inUser); // Validate and get the password
        } else if (logOrCreate == 5) { // If creating an account
            ID = validateID(inUser); // Validate and get the ID
            name = validateName(inUser); // Validate and get the name
            password = validatePassword(inUser); // Validate and get the password
            location = validateLocation(inUser); // Validate and get the location
            phoneNumber = ID + ""; // Set the phone number to the ID for simplicity
        }

        try (Socket server = new Socket("127.0.0.1", 1818); // Connect to the server
                Scanner inServer = new Scanner(server.getInputStream());
                PrintWriter outServer = new PrintWriter(server.getOutputStream(), true)) {

            //*2
            // Read welcome message from server
            String welcomeMessage = inServer.nextLine();
            System.out.println(welcomeMessage);

            //*3
            // Send user type (1, 2, or 3)
            String send = typeOfUser + "";
            outServer.println(send);

            //*6
            // Receive request to send 4 or 5 for login or create account
            String recv = inServer.nextLine();
            System.out.println("server: " + recv);

            //*7
            // Send 4 or 5 for login or create account
            send = logOrCreate + "";
            outServer.println(send);

            if (logOrCreate == 4) { // If logging in
                //*10
                // Receive message requesting ID
                recv = inServer.nextLine();
                System.out.println("server: " + recv);

                //*11
                // Send ID to server
                send = ID + "";
                outServer.println(send);

                //*14
                // Receive message requesting password
                recv = inServer.nextLine();
                System.out.println("server: " + recv);

                //*15
                // Send password to server
                send = password;
                outServer.println(send);

                if (ID == 518181815) { // Special case for ID 518181815
                    recv = inServer.nextLine();
                    System.out.println("server: Hello Essam! " + recv);
                    send = 1 + "";
                    outServer.println(send);

                    return;
                }

            } else { // If creating an account
                //*10
                // Receive message requesting ID
                recv = inServer.nextLine();
                if (recv.charAt(0) == '!') {
                    System.out.println("Server: " + recv);
                } else {
                    System.out.println("server: " + recv);
                    //*11
                    // Send ID
                    outServer.println(ID);
                    //*14
                    // Receive message requesting name
                    recv = inServer.nextLine();
                    System.out.println("server: " + recv);
                    //*15
                    // Send name
                    outServer.println(name);
                    //*18
                    // Receive message requesting password
                    recv = inServer.nextLine();
                    System.out.println("server: " + recv);
                    //*19
                    // Send password
                    outServer.println(password);
                    //*22
                    // Receive message requesting location
                    recv = inServer.nextLine();
                    System.out.println("server: " + recv);
                    //*23
                    // Send location
                    outServer.println(location);
                    //*26
                    // Receive message requesting phone number
                    recv = inServer.nextLine();
                    System.out.println("server: " + recv);
                    //*27
                    // Send phone number
                    outServer.println(phoneNumber);

                    System.out.println("New Account Created");
                }
            }
            recv = inServer.nextLine();
            if (recv.charAt(0) == '!') {
                System.out.println(recv);
                return;
            } else {
                // For Donor operations
                if (typeOfUser == 1) {
                    //*30
                    // Receive message for donating clothes or exchanging points

                    System.out.println("server: " + recv);
                    int donorAction = validateDonorAction(inUser); // Validate and get donor action
                    System.out.println("Donor action: " + (donorAction == 1 ? "Donate clothes" : "Exchange points"));
                    //*31
                    // Send action number
                    send = donorAction + "";
                    outServer.println(send);

                    if (donorAction == 1) { // If donating clothes
                        // Enter clothes information
                        String clothingType = validateClothingType(inUser); // Validate and get clothing type
                        int clothingSize = validateClothingSize(inUser); // Validate and get clothing size
                        //*34
                        // Receive message waiting for clothes type
                        recv = inServer.nextLine();
                        System.out.println("server: " + recv);
                        //*35
                        // Send clothes type
                        outServer.println(clothingType);
                        //*38
                        // Receive message waiting for clothes size
                        recv = inServer.nextLine();
                        System.out.println("server: " + recv);
                        //*39
                        // Send clothes size
                        send = clothingSize + "";
                        outServer.println(send);
                        recv = inServer.nextLine();
                        if (recv.charAt(0) == '!') {
                            System.out.println(recv);
                            return;
                        }
                    } else { // If exchanging points
                        recv = inServer.nextLine();
                        System.out.println("server: " + recv);
                    }

                } else if (typeOfUser == 2) { // For Association operations
                    // Add Association operations here
                } else if (typeOfUser == 3) { // For Store operations
                    //*42
                    // Receive message for editing promotion or adding promotion code
                    recv = inServer.nextLine();
                    System.out.println("server: " + recv);
                    int storeAction = validateStoreAction(inUser); // Validate and get store action
                    System.out.println("Store action: " + (storeAction == 1 ? "Edit promotion" : "Add code"));
                    //*43
                    // Send action number
                    send = storeAction + "";
                    outServer.println(send);

                    if (storeAction == 1) { // If editing promotion
                        //*46
                        // Receive message waiting for promotion
                        recv = inServer.nextLine();
                        System.out.println("server: " + recv);
                        //*47
                        // Send promotion
                        int promotion = validatePromotion(inUser); // Validate and get promotion
                        outServer.println(promotion);
                    } else { // If adding promotion code
                        //*50
                        // Receive message waiting for promotion code
                        recv = inServer.nextLine();
                        System.out.println("server: " + recv);
                        //*51
                        // Send promotion code
                        String promotionCode = validatePromotionCode(inUser); // Validate and get promotion code
                        outServer.println(promotionCode);
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Validate user type input
    public static int validateUserType(Scanner scanner) {
        while (true) {
            System.out.println("Enter user type (1 for Donor, 2 for Association, 3 for Store):");
            String input = scanner.nextLine();
            if (input.length() <= 200 && (input.equals("1") || input.equals("2") || input.equals("3"))) {
                return Integer.parseInt(input);
            } else {
                System.out.println("Invalid user type. Please enter 1, 2, or 3.");
            }
        }
    }

    // Validate choice for login or create account
    public static int validateChoice(Scanner scanner) {
        while (true) {
            System.out.println("Enter 4 to login or 5 to create an account:");
            String input = scanner.nextLine();
            if (input.length() <= 200 && (input.equals("5") || input.equals("4"))) {
                return Integer.parseInt(input);
            } else {
                System.out.println("Invalid choice. Please enter 4 or 5.");
            }
        }
    }

    // Validate and get ID
    public static int validateID(Scanner scanner) {
        while (true) {
            System.out.println("Enter ID:");
            String input = scanner.nextLine();
            if (input.length() <= 200 && input.matches("\\d{9}") && input.charAt(0) == '5') {
                return Integer.parseInt(input);
            } else {
                System.out.println("Invalid ID. ID must be a 9-digit number AND start with 5.");
            }
        }
    }

    // Validate and get name
    public static String validateName(Scanner scanner) {
        while (true) {
            System.out.println("Enter name:");
            String input = scanner.nextLine();
            if (input.length() <= 200 && input.matches("[a-zA-Z]+")) {
                return input;
            } else {
                System.out.println("Invalid name. Name must contain only characters.");
            }
        }
    }

    // Validate and get password
    public static String validatePassword(Scanner scanner) {
        while (true) {
            System.out.println("Enter password:");
            String input = scanner.nextLine();
            if (input.length() >= 8 && input.length() <= 200) {
                return input;
            } else {
                System.out.println("Invalid password. Password must contain at least 8 characters.");
            }
        }
    }

    // Validate and get location
    public static String validateLocation(Scanner scanner) {
        while (true) {
            System.out.println("Enter location (city name):");
            String input = scanner.nextLine().toLowerCase();
            if (input.length() <= 200) {
                System.out.println("You entered: " + input + ". Is this correct? (yes/no)");
                String confirmation = scanner.nextLine().toLowerCase();
                if (confirmation.equals("yes")) {
                    return input;
                } else {
                    System.out.println("Let's try again.");
                }
            } else {
                System.out.println("Invalid location. Location must not exceed 200 characters.");
            }
        }
    }

    // Validate and get donor action choice
    public static int validateDonorAction(Scanner scanner) {
        while (true) {
            System.out.println("If you want to donate clothes, press 1. If you want to exchange points, enter 2:");
            String input = scanner.nextLine();
            if (input.length() <= 200 && (input.equals("1") || input.equals("2"))) {
                return Integer.parseInt(input);
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }

    // Validate and get clothing type
    public static String validateClothingType(Scanner scanner) {
        while (true) {
            System.out.println("Enter clothing type (1 for Men, 2 for Women, 3 for Men Child, 4 for Women Child):");
            String input = scanner.nextLine();
            if (input.length() <= 200) {
                switch (input) {
                    case "1":
                        return "Men";
                    case "2":
                        return "Women";
                    case "3":
                        return "Men Child";
                    case "4":
                        return "Women Child";
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
                }
            } else {
                System.out.println("Invalid choice. Input must not exceed 200 characters.");
            }
        }
    }

    // Validate and get clothing size
    public static int validateClothingSize(Scanner scanner) {
        while (true) {
            System.out.println("Enter clothing size (integer between 1 and 60):");
            String input = scanner.nextLine();
            try {
                int size = Integer.parseInt(input);
                if (input.length() <= 200 && size >= 1 && size <= 60) {
                    return size;
                } else {
                    System.out.println("Invalid size. Size must be an integer between 1 and 60.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    // Validate and get store action choice
    public static int validateStoreAction(Scanner scanner) {
        while (true) {
            System.out.println("If you want to edit a promotion, press 1. If you want to add a promotion code, press 2:");
            String input = scanner.nextLine();
            if (input.length() <= 200 && (input.equals("1") || input.equals("2"))) {
                return Integer.parseInt(input);
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }

    // Validate and get promotion details
    public static int validatePromotion(Scanner scanner) {
        while (true) {
            System.out.println("Enter the promotion details:");
            String input = scanner.nextLine().trim();
            try {
                if (input.length() <= 200) {
                    int number = Integer.parseInt(input);
                    return number;
                } else {
                    System.out.println("Invalid promotion. Input must not exceed 200 characters.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid promotion. Please enter a valid integer number.");
            }
        }
    }

    // Validate and get promotion code
    public static String validatePromotionCode(Scanner scanner) {
        while (true) {
            System.out.println("Enter the promotion code:");
            String input = scanner.nextLine();
            if (input.length() <= 200 && input.matches("\\w+")) {
                return input;
            } else {
                System.out.println("Invalid promotion code. Code must contain only alphanumeric characters and must not exceed 200 characters.");
            }
        }
    }
}
