public class Binario {
    /*Para convertir un numero a binario se debe dividir dicho numero por 2 y 
     * "guardar el resto"- Los resto son los que forman el numero en binario hasta que ya no pueda seguirse dividiendo
     * por la base
     */
    public static String convBinaria (int n){
        //primero condición de corte
        if (n < 2) {
            return Integer.toString(n);

        }else{ //caso recursivo
            return convBinaria(n/2) + (n%2); 
        }
    }
}
