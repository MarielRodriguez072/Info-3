import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        int opcion;
        do{
            System.out.println("MENU:\n1)Contador de cifras");
            opcion = teclado.nextInt();  
            
            switch (opcion) {
                case 0:
                    System.out.println("adios");
                    break;

                case 1:
                    System.out.println("Ingrese un numero entero");
                    teclado.nextLine();

                    String numero;
                    numero = teclado.nextLine();

                    System.out.println("Cifras totales: " + Contar.contador(Integer.parseInt(numero)));
                    System.out.println("\n");
                    break;
                default:
                    System.out.println("ingrese otra opcion");
                    break;
            }

        }while(opcion !=0);
    
    }

}
