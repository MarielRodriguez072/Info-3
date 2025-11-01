public interface MapaPacientes {
    void put(String dni, Paciente paciente);
    Paciente get(String dni);
    boolean remove(String dni);
    boolean containsKey(String dni);
    int size();
    Iterable<String> keys();
}
