import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MergeadorAgendas {

    public List<Turno> merge(List<Turno> local, List<Turno> nube, List<String> conflictos) {
        List<Turno> resultado = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < local.size() && j < nube.size()) {
            Turno a = local.get(i);
            Turno b = nube.get(j);
            if (esConflicto(a, b)) {
                conflictos.add("Conflicto entre " + describir(a) + " y " + describir(b));
                resultado.add(a);
                i++;
                j++;
                continue;
            }
            if (comparar(a, b) <= 0) {
                resultado.add(a);
                i++;
            } else {
                resultado.add(b);
                j++;
            }
        }
        while (i < local.size()) {
            resultado.add(local.get(i++));
        }
        while (j < nube.size()) {
            resultado.add(nube.get(j++));
        }
        return resultado;
    }

    private boolean esConflicto(Turno a, Turno b) {
        if (a.getId().equals(b.getId())) {
            return true;
        }
        if (!a.getMatriculaMedico().equals(b.getMatriculaMedico())) {
            return false;
        }
        LocalDateTime inicioA = a.getFechaHora();
        LocalDateTime inicioB = b.getFechaHora();
        return inicioA.equals(inicioB);
    }

    private int comparar(Turno a, Turno b) {
        int cmp = a.getFechaHora().compareTo(b.getFechaHora());
        if (cmp != 0) {
            return cmp;
        }
        return a.getId().compareTo(b.getId());
    }

    private String describir(Turno turno) {
        return turno.getId() + "(" + turno.getMatriculaMedico() + " " + turno.getFechaHora() + ")";
    }
}
