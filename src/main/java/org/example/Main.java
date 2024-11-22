package org.example;
import java.util.Random;
import java.security.MessageDigest;
import java.io.FileReader;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main{
    public static void main(String[] args) {
        if(args.length<2){
            System.out.println("Invalid arguments.");
            return;
        }

        String rollNumber = args[0];
        String fileAddress = args[1];

        try{
            String jsonContent = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(fileAddress)));
            JsonObject jsonObject = JsonParser.parseString(jsonContent).getAsJsonObject();

            String destination=null;
            for (String key : jsonObject.keySet()) {
                if (key.equals("destination")) {
                    destination = jsonObject.get(key).getAsString();
                    break;
                }
            }

            if (destination == null) {
                throw new IllegalArgumentException("Key 'destination' not found in the JSON.");
            }

            String randString = generateRandomString(8);
            String combinedString = rollNumber + destination + randString;
            String hashValue = generateHash(combinedString);

            String result = hashValue + ";" + randString;
            System.out.println("Output: " + result);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public static String generateHash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}