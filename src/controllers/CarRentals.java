// package store;


import java.io.*;
import java.nio.file.Files;
import java.util.*;

// import controllers.ManageUser;

public class CarRentals extends ManageUser {

    // private static final String FILE_PATH = "../store_info/rentals.txt"; // your
    // database file
    // private static final String FILE_PATH =
    // "src\\controllers\\store\\rentals.txt"; // your database file
    private static final String FILE_PATH = "src/controllers/store/rentals.txt"; // your database file
    private static int nextID = 1; // auto-increment booking ID

    public static void main(String[] args) {
        // check if user is logged in
        if (!ManageUser.isUserLoggedIn()) {
            System.out.println("Please login first to access Car Rentals.");
            return;
        }

        // System.out.println("Welcome Mr. " + ManageUser.currentUser + " to Car Rentals System!");
        System.out.println("\n=========================================================================== \n\n Welcome Mr. " + getLoggedInUser() + " to Car Rentals interface! Book a car for your journey. \n\n=========================================================================== \n");




        // System.out.println("Logged in user: " + ManageUser.currentUser);
        // System.out.println("Role: " + ManageUser.currentRole);
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add Booking");
            System.out.println("2. Search Booking");
            System.out.println("3. Update Booking");
            System.out.println("4. Delete Booking");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> addBooking(sc);
                case "2" -> searchBooking(sc);
                case "3" -> updateBooking(sc);
                case "4" -> deleteBooking(sc);
                case "5" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    
    private static void addBooking(Scanner sc) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            System.out.print("Enter ClientName: ");
            String ClientName = sc.nextLine();

            System.out.println("Enter journey details (end with a single line containing only 'END'):");
            StringBuilder journey = new StringBuilder();
            while (true) {
                String line = sc.nextLine();
                if (line.equalsIgnoreCase("END"))
                    break;
                journey.append(line).append("\n");
            }

            System.out.print("Enter number of passengers: ");
            String passengers = sc.nextLine();

            // System.out.print("Enter car suggested: ");
            // String car = sc.nextLine();
            System.out.print("Enter car ID : ");
            String car = sc.nextLine();

            int id = getNextID();
            bw.write("ID=" + id + "\n");
            bw.write("ClientName=" + ClientName + "\n");
            bw.write("Journey=" + journey.toString().trim() + "\n");
            bw.write("Passengers=" + passengers + "\n");
            bw.write("Car ID=" + car + "\n");
            bw.write("---\n");

            System.out.println("Booking added with ID " + id);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    
    private static void searchBooking(Scanner sc) {
        System.out.print("Enter search term (ClientName/journey/car): ");
        String term = sc.nextLine().toLowerCase();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            List<String> block = new ArrayList<>();
            boolean found = false;

            while ((line = br.readLine()) != null) {
                if (line.equals("---")) {
                    String blockContent = String.join("\n", block);
                    if (blockContent.toLowerCase().contains(term)) {
                        System.out.println("\nFound Booking:\n" + blockContent + "\n---");
                        found = true;
                    }
                    block.clear();
                } else {
                    block.add(line);
                }
            }

            if (!found)
                System.out.println("No booking found for: " + term);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    
    private static void updateBooking(Scanner sc) {
        System.out.print("Enter booking ID to update: ");
        String idToUpdate = sc.nextLine();

        try {
            List<String> lines = new ArrayList<>(Files.readAllLines(new File(FILE_PATH).toPath()));
            List<String> newLines = new ArrayList<>();
            List<String> block = new ArrayList<>();
            boolean updated = false;

            for (String line : lines) {
                if (line.equals("---")) {
                    String blockID = block.stream()
                            .filter(l -> l.startsWith("ID="))
                            .findFirst()
                            .map(l -> l.substring(3))
                            .orElse("");

                    if (blockID.equals(idToUpdate)) {
                        System.out.println("Current booking:\n" + String.join("\n", block));
                        System.out.println("Enter new details (leave blank to keep current)");

                        Map<String, String> fields = new LinkedHashMap<>();
                        for (String l : block) {
                            int idx = l.indexOf("=");
                            fields.put(l.substring(0, idx), l.substring(idx + 1));
                        }

                        System.out.print("ClientName (" + fields.get("ClientName") + "): ");
                        String newClientName = sc.nextLine();
                        if (!newClientName.isEmpty())
                            fields.put("ClientName", newClientName);

                        System.out.println("Journey (" + fields.get("Journey") + ") (end with 'END'):");
                        StringBuilder newJourney = new StringBuilder();
                        while (true) {
                            String jl = sc.nextLine();
                            if (jl.equalsIgnoreCase("END"))
                                break;
                            if (!jl.isEmpty())
                                newJourney.append(jl).append("\n");
                        }
                        if (newJourney.length() > 0)
                            fields.put("Journey", newJourney.toString().trim());

                        System.out.print("Passengers (" + fields.get("Passengers") + "): ");
                        String newPassengers = sc.nextLine();
                        if (!newPassengers.isEmpty())
                            fields.put("Passengers", newPassengers);

                        System.out.print("CarSuggested (" + fields.get("CarSuggested") + "): ");
                        String newCar = sc.nextLine();
                        if (!newCar.isEmpty())
                            fields.put("CarSuggested", newCar);

                        // write updated block
                        for (Map.Entry<String, String> entry : fields.entrySet()) {
                            newLines.add(entry.getKey() + "=" + entry.getValue());
                        }
                        newLines.add("---");
                        updated = true;
                    } else {
                        newLines.addAll(block);
                        newLines.add("---");
                    }
                    block.clear();
                } else {
                    block.add(line);
                }
            }

            Files.write(new File(FILE_PATH).toPath(), newLines);
            System.out.println(updated ? "Booking updated!" : "Booking ID not found.");

        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }
    }

    
    private static void deleteBooking(Scanner sc) {
        System.out.print("Enter booking ID to delete: ");
        String idToDelete = sc.nextLine();

        try {
            List<String> lines = new ArrayList<>(Files.readAllLines(new File(FILE_PATH).toPath()));
            List<String> newLines = new ArrayList<>();
            List<String> block = new ArrayList<>();
            boolean deleted = false;

            for (String line : lines) {
                if (line.equals("---")) {
                    String blockID = block.stream()
                            .filter(l -> l.startsWith("ID="))
                            .findFirst()
                            .map(l -> l.substring(3))
                            .orElse("");

                    if (blockID.equals(idToDelete)) {
                        deleted = true; // skip this block
                    } else {
                        newLines.addAll(block);
                        newLines.add("---");
                    }
                    block.clear();
                } else {
                    block.add(line);
                }
            }

            Files.write(new File(FILE_PATH).toPath(), newLines);
            System.out.println(deleted ? "Booking deleted!" : "Booking ID not found.");

        } catch (IOException e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }
    }

    
    private static int getNextID() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int maxID = 0;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ID=")) {
                    int id = Integer.parseInt(line.substring(3));
                    if (id > maxID)
                        maxID = id;
                }
            }
            return maxID + 1;
        } catch (IOException e) {
            return nextID++;
        }
    }
}
