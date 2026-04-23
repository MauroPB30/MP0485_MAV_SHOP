package model;

public class Product {

    private int id;
    public String name;
    private Amount publicPrice;
    private Amount wholesalerPrice;
    private boolean available;
    private int stock;
    private static int totalProducts;

    static double EXPIRATION_RATE = 0.60;

    public Product(String name, double wholesalerPrice, boolean available, int stock) {
        super();
        this.id = totalProducts + 1;
        this.name = name;
        this.publicPrice = new Amount(wholesalerPrice * 2);
        this.wholesalerPrice = new Amount(wholesalerPrice);
        this.available = available;
        this.stock = stock;
        totalProducts++;
    }
// Se agrega un toString para que se printen los datos y no la ubicacion de los datos

    public Product() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Product(String name, Amount wholesalesPrice, int stock) {

    this.id = totalProducts + 1;
    this.name = name;
    this.wholesalerPrice = wholesalesPrice;

    this.publicPrice = new Amount(wholesalesPrice.getValue() * 2);

    this.available = true;
    this.stock = stock;

    totalProducts++;
}

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", publicPrice=" + publicPrice + ", wholesalerPrice=" + wholesalerPrice + ", available=" + available + ", stock=" + stock + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Amount getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(Amount publicPrice) {
        this.publicPrice = publicPrice;
    }

    public Amount getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(Amount wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public static int getTotalProducts() {
        return totalProducts;
    }

    public static void setTotalProducts(int totalProducts) {
        Product.totalProducts = totalProducts;
    }

    public void expire() {
        this.publicPrice.setValue(this.publicPrice.getValue() * EXPIRATION_RATE);
    }
}
