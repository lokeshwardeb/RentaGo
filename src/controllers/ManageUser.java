import java.io.*;
import java.nio.file.Files;
import java.util.*;

class ManageUser {

    boolean loggedIn = false;
    public static String currentUser = "";
    public static String currentRole = "Admin";
    // public static String currentRole = "Admin";

    // private static final String FILE_PATH = "../store_info/rentals.txt"; // your
    // database file
    // private static final String FILE_PATH =
    // "src\\controllers\\store\\rentals.txt"; // your database file

    // add the users.txt file to store user info
    private static final String FILE_PATH = "src/controllers/store/users.txt"; // your database file
    private static final String SESSION_FILE_PATH = "src/controllers/store/session.txt"; // your database file
    private static int nextID = 1; // auto-increment booking ID

    /**
     * The main method to run the user management interface (or the main entry point
     * of the user management interface)
     * 
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {

            String role = getLoggedInUserRole();

            System.out.println(
                    "\n===================================================\n Welcome to the login / registration interface! \n===================================================\n");
            System.out.println("1. Register a new User");
            System.out.println("2. Login User");

            if ("Admin".equals(role)) {
                System.out.println("3. Search User");
                System.out.println("4. Update User");
                System.out.println("5. Delete User");
                System.out.println("6. Approve User");
            }

            System.out.println("7. Exit from the login / registration interface\n");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            if ("User".equals(role) && (choice.equals("3") || choice.equals("4") || choice.equals("5"))) {
                // System.out.println("Access Denied! You are not an Admin.");
                System.out.println("Invalid choice!");
                continue;
            } 
            


            // if ("Admin".equals(role)) {
            // System.out.println("3. Search User");
            // System.out.println("4. Update User");
            // System.out.println("5. Delete User");
            // }

            // System.out.println("3. Search User");
            // System.out.println("4. Update User");
            // System.out.println("5. Delete User");

            // System.out.println("the role is : " + role + " and choice is : " + choice);

            switch (choice) {
                case "1" -> registerUser(sc);
                case "2" -> loginUser(sc);
                case "3" -> searchUser(sc);
                case "4" -> updateUser(sc);
                case "5" -> deleteUser(sc);
                case "6" -> approveUser(sc);
                case "7" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    public static boolean isUserLoggedIn() {
        // return true if the user is logged in else false
        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE_PATH))) {
            if (br.readLine() != null) {
                // that means the file has data and the user is logged in
                // System.out.println("Session file is empty.");
                return true;
            } else {
                // System.out.println("Session file has data.");
                return false;
            }
            // return false;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return false;

    }

    public static void logoutUser() {
        // first check if the user is logged in or not
        if (!isUserLoggedIn()) {
            System.out.println("No user is currently logged in.");
            return;
        }
        // clear the session file to logout the user
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SESSION_FILE_PATH))) {
            bw.write("");
            System.out.println("User logged out successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // show logged in user
    public static String getLoggedInUserRole() {
        String role = "";
        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Role=")) {
                    role = line.substring(5);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return role;
    }

    // show logged in user
    public static String getLoggedInUser() {
        String username = "";
        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Username=")) {
                    username = line.substring(9);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return username;
    }

    /**
     * Login User features and functionalities
     * 
     */

