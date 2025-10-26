public class Paciente implements Comparable<Paciente> {
    private String nombre;
    private int prioridad; // 1 = alta, 2 = media, 3 = baja
    
    BinaryHeap<Paciente> colaPrioridad = new BinaryHeap<>(false);//tiene que ser de tipo mínimo

    public Paciente(String nombre, int prioridad) {
        this.nombre = nombre;
        this.prioridad = prioridad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrioridad() {
        return prioridad;
    }

    // Método necesario para usar en el Montículo Binario
    @Override
    public int compareTo(Paciente otro) {
        return Integer.compare(this.prioridad, otro.prioridad);
        // Si this < otro → se atiende antes
    }

    public void ingresar(Paciente p) {
        colaPrioridad.insert(p);
    }

    public Paciente atender() {
        return colaPrioridad.removeRoot();
    }

    @Override
    public String toString() {
        return nombre + " (Prioridad: " + prioridad + ")";
    }
}
