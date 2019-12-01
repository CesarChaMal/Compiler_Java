package com.compiler;

public class TablaErrores {

    // propiedades de errores
    private int NumError; // Numero de error asignado
    private String Palabra; //palabra que genera el error
    private String sLinea;  // Linea de codigo donde se encontro el error
    private int Tipo;  // Indica si es error Sintactico.. o Lexico
    private int nLinea;  // numero de linea donde esta el error
    private static int NumErrores;  // contador del numero de errores

    TablaErrores(String pPalabra, String psLinea, int pNumError, int pTipo, int pnLinea) {
        NumError = pNumError;
        Palabra = pPalabra;
        sLinea = psLinea;
        Tipo = pTipo;
        nLinea = pnLinea;
        NumErrores++;
    }

    public int getNumErrores() {
        return NumErrores;
    }

    public String getPalabra() {
        return Palabra.trim();
    }

    public String getsLinea() {
        return sLinea.trim();
    }

    public int getTipo() {
        return Tipo;
    }

    public int getnLinea() {
        return nLinea;
    }

    public void setPalabra(String pPalabra) {
        Palabra = pPalabra;
    }

    public void setTipo(int pTipo) {
        Tipo = pTipo;
    }

    public String getTipoS() {
        return "I/O";
    }
}



