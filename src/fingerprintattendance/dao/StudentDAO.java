/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.dao;

import fingerprintattendance.model.Student;
import fingerprintattendance.utils.DBUtils.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author hp
 */
public class StudentDAO {

    static Connection conn = null;
    static PreparedStatement ps = null;



    //*************************************
    //INSERT a Student
    //*************************************

    public static boolean insertStudent(String name, String matric_no, String level, byte[] image) throws SQLException, ClassNotFoundException {

        conn = dbConnection.connector();

        //Declare an INSERT statement
        String sql = "INSERT INTO student(name, matric_no, level, image) VALUES(?,?,?,?)";

        try {

            ps = conn.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, matric_no);
            ps.setString(3, level);
            ps.setBytes(4, image);

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        } finally {
            if (ps != null) {
                //Close statement
                ps.close();
            }

        }

    }

    /**
     * // Get all Students from database //
     *
     */
    public static List<Student> getAllStudents() throws SQLException, ClassNotFoundException {

        conn = dbConnection.connector();
        //Declare an INSERT statement
        String sql = "SELECT * FROM student";

        //Execute SELECT statement
        try {

            ps = conn.prepareStatement(sql);

            //Get ResultSet from dbExecuteQuery
            ResultSet rs = ps.executeQuery();
            List<Student> students = getStudentList(rs);

            //return RS
            return students;
        } catch (SQLException e) {
            System.out.println("SQL STATEMENT HAS FAILED " + e);
            throw e;
        }

    }

    private static List<Student> getStudentList(ResultSet rs) throws SQLException {
        ObservableList<Student> students = FXCollections.observableArrayList();

        while (rs.next()) {
            Student student = new Student();

            student.setName(rs.getString("name"));
            student.setMatric_no(rs.getString("matric_no"));
            student.setImage_byte(rs.getBytes("image"));
            student.setLevel(rs.getString("level"));

            students.add(student);
        }

        return students;
    }

}
