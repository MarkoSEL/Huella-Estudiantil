package HuellaEstudiantil.vista;

import HuellaEstudiantil.controlador.Controlador;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Vista {

    private Controlador controlador;

    public Vista() {
        this.controlador = new Controlador();
    }
    
    // --- MÉTODO FALTANTE RESTAURADO ---
    private DayOfWeek traducirDia(String diaEnEspanol) {
        switch (diaEnEspanol.trim().toUpperCase()) {
            case "LUNES": return DayOfWeek.MONDAY;
            case "MARTES": return DayOfWeek.TUESDAY;
            case "MIERCOLES":
            case "MIÉRCOLES": return DayOfWeek.WEDNESDAY;
            case "JUEVES": return DayOfWeek.THURSDAY;
            case "VIERNES": return DayOfWeek.FRIDAY;
            case "SABADO":
            case "SÁBADO": return DayOfWeek.SATURDAY;
            case "DOMINGO": return DayOfWeek.SUNDAY;
            default: return null; // Devuelve null si no es un día válido
        }
    }

    public void mostrarMenu() {
        // --- MENÚ REORGANIZADO SEGÚN ESTRUCTURA SOLICITADA ---
        String menu = "SISTEMA DE PARTICIPACIONES 'HUELLA ESTUDIANTIL'\n\n"
                + "--- MÓDULO DE CONFIGURACIÓN ---\n"
                + "1. Registrar Curso\n"
                + "2. Registrar Docente\n"
                + "3. Registrar Sección\n"
                + "4. Definir Estructura de Evaluaciones por Curso\n"
                + "5. Generar Sesiones de Clase\n\n"
                + "--- MÓDULO DE GESTIÓN DE CLASE ---\n"
                + "6. Registrar Estudiante\n"
                + "7. Registrar Participación\n"
                + "8. Procesar Participaciones\n"
                + "9. Ajustar Puntos Manualmente\n\n"
                + "--- MÓDULO DE ADMINISTRACIÓN Y CALIFICACIONES ---\n"
                + "10. Matricular Estudiante en Sección\n\n"
                + "0. Salir\n\n"
                + "Ingrese una opción:";

        int opcion = -1;
        do {
            try {
                String input = JOptionPane.showInputDialog(null, menu);
                if (input == null) {
                    opcion = 0;
                    break;
                }
                opcion = Integer.parseInt(input);

                switch (opcion) {
                    case 1: gestionarRegistrarCurso(); break;
                    case 2: gestionarRegistrarDocente(); break;
                    case 3: gestionarRegistrarSeccion(); break;
                    case 4: gestionarDefinirEvaluacion(); break;
                    case 5: gestionarGenerarSesiones(); break;
                    case 6: gestionarRegistrarEstudiante(); break;
                    case 7: gestionarRegistroParticipacion(); break;
                    case 8: gestionarProcesarParticipaciones(); break;
                    case 9: gestionarAjusteManual(); break;
                    case 10: gestionarMatricula(); break;

                    case 0:
                        JOptionPane.showMessageDialog(null, "Saliendo del sistema...");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción no válida.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Por favor, ingrese solo números.");
            }
        } while (opcion != 0);
    }
    
    private void gestionarMatricula() { // Antes 1, ahora 7
        String codigoEst = JOptionPane.showInputDialog("Ingrese el código del estudiante (ej. U2024001):");
        if (codigoEst == null || codigoEst.trim().isEmpty()) return;
        String idSec = JOptionPane.showInputDialog("Ingrese el ID de la sección (ej. SI-123):");
        if (idSec == null || idSec.trim().isEmpty()) return;
        String resultado = controlador.matricularEstudiante(codigoEst, idSec);
        JOptionPane.showMessageDialog(null, resultado);
    }
    
    private void gestionarGenerarSesiones() {
        String idSec = JOptionPane.showInputDialog("Ingrese el ID de la sección:");
        if (idSec == null || idSec.trim().isEmpty()) return;
        
        // Solicitar fecha de inicio
        String fechaInput = JOptionPane.showInputDialog("Ingrese la fecha de inicio (ej. 10/03/2025):");
        if (fechaInput == null || fechaInput.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: No se ingresó la fecha de inicio.");
            return;
        }
        
        LocalDate fechaInicio;
        try {
            String[] partes = fechaInput.trim().split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int anio = Integer.parseInt(partes[2]);
            fechaInicio = LocalDate.of(anio, mes, dia);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: Formato de fecha inválido. Use dd/MM/yyyy");
            return;
        }
        
        // Solicitar días de la semana (puede ser uno o varios)
        String diasInput = JOptionPane.showInputDialog("Ingrese los días de la semana separados por comas (ej. LUNES,MARTES o LUNES):");
        if (diasInput == null || diasInput.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: No se ingresaron los días de la semana.");
            return;
        }
        
        String[] diasArray = diasInput.toUpperCase().split(",");
        ArrayList<DayOfWeek> diasList = new ArrayList<>();
        
        for (String diaStr : diasArray) {
            DayOfWeek dia = traducirDia(diaStr.trim());
            if (dia == null) {
                JOptionPane.showMessageDialog(null, "Error: Día no válido: " + diaStr);
                return;
            }
            diasList.add(dia);
        }
        
        if (diasList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: Debe ingresar al menos un día válido.");
            return;
        }
        
        DayOfWeek[] dias = diasList.toArray(new DayOfWeek[0]);
        String resultado = controlador.generarSesiones(idSec, fechaInicio, dias);
        
        // Mostrar en JTextArea
        javax.swing.JTextArea textArea = new javax.swing.JTextArea(resultado);
        textArea.setEditable(false);
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));
        JOptionPane.showMessageDialog(null, scrollPane, "Generación de Sesiones", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gestionarRegistroParticipacion() {
        String idSeccion = JOptionPane.showInputDialog("Ingrese el ID de la sección:");
        if (idSeccion == null || idSeccion.trim().isEmpty()) return;
        
        String idEvaluacion = JOptionPane.showInputDialog("Ingrese el ID de la evaluación (ej. PC1):");
        if (idEvaluacion == null || idEvaluacion.trim().isEmpty()) return;
        
        String semana = JOptionPane.showInputDialog("Ingrese la Semana (ej. Semana 5):");
        if (semana == null || semana.trim().isEmpty()) return;
        
        String sesion = JOptionPane.showInputDialog("Ingrese la Sesión (ej. Lunes 15/09):");
        if (sesion == null || sesion.trim().isEmpty()) return;
        
        String codigoEst = JOptionPane.showInputDialog("Ingrese el código del estudiante:");
        if (codigoEst == null || codigoEst.trim().isEmpty()) return;
        
        String resultado = controlador.registrarParticipacion(idSeccion, idEvaluacion, semana, sesion, codigoEst);
        JOptionPane.showMessageDialog(null, resultado);
    }
    
    private void gestionarProcesarParticipaciones() { // Antes 4, ahora 9
        String idSec = JOptionPane.showInputDialog("Ingrese el ID de la sección (ej. SI-123):");
        if (idSec == null || idSec.trim().isEmpty()) return;
        
        String idEval = JOptionPane.showInputDialog("Ingrese la evaluación (ej. PC1):");
        if (idEval == null || idEval.trim().isEmpty()) return;
        
        String resultado = controlador.procesarParticipaciones(idSec, idEval);
        
        // --- CÓDIGO FALTANTE PARA MOSTRAR EN JTEXTAREA ---
        javax.swing.JTextArea textArea = new javax.swing.JTextArea(resultado);
        textArea.setEditable(false);
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 200));
        JOptionPane.showMessageDialog(null, scrollPane, "Resultado del Procesamiento", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // --- NUEVOS MÉTODOS DE GESTIÓN ---

    private void gestionarRegistrarEstudiante() {
        String nombre = JOptionPane.showInputDialog("Ingrese Nombre Completo:");
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: El campo Nombre está vacío.");
            return;
        }
        
        String carreraInput = JOptionPane.showInputDialog("Ingrese Carrera (1: Sistemas, 2: Software, 3: Otro):");
        if (carreraInput == null || carreraInput.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: El campo Carrera está vacío.");
            return;
        }
        String carrera = "Otro";
        if (carreraInput.equals("1")) carrera = "Sistemas";
        if (carreraInput.equals("2")) carrera = "Software";
        
        try {
            String cicloInput = JOptionPane.showInputDialog("Ingrese Ciclo (1-12):");
            if (cicloInput == null || cicloInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Error: El campo Ciclo está vacío.");
                return;
            }
            int ciclo = Integer.parseInt(cicloInput);
            String resultado = controlador.registrarEstudiante(nombre, carrera, ciclo);
            JOptionPane.showMessageDialog(null, resultado);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: El ciclo debe ser un número válido.");
        }
    }

    private void gestionarRegistrarDocente() {
        String id = JOptionPane.showInputDialog("Ingrese ID del Docente (ej. D001):");
        if (id == null || id.trim().isEmpty()) return;
        String nombre = JOptionPane.showInputDialog("Ingrese Nombre Completo del Docente:");
        if (nombre == null || nombre.trim().isEmpty()) return;
        
        String resultado = controlador.registrarDocente(id, nombre);
        JOptionPane.showMessageDialog(null, resultado);
    }

    private void gestionarRegistrarCurso() {
        String codigo = JOptionPane.showInputDialog("Ingrese Código del Curso (ej. CS101):");
        if (codigo == null || codigo.trim().isEmpty()) return;
        String nombre = JOptionPane.showInputDialog("Ingrese Nombre del Curso:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        String tipoInput = JOptionPane.showInputDialog("Ingrese Tipo (1: Presencial, 2: Remoto, 3: Virtual):");
        if (tipoInput == null) return;
        String tipo = "Presencial";
        if (tipoInput.equals("2")) tipo = "Remoto";
        if (tipoInput.equals("3")) tipo = "Virtual";
        
        try {
            int capacidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese Capacidad Máxima (ej. 32):"));
            String resultado = controlador.registrarCurso(codigo, nombre, tipo, capacidad);
            JOptionPane.showMessageDialog(null, resultado);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: La capacidad debe ser un número.");
        }
    }

    private void gestionarDefinirEvaluacion() {
        String codigoCurso = JOptionPane.showInputDialog("Ingrese Código del Curso (ej. CS101):");
        if (codigoCurso == null || codigoCurso.trim().isEmpty()) return;
        String nombreEval = JOptionPane.showInputDialog("Ingrese Nombre de la Evaluación (ej. PC1):");
        if (nombreEval == null || nombreEval.trim().isEmpty()) return;
        
        String resultado = controlador.definirEvaluacion(codigoCurso, nombreEval);
        JOptionPane.showMessageDialog(null, resultado);
    }

    private void gestionarRegistrarSeccion() {
        String codigoCurso = JOptionPane.showInputDialog("Ingrese Código del Curso (ej. CS101):");
        if (codigoCurso == null || codigoCurso.trim().isEmpty()) return;
        String idDocente = JOptionPane.showInputDialog("Ingrese ID del Docente (ej. D001):");
        if (idDocente == null || idDocente.trim().isEmpty()) return;
        String periodo = JOptionPane.showInputDialog("Ingrese Periodo (ej. 2025-Marzo):");
        if (periodo == null || periodo.trim().isEmpty()) return;
        
        String resultado = controlador.registrarSeccion(codigoCurso, idDocente, periodo);
        JOptionPane.showMessageDialog(null, resultado);
    }

    private void gestionarAjusteManual() {
        String idSeccion = JOptionPane.showInputDialog("Ingrese ID de Sección:");
        if (idSeccion == null || idSeccion.trim().isEmpty()) return;
        String idEvaluacion = JOptionPane.showInputDialog("Ingrese ID de Evaluación:");
        if (idEvaluacion == null || idEvaluacion.trim().isEmpty()) return;
        String codigoEstudiante = JOptionPane.showInputDialog("Ingrese Código de Estudiante:");
        if (codigoEstudiante == null || codigoEstudiante.trim().isEmpty()) return;
        
        try {
            int nuevoPuntaje = Integer.parseInt(JOptionPane.showInputDialog("Ingrese Nuevo Puntaje:"));
            String resultado = controlador.ajustarParticipaciones(idSeccion, idEvaluacion, codigoEstudiante, nuevoPuntaje);
            JOptionPane.showMessageDialog(null, resultado);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: El puntaje debe ser un número.");
        }
    }
}