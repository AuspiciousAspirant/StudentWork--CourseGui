package FinalProject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

public class DBUtility {
    private static final String DB_URL = "jdbc:mysql://database-cuny.c4piq2ndsfvh.us-west-1.rds.amazonaws.com:3306/CUNY_DB";
    private static final String USERNAME = "cst3613";
    private static final String PASSWORD = "password1";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    public static boolean checkStudentExists(String ssn) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Students WHERE SSN = ?")) {
            statement.setString(1, ssn);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If resultSet.next() is true, the student exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String retrieveStudentName(String ssn) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT firstName, mi, lastName FROM Students WHERE ssn = ?")) {
            statement.setString(1, ssn);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String mi = resultSet.getString("mi");
                String lastName = resultSet.getString("lastName");
                return firstName + " " + (mi.isEmpty() ? "" : mi + " ") + lastName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void populateListView(String ssn, ListView<String> listView) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT Course.courseId, Course.title, Enrollment.grade " +
                     "FROM Enrollment INNER JOIN Course ON Enrollment.courseId = Course.courseId " +
                     "WHERE Enrollment.ssn = ?")) {
            statement.setString(1, ssn);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String courseId = resultSet.getString("courseId");
                String title = resultSet.getString("title");
                String grade = resultSet.getString("grade");

                // Add course information to the ListView
                listView.getItems().add(courseId + " - " + title + " - " + grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void populateComboBox(String ssn, ComboBox<String> comboBox) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT courseId FROM Course " +
                     "WHERE courseId NOT IN (SELECT courseId FROM Enrollment WHERE ssn = ?)")) {
            statement.setString(1, ssn);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                comboBox.getItems().add(resultSet.getString("courseId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addClass(String ssn, String courseId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Enrollment (ssn, courseId, dateRegistered, grade) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, ssn);
            statement.setString(2, courseId);
            statement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            statement.setString(4, "A"); // Default grade
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeClass(String ssn, String courseId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Enrollment WHERE ssn = ? AND courseId = ?")) {
            statement.setString(1, ssn);
            statement.setString(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void registerStudent(String ssn, String firstName, String mi, String lastName) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Students (SSN, firstName, mi, lastName) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, ssn);
            statement.setString(2, firstName);
            statement.setString(3, mi);
            statement.setString(4, lastName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
