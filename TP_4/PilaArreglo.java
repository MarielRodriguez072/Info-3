


public class PilaArreglo <T>{
    T[] stackArray;  // Arreglo generico para almacenar cualquier tipo de elemento en la pila
    private int base = -1;  // indica pila vacía así el primer elemento cargado ocupa la posicion 0  
    private int capacidad; 

    // Metodo constructor
    public PilaArreglo(int capacidad) {
        this.capacidad = capacidad;
       this.stackArray = (T[]) new Object[capacidad];
    }

    public void push( T dato){ //push para meter un nuevo dato en la pila
        if (isFull()) {
            System.out.println("La pila está llena");
        }else{
            stackArray[++base] = dato; 
        }
    }

    public T pop(){   //metodo para eliminar el elemento superior de la pila
        if (isEmpty()) {
            System.out.println("La pila está vacía");
            return null; //podria arrojar una exception, prefiero retornar null
        }else{
            System.out.println("Valor a extraer: "+ stackArray[base]);
            return stackArray[--base];
        }
    }

    public T top(){   //metodo para consultar el elemento superior de la pila
        if(isEmpty()){
            System.out.println("La pila está vacía");
            return null;
        }else{
            return stackArray[base];
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