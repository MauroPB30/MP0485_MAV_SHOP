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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Product;

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

    private static final String SQL_SELECT_ALL_PRODUCTS = "SELECT * FROM products";
    private static final String SQL_INSERT_PRODUCT = "INSERT INTO products (name, price, stock, available) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_STOCK = "UPDATE products SET stock = ? WHERE name = ?";
    private static final String SQL_DELETE_PRODUCT = "DELETE FROM products WHERE name = ?";
    private static final String SQL_SELECT_EMPLOYEE = "SELECT * FROM employees WHERE username = ? AND password = ?";

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
            PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_EMPLOYEE);
            stmt.setString(1, user);
            stmt.setString(2, pw);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("employeeId");
                String nombre = rs.getString("name");
                return new Employee(id, null, nombre);
            }
        } catch (SQLException ex) {
            throw new DAO_exception("Error al buscar empleado: " + ex.getMessage());
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

    @Override
    public List<Product> readAllProducts() throws DAO_exception {
        List<Product> products = new ArrayList<>();
        Statement instruction = null;
        ResultSet rs = null;

        try {
            instruction = connection.createStatement();
            rs = instruction.executeQuery(SQL_SELECT_ALL_PRODUCTS);

            while (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                boolean available = rs.getBoolean("available");
                products.add(new Product(name, price, available, stock));
            }

        } catch (SQLException ex) {
            throw new DAO_exception("Error al leer productos: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (instruction != null) {
                    instruction.close();
                }
            } catch (SQLException ex) {
                throw new DAO_exception("Error al cerrar recursos: " + ex.getMessage());
            }
        }
        return products;
    }

    @Override
    public boolean updateStock(String name, int newStock) throws DAO_exception {
        try {
            String sql = "UPDATE products SET stock = ? WHERE name = ?";
            PreparedStatement stmt = connection.prepareStatement(SQL_UPDATE_STOCK);
            stmt.setInt(1, newStock);    // nuevo valor de stock
            stmt.setString(2, name);     // producto a actualizar
            int rows = stmt.executeUpdate();
            return rows > 0; // true si actualizó alguna fila
        } catch (SQLException ex) {
            throw new DAO_exception("Error actualizando stock: " + ex.getMessage());
        }
    }

    @Override
    public boolean insertProduct(Product p) throws DAO_exception {
        try {
            String sql = "INSERT INTO products (name, price, stock, available) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(SQL_INSERT_PRODUCT);
            stmt.setString(1, p.getName());
            stmt.setDouble(2, p.getWholesalerPrice().getValue()); // Amount → double
            stmt.setInt(3, p.getStock());
            stmt.setBoolean(4, p.isAvailable());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean deleteProduct(String name) throws DAO_exception {
        try {
            String sql = "DELETE FROM products WHERE name = ?";
            PreparedStatement stmt = connection.prepareStatement(SQL_DELETE_PRODUCT);
            stmt.setString(1, name);
            int rows = stmt.executeUpdate();
            return rows > 0; // true si borró alguna fila
        } catch (SQLException ex) {
            throw new DAO_exception("Error eliminando producto: " + ex.getMessage());
        }
    }
}
