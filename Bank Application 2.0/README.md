Bank Application 2.0

This is an upgrade from the initial Bank Application project.
This application will utilize a MySQL database to save user information instead of using
txt documents.

Requirements:
1. I used Oracle OpenJDK version 19. (Not sure if this changes anything)
2. MySQL server.
3. JDBC driver for MySQL.
4. Update MySQL Username and Password for lines:
   63, 84, 99, 131, 208, 236, 262, 310, and 355.
5. Add the 'Bank Application' database to MySQL.

Features:
   - Create account.
   - Check account balance.
   - Withdraw from account.
   - Deposit to account.
   - Check transaction history.

Usage:
(Make sure your project is using openjdk-19 as the SDK. Change it on the project settings.)
To use this application you need to have MySQL server installed. Have the mysql-connector jar file 
added to the Project Structures dependencies. (File/Project Structures/Dependencies/Add the file)
Update the MySQL username and password on the stated lines to your own MySQL password saved on your
system. Add the 'Bank Application' database to your MySQL to have the Users table save the accounts created.

1. Create an account.
2. Login.
3. Enjoy!

Installation:
- MySQL (I used workbench to access it because it's easier lol)
- mysql-connector jar file (8.0.32 included in the application folder)

