import java.sql.*;
import java.util.Scanner;
public class EmployeeManagementSystem {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/emp";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "sakthi@123";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                createEmployeeTable(connection);

                Scanner scanner = new Scanner(System.in);
                int choice;
                do {
                    printMenu();
                    System.out.print("Enter your choice: ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
                            addEmployee(connection);
                            break;
                        case 2:
                            displayAllEmployees(connection);
                            break;
                        case 3:
                            updateEmployee(connection);
                            break;
                        case 4:
                            deleteEmployee(connection);
                            break;
                        case 0:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } while (choice != 0);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("Employee Management System Menu:");
        System.out.println("1. Add new employee");
        System.out.println("2. View all employees");
        System.out.println("3. Update employee details");
        System.out.println("4. Delete an employee");
        System.out.println("0. Exit");
    }

    private static void createEmployeeTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS employees (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(50) NOT NULL," +
                "age INT," +
                "department VARCHAR(50)" +
                ")";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        }
    }

    private static void addEmployee(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO employees (name, age, department) VALUES (?, ?, ?)")) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter employee name: ");
            String name = scanner.nextLine();
            System.out.print("Enter employee age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter employee department: ");
            String department = scanner.nextLine();

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, department);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee added successfully.");
            } else {
                System.out.println("Failed to add employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayAllEmployees(Connection connection) {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM employees")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String department = resultSet.getString("department");
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ", Department: " + department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateEmployee(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE employees SET name = ?, age = ?, department = ? WHERE id = ?")) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter employee ID to update: ");
            int employeeId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter new age: ");
            int newAge = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new department: ");
            String newDepartment = scanner.nextLine();

            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, newAge);
            preparedStatement.setString(3, newDepartment);
            preparedStatement.setInt(4, employeeId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee details updated successfully.");
            } else {
                System.out.println("Failed to update employee details.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteEmployee(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM employees WHERE id = ?")) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter employee ID to delete: ");
            int employeeId = scanner.nextInt();

            preparedStatement.setInt(1, employeeId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Failed to delete employee.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

//javac -cp .;path/to/mysql-connector-java-x.x.xx.jar EmployeeManagementSystem.java
//java -cp .;path/to/mysql-connector-java-x.x.xx.jar EmployeeManagementSystem

//javac -cp .;U:\java\jdbc-x.x.xx.jar EmployeeManagementSystem.java