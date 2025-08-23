
import java.util.Scanner;

/*Contexto:
 Imagina que trabajas en una pequeña startup y te han pedido desarrollar un sistema simple en Java para gestionar tareas personales. El sistema debe permitir:
Agregar tareas con una descripción y un estado (“pendiente” o “completada”).
Listar todas las tareas.
Marcar una tarea como completada.
Eliminar tareas completadas.
Requisitos Técnicos:
Debe usarse una clase Tarea con atributos descripcion y estado.
Una clase GestorTareas que almacene las tareas en un ArrayList<Tarea> y tenga métodos para cada funcionalidad.
Un main que muestre un menú en consola para que el usuario pueda elegir qué hacer.
Usar bucles (while, for) y condicionales (if, switch) para controlar el flujo.
Validar que no se ingresen descripciones vacías.
Extra (para puntos adicionales):
Permitir que el usuario guarde las tareas en un archivo de texto y las cargue al iniciar el programa.
*/

public class Main{

    public static void main(String[] args) {
        GestorTareas gestor = new GestorTareas(); 
        Scanner teclado = new Scanner(System.in); 

        /*  Testeo
        Tarea nuevTarea = new Tarea("test", false);
        Tarea nuevTarea2 = new Tarea("test2", false);
        Tarea nuevTarea3 = new Tarea("test3", false);
        Tarea nuevTarea4 = new Tarea("test4", false);

        /*gestor.agregarTarea(nuevTarea);
        gestor.agregarTarea(nuevTarea2);
        gestor.agregarTarea(nuevTarea3);
        gestor.agregarTarea(nuevTarea4);
        gestor.listarTareas();
        gestor.eliminarTarea(2);
        gestor.listarTareas();*/
        int opcion;
        do { 
            System.out.println("MENU:\n1)Agregar una nueva tarea");
            System.out.println("2)Listar las tareas");
            System.out.println("3)Eliminar una tarea");
            System.out.println("0)Salir del menu");

            opcion = teclado.nextInt();

         switch (opcion) {
            
            case 0:
            System.out.println("Adios");
            break;

            case 1: 
            teclado.nextLine();
            System.out.println("Agregar tarea");
            

            System.out.println("Ingrese descripcion de la tarea: ");
            
            String nombre;
            nombre = teclado.nextLine();

            System.out.println("Ingrese el estado de la tarea(debe ser ture-terminado- o un false -pendiente-):");
            boolean estado;
            estado = teclado.nextBoolean();
            
            Tarea nuevaTarea = new Tarea(nombre, estado);
            gestor.agregarTarea(nuevaTarea);

            
            break;

            case 2:
            System.out.println("Listar las tareas");
            gestor.listarTareas();
            break; 
            
            case 3:
            System.out.println("Eliminar una tarea");

            System.out.println("Ingrese el índice de la tarea a eliminar");
            int indice = teclado.nextInt();

            gestor.eliminarTarea(indice);

            break;

            default:
            System.out.println("Ingrese otra opcion");
            break;

         }   
        } while (opcion != 0);

    }
}