    private static void loginUser(Scanner sc) {

        // check if a user is already logged in or not
        if (isUserLoggedIn()) {
            System.out.println("\nHello, " + getLoggedInUser() + " you are already logged in. Please logout first.");
            return;
        }

        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            List<String> block = new ArrayList<>();
            boolean loggedIn = false;

            while ((line = br.readLine()) != null) {
                if (line.equals("---")) {
                    String userBlock = String.join("\n", block);
                    if (userBlock.contains("Username=" + username) && userBlock.contains("Password=" + password)) {
                        // that means the users info is correct and it should login the user

                        // now write the current loggedin user sessions info to the session.txt file
                        try (BufferedWriter sbw = new BufferedWriter(new FileWriter(SESSION_FILE_PATH, true))) {
                            sbw.write("Username=" + username + "\n");
                            sbw.write("Role=" + (userBlock.contains("Role=Admin") ? "Admin" : "User") + "\n");
                            sbw.write("Status=" + (userBlock.contains("Status=Rejected") ? "Rejected" : "Approved") + "\n");
                            sbw.write("---\n");

                        } catch (IOException e) {
                            System.out.println("Error writing to session file: " + e.getMessage());
                        }

                        // BufferedWriter sbw = new BufferedWriter(new FileWriter(SESSION_FILE_PATH,
                        // true));
                        // sbw.write("Username=" + username + "\n");
                        // sbw.write("Role=" + (userBlock.contains("Type=Admin") ? "Admin" : "User") +
                        // "\n");
                        // sbw.write("---\n");

                        // check if the user is admin or not
                        if (userBlock.contains("Role=Admin")) {
                            currentRole = "Admin";
                        } else {
                            currentRole = "User";
                        }

                        // System.out.println("the user block is : " +
                        // userBlock.contains("Type=Admin"));

                        System.out.println("\nLogin successful! Welcome, " + username + "!\n");
                        loggedIn = true;
                        break;
                    }
                    block.clear();
                } else {
                    block.add(line);
                }
            }

            if (!loggedIn) {
                System.out.println("Invalid username or password.\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // private static boolean isSessionUserExists(String input) {
    // // System.out.print("Enter search term (username/journey/car): ");
    // // String term = sc.nextLine().toLowerCase();
    // String term = input.toLowerCase();

    // try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
    // String line;
    // List<String> block = new ArrayList<>();
    // boolean found = false;

    // while ((line = br.readLine()) != null) {
    // if (line.equals("---")) {
    // String blockContent = String.join("\n", block);
    // if (blockContent.toLowerCase().contains(term)) {
    // System.out.println("\nFound Booking:\n" + blockContent + "\n---");
    // found = true;
    // return true;
    // }
    // block.clear();
    // } else {
    // block.add(line);
    // }
    // }

    // System.out.println("User found for: " + term);
    // } catch (IOException e) {
    // System.out.println("Error reading file: " + e.getMessage());
    // }
    // return false;
    // }

    /**
     * Register User features and functionalities
     * 
     */

    private static void registerUser(Scanner sc) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password (Note please enter password carefully, you cannot change it later !!): ");
            String password = sc.nextLine(); // for simplicity, password is same as username

            // username.toLowerCase();

            // check if the user already exists or not
            // boolean exists = isUserExists(username);
            // if (exists) {
            //     System.out.println("Cannot Register !! User already exists! \n");
            //     return;
            // }

            int id = getNextID();
            bw.write("ID=" + id + "\n");
            bw.write("Username=" + username + "\n");
            bw.write("Password=" + password + "\n");
            bw.write("Role=User\n");
            bw.write("Status=Rejected\n");
            // bw.write("Journey=" + journey.toString().trim() + "\n");
            // bw.write("Passengers=" + passengers + "\n");
            // bw.write("CarSuggested=" + car + "\n");
            bw.write("---\n");

            System.out.println("✅User added with ID " + id + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /*
     * Check if user exists or not, if exists return true else false
     */

    private static boolean isUserExists(String input) {
        // System.out.print("Enter search term (username/journey/car): ");
        // String term = sc.nextLine().toLowerCase();
        String term = input.toLowerCase();

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
                        return true;
                    }
                    block.clear();
                } else {
                    block.add(line);
                }
            }

            System.out.println("User found for: " + term);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    
    private static void searchUser(Scanner sc) {
        System.out.print("Enter search user (username): ");
        String term = sc.nextLine().toLowerCase();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            List<String> block = new ArrayList<>();
            boolean found = false;

            while ((line = br.readLine()) != null) {
                if (line.equals("---")) {
                    String blockContent = String.join("\n", block);
                    if (blockContent.toLowerCase().contains(term)) {
                        System.out.println("\nFound User:\n" + blockContent + "\n---");
                        found = true;
                    }
                    block.clear();
                } else {
                    block.add(line);
                }
            }

            if (!found)
                System.out.println("No User found for: " + term);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Update the users information
     * 
     */

    private static void approveUser(Scanner sc) {
        System.out.print("Enter user ID to approve: ");
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

                        System.out.print("Status (" + fields.get("Status") + "): ");
                        String status = sc.nextLine();
                        if (!status.isEmpty())
                            fields.put("Status", status);

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

                        System.out.print("Username (" + fields.get("Username") + "): ");
                        String newUsername = sc.nextLine();
                        if (!newUsername.isEmpty())
                            fields.put("Username", newUsername);

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

    /**
     * Delete the user from the users.txt file
     * 
     */

    private static void deleteUser(Scanner sc) {
        System.out.print("Enter user ID to delete: ");
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
            System.out.println(deleted ? "User deleted!" : "User ID not found.");

        } catch (IOException e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }
    }

    /**
     * Generate the new unique ID for the new user
     * 
     */

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

// public class Register {

// }
