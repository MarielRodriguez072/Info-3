
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        int op_menu, op_menu2;
        int cantidad; //pila
        int dato;
        int size; //cola

        //Definicion cantidad de elementos de la pila
        teclado.nextLine();
        System.out.println("Ingrese la cantidad de elementos que tendrá la pila");
        cantidad = teclado.nextInt();
        /****************************************** */

        //Definicion tamaño de la cola
              teclado.nextLine();
            System.out.println("Ingrese el tamaño maximo de la cola");
            size = teclado.nextInt();

        /*************************************** */

        //Declaración de la pila y cola
        PilaArreglo <Integer> pila = new PilaArreglo(cantidad);
        ColaArreglo <Integer> cola = new ColaArreglo(cantidad);

        
    }

   
}
