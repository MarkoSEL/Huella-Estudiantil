package HuellaEstudiantil.modelo;

// Se ha renombrado a "NodoEstudiante" y se añaden los campos del feedback
public class NodoEstudiante {
    
    public static final int LIMITE_PARTICIPACIONES = 4;
    
    private String codigo;
    private String nombre;
    private String carrera; 
    private int ciclo;
    private int participaciones;
    private NodoEstudiante sgte; 

    public NodoEstudiante(String codigo, String nombre, String carrera, int ciclo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.carrera = carrera;
        this.ciclo = ciclo;
        this.participaciones = 0;
        this.sgte = null;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getCarrera() { return carrera; }
    public int getCiclo() { return ciclo; }
    public int getParticipaciones() { return participaciones; }
    public void setParticipaciones(int participaciones) { this.participaciones = participaciones; }
    public void incrementarParticipacion() { this.participaciones++; }
    public NodoEstudiante getSgte() { return sgte; }
    public void setSgte(NodoEstudiante sgte) { this.sgte = sgte; }
}