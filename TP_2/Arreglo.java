/*Ejercicio 3 – Suma de elementos de un arreglo
Implemente una función recursiva que calcule la suma de todos los elementos de un arreglo
de enteros.
Ejemplo: [2, 4, 6, 8] → 20.
Extienda la solución para que además devuelva el promedio usando únicamente
recursión. */
public class Arreglo {
    public static int sumar (int[]arreglo, int elementos){

        if (elementos == 0) {

            return arreglo[0];

        }else{
            return arreglo[elementos] + sumar(arreglo, elementos-1); 
        }
    }

    public static double promedio (int[] arreglo, int elementos){

        int total = sumar (arreglo, elementos - 1);
        return (double) total / arreglo.length; 
    }
}
