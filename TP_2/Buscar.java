public class Buscar {

    public static boolean look(int[] arr, int n) {

        return buscarRecursivo(arr, n, 0);
    }

    // Método privado recursivo que sí usa el índice
    private static boolean buscarRecursivo(int[] arr, int n, int indice) {
        // Caso base: llegamos al final sin encontrar
        if (indice >= arr.length) {
            return false;
        }
        // Caso base: encontramos el número
        if (arr[indice] == n) {
            return true;
        }
        // Paso recursivo: probar en la siguiente posición
        return buscarRecursivo(arr, n, indice + 1);
    }
}
