import java.time.LocalDateTime;
import java.util.Optional;

public interface AgendaMedico {
    boolean agendar(Turno turno);
    boolean cancelar(String idTurno);
    Optional<Turno> siguiente(LocalDateTime fecha);
    Optional<LocalDateTime> primerHueco(LocalDateTime desde, int duracionMin);
    int size();
}
