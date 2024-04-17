# Renzone - Desktop Application for PlayStation Rent

Renzone is a desktop application developed using JavaFX and SQL Server, designed specifically for managing PlayStation rentals. This README provides an overview of the project, installation instructions, usage guidelines, and other important information.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
- [License](#license)

## Installation

1. Clone the repository to your local machine: https://github.com/FarhanAdiyasa/Renzone_Java.git

2. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).

3. Set up the SQL Server database:
- Create a new database named `Renzone`.
- Execute the SQL scripts located in the `sql-scripts-renzone` file to create the necessary tables and populate initial data.

4. Configure database connection:
- Open the `src/main/java/com/example/renzone/DBConnection/Connection.java` file.
- Update the database URL, username, and password according to your SQL Server configuration in the Connection and getConnect Method.
  ![image](https://github.com/FarhanAdiyasa/Renzone_Java/assets/119157451/a8812ac5-58af-46f8-9a87-9ace6b49690c)


5. Build and run the application from your IDE.

## Usage

Renzone is a PlayStation Rental application designed to assist service providers in renting consoles and accessories, managing assets, members, employees, transactions, and recording reports with a more effective application-based system. The application aims to make operational management more efficient by processing operational data that is application-based and integrated with a database.

Key features of the Renzone application include:

- **Rental Management:** Record rentals, schedule, cancel, and extend leases for PS consoles and accessories.
- **Asset Management:** Manage inventory and condition of assets such as PS consoles, controllers, and accessories.
- **Member and Employee Management:** Record member and employee information, and manage access rights related to rental operations.
- **Transaction Management:** Track rental transactions, payments, and cancellations.
- **Operational Reporting:** Record and analyze operational data for financial reporting and performance analysis.
- **Default Login Credentials: [see this](DEFAULT-LOGIN-RENZONE.txt)

For more detailed usage guidance, please refer to the [User Guide](UserGuide.pdf) included.

The Renzone application aims to provide an integrated and effective solution for PS rental businesses to manage day-to-day operations more efficiently and systematically.

## Technologies Used

- **JavaFX:** Used for building the user interface (UI) and frontend functionalities.
- **SQL Server:** Used for database management and storing business data.
- **Maven:** Dependency management and build tool.
