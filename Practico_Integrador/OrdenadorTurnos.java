import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OrdenadorTurnos {

    public List<Turno> ordenarPorHora(List<Turno> turnos) {
        List<Turno> copia = new ArrayList<>(turnos);
        for (int i = 1; i < copia.size(); i++) {
            Turno clave = copia.get(i);
            int j = i - 1;
            while (j >= 0 && compararHora(clave, copia.get(j)) < 0) {
                copia.set(j + 1, copia.get(j));
                j--;
            }
            copia.set(j + 1, clave);
        }
        return copia;
    }

    public List<Turno> ordenarPorDuracion(List<Turno> turnos) {
        List<Turno> copia = new ArrayList<>(turnos);
        int n = copia.size();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Turno temp = copia.get(i);
                int j = i;
                while (j >= gap && compararDuracion(temp, copia.get(j - gap)) < 0) {
                    copia.set(j, copia.get(j - gap));
                    j -= gap;
                }
                copia.set(j, temp);
            }
        }
        return copia;
    }

    public List<Turno> ordenarPorApellido(List<Turno> turnos, MapaPacientes mapa) {
        List<Turno> copia = new ArrayList<>(turnos);
        quicksortApellido(copia, 0, copia.size() - 1, mapa);
        return copia;
    }

    public void medirAlgoritmos() {
        int[] tamaños = {1000, 10000, 50000};
        for (int tamaño : tamaños) {
            List<Turno> casos = generarTurnos(tamaño);
            long insercion = medir(() -> ordenarPorHora(casos));
            long shell = medir(() -> ordenarPorDuracion(casos));
            long quick = medir(() -> ordenarPorApellido(casos, pacientesSynthetic()));
            System.out.println("Turnos: " + tamaño);
            System.out.println("  Insercion: " + TimeUnit.NANOSECONDS.toMillis(insercion) + " ms");
            System.out.println("  Shellsort: " + TimeUnit.NANOSECONDS.toMillis(shell) + " ms");
            System.out.println("  Quicksort: " + TimeUnit.NANOSECONDS.toMillis(quick) + " ms");
        }
    }

    private long medir(Runnable tarea) {
        long inicio = System.nanoTime();
        tarea.run();
        return System.nanoTime() - inicio;
    }

    private int compararHora(Turno a, Turno b) {
        int cmp = a.getFechaHora().compareTo(b.getFechaHora());
        if (cmp != 0) {
            return cmp;
        }
        return a.getId().compareTo(b.getId());
    }

    private int compararDuracion(Turno a, Turno b) {
        int cmp = Integer.compare(a.getDuracionMin(), b.getDuracionMin());
        if (cmp != 0) {
            return cmp;
        }
        return a.getId().compareTo(b.getId());
    }

    private void quicksortApellido(List<Turno> turnos, int bajo, int alto, MapaPacientes mapa) {
        if (bajo < alto) {
            int pivote = particionar(turnos, bajo, alto, mapa);
            quicksortApellido(turnos, bajo, pivote - 1, mapa);
            quicksortApellido(turnos, pivote + 1, alto, mapa);
        }
    }

    private int particionar(List<Turno> turnos, int bajo, int alto, MapaPacientes mapa) {
        Turno pivote = turnos.get(alto);
        int i = bajo - 1;
        for (int j = bajo; j < alto; j++) {
            if (compararApellido(turnos.get(j), pivote, mapa) <= 0) {
                i++;
                intercambiar(turnos, i, j);
            }
        }
        intercambiar(turnos, i + 1, alto);
        return i + 1;
    }

    private int compararApellido(Turno a, Turno b, MapaPacientes mapa) {
        String apellidoA = apellidoPaciente(a, mapa);
        String apellidoB = apellidoPaciente(b, mapa);
        int cmp = apellidoA.compareTo(apellidoB);
        if (cmp != 0) {
            return cmp;
        }
        return a.getId().compareTo(b.getId());
    }

    private void intercambiar(List<Turno> turnos, int i, int j) {
        Turno temp = turnos.get(i);
        turnos.set(i, turnos.get(j));
        turnos.set(j, temp);
    }

    private String apellidoPaciente(Turno turno, MapaPacientes mapa) {
        Paciente paciente = mapa.get(turno.getDniPaciente());
        if (paciente == null) {
            return "";
        }
        String[] partes = paciente.getNombre().split(" ");
        return partes[partes.length - 1];
    }

    private List<Turno> generarTurnos(int cantidad) {
        List<Turno> turnos = new ArrayList<>(cantidad);
        LocalDateTime base = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
        for (int i = 0; i < cantidad; i++) {
            LocalDateTime fecha = base.plusMinutes(i * 15L);
            String id = "S" + i;
            String dni = String.format("%08d", i % 9000 + 1000);
            String medico = "MP" + (i % 10);
            int duracion = 15 + (i % 6) * 5;
            turnos.add(new Turno(id, dni, medico, fecha, duracion, "Sintetico"));
        }
        return turnos;
    }

    private MapaPacientes pacientesSynthetic() {
        MapaPacientesTabla mapa = new MapaPacientesTabla();
        for (int i = 0; i < 9000; i++) {
            String dni = String.format("%08d", i + 1000);
            mapa.put(dni, new Paciente(dni, "Paciente " + i));
        }
        return mapa;
    }
}
