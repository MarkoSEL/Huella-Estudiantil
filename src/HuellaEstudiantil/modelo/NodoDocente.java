package HuellaEstudiantil.modelo;

// Clase para el requerimiento "Registrar Docente"
public class NodoDocente {
    private String id;
    private String nombre;
    private NodoDocente sgte; 

    public NodoDocente(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.sgte = null;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public NodoDocente getSgte() { return sgte; }
    public void setSgte(NodoDocente sgte) { this.sgte = sgte; }
}
