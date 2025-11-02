import java.util.List;

public interface PlanificadorQuirofano {
    void procesar(SolicitudCirugia solicitud);
    List<String> topKMedicosBloqueados(int k);
}
