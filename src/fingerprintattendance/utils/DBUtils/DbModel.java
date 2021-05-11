package fingerprintattendance.utils.DBUtils;

import fingerprintattendance.utils.DBUtils.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hp
 */
public class DbModel {

    static Connection conn;
    static Statement stmt = null;
    static PreparedStatement p_stmt = null;
    static ResultSet resultSet = null;

    private static DbModel handler = null;

    public DbModel() {

        try {
            this.conn = dbConnection.connector();

            if (this.conn == null) {
                System.exit(1);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isDatabaseConnected() {
        try {
            return !conn.isClosed();
        } catch (SQLException ex) {
            Logger.getLogger(DbModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static DbModel getInstance() {
        if (handler == null) {
            handler = new DbModel();
        }
        return handler;
    }

    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {

        try {
            //Connect to DB (Establish MySql Connection)
            getInstance();
            System.out.println("Select statement: " + queryStmt + "\n");

            //Create statement
            p_stmt = conn.prepareStatement(queryStmt);

            //Execute select (query) operation
            resultSet = p_stmt.executeQuery();

            
        return resultSet;
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
//                //Close resultSet
                resultSet.close();
            }
            if (p_stmt != null) {
                //Close Statement
                p_stmt.close();
            }
            //Close connection

            conn.close();
        }
//        return null;
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        //Declare statement as null
        Statement stmt = null;
        try {
            //Connect to DB (Establish Oracle Connection)
//            dbConnect();
            getInstance();
            //Create Statement
            stmt = conn.createStatement();
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }

        }

    }
}
