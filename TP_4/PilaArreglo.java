
public class PilaArreglo {
    private int[] pila;   // Arreglo que almacena los datos
    private int base = -1;  // indica pila vacía así el primer elemento cargado ocupa la posicion 0  
    private int capacidad; 

    // Metodo constructor
    public PilaArreglo(int capacidad) {
        this.capacidad = capacidad;
        pila = new int[capacidad];
        
    }

    public void push(int dato){ //push para meter un nuevo dato en la pila
        if (isFull()) {
            System.out.println("La pila está llena");
        }else{
            pila[++base]=dato; 
        }
    }

    public int pop(){   //metodo para eliminar el elemento superior de la pila
        if (isEmpty()) {
            System.out.println("La pila está vacía");
            return -1;
        }else{
            return pila[--base];
        }
    }

    public int top(){   //metodo para consultar el elemento superior de la pila
        if(isEmpty()){
            System.out.println("La pila está vacía");
        }else{
            return pila[base];
        }
    }

    //metodos para controlar el contenido de la pila
    public boolean isEmpty(){
        return base == -1;
    }
    public boolean isFull(){
        return base == capacidad -1;
    }
}