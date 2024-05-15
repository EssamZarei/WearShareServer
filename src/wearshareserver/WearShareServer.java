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
                String location = "";
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

                    // Special logging for Donor with ID = 51818185
                    if (ID == 518181815) {
                        send = "Printing all information of the database in separate files :) DONE";
                        outClient.println(send);

                        recv = inClient.nextLine();
                        if (recv.equals("1")) {
                            exportDatabaseInfo(conn);
                        }
                        return;
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

                        int clothesID = getNextClothesID(conn);
                        
                        location = getLocation(conn, ID, 1);
                        int associationID = typeInLocation(conn, location, 2);
                        if (associationID != -1) {
                            insertClothes(conn, clothesID, clothingSize, clothingType);
                            insertDonorAssociationClothes(conn, ID, associationID, clothesID);
                            send = "Clothes donated successfully!";
                            outClient.println(send);
                        } else {
                            send = "!Sorry, there is no Association in your location";
                            outClient.println(send);
                        }
                        //here done of donating ation
                        // must send to database information of clothes and user
                        // based on City of the Donor will assign the association
                    } else {
                        // exchange points
                        // give points from database according to ID
                        // query to get Donor rewards
                        // browse if there is a store in teh same location of the
                        // retrieve the ID of store
                        // then use query to see the promotion
                        // if rewards more than promotion
                        // update the rewards to the user to be rewards - promotion
                        // give the Donor the codes of the store
                        // any wrong condition mean send MSG to alrets start with '!' and return;
                        location = getLocation(conn, ID, 1);
                        int storeID = typeInLocation(conn, location, 3);
                        if (storeID != -1) {

                            send = "Clothes donated successfully!";
                            outClient.println(send);
                        } else {
                            send = "!Sorry, there is no Store in your location";
                            outClient.println(send);
                        }

                        String points = "0";

                    }

                } // for Association
                else if (typeOfClient == 2) {
                    handleAssociationActions(conn, inClient, outClient, ID);

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

    public static int typeInLocation(Connection conn, String location, int typeOfClient) throws SQLException {

        String query = "SELECT id FROM " + getTableName(typeOfClient) + " WHERE location = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, location);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    return -1; // No TypeOfClient found
                }
            }
        }
    }

    public static int getNextClothesID(Connection conn) throws SQLException {
        String query = "SELECT MAX(id) AS max_id FROM Clothes";

        try (PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            if (rs.next() && rs.getObject("max_id") != null) {
                return rs.getInt("max_id") + 1;
            } else {
                return 500000001; // Starting ID for the first item
            }
        }
    }

    public static void insertClothes(Connection conn, int clothesID, int size, String type) throws SQLException {
        String query = "INSERT INTO Clothes (id, size, type) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clothesID);
            stmt.setInt(2, size);
            stmt.setString(3, type);

            stmt.executeUpdate();
        }
    }

    public static void insertDonorAssociationClothes(Connection conn, int donorID, int associationID, int clothesID) throws SQLException {
        String query = "INSERT INTO Donor_Association_Clothes (donor_id, association_id, clothes_id) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, donorID);
            stmt.setInt(2, associationID);
            stmt.setInt(3, clothesID);

            stmt.executeUpdate();
        }
    }

    private void incrementDonorRewards(Connection conn, int donorID) throws SQLException {
        String query = "UPDATE Donor SET rewards = rewards + 20 WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, donorID);

            stmt.executeUpdate();
        }
    }

    private int getDonorRewards(Connection conn, int donorID) throws SQLException {
        String query = "SELECT rewards FROM Donor WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, donorID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("rewards");
                } else {
                    return 0;
                }
            }
        }
    }

    private void updateDonorRewards(Connection conn, int donorID, int newRewards) throws SQLException {
        String query = "UPDATE Donor SET rewards = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newRewards);
            stmt.setInt(2, donorID);

            stmt.executeUpdate();
        }
    }

    private String getStorePromoCode(Connection conn, int storeID) throws SQLException {
        String query = "SELECT codes FROM Store WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, storeID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("codes");
                } else {
                    return "";
                }
            }
        }
    }

    private void updateStorePromotion(Connection conn, int storeID, int promotion) throws SQLException {
        // Assume promotion is a new column to be updated
        String query = "UPDATE Store SET promotion = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, promotion);
            stmt.setInt(2, storeID);

            stmt.executeUpdate();
        }
    }

    private void addStorePromoCode(Connection conn, int storeID, String promoCode) throws SQLException {
        String query = "UPDATE Store SET codes = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, promoCode);
            stmt.setInt(2, storeID);

            stmt.executeUpdate();
        }
    }

    public static void exportDatabaseInfo(Connection conn) {
        try {
            exportTableData(conn, "Donor");
            exportTableData(conn, "Association");
            exportTableData(conn, "Clothes");
            exportTableData(conn, "Store");
            exportCompositeTableData(conn, "Donor_Association_Clothes", "Donor", "Association", "Clothes");
            exportCompositeTableData(conn, "Donor_Store", "Donor", "Store", "");
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void exportTableData(Connection conn, String tableName) throws SQLException, IOException {
        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                PrintWriter writer = new PrintWriter(new FileWriter(tableName + ".txt"))) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    writer.println(rsmd.getColumnName(i) + ": " + rs.getString(i));
                }
                writer.println();
            }
        }
    }

    public static void exportCompositeTableData(Connection conn, String tableName, String firstTable, String secondTable, String thirdTable) throws SQLException, IOException {
        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                PrintWriter writer = new PrintWriter(new FileWriter(tableName + ".txt"))) {

            while (rs.next()) {
                writer.println(firstTable + " ID: " + rs.getInt(1));
                writer.println(secondTable + " ID: " + rs.getInt(2));
                writer.println(thirdTable + " ID: " + rs.getInt(3));
                writer.println();
            }
        }
    }

    public static void handleAssociationActions(Connection conn, Scanner inClient, PrintWriter outClient, int associationID) throws SQLException, IOException {
        String send = "Fetching Donor and Clothes information ...";
        outClient.println(send);

        String query = "SELECT D.id AS donor_id, D.name AS donor_name, D.phone_number AS donor_phone, D.location AS donor_location, "
                + "A.id AS association_id, A.name AS association_name, A.phone_number AS association_phone, A.location AS association_location, "
                + "C.id AS clothes_id, C.size, C.type "
                + "FROM Donor_Association_Clothes DAC "
                + "JOIN Donor D ON DAC.donor_id = D.id "
                + "JOIN Association A ON DAC.association_id = A.id "
                + "JOIN Clothes C ON DAC.clothes_id = C.id "
                + "WHERE A.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, associationID);

            try (ResultSet rs = stmt.executeQuery();
                    PrintWriter writer = new PrintWriter(new FileWriter("Association_" + associationID + "_info.txt"))) {
                while (rs.next()) {
                    int donorId = rs.getInt("donor_id");
                    String donorName = rs.getString("donor_name");
                    String donorPhone = rs.getString("donor_phone");
                    String donorLocation = rs.getString("donor_location");
                    int assocId = rs.getInt("association_id");
                    String assocName = rs.getString("association_name");
                    String assocPhone = rs.getString("association_phone");
                    String assocLocation = rs.getString("association_location");
                    int clothesId = rs.getInt("clothes_id");
                    int size = rs.getInt("size");
                    String type = rs.getString("type");

                    writer.println("Association ID: " + assocId);
                    writer.println("Association Name: " + assocName);
                    writer.println("Association Phone: " + assocPhone);
                    writer.println("Association Location: " + assocLocation);
                    writer.println();
                    writer.println("Donor ID: " + donorId);
                    writer.println("Donor Name: " + donorName);
                    writer.println("Donor Phone: " + donorPhone);
                    writer.println("Donor Location: " + donorLocation);
                    writer.println();
                    writer.println("Clothes ID: " + clothesId);
                    writer.println("Clothes Size: " + size);
                    writer.println("Clothes Type: " + type);
                    writer.println();
                }
                send = "Information printed to file: Association_" + associationID + "_info.txt";
                outClient.println(send);
            }
        }
    }
    
    public static String getLocation(Connection conn, int ID, int typeOfClient) throws SQLException{
        String query = "SELECT location from " + getTableName(typeOfClient) + " WHERE ID = ?";
        
        try(PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, ID);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getString("location");
                }else{
                    return "null";
                }
            }
        }
    }

}
