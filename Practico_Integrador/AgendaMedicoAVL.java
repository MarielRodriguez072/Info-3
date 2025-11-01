import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgendaMedicoAVL implements AgendaMedico {

    private static final LocalTime INICIO_JORNADA = LocalTime.of(9, 0);
    private static final LocalTime FIN_JORNADA = LocalTime.of(18, 0);

    private Nodo raiz;
    private int tamaño;
    private final TablaHash<String, Turno> indicePorId;

    public AgendaMedicoAVL() {
        this.indicePorId = new TablaHash<>();
    }

    @Override
    public boolean agendar(Turno turno) {
        if (turno == null || solapa(raiz, turno)) {
            return false;
        }
        raiz = insertar(raiz, turno);
        indicePorId.put(turno.getId(), turno);
        tamaño++;
        return true;
    }

    @Override
    public boolean cancelar(String idTurno) {
        Turno existente = indicePorId.get(idTurno);
        if (existente == null) {
            return false;
        }
        raiz = eliminar(raiz, existente);
        indicePorId.remove(idTurno);
        tamaño--;
        return true;
    }

    @Override
    public Optional<Turno> siguiente(LocalDateTime fecha) {
        Nodo candidato = siguienteNodo(raiz, fecha);
        if (candidato == null) {
            return Optional.empty();
        }
        return Optional.of(candidato.turno);
    }

    @Override
    public Optional<LocalDateTime> primerHueco(LocalDateTime desde, int duracionMin) {
        if (duracionMin <= 0) {
            return Optional.empty();
        }
        LocalDateTime cursor = ajustarHora(desde);
        while (true) {
            if (!mismoDia(cursor, desde)) {
                desde = cursor;
            }
            LocalDateTime finDia = finJornada(cursor.toLocalDate());
            if (cursor.plusMinutes(duracionMin).isAfter(finDia)) {
                cursor = inicioProximoDia(cursor.toLocalDate());
                continue;
            }
            Turno bloqueoAnterior = turnoAnterior(cursor);
            if (bloqueoAnterior != null && !bloqueoAnterior.getFin().isBefore(cursor)) {
                cursor = ajustarHora(bloqueoAnterior.getFin());
                continue;
            }
            Optional<Turno> siguiente = siguiente(cursor);
            if (siguiente.isPresent() && cursor.plusMinutes(duracionMin).isAfter(siguiente.get().getFechaHora())) {
                cursor = ajustarHora(siguiente.get().getFin());
                continue;
            }
            return Optional.of(cursor);
        }
    }

    @Override
    public int size() {
        return tamaño;
    }

    public List<Turno> listar() {
        List<Turno> turnos = new ArrayList<>();
        inOrder(raiz, turnos);
        return turnos;
    }

    protected Turno turnoPorId(String id) {
        return indicePorId.get(id);
    }

    protected void reemplazarTurno(Turno turno) {
        indicePorId.put(turno.getId(), turno);
    }

    private boolean solapa(Nodo actual, Turno turno) {
        if (actual == null) {
            return false;
        }
        if (turno.getFechaHora().isAfter(actual.turno.getFin()) || turno.getFechaHora().equals(actual.turno.getFin())) {
            return solapa(actual.derecho, turno);
        }
        if (turno.getFin().isBefore(actual.turno.getFechaHora()) || turno.getFin().equals(actual.turno.getFechaHora())) {
            return solapa(actual.izquierdo, turno);
        }
        return true;
    }

    private Nodo insertar(Nodo nodo, Turno turno) {
        if (nodo == null) {
            return new Nodo(turno);
        }
        int cmp = comparar(turno, nodo.turno);
        if (cmp < 0) {
            nodo.izquierdo = insertar(nodo.izquierdo, turno);
        } else if (cmp > 0) {
            nodo.derecho = insertar(nodo.derecho, turno);
        } else {
            nodo.turno = turno;
        }
        actualizarAltura(nodo);
        return balancear(nodo);
    }

    private Nodo eliminar(Nodo nodo, Turno turno) {
        if (nodo == null) {
            return null;
        }
        int cmp = comparar(turno, nodo.turno);
        if (cmp < 0) {
            nodo.izquierdo = eliminar(nodo.izquierdo, turno);
        } else if (cmp > 0) {
            nodo.derecho = eliminar(nodo.derecho, turno);
        } else {
            if (nodo.izquierdo == null || nodo.derecho == null) {
                nodo = (nodo.izquierdo != null) ? nodo.izquierdo : nodo.derecho;
            } else {
                Nodo sucesor = minimo(nodo.derecho);
                nodo.turno = sucesor.turno;
                nodo.derecho = eliminar(nodo.derecho, sucesor.turno);
            }
        }
        if (nodo == null) {
            return null;
        }
        actualizarAltura(nodo);
        return balancear(nodo);
    }

    private Nodo siguienteNodo(Nodo nodo, LocalDateTime fecha) {
        Nodo actual = nodo;
        Nodo candidato = null;
        while (actual != null) {
            if (actual.turno.getFechaHora().isBefore(fecha)) {
                actual = actual.derecho;
            } else {
                candidato = actual;
                actual = actual.izquierdo;
            }
        }
        return candidato;
    }

    private Turno turnoAnterior(LocalDateTime fecha) {
        Nodo actual = raiz;
        Nodo candidato = null;
        while (actual != null) {
            if (actual.turno.getFechaHora().isBefore(fecha)) {
                candidato = actual;
                actual = actual.derecho;
            } else {
                actual = actual.izquierdo;
            }
        }
        return candidato == null ? null : candidato.turno;
    }

    private Nodo minimo(Nodo nodo) {
        Nodo actual = nodo;
        while (actual != null && actual.izquierdo != null) {
            actual = actual.izquierdo;
        }
        return actual;
    }

    private int altura(Nodo nodo) {
        return nodo == null ? 0 : nodo.altura;
    }

    private void actualizarAltura(Nodo nodo) {
        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
    }

    private int balance(Nodo nodo) {
        return altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    private Nodo balancear(Nodo nodo) {
        int balance = balance(nodo);
        if (balance > 1) {
            if (balance(nodo.izquierdo) < 0) {
                nodo.izquierdo = rotacionIzquierda(nodo.izquierdo);
            }
            return rotacionDerecha(nodo);
        }
        if (balance < -1) {
            if (balance(nodo.derecho) > 0) {
                nodo.derecho = rotacionDerecha(nodo.derecho);
            }
            return rotacionIzquierda(nodo);
        }
        return nodo;
    }

    private Nodo rotacionDerecha(Nodo y) {
        Nodo x = y.izquierdo;
        Nodo t2 = x.derecho;
        x.derecho = y;
        y.izquierdo = t2;
        actualizarAltura(y);
        actualizarAltura(x);
        return x;
    }

    private Nodo rotacionIzquierda(Nodo x) {
        Nodo y = x.derecho;
        Nodo t2 = y.izquierdo;
        y.izquierdo = x;
        x.derecho = t2;
        actualizarAltura(x);
        actualizarAltura(y);
        return y;
    }

    private int comparar(Turno a, Turno b) {
        int cmp = a.getFechaHora().compareTo(b.getFechaHora());
        if (cmp != 0) {
            return cmp;
        }
        return a.getId().compareTo(b.getId());
    }

    private LocalDateTime ajustarHora(LocalDateTime fecha) {
        LocalDateTime inicioDia = inicioJornada(fecha.toLocalDate());
        LocalDateTime finDia = finJornada(fecha.toLocalDate());
        if (fecha.isBefore(inicioDia)) {
            return inicioDia;
        }
        if (!fecha.isBefore(finDia)) {
            return inicioProximoDia(fecha.toLocalDate());
        }
        return fecha;
    }

    private LocalDateTime inicioJornada(LocalDate dia) {
        return LocalDateTime.of(dia, INICIO_JORNADA);
    }

    private LocalDateTime finJornada(LocalDate dia) {
        return LocalDateTime.of(dia, FIN_JORNADA);
    }

    private LocalDateTime inicioProximoDia(LocalDate dia) {
        return LocalDateTime.of(dia.plusDays(1), INICIO_JORNADA);
    }

    private boolean mismoDia(LocalDateTime a, LocalDateTime b) {
        return a.toLocalDate().equals(b.toLocalDate());
    }

    private static class Nodo {
        private Turno turno;
        private Nodo izquierdo;
        private Nodo derecho;
        private int altura;

        private Nodo(Turno turno) {
            this.turno = turno;
            this.altura = 1;
        }
    }

    private void inOrder(Nodo nodo, List<Turno> acumulador) {
        if (nodo == null) {
            return;
        }
        inOrder(nodo.izquierdo, acumulador);
        acumulador.add(nodo.turno);
        inOrder(nodo.derecho, acumulador);
    }
}
