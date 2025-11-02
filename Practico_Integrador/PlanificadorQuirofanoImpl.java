import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlanificadorQuirofanoImpl implements PlanificadorQuirofano {

    private final List<Quirofano> heapQuirofanos;
    private final TablaHash<String, Integer> minutosBloqueados;

    public PlanificadorQuirofanoImpl(int cantidadQuirofanos) {
        if (cantidadQuirofanos <= 0) {
            throw new IllegalArgumentException("Cantidad de quirofanos invalida");
        }
        this.heapQuirofanos = new ArrayList<>();
        this.minutosBloqueados = new TablaHash<>();
        LocalDateTime ahora = LocalDateTime.now();
        for (int i = 0; i < cantidadQuirofanos; i++) {
            heapQuirofanos.add(new Quirofano("Q" + (i + 1), ahora));
        }
    }

    @Override
    public void procesar(SolicitudCirugia solicitud) {
        List<Quirofano> descartados = new ArrayList<>();
        Quirofano elegido = null;
        while (!heapQuirofanos.isEmpty()) {
            Quirofano candidato = extraer();
            if (candidato.finDisponible.plusMinutes(solicitud.getDuracionMin()).isAfter(solicitud.getDeadline())) {
                descartados.add(candidato);
            } else {
                elegido = candidato;
                break;
            }
        }
        for (Quirofano q : descartados) {
            insertar(q);
        }
        if (elegido == null) {
            return;
        }
        elegido.finDisponible = elegido.finDisponible.plusMinutes(solicitud.getDuracionMin());
        insertar(elegido);
        acumularBloqueo(solicitud.getMatricula(), solicitud.getDuracionMin());
    }

    @Override
    public List<String> topKMedicosBloqueados(int k) {
        if (k <= 0) {
            return Collections.emptyList();
        }
        List<TopEntrada> heap = new ArrayList<>();
        for (String clave : minutosBloqueados.keys()) {
            int minutos = minutosBloqueados.get(clave);
            if (heap.size() < k) {
                heap.add(new TopEntrada(clave, minutos));
                subirTop(heap, heap.size() - 1);
            } else if (minutos > heap.get(0).minutos) {
                heap.set(0, new TopEntrada(clave, minutos));
                bajarTop(heap, 0);
            }
        }
        List<String> resultado = new ArrayList<>();
        while (!heap.isEmpty()) {
            resultado.add(0, heapRemove(heap).matricula);
        }
        return resultado;
    }

    private void acumularBloqueo(String matricula, int minutos) {
        Integer actual = minutosBloqueados.get(matricula);
        if (actual == null) {
            minutosBloqueados.put(matricula, minutos);
        } else {
            minutosBloqueados.put(matricula, actual + minutos);
        }
    }

    private Quirofano extraer() {
        Quirofano raiz = heapQuirofanos.get(0);
        Quirofano ultimo = heapQuirofanos.remove(heapQuirofanos.size() - 1);
        if (heapQuirofanos.isEmpty()) {
            return raiz;
        }
        heapQuirofanos.set(0, ultimo);
        bajar(0);
        return raiz;
    }

    private void insertar(Quirofano q) {
        heapQuirofanos.add(q);
        subir(heapQuirofanos.size() - 1);
    }

    private void subir(int indice) {
        int actual = indice;
        while (actual > 0) {
            int padre = (actual - 1) / 2;
            if (heapQuirofanos.get(actual).finDisponible.isBefore(heapQuirofanos.get(padre).finDisponible)) {
                intercambiar(actual, padre);
                actual = padre;
            } else {
                break;
            }
        }
    }

    private void bajar(int indice) {
        int actual = indice;
        int tamaño = heapQuirofanos.size();
        while (true) {
            int izquierdo = 2 * actual + 1;
            int derecho = 2 * actual + 2;
            int menor = actual;
            if (izquierdo < tamaño && heapQuirofanos.get(izquierdo).finDisponible.isBefore(heapQuirofanos.get(menor).finDisponible)) {
                menor = izquierdo;
            }
            if (derecho < tamaño && heapQuirofanos.get(derecho).finDisponible.isBefore(heapQuirofanos.get(menor).finDisponible)) {
                menor = derecho;
            }
            if (menor != actual) {
                intercambiar(actual, menor);
                actual = menor;
            } else {
                break;
            }
        }
    }

    private void intercambiar(int i, int j) {
        Quirofano tmp = heapQuirofanos.get(i);
        heapQuirofanos.set(i, heapQuirofanos.get(j));
        heapQuirofanos.set(j, tmp);
    }

    private void subirTop(List<TopEntrada> heap, int indice) {
        int actual = indice;
        while (actual > 0) {
            int padre = (actual - 1) / 2;
            if (heap.get(actual).minutos < heap.get(padre).minutos) {
                TopEntrada tmp = heap.get(actual);
                heap.set(actual, heap.get(padre));
                heap.set(padre, tmp);
                actual = padre;
            } else {
                break;
            }
        }
    }

    private void bajarTop(List<TopEntrada> heap, int indice) {
        int actual = indice;
        int tamaño = heap.size();
        while (true) {
            int izquierdo = 2 * actual + 1;
            int derecho = 2 * actual + 2;
            int menor = actual;
            if (izquierdo < tamaño && heap.get(izquierdo).minutos < heap.get(menor).minutos) {
                menor = izquierdo;
            }
            if (derecho < tamaño && heap.get(derecho).minutos < heap.get(menor).minutos) {
                menor = derecho;
            }
            if (menor != actual) {
                TopEntrada tmp = heap.get(actual);
                heap.set(actual, heap.get(menor));
                heap.set(menor, tmp);
                actual = menor;
            } else {
                break;
            }
        }
    }

    private TopEntrada heapRemove(List<TopEntrada> heap) {
        TopEntrada raiz = heap.get(0);
        TopEntrada ultimo = heap.remove(heap.size() - 1);
        if (heap.isEmpty()) {
            return raiz;
        }
        heap.set(0, ultimo);
        bajarTop(heap, 0);
        return raiz;
    }

    private static class Quirofano {
        private final String id;
        private LocalDateTime finDisponible;

        private Quirofano(String id, LocalDateTime finDisponible) {
            this.id = id;
            this.finDisponible = finDisponible;
        }
    }

    private static class TopEntrada {
        private final String matricula;
        private final int minutos;

        private TopEntrada(String matricula, int minutos) {
            this.matricula = matricula;
            this.minutos = minutos;
        }
    }
}
