package wearshareserver;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import java.sql.*;
import java.math.BigInteger;

public class WearShareServer {

    public static void main(String[] args) {
        System.out.println("Hi from Server side");

        try (ServerSocket serverSocket = new ServerSocket(1818)) {
            System.out.println("Server start listening on Port: " + 1818);

            // Handle multiple clients
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new MultiClientHandler(clientSocket)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Class to handle multiple clients concurrently
    public static class MultiClientHandler implements Runnable {

        private final Socket clientSocket;

        public MultiClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (Scanner inClient = new Scanner(clientSocket.getInputStream());
                    PrintWriter outClient = new PrintWriter(clientSocket.getOutputStream(), true);
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/wearshareserverdb", "root", Clothes.w)) {

                // *1
                String welcomeMessage = "Welcome to our WearShare system, the latest version of donation clothes!! I'm receiving your type of user, please wait ...";
                outClient.println(welcomeMessage);

                // *2
                // Receive user type
                String recv = inClient.nextLine();
                int typeOfClient = Integer.parseInt(recv);
                System.out.println("Client: " + recv);

                // *3
                // Prompt for login or create account
                String send = "Wait, sending log in or create an account ...";
                outClient.println(send);

                // *4
                // Receive login or create account choice
                recv = inClient.nextLine();
                int logOrCreate = Integer.parseInt(recv);
                System.out.println("Client: " + recv);

                int ID;
                String name;
                String location = "";
                String phone;
                String password;

                if (logOrCreate == 4) {
                    // *5
                    // Prompt for ID
                    send = "Wait, Entering ID ...";
                    outClient.println(send);

                    // *6
                    // Receive ID
                    recv = inClient.nextLine();
                    ID = Integer.parseInt(recv);

                    // *7
                    // Prompt for password
                    send = "Wait, Entering Password ...";
                    outClient.println(send);

                    // *8
                    // Receive password
                    recv = inClient.nextLine();
                    password = recv;

                    System.out.println("ID: " + ID);
                    System.out.println("Pass: " + password);

                    // *9
                    // Validate user credentials
                    if (!isValidUser(conn, ID, password, typeOfClient)) {
                        send = "!Wrong ID OR Password :( Please try again :)";
                        outClient.println(send);
                        return;
                    }

                    // *10
                    // Special logging for Donor with ID = 51818185
                    if (ID == 518181815) {
                        send = "Printing all information of the database in separate files :) DONE";
                        outClient.println(send);

                        if (inClient.nextLine().equals("1")) {
                            exportDatabaseInfo(conn);
                            exportDonorStoreInfo(conn);
                        }
                        return;
                    }

                } else {
                    // *11
                    // Create new account
                    send = "Wait, Entering ID ...";
                    outClient.println(send);

                    // *12
                    // Receive ID
                    ID = Integer.parseInt(inClient.nextLine());
                    System.out.println("ID entered " + ID);

                    // *13
                    // Prompt for name
                    send = "Wait, Entering Name ...";
                    outClient.println(send);

                    // *14
                    // Receive name
                    name = inClient.nextLine();
                    System.out.println("Name entered " + name);

                    // *15
                    // Prompt for password
                    send = "Wait, Entering Password ...";
                    outClient.println(send);

                    // *16
                    // Receive password
                    password = inClient.nextLine();
                    System.out.println("Password entered");

                    // *17
                    // Prompt for location
                    send = "Wait, Entering Location ...";
                    outClient.println(send);

                    // *18
                    // Receive location
                    location = inClient.nextLine();
                    System.out.println("Location entered " + location);

                    // *19
                    // Prompt for phone number
                    send = "Wait, Entering Phone Number ...";
                    outClient.println(send);

                    // *20
                    // Receive phone number
                    phone = inClient.nextLine();

                    // *21
                    // Create account in database
                    createAccount(conn, ID, name, password, location, phone, typeOfClient);

                    System.out.println("Account created: ID = " + ID + ", Name = " + name + ", Location = " + location + ", Phone = " + phone);
                }

                // *22
                // Determine user actions based on type
                if (typeOfClient == 1) {
                    handleDonorActions(conn, inClient, outClient, ID, location);
                } else if (typeOfClient == 2) {
                    handleAssociationActions(conn, inClient, outClient, ID);
                } else if (typeOfClient == 3) {
                    handleStoreActions(conn, inClient, outClient, ID);
                }

            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        }

        // *23
        // Handle actions for Donor users
        private void handleDonorActions(Connection conn, Scanner inClient, PrintWriter outClient, int donorID, String donorLocation) throws SQLException {
            // *24
            // Prompt for Donor actions
            String send = "Hi Donor, Enter 1 for clothes donation OR 2 for exchange your points";
            outClient.println(send);

            // *25
            // Receive action number
            String donorAction = inClient.nextLine();
            String location = donorLocation;
            if (donorAction.equals("1")) {
                // *26
                // Handle clothes donation
                send = "Wait, Entering clothes type ...";
                outClient.println(send);

                // *27
                // Receive clothes type
                String recv = inClient.nextLine();
                String clothingType = recv;

                send = "Wait, Entering clothes size ...";
                outClient.println(send);

                // *28
                // Receive clothes size
                recv = inClient.nextLine();
                int clothingSize = Integer.parseInt(recv);

                int clothesID = getNextClothesID(conn);
                location = getLocation(conn, donorID, 1);
                int associationID = typeInLocation(conn, location, 2);

                if (associationID != -1) {
                    insertClothes(conn, clothesID, clothingSize, clothingType);
                    insertDonorAssociationClothes(conn, donorID, associationID, clothesID);
                    updateDonorRewards(conn, donorID, 20);
                    send = "Clothes donated successfully!";
                    outClient.println(send);
                } else {
                    send = "!Sorry, there is no Association in your location";
                    outClient.println(send);
                }
            } else {
                // *29
                // Handle exchange points
                location = getLocation(conn, donorID, 1);
                int storeID = typeInLocation(conn, location, 3);
                if (storeID != -1) {
                    int promotionRequired = getStorePromo(conn, storeID);
                    int donorRewards = getDonorRewards(conn, donorID);
                    if (promotionRequired > donorRewards) {
                        send = "Your Rewards: " + donorRewards + " is less than Promotion: " + promotionRequired;
                        outClient.println(send);
                    } else {
                        String code = getStorePromoCode(conn, storeID);
                        if (code.charAt(0) != '!') {
                            inserDonorStore(conn, donorID, storeID);
                            updateDonorRewards(conn, donorID, -50);
                        }
                        send = "Points exchanged successfully! Your code: " + code;
                        outClient.println(send);
                    }
                } else {
                    send = "!Sorry, there is no Store in your location";
                    outClient.println(send);
                }
            }
        }

        // *30
        // Handle actions for Association users
        private void handleAssociationActions(Connection conn, Scanner inClient, PrintWriter outClient, int associationID) throws SQLException, IOException {
            // *31
            // Fetch and print Donor and Clothes information
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

        // *32
        // Handle actions for Store users
        private void handleStoreActions(Connection conn, Scanner inClient, PrintWriter outClient, int storeID) throws SQLException {
            // *33
            // Prompt for Store actions
            String send = "Hi Store, enter 1 to edit promotion OR 2 to add code";
            outClient.println(send);

            // *34
            // Receive action number
            String storeAction = inClient.nextLine();

            if (storeAction.equals("1")) {
                send = "Wait, Entering promotion ...";
                outClient.println(send);

                // *35
                // Receive promotion
                String recv = inClient.nextLine();
                int promotion = Integer.parseInt(recv);
                updateStorePromotion(conn, storeID, promotion);
                System.out.println("Promotion: " + promotion);
            } else {
                send = "Wait, Entering code ...";
                outClient.println(send);

                // *36
                // Receive promotion code
                String recv = inClient.nextLine();
                String promotionCode = recv;
                System.out.println("Code: " + promotionCode);
                addStorePromoCode(conn, storeID, promotionCode);
            }
        }

        // *9
        // Validate user credentials
        private boolean isValidUser(Connection conn, int ID, String password, int typeOfClient) throws SQLException {
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

        // *21
        // Create new account in the database
        private void createAccount(Connection conn, int ID, String name, String password, String location, String phone, int typeOfClient) throws SQLException {
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
                    System.out.println("Account created successfully.");
                } else {
                    System.out.println("Failed to create account.");
                }
            } catch (SQLException ex) {
                System.err.println("Error creating account: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        // *18
        // Get table name based on the type of client
        private String getTableName(int typeOfClient) {
            if (typeOfClient == 1) {
                return "Donor";
            }
            if (typeOfClient == 2) {
                return "Association";
            }
            return "Store";
        }

        // *31
        // Get ID of a client type based on location
        private int typeInLocation(Connection conn, String location, int typeOfClient) throws SQLException {
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

        // *32
        // Get location of a client based on ID
        private String getLocation(Connection conn, int ID, int typeOfClient) throws SQLException {
            String query = "SELECT location from " + getTableName(typeOfClient) + " WHERE ID = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, ID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("location");
                    } else {
                        return "null";
                    }
                }
            }
        }

        // *27
        // Get the next available clothes ID
        private int getNextClothesID(Connection conn) throws SQLException {
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

        // *28
        // Insert new clothes into the database
        private void insertClothes(Connection conn, int clothesID, int size, String type) throws SQLException {
            String query = "INSERT INTO Clothes (id, size, type) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, clothesID);
                stmt.setInt(2, size);
                stmt.setString(3, type);

                stmt.executeUpdate();
            }
        }

        // *28
        // Insert new donor-association clothes relationship into the database
        private void insertDonorAssociationClothes(Connection conn, int donorID, int associationID, int clothesID) throws SQLException {
            String query = "INSERT INTO Donor_Association_Clothes (donor_id, association_id, clothes_id) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, donorID);
                stmt.setInt(2, associationID);
                stmt.setInt(3, clothesID);

                stmt.executeUpdate();
            }
        }

        // *29
        // Insert new donor-store relationship into the database
        private void inserDonorStore(Connection conn, int donorID, int storeID) throws SQLException {
            String query = "INSERT INTO Donor_Store (donor_id, store_id) VALUES(?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, donorID);
                stmt.setInt(2, storeID);

                stmt.executeUpdate();
            }
        }

        // *29
        // Get rewards of a donor based on their ID
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

        // *29
        // Update rewards of a donor based on their ID
        private void updateDonorRewards(Connection conn, int donorID, int amountToAdd) throws SQLException {
            String operator = (amountToAdd >= 0) ? "+" : "-";
            String query = "UPDATE Donor SET rewards = rewards " + operator + " ? WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, Math.abs(amountToAdd)); // Ensure positive value for amount to add
                stmt.setInt(2, donorID);

                stmt.executeUpdate();
            }
        }

        // *29
        // Get promotion value of a store based on its ID
        private int getStorePromo(Connection conn, int storeID) throws SQLException {
            String query = "SELECT promotion FROM Store WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, storeID);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int promotion = rs.getInt("promotion");
                        return promotion;
                    }
                }
            }
            return 50;
        }

        // *29
        // Get promotion code of a store based on its ID
        private String getStorePromoCode(Connection conn, int storeID) throws SQLException {
            String query = "SELECT codes FROM Store WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, storeID);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String code = rs.getString("codes");
                        if (code == null) {
                            return "!SorryNoCodeAvailable";
                        } else {
                            return code;
                        }
                    }
                }
            }
            return "!SorryNoCodeAvailable";
        }

        // *35
        // Update promotion value of a store based on its ID
        private void updateStorePromotion(Connection conn, int storeID, int promotion) throws SQLException {
            String query = "UPDATE Store SET promotion = ? WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, promotion);
                stmt.setInt(2, storeID);

                stmt.executeUpdate();
            }
        }

        // *36
        // Add promotion code to a store based on its ID
        private void addStorePromoCode(Connection conn, int storeID, String promoCode) throws SQLException {
            String query = "UPDATE Store SET codes = ? WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, promoCode);
                stmt.setInt(2, storeID);

                stmt.executeUpdate();
            }
        }

        // *10
        // Export information from database tables to text files
        private void exportDatabaseInfo(Connection conn) {
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

        // *10
        // Export information from a single database table to a text file
        private void exportTableData(Connection conn, String tableName) throws SQLException, IOException {
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

        // *10
        // Export composite table information to a text file
        private void exportCompositeTableData(Connection conn, String tableName, String firstTable, String secondTable, String thirdTable) throws SQLException, IOException {
            String query = "SELECT * FROM " + tableName;
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    PrintWriter writer = new PrintWriter(new FileWriter(tableName + ".txt"))) {

                while (rs.next()) {
                    int firstId = rs.getInt(1);
                    int secondId = rs.getInt(2);
                    int thirdId = rs.getInt(3);

                    writer.println(firstTable + " Details:");
                    writeTableData(conn, writer, firstTable, firstId);

                    writer.println(secondTable + " Details:");
                    writeTableData(conn, writer, secondTable, secondId);

                    if (!thirdTable.isEmpty()) {
                        writer.println(thirdTable + " Details:");
                        writeTableData(conn, writer, thirdTable, thirdId);
                    }

                    writer.println();
                }
            }
        }

        // *10
        // Write table data to the specified writer
        private void writeTableData(Connection conn, PrintWriter writer, String tableName, int id) throws SQLException {
            String query = "SELECT * FROM " + tableName + " WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();

                    if (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            writer.println(rsmd.getColumnName(i) + ": " + rs.getString(i));
                        }
                        writer.println();
                    }
                }
            }
        }

        // *10
        // Export donor-store relationship information to a text file
        private void exportDonorStoreInfo(Connection conn) throws SQLException, IOException {
            String query = "SELECT donor_id, store_id FROM Donor_Store";
            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    PrintWriter writer = new PrintWriter(new FileWriter("Donor_Store_info.txt"))) {

                while (rs.next()) {
                    int donorId = rs.getInt("donor_id");
                    int storeId = rs.getInt("store_id");

                    writer.println("Donor Details:");
                    writeTableData(conn, writer, "Donor", donorId);

                    writer.println("Store Details:");
                    writeTableData(conn, writer, "Store", storeId);

                    writer.println();
                }
            }
        }
    }

    // we will implement this method in future but because of testing stage and we stop using it
    // Define a static method named 'encryptString' which takes a String 'text' as input and returns a String.
// This method declares that it can throw a NoSuchAlgorithmException, which must be handled by the caller.
    public static String encryptString(String text) throws NoSuchAlgorithmException {
        // Create a MessageDigest instance for MD5 hashing.
        MessageDigest MD = MessageDigest.getInstance("MD5");

        // Convert the input text to a byte array and compute its digest (hash).
        byte[] massageDigest = MD.digest(text.getBytes());

        // Convert the byte array of the hash into a positive BigInteger.
        BigInteger bigInteger = new BigInteger(1, massageDigest);

        // Convert the BigInteger to a hexadecimal string and return it.
        return bigInteger.toString(16);
    }
}
