package HuellaEstudiantil.datos;

// --- IMPORTS AÑADIDOS ---
import HuellaEstudiantil.modelo.NodoCurso;
import HuellaEstudiantil.modelo.NodoDocente;
import HuellaEstudiantil.modelo.NodoEstudiante;
import HuellaEstudiantil.modelo.NodoSeccion;
// ----------------------
public class BaseDeDatos {

    private static BaseDeDatos instancia;

    public ListaEstudiante listaEstudiantes;
    public ListaCurso listaCursos;
    public ListaDocente listaDocentes;
    public ListaSeccion listaSecciones;
    
    private BaseDeDatos() {
        listaEstudiantes = new ListaEstudiante();
        listaCursos = new ListaCurso();
        listaDocentes = new ListaDocente();
        listaSecciones = new ListaSeccion();
    }

    public static BaseDeDatos getInstancia() {
        if (instancia == null) {
            instancia = new BaseDeDatos();
        }
        return instancia;
    }

    // Métodos de Búsqueda
    public NodoEstudiante buscarEstudiantePorCodigo(String codigo) {
        return listaEstudiantes.buscarPorCodigo(codigo);
    }
    
    public NodoCurso buscarCursoPorCodigo(String codigo) {
        return listaCursos.buscarPorCodigo(codigo);
    }
    
    public NodoDocente buscarDocentePorId(String id) {
        return listaDocentes.buscarPorId(id);
    }

    public NodoSeccion buscarSeccionPorId(String id) {
        return listaSecciones.buscarPorId(id);
    }

    public void inicializarDatos() {
        // Cursos
        NodoCurso cursoAlgo = new NodoCurso("CS101", "Algoritmos", "Presencial", 32);
        listaCursos.agregarAlFinal(cursoAlgo);

        // Docentes
        NodoDocente docente1 = new NodoDocente("D001", "Marko Encalada");
        listaDocentes.agregarAlFinal(docente1);

        // Estudiantes
        listaEstudiantes.agregarAlFinal(new NodoEstudiante("U2024001", "Ana Gomez", "Sistemas", 5));
        listaEstudiantes.agregarAlFinal(new NodoEstudiante("U2024002", "Luis Torres", "Software", 4));
        listaEstudiantes.agregarAlFinal(new NodoEstudiante("U2024003", "Maria Fernandez", "Sistemas", 5));
        listaEstudiantes.agregarAlFinal(new NodoEstudiante("U2024004", "Juan Perez", "Software", 3));
        listaEstudiantes.agregarAlFinal(new NodoEstudiante("U2024005", "Carlos Ruiz", "Sistemas", 2));

        // Secciones
        NodoSeccion seccion123 = new NodoSeccion("SI-123", cursoAlgo, docente1, "2025-Marzo");
        listaSecciones.agregarAlFinal(seccion123);
        
        System.out.println("Datos de prueba inicializados.");
    }
}