public class Contar {
    public static int contador (int n){
        if(n<10){
            return 1;
        }else{
            return 1 + contador(n/10); 
        }
        
    }
    /*public static void main(String[] args) {
        int prueba = 123456; 
        System.out.println("Cifras totales: " + contador(prueba));

        funciona ver por que no funnicona cuanod saco el main de esta parte
    }*/
}