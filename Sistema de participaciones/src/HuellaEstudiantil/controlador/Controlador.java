package HuellaEstudiantil.controlador;

import HuellaEstudiantil.datos.BaseDeDatos;
import HuellaEstudiantil.modelo.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashSet;
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

    // Lista fija de feriados (ejemplo)
    private HashSet<LocalDate> obtenerFeriados() {
        HashSet<LocalDate> feriados = new HashSet<>();
        // Feriados de ejemplo - ajustar según necesidad
        feriados.add(LocalDate.of(2025, 3, 25));   // Año Nuevo
        feriados.add(LocalDate.of(2025, 5, 1));   // Día del Trabajo
        feriados.add(LocalDate.of(2025, 7, 28));  // Independencia
        feriados.add(LocalDate.of(2025, 7, 29));  // Independencia
        feriados.add(LocalDate.of(2025, 8, 30));  // Santa Rosa de Lima
        feriados.add(LocalDate.of(2025, 10, 8));  // Combate de Angamos
        feriados.add(LocalDate.of(2025, 11, 1));  // Todos los Santos
        feriados.add(LocalDate.of(2025, 12, 8));  // Inmaculada Concepción
        feriados.add(LocalDate.of(2025, 12, 25)); // Navidad
        return feriados;
    }
    
    private boolean esFeriado(LocalDate fecha, HashSet<LocalDate> feriados) {
        return feriados.contains(fecha);
    }
    
    // 2. GENERAR SESIONES DE CLASE
    public String generarSesiones(String idSeccion, LocalDate fechaInicio, DayOfWeek... dias) {
        // Búsqueda Lineal: Buscar sección
        NodoSeccion seccion = db.buscarSeccionPorId(idSeccion);
        if (seccion == null) {
            return "Error: El ID de sección no se encuentra.";
        }
        
        // Validar datos de entrada
        if (fechaInicio == null) {
            return "Error: No se ingresó la fecha de inicio.";
        }
        if (dias == null || dias.length == 0) {
            return "Error: No se ingresaron los días de la semana.";
        }
        
        // Limpiar sesiones previas
        seccion.getSesionesDeClase().clear();
        
        // Detectar periodo: Verano, Marzo, Agosto
        String periodo = seccion.getPeriodo().toLowerCase();
        int totalSesiones;
        if (periodo.contains("verano")) {
            totalSesiones = 9;
        } else {
            totalSesiones = 18; // Marzo o Agosto (periodo normal)
        }
        
        HashSet<LocalDate> feriados = obtenerFeriados();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder resultado = new StringBuilder();
        resultado.append("SESIONES DE CLASE GENERADAS\n");
        resultado.append("============================\n");
        resultado.append("Sección: ").append(seccion.getId()).append("\n");
        resultado.append("Curso: ").append(seccion.getCurso().getNombre()).append("\n");
        resultado.append("Período: ").append(seccion.getPeriodo()).append("\n");
        resultado.append("Fecha de inicio: ").append(fechaInicio.format(formatoFecha)).append("\n");
        resultado.append("Días de clase: ");
        
        for (int i = 0; i < dias.length; i++) {
            resultado.append(dias[i].getDisplayName(TextStyle.FULL, Locale.of("es", "ES")));
            if (i < dias.length - 1) resultado.append(", ");
        }
        resultado.append("\n");
        resultado.append("Total de sesiones a generar: ").append(totalSesiones).append("\n\n");
        resultado.append("SESIONES:\n");
        resultado.append("----------\n");
        
        int numeroSesion = 1;
        LocalDate fechaActual = fechaInicio;
        int semanas = 0;
        
        // Generar sesiones hasta alcanzar el total requerido
        while (numeroSesion <= totalSesiones) {
            // Iterar por cada día de la semana especificado
            for (DayOfWeek dia : dias) {
                if (numeroSesion > totalSesiones) break;
                
                // Calcular la fecha del día específico en la semana actual
                int diasHastaDia = dia.getValue() - fechaActual.getDayOfWeek().getValue();
                if (diasHastaDia < 0) diasHastaDia += 7;
                LocalDate fechaSesion = fechaActual.plusDays(diasHastaDia);
                
                // Agregar a la lista de sesiones (aunque sea feriado)
                seccion.getSesionesDeClase().add(fechaSesion);
                
                // Verificar si es feriado para mostrar "A reprogramar"
                if (esFeriado(fechaSesion, feriados)) {
                    // Mostrar como "A reprogramar" cuando es feriado
                    resultado.append(String.format("Sesión %02d: A reprogramar %s\n", 
                        numeroSesion++, 
                        fechaSesion.format(formatoFecha)));
                } else {
                    // Mostrar día de la semana normal cuando no es feriado
                    String diaNombre = fechaSesion.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.of("es", "ES"));
                    resultado.append(String.format("Sesión %02d: %s %s\n", 
                        numeroSesion++, 
                        diaNombre,
                        fechaSesion.format(formatoFecha)));
                }
            }
            
            // Avanzar a la siguiente semana
            semanas++;
            fechaActual = fechaInicio.plusWeeks(semanas);
        }
        
        resultado.append("\nTotal de sesiones generadas: ").append(seccion.getSesionesDeClase().size());
        return resultado.toString();
    }

    // 1. MATRICULAR ESTUDIANTE EN SECCIÓN
    public String matricularEstudiante(String codigoEstudiante, String idSeccion) {
        // Búsqueda Lineal: Buscar estudiante
        NodoEstudiante estudiante = db.buscarEstudiantePorCodigo(codigoEstudiante);
        if (estudiante == null) {
            return "Error: El código de estudiante no se encuentra.";
        }
        
        // Búsqueda Lineal: Buscar sección
        NodoSeccion seccion = db.buscarSeccionPorId(idSeccion);
        if (seccion == null) {
            return "Error: El ID de sección no se encuentra.";
        }
        
        // Validar duplicados usando búsqueda lineal en el ArrayList
        for (NodoEstudiante e : seccion.getEstudiantesMatriculados()) {
            if (e.getCodigo().equalsIgnoreCase(codigoEstudiante)) {
                return "Error: El estudiante ya se encuentra matriculado en esta sección.";
            }
        }
        
        // Validar capacidad máxima
        int capacidadMaxima = seccion.getCurso().getCapacidadMaxima();
        int estudiantesMatriculados = seccion.getEstudiantesMatriculados().size();
        if (estudiantesMatriculados >= capacidadMaxima) {
            return "Error: La sección ha alcanzado su capacidad máxima.\n" +
                   "Estudiantes matriculados: " + estudiantesMatriculados + "\n" +
                   "Capacidad máxima: " + capacidadMaxima + " estudiantes.";
        }
        
        // Matricular estudiante
        seccion.matricularEstudiante(estudiante);
        return "Estudiante " + estudiante.getNombre() + " (" + codigoEstudiante + 
               ") matriculado exitosamente en la sección " + idSeccion + ".";
    }
    
    // 3. REGISTRAR PARTICIPACIÓN
    public String registrarParticipacion(String idSeccion, String idEvaluacion, String semana, String sesion, String codigoEstudiante) {
        // Búsqueda Lineal: Buscar sección
        NodoSeccion seccion = db.buscarSeccionPorId(idSeccion);
        if (seccion == null) {
            return "Error: El ID de sección no se encuentra.";
        }
        
        // Búsqueda Lineal: Buscar estudiante
        NodoEstudiante estudiante = db.buscarEstudiantePorCodigo(codigoEstudiante);
        if (estudiante == null) {
            return "Error: El código de estudiante no se encuentra.";
        }
        
        // Validar que el estudiante esté matriculado en la sección
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
        
        // Validar límite de 4 participaciones por sesión/evaluación
        int participacionesActuales = seccion.obtenerParticipacionesPorSesionEvaluacion(
            codigoEstudiante, sesion, idEvaluacion);
        if (participacionesActuales >= 4) {
            return "Error: El estudiante ya ha alcanzado el límite de 4 participaciones para esta sesión/evaluación.";
        }
        
        // Registrar participación
        seccion.incrementarParticipacionPorSesionEvaluacion(codigoEstudiante, sesion, idEvaluacion);
        estudiante.incrementarParticipacion(); // Incrementar contador general también
        
        return "Participación registrada exitosamente.\n" +
               "Estudiante: " + estudiante.getNombre() + " (" + codigoEstudiante + ")\n" +
               "Sección: " + idSeccion + "\n" +
               "Evaluación: " + idEvaluacion.toUpperCase() + "\n" +
               "Semana: " + semana + "\n" +
               "Sesión: " + sesion + "\n" +
               "Participaciones en esta sesión: " +
               (participacionesActuales + 1) + "/4";
    }
    
    // 4. PROCESAR PARTICIPACIONES
    public String procesarParticipaciones(String idSeccion, String idEvaluacion) {
        // Búsqueda Lineal: Buscar sección
        NodoSeccion seccion = db.buscarSeccionPorId(idSeccion);
        if (seccion == null) {
            return "Error: La sección no existe.";
        }
        
        // Validar que la evaluación no haya sido procesada
        if (seccion.getEvaluacionesProcesadas().contains(idEvaluacion.toUpperCase())) {
            return "Error: La evaluación ya ha sido procesada (revisando el HashSet).";
        }

        // Búsqueda lineal: Filtrar estudiantes con participaciones > 0
        ArrayList<NodoEstudiante> participantes = new ArrayList<>();
        for (NodoEstudiante e : seccion.getEstudiantesMatriculados()) {
            if (e.getParticipaciones() > 0) {
                participantes.add(e);
            }
        }

        if (participantes.isEmpty()) {
            // Marcar como procesada aunque no haya participaciones
            seccion.getEvaluacionesProcesadas().add(idEvaluacion.toUpperCase());
            return "No hay estudiantes con participaciones registradas para procesar.";
        }
        
        // --- APLICANDO EL ALGORITMO DE ORDENAMIENTO BURBUJA ---
        ordenamientoBurbuja(participantes);
        
        // Asignación de puntos: 3, 2, 1 manejando empates
        StringBuilder resultado = new StringBuilder();
        resultado.append("PROCESAMIENTO DE PARTICIPACIONES\n");
        resultado.append("===============================\n");
        resultado.append("Sección: ").append(seccion.getId()).append("\n");
        resultado.append("Evaluación: ").append(idEvaluacion.toUpperCase()).append("\n\n");
        resultado.append("RANKING DE ESTUDIANTES:\n");
        resultado.append("------------------------\n");
        
        int puntosDisponibles = 3;
        int posicion = 1;
        int participacionesAnteriores = -1;
        
        for (int i = 0; i < participantes.size(); i++) {
            NodoEstudiante estudiante = participantes.get(i);
            int participacionesActuales = estudiante.getParticipaciones();
            
            // Si el número de participaciones cambia, ajustar los puntos
            if (participacionesActuales != participacionesAnteriores) {
                // Si hay más de 3 estudiantes, asignar 3 al primero, 2 al segundo, 1 al resto
                if (i == 0) {
                    puntosDisponibles = 3;
                } else if (i == 1) {
                    puntosDisponibles = 2;
                } else {
                    puntosDisponibles = 1;
                }
            }
            // Si hay empate (mismo número de participaciones), mantener el mismo puntaje
            // (esto ya se maneja porque participacionesActuales == participacionesAnteriores)
            
            int puntosAsignados = puntosDisponibles;
            
            // Guardar puntos usando la estructura de datos
            seccion.asignarPuntos(estudiante.getCodigo(), idEvaluacion, puntosAsignados);
            
            resultado.append(String.format("%d. %s (%s) - %d participación(es) → %d puntos\n",
                posicion++,
                estudiante.getNombre(),
                estudiante.getCodigo(),
                participacionesActuales,
                puntosAsignados));
            
            participacionesAnteriores = participacionesActuales;
            
            // Reiniciar participaciones a 0
            estudiante.setParticipaciones(0);
        }
        
        resultado.append("\nTotal de estudiantes procesados: ").append(participantes.size());
        resultado.append("\nParticipaciones reiniciadas para todos los estudiantes.");
        
        // Marcar evaluación como procesada
        seccion.getEvaluacionesProcesadas().add(idEvaluacion.toUpperCase());
        return resultado.toString();
    }

    // 5. REGISTRAR ESTUDIANTE
    public String registrarEstudiante(String nombre, String carrera, int ciclo) {
        // Validar campos vacíos
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El campo Nombre está vacío.";
        }
        if (carrera == null || carrera.trim().isEmpty()) {
            return "Error: El campo Carrera está vacío.";
        }
        
        // Validar ciclo (1-12)
        if (ciclo < 1 || ciclo > 12) {
            return "Error: El Ciclo debe ser un número válido entre 1 y 12.";
        }
        
        // Búsqueda Lineal: Buscar el estudiante en la base de datos precargada
        NodoEstudiante estudianteExistente = db.buscarEstudiantePorNombre(nombre);
        
        if (estudianteExistente != null) {
            // El estudiante existe en la BD precargada, verificar si ya fue registrado
            if (db.estaEstudianteRegistrado(estudianteExistente.getCodigo())) {
                return "Error: El estudiante ya se encuentra registrado.";
            }
            
            // Validar que los datos coincidan con los de la BD precargada
            if (!estudianteExistente.getCarrera().equalsIgnoreCase(carrera.trim())) {
                return "Error: La carrera no coincide con el estudiante existente en la base de datos.";
            }
            if (estudianteExistente.getCiclo() != ciclo) {
                return "Error: El ciclo no coincide con el estudiante existente en la base de datos.";
            }
            
            // Datos coinciden, marcar como registrado (por código)
            db.marcarEstudianteComoRegistrado(estudianteExistente.getCodigo());
            return "Estudiante registrado exitosamente.\n" +
                   "Código: " + estudianteExistente.getCodigo() + "\n" +
                   "Nombre: " + nombre + "\n" +
                   "Carrera: " + carrera + "\n" +
                   "Ciclo: " + ciclo;
        } else {
            // El estudiante no existe en la BD precargada, crear uno nuevo
            // Generar código automático único (ej. "U00006")
            String codigoGenerado = null;
            int contador = 1;
            do {
                codigoGenerado = String.format("U%05d", contador);
                contador++;
                // Búsqueda Lineal: Verificar que el código no exista
                // Si existe, continuar el bucle
            } while (db.buscarEstudiantePorCodigo(codigoGenerado) != null && contador < 99999);
            
            // Crear nuevo nodo y añadirlo a la lista enlazada
            NodoEstudiante nuevoEstudiante = new NodoEstudiante(codigoGenerado, nombre.trim(), carrera.trim(), ciclo);
            db.listaEstudiantes.agregarAlFinal(nuevoEstudiante);
            db.marcarEstudianteComoRegistrado(codigoGenerado);
            
            return "Estudiante registrado exitosamente.\n" +
                   "Código generado: " + codigoGenerado + "\n" +
                   "Nombre: " + nombre + "\n" +
                   "Carrera: " + carrera + "\n" +
                   "Ciclo: " + ciclo;
        }
    }

    // 6. REGISTRAR DOCENTE
    public String registrarDocente(String id, String nombre) {
        // Validar campos vacíos
        if (id == null || id.trim().isEmpty()) {
            return "Error: El campo ID está vacío.";
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El campo Nombre está vacío.";
        }
        
        // Verificar si el docente ya fue registrado anteriormente
        if (db.estaDocenteRegistrado(id)) {
            return "Error: El ID del docente ya existe.";
        }
        
        // Búsqueda Lineal: Buscar el docente en la base de datos precargada
        NodoDocente docenteExistente = db.buscarDocentePorId(id);
        
        if (docenteExistente != null) {
            // El docente existe en la BD precargada, validar que los datos coincidan
            if (!docenteExistente.getNombre().equalsIgnoreCase(nombre.trim())) {
                return "Error: El nombre no coincide con el docente existente en la base de datos.";
            }
            
            // Datos coinciden, marcar como registrado
            db.marcarDocenteComoRegistrado(id);
            return "Docente registrado exitosamente.\n" +
                   "ID: " + id + "\n" +
                   "Nombre: " + nombre;
        } else {
            // El docente no existe en la BD precargada, crear uno nuevo
            NodoDocente nuevoDocente = new NodoDocente(id.trim(), nombre.trim());
            db.listaDocentes.agregarAlFinal(nuevoDocente);
            db.marcarDocenteComoRegistrado(id);
            
            return "Docente registrado exitosamente.\n" +
                   "ID: " + id + "\n" +
                   "Nombre: " + nombre;
        }
    }

    // 7. REGISTRAR CURSO
    public String registrarCurso(String codigo, String nombre, String tipo, int capacidad) {
        // Validar campos vacíos
        if (codigo == null || codigo.trim().isEmpty()) {
            return "Error: El campo Código está vacío.";
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Error: El campo Nombre está vacío.";
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            return "Error: El campo Tipo está vacío.";
        }
        
        // Validar capacidad positiva
        if (capacidad <= 0) {
            return "Error: La Capacidad Máxima debe ser un número positivo.";
        }
        
        // Verificar si el curso ya fue registrado anteriormente
        if (db.estaCursoRegistrado(codigo)) {
            return "Error: El código del curso ya existe.";
        }
        
        // Búsqueda Lineal: Buscar el curso en la base de datos precargada
        NodoCurso cursoExistente = db.buscarCursoPorCodigo(codigo);
        
        if (cursoExistente != null) {
            // El curso existe en la BD precargada, validar que los datos coincidan
            if (!cursoExistente.getNombre().equalsIgnoreCase(nombre.trim())) {
                return "Error: El nombre no coincide con el curso existente en la base de datos.";
            }
            if (!cursoExistente.getTipo().equalsIgnoreCase(tipo.trim())) {
                return "Error: El tipo no coincide con el curso existente en la base de datos.";
            }
            if (cursoExistente.getCapacidadMaxima() != capacidad) {
                return "Error: La capacidad no coincide con el curso existente en la base de datos.";
            }
            
            // Datos coinciden, marcar como registrado
            db.marcarCursoComoRegistrado(codigo);
            return "Curso registrado exitosamente.\n" +
                   "Código: " + codigo + "\n" +
                   "Nombre: " + nombre + "\n" +
                   "Tipo: " + tipo + "\n" +
                   "Capacidad Máxima: " + capacidad;
        } else {
            // El curso no existe en la BD precargada, crear uno nuevo
            NodoCurso nuevoCurso = new NodoCurso(codigo.trim(), nombre.trim(), tipo.trim(), capacidad);
            db.listaCursos.agregarAlFinal(nuevoCurso);
            db.marcarCursoComoRegistrado(codigo);
            
            return "Curso registrado exitosamente.\n" +
                   "Código: " + codigo + "\n" +
                   "Nombre: " + nombre + "\n" +
                   "Tipo: " + tipo + "\n" +
                   "Capacidad Máxima: " + capacidad;
        }
    }

    // 8. REGISTRAR SECCIÓN
    public String registrarSeccion(String codigoCurso, String idDocente, String periodo) {
        // Validar campos vacíos
        if (codigoCurso == null || codigoCurso.trim().isEmpty()) {
            return "Error: El campo Código de Curso está vacío.";
        }
        if (idDocente == null || idDocente.trim().isEmpty()) {
            return "Error: El campo ID de Docente está vacío.";
        }
        if (periodo == null || periodo.trim().isEmpty()) {
            return "Error: El campo Periodo está vacío.";
        }
        
        // Búsqueda Lineal: Buscar curso
        NodoCurso curso = db.buscarCursoPorCodigo(codigoCurso);
        if (curso == null) {
            return "Error: El código de curso no existe.";
        }
        
        // Búsqueda Lineal: Buscar docente
        NodoDocente docente = db.buscarDocentePorId(idDocente);
        if (docente == null) {
            return "Error: El ID de docente no existe.";
        }
        
        // Búsqueda Lineal: Buscar sección por curso, docente y periodo en la BD precargada
        NodoSeccion seccionExistente = db.buscarSeccionPorDatos(codigoCurso, idDocente, periodo.trim());
        
        if (seccionExistente != null) {
            // La sección existe en la BD precargada, verificar si ya fue registrada
            if (db.estaSeccionRegistrada(seccionExistente.getId())) {
                return "Error: El ID de sección ya existe.";
            }
            
            // Datos coinciden, marcar como registrada (por ID)
            db.marcarSeccionComoRegistrada(seccionExistente.getId());
            return "Sección registrada exitosamente.\n" +
                   "ID: " + seccionExistente.getId() + "\n" +
                   "Curso: " + curso.getNombre() + " (" + codigoCurso + ")\n" +
                   "Docente: " + docente.getNombre() + " (" + idDocente + ")\n" +
                   "Periodo: " + periodo;
        } else {
            // La sección no existe en la BD precargada, crear una nueva con ID generado
            // Generar ID automático único (ej. "CS101-M-451")
            Random rand = new Random();
            String idGenerado;
            do {
                char periodoChar = periodo.trim().charAt(0);
                int numero = rand.nextInt(900) + 100;
                idGenerado = curso.getCodigo() + "-" + periodoChar + "-" + numero;
                // Búsqueda Lineal: Validar unicidad
            } while (db.buscarSeccionPorId(idGenerado) != null);
            
            // Crear nuevo nodo y añadirlo a la lista enlazada
            NodoSeccion nuevo = new NodoSeccion(idGenerado, curso, docente, periodo.trim());
            db.listaSecciones.agregarAlFinal(nuevo);
            db.marcarSeccionComoRegistrada(idGenerado);
            
            return "Sección registrada exitosamente.\n" +
                   "ID generado: " + idGenerado + "\n" +
                   "Curso: " + curso.getNombre() + " (" + codigoCurso + ")\n" +
                   "Docente: " + docente.getNombre() + " (" + idDocente + ")\n" +
                   "Periodo: " + periodo;
        }
    }

    // 9. DEFINIR ESTRUCTURA DE EVALUACIONES POR CURSO
    public String definirEvaluacion(String codigoCurso, String nombreEvaluacion) {
        // Validar campos vacíos
        if (codigoCurso == null || codigoCurso.trim().isEmpty()) {
            return "Error: El campo Código de Curso está vacío.";
        }
        if (nombreEvaluacion == null || nombreEvaluacion.trim().isEmpty()) {
            return "Error: El campo Nombre de Evaluación está vacío.";
        }
        
        // Búsqueda Lineal: Buscar curso
        NodoCurso curso = db.buscarCursoPorCodigo(codigoCurso);
        if (curso == null) {
            return "Error: El código de curso no existe.";
        }
        
        // Validar que la evaluación no exista (búsqueda lineal en ArrayList)
        for (String eval : curso.getEstructuraEvaluaciones()) {
            if (eval.equalsIgnoreCase(nombreEvaluacion.trim())) {
                return "Error: La evaluación '" + nombreEvaluacion + "' ya existe en la lista de evaluaciones de este curso.";
            }
        }
        
        // Agregar evaluación
        curso.agregarEvaluacion(nombreEvaluacion.trim());
        
        return "Evaluación agregada exitosamente.\n" +
               "Curso: " + curso.getNombre() + " (" + codigoCurso + ")\n" +
               "Evaluación: " + nombreEvaluacion;
    }

    // 10. AJUSTAR MANUALMENTE EL CONTEO DE PARTICIPACIONES
    public String ajustarParticipaciones(String idSeccion, String idEvaluacion, String codigoEstudiante, int nuevoPuntaje) {
        // Búsqueda Lineal: Buscar sección
        NodoSeccion seccion = db.buscarSeccionPorId(idSeccion);
        if (seccion == null) {
            return "Error: La sección no existe.";
        }
        
        // Búsqueda Lineal: Buscar estudiante
        NodoEstudiante estudiante = db.buscarEstudiantePorCodigo(codigoEstudiante);
        if (estudiante == null) {
            return "Error: El estudiante no existe.";
        }
        
        // Validar que la evaluación haya sido procesada
        if (!seccion.getEvaluacionesProcesadas().contains(idEvaluacion.toUpperCase())) {
            return "Error: La evaluación '" + idEvaluacion + "' no ha sido procesada aún.";
        }
        
        // Validar que el nuevoPuntaje sea un número válido
        if (nuevoPuntaje < 0) {
            return "Error: El nuevo puntaje debe ser un número válido (no negativo).";
        }
        
        // Obtener puntaje anterior (puede ser null si no había puntos asignados)
        Integer puntajeAnterior = seccion.obtenerPuntos(codigoEstudiante, idEvaluacion);
        if (puntajeAnterior == null) {
            puntajeAnterior = 0;
        }
        
        // Simular guardado del nuevo puntaje
        seccion.asignarPuntos(codigoEstudiante, idEvaluacion, nuevoPuntaje);
        
        return "Ajuste manual registrado exitosamente.\n" +
               "Estudiante: " + estudiante.getNombre() + " (" + codigoEstudiante + ")\n" +
               "Sección: " + idSeccion + "\n" +
               "Evaluación: " + idEvaluacion.toUpperCase() + "\n" +
               "Puntaje anterior: " + puntajeAnterior + "\n" +
               "Nuevo puntaje: " + nuevoPuntaje;
    }
}