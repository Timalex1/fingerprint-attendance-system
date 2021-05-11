/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import fingerprintattendance.utils.DBUtils.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author hp
 */
public class AdminDAO {

    static Connection conn = null;
    static PreparedStatement pr = null;

    // Check if admin details exist in the database and Login the admin
    public static boolean login(String email, String password) throws SQLException, ClassNotFoundException {

        conn = dbConnection.connector();


        String query = "select * from admin WHERE email = ?";
        ResultSet rs = null;

        try {

            pr = conn.prepareStatement(query);

            pr.setString(1, email);

            rs = pr.executeQuery();

            if (rs.next()) {

                boolean verified = BCrypt.verifyer().verify(password.toCharArray(), rs.getString("password")).verified;

                if (verified) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
 finally {
            if (pr != null && rs != null) {
                pr.close();
                rs.close();
            }
        }
    }

    public static boolean insertAdmin(String email, String password) throws SQLException, ClassNotFoundException {

        conn = dbConnection.connector();

        //Declare an INSERT statement
        String sql = "INSERT INTO admin(email, password) VALUES(?,?)";

        try {
            pr = conn.prepareStatement(sql);

            pr.setString(1, email);
            pr.setString(2, password);

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
}
