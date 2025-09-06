import java.util.Scanner;
// import services.*;
import services.AIServices;
import services.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to RentaGo AI Car Renting System!");

        // Login / Register (simplified)
        System.out.print("Enter your username: ");
        String username = sc.nextLine();
        System.out.print("Enter journey details (e.g., From Dhaka to Chittagong with 6 passengers): ");
        String journey = sc.nextLine();

        // Call AI Service
        System.out.println("\nAI is analyzing your journey...");
        String suggestion = AIServices.getCarSuggestion(journey);

        System.out.println("\nAI Suggestion:");
        System.out.println(suggestion);


        // Here you could parse the suggestion and show matching cars from cars.txt
        // Then allow booking
    }
}
