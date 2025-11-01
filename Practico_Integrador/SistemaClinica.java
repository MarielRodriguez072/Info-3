import java.util.ArrayList;
import java.util.List;

public class SistemaClinica {

    private final MapaPacientesTabla pacientes;
    private final TablaHash<String, Medico> medicos;
    private final TablaHash<String, AgendaConHistorialImpl> agendas;
    private final TablaHash<String, Turno> turnosPorId;
    private final List<String> errores;

    public SistemaClinica(MapaPacientesTabla pacientes, TablaHash<String, Medico> medicos, TablaHash<String, AgendaConHistorialImpl> agendas, TablaHash<String, Turno> turnosPorId, List<String> errores) {
        this.pacientes = pacientes;
        this.medicos = medicos;
        this.agendas = agendas;
        this.turnosPorId = turnosPorId;
        this.errores = new ArrayList<>(errores);
    }

    public MapaPacientes pacientes() {
        return pacientes;
    }

    public int cantidadPacientes() {
        return pacientes.size();
    }

    public Iterable<Paciente> pacientesLista() {
        if (pacientes instanceof MapaPacientesTabla) {
            return ((MapaPacientesTabla) pacientes).values();
        }
        List<Paciente> lista = new ArrayList<>();
        for (String dni : pacientes.keys()) {
            lista.add(pacientes.get(dni));
        }
        return lista;
    }

    public Medico medico(String matricula) {
        return medicos.get(matricula);
    }

    public Iterable<Medico> medicosLista() {
        return medicos.values();
    }

    public int cantidadMedicos() {
        return medicos.size();
    }

    public AgendaMedico agenda(String matricula) {
        return agendas.get(matricula);
    }

    public AgendaConHistorial agendaConHistorial(String matricula) {
        return agendas.get(matricula);
    }

    public Turno turno(String id) {
        return turnosPorId.get(id);
    }

    public Iterable<Turno> turnos() {
        return turnosPorId.values();
    }

    public int cantidadTurnos() {
        return turnosPorId.size();
    }

    public Iterable<String> medicosIds() {
        return medicos.keys();
    }

    public List<String> errores() {
        return new ArrayList<>(errores);
    }
}
