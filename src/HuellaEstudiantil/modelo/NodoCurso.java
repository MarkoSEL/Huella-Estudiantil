package HuellaEstudiantil.modelo;

import java.util.ArrayList;

// Nueva clase para "Registrar Curso", aplicando el feedback
public class NodoCurso {
    private String codigo;
    private String nombre;
    private String tipo; 
    private int capacidadMaxima; 
    private ArrayList<String> estructuraEvaluaciones;
    private NodoCurso sgte; 

    public NodoCurso(String codigo, String nombre, String tipo, int capacidadMaxima) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidadMaxima = capacidadMaxima;
        this.estructuraEvaluaciones = new ArrayList<>();
        this.sgte = null;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public NodoCurso getSgte() { return sgte; }
    public void setSgte(NodoCurso sgte) { this.sgte = sgte; }
    public ArrayList<String> getEstructuraEvaluaciones() { return estructuraEvaluaciones; }
    public void agregarEvaluacion(String nombreEval) {
        this.estructuraEvaluaciones.add(nombreEval);
    }
}