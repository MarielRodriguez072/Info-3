

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        int op_menu;

        do { 
            System.out.println("MENU:");
            System.out.println("0)Para salir\n1)Crear una pila");
            System.out.println("2)Invertir una Cadena con Pila");
            op_menu = teclado.nextInt();

            switch (op_menu) {
                case 0:
                    System.out.println("Adios");
                    break;
                case 1:
                    
                    break;
                case 2:
                
                    break;

                case 3:
                    
                    break;

                
                default:
                    System.out.println("Ingrese otra opcion");
                    break;
            }
        } while (op_menu != 0);

        teclado.close();
    }
}
