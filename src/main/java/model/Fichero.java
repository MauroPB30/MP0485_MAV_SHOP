package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.Product;

public class Fichero {

    public static void LeerInventario(ArrayList<Product> inventory) {

        String linea;

        String nombre = null;

        Amount wholesalerprice = new Amount(0.0);

        int stock = 0;

        FileReader inputStream = null;
        BufferedReader in = null;

        try {
            inputStream = new FileReader("inputInventory.txt");
            in = new BufferedReader(inputStream);

            while ((linea = in.readLine()) != null) {

                String datos[] = linea.split(";");

                for (int i = 0; i < datos.length; i++) {
                    String finalDatos[] = datos[i].split(":");
                    if (finalDatos[0].equals("Product")) {
                        nombre = finalDatos[1];
                    } else if (finalDatos[0].equals("Wholesaler Price")) {
                        wholesalerprice = new Amount(Double.parseDouble(finalDatos[1]));
                    } else if (finalDatos[0].equals("Stock")) {
                        stock = Integer.parseInt(finalDatos[1]);

                    }

                }
                Product p = new Product(nombre, wholesalerprice, stock);
                inventory.add(p);
            }

        } catch (java.io.IOException ex) {
            System.out.println(ex);
            System.out.println("No se puede acceder al archivo.");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    System.out.println("No se puede acceder al archivo.");
                }
            }
        }

    }

    public static void EscribirInventario(ArrayList<Sale> sales) {

//         FileWriter outputStreamProducto;
        BufferedWriter out1 = null;

        try {

            String date = java.time.LocalDate.now().toString();
            String fileName = "sales_" + date + ".txt";
//             outputStreamProducto = new FileWriter("sales_yyyy-mm-dd.txt");
//             out1 = new BufferedWriter(outputStreamProducto);
            out1 = new BufferedWriter(new FileWriter(fileName));
            int index = 1;

            for (Sale sale : sales) {

                out1.write(index + " Cliente: " + sale.getClient().getName() + "\n");

                out1.write(index + " Product: ");
                for (Product p : sale.getProducts()) {
                    out1.write((p.getName() + ", " + p.getPublicPrice() + "; "));
                }

                out1.newLine();
                out1.write(index + " Amount: " + sale.getAmount() + "\n");
//                String products = index + ";Products=";

                index++;
//                }

                System.out.println("Ventas exportadas correctamente");
            }
        } catch (java.io.IOException ex) {
            System.out.println(ex);
            System.out.println("Error al escribir ventas.");
        } finally {
            if (out1 != null) {
                try {
                    out1.close();
                } catch (IOException ex) {
                    System.out.println("Error al cerrar fichero.");
                }
            }
        }

    }
}
