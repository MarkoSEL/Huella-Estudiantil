package HuellaEstudiantil.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class NodoSeccion {
    private String id; 
    private NodoCurso curso;
    private NodoDocente docente;
    private String periodo;
    
    private ArrayList<NodoEstudiante> estudiantesMatriculados;
    private ArrayList<LocalDate> sesionesDeClase;
    private HashSet<String> evaluacionesProcesadas;
    // Estructura para guardar puntos: clave = "codigoEstudiante-evaluacion", valor = puntaje
    private HashMap<String, Integer> puntosPorEvaluacion;
    // Estructura para rastrear participaciones por sesión/evaluación: clave = "codigoEstudiante-sesion-evaluacion", valor = contador
    private HashMap<String, Integer> participacionesPorSesionEvaluacion; 
    
    private NodoSeccion sgte; 

    public NodoSeccion(String id, NodoCurso curso, NodoDocente docente, String periodo) {
        this.id = id;
        this.curso = curso;
        this.docente = docente;
        this.periodo = periodo;
        this.estudiantesMatriculados = new ArrayList<>();
        this.sesionesDeClase = new ArrayList<>();
        this.evaluacionesProcesadas = new HashSet<>();
        this.puntosPorEvaluacion = new HashMap<>();
        this.participacionesPorSesionEvaluacion = new HashMap<>();
        this.sgte = null;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public NodoCurso getCurso() { return curso; }
    public NodoDocente getDocente() { return docente; }
    public String getPeriodo() { return periodo; }
    public NodoSeccion getSgte() { return sgte; }
    public void setSgte(NodoSeccion sgte) { this.sgte = sgte; }
    public HashSet<String> getEvaluacionesProcesadas() { return this.evaluacionesProcesadas; } 
    public ArrayList<NodoEstudiante> getEstudiantesMatriculados() { return estudiantesMatriculados; }
    public ArrayList<LocalDate> getSesionesDeClase() { return sesionesDeClase; }
    
    public void matricularEstudiante(NodoEstudiante estudiante) {
        this.estudiantesMatriculados.add(estudiante);
    }
    
    // Métodos para gestionar puntos por evaluación
    public void asignarPuntos(String codigoEstudiante, String evaluacion, int puntos) {
        String clave = codigoEstudiante + "-" + evaluacion.toUpperCase();
        this.puntosPorEvaluacion.put(clave, puntos);
    }
    
    public Integer obtenerPuntos(String codigoEstudiante, String evaluacion) {
        String clave = codigoEstudiante + "-" + evaluacion.toUpperCase();
        return this.puntosPorEvaluacion.get(clave);
    }
    
    // Métodos para gestionar participaciones por sesión/evaluación
    public int obtenerParticipacionesPorSesionEvaluacion(String codigoEstudiante, String sesion, String evaluacion) {
        String clave = codigoEstudiante + "-" + sesion + "-" + evaluacion.toUpperCase();
        Integer contador = this.participacionesPorSesionEvaluacion.get(clave);
        return (contador == null) ? 0 : contador;
    }
    
    public void incrementarParticipacionPorSesionEvaluacion(String codigoEstudiante, String sesion, String evaluacion) {
        String clave = codigoEstudiante + "-" + sesion + "-" + evaluacion.toUpperCase();
        int contador = obtenerParticipacionesPorSesionEvaluacion(codigoEstudiante, sesion, evaluacion);
        this.participacionesPorSesionEvaluacion.put(clave, contador + 1);
    } 
}