package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Sale {

    private Client client;

    private ArrayList<Product> products = new ArrayList<>();
    Amount amount;
//
//    public Sale(String client, Product[] products, Amount amount) {
//        super();
//        this.client = client;
//        this.products = products;
//        this.amount = amount;
//    }

    public Sale(Client client,ArrayList<Product> products, Amount amount) {
        this.client = client;
        this.products = products;
        this.amount = amount;
    }

    public Client getClient() {
        return client;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public Amount getAmount() {
        return this.amount;
    }

//    @Override
//    public String toString() {
//        return "Sale [client=" + client + ", products=" + Arrays.toString(products) + ", amount=" + amount + "]";
//    }

    @Override
    public String toString() {
        return "Sale{" + "client=" + client + ", product=" + products + ", amount=" + amount + '}';
    }

}