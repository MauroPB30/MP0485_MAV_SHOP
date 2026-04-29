/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Employee;
import Exception.DAO_exception;
import java.util.List;
import model.Product;

/**
 *
 * @author usuario
 */
public interface IDao {

    public void connect() throws DAO_exception;

    public abstract Employee getEmployee(String user, String pw) throws DAO_exception;

    public void disconnect() throws DAO_exception;

    List<Product> readAllProducts() throws DAO_exception;

}
