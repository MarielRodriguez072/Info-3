

//esqueleto de una tarea
public class Tarea {
    private String descripcion; 
    private boolean estado;
    
    public Tarea (String descripcion, boolean estado){ //constructor de la clase
        this.descripcion = descripcion; 
        this.estado = estado; 
    }
    //getter me trae el atributo
    //el setter me setea el valor

    public String getDescripcion(){
        return this.descripcion; 
    }
    public boolean getEstado(){
        return this.estado; 
    }

    //la estructura de toda funcion en java es alcance + tipo de dato + nombre
    public void setDescripcion(String descripcion){

        this.descripcion = descripcion; 
    }
    
    public void setEstado(boolean estado){

        this.estado = estado; 

    }
}
