package HuellaEstudiantil.datos;

import HuellaEstudiantil.modelo.NodoDocente;

public class ListaDocente {
    private NodoDocente inicio;

    public ListaDocente() { this.inicio = null; }

    public void agregarAlFinal(NodoDocente nuevo) {
        if (inicio == null) {
            inicio = nuevo;
        } else {
            NodoDocente p = inicio;
            while (p.getSgte() != null) { p = p.getSgte(); }
            p.setSgte(nuevo);
        }
    }

    // Búsqueda Lineal
    public NodoDocente buscarPorId(String id) {
        NodoDocente p = inicio;
        while (p != null) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
            p = p.getSgte();
        }
        return null;
    }
}
