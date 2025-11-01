public interface Planner {
    void programar(Recordatorio recordatorio);
    Recordatorio proximo();
    void reprogramar(String id, java.time.LocalDateTime nuevaFecha);
    int size();
}
