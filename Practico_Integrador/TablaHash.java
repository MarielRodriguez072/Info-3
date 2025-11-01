import java.util.ArrayList;
import java.util.List;

public class TablaHash<K, V> {

    private static final double FACTOR_CARGA = 0.75;
    private Entrada<K, V>[] tabla;
    private int tamaño;

    public TablaHash() {
        this.tabla = nuevaTabla(8);
        this.tamaño = 0;
    }

    public void put(K clave, V valor) {
        if (clave == null) {
            throw new IllegalArgumentException("Clave nula");
        }
        if (necesitaRehash()) {
            rehash();
        }
        int indice = indice(clave, tabla.length);
        Entrada<K, V> actual = tabla[indice];
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                actual.valor = valor;
                return;
            }
            actual = actual.siguiente;
        }
        Entrada<K, V> nuevo = new Entrada<>(clave, valor);
        nuevo.siguiente = tabla[indice];
        tabla[indice] = nuevo;
        tamaño++;
    }

    public V get(K clave) {
        int indice = indice(clave, tabla.length);
        Entrada<K, V> actual = tabla[indice];
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean containsKey(K clave) {
        return get(clave) != null;
    }

    public boolean remove(K clave) {
        int indice = indice(clave, tabla.length);
        Entrada<K, V> actual = tabla[indice];
        Entrada<K, V> previo = null;
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (previo == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    previo.siguiente = actual.siguiente;
                }
                tamaño--;
                return true;
            }
            previo = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    public int size() {
        return tamaño;
    }

    public Iterable<K> keys() {
        List<K> claves = new ArrayList<>();
        for (Entrada<K, V> bucket : tabla) {
            Entrada<K, V> actual = bucket;
            while (actual != null) {
                claves.add(actual.clave);
                actual = actual.siguiente;
            }
        }
        return claves;
    }

    public Iterable<V> values() {
        List<V> valores = new ArrayList<>();
        for (Entrada<K, V> bucket : tabla) {
            Entrada<K, V> actual = bucket;
            while (actual != null) {
                valores.add(actual.valor);
                actual = actual.siguiente;
            }
        }
        return valores;
    }

    public int capacidad() {
        return tabla.length;
    }

    public double factorCarga() {
        return (double) tamaño / tabla.length;
    }

    public List<String> diagnosticoBuckets() {
        List<String> lineas = new ArrayList<>();
        for (int i = 0; i < tabla.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("[Bucket ").append(i).append("] -> ");
            Entrada<K, V> actual = tabla[i];
            if (actual == null) {
                sb.append("vacio");
            } else {
                while (actual != null) {
                    sb.append("(").append(actual.clave).append(")");
                    actual = actual.siguiente;
                    if (actual != null) {
                        sb.append(" -> ");
                    }
                }
            }
            lineas.add(sb.toString());
        }
        return lineas;
    }

    private boolean necesitaRehash() {
        return (double) (tamaño + 1) / tabla.length > FACTOR_CARGA;
    }

    private void rehash() {
        Entrada<K, V>[] anterior = tabla;
        tabla = nuevaTabla(anterior.length * 2);
        tamaño = 0;
        for (Entrada<K, V> bucket : anterior) {
            Entrada<K, V> actual = bucket;
            while (actual != null) {
                put(actual.clave, actual.valor);
                actual = actual.siguiente;
            }
        }
    }

    private int indice(K clave, int capacidad) {
        int hash = clave.hashCode();
        hash ^= (hash >>> 16);
        if (hash < 0) {
            hash = -hash;
        }
        return hash % capacidad;
    }

    @SuppressWarnings("unchecked")
    private Entrada<K, V>[] nuevaTabla(int capacidad) {
        return (Entrada<K, V>[]) new Entrada[capacidad];
    }

    private static class Entrada<K, V> {
        private final K clave;
        private V valor;
        private Entrada<K, V> siguiente;

        private Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }
}
