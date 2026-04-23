/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Scanner;
import main.Ilogable;
import dao.DaolmpJDBC;
import dao.IDao;
import Exception.DAO_exception;

/**
 *
 * @author usuario
 */
public class Employee extends Person implements Ilogable {

    private int employeeId;
    public IDao dao;

//    private final String PASSWORD = "test";
//    private final int EMPLOYEEID = 123;

    public Employee(int employeeId, DaolmpJDBC dao, String name) {
        super(name);
        this.employeeId = employeeId;
        this.dao = dao;
    }


    @Override
    public boolean login(String user, String password) {
        try {
            dao.connect();
            Employee found = dao.getEmployee(user, password);
            dao.disconnect();
            return found != null;
        } catch (DAO_exception dExcp) {
            System.out.println("Error en login" + dExcp.getMessage());
            return false;
        }
    }
    
    public int getEmployeedId() {
        return employeeId;
    }
}

