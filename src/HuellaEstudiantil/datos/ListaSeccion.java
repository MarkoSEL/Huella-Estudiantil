package HuellaEstudiantil.datos;

import HuellaEstudiantil.modelo.NodoSeccion;

public class ListaSeccion {
    private NodoSeccion inicio;

    public ListaSeccion() { this.inicio = null; }

    public void agregarAlFinal(NodoSeccion nuevo) {
        if (inicio == null) {
            inicio = nuevo;
        } else {
            NodoSeccion p = inicio;
            while (p.getSgte() != null) { p = p.getSgte(); }
            p.setSgte(nuevo);
        }
    }

    // Búsqueda Lineal por ID
    public NodoSeccion buscarPorId(String id) {
        NodoSeccion p = inicio;
        while (p != null) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
            p = p.getSgte();
        }
        return null;
    }
    
    // Búsqueda Lineal por curso, docente y periodo
    public NodoSeccion buscarPorDatos(String codigoCurso, String idDocente, String periodo) {
        NodoSeccion p = inicio;
        while (p != null) {
            if (p.getCurso().getCodigo().equalsIgnoreCase(codigoCurso) &&
                p.getDocente().getId().equalsIgnoreCase(idDocente) &&
                p.getPeriodo().equalsIgnoreCase(periodo)) {
                return p;
            }
            p = p.getSgte();
        }
        return null;
    }
}
