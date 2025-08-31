/*Segun la IA el uso de memorización mejoraria el uso de la 
recursividad a la hora de calcular la serie
 */

public class Fibonacci {
    public static long fib(int n, long[] memo) {
        if (n <= 1) {
            return n;
        }
        if (memo[n] != -1) { // Si ya está calculado, lo devuelve
            return memo[n];
        }
        // Guarda el resultado en la memoria antes de retornarlo
        memo[n] = fib(n - 1, memo) + fib(n - 2, memo);
        return memo[n];
    }
}
