
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

        do { 
            System.out.println("MENU de PILA:");
            System.out.println("0)Para salir\n1)Agregar un elemento a la pila");
            System.out.println("2)Eliminar un elemento de la pila\n3)Consultar valor superior\n4)Estado de la pila");
            System.out.println("5)MENU de COLA:");
            op_menu = teclado.nextInt();

            switch (op_menu) {
                case 0:
                    System.out.println("Adios");
                    break;
                    
                case 1:
                    teclado.nextLine();
                    System.out.println("Ingrese un valor a almacenar en la pila");
                    dato = teclado.nextInt();
                    pila.push(dato);
                    
                    break;
                case 2:
                    System.out.println( pila.pop()+ ":nuevo valor superior");
                    break;

                case 3:
                    System.out.println("Valor superior de la pila: "+ pila.top());
                    break;

                case 4: 
                    System.out.println("¿Está vacía la pila? " + pila.isEmpty());
                    System.out.println("Está llena la pila: + " + pila.isFull());
                    break;

                case 5:
                    //colaMenu(); 
                    break;
                
                default:
                    System.out.println("Ingrese otra opcion");
                    break;
            }
        } while (op_menu != 0);

        /*private static void colaMenu(){

            System.out.println("0 para salir");
            System.out.println("1)Agregar elemento\n2)Despachar elemento del frente");
            System.out.println("3)Ver primer elemento\n4)");
            op_menu2 = teclado.nextInt();

        }
        */
    }

   
}
