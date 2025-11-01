import java.time.LocalDateTime;

public class Recordatorio implements Comparable<Recordatorio> {
    private final String id;
    private LocalDateTime fecha;
    private final String dniPaciente;
    private final String mensaje;

    public Recordatorio(String id, LocalDateTime fecha, String dniPaciente, String mensaje) {
        this.id = id;
        this.fecha = fecha;
        this.dniPaciente = dniPaciente;
        this.mensaje = mensaje;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDniPaciente() {
        return dniPaciente;
    }

    public String getMensaje() {
        return mensaje;
    }

    @Override
    public int compareTo(Recordatorio otro) {
        int cmp = fecha.compareTo(otro.fecha);
        if (cmp != 0) {
            return cmp;
        }
        return id.compareTo(otro.id);
    }
}
