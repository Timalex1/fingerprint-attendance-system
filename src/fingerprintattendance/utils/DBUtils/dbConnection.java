/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fingerprintattendance.utils.DBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author hp
 */
public class dbConnection {
    
    private static final String CONN = "jdbc:mysql://localhost/attendance";
            
    public static Connection connector() throws ClassNotFoundException{
        try{
            
                Class.forName("com.mysql.jdbc.Driver");
              Connection conn = DriverManager.getConnection(CONN, "root", "tim-alex1");
              return conn;
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
        
    }
    
}
