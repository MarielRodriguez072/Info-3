public class SalaEspera {

    private final String[] buffer;
    private final int capacidad;
    private int frente;
    private int fin;
    private int cantidad;

    public SalaEspera(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("Capacidad invalida");
        }
        this.capacidad = capacidad;
        this.buffer = new String[capacidad];
        this.frente = 0;
        this.fin = -1;
        this.cantidad = 0;
    }

    public void llega(String dni) {
        fin = (fin + 1) % capacidad;
        buffer[fin] = dni;
        if (cantidad < capacidad) {
            cantidad++;
        } else {
            frente = (frente + 1) % capacidad;
        }
    }

    public String atiende() {
        if (cantidad == 0) {
            return null;
        }
        String valor = buffer[frente];
        frente = (frente + 1) % capacidad;
        cantidad--;
        return valor;
    }

    public String peek() {
        if (cantidad == 0) {
            return null;
        }
        return buffer[frente];
    }

    public int size() {
        return cantidad;
    }
}
