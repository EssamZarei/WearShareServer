package wearshareserver;

import java.math.BigInteger;
import java.security.*;
import java.util.*;
import java.io.*;
import java.net.*;


public class WearShareServer {

    public static void main(String[] args) {

        System.out.println("Im Essam");

        Donor d = new Donor(1, "essam", "essam", "essam", "essam");
        System.out.println(d.getName());
    }
    
    
    //      ---         ---         ---         ---         ---         ---         ---         ---         ---
    
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
