# Renzone - Desktop Application for PlayStation Rent

Renzone is a desktop application developed using JavaFX and SQL Server, designed specifically for managing PlayStation rentals. This README provides an overview of the project, installation instructions, usage guidelines, and other important information.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
- [License](#license)

## Installation

1. Clone the repository to your local machine:
git clone https://github.com/your-username/renzone.git

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

Upon launching Renzone, you will be presented with the main dashboard containing options to manage rentals, customers, assets, and generate reports. Here are some key features and usage guidelines:

- **Managing Rentals:** Add new rentals, view existing rentals, extend or cancel rentals.
- **Managing Customers:** Add new customers, update customer information, view rental history.
- **Managing Assets:** Track PlayStation consoles, controllers, and accessories, manage stock levels.
- **Generating Reports:** Generate financial reports, analyze revenue, expenses, and profitability.

Refer to the [User Manual](UserGuide.pdf) for detailed instructions on using Renzone.

## Technologies Used

- **JavaFX:** Used for building the user interface (UI) and frontend functionalities.
- **SQL Server:** Used for database management and storing business data.
- **Maven:** Dependency management and build tool.
