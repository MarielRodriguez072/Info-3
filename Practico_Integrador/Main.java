import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner teclado = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_LISTA = DateTimeFormatter.ofPattern("dd/MM HH:mm");
    private static final OrdenadorTurnos ORDENADOR = new OrdenadorTurnos();
    private static final MergeadorAgendas MERGEADOR = new MergeadorAgendas();

    private static SistemaClinica sistema;
    private static SalaEspera salaEspera;
    private static Planner planner;
    private static PlanificadorQuirofano planificadorQuirofano;
    private static int capacidadSala = 5;
    private static int cantidadQuirofanos = 3;

    public static void main(String[] args) {
        inicializarRecursos(true);
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    verAgendaMedico();
                    break;
                case 2:
                    buscarProximoHueco();
                    break;
                case 3:
                    menuSalaEspera();
                    break;
                case 4:
                    menuPlanner();
                    break;
                case 5:
                    menuMapaPacientes();
                    break;
                case 6:
                    menuMergeAgendas();
                    break;
                case 7:
                    menuReportes();
                    break;
                case 8:
                    menuAuditoria();
                    break;
                case 9:
                    menuQuirofano();
                    break;
                case 10:
                    listarMedicos();
                    break;
                case 11:
                    inicializarRecursos(false);
                    break;
                case 0:
                    System.out.println("-------------------------------------------");
                    System.out.println("Fin de ejecucion.");
                    System.out.println("-------------------------------------------");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private static void inicializarRecursos(boolean banner) {
        if (banner) {
            System.out.println("===========================================");
            System.out.println("SISTEMA DE GESTION DE TURNOS MEDICOS");
            System.out.println("===========================================");
        } else {
            System.out.println("\n===========================================");
            System.out.println("RECARGA DE DATOS DUMMY");
            System.out.println("===========================================");
        }
        System.out.println("Cargando datos iniciales...");
        CargadorInicial cargador = new CargadorInicial();
        sistema = cargador.cargar();
        System.out.println("> Leyendo pacientes.csv ... [OK] (" + sistema.cantidadPacientes() + " registros)");
        System.out.println("> Leyendo medicos.csv ...... [OK] (" + sistema.cantidadMedicos() + " registros)");
        System.out.println("> Leyendo turnos.csv ....... [OK] (" + sistema.cantidadTurnos() + " registros)");
        imprimirValidaciones(sistema.errores());
        System.out.println("> Estructuras internas inicializadas correctamente.");
        System.out.println("-------------------------------------------");
        if (capacidadSala <= 0) {
            capacidadSala = 5;
        }
        if (cantidadQuirofanos <= 0) {
            cantidadQuirofanos = 3;
        }
        salaEspera = new SalaEspera(capacidadSala);
        planner = new PlannerMinHeap();
        planificadorQuirofano = new PlanificadorQuirofanoImpl(cantidadQuirofanos);
    }

    private static void imprimirValidaciones(List<String> errores) {
        if (errores.isEmpty()) {
            System.out.println("> Validando datos ...");
            System.out.println("- Sin errores de validacion");
            return;
        }
        int fechas = 0;
        int duplicados = 0;
        int inexistentes = 0;
        for (String error : errores) {
            String mensaje = error.toLowerCase();
            if (mensaje.contains("fecha")) {
                fechas++;
            } else if (mensaje.contains("duplicado")) {
                duplicados++;
            } else if (mensaje.contains("inexistente")) {
                inexistentes++;
            }
        }
        System.out.println("> Validando datos ...");
        if (fechas > 0) {
            System.out.println("- " + fechas + " turnos rechazados (fecha pasada)");
        }
        if (duplicados > 0) {
            System.out.println("- " + duplicados + " turno/s duplicado/s por ID");
        }
        if (inexistentes > 0) {
            System.out.println("- " + inexistentes + " referencias a paciente/medico inexistente");
        }
        for (String error : errores) {
            if (!error.toLowerCase().contains("fecha") && !error.toLowerCase().contains("duplicado") && !error.toLowerCase().contains("inexistente")) {
                System.out.println("- " + error);
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("-------------------------------------------");
        System.out.println("MENU PRINCIPAL");
        System.out.println("-------------------------------------------");
        System.out.println("1) Ver agenda de un medico");
        System.out.println("2) Buscar proximo turno disponible");
        System.out.println("3) Simular sala de espera");
        System.out.println("4) Programar recordatorios");
        System.out.println("5) Consultar indice de pacientes (Hash)");
        System.out.println("6) Consolidador de agendas");
        System.out.println("7) Reportes de ordenamiento");
        System.out.println("8) Auditoria Undo/Redo");
        System.out.println("9) Planificador de quirofano");
        System.out.println("0) Salir");
        System.out.println("-------------------------------------------");
        System.out.println("Extras:");
        System.out.println("10) Listar medicos");
        System.out.println("11) Recargar datos dummy");
        System.out.println("-------------------------------------------");
    }

    private static void verAgendaMedico() {
        String matricula = seleccionarMedico();
        if (matricula == null) {
            return;
        }
        AgendaConHistorial agenda = sistema.agendaConHistorial(matricula);
        Medico medico = sistema.medico(matricula);
        if (agenda == null || medico == null) {
            System.out.println("Medico inexistente.");
            return;
        }
        int opcion;
        do {
            System.out.println("-------------------------------------------");
            System.out.println("[AGENDA DEL DR. " + medico.getNombre().toUpperCase() + " - " + medico.getEspecialidad().toUpperCase() + "]");
            System.out.println("-------------------------------------------");
            System.out.println("Turnos ordenados por fecha (AVL Tree):");
            imprimirTurnosTabla(((AgendaMedicoAVL) agenda).listar());
            System.out.println("Consultas:");
            System.out.println("1. Siguiente turno desde fecha");
            System.out.println("2. Buscar primer hueco de X minutos");
            System.out.println("3. Buscar por anio");
            System.out.println("4. Buscar por mes");
            System.out.println("5. Buscar por dia");
            System.out.println("6. Buscar entre rangos");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1:
                    LocalDateTime referencia = solicitarFechaDetallada("desde", true);
                    if (referencia == null) {
                        System.out.println("Operacion cancelada.");
                        break;
                    }
                    Optional<Turno> siguiente = agenda.siguiente(referencia);
                    System.out.print("Siguiente turno >= " + referencia + " -> ");
                    if (siguiente.isPresent()) {
                        Turno turno = siguiente.get();
                        System.out.println(turno.getFechaHora().format(FORMATO_LISTA) + " hs (" + turno.getId() + ")");
                    } else {
                        System.out.println("sin turnos");
                    }
                    System.out.println("[Operacion O(log n) - Arbol AVL balanceado]");
                    break;
                case 2:
                    LocalDateTime desde = solicitarFechaDetallada("desde", true);
                    if (desde == null) {
                        System.out.println("Operacion cancelada.");
                        break;
                    }
                    int duracion = leerEntero("Duracion del hueco (minutos): ");
                    Optional<LocalDateTime> hueco = agenda.primerHueco(desde, duracion);
                    System.out.print("Siguiente disponible >= " + desde + " -> ");
                    if (hueco.isPresent()) {
                        System.out.println(hueco.get().format(FORMATO_LISTA) + " hs");
                    } else {
                        System.out.println("sin disponibilidad");
                    }
                    System.out.println("[Operacion O(log n) - Arbol AVL balanceado]");
                    break;
                case 3:
                    buscarTurnosPorAnio(agenda);
                    break;
                case 4:
                    buscarTurnosPorMes(agenda);
                    break;
                case 5:
                    buscarTurnosPorDia(agenda);
                    break;
                case 6:
                    buscarTurnosEntreRangos(agenda);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private static void buscarProximoHueco() {
        String matricula = seleccionarMedico();
        if (matricula == null) {
            return;
        }
        AgendaConHistorial agenda = sistema.agendaConHistorial(matricula);
        if (agenda == null) {
            System.out.println("Medico inexistente.");
            return;
        }
        LocalDateTime desde = solicitarFechaDetallada("desde", true);
        if (desde == null) {
            System.out.println("Operacion cancelada.");
            return;
        }
        int duracion = leerEntero("Duracion requerida (minutos): ");
        Optional<LocalDateTime> hueco = agenda.primerHueco(desde, duracion);
        if (hueco.isPresent()) {
            System.out.println("Primer hueco >= " + desde + " (" + duracion + " min) -> " + hueco.get().format(FORMATO_LISTA) + " hs");
        } else {
            System.out.println("Sin disponibilidad para la duracion solicitada.");
        }
    }

    private static void menuSalaEspera() {
        System.out.println("-------------------------------------------");
        System.out.println("Simulacion de Sala de Espera (Cola Circular)");
        System.out.println("Capacidad maxima: " + capacidadSala);
        System.out.println("1) Ejecutar demo rapida");
        System.out.println("2) Gestion manual");
        System.out.println("0) Volver");
        int opcion = leerEntero("Opcion: ");
        switch (opcion) {
            case 1:
                ejecutarDemoSala();
                break;
            case 2:
                gestionarSalaManual();
                break;
            default:
                break;
        }
    }

    private static void ejecutarDemoSala() {
        String[] llegadas = {"32045982", "32458910", "31890432", "31247856", "32500890", "31111222"};
        salaEspera = new SalaEspera(capacidadSala);
        System.out.println("> Llegadas demo: ");
        for (String dni : llegadas) {
            if (salaEspera.size() == capacidadSala) {
                String desplazado = salaEspera.atiende();
                System.out.println("> Llega paciente " + dni + " -> Desborda, se elimina el mas antiguo (" + desplazado + ")");
            } else {
                System.out.println("> Llega paciente " + dni);
            }
            salaEspera.llega(dni);
        }
        System.out.println("Estado actual:");
        mostrarEstadoSala();
        System.out.println("[Operaciones O(1)]");
    }

    private static void gestionarSalaManual() {
        int opcion;
        do {
            System.out.println("\n--- Sala de espera manual ---");
            System.out.println("1. Llegada de paciente");
            System.out.println("2. Atiende");
            System.out.println("3. Peek");
            System.out.println("4. Estado actual");
            System.out.println("5. Reiniciar sala");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1:
                    salaEspera.llega(leerCadena("DNI paciente: "));
                    break;
                case 2:
                    System.out.println("Atiende: " + salaEspera.atiende());
                    break;
                case 3:
                    System.out.println("Peek: " + salaEspera.peek());
                    break;
                case 4:
                    mostrarEstadoSala();
                    break;
                case 5:
                    int nuevaCapacidad = leerEntero("Nueva capacidad: ");
                    if (nuevaCapacidad > 0) {
                        capacidadSala = nuevaCapacidad;
                        salaEspera = new SalaEspera(capacidadSala);
                    } else {
                        System.out.println("La capacidad debe ser positiva.");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarEstadoSala() {
        List<String> elementos = new ArrayList<>();
        String turno;
        while ((turno = salaEspera.atiende()) != null) {
            elementos.add(turno);
        }
        for (String valor : elementos) {
            salaEspera.llega(valor);
        }
        System.out.println("FRONT -> " + elementos + " <- REAR");
        System.out.println("Tamaño actual: " + salaEspera.size());
    }

    private static void menuPlanner() {
        int opcion;
        do {
            System.out.println("-------------------------------------------");
            System.out.println("Planner de Recordatorios (Min-Heap)");
            System.out.println("1. Programar recordatorio");
            System.out.println("2. Proximo recordatorio");
            System.out.println("3. Reprogramar recordatorio");
            System.out.println("4. Cantidad programada");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1:
                    programarRecordatorio();
                    break;
                case 2:
                    Recordatorio proximo = planner.proximo();
                    System.out.println("Proximo: " + (proximo == null ? "sin recordatorios" : proximo.getId() + " -> " + proximo.getFecha()));
                    break;
                case 3:
                    String id = leerCadena("Id recordatorio: ");
                    LocalDateTime nueva = solicitarFechaDetallada("nueva", true);
                    if (nueva != null) {
                        planner.reprogramar(id, nueva);
                    } else {
                        System.out.println("Accion cancelada.");
                    }
                    break;
                case 4:
                    System.out.println("Cantidad: " + planner.size());
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private static void programarRecordatorio() {
        String id = leerCadena("Id: ");
        LocalDateTime fecha = solicitarFechaDetallada("del recordatorio", true);
        if (fecha == null) {
            System.out.println("Accion cancelada.");
            return;
        }
        String dni = leerCadena("DNI paciente: ");
        String mensaje = leerCadena("Mensaje: ");
        planner.programar(new Recordatorio(id, fecha, dni, mensaje));
    }

    private static void menuMapaPacientes() {
        System.out.println("-------------------------------------------");
        System.out.println("Indice rapido de Pacientes (Hash con Chaining)");
        MapaPacientes mapa = sistema.pacientes();
        if (mapa instanceof MapaPacientesTabla) {
            MapaPacientesTabla tabla = (MapaPacientesTabla) mapa;
            System.out.println("Tamaño tabla = " + tabla.capacidad() + " | Load Factor = " + String.format("%.2f", tabla.factorCarga()));
            for (String linea : tabla.diagnosticoBuckets()) {
                System.out.println(linea);
            }
            if (tabla.factorCarga() > 0.75) {
                System.out.println("Rehash pendiente al superar load factor 0.75");
            }
        }
        int opcion;
        do {
            System.out.println("\n1. Buscar paciente");
            System.out.println("2. Agregar/actualizar paciente");
            System.out.println("3. Eliminar paciente");
            System.out.println("4. Listar DNIs");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1:
                    String dni = leerCadena("DNI: ");
                    System.out.println("Resultado: " + mapa.get(dni));
                    break;
                case 2:
                    String nuevoDni = leerCadena("DNI: ");
                    String nombre = leerCadena("Nombre completo: ");
                    mapa.put(nuevoDni, new Paciente(nuevoDni, nombre));
                    System.out.println("Guardado.");
                    break;
                case 3:
                    System.out.println("Eliminado: " + mapa.remove(leerCadena("DNI: ")));
                    break;
                case 4:
                    for (String clave : mapa.keys()) {
                        System.out.println(clave);
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private static void menuReportes() {
        String matricula = seleccionarMedico();
        if (matricula == null) {
            return;
        }
        AgendaConHistorial agenda = sistema.agendaConHistorial(matricula);
        if (agenda == null) {
            System.out.println("Medico inexistente.");
            return;
        }
        List<Turno> turnos = ((AgendaMedicoAVL) agenda).listar();
        System.out.println("Orden por hora:");
        for (Turno turno : ORDENADOR.ordenarPorHora(turnos)) {
            System.out.println("  " + turno.getId() + " - " + turno.getFechaHora());
        }
        System.out.println("Orden por duracion:");
        for (Turno turno : ORDENADOR.ordenarPorDuracion(turnos)) {
            System.out.println("  " + turno.getId() + " - " + turno.getDuracionMin() + " min");
        }
        System.out.println("Orden por apellido:");
        for (Turno turno : ORDENADOR.ordenarPorApellido(turnos, sistema.pacientes())) {
            System.out.println("  " + turno.getId() + " - " + turno.getDniPaciente());
        }
        System.out.println("Mediciones sinteticas:");
        ORDENADOR.medirAlgoritmos();
    }

    private static void menuAuditoria() {
        String matricula = seleccionarMedico();
        if (matricula == null) {
            return;
        }
        AgendaConHistorial agenda = sistema.agendaConHistorial(matricula);
        if (agenda == null) {
            System.out.println("Medico inexistente.");
            return;
        }
        int opcion;
        do {
            System.out.println("-------------------------------------------");
            System.out.println("AUDITORIA Y UNDO/REDO (Pilas)");
            System.out.println("1. Agendar turno");
            System.out.println("2. Cancelar turno");
            System.out.println("3. Reprogramar turno");
            System.out.println("4. Undo");
            System.out.println("5. Redo");
            System.out.println("6. Mostrar agenda actual");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1:
                    agendarTurno(agenda, matricula);
                    break;
                case 2:
                    cancelarTurno(agenda);
                    break;
                case 3:
                    reprogramarTurno(agenda);
                    break;
                case 4:
                    System.out.println("Undo -> " + agenda.undo());
                    break;
                case 5:
                    System.out.println("Redo -> " + agenda.redo());
                    break;
                case 6:
                    listarTurnos(agenda);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private static void menuQuirofano() {
        int opcion;
        do {
            System.out.println("-------------------------------------------");
            System.out.println("Planificador de Quirofano (Min-Heap + Top-K)");
            System.out.println("Quirofanos disponibles: " + cantidadQuirofanos);
            System.out.println("1. Procesar solicitud");
            System.out.println("2. Top K medicos bloqueados");
            System.out.println("3. Reiniciar planificador");
            System.out.println("0. Volver");
            opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1:
                    procesarSolicitud();
                    break;
                case 2:
                    int k = leerEntero("Valor de K: ");
                    System.out.println("TopK: " + planificadorQuirofano.topKMedicosBloqueados(k));
                    break;
                case 3:
                    int nuevosQuirofanos = leerEntero("Cantidad de quirofanos: ");
                    if (nuevosQuirofanos > 0) {
                        cantidadQuirofanos = nuevosQuirofanos;
                        planificadorQuirofano = new PlanificadorQuirofanoImpl(cantidadQuirofanos);
                    } else {
                        System.out.println("La cantidad debe ser positiva.");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private static void procesarSolicitud() {
        String id = leerCadena("Id solicitud: ");
        String matricula = leerCadena("Matricula medico: ");
        int duracion = leerEntero("Duracion (min): ");
        LocalDateTime deadline = solicitarFechaDetallada("del deadline", true);
        if (deadline == null) {
            System.out.println("Accion cancelada.");
            return;
        }
        planificadorQuirofano.procesar(new SolicitudCirugia(id, matricula, duracion, deadline));
    }

    private static Turno crearTurnoManual(String matricula) {
        try {
            String id = leerCadena("Id turno nube: ");
            String dni = leerCadena("DNI paciente: ");
            LocalDateTime fecha = solicitarFechaDetallada("del turno", true);
            if (fecha == null) {
                return null;
            }
            int duracion = leerEntero("Duracion minutos: ");
            String motivo = leerCadena("Motivo: ");
            return new Turno(id, dni, matricula, fecha, duracion, motivo);
        } catch (Exception e) {
            System.out.println("No se pudo crear turno extra.");
            return null;
        }
    }

    private static void listarTurnos(AgendaConHistorial agenda) {
        List<Turno> turnos = ((AgendaMedicoAVL) agenda).listar();
        if (turnos.isEmpty()) {
            System.out.println("Agenda vacia.");
            return;
        }
        for (Turno turno : turnos) {
            System.out.println(turno);
        }
    }

    private static void agendarTurno(AgendaConHistorial agenda, String matricula) {
        String id = leerCadena("Id turno: ");
        String dni = leerCadena("DNI paciente: ");
        LocalDateTime fecha = solicitarFechaDetallada("del turno", true);
        if (fecha == null) {
            System.out.println("Accion cancelada.");
            return;
        }
        int duracion = leerEntero("Duracion minutos: ");
        String motivo = leerCadena("Motivo: ");
        Turno turno = new Turno(id, dni, matricula, fecha, duracion, motivo);
        System.out.println("Agendar -> " + agenda.agendar(turno));
    }

    private static void cancelarTurno(AgendaConHistorial agenda) {
        String id = leerCadena("Id turno: ");
        System.out.println("Cancelar -> " + agenda.cancelar(id));
    }

    private static void reprogramarTurno(AgendaConHistorial agenda) {
        String id = leerCadena("Id turno: ");
        LocalDateTime fecha = solicitarFechaDetallada("nueva", true);
        if (fecha == null) {
            System.out.println("Accion cancelada.");
            return;
        }
        System.out.println("Reprogramar -> " + agenda.reprogramar(id, fecha));
    }

    private static void menuMergeAgendas() {
        String matricula = seleccionarMedico();
        if (matricula == null) {
            return;
        }
        AgendaConHistorial agenda = sistema.agendaConHistorial(matricula);
        if (agenda == null) {
            System.out.println("Medico inexistente.");
            return;
        }
        List<Turno> origen = ((AgendaMedicoAVL) agenda).listar();
        List<Turno> nube = new ArrayList<>(origen);
        System.out.println("Agregar turno extra a la agenda nube? (s/n)");
        String respuesta = teclado.nextLine().trim();
        if (respuesta.equalsIgnoreCase("s")) {
            Turno extra = crearTurnoManual(matricula);
            if (extra != null) {
                nube.add(extra);
            }
        }
        List<String> conflictos = new ArrayList<>();
        List<Turno> resultado = MERGEADOR.merge(origen, nube, conflictos);
        System.out.println("Merge resultante: " + resultado.size() + " turnos");
        if (!conflictos.isEmpty()) {
            System.out.println("Conflictos:");
            for (String conflicto : conflictos) {
                System.out.println("  " + conflicto);
            }
        }
    }

    private static void listarMedicos() {
        System.out.println("-------------------------------------------");
        System.out.println("Medicos disponibles:");
        for (Medico medico : sistema.medicosLista()) {
            System.out.println(medico.getMatricula() + " - " + medico.getNombre() + " (" + medico.getEspecialidad() + ")");
        }
    }

    private static String seleccionarMedico() {
        listarMedicos();
        String matricula = leerCadena("Matricula del medico (enter para cancelar): ");
        if (matricula.isEmpty()) {
            return null;
        }
        if (sistema.medico(matricula) == null) {
            System.out.println("Matricula no encontrada.");
            return null;
        }
        return matricula;
    }

    private static void buscarTurnosPorAnio(AgendaConHistorial agenda) {
        Integer anio = leerComponenteFecha("Anio", "", true, 1900, 3000);
        if (anio == null) {
            System.out.println("Operacion cancelada.");
            return;
        }
        List<Turno> resultado = new ArrayList<>();
        for (Turno turno : ((AgendaMedicoAVL) agenda).listar()) {
            if (turno.getFechaHora().getYear() == anio) {
                resultado.add(turno);
            }
        }
        imprimirTurnosTabla(resultado);
    }

    private static void buscarTurnosPorMes(AgendaConHistorial agenda) {
        Integer anio = leerComponenteFecha("Anio", "del mes", true, 1900, 3000);
        if (anio == null) {
            System.out.println("Operacion cancelada.");
            return;
        }
        Integer mes = leerComponenteFecha("Mes", "", true, 1, 12);
        if (mes == null) {
            System.out.println("Operacion cancelada.");
            return;
        }
        List<Turno> resultado = new ArrayList<>();
        for (Turno turno : ((AgendaMedicoAVL) agenda).listar()) {
            LocalDateTime fecha = turno.getFechaHora();
            if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                resultado.add(turno);
            }
        }
        imprimirTurnosTabla(resultado);
    }

    private static void buscarTurnosPorDia(AgendaConHistorial agenda) {
        Integer anio = leerComponenteFecha("Anio", "del dia", true, 1900, 3000);
        if (anio == null) {
            System.out.println("Operacion cancelada.");
            return;
        }
        Integer mes = leerComponenteFecha("Mes", "del dia", true, 1, 12);
        if (mes == null) {
            System.out.println("Operacion cancelada.");
            return;
        }
        Integer dia = leerComponenteFecha("Dia", "", true, 1, 31);
        if (dia == null) {
            System.out.println("Operacion cancelada.");
            return;
        }
        List<Turno> resultado = new ArrayList<>();
        for (Turno turno : ((AgendaMedicoAVL) agenda).listar()) {
            LocalDateTime fecha = turno.getFechaHora();
            if (fecha.getYear() == anio && fecha.getMonthValue() == mes && fecha.getDayOfMonth() == dia) {
                resultado.add(turno);
            }
        }
        imprimirTurnosTabla(resultado);
    }

    private static void buscarTurnosEntreRangos(AgendaConHistorial agenda) {
        LocalDateTime[] rango = solicitarRangoFechas();
        if (rango == null) {
            System.out.println("Operacion cancelada.");
            return;
        }
        List<Turno> resultado = new ArrayList<>();
        for (Turno turno : ((AgendaMedicoAVL) agenda).listar()) {
            LocalDateTime fecha = turno.getFechaHora();
            if (!fecha.isBefore(rango[0]) && !fecha.isAfter(rango[1])) {
                resultado.add(turno);
            }
        }
        imprimirTurnosTabla(resultado);
    }

    private static void imprimirTurnosTabla(List<Turno> turnos) {
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos para mostrar.");
            return;
        }
        System.out.println("ID      PACIENTE    FECHA Y HORA    MOTIVO");
        System.out.println("---------------------------------------------------------");
        for (Turno turno : turnos) {
            String fecha = turno.getFechaHora().format(FORMATO_LISTA) + " hs";
            System.out.printf("%-7s %-11s %-15s %s%n", turno.getId(), turno.getDniPaciente(), fecha, turno.getMotivo());
        }
        System.out.println("---------------------------------------------------------");
    }

    private static LocalDateTime[] solicitarRangoFechas() {
        while (true) {
            LocalDateTime desde = solicitarFechaDetallada("desde", true);
            if (desde == null) {
                return null;
            }
            LocalDateTime hasta = solicitarFechaDetallada("hasta", true);
            if (hasta == null) {
                return null;
            }
            if (hasta.isBefore(desde)) {
                System.out.println("La fecha 'hasta' debe ser posterior o igual a 'desde'.");
                continue;
            }
            return new LocalDateTime[]{desde, hasta};
        }
    }

    private static LocalDateTime solicitarFechaDetallada(String etiqueta, boolean permitirCancelar) {
        while (true) {
            Integer anio = leerComponenteFecha("Anio", etiqueta, permitirCancelar, 1900, 3000);
            if (anio == null) {
                return null;
            }
            Integer mes = leerComponenteFecha("Mes", etiqueta, permitirCancelar, 1, 12);
            if (mes == null) {
                return null;
            }
            Integer dia = leerComponenteFecha("Dia", etiqueta, permitirCancelar, 1, 31);
            if (dia == null) {
                return null;
            }
            Integer hora = leerComponenteFecha("Hora", etiqueta, permitirCancelar, 0, 23);
            if (hora == null) {
                return null;
            }
            Integer minuto = leerComponenteFecha("Minuto", etiqueta, permitirCancelar, 0, 59);
            if (minuto == null) {
                return null;
            }
            try {
                return LocalDateTime.of(anio, mes, dia, hora, minuto);
            } catch (DateTimeException e) {
                System.out.println("Fecha/hora invalida: " + e.getMessage());
                System.out.println("Intenta nuevamente.");
            }
        }
    }

    private static Integer leerComponenteFecha(String componente, String etiqueta, boolean permitirCancelar, int min, int max) {
        String sufijo = (etiqueta == null || etiqueta.isEmpty()) ? "" : " " + etiqueta;
        while (true) {
            System.out.print("Ingresa " + componente + sufijo + (permitirCancelar ? " (enter para cancelar): " : ": "));
            String linea = teclado.nextLine().trim();
            if (linea.isEmpty()) {
                if (permitirCancelar) {
                    return null;
                }
                System.out.println("Valor obligatorio.");
                continue;
            }
            try {
                int valor = Integer.parseInt(linea);
                if (valor < min || valor > max) {
                    System.out.println("Valor fuera de rango (" + min + " - " + max + ").");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido.");
            }
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String linea = teclado.nextLine().trim();
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido.");
            }
        }
    }

    private static String leerCadena(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }
}
