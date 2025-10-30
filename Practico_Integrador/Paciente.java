public class Paciente {
    private String nombre, DNI;

    public Paciente(String DNI, String nombre) {
        this.DNI = DNI;
        this.nombre = nombre;
    }

    public String getDni() { 
        return DNI; 
    }

    public String getNombre() { 
        return nombre;
 }

    @Override
    public String toString() {
        return "Paciente{" + "dni='" + DNI + '\'' + ", nombre='" + nombre + '\'' + '}';
    }
}
