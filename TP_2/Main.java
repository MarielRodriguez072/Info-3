import java.util.Scanner;

public class Main{
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        
        //Declaracion de variables.
        int opcion;
        String numero;
        String cadena; 
        //-------------------Suma + promedio --------------------------
        int[] elementos = {2, 2, 2, 2}; //suma=8, promedio= 2
        int total;
        double prom;
        //------------------------------------
         

        do{
            System.out.println("MENU:Para salir ingrese 0\n1)Contador de cifras");
            System.out.println("\n2)Invertir cadena\n");
            System.out.println("3)Suma de elementos de un arreglo + promedio\n4)");

            opcion = teclado.nextInt();  
            
            switch (opcion) {
                case 0:
                    System.out.println("adios");

                    break;

                case 1:
                    System.out.println("Ingrese un numero entero:");
                    teclado.nextLine();
                   
                    numero = teclado.nextLine();

                    System.out.println("Cifras totales: " + Contar.contador(Integer.parseInt(numero)));
                    System.out.println("\n");

                    break;

                case 2: 
                    System.out.println("Ingrese una cadena a invertir:");
                    teclado.nextLine(); 

                    cadena = teclado.nextLine();

                    System.out.println("Cadena original: " + cadena + "\n");
                    System.out.println("Cadena invertida: " + Cadena.inversor(cadena));

                    System.out.println("\n");

                    break; 

                case 3:
                    System.out.println("Suma + promedio");
                    total= Arreglo.sumar(elementos,elementos.length - 1 );
                    System.out.println("Total de la suma: " + total);

                    prom = Arreglo.promedio(elementos,elementos.length);
                    System.out.println("Promedio de los elementos: " + prom);

                    System.out.println("\n");
                    break; 

                case 4: 

                    break;
                    
                default:
                    System.out.println("ingrese otra opcion");

                    break;
            }

        }while(opcion !=0);
    
    }

}
