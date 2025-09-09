import java.util.Scanner;
// import services.*;
import services.AIServices;
import services.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // start while loop from here
        while (true) {
            System.out.println("\n===========================================\n Welcome to RentaGo AI Car Renting System! \n===========================================\n");

            // show the menu
            System.out.println("1. Login");
            System.out.println("2. Rent a Car");
            
            if (ManageUser.isUserLoggedIn()) {
                
                System.out.println("3. Logout");
            }
            if(ManageUser.getLoggedInUserRole().equals("Admin")){
                System.out.println("4. Manage Cars");
            }

            System.out.println("5. Get AI Car Suggestion");



            // System.out.println("2. Exit\n");
            System.out.print("Choose an option: ");
            // int choice = sc.nextInt();
            String choice = sc.nextLine();
            // sc.nextLine(); // consume newline
            // if (choice != 1) {
            //     System.out.println("\nThank you for using RentaGo. Goodbye!");
            //     return;
            // }

            // add the switch case from here
            switch (choice) {
                case "1":
                    ManageUser.main(null);
                    
                    break;
                
                case "2":
                    CarRentals.main(null);
                    break;
                
                case "3":
                    ManageUser.logoutUser();
                    return;
                    // break;
                
                case "4":
                    if(ManageUser.getLoggedInUserRole().equals("Admin")){
                        ManageCar.main(null);
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                    break;

                case "5":
                    AIServices.main(null);
                    break;
            
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

            // String choice = sc.nextLine();


            // // Login / Register (simplified)
            // System.out.print("Enter your username: hgell :  ");
            // String username = sc.nextLine();

            // //

            // System.out.print("Enter journey details (e.g., From Dhaka to Chittagong with 6 passengers): ");
            // String journey = sc.nextLine();

            // // Call AI Service
            // System.out.println("\nAI is analyzing your journey...");
            // String suggestion = AIServices.getCarSuggestion(journey);

            // System.out.println("\nAI Suggestion:");
            // System.out.println(suggestion);

        }

        // Here you could parse the suggestion and show matching cars from cars.txt
        // Then allow booking
    }
}
