
public class Palindromo {
    public static boolean  verifico(String t){
        //si bien no es una funcion recursiva está utilizando una funcion que si lo es
        //Aqui compara que la cadena invertida sea igual a la cadena original
         String comp;

            comp = Cadena.inversor(t) ;
            
            return t.equals(comp); 
       
    }
        
    }
