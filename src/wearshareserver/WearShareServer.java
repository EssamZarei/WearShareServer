package wearshareserver;

import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

public class WearShareServer {

    public static void main(String[] args) {

        System.out.println("I am Server side");

//Connection conn = null;
//try {
//    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wearshareserverdb", "root", Clothes.w);
//    System.out.println(conn.getCatalog());
//} catch (Exception ex) {
//            System.err.println("Failed to connect to the database: " + ex.getMessage());
//            ex.printStackTrace(); // Print the stack trace
//}
//        
        try (ServerSocket sk = new ServerSocket(1818);) {
            System.out.println("Server start listening on Port: " + 1818);

            // to handle multi clients
            while (true) {
                // accept a client
                Socket clientSocket = sk.accept();
                // make a Thread to handle the client
                new Thread(new multiClientThreaded(clientSocket)).start();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static class multiClientThreaded implements Runnable {

        private Socket skClient;

        public multiClientThreaded(Socket skClient) {
            this.skClient = skClient;
        }

        @Override
        public void run() {

            try (Scanner inClient = new Scanner(skClient.getInputStream());
                    PrintWriter outClient = new PrintWriter(skClient.getOutputStream(), true);
                    Scanner inUser = new Scanner(System.in);
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wearshareserverdb", "root", Clothes.w);) {

                //*1
                String welcomeMSG = "Welcome to our WearShare system, the latest version of donation clothes !! Im recieving you type of user please wait ...";
                outClient.println(welcomeMSG);

                //*4
                // take 1 or 2 or 3 from client and show it
                String recv = inClient.nextLine();
                int typeOfClient = Integer.parseInt(recv);
                System.out.println("Client: " + recv);

                //*5
                // send msg of 4 5
                String send = "Wait sending log in or create an account ...";
                outClient.println(send);

                //*8 recv from clint if 4 or 5
                recv = inClient.nextLine();
                int logOrCreate = Integer.parseInt(recv);
                System.out.println("Client: " + recv);

                // turn to send
                int ID;
                String name;
                String location;
                String phone;
                String password;

                if (logOrCreate == 4) {
                    //*9
                    //here has to send for ID
                    send = "Wait Entering ID ...";
                    outClient.println(send);
                    //*12
                    // recv ID
                    recv = inClient.nextLine();
                    ID = Integer.parseInt(recv);
                    //*13
                    // send msg for request password
                    send = "Wait Entering Password ...";
                    outClient.println(send);

                    //*16
                    //recv password
                    recv = inClient.nextLine();
                    password = recv;
                    // turn to send MSG
                    // encrypted pass?
                    // database check about ID and password
                    System.out.println("ID: " + ID);
                    System.out.println("Pass: " + password);

                    // if wrong system.
                    //return;
                    // I know return is not solution but we focus to make it harder for hackers to try again :)
                    if (!isValidUser(conn, ID, password, typeOfClient)) {
                        send = "!Wrong ID OR Password :(   Please try again :)";
                        outClient.println(send);
                        return; // If the credentials are invalid, end the thread
                    }

                } // create new account
                else {
                    // here mean have to create account
                    //*9
                    send = "Wait Entering ID ...";
                    outClient.println(send);
                    //*12
                    // recv ID
                    ID = Integer.parseInt(inClient.nextLine());
                    System.out.println("ID entered " + ID);
                    //*13
                    // send waiting name
                    send = "Wait Entering Name ...";
                    outClient.println(send);
                    //*16
                    // recv name
                    name = inClient.nextLine();
                    System.out.println("name entered " + name);
                    //*17
                    // send waiting password
                    send = "Wait Entering Password ...";
                    outClient.println(send);
                    //*20
                    //recv password
                    password = inClient.nextLine();
                    System.out.println("password entered");
                    //*21
                    // send waiting for location
                    send = "Wait Entering Location ...";
                    outClient.println(send);
                    //*24
                    //recv location
                    location = inClient.nextLine();
                    System.out.println("lpcation entered " + location);
                    //*25
                    // MSG waiting Phone Number
                    send = "Wait Entering Phone Number ...";
                    outClient.println(send);
                    //*28
                    // recv phone number
                    phone = inClient.nextLine();

                    // insert the user into database according to his type
                    createAccount(conn, ID, name, password, location, phone, typeOfClient);

                    System.out.println("Account created: ID = " + ID + ", Name = " + name + ", Location = " + location + ", Phone = " + phone + ", Password = You want to see my pass hhhhhh");

                    System.out.println("                int ID = " + ID + ";");
                    System.out.println("                String name = \"" + name + "\";");
                    System.out.println("                String location = \"" + location + "\";");
                    System.out.println("                String phone = \"" + phone + "\";");
                    System.out.println("                String password = \"" + password + "\";");

                }

                // reaching this line mean the user log in or creates his account
                // user now want to do his actions
                //     ---     ---     Part OF Users Oprations     ---     ---
                // for Donor
                if (typeOfClient == 1) {

                    //*29
                    //send MSG of Donor actions
                    send = "Hi Donor, Enter 1 for clothes donation OR 2 for exchange your points";
                    outClient.println(send);

                    //*32
                    //recv action number
                    String donorAction = inClient.nextLine();

                    if (donorAction.equals("1")) {
                        // donate clothes
                        //*33
                        //send MSG waiting clothes type
                        send = "Wait Entering clothes type ...";
                        outClient.println(send);
                        //*36
                        //recv clothes type
                        recv = inClient.nextLine();
                        String clothingType = recv;
                        //*37
                        // send MSG waiting clothes size
                        send = "Wait Entering clothes size ...";
                        outClient.println(send);
                        //*40
                        // recv MSG of clothes size
                        recv = inClient.nextLine();
                        int clothingSize = Integer.parseInt(recv);

                        //here done of donating ation
                        // must send to database information of clothes and user
                        // based on City of the Donor will assign the association
                    } else {
                        // exchange points
                        // give points from database according to ID
                        String points = "0";

                    }

                } // for Association
                else if (typeOfClient == 2) {

                    // database print all the clothes and users information where
                    // where the ID specific Association
                } // for Store
                else if (typeOfClient == 3) {
                    //*41
                    //send MSG of Store actions
                    send = "Hi Store, enter 1 to edit promotion OR 2 to add code";
                    outClient.println(send);

                    //*44
                    //recv action number
                    String storeAction = inClient.nextLine();

                    if (storeAction.equals("1")) {
                        // edit promotion
                        //*45
                        //send MSG waiting for promotion
                        send = "Wait Entering promotion ...";
                        outClient.println(send);
                        //*48
                        //recv promotion
                        recv = inClient.nextLine();
                        int promotion = Integer.parseInt(recv);
                        System.out.println("Promotion: " + promotion);
                        // send the updated promotion to database

                    } else {
                        // add promotion code
                        //*49
                        //send MSG waiting for code
                        send = "Wait Entering code ...";
                        outClient.println(send);
                        //*52
                        //recv promotion code
                        recv = inClient.nextLine();
                        String promotionCode = recv;
                        System.out.println("Code: " + promotionCode);
                        // update code in database
                    }
                }

            } catch (SQLException ex) {
                System.err.println("Failed to connect to the database: " + ex.getMessage());
                ex.printStackTrace();
            } catch (IOException ex) {
                System.err.println("Error from multi client threaded");
                ex.printStackTrace();
            }

        }
    }

    public static void p() {
        System.out.println("usssee from run");
    }

    public static boolean isValidUser(Connection conn, int ID, String password, int typeOfClient) throws SQLException {
        String tableName = getTableName(typeOfClient);

        String query = "SELECT * FROM " + tableName + " WHERE id = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ID);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If a row is found credentials are valid
            }
        }
    }

    public static void createAccount(Connection conn, int ID, String name, String password, String location, String phone, int typeOfClient) throws SQLException {
        String tableName = getTableName(typeOfClient);

        String query = "INSERT INTO " + tableName + " (id, name, password, location, phone_number) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ID);
            stmt.setString(2, name);
            stmt.setString(3, password);
            stmt.setString(4, location);
            stmt.setString(5, phone);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Account created successfully."); // Print a message indicating success
            } else {
                System.out.println("Failed to create account."); // Print a message indicating failure
            }
        } catch (SQLException ex) {
            System.err.println("Error creating account: " + ex.getMessage());
            ex.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public static String getTableName(int typeOfClient) {
        if (typeOfClient == 1) {
            return "Donor";
        }
        if (typeOfClient == 2) {
            return "Association";
        }
        return "Store";
    }
}
