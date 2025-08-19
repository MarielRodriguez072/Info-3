
import java.util.ArrayList;

//va a tener la lista de tareas y los métodos para Listar todas las tareas.Marcar una tarea como completada.Eliminar tareas completadas.

public class GestorTareas {
    private ArrayList <Tarea> listaTareas;

    public GestorTareas(){  //generara un nuevo arraylist vacio y a medida que si vayan creando las tareas se agregan al gestor
        this.listaTareas = new ArrayList<>();
    }

    public void setListaTareas(ArrayList<Tarea>listaTarea){
        this.listaTareas = listaTarea; 
    }

    public ArrayList<Tarea> getListaTarea(){
        return this.listaTareas;
    }

                        //ahora los métodos puntuales para el gestor
    public void agregarTarea(Tarea tarea){
        this.listaTareas.add(tarea);
    }

    public void listarTareas(){
        for(int i = 0; i < listaTareas.size(); i++) {
            System.out.println(listaTareas.get(i).getDescripcion());
        }
    }

    public void eliminarTarea(int numeroTarea){
        this.listaTareas.remove(numeroTarea);


    }
    
    
}
