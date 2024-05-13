package wearshareserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class WearShareServer {

    public static void main(String[] args) {

        System.out.println("I am Server side");

        try (ServerSocket sk = new ServerSocket(8189);
             Socket incoming = sk.accept();
             Scanner inClient = new Scanner(incoming.getInputStream());
             PrintWriter outClient = new PrintWriter(incoming.getOutputStream(), true);
             Scanner inUser = new Scanner(System.in);) {

            //*1
            String welcomeMSG = "Welcome to our WearShare system, the latest version of donation clothes !! Enter 1 for Donor Enter 2 for Association Enter 3 for Store";
            outClient.println(welcomeMSG);
            
            //*4
            // take 1 or 2 or 3 from client and show it
            String recv = inClient.nextLine();
            int typeOfClient = Integer.parseInt(recv);
            System.out.println("Clint: " + recv);
            
            //*5
            // send msg of 4 5
            String send = "Enter 4 for log in or 5 for create account";
            outClient.println(send);
            
            //*8 recv from clint if 4 or 5
            recv = inClient.nextLine();
            int logOrCreate = Integer.parseInt(recv);
            System.out.println("clint: " + recv);
            
            // turn to send
            
            int ID;
            String password;
            // for Donor
            if(typeOfClient == 1){
                // log in
                if(logOrCreate == 4){
                    //*9
                    //here has to send for ID
                    send = "Enter your ID";
                    outClient.println(send);
                    
                    //*12
                    // recv ID
                    recv = inClient.nextLine();
                    ID = Integer.parseInt(recv);
                    
                    //*13
                    // send msg for request password
                    send = "Enter your password";
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

                    
                    
                }
                //create account
                else{
                    
                }
            }
            // for Association
            else if(typeOfClient == 2){
                // log in
                if(logOrCreate == 4){
                    
                }
                //create account
                else{
                    
                }
            }
            // for Store
            else if(typeOfClient == 3){
                // log in
                if(logOrCreate == 4){
                    
                }
                //create account
                else{
                    
                }
            }
            
            
            
            
            
            

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
