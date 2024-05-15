WearShareServer
Project Description
WearShareServer is a server application designed to facilitate clothes donations and exchanges. The system enables donors to donate clothes and earn rewards, associations to receive and distribute clothes, and stores to provide promotions to motivate donors. The project includes server-side functionality with database connections and file printing, as well as client-side for user data validation. The multi-threaded server efficiently handles multiple clients simultaneously.


Main Features

User Authentication
Description: Supports login and registration for different user roles (donors, associations, stores).
Implementation: Utilizes secure authentication mechanisms to ensure user data protection. Users can either log in or create a new account.

Donation Management
Description: Allows donors to donate clothes and manage their donations.
Implementation: Donors can enter details about the clothes they wish to donate, including type and size, and the system assigns these donations to the nearest association.

Exchange System
Description: Enables users to exchange points for clothes.
Implementation: Donors earn points for their donations, which they can then exchange for clothes from stores. The system checks the donor’s rewards and handles the exchange process.

Validation
Description: Ensures valid entries for clothing type and size.
Implementation: Implements validation logic to check clothing attributes before acceptance to maintain data integrity and user experience.

Database Integration
Description: Uses MySQL for storing user and donation data.
Implementation: Database connection is established using JDBC, and SQL queries are executed to manage user, donation, and store data.

Client-Server Communication
Description: Facilitates interactions between the server and client applications.
Implementation: Uses sockets and communication protocols to handle requests and responses between the server and multiple clients.

Multi-Threading
Description: Handles multiple clients simultaneously to ensure efficient operations.
Implementation: Implements multi-threading to handle concurrent client requests without blocking, ensuring efficient server performance.

IO Operations
Description: Manages input and output operations, including printing to files.
Implementation: Uses Java IO classes to read and write data to files for logging and data persistence.

Exception Handling
Description: Ensures robust operations by preventing and handling exceptions.
Implementation: Implements try-catch blocks and custom exception classes to manage errors gracefully, ensuring the system remains stable and secure.

Installation
Clone the repository:
bash


Copy code
git clone https://github.com/EssamZarei/WearShareServer.git

Open the project in your preferred IDE.

Set up the database:

Create a MySQL database.
Import the provided SQL script to set up the necessary tables.

Configure database connection:
Update the database connection settings in the configuration file.

Build and run the application:
Use your IDE or command line to build and run the project.

Usage
Register as a donor, association, or store.

Log in with your credentials.

Donors:
Donate clothes and manage your donations.

Associations:
Manage incoming donations and distribute them.

Stores:
Handle the exchange of points for clothes.

Contributing
Contributions are welcome! Please fork the repository and submit a pull request for any improvements or bug fixes.

License
This project is licensed under the MIT License. See the LICENSE file for details.

Contact
For any questions or suggestions, please contact Essam Zarei.
