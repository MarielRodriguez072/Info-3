import java.time.LocalDateTime;

public class Turno {
    private final String id; 
    private String dniPaciente;
    private String matriculaMedico;
    private LocalDateTime fechaHora;
    private int duracionMin;
    private String motivo;

    public Turno(String id, String dniPaciente, String matriculaMedico,LocalDateTime fechaHora, int duracionMin, String motivo) {
        this.id = id;
        this.dniPaciente = dniPaciente;
        this.matriculaMedico = matriculaMedico;
        this.fechaHora = fechaHora;
        this.duracionMin = duracionMin;
        this.motivo = motivo;
    }

    public String getId() { 
        return id; 
    }
    public String getDniPaciente() { 
        return dniPaciente; 
    }
    public String getMatriculaMedico() { 
        return matriculaMedico;
    }
    public LocalDateTime getFechaHora() { 
        return fechaHora;
    }
    public int getDuracionMin() { 
        return duracionMin; 
    }
    public String getMotivo() { 
        return motivo; 
    }

    public LocalDateTime getFin() {
        return fechaHora.plusMinutes(duracionMin);
    }

    public void actualizarFecha(LocalDateTime nuevaFecha) {
        this.fechaHora = nuevaFecha;
    }

    public void actualizarDuracion(int nuevaDuracion) {
        this.duracionMin = nuevaDuracion;
    }

    public void actualizarMotivo(String nuevoMotivo) {
        this.motivo = nuevoMotivo;
    }

    @Override
    public String toString() {
        return "Turno{" + id + ", paciente: " + dniPaciente +", medico: " + matriculaMedico +", fecha: " + fechaHora + ", duracion: " + duracionMin + " min, motivo: " + motivo + "}";
    }
}
