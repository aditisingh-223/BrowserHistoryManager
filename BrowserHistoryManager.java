
import java.util.*;
import java.io.*;

public class BrowserHistoryManager {

    // File name
    private static final String FILE_NAME = "history.txt";

    // Data Structures
    LinkedList<String> history = new LinkedList<>();

    Stack<String> backStack = new Stack<>();

    Stack<String> forwardStack = new Stack<>();

    HashMap<String, Integer> visitCount = new HashMap<>();

    String currentPage = null;


    // =====================================
    // 1. VISIT A NEW PAGE
    // =====================================

    void visitPage(String url) {

        if (!isValidURL(url)) {
            System.out.println(
                "Invalid URL. Enter a URL like example.com."
            );
            return;
        }

        // Store previous page in Back Stack
        if (currentPage != null) {
            backStack.push(currentPage);
        }

        // Update current page
        currentPage = url;

        // Clear Forward Stack
        forwardStack.clear();

        // Add page to LinkedList
        history.add(url);

        // Update visit count
        visitCount.put(
            url,
            visitCount.getOrDefault(url, 0) + 1
        );

        System.out.println("Visited: " + url);
        System.out.println("Current page: " + currentPage);
    }


    // =====================================
    // 2. GO BACK
    // =====================================

    void goBack() {

        if (backStack.isEmpty()) {
            System.out.println("No previous page.");
            return;
        }

        // Save current page in Forward Stack
        forwardStack.push(currentPage);

        // Get previous page
        currentPage = backStack.pop();

        System.out.println("Back to: " + currentPage);
    }


    // =====================================
    // 3. GO FORWARD
    // =====================================

    void goForward() {

        if (forwardStack.isEmpty()) {
            System.out.println("No forward page.");
            return;
        }

        // Save current page in Back Stack
        backStack.push(currentPage);

        // Get next page
        currentPage = forwardStack.pop();

        System.out.println("Forward to: " + currentPage);
    }


    // =====================================
    // 4. SHOW BROWSING HISTORY
    // =====================================

    void showHistory() {

        System.out.println("\n===== BROWSING HISTORY =====");

        if (history.isEmpty()) {
            System.out.println("No browsing history available.");
            return;
        }

        int index = 1;

        for (String page : history) {

            System.out.println(index + ". " + page);
            index++;
        }

        System.out.println("Current page: " +
                (currentPage == null ? "None" : currentPage));
    }


    // =====================================
    // 5. SHOW WEBSITE VISIT COUNT
    // =====================================

    void showVisitCount() {

        System.out.println("\n===== WEBSITE VISIT COUNTS =====");

        if (visitCount.isEmpty()) {
            System.out.println("No website visits recorded.");
            return;
        }

        for (String site : visitCount.keySet()) {

            System.out.println(
                site + " : " + visitCount.get(site) + " visits"
            );
        }
    }


    // =====================================
    // 6. VALIDATE URL
    // =====================================

    boolean isValidURL(String url) {

        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        url = url.trim();

        // URL must contain a dot
        if (!url.contains(".")) {
            return false;
        }

        // Dot cannot be the first or last character
        if (url.startsWith(".") || url.endsWith(".")) {
            return false;
        }

        // Spaces are not allowed
        if (url.matches(".*\\s+.*")) {
            return false;
        }

        return true;
    }


    // =====================================
    // 7. LOAD HISTORY FROM FILE
    // =====================================

    void loadHistory() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No saved history found.");
            return;
        }

        try (BufferedReader reader =
                new BufferedReader(new FileReader(file))) {

            String url;

            while ((url = reader.readLine()) != null) {

                url = url.trim();

                if (!isValidURL(url)) {
                    continue;
                }

                // Restore LinkedList
                history.add(url);

                // Restore visit count
                visitCount.put(
                    url,
                    visitCount.getOrDefault(url, 0) + 1
                );
            }

            // Restore current page and Back Stack
            if (!history.isEmpty()) {

                currentPage = history.getLast();

                for (int i = 0; i < history.size() - 1; i++) {
                    backStack.push(history.get(i));
                }
            }

            System.out.println(
                "History loaded successfully. Total visits: "
                + history.size()
            );

        } catch (IOException e) {

            System.out.println(
                "Error loading history: " + e.getMessage()
            );
        }
    }


    // =====================================
    // 8. SAVE HISTORY TO FILE
    // =====================================

    void saveHistory() {

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (String page : history) {

                writer.write(page);
                writer.newLine();
            }

            System.out.println(
                "History saved successfully!"
            );

        } catch (IOException e) {

            System.out.println(
                "Error saving history: " + e.getMessage()
            );
        }
    }


    // =====================================
    // 9. DISPLAY MENU
    // =====================================

    void displayMenu() {

        System.out.println("\n================================");
        System.out.println("     BROWSER HISTORY MANAGER");
        System.out.println("================================");

        System.out.println("1. Visit a new page");
        System.out.println("2. Go Back");
        System.out.println("3. Go Forward");
        System.out.println("4. Show browsing history");
        System.out.println("5. Show visit count");
        System.out.println("6. Exit");

        System.out.println("================================");
        System.out.print("Enter your choice: ");
    }


    // =====================================
    // 10. MAIN METHOD
    // =====================================

    public static void main(String[] args) {

        BrowserHistoryManager app =
                new BrowserHistoryManager();

        Scanner sc = new Scanner(System.in);

        // Load previous history
        app.loadHistory();

        boolean running = true;

        while (running) {

            app.displayMenu();

            String input = sc.nextLine().trim();

            int choice;

            try {
                choice = Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                    "Invalid choice. Enter a number from 1 to 6."
                );

                continue;
            }

            switch (choice) {

                case 1:

                    System.out.print("Enter URL: ");

                    String url = sc.nextLine();

                    app.visitPage(url);

                    break;


                case 2:

                    app.goBack();

                    break;


                case 3:

                    app.goForward();

                    break;


                case 4:

                    app.showHistory();

                    break;


                case 5:

                    app.showVisitCount();

                    break;


                case 6:

                    app.saveHistory();

                    System.out.println(
                        "Thank you for using Browser History Manager!"
                    );

                    running = false;

                    break;


                default:

                    System.out.println(
                        "Invalid choice. Please enter 1 to 6."
                    );
            }
        }

        sc.close();
    }
}