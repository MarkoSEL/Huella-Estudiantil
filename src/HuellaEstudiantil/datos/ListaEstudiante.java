package HuellaEstudiantil.datos;

import HuellaEstudiantil.modelo.NodoEstudiante;
import java.util.ArrayList;

public class ListaEstudiante {

    private NodoEstudiante inicio;

    public ListaEstudiante() {
        this.inicio = null;
    }

    public void agregarAlFinal(NodoEstudiante nuevo) {
        if (inicio == null) {
            inicio = nuevo; 
        } else {
            NodoEstudiante p = inicio;
            while (p.getSgte() != null) {
                p = p.getSgte();
            }
            p.setSgte(nuevo);
        }
    }

    public NodoEstudiante buscarPorCodigo(String codigo) {
        NodoEstudiante p = inicio;
        while (p != null) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
            p = p.getSgte(); 
        }
        return null; 
    }

    public ArrayList<NodoEstudiante> getTodosComoArrayList() {
        ArrayList<NodoEstudiante> lista = new ArrayList<>();
        NodoEstudiante p = inicio;
        while (p != null) {
            lista.add(p);
            p = p.getSgte();
        }
        return lista;
    }
}