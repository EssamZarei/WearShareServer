
import wearshareserver.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class WearShareClient {

    public static void main(String[] args) {
        Scanner inUser = new Scanner(System.in);
        System.out.println("I am client side");

        int typeOfUser = validateUserType(inUser);
        int logOrCreate = validateChoice(inUser);
        int ID = 0;
        String name = "";
        String password = "";
        String location = "";
        String phoneNumber = "";

        if (logOrCreate == 4) {
            login(inUser);
        } else if (logOrCreate == 5) {
            ID = validateID(inUser);
            name = validateName(inUser);
            password = validatePassword(inUser);
            location = validateLocation(inUser);
            phoneNumber = ID + "";
        }
        
        
        
        
        
        try (Socket server = new Socket("127.0.0.1", 1818);
                Scanner inServer = new Scanner(server.getInputStream());
                PrintWriter outServer = new PrintWriter(server.getOutputStream(), true);) {

            //*2
            // Read welcome message from server
            String welcomeMessage = inServer.nextLine();
            System.out.println(welcomeMessage);

            //*3
            // to send 1 2 3
            String send = typeOfUser + "";
            outServer.println(send);

            //*6
            // recv request of send 4 5 for log in or create account
            String recv = inServer.nextLine();
            System.out.println("server: " + recv);

            //*7
            // send 4 or 5 for log in or create account
            send = logOrCreate + "";
            outServer.println(send);

            //
            if (logOrCreate == 4) {

                //*10
                // recv msg request of ID
                recv = inServer.nextLine();
                System.out.println("server: " + recv);

                //*11
                //send ID to server
                send = ID + "";
                outServer.println(send);

                //*14
                // recv msg of request password
                recv = inServer.nextLine();
                System.out.println("server: " + recv);

                //*15
                // send password to server
                send = password;
                outServer.println(send);
            } // else mean create new account
            else {

                if (typeOfUser == 1 || typeOfUser == 2) {
                    //*10
                    // recieving MSG
                    recv = inServer.nextLine();
                    System.out.println("server: " + recv);
                    //*11
                    // send int ID
                    outServer.println(ID);
                    //*14
                    //MSG of entring Name
                    recv = inServer.nextLine();
                    System.out.println("server: " + recv);
                    //*15
                    // sending name
                    outServer.println(name);
                    //*18
                    // recv MSG waiting pass
                    recv = inServer.nextLine();
                    System.out.println("server: " + recv);
                    //*19
                    // send password
                    outServer.println(password);
                    //*22
                    // recv MSG waiting location
                    recv = inServer.nextLine();
                    System.out.println("server: " + recv);
                    //*23
                    // send location
                    outServer.println(location);
                    //*26
                    //recv MSG waiting phone number
                    recv = inServer.nextLine();
                    System.out.println("server: " + recv);
                    //*27
                    //send pgone number
                    outServer.println(phoneNumber);

                    System.out.println("New Account Created");

                    // here have to ask every time and send the value NOTE SUTE WHAT TURN
                } else if (typeOfUser == 3) {
                    // here have to ask every time and send the value NOTE SUTE WHAT TURN
                    String promotions = inUser.nextLine();
                    String code = inUser.nextLine();
                }
            } // End of else of create account

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
     public static int validateUserType(Scanner scanner) {
        while (true) {
            System.out.println("Enter user type (1 for Donor, 2 for Association, 3 for Store):");
            String input = scanner.nextLine();
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                return Integer.parseInt(input);
            } else {
                System.out.println("Invalid user type. Please enter 1, 2, or 3.");
            }
        }
    }
     
    public static int validateChoice(Scanner scanner) {
        while (true) {
            System.out.println("Enter 4 to login or 5 to create an account:");
            String input = scanner.nextLine();
            if (input.equals("5") || input.equals("4")) {
                return Integer.parseInt(input);
            } else {
                System.out.println("Invalid choice. Please enter 4 or 5.");
            }
        }
    }

        public static void login(Scanner scanner) {
        int ID = validateID(scanner);
        String password = validatePassword(scanner);

        System.out.println("Login successful!");
        System.out.println("ID: " + ID);
        System.out.println("Password: " + password);
    }
    
        public static int validateID(Scanner scanner) {
        while (true) {
            System.out.println("Enter ID:");
            String input = scanner.nextLine();
            if (input.matches("\\d{9}") && input.charAt(0) == '5') {
                return Integer.parseInt(input);
            } else {
                System.out.println("Invalid ID. ID must be a 9-digit number AND start with 5.");
            }
        }
    }

    public static String validateName(Scanner scanner) {
        while (true) {
            System.out.println("Enter name:");
            String input = scanner.nextLine();
            if (input.matches("[a-zA-Z]+")) {
                return input;
            } else {
                System.out.println("Invalid name. Name must contain only characters.");
            }
        }
    }

    public static String validatePassword(Scanner scanner) {
        while (true) {
            System.out.println("Enter password:");
            String input = scanner.nextLine();
            if (input.length() >= 8) {
                return input;
            } else {
                System.out.println("Invalid password. Password must contain at least 8 characters.");
            }
        }
    }

    public static String validateLocation(Scanner scanner) {
        while (true) {
            System.out.println("Enter location (city name):");
            String input = scanner.nextLine().toLowerCase();
            System.out.println("You entered: " + input + ". Is this correct? (yes/no)");
            String confirmation = scanner.nextLine().toLowerCase();
            if (confirmation.equals("yes")) {
                return input;
            } else {
                System.out.println("Let's try again.");
            }
        }
    }

    public static String validatePhoneNumber(Scanner scanner) {
        while (true) {
            System.out.println("Enter phone number:");
            String input = scanner.nextLine();
            if (input.matches("\\d+")) {
                return input;
            } else {
                System.out.println("Invalid phone number. Phone number must contain only digits.");
            }
        }
    }
    
}
