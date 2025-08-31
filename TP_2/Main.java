import java.util.Arrays;
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

        //-------------MCD: prueba con nums hardcodeado--------------
        int num1;
        int num2;
        int resultado;
        //---------------------------------
        int binario; 
        //----------------fibo-------------
        int cantidad;
        long [] memo;
        //---------------8-------------
        int [] arreglo = {3, 5, 7, 9, 1, 8};
        boolean x;
        //
        do{
            System.out.println("***********MENU: Para salir ingrese 0***********\n");
            System.out.println("1)Contador de cifras\n2)Invertir cadena");
            System.out.println("3)Suma de elementos de un arreglo + promedio\n4)Maximo comun divisor");
            System.out.println("5)Conversion binaria\n6)Palindromo");
            System.out.println("7)Fibonacci optimizado\n8)Buscar en un arreglo");

            opcion = teclado.nextInt(); 
            
            
            switch (opcion) {
                case 0:
                    System.out.println("Adios");

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
                    System.out.println("MCD");
                    System.out.println("Ingrese el 1er numero");
                    teclado.nextLine();

                    num1= teclado.nextInt();
                    System.out.println("Ingrese el  2do numero");
                    num2=teclado.nextInt(); 

                    resultado = Mcd.buscar(num1, num2); 
                    System.out.println("El MCD entre los numeros " + num1 + " y " + num2 + " es: "+ resultado);

                    System.out.println("\n");

                    break;

                case 5:
                    System.out.println("Ingrese un numero entero:");
                    teclado.nextLine();
                   
                    binario = teclado.nextInt();

                    System.out.println("El numero: " + binario+ "convertido a binario es: " + Binario.convBinaria(binario));
                    System.out.println("\n");

                    break;
                    
                case 6:
                    System.out.println("Palindromo:\n");
                    teclado.nextLine(); 

                    System.out.println("Ingrese una palabra para determinar si es palíndroma:\n");
                    cadena = teclado.nextLine();
                    System.out.println("La palabra, " + cadena +": " + Palindromo.verifico(cadena)+ "\n");

                    System.out.println("\n");

                    break; 
                
                case 7:
                    System.out.println("Fibonacci");
                    teclado.nextLine();

                    System.out.println("Ingrese la cantidad de elementos que quiere en la serie");
                    cantidad = teclado.nextInt();
                    memo = new long[cantidad + 1];

                    Arrays.fill(memo, -1);
                    System.out.println("Serie de Fibonacci de: "+ cantidad+ " es =" + Fibonacci.fib(cantidad,memo));
                    break;
                
                case 8:
                    System.out.println("Buscar en un arreglo");
                    teclado.nextLine();

                    System.out.println("Ingrese el numero a buscar");
                    cantidad = teclado.nextInt();
                    x = Buscar.look(arreglo, cantidad);

                    System.out.println("¿El numero "+ cantidad + " está en el arreglo? " + x);

                    break;
                default:
                    System.out.println("Ingrese otra opcion:");

                    break;
            }

        }while(opcion !=0);
    
    }

}
