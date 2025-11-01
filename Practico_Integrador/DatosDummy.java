import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatosDummy {

    public static List<String> pacientesCsv() {
        List<String> lineas = new ArrayList<>();
        lineas.add("dni,nombre");
        lineas.add("20111222,Luis Perez");
        lineas.add("30123123,Ana Gomez");
        lineas.add("40999888,Rosa Diaz");
        lineas.add("50222333,Juan Martinez");
        return lineas;
    }

    public static List<String> medicosCsv() {
        List<String> lineas = new ArrayList<>();
        lineas.add("matricula,nombre,especialidad");
        lineas.add("MP001,Emilia Ruiz,clinica");
        lineas.add("MP002,Marcelo Luna,cardiologia");
        lineas.add("MP003,Carla Ibarra,traumatologia");
        return lineas;
    }

    public static List<String> turnosCsv() {
        List<String> lineas = new ArrayList<>();
        lineas.add("id,dniPaciente,matriculaMedico,fechaHora,duracionMin,motivo");
        LocalDateTime base = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0);
        if (base.isBefore(LocalDateTime.now())) {
            base = base.plusDays(1);
        }
        lineas.add("T001,20111222,MP001," + base.plusHours(1) + ",30,Consulta general");
        lineas.add("T002,30123123,MP001," + base.plusHours(2) + ",45,Control anual");
        lineas.add("T003,40999888,MP002," + base.plusHours(1) + ",60,Chequeo cardiologico");
        lineas.add("T004,50222333,MP003," + base.plusHours(3) + ",30,Revision post-operatoria");
        return lineas;
    }
}
