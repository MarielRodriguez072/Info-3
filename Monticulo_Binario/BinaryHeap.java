//Monticulo binario estructura
import java.util.ArrayList;
import java.util.List;

public class BinaryHeap<T extends Comparable<T>> {/*Permite que el heap funcione con cualquier tipo de dato 
                                                    comparable (números, texto, objetos personalizados).*/
    private List<T> heap;
    private boolean isMaxHeap;//heap máximo o mínimo

    public BinaryHeap(boolean isMaxHeap) {//constructor
        this.heap = new ArrayList<>();
        this.isMaxHeap = isMaxHeap; //indica si es un heap máximo o mínimo
    }
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    public void insert(T value) {
        if (value == null) {
            System.out.println("No se puede insertar un valor nulo en el montículo.");
        }else {
            heap.add(value); //agrega el metodo al final 
            heapifyUp(heap.size() - 1); //lo acomoda hacia arriba
        }
    }

    public T removeRoot() {
        if (isEmpty()) {
            System.out.println("El montículo está vacío");
            return null;
        }
        T root = heap.get(0); //guarda la raiz
        T lastElement = heap.remove(heap.size() - 1); //elimina el ultimo elemento
        if (!isEmpty()) {
            heap.set(0, lastElement); //coloca el ultimo elemento en la raiz
            heapifyDown(0); //reorganiza el montículo
        }
        return root; //retorna la raiz eliminada
    }   

    public T cresta() {
        if (isEmpty()) {
            System.out.println("El montículo está vacío.");
            return null;
        }
        return heap.get(0); //retorna la raiz
    }

    public int size() {
        return heap.size(); //retorna el tamaño del montículo
    }
    public List<T> getElementos() {
        return new ArrayList<>(heap); //retorna una copia de los elementos del montículo
    }
    //Mostar montículo
    public void mostrarMonticulo(){
        if(isEmpty()){
            System.out.println("El montículo está vacío.");
            return;

        } else{
            System.out.print("Elementos del montículo:\n");
            /*for (T x : heap) {
                System.out.print( x + ", ");
                
            }*/
            mostrarMonticulo(0, "", true);
        }

    }
    //Sobrecarga de métodos---Solucion profe
    private void mostrarMonticulo(int index, String prefix, boolean isTail) {
        if (index < heap.size()) {
            System.out.println(prefix + (isTail ? "└── " : "├── ") + heap.get(index));
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            if (leftChild < heap.size()) {
                mostrarMonticulo(leftChild, prefix + (isTail ? "    " : "│   "), rightChild >= heap.size());
            }
            if (rightChild < heap.size()) {
                mostrarMonticulo(rightChild, prefix + (isTail ? "    " : "│   "), true);
            }
        }
    }
    //*************************************
    private boolean comparar(T padre, T hijo){
        int comp = hijo.compareTo(padre);
        if (isMaxHeap) {
            return comp < 0; //heap máximo
        } else {
            return comp > 0; //heap mínimo
        }
    }

    private void intercambiar(int i, int j){
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    private void heapifyUp(int index){
        if (index == 0) return; //si es la raiz
        int indexPadre = (index - 1) / 2; //calcula el indice del padre
        if(comparar(heap.get(index),heap.get(indexPadre))) {
            intercambiar(index,indexPadre);
            heapifyUp(indexPadre); //llamada recursiva
        }
    }

    private void heapifyDown(int index) {
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;
        int target = index;

        if (leftChild < heap.size() && comparar(heap.get(leftChild), heap.get(target))) {
            target = leftChild;
        }
        if (rightChild < heap.size() && comparar(heap.get(rightChild), heap.get(target))) {
            target = rightChild;
        }
        if (target != index) {
            intercambiar(index, target);
            heapifyDown(target);
        }
    }

}  