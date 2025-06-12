/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nam
 */
public class DBConnect {
     Connection conn = null;
    
    
    public DBConnect(String URL, String Username, String password){
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL, Username,password);
            System.out.println("");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, URL);
        } catch(SQLException ex){
            Logger.getLogger(DBConnect.class.getName()).log(Level.SEVERE, URL);
        }
        
    }
    public DBConnect(){
        
        this("jdbc:sqlserver://localhost:1468;databaseName=farmer", "sa", "123456");
    }
    
    public ResultSet getData(String sql){
        ResultSet rs = null;
        try {
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs= state.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DAOProducts.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    public static void main(String[] args) {
        new DBConnect();
    }
}
