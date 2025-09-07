package services;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AIServices {

    private static final String API_KEY = System.getenv("OPENROUTER_API_KEY"); 
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    public static void main(String[] args) {
        if (API_KEY == null || API_KEY.isEmpty()) {
            System.out.println("❌ Error: OPENROUTER_API_KEY not set. Use: export OPENROUTER_API_KEY=\"your_key\"");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to RentaGo AI Car Renting System!");

        System.out.print("Enter your username: ");
        String username = sc.nextLine();

        System.out.print("Enter journey details (e.g., From Dhaka to Chittagong with 6 passengers): ");
        String journey = sc.nextLine();

        System.out.println("\nAI is analyzing your journey...");
        String suggestion = getCarSuggestion(journey);

        System.out.println("\nAI Suggestion:\n" + suggestion);
        System.out.println("\n[Booking functionality will go here...]");
    }

    public static String getCarSuggestion(String journeyDetails) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setDoOutput(true);

            String jsonData = """
            {
                "model": "deepseek/deepseek-chat-v3.1:free",
                "messages": [
                    {
                        "role": "user",
                        "content": "You are a car renting assistant. Suggest only cars for this journey: %s. Do NOT suggest trains, buses, flights, or any other alternative routes."
                    }
                ]
            }
            """.formatted(journeyDetails);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonData.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 429) {
                System.out.println("⚠️ Rate limit hit. Waiting 10 seconds...");
                Thread.sleep(10000);
                return getCarSuggestion(journeyDetails); // Retry
            }

            if (responseCode != 200) {
                return "Error: API returned status " + responseCode;
            }

            // Read response
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            // Monitor credits
            String creditsUsed = conn.getHeaderField("x-openrouter-credits-used");
            String creditsRemaining = conn.getHeaderField("x-openrouter-credits-remaining");
            System.out.println("💰 Used: " + creditsUsed + ", Remaining: " + creditsRemaining);

            // Extract AI response with plain string matching
            String result = extractContent(response.toString());

            // Add delay between requests
            Thread.sleep(2000);

            return result != null ? result : "Error: Could not extract AI response.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Could not get AI suggestion.";
        }
    }

    private static String extractContent(String jsonResponse) {
        String marker = "\"content\":";
        int index = jsonResponse.indexOf(marker);
        if (index == -1) return null;

        int start = jsonResponse.indexOf("\"", index + marker.length());
        int end = jsonResponse.indexOf("\"", start + 1);
        if (start == -1 || end == -1) return null;

        return jsonResponse.substring(start + 1, end);
    }
}
