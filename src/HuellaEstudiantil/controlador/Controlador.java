package HuellaEstudiantil.controlador;

import HuellaEstudiantil.datos.BaseDeDatos;
import HuellaEstudiantil.modelo.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Controlador {

    private BaseDeDatos db;

    public Controlador() {
        this.db = BaseDeDatos.getInstancia();
    }

    // --- ALGORITMO DE ORDENAMIENTO (TAREA 1.3) ---
    private void ordenamientoBurbuja(ArrayList<NodoEstudiante> lista) {
        int n = lista.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (lista.get(j).getParticipaciones() < lista.get(j + 1).getParticipaciones()) {
                    NodoEstudiante aux = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, aux);
                }
            }
        }
    }
    
    // --- REQUERIMIENTOS ANTERIORES (YA FUNCIONAN CON LA REFACTORIZACIÓN) ---
    // (generarSesiones, matricularEstudiante, registrarParticipacion, procesarParticipaciones)
    // Nota: El código interno de estos métodos se mantiene casi idéntico, 
    // pero ahora operan sobre "NodoEstudiante", "NodoCurso", etc.

    public String generarSesiones(String idSeccion, LocalDate fechaInicio, DayOfWeek... dias) {
        NodoSeccion seccion = db.buscarSeccionPorId(idSeccion);
        if (seccion == null) {
            return "Error: La sección no existe.";
        }
        
        // Limpiar sesiones previas si existen
        seccion.getSesionesDeClase().clear();
        
        // Generar sesiones durante 16 semanas (semestre típico)
        int semanas = 16;
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder resultado = new StringBuilder();
        resultado.append("SESIONES DE CLASE GENERADAS\n");
        resultado.append("Sección: ").append(seccion.getId()).append("\n");
        resultado.append("Curso: ").append(seccion.getCurso().getNombre()).append("\n");
        resultado.append("Período: ").append(seccion.getPeriodo()).append("\n");
        resultado.append("Fecha de inicio: ").append(fechaInicio.format(formatoFecha)).append("\n");
        resultado.append("Días de clase: ");
        
        for (int i = 0; i < dias.length; i++) {
            resultado.append(dias[i].getDisplayName(TextStyle.FULL, Locale.of("es", "ES")));
            if (i < dias.length - 1) resultado.append(", ");
        }
        resultado.append("\n\n");
        resultado.append("SESIONES:\n");
        resultado.append("----------\n");
        
        int numeroSesion = 1;
        for (int semana = 0; semana < semanas; semana++) {
            LocalDate fechaActual = fechaInicio.plusWeeks(semana);
            for (DayOfWeek dia : dias) {
                // Ajustar la fecha al día de la semana correspondiente
                int diasHastaDia = dia.getValue() - fechaActual.getDayOfWeek().getValue();
                if (diasHastaDia < 0) diasHastaDia += 7;
                LocalDate fechaSesion = fechaActual.plusDays(diasHastaDia);
                
                // Agregar a la lista de sesiones
                seccion.getSesionesDeClase().add(fechaSesion);
                
                // Formatear para mostrar
                String diaNombre = fechaSesion.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.of("es", "ES"));
                resultado.append(String.format("Sesión %02d: %s %s\n", 
                    numeroSesion++, 
                    diaNombre,
                    fechaSesion.format(formatoFecha)));
            }
        }
        
        resultado.append("\nTotal de sesiones generadas: ").append(seccion.getSesionesDeClase().size());
        return resultado.toString();
    }

    public String matricularEstudiante(String codigoEstudiante, String idSeccion) {
        NodoEstudiante estudiante = db.buscarEstudiantePorCodigo(codigoEstudiante); // Devuelve NodoEstudiante
        NodoSeccion seccion = db.buscarSeccionPorId(idSeccion); // Devuelve NodoSeccion
        if (estudiante == null) return "Error: El estudiante no existe.";
        if (seccion == null) return "Error: La sección no existe.";
        if (seccion.getEstudiantesMatriculados().contains(estudiante)) {
            return "Error: El estudiante ya está matriculado en esta sección.";
        }
        seccion.matricularEstudiante(estudiante);
        return "Estudiante " + estudiante.getNombre() + " matriculado exitosamente.";
    }
    
    public String registrarParticipacion(String codigoEstudiante) {
        NodoEstudiante estudiante = db.buscarEstudiantePorCodigo(codigoEstudiante); // Devuelve NodoEstudiante
        if (estudiante == null) {
            return "Error: No se encontró al estudiante.";
        }
        if (estudiante.getParticipaciones() >= NodoEstudiante.LIMITE_PARTICIPACIONES) {
            return "Acción denegada: Límite de 4 participaciones alcanzado.";
        }
        estudiante.incrementarParticipacion();
        return "Participación registrada. Total actual: " + estudiante.getParticipaciones();
    }
    
    public String procesarParticipaciones(String idSeccion, String idEvaluacion) {
        NodoSeccion seccion = db.buscarSeccionPorId(idSeccion);
        if (seccion == null) return "Error: La sección no existe.";
        if (seccion.getEvaluacionesProcesadas().contains(idEvaluacion.toUpperCase())) {
            return "Error: La evaluación ya ha sido procesada.";
        }

        // Búsqueda lineal: Filtrar estudiantes con participaciones > 0
        ArrayList<NodoEstudiante> participantes = new ArrayList<>();
        for (NodoEstudiante e : seccion.getEstudiantesMatriculados()) {
            if (e.getParticipaciones() > 0) {
                participantes.add(e);
            }
        }

        if (participantes.isEmpty()) {
            seccion.getEvaluacionesProcesadas().add(idEvaluacion.toUpperCase());
            return "No hay estudiantes con participaciones registradas para procesar.";
        }
        
        // --- APLICANDO EL ALGORITMO DE ORDENAMIENTO BURBUJA ---
        ordenamientoBurbuja(participantes);
        
        // Asignación de puntos según ranking
        // El estudiante con más participaciones recibe el máximo (20 puntos)
        // Los demás reciben puntos proporcionales
        int maxPuntos = 20;
        int totalParticipantes = participantes.size();
        StringBuilder resultado = new StringBuilder();
        resultado.append("PROCESAMIENTO DE PARTICIPACIONES\n");
        resultado.append("===============================\n");
        resultado.append("Sección: ").append(seccion.getId()).append("\n");
        resultado.append("Evaluación: ").append(idEvaluacion.toUpperCase()).append("\n\n");
        resultado.append("RANKING DE ESTUDIANTES:\n");
        resultado.append("------------------------\n");
        
        for (int i = 0; i < participantes.size(); i++) {
            NodoEstudiante estudiante = participantes.get(i);
            // Asignar puntos: el primero recibe 20, y disminuye según posición
            int puntosAsignados;
            if (totalParticipantes == 1) {
                puntosAsignados = maxPuntos;
            } else {
                // Distribución proporcional: 20, 18, 16, 14, 12, 10, 8, 6, 4, 2...
                puntosAsignados = Math.max(2, maxPuntos - (i * 2));
            }
            
            // Guardar puntos usando la estructura de datos
            seccion.asignarPuntos(estudiante.getCodigo(), idEvaluacion, puntosAsignados);
            
            resultado.append(String.format("%d. %s (%s) - %d participación(es) → %d puntos\n",
                i + 1,
                estudiante.getNombre(),
                estudiante.getCodigo(),
                estudiante.getParticipaciones(),
                puntosAsignados));
            
            // Reiniciar participaciones a 0
            estudiante.setParticipaciones(0);
        }
        
        resultado.append("\nTotal de estudiantes procesados: ").append(totalParticipantes);
        resultado.append("\nParticipaciones reiniciadas para todos los estudiantes.");
        
        // Marcar evaluación como procesada
        seccion.getEvaluacionesProcesadas().add(idEvaluacion.toUpperCase());
        return resultado.toString();
    }

    // --- NUEVOS 6 REQUERIMIENTOS ---

    // REQ 1: REGISTRAR ESTUDIANTE
    // Validación contra datos en inicializarDatos() - Búsqueda lineal
    public String registrarEstudiante(String codigo, String nombre, String carrera, int ciclo) {
        // Búsqueda lineal: Buscar estudiante en la base de datos
        NodoEstudiante estudiante = db.buscarEstudiantePorCodigo(codigo);
        
        if (estudiante == null) {
            return "Error: El estudiante no existe en la base de datos.";
        }
        
        // Validar que los datos coincidan con los de inicializarDatos()
        if (!estudiante.getNombre().equalsIgnoreCase(nombre)) {
            return "Error: El nombre no coincide con los datos registrados.";
        }
        if (!estudiante.getCarrera().equalsIgnoreCase(carrera)) {
            return "Error: La carrera no coincide con los datos registrados.";
        }
        if (estudiante.getCiclo() != ciclo) {
            return "Error: El ciclo no coincide con los datos registrados.";
        }
        
        return "Estudiante " + nombre + " validado y registrado exitosamente.";
    }

    // REQ 2: REGISTRAR DOCENTE
    // Validación contra datos en inicializarDatos() - Búsqueda lineal
    public String registrarDocente(String id, String nombre) {
        // Búsqueda lineal: Buscar docente en la base de datos
        NodoDocente docente = db.buscarDocentePorId(id);
        
        if (docente == null) {
            return "Error: El docente no existe en la base de datos.";
        }
        
        // Validar que los datos coincidan con los de inicializarDatos()
        if (!docente.getNombre().equalsIgnoreCase(nombre)) {
            return "Error: El nombre no coincide con los datos registrados.";
        }
        
        return "Docente " + nombre + " validado y registrado exitosamente.";
    }

    // REQ 3: REGISTRAR CURSO
    // Validación contra datos en inicializarDatos() - Búsqueda lineal
    public String registrarCurso(String codigo, String nombre, String tipo, int capacidad) {
        // Búsqueda lineal: Buscar curso en la base de datos
        NodoCurso curso = db.buscarCursoPorCodigo(codigo);
        
        if (curso == null) {
            return "Error: El curso no existe en la base de datos.";
        }
        
        // Validar que los datos coincidan con los de inicializarDatos()
        if (!curso.getNombre().equalsIgnoreCase(nombre)) {
            return "Error: El nombre no coincide con los datos registrados.";
        }
        if (!curso.getTipo().equalsIgnoreCase(tipo)) {
            return "Error: El tipo no coincide con los datos registrados.";
        }
        if (curso.getCapacidadMaxima() != capacidad) {
            return "Error: La capacidad no coincide con los datos registrados.";
        }
        
        return "Curso " + nombre + " validado y registrado exitosamente.";
    }

    // REQ 4: REGISTRAR SECCIÓN
    public String registrarSeccion(String codigoCurso, String idDocente, String periodo) {
        NodoCurso curso = db.buscarCursoPorCodigo(codigoCurso);
        NodoDocente docente = db.buscarDocentePorId(idDocente);
        
        if (curso == null) return "Error: El curso no existe.";
        if (docente == null) return "Error: El docente no existe.";

        // Feedback: Generación de ID automático
        Random rand = new Random();
        String idGenerado;
        do {
            idGenerado = curso.getCodigo() + "-" + periodo.charAt(0) + "-" + (rand.nextInt(900) + 100);
        } while (db.buscarSeccionPorId(idGenerado) != null); // Valida unicidad
        
        NodoSeccion nuevo = new NodoSeccion(idGenerado, curso, docente, periodo);
        db.listaSecciones.agregarAlFinal(nuevo);
        return "Sección " + idGenerado + " creada exitosamente.";
    }

    // REQ 5: DEFINIR ESTRUCTURA DE EVALUACIONES
    public String definirEvaluacion(String codigoCurso, String nombreEvaluacion) {
        NodoCurso curso = db.buscarCursoPorCodigo(codigoCurso);
        if (curso == null) return "Error: El curso no existe.";
        
        curso.agregarEvaluacion(nombreEvaluacion);
        return "Evaluación '" + nombreEvaluacion + "' añadida al curso " + curso.getNombre();
    }

    // REQ 6: AJUSTAR MANUALMENTE PARTICIPACIONES
    public String ajustarParticipaciones(String idSeccion, String idEvaluacion, String codigoEstudiante, int nuevoPuntaje) {
        NodoSeccion seccion = db.buscarSeccionPorId(idSeccion);
        if (seccion == null) return "Error: La sección no existe.";
        
        NodoEstudiante estudiante = db.buscarEstudiantePorCodigo(codigoEstudiante);
        if (estudiante == null) return "Error: El estudiante no existe.";
        
        // Búsqueda lineal: Verificar que el estudiante esté matriculado en la sección
        boolean estaMatriculado = false;
        for (NodoEstudiante e : seccion.getEstudiantesMatriculados()) {
            if (e.getCodigo().equalsIgnoreCase(codigoEstudiante)) {
                estaMatriculado = true;
                break;
            }
        }
        
        if (!estaMatriculado) {
            return "Error: El estudiante no está matriculado en esta sección.";
        }
        
        // Validar rango de puntos (0-20)
        if (nuevoPuntaje < 0 || nuevoPuntaje > 20) {
            return "Error: El puntaje debe estar entre 0 y 20.";
        }
        
        // Obtener puntaje anterior (puede ser null si no había puntos asignados)
        Integer puntajeAnterior = seccion.obtenerPuntos(codigoEstudiante, idEvaluacion);
        if (puntajeAnterior == null) {
            puntajeAnterior = 0;
        }
        
        // Asignar el nuevo puntaje usando la estructura de datos
        seccion.asignarPuntos(codigoEstudiante, idEvaluacion, nuevoPuntaje);
        
        return String.format("Ajuste manual registrado exitosamente.\n" +
            "Estudiante: %s (%s)\n" +
            "Evaluación: %s\n" +
            "Puntaje anterior: %d\n" +
            "Nuevo puntaje: %d",
            estudiante.getNombre(),
            codigoEstudiante,
            idEvaluacion.toUpperCase(),
            puntajeAnterior,
            nuevoPuntaje);
    }
}