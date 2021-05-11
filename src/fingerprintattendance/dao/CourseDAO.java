/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.dao;

import fingerprintattendance.model.Course;
import fingerprintattendance.utils.DBUtils.DbModel;
import fingerprintattendance.utils.DBUtils.DbUtils;
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
public class CourseDAO {
    
    
    //*************************************
    //INSERT a course
    //*************************************
    public static boolean insertCourse (String title, String code, String teacher) throws SQLException, ClassNotFoundException {
        //Declare an INSERT statement
         String sql = "INSERT INTO COURSE(title, code, teacher) VALUES('"+title+"','"+code+"','"+teacher+"')";
         
         try{
             //Pass the statement to be executed
             DbModel.dbExecuteUpdate(sql);
             return true;
         }
         catch(SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
         
    }
    
    public static ObservableList<Course> getAllCourses() throws SQLException, ClassNotFoundException{        
       
//Declare an INSERT statement
         String sql = "SELECT * FROM course";
         PreparedStatement ps = null;
         //Execute SELECT statement
         try{
             
             Connection conn = dbConnection.connector();
             ps = conn.prepareStatement(sql);
             
             //Get ResultSet from dbExecuteQuery
              ResultSet rs = ps.executeQuery();
             ObservableList<Course> courses = getCourseList(rs);
             
             //return RS
             
             return courses;
         }
         catch(SQLException e){
             System.out.println("SQL STATEMENT HAS FAILED " + e);
             throw e;
         }
         
    }

    private static ObservableList<Course> getCourseList(ResultSet rs) throws SQLException, ClassNotFoundException{
       
        ObservableList<Course> courses = FXCollections.observableArrayList();
        
        while (rs.next()) {
           Course course = new Course();
           
           course.setCourse_title(rs.getString("title"));
           course.setCourse_code(rs.getString("code"));
           
           courses.add(course);
        }
        
        return courses;
    }
    
}
