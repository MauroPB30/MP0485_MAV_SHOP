package main;

import Exception.DAO_exception;
import dao.DaolmpJDBC;
import dao.IDao;
import java.util.ArrayList;
import model.Product;
import model.Sale;
import java.util.Scanner;
import model.Amount;
import model.Client;
import model.Employee;
import model.Fichero;

public class Shop {

    private Amount cash = new Amount(100.00);
    private static ArrayList<Product> inventory = new ArrayList<>();
    private ArrayList<Sale> sales = new ArrayList<>();
    private IDao dao;

    final static double TAX_RATE = 1.04;

    // Constructor
    public Shop() {
        this.dao = new DaolmpJDBC();
        inventory = new ArrayList<>();
        sales = new ArrayList<>();
        loadFromDB();
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Shop shop = new Shop();
        shop.initSession();
//        shop.loadInventory(inventory);
        int opcion = 0;
        boolean exit = false;

        do {
            System.out.println("\n===========================");
            System.out.println("Menu principal miTienda.com");
            System.out.println("===========================");
            System.out.println("1) Contar caja");
            System.out.println("2) A�adir producto");
            System.out.println("3) A�adir stock");
            System.out.println("4) Marcar producto proxima caducidad");
            System.out.println("5) Ver inventario");
            System.out.println("6) Venta");
            System.out.println("7) Ver ventas");
            System.out.println("8) Ver total de ventas");
            System.out.println("9) Eliminar producto del inventario");
            System.out.println("10) Salir programa");
            System.out.print("Seleccione una opcion: ");

            opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    shop.showCash();
                    break;
                case 2:
                    shop.addProduct();
                    break;
                case 3:
                    shop.addStock();
                    break;
                case 4:
                    shop.setExpired();
                    break;
                case 5:
                    shop.showInventory();
                    break;
                case 6:
                    shop.sale();
                    break;
                case 7:
                    shop.showSales();
                    break;
                case 8:
                    shop.showTotalSales();
                    break;
                case 9:
                    shop.deleteProduct("");
                    break;
                case 10:
                    exit = true;
                    break;
            }
        } while (!exit);
    }

//    public void loadInventory(ArrayList<Product> inventory) {
//        Fichero.LeerInventario(inventory);
    ////        System.out.println("Productos cargados: " + inventory.size());
//    }
    
    private void loadFromDB() {
        try {
            dao.connect();
            inventory = new ArrayList<>(dao.readAllProducts());
            dao.disconnect();
            System.out.println("Productos cargados desde BD: " + inventory.size());
        } catch (DAO_exception e) {
            System.out.println("Error cargando productos: " + e.getMessage());
        }
    }

    public void showCash() {
        System.out.println("Dinero actual: " + cash);
    }

    public void addProduct() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nombre: ");
        String name = sc.nextLine();

        if (findProduct(name) != null) {
            System.out.println("El producto ya existe.");
            return;
        }

        System.out.print("Precio mayorista: ");
        double wholesalerPrice = sc.nextDouble();
        System.out.print("Stock: ");
        int stock = sc.nextInt();

        inventory.add(new Product(name, wholesalerPrice, true, stock));
        System.out.println("Producto a�adido correctamente");
    }

    public void addStock() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = sc.nextLine();

        Product product = findProduct(name);

        if (product != null) {
            System.out.print("Seleccione la cantidad a a�adir: ");
            int stock = sc.nextInt();
            product.setStock(product.getStock() + stock);
            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());
        } else {
            System.out.println("No se ha encontrado el producto con nombre " + name);
        }
    }

    private void setExpired() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = sc.nextLine();

        Product product = findProduct(name);

        if (product != null) {
            product.expire();
            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getPublicPrice());
        } else {
            System.out.println("El producto no existe");
        }
    }

    public void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventario vac�o.");
            return;
        }
        System.out.println("\nInventario actual:");
        for (Product p : inventory) {
            System.out.println(p.getName() + " | " + p.getWholesalerPrice() + " | " + p.getStock());
        }
    }

    public void sale() {
        // ask for client name
        Scanner sc = new Scanner(System.in);
        ArrayList<Product> products = new ArrayList<>();

        System.out.println("Realizar venta, escribir nombre cliente");
        String clientName = sc.nextLine();

        Client client = new Client(clientName);
        double totalAmount = 0.0;

        while (true) {
            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
            String productName = sc.nextLine();

            if (productName.equals("0")) {
                break;
            }
            Product product = findProduct(productName);

            boolean productAvailable = false;

            if (product != null && product.isAvailable()) {

                products.add(product);
                totalAmount += product.getPublicPrice().getValue();

                product.setStock(product.getStock() - 1);
                // if no more stock, set as not available to sale
                if (product.getStock() == 0) {
                    product.setAvailable(false);
                }
                System.out.println("Producto a\u00f1adido con exito");

            } else {
                System.out.println("Producto no encontrado o sin stock");
            }
        }
        totalAmount *= TAX_RATE;
        Amount totalAmountObj = new Amount(totalAmount);

        boolean payCheck = client.pay(totalAmountObj);

        sales.add(new Sale(client, products, totalAmountObj));

        if (payCheck) {
            cash.setValue(cash.getValue() + totalAmountObj.getValue());
            System.out.println("Venta realizada con exito, total: " + totalAmountObj);
            System.out.println("Saldo cliente: " + client.getBalance());
        } else {
            cash.setValue(cash.getValue() + totalAmountObj.getValue());
            System.out.println("Venta realizada pero cliente con deuda.");
            System.out.println("Cantidad a deber: " + Math.abs(client.getBalance().getValue()) + " euro");
        }

    }

    private void showSales() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Lista de ventas:");
        for (Sale sale : sales) {
            System.out.println(sale.toString());
        }

        System.out.println("Quieres exportar las ventas (Si/No)?");
        String answer = sc.nextLine();
        if (answer.equalsIgnoreCase("Si")) {
            Fichero.EscribirInventario(sales);
        }
    }

    public void showTotalSales() {
        double totalSales = 0.0;
        for (Sale sale : sales) {
            totalSales += sale.getAmount().getValue();
        }
        System.out.println("Total de todas las ventas: " + totalSales);
    }

    // ===================== Versiones program�ticas (para ProductView) =====================
    public boolean addProduct(String name, double price, int stock) {
        if (findProduct(name) != null) {
            return false;
        }
        inventory.add(new Product(name, price, true, stock));
        return true;
    }

    public boolean addStock(String name, int stockToAdd) {
        Product product = findProduct(name);
        if (product != null) {
            product.setStock(product.getStock() + stockToAdd);
            return true;
        }
        return false;
    }

    public boolean deleteProduct(String name) {
        Product product = findProduct(name);
        if (product != null) {
            inventory.remove(product);
            return true;
        }
        return false;
    }

    public Product findProduct(String name) {
        for (Product product : inventory) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    public void initSession() {
        Scanner sc = new Scanner(System.in);
        boolean logged = false;
//        Employee emp = new Employee(0, dao, name);

        do {
            System.out.println("Ingrese el ID de empleado");
            String empId = sc.nextLine();
            sc.nextLine();
            System.out.println("Ingrese la contrase�a del empleado");
            String psw = sc.nextLine();

//            if (Employee.login(empId, psw)) {
//                logged = true;
//                System.out.println("Login correcto");
//            } else {
//                System.out.println("Usuario o contrase�a incorrectos");
//            }
        } while (!logged);
    }

    public Amount getCash() {
        return cash;
    }
    
    public IDao getDao() {
    return dao;
}
}
