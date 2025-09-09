// public class ManageCar {
    
// }
// package store;


import java.io.*;
import java.nio.file.Files;
import java.util.*;

// import controllers.ManageUser;

public class ManageCar extends ManageUser {

    // private static final String FILE_PATH = "../store_info/rentals.txt"; // your
    // database file
    // private static final String FILE_PATH =
    // "src\\controllers\\store\\rentals.txt"; // your database file
    private static final String FILE_PATH = "src/controllers/store/cars.txt"; // your database file
    private static int nextID = 1; // auto-increment booking ID

    public static void main(String[] args) {
        // check if user is logged in
        if (!ManageUser.isUserLoggedIn()) {
            System.out.println("Please login first to access Car Rentals.");
            return;
        }

        // System.out.println("Welcome Mr. " + ManageUser.currentUser + " to Car Rentals System!");
        System.out.println("\n=========================================================================== \n\n Welcome Mr. " + getLoggedInUser() + " to Car Management interface! Manage you car from here \n\n=========================================================================== \n");




        // System.out.println("Logged in user: " + ManageUser.currentUser);
        // System.out.println("Role: " + ManageUser.currentRole);
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add Car");
            System.out.println("2. Search Car");
            System.out.println("3. Update Car");
            System.out.println("4. Delete Car");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> addCar(sc);
                case "2" -> searchCar(sc);
                case "3" -> updateCar(sc);
                case "4" -> deleteCar(sc);
                case "5" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // -------------------- Add Booking --------------------
    private static void addCar(Scanner sc) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            System.out.print("Enter car name: ");
            String carName = sc.nextLine();

            System.out.println("Car Type : SUV, Sedan, Hatchback, Convertible, Coupe, Minivan, Pickup Truck or others");
            String carType = sc.nextLine();

            // System.out.println("");

            

            System.out.print("Enter number of passengers capacity : ");
            String passengersCapacity = sc.nextLine();

            System.out.println("Enter car rate per day(BDT) : ");
            String rate = sc.nextLine();

            // System.out.print("Enter car suggested: ");
            // String car = sc.nextLine();

            int id = getNextID();
            bw.write("ID=" + id + "\n");
            bw.write("Car Name=" + carName + "\n");
            // bw.write("Journey=" + journey.toString().trim() + "\n");
            bw.write("Type=" + carType + "\n");
            bw.write("Passengers Capacity=" + passengersCapacity + "\n");
            bw.write("Rate=" + rate + "\n");
            bw.write("---\n");

            System.out.println("Car added with ID " + id);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    
    private static void searchCar(Scanner sc) {
        System.out.print("Enter search term (car name/type/rate): ");
        String term = sc.nextLine().toLowerCase();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            List<String> block = new ArrayList<>();
            boolean found = false;

            while ((line = br.readLine()) != null) {
                if (line.equals("---")) {
                    String blockContent = String.join("\n", block);
                    if (blockContent.toLowerCase().contains(term)) {
                        System.out.println("\nFound Car:\n" + blockContent + "\n---");
                        found = true;
                    }
                    block.clear();
                } else {
                    block.add(line);
                }
            }

            if (!found)
                System.out.println("No car found for: " + term);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    
    private static void updateUser(Scanner sc) {
        System.out.print("Enter user ID to update: ");
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
                        System.out.println("Current user:\n" + String.join("\n", block));
                        System.out.println("Enter new details (leave blank to keep current)");

                        Map<String, String> fields = new LinkedHashMap<>();
                        for (String l : block) {
                            int idx = l.indexOf("=");
                            fields.put(l.substring(0, idx), l.substring(idx + 1));
                        }

                        System.out.print("Car Name (" + fields.get("name") + "): ");
                        String newCarName = sc.nextLine();
                        if (!newCarName.isEmpty())
                            fields.put("name", newCarName);

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
            System.out.println(updated ? "✅ Booking updated!" : "Booking ID not found.");

        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }
    }


    
    private static void updateCar(Scanner sc) {
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

                        System.out.print("Car Name (" + fields.get("Car Name") + "): ");
                        String newCarName = sc.nextLine();
                        if (!newCarName.isEmpty())
                            fields.put("Car Name", newCarName);

                        // System.out.println("Journey (" + fields.get("Journey") + ") (end with 'END'):");
                        // StringBuilder newJourney = new StringBuilder();
                        // while (true) {
                        //     String jl = sc.nextLine();
                        //     if (jl.equalsIgnoreCase("END"))
                        //         break;
                        //     if (!jl.isEmpty())
                        //         newJourney.append(jl).append("\n");
                        // }
                        // if (newJourney.length() > 0)
                        //     fields.put("Journey", newJourney.toString().trim());

                        System.out.print("Passengers Capacity (" + fields.get("Passengers Capacity") + "): ");
                        String newPassengersCapacity = sc.nextLine();
                        if (!newPassengersCapacity.isEmpty())
                            fields.put("Passengers Capacity", newPassengersCapacity);

                        System.out.print("Type (" + fields.get("Type") + "): ");
                        String type = sc.nextLine();
                        if (!type.isEmpty())
                            fields.put("Type", type);

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
            System.out.println(updated ? "Car updated!" : "Car ID not found.");

        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }
    }

    // delete the cars
    private static void deleteCar(Scanner sc) {
        System.out.print("Enter car ID to delete: ");
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
            System.out.println(deleted ? "Car deleted!" : "Car ID not found.");

        } catch (IOException e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }
    }

    // -------------------- Get Next ID --------------------
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
