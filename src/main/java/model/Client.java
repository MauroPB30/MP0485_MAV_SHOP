/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import main.Payable;

/**
 *
 * @author usuario
 */
public class Client extends Person implements Payable {

    private int memberId;
    private Amount balance;

    private final int MEMBER_ID = 456;
    private final double BALANCE = 50.00;

    public Client(String name) {
        super(name);
        this.memberId = MEMBER_ID;
        this.balance = new Amount(BALANCE);
    }

    /**
     * Get the value of balance
     *
     * @return the value of balance
     */
    public Amount getBalance() {
        return balance;
    }

    /**
     * Set the value of balance
     *
     * @param balance new value of balance
     */
    public void setBalance(Amount balance) {
        this.balance = balance;
    }

    /**
     * Get the value of memberId
     *
     * @return the value of memberId
     */
    public int getMemberId() {
        return memberId;
    }

    /**
     * Set the value of memberId
     *
     * @param memberId new value of memberId
     */
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    @Override
    public boolean pay(Amount amount) {
        
        double newCash = balance.getValue() - amount.getValue();
        balance.setValue(newCash);
        
        if (newCash >= 0){
            return true;
        }else{
            return false;
        }
        
    }
    @Override
    public String toString(){
        
        return name;  
    }
}
