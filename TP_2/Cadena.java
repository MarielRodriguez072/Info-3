/*Ejercicio 2 – Invertir una cadena
Escriba un método recursivo que reciba una cadena y devuelva la misma cadena invertida.Ejemplo: "recursivo" → "ovisrucer". */

public class Cadena {
    private String texto; 

    public void setTexto(String texto){

        this.texto = texto; 
    }
    
    public String getTexto(){
        return this.texto; 
    }

    public static String inversor(String c){
        if (c == null || c.length()<=1) {
            return c; 
        }
           //El método charAt(int índice) de la clase String
           // devuelve el carácter que está en la posición indicada dentro de la cadena.
            return inversor(c.substring(1)) + c.charAt(0);
       
    }
}
