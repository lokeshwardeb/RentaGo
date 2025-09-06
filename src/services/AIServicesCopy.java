package services;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class AIServicesCopy {

    // private static final String API_KEY = "YOUR_API_KEY"; // Replace with your OpenRouter API key
    private static final String API_KEY = "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"; // Replace with your OpenRouter API key
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to RentaGo AI Car Renting System!");

        // Simple username input
        System.out.print("Enter your username: ");
        String username = sc.nextLine();

        // User enters journey details
        System.out.print("Enter journey details (e.g., From Dhaka to Chittagong with 6 passengers): ");
        String journey = sc.nextLine();

        // Call AI Service
        System.out.println("\nAI is analyzing your journey...");
        String suggestion = getCarSuggestion(journey);

        System.out.println("\nAI Suggestion:\n");
        System.out.println(suggestion);

        // TODO: Here you can match the AI suggestion to cars.txt for booking
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

            // Build JSON request (single line string)
            String jsonData = "{\"model\":\"deepseek/deepseek-chat-v3.1:free\",\"messages\":[{\"role\":\"user\",\"content\":\""
                    + journeyDetails + "\"}]}";

            // Send JSON
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonData.getBytes(StandardCharsets.UTF_8));
            }

            // Read response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            // Quick string parsing to extract AI text (without external JSON library)
            String raw = response.toString();
            int start = raw.indexOf("\"content\":\"") + 11;
            int end = raw.indexOf("\"}", start);
            if (start == -1 || end == -1) {
                return "Error: Could not extract AI response.";
            }
            String content = raw.substring(start, end)
                                .replace("\\n", "\n")      // convert newline
                                .replace("\\\"", "\"");     // convert escaped quotes
            return content;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return "Error: Could not get AI suggestion.";
        }
    }
}
