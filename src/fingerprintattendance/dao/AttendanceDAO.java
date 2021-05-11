package fingerprintattendance.dao;

import fingerprintattendance.utils.DBUtils.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author hp
 */
public class AttendanceDAO {

    static Connection conn = null;
    static PreparedStatement pr = null;

    public static boolean takeAttendance(String course, String student, String date) throws ClassNotFoundException, SQLException {

        conn = dbConnection.connector();

        String query = "INSERT INTO attendance(course, student, date) VALUES(?,?,?)";

        try {
            pr = conn.prepareStatement(query);

            pr.setString(1, course);
            pr.setString(2, student);

            pr.setString(3, date);


            pr.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;

        } finally {
            if (pr != null) {
                //Close statement
                pr.close();
            }

        }

    }

    public static boolean checkStudentAttended(String course, String student, String date) throws ClassNotFoundException, SQLException {
        conn = dbConnection.connector();

        String query = "select * from attendance WHERE course = ? and student = ? and date = ?";
        ResultSet rs = null;

        try {

            pr = conn.prepareStatement(query);

            pr.setString(1, course);
            pr.setString(2, student);
            pr.setString(3, date);


            rs = pr.executeQuery();

            if (rs.next()) {

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (pr != null && rs != null) {
                pr.close();
                rs.close();
            }
        }
    }

    public static ObservableList<String> getAllAttendanceByCourseAndDate(String course, String date) throws SQLException, ClassNotFoundException {

        conn = dbConnection.connector();

        String query = "select * from attendance WHERE course = ? and date = ?";
        ResultSet rs = null;

        ObservableList<String> students = FXCollections.observableArrayList();

        try {

            pr = conn.prepareStatement(query);

            pr.setString(1, course);
            pr.setString(2, date);

            rs = pr.executeQuery();

            while (rs.next()) {
                students.add(rs.getString("student"));
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (pr != null && rs != null) {
                pr.close();
                rs.close();
            }
        }

        return students;
    }

}
