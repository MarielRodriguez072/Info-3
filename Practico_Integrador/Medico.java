public class Medico {
    private final String matricula;
    private final String nombre;
    private final String especialidad;

    public Medico(String matricula, String nombre, String especialidad) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public String getMatricula() {
         return matricula; 
        }
    public String getNombre() {
         return nombre;
         }
    public String getEspecialidad() { 
        return especialidad; 
    }

    @Override
    public String toString() {
        return "Medico{" + "matricula='" + matricula + '\'' + ", nombre='" + nombre + '\'' + ", especialidad='" + especialidad + '\'' + '}';
    }
}
