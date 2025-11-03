package HuellaEstudiantil.main;

import HuellaEstudiantil.datos.BaseDeDatos;
import HuellaEstudiantil.vista.Vista;

public class Principal {

    public static void main(String[] args) {

        BaseDeDatos.getInstancia().inicializarDatos();

        Vista vista = new Vista();
        vista.mostrarMenu();
    }
}
