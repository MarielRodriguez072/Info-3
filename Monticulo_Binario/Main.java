import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        BinaryHeap<Integer> heap = null;
        int opcion=0;
        int tipoHeap = 0;
        do { 
            System.out.println("Menú MONTICULO BINARIO: 0-Salir\n1)Crear monticulo (máximo o mínimo)");
            System.out.print("2)Insertar elemento\n3)Eliminar elemento raíz\n4)Ver raíz(la cima del montículo)\n5)Mostrar elementos del montículo");
            System.out.print("\n6)Tamaño del montículo\n7)Está vacío?\n8)Insertar arreglo\n");
            opcion = teclado.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    System.out.println("Elija un tipo de montículo: 1)Máximo 2) Mínimo");
                    tipoHeap = teclado.nextInt();
                    if (tipoHeap == 1){
                        heap = new BinaryHeap<>(true);
                    }else if(tipoHeap == 2){
                        heap = new BinaryHeap<>(false);
                    }else{
                        System.out.println("Opción no válida. Se creará un montículo máximo por defecto.");
                        heap = new BinaryHeap<>(true);
                    }
                    System.out.println("\n");
                    break;

                case 2:
                    System.out.print("Ingrese el elemento a insertar: ");
                    int elemento = teclado.nextInt();
                    if (heap != null) {
                        heap.insert(elemento);
                    } else {
                        System.out.println("Primero debe crear un montículo.");
                    }
                    System.out.println("\n");
                    break;

                case 3:
                    heap.removeRoot();
                    heap.mostrarMonticulo();
                    System.out.println("\n");
                    break;

                case 4:
                    System.out.println("La raíz del montículo es: " + heap.cresta());
                    System.out.println("\n");
                    break;

                case 5: 
                    heap.mostrarMonticulo();
                    System.out.println("\n");
                    break;

                case 6:     
                    System.out.println("El tamaño del montículo es: " + heap.size());
                    System.out.println("\n");
                    break;

                case 7:
                    System.out.println("¿El montículo está vacío? " + heap.isEmpty());
                 System.out.println("\n");
                    break;
                
                case 8:
                    Integer[] arr = {9, 4, 7, 1, 6, 2};
                    heap.insertArray(arr);
                    heap.mostrarMonticulo();
                    System.out.println("\n");
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        } while (opcion!=0);
    }
}
