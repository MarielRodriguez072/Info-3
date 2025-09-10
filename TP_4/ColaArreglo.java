
public class ColaArreglo <T> {
    
    private int front = 0;
    private int rear = -1;
    private int maxSize;
    private int count = 0;
    private T[] queueArray;

    
    public ColaArreglo (int size) {
       
        this.maxSize = size;
        this.queueArray = (T[]) new Object[maxSize];
    }

    //valor para agregar un elemento a la cola
    public void enqueue(T value) {
        if (isFull()) {
            throw new IllegalStateException("La cola está llena");
        }
        rear = (rear + 1) % maxSize;
        queueArray[rear] = value;
        count++;
    }

    //Metodo para desencolar un elemento
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T value = queueArray[front];
        front = (front + 1) % maxSize;
        count--;
        return value;
    }

    //Consultar el primer elemento 
    public T front() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return queueArray[front];
    }

   //Cosultar estados de la cola
    public boolean isEmpty() {
        return count == 0;
    }
    public boolean isFull() {
        return count == maxSize;
    }
}
