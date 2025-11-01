import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlannerMinHeap implements Planner {

    private final List<Recordatorio> heap;
    private final TablaHash<String, Integer> indices;

    public PlannerMinHeap() {
        this.heap = new ArrayList<>();
        this.indices = new TablaHash<>();
    }

    @Override
    public void programar(Recordatorio recordatorio) {
        heap.add(recordatorio);
        indices.put(recordatorio.getId(), heap.size() - 1);
        heapifyUp(heap.size() - 1);
    }

    @Override
    public Recordatorio proximo() {
        if (heap.isEmpty()) {
            return null;
        }
        Recordatorio raiz = heap.get(0);
        Recordatorio ultimo = heap.remove(heap.size() - 1);
        indices.remove(raiz.getId());
        if (!heap.isEmpty()) {
            heap.set(0, ultimo);
            indices.put(ultimo.getId(), 0);
            heapifyDown(0);
        }
        return raiz;
    }

    @Override
    public void reprogramar(String id, LocalDateTime nuevaFecha) {
        Integer indice = indices.get(id);
        if (indice == null) {
            return;
        }
        Recordatorio recordatorio = heap.get(indice);
        LocalDateTime anterior = recordatorio.getFecha();
        recordatorio.setFecha(nuevaFecha);
        if (nuevaFecha.isBefore(anterior)) {
            heapifyUp(indice);
        } else {
            heapifyDown(indice);
        }
    }

    @Override
    public int size() {
        return heap.size();
    }

    private void heapifyUp(int indice) {
        int actual = indice;
        while (actual > 0) {
            int padre = (actual - 1) / 2;
            if (heap.get(actual).compareTo(heap.get(padre)) < 0) {
                intercambiar(actual, padre);
                actual = padre;
            } else {
                break;
            }
        }
    }

    private void heapifyDown(int indice) {
        int actual = indice;
        int tamaño = heap.size();
        while (true) {
            int izquierdo = 2 * actual + 1;
            int derecho = 2 * actual + 2;
            int menor = actual;
            if (izquierdo < tamaño && heap.get(izquierdo).compareTo(heap.get(menor)) < 0) {
                menor = izquierdo;
            }
            if (derecho < tamaño && heap.get(derecho).compareTo(heap.get(menor)) < 0) {
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
        Recordatorio tmp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, tmp);
        indices.put(heap.get(i).getId(), i);
        indices.put(heap.get(j).getId(), j);
    }
}
