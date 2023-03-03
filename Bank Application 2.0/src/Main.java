import java.time.LocalDateTime;
import java.util.Scanner;
import java.sql.*;
public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = false;
        while(!exit) {
            System.out.println("--------------------------------");
            System.out.println("   How can we help you today?");
            System.out.println("--------------------------------");
            System.out.println("1. Create New Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("--------------------------------");
            System.out.println("Enter the service number: ");

            // While loop to validate user input.
            int userInput = 0;
            boolean isValidInput = false;
            while(!isValidInput){
                if(input.hasNextInt()){
                    userInput = input.nextInt();
                    if(userInput > 0 && userInput <= 3) { // check if input is within valid range
                        isValidInput = true;
                    } else {
                        System.out.println("Invalid input. Please enter a valid number.");
                        System.out.println("-------------------------------------------");
                        System.out.print("Enter the service number:");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    System.out.println("-------------------------------------------");
                    System.out.print("Enter the service number:");
                    input.next();
                }
            }
            // Conditional statements to direct user to chosen service.
            if(userInput == 1){
                newAccount();
            } else if(userInput == 2){
                login();
            } else if(userInput == 3){
                exit = true;
            }
        }
        input.close();
        System.out.println("Thank you! Have a good day.");
    }

    // This method creates a new account and appends it to accounts.txt file. (Also creates the needed files if not created yet.)
    public static void newAccount(){
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------");
        System.out.println("        Account Creation");
        System.out.println("--------------------------------");
        System.out.print("Username: ");
        String username = input.next();

        // Validate if the username already exists in accounts.txt file.
        boolean usernameExists = false;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank Application", "mysqlUsername", "mysqlPassword");
            PreparedStatement stmt = conn.prepareStatement("SELECT username FROM Users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Username already exists.");
                usernameExists = true;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Proceed to enter account password if there is no existing username.
        if (!usernameExists) {
            System.out.print("Password: ");
            String password = input.next();

            // Inputs the Username, Password, and default Balance into the table Users.
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank Application", "mysqlUsername", "mysqlPassword");
                String insertQuery = "INSERT INTO Users (Username, Password, Balance) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.setDouble(3, 0.0); // Set the initial balance to 0.0
                pstmt.executeUpdate();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }

            // Creates the user's transaction history table.
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank Application", "mysqlUsername", "mysqlPassword");
                String createTableQuery = "CREATE TABLE " + username + "_history ("
                        + "id INT NOT NULL AUTO_INCREMENT,"
                        + "date DATETIME,"
                        + "type VARCHAR(20),"
                        + "amount DOUBLE(10, 2),"
                        + "balance DOUBLE(10, 2),"
                        + "PRIMARY KEY (id)"
                        + ")";
                PreparedStatement pstmt = conn.prepareStatement(createTableQuery);
                pstmt.executeUpdate();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // This method authenticates if the username and password match the ones saved in accounts.txt file.
    public static void login(){
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------");
        System.out.println("             Login");
        System.out.println("--------------------------------");
        System.out.print("Username: ");
        String username = input.next();
        System.out.print("Password: ");
        String password = input.next();
        System.out.println("--------------------------------");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank Application", "mysqlUsername", "mysqlPassword");
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login Successful!");
                String authenticatedUsername = rs.getString("username");
                services(authenticatedUsername);
            } else {
                System.out.println("Invalid username or password.");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // This method transfers you to the desired bank service depending on user input.
    public static void services(String username){
        boolean exit = false;
        Scanner input = new Scanner(System.in);
        while(!exit) {
            System.out.println("--------------------------------");
            System.out.println("            Services");
            System.out.println("--------------------------------");
            System.out.println("1. Check Account Balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transaction History");
            System.out.println("5. Exit");
            System.out.println("--------------------------------");
            System.out.println("Enter the service number: ");

            // While loop to validate user input.
            int userInput = 0;
            boolean isValidInput = false;
            while(!isValidInput){
                if(input.hasNextInt()){
                    userInput = input.nextInt();
                    if(userInput > 0 && userInput <= 5) {
                        isValidInput = true;
                    } else {
                        System.out.println("Invalid input. Please enter a valid number.");
                        System.out.println("-------------------------------------------");
                        System.out.print("Enter the service number:");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    System.out.println("-------------------------------------------");
                    System.out.print("Enter the service number:");
                    input.next();
                }
            }
            // Open method based on user input.
            if(userInput == 1){
                checkBalance(username);
            } else if(userInput == 2){
                withdraw(username);
            } else if(userInput == 3){
                deposit(username);
            } else if(userInput == 4){
                transactionHistory(username);
            } else if(userInput == 5){
                exit = true;
            }
        }
    }

    // This method checks the balance of the user based on their (username)_balance.txt file.
    public static void checkBalance(String username) {
        // Get the balance of the user with the given username.
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank Application", "mysqlUsername", "mysqlPassword");
            PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM Users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.printf("Your current balance is %.2f%n", balance);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // This method withdraws from the user's balance and saves the transaction in the (username)_history.txt file.
    public static void withdraw(String username){
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------");
        System.out.println("             Withdraw");
        System.out.println("--------------------------------");
        System.out.print("Enter the amount to withdraw: ");
        double amount = input.nextDouble();

        // Get current balance
        double currentBalance = 0.0;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank Application", "mysqlUsername", "mysqlPassword");
            PreparedStatement pstmt = conn.prepareStatement("SELECT balance FROM Users WHERE username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                currentBalance = rs.getDouble("balance");
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        // Check if amount is valid
        if(amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        } else if(amount > currentBalance) {
            System.out.println("Insufficient funds.");
            return;
        }

        // Update balance and transaction history
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank Application", "mysqlUsername", "mysqlPassword");
            conn.setAutoCommit(false);

            // Update balance in Users table
            PreparedStatement pstmt = conn.prepareStatement("UPDATE Users SET balance = balance - ? WHERE username = ?");
            pstmt.setDouble(1, amount);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            pstmt.close();

            // Update current balance
            currentBalance -= amount;

            // Insert withdrawal transaction into transaction history table
            pstmt = conn.prepareStatement("INSERT INTO " + username + "_history (type, amount, date, balance) VALUES (?, ?, ?, ?)");
            pstmt.setString(1, "Withdrawal");
            pstmt.setDouble(2, amount);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setDouble(4, currentBalance);
            pstmt.executeUpdate();
            pstmt.close();

            conn.commit();
            conn.setAutoCommit(true);
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
    }

    // This method deposit's to the user's balance and saves the transaction to the (username)_history.txt file.
    public static void deposit(String username) {
        Scanner input = new Scanner(System.in);
        System.out.println("--------------------------------");
        System.out.println("             Deposit");
        System.out.println("--------------------------------");
        System.out.print("Enter the amount to deposit: ");
        double amount = input.nextDouble();

        // Check if amount is valid
        if(amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        }

        // Update balance and transaction history
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank Application", "mysqlUsername", "mysqlPassword");
            conn.setAutoCommit(false);

            // Get current balance
            double currentBalance = 0.0;
            PreparedStatement pstmt = conn.prepareStatement("SELECT balance FROM Users WHERE username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                currentBalance = rs.getDouble("balance");
            }
            rs.close();
            pstmt.close();

            // Update balance in Users table
            pstmt = conn.prepareStatement("UPDATE Users SET balance = balance + ? WHERE username = ?");
            pstmt.setDouble(1, amount);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            pstmt.close();

            // Update current balance
            currentBalance += amount;

            // Insert deposit transaction into transaction history table
            pstmt = conn.prepareStatement("INSERT INTO " + username + "_history (type, amount, date, balance) VALUES (?, ?, ?, ?)");
            pstmt.setString(1, "Deposit");
            pstmt.setDouble(2, amount);
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setDouble(4, currentBalance);
            pstmt.executeUpdate();
            pstmt.close();

            conn.commit();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Deposit Successful!");
    }

    // This method shows the transactions done by the user in the (username)_history.txt file.
    public static void transactionHistory(String username){
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank Application", "mysqlUsername", "mysqlPassword");
            PreparedStatement pstmt = conn.prepareStatement("SELECT type, amount, date, balance FROM " + username + "_history");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                Timestamp date = rs.getTimestamp("date");
                double balance = rs.getDouble("balance");

                System.out.printf("%-12s $%-10.2f %s (Balance: $%.2f)\n", type, amount, date, balance);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}