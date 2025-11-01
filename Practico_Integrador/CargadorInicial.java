import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CargadorInicial {

    private final List<String> errores;

    public CargadorInicial() {
        this.errores = new ArrayList<>();
    }

    public SistemaClinica cargar() {
        MapaPacientesTabla pacientes = new MapaPacientesTabla();
        TablaHash<String, Medico> medicos = new TablaHash<>();
        TablaHash<String, Turno> turnosPorId = new TablaHash<>();
        TablaHash<String, AgendaConHistorialImpl> agendas = new TablaHash<>();

        cargarPacientes(DatosDummy.pacientesCsv(), pacientes);
        cargarMedicos(DatosDummy.medicosCsv(), medicos, agendas);
        cargarTurnos(DatosDummy.turnosCsv(), pacientes, medicos, agendas, turnosPorId);

        return new SistemaClinica(pacientes, medicos, agendas, turnosPorId, errores);
    }

    private void cargarPacientes(List<String> lineas, MapaPacientesTabla pacientes) {
        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] partes = linea.split(",");
            if (partes.length < 2) {
                errores.add("pacientes.csv linea " + (i + 1) + ": formato incorrecto");
                continue;
            }
            String dni = partes[0].trim();
            String nombre = partes[1].trim();
            pacientes.put(dni, new Paciente(dni, nombre));
        }
    }

    private void cargarMedicos(List<String> lineas, TablaHash<String, Medico> medicos, TablaHash<String, AgendaConHistorialImpl> agendas) {
        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] partes = linea.split(",");
            if (partes.length < 3) {
                errores.add("medicos.csv linea " + (i + 1) + ": formato incorrecto");
                continue;
            }
            String matricula = partes[0].trim();
            String nombre = partes[1].trim();
            String especialidad = partes[2].trim();
            Medico medico = new Medico(matricula, nombre, especialidad);
            medicos.put(matricula, medico);
            agendas.put(matricula, new AgendaConHistorialImpl());
        }
    }

    private void cargarTurnos(List<String> lineas, MapaPacientesTabla pacientes, TablaHash<String, Medico> medicos, TablaHash<String, AgendaConHistorialImpl> agendas, TablaHash<String, Turno> turnosPorId) {
        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] partes = linea.split(",");
            if (partes.length < 6) {
                errores.add("turnos.csv linea " + (i + 1) + ": formato incorrecto");
                continue;
            }
            String id = partes[0].trim();
            String dni = partes[1].trim();
            String matricula = partes[2].trim();
            String fechaStr = partes[3].trim();
            String duracionStr = partes[4].trim();
            String motivo = partes[5].trim();

            if (!pacientes.containsKey(dni)) {
                errores.add("turnos.csv linea " + (i + 1) + ": paciente inexistente " + dni);
                continue;
            }
            if (!medicos.containsKey(matricula)) {
                errores.add("turnos.csv linea " + (i + 1) + ": medico inexistente " + matricula);
                continue;
            }
            if (turnosPorId.containsKey(id)) {
                errores.add("turnos.csv linea " + (i + 1) + ": turno duplicado " + id);
                continue;
            }
            int duracion;
            try {
                duracion = Integer.parseInt(duracionStr);
            } catch (NumberFormatException e) {
                errores.add("turnos.csv linea " + (i + 1) + ": duracion invalida");
                continue;
            }
            if (duracion <= 0) {
                errores.add("turnos.csv linea " + (i + 1) + ": duracion no positiva");
                continue;
            }
            LocalDateTime fecha;
            try {
                fecha = LocalDateTime.parse(fechaStr);
            } catch (Exception e) {
                errores.add("turnos.csv linea " + (i + 1) + ": fecha invalida");
                continue;
            }
            if (fecha.isBefore(LocalDateTime.now())) {
                errores.add("turnos.csv linea " + (i + 1) + ": fecha pasada");
                continue;
            }
            Turno turno = new Turno(id, dni, matricula, fecha, duracion, motivo);
            AgendaConHistorialImpl agenda = agendas.get(matricula);
            if (!agenda.agendar(turno)) {
                errores.add("turnos.csv linea " + (i + 1) + ": solapamiento");
                continue;
            }
            turnosPorId.put(id, turno);
        }
    }
}
