package HuellaEstudiantil.datos;


import HuellaEstudiantil.modelo.NodoCurso;
import HuellaEstudiantil.modelo.NodoDocente;
import HuellaEstudiantil.modelo.NodoEstudiante;
import HuellaEstudiantil.modelo.NodoSeccion;
import java.util.HashSet;
// ----------------------
public class BaseDeDatos {

    private static BaseDeDatos instancia;

    public ListaEstudiante listaEstudiantes;
    public ListaCurso listaCursos;
    public ListaDocente listaDocentes;
    public ListaSeccion listaSecciones;
    
    // HashSet para rastrear cursos ya registrados por el usuario
    private HashSet<String> cursosRegistrados;
    // HashSet para rastrear docentes ya registrados por el usuario
    private HashSet<String> docentesRegistrados;
    // HashSet para rastrear secciones ya registradas por el usuario (por ID)
    private HashSet<String> seccionesRegistradas;
    // HashSet para rastrear estudiantes ya registrados por el usuario (por código)
    private HashSet<String> estudiantesRegistrados;
    
    private BaseDeDatos() {
        listaEstudiantes = new ListaEstudiante();
        listaCursos = new ListaCurso();
        listaDocentes = new ListaDocente();
        listaSecciones = new ListaSeccion();
        cursosRegistrados = new HashSet<>();
        docentesRegistrados = new HashSet<>();
        seccionesRegistradas = new HashSet<>();
        estudiantesRegistrados = new HashSet<>();
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
    
    public NodoEstudiante buscarEstudiantePorNombre(String nombre) {
        return listaEstudiantes.buscarPorNombre(nombre);
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
    
    public NodoSeccion buscarSeccionPorDatos(String codigoCurso, String idDocente, String periodo) {
        return listaSecciones.buscarPorDatos(codigoCurso, idDocente, periodo);
    }
    
    // Métodos para gestionar cursos registrados
    public boolean estaCursoRegistrado(String codigo) {
        return cursosRegistrados.contains(codigo.toUpperCase());
    }
    
    public void marcarCursoComoRegistrado(String codigo) {
        cursosRegistrados.add(codigo.toUpperCase());
    }
    
    // Métodos para gestionar docentes registrados
    public boolean estaDocenteRegistrado(String id) {
        return docentesRegistrados.contains(id.toUpperCase());
    }
    
    public void marcarDocenteComoRegistrado(String id) {
        docentesRegistrados.add(id.toUpperCase());
    }
    
    // Métodos para gestionar secciones registradas
    public boolean estaSeccionRegistrada(String id) {
        return seccionesRegistradas.contains(id.toUpperCase());
    }
    
    public void marcarSeccionComoRegistrada(String id) {
        seccionesRegistradas.add(id.toUpperCase());
    }
    
    // Métodos para gestionar estudiantes registrados
    public boolean estaEstudianteRegistrado(String codigo) {
        return estudiantesRegistrados.contains(codigo.toUpperCase());
    }
    
    public void marcarEstudianteComoRegistrado(String codigo) {
        estudiantesRegistrados.add(codigo.toUpperCase());
    }

    public void inicializarDatos() {
        // Cursos
        NodoCurso cursoAlgo = new NodoCurso("CS101", "Algoritmos", "Presencial", 2);
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