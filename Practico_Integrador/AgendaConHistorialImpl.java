import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

public class AgendaConHistorialImpl extends AgendaMedicoAVL implements AgendaConHistorial {

    private final Deque<Accion> acciones;
    private final Deque<Accion> rehacer;
    private boolean registrar = true;

    public AgendaConHistorialImpl() {
        this.acciones = new ArrayDeque<>();
        this.rehacer = new ArrayDeque<>();
    }

    @Override
    public boolean agendar(Turno turno) {
        boolean exito = super.agendar(turno);
        if (exito && registrar) {
            acciones.push(Accion.agregar(copiar(turno)));
            rehacer.clear();
        }
        return exito;
    }

    @Override
    public boolean cancelar(String idTurno) {
        Turno original = turnoPorId(idTurno);
        if (original == null) {
            return false;
        }
        Turno respaldo = copiar(original);
        boolean exito = super.cancelar(idTurno);
        if (exito && registrar) {
            acciones.push(Accion.cancelar(respaldo));
            rehacer.clear();
        }
        return exito;
    }

    @Override
    public boolean reprogramar(String idTurno, LocalDateTime nuevaFecha) {
        Turno original = turnoPorId(idTurno);
        if (original == null) {
            return false;
        }
        Turno estadoPrevio = copiar(original);
        super.cancelar(idTurno);
        original.actualizarFecha(nuevaFecha);
        boolean reinsertado = super.agendar(original);
        if (!reinsertado) {
            original.actualizarFecha(estadoPrevio.getFechaHora());
            super.agendar(original);
            return false;
        }
        if (registrar) {
            acciones.push(Accion.reprogramar(estadoPrevio, copiar(original)));
            rehacer.clear();
        }
        return true;
    }

    @Override
    public boolean undo() {
        if (acciones.isEmpty()) {
            return false;
        }
        Accion accion = acciones.pop();
        registrar = false;
        boolean exito = ejecutarInverso(accion);
        registrar = true;
        if (exito) {
            rehacer.push(accion);
        }
        return exito;
    }

    @Override
    public boolean redo() {
        if (rehacer.isEmpty()) {
            return false;
        }
        Accion accion = rehacer.pop();
        registrar = false;
        boolean exito = ejecutarAccion(accion);
        registrar = true;
        if (exito) {
            acciones.push(accion);
        }
        return exito;
    }

    private boolean ejecutarAccion(Accion accion) {
        switch (accion.tipo) {
            case AGENDAR:
                return super.agendar(copiar(accion.despues));
            case CANCELAR:
                return super.cancelar(accion.antes.getId());
            case REPROGRAMAR:
                return aplicarReprogramacion(accion.antes, accion.despues);
            default:
                return false;
        }
    }

    private boolean ejecutarInverso(Accion accion) {
        switch (accion.tipo) {
            case AGENDAR:
                return super.cancelar(accion.despues.getId());
            case CANCELAR:
                return super.agendar(copiar(accion.antes));
            case REPROGRAMAR:
                return aplicarReprogramacion(accion.despues, accion.antes);
            default:
                return false;
        }
    }

    private boolean aplicarReprogramacion(Turno desde, Turno hacia) {
        Turno actual = turnoPorId(desde.getId());
        if (actual == null) {
            return false;
        }
        super.cancelar(actual.getId());
        actual.actualizarFecha(hacia.getFechaHora());
        actual.actualizarDuracion(hacia.getDuracionMin());
        actual.actualizarMotivo(hacia.getMotivo());
        boolean reinsertado = super.agendar(actual);
        if (!reinsertado) {
            actual.actualizarFecha(desde.getFechaHora());
            actual.actualizarDuracion(desde.getDuracionMin());
            actual.actualizarMotivo(desde.getMotivo());
            super.agendar(actual);
        }
        return reinsertado;
    }

    private Turno copiar(Turno turno) {
        return new Turno(turno.getId(), turno.getDniPaciente(), turno.getMatriculaMedico(), turno.getFechaHora(), turno.getDuracionMin(), turno.getMotivo());
    }

    private static class Accion {
        private final Tipo tipo;
        private final Turno antes;
        private final Turno despues;

        private Accion(Tipo tipo, Turno antes, Turno despues) {
            this.tipo = tipo;
            this.antes = antes;
            this.despues = despues;
        }

        private static Accion agregar(Turno turno) {
            return new Accion(Tipo.AGENDAR, null, turno);
        }

        private static Accion cancelar(Turno turno) {
            return new Accion(Tipo.CANCELAR, turno, null);
        }

        private static Accion reprogramar(Turno antes, Turno despues) {
            return new Accion(Tipo.REPROGRAMAR, antes, despues);
        }
    }

    private enum Tipo {
        AGENDAR, CANCELAR, REPROGRAMAR
    }
}
