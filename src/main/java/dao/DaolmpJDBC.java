/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Employee;
import Exception.DAO_exception;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author usuario
 */
public class DaolmpJDBC implements IDao {

    private final String JDBC_URL = "jdbc:mysql://localhost:3306/shop_db";
    private final String JDBC_USER = "root";
    private final String JDBC_PASS = "";
    private final String JDBC_DDBB = "shop_db";
    private final String JDBC_TABLE = "employees";
    

    private Connection connection;
    
    @Override
    public void connect() throws DAO_exception {
        try {
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
            System.out.println("Conexión establecida.");
            
        } catch (SQLException ex) {
        System.out.println("Error al conectar: " + ex.getMessage());
        }
    }

    @Override
    public Employee getEmployee(String user, String pw) throws DAO_exception {
        try {
            String sql = "SELECT * FROM employees WHERE username = ? AND password = ?";
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user);
            stmt.setString(2, pw);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Si encuentra una fila, construye y devuelve el Employee
                int    id       = rs.getInt("employeeId");
                String nombre   = rs.getString("name");
//                return new Employee(id, pw, nombre);
            }
        } catch (SQLException ex) {
            System.getLogger(DaolmpJDBC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return null;
    }

    @Override
    public void disconnect() throws DAO_exception {
        if (connection != null) 
        try {
            connection.close();
            
        } catch (SQLException ex) {
            
            System.getLogger(DaolmpJDBC.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

}
