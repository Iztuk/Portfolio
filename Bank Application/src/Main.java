import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
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
            File accounts = new File("accounts.txt");
            if (!accounts.exists()) {
                accounts.createNewFile();
            }
            Scanner usernameCheck = new Scanner(accounts);
            while (usernameCheck.hasNextLine()) {
                String data = usernameCheck.nextLine();
                if (data.equals(username)) {
                    System.out.println("Username already exists.");
                    usernameExists = true;
                    break;
                }
                // Skip over the password for each account.
                if(usernameCheck.hasNextLine()){
                    usernameCheck.nextLine();
                }

            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Proceed to enter account password if there is no existing username.
        if(!usernameExists){
            System.out.print("Password: ");
            String password = input.next();

            // Save the new account to accounts.txt file.
            try {
                FileWriter accountsWriter = new FileWriter("accounts.txt", true);
                accountsWriter.write(System.lineSeparator() + username + "\n");
                accountsWriter.write(password + "\n");
                accountsWriter.close();
            } catch(IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

            // Create files for user account balance and transaction history.
            try{
                File balanceFile = new File(username + "_balance.txt");
                balanceFile.createNewFile();
                FileWriter balanceWriter = new FileWriter(balanceFile);
                balanceWriter.write("0.0\n");
                balanceWriter.close();

                File historyFile = new File(username + "_history.txt");
                historyFile.createNewFile();
                FileWriter historyWriter = new FileWriter(historyFile);
                historyWriter.write("No previous transactions.\n");
                historyWriter.close();
            } catch(IOException e){
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

        try{
            boolean isAuthenticated = false;
            Scanner accountsReader = new Scanner(new File("accounts.txt"));
            while(accountsReader.hasNextLine()) {
                accountsReader.nextLine(); // Skips the header line.
                if(accountsReader.hasNextLine()){
                    String storedUsername = accountsReader.nextLine();
                    if(accountsReader.hasNextLine()){
                        String storedPassword = accountsReader.nextLine();

                        if(username.equals(storedUsername) && password.equals(storedPassword)) {
                            isAuthenticated = true;
                            break;
                        }
                    }
                }
            }
            accountsReader.close();
            if(isAuthenticated){
                System.out.println("Login Successful!");
                services(username);
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (FileNotFoundException e){
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
    public static void checkBalance(String username){
        double balance = 0.0;
        try{
            File balanceFile = new File(username + "_balance.txt");
            Scanner balanceReader = new Scanner(balanceFile);
            balance = Double.parseDouble(balanceReader.nextLine());
            balanceReader.close();
        } catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Your balance is: $" + balance);
    }

    // This method withdraws from the user's balance and saves the transaction in the (username)_history.txt file.
    public static void withdraw(String username){
        Scanner input = new Scanner(System.in);

        // Get user's input balance from their balance file.
        double balance = 0;
        try{
            File balanceFile = new File(username + "_balance.txt");
            Scanner balanceReader = new Scanner(balanceFile);
            balance = Double.parseDouble(balanceReader.nextLine());
            balanceReader.close();
        } catch(FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }

        // User input for withdrawal amount.
        System.out.print("Enter withdrawal amount: $");
        double amount = input.nextDouble();

        // Validate user input.
        if(amount < 0){
            System.out.println("Error: Withdrawal amount cannot be negative.");
            return;
        }
        if(amount > balance){
            System.out.println("Error: Withdrawal amount cannot exceed balance.");
            return;
        }

        // Subtract amount from balance.
        balance -= amount;

        // Write updated balance to the balance file.
        try{
            FileWriter balanceWriter = new FileWriter(username + "_balance.txt");
            balanceWriter.write(String.format("%.2f\n", balance));
            balanceWriter.close();
        } catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }

        // Add transaction to the transaction history file.
        try{
            FileWriter historyWriter = new FileWriter(username + "_history.txt", true);
            historyWriter.write(String.format("Withdrawal of $%.2f on %s\n", amount, LocalDateTime.now()));
            historyWriter.close();
        } catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Withdrawal Successful!");
    }

    // This method deposit's to the user's balance and saves the transaction to the (username)_history.txt file.
    public static void deposit(String username){
        Scanner input = new Scanner(System.in);

        // Get user's input balance from their balance file.
        double balance = 0;
        try{
            File balanceFile = new File(username + "_balance.txt");
            Scanner balanceReader = new Scanner(balanceFile);
            balance = Double.parseDouble(balanceReader.nextLine());
            balanceReader.close();
        } catch(FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }

        // User input for withdrawal amount.
        System.out.print("Enter deposit amount: $");
        double amount = input.nextDouble();

        // Validate user input.
        if(amount < 0){
            System.out.println("Error: Deposit amount cannot be negative.");
            return;
        }

        // Add amount from balance.
        balance += amount;

        // Write updated balance to the balance file.
        try{
            FileWriter balanceWriter = new FileWriter(username + "_balance.txt");
            balanceWriter.write(String.format("%.2f\n", balance));
            balanceWriter.close();
        } catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }

        // Add transaction to the transaction history file.
        try{
            FileWriter historyWriter = new FileWriter(username + "_history.txt", true);
            historyWriter.write(String.format("Deposit of $%.2f on %s\n", amount, LocalDateTime.now()));
            historyWriter.close();
        } catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Deposit Successful!");
    }

    // This method shows the transactions done by the user in the (username)_history.txt file.
    public static void transactionHistory(String username){
        Scanner input = new Scanner(System.in);
        try{
            File historyFile = new File(username + "_history.txt");
            Scanner historyReader = new Scanner(historyFile);
            while(historyReader.hasNextLine()){
                System.out.println(historyReader.nextLine());
            }
            historyReader.close();
        } catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

}