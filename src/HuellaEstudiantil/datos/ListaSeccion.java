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

    // Búsqueda Lineal
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
}
