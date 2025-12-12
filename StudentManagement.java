package StudentManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class StudentManagement {

    static final String URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root";
    static final String PASS = "";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver Loaded Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            System.out.println("\n--- Student Management System (JDBC) ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by Roll No");
            System.out.println("4. Delete Student by Roll No");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addStudent(sc);
                case 2 -> viewStudents();
                case 3 -> searchStudent(sc);
                case 4 -> deleteStudent(sc);
                case 5 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // Add Student (INSERT)
    static void addStudent(Scanner sc) {
        System.out.print("Enter Roll No: ");
        int roll = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Age: ");
        int age = sc.nextInt();

        String sql = "INSERT INTO students VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roll);
            ps.setString(2, name);
            ps.setInt(3, age);

            ps.executeUpdate();
            System.out.println("Student added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // View Students (SELECT)
    static void viewStudents() {
        String sql = "SELECT * FROM students";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- Student List ---");

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    "Roll No: " + rs.getInt(1) +
                    ", Name: " + rs.getString(2) +
                    ", Age: " + rs.getInt(3)
                );
            }

            if (!found) System.out.println("No students found.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Search Student (SELECT WHERE)
    static void searchStudent(Scanner sc) {
        System.out.print("Enter Roll No: ");
        int roll = sc.nextInt();

        String sql = "SELECT * FROM students WHERE rollNo = ?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roll);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Found: Roll No: " + rs.getInt(1) +
                        ", Name: " + rs.getString(2) +
                        ", Age: " + rs.getInt(3));
            } else {
                System.out.println("Student not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete Student (DELETE WHERE)
    static void deleteStudent(Scanner sc) {
        System.out.print("Enter Roll No: ");
        int roll = sc.nextInt();

        String sql = "DELETE FROM students WHERE rollNo = ?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roll);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Student deleted successfully!");
            else
                System.out.println("No student found with given roll number.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
