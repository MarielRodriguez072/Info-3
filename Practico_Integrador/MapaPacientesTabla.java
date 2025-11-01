import java.util.List;

public class MapaPacientesTabla implements MapaPacientes {

    private final TablaHash<String, Paciente> tabla;

    public MapaPacientesTabla() {
        this.tabla = new TablaHash<>();
    }

    @Override
    public void put(String dni, Paciente paciente) {
        tabla.put(dni, paciente);
    }

    @Override
    public Paciente get(String dni) {
        return tabla.get(dni);
    }

    @Override
    public boolean remove(String dni) {
        return tabla.remove(dni);
    }

    @Override
    public boolean containsKey(String dni) {
        return tabla.containsKey(dni);
    }

    @Override
    public int size() {
        return tabla.size();
    }

    @Override
    public Iterable<String> keys() {
        return tabla.keys();
    }

    public int capacidad() {
        return tabla.capacidad();
    }

    public double factorCarga() {
        return tabla.factorCarga();
    }

    public Iterable<Paciente> values() {
        return tabla.values();
    }

    public List<String> diagnosticoBuckets() {
        return tabla.diagnosticoBuckets();
    }
}
