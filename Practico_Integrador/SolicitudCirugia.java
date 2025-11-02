public class SolicitudCirugia {
    private final String id;
    private final String matricula;
    private final int duracionMin;
    private final java.time.LocalDateTime deadline;

    public SolicitudCirugia(String id, String matricula, int duracionMin, java.time.LocalDateTime deadline) {
        this.id = id;
        this.matricula = matricula;
        this.duracionMin = duracionMin;
        this.deadline = deadline;
    }

    public String getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public int getDuracionMin() {
        return duracionMin;
    }

    public java.time.LocalDateTime getDeadline() {
        return deadline;
    }
}
