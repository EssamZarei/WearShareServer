
import wearshareserver.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class WearShareClient {

    public static void main(String[] args) {

        System.out.println("I am client side");

        try (Socket server = new Socket("127.0.0.1", 8189);
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
                    int ID = inUser.nextInt();
                    String name = inUser.nextLine();
                    String password = inUser.nextLine();
                    String location = inUser.nextLine();
                    String phoneNumber = inUser.nextLine();
                    
                if (typeOfUser == 1 || typeOfUser == 2) {
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
