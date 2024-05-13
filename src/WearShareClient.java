
import wearshareserver.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class WearShareClient {

    public static void main(String[] args) {

        System.out.println("I am client side");

        try (Socket server = new Socket("127.0.0.1", 1818);
                Scanner inServer = new Scanner(server.getInputStream());
                PrintWriter outServer = new PrintWriter(server.getOutputStream(), true);
                Scanner inUser = new Scanner(System.in);) {

            
            //*2
            // Read welcome message from server
            String welcomeMessage = inServer.nextLine();
            System.out.println(welcomeMessage);

            //*3
            // to send 1 2 3
            String send = inUser.nextLine();
            int typeOfUser = Integer.parseInt(send);
            outServer.println(send);

            //*6
            // recv request of send 4 5 for log in or create account
            String recv = inServer.nextLine();
            System.out.println("server: " + recv);

            //*7
            // send 4 or 5 for log in or create account
            send = inUser.nextLine();
            int logOrCreate = Integer.parseInt(send);
            outServer.println(send);

            //
            if (logOrCreate == 4) {

                //*10
                // recv msg request of ID
                recv = inServer.nextLine();
                System.out.println("server: " + recv);

                //*11
                //send ID to server
                send = inUser.nextLine();
                outServer.println(send);

                //*14
                // recv msg of request password
                recv = inServer.nextLine();
                System.out.println("server: " + recv);

                //*15
                // send password to server
                send = inUser.nextLine();
                outServer.println(send);
            } 
            // else mean create new account
            else {
                    System.out.println("Enter ID");
                    int ID = Integer.parseInt(inUser.nextLine());
                    System.out.println("Enter name");
                    String name = inUser.nextLine();
                    System.out.println("Enter password");
                    String password = inUser.nextLine();
                    System.out.println("Enter location");
                    String location = inUser.nextLine();
                    System.out.println("Enter phoneNumber");
                    String phoneNumber = inUser.nextLine();
                    
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
}
