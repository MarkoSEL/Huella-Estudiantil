package HuellaEstudiantil.datos;

import HuellaEstudiantil.modelo.NodoCurso;

public class ListaCurso {
    private NodoCurso inicio;

    public ListaCurso() { this.inicio = null; }

    public void agregarAlFinal(NodoCurso nuevo) {
        if (inicio == null) {
            inicio = nuevo;
        } else {
            NodoCurso p = inicio;
            while (p.getSgte() != null) { p = p.getSgte(); }
            p.setSgte(nuevo);
        }
    }

    public NodoCurso buscarPorCodigo(String codigo) {
        NodoCurso p = inicio;
        while (p != null) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
            p = p.getSgte();
        }
        return null;
    }
}