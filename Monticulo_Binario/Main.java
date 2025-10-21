import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        
        int opcion;
        do { 
            System.out.println("Menu MONTICULO BINARIO: 0-Salir");
            opcion = teclado.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    System.out.println("Opcion 1 seleccionada");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion!=0);
    }
}
