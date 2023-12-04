import java.sql.*;
import java.util.Scanner;

public class StudentGradeManagement {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/student";
    private static final String USER = "root";
    private static final String PASSWORD = "sakthi@123";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            createTableIfNotExists(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Add new student");
                System.out.println("2. Calculate and display average grade for each student");
                System.out.println("3. Retrieve and display students enrolled in a specific course");
                System.out.println("4. Update grades for a student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addNewStudent(connection, scanner);
                        break;
                    case 2:
                        calculateAndDisplayAverageGrade(connection);
                        break;
                    case 3:
                        retrieveStudentsInCourse(connection, scanner);
                        break;
                    case 4:
                        updateStudentGrades(connection, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting the program.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTableIfNotExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "course VARCHAR(255) NOT NULL," +
                    "grade INT NOT NULL)";
            statement.executeUpdate(createTableQuery);
        }
    }

    private static void addNewStudent(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter student name: ");
        String name = scanner.next();

        System.out.print("Enter student course: ");
        String course = scanner.next();

        System.out.print("Enter student grade: ");
        int grade = scanner.nextInt();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO students (name, course, grade) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, course);
            preparedStatement.setInt(3, grade);
            preparedStatement.executeUpdate();
            System.out.println("Student added successfully.");
        }
    }

    private static void calculateAndDisplayAverageGrade(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT id, name, course, AVG(grade) AS average_grade FROM students GROUP BY id")) {

            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Course: " + resultSet.getString("course"));
                System.out.println("Average Grade: " + resultSet.getDouble("average_grade"));
                System.out.println("------------------------");
            }
        }
    }

    private static void retrieveStudentsInCourse(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter the course to retrieve students: ");
        String course = scanner.next();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM students WHERE course = ?")) {
            preparedStatement.setString(1, course);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id"));
                    System.out.println("Name: " + resultSet.getString("name"));
                    System.out.println("Course: " + resultSet.getString("course"));
                    System.out.println("Grade: " + resultSet.getInt("grade"));
                    System.out.println("------------------------");
                }
            }
        }
    }

    private static void updateStudentGrades(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter the ID of the student to update grades: ");
        int studentId = scanner.nextInt();

        System.out.print("Enter new grade: ");
        int newGrade = scanner.nextInt();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE students SET grade = ? WHERE id = ?")) {
            preparedStatement.setInt(1, newGrade);
            preparedStatement.setInt(2, studentId);
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("Student grades updated successfully.");
            } else {
                System.out.println("No student found with the provided ID.");
            }
        }
    }
}