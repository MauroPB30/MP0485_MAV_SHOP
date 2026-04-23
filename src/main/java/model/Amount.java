/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author usuario
 */
public class Amount {
    private double value;
    private String currency;

    public Amount(double value) {
        this.value = value;
        this.currency = "Euros"; 
    }


    public double getValue() {
        return value;
    }


    public void setValue(double value) {
        this.value = value;
    }


    public String getCurrency() {
        return currency;
    }

    // Método toString para mostrar el valor y la moneda
    @Override
    public String toString() {
        return value + " " + currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

