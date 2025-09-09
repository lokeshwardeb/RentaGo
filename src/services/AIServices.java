package services;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AIServices {

    // private static final String API_KEY =
    // "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";
    // private static final String API_KEY =
    // "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";
    // private static final String API_KEY =
    // "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";

    // private static final String API_KEY =
    // "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";

    private static final String API_KEY = "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";
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    private static final String FILE_PATH = "src/controllers/store/session.txt"; // your database file

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to RentaGo AI Car Renting System!");

        // System.out.print("Enter your username: ");
        // String username = sc.nextLine();

        System.out.print("Enter journey details (e.g., From Dhaka to Chittagong with 6 passengers): ");
        String journey = sc.nextLine();

        System.out.println("\nAI is analyzing your journey...");
        String suggestion = getCarSuggestion(journey);

        System.out.println("\nAI Suggestion:\n" + suggestion);
        System.out.println("\n[Booking functionality will go here...]");
    }

    private static String readAllCars() {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading car data: " + e.getMessage());
        }
        return data.toString();
    }

    // private static String searchCar(Scanner sc) {
    //     // System.out.print("Enter search term (car name/type/rate): ");
    //     String term = sc.nextLine().toLowerCase();

    //     try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
    //         String line;
    //         List<String> block = new ArrayList<>();
    //         boolean found = false;

    //         while ((line = br.readLine()) != null) {
    //             if (line.equals("---")) {
    //                 String blockContent = String.join("\n", block);
    //                 // if (blockContent.toLowerCase().contains(term)) {
    //                 // System.out.println("\nFound Car:\n" + blockContent + "\n---");
    //                 // found = true;
    //                 // }

    //                 return block;

    //                 // block.clear();
    //             } else {
    //                 block.add(line);
    //             }
    //         }

    //         if (!found)
    //             System.out.println("No car found for: " + term);
    //     } catch (IOException e) {
    //         System.out.println("Error reading file: " + e.getMessage());
    //     }

    //     return null;
    // }

    public static String getCarSuggestion(String journeyDetails) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setDoOutput(true);

            // String jsonData =
            // "{\"model\":\"deepseek/deepseek-chat-v3.1:free\",\"messages\":[{\"role\":\"user\",\"content\":\""
            // + journeyDetails + "\"}]}";

            String jsonData = "{\"model\":\"deepseek/deepseek-chat-v3.1:free\",\"messages\":[{\"role\":\"user\",\"content\":\""
                    + "You are a car renting assistant. Suggest only cars for this journey: "
                    + journeyDetails
                    + ". You Have These car(s) : " 
                    + readAllCars() 
                    +"Do NOT suggest trains, buses, flights, or any other alternative routes.\"}]}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonData.getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            String raw = response.toString();
            int start = raw.indexOf("\"content\":\"");
            if (start == -1)
                return "Error: No AI response found.";

            start += 11;
            int end = raw.indexOf("\",\"", start);
            if (end == -1)
                end = raw.length();

            return raw.substring(start, end).replace("\\n", "\n").replace("\\\"", "\"");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Could not get AI suggestion.";
        }
    }
}
