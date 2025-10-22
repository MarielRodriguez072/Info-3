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
            System.out.print("\n6)Ver con jerarquía\n7)Tamaño del montículo\n8)Está vacío? ");
            opcion = teclado.nextInt();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    System.out.println("Elija un tipo de montículo: 1) Máximo 2) Mínimo");
                    tipoHeap = teclado.nextInt();
                    if (tipoHeap == 1){
                        heap = new BinaryHeap<>(true);
                    }else if(tipoHeap == 2){
                        heap = new BinaryHeap<>(false);
                    }else{
                        System.out.println("Opción no válida. Se creará un montículo máximo por defecto.");
                        heap = new BinaryHeap<>(true);
                    }

                    break;

                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5: 
                    break;
                case 6:     
                    break;
                case 7:
                    break;
                case 8: 
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcion!=0);
    }
}
