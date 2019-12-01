package com.compiler;

public class TablaSimbolos {

    private static int NumRegistros;
    // propiedades de la tabla de simbolos
    private String Palabra;
    private int Tipo;
    private int nVeces;

    TablaSimbolos(String pPalabra, int pTipo, int pnVeces) {
        Palabra = pPalabra;
        Tipo = pTipo;
        nVeces = pnVeces;
        NumRegistros++;
    }

    public int getNumRegistros() {
        return NumRegistros;
    }

    public void incrementa() {
        nVeces++;
    }

    public String getPalabra() {
        return Palabra.trim();
    }

    public void setPalabra(String pPalabra) {
        Palabra = pPalabra;
    }

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int pTipo) {
        Tipo = pTipo;
    }

    public int getnVeces() {
        return nVeces;
    }

    public void setnVeces(int pnVeces) {
        nVeces = pnVeces;
    }

    public String getTipoS() {
        if (Tipo == 0) return ("P.RES PROGRAMA");
        else if (Tipo == 1) return ("P.RES ETIQUETA");
        else if (Tipo == 2) return ("P.RES VAR");
        else if (Tipo == 3) return ("P.RES CONST");
        else if (Tipo == 4) return ("P.RES ENTERA");
        else if (Tipo == 5) return ("P.RES REAL");
        else if (Tipo == 6) return ("P.RES PI");
        else if (Tipo == 7) return ("P.RES LEE");
        else if (Tipo == 8) return ("P.RES ESCRIBE");
        else if (Tipo == 9) return ("P.RES SI");
        else if (Tipo == 10) return ("P.RES ENTONCES");
        else if (Tipo == 11) return ("P.RES SINO");
        else if (Tipo == 12) return ("P.RES FINSI");
        else if (Tipo == 13) return ("P.RES MIENTRAS");
        else if (Tipo == 14) return ("P.RES HAZ");
        else if (Tipo == 15) return ("P.RES FINM");
        else if (Tipo == 16) return ("P.RES REPITE");
        else if (Tipo == 17) return ("P.RES HASTA");
        else if (Tipo == 18) return ("P.RES OR");
        else if (Tipo == 19) return ("P.RES AND");
        else if (Tipo == 20) return ("P.RES NOT");
        else if (Tipo == 21) return ("P.RES SALTA");
        else if (Tipo == 22) return ("IDENTIFICADOR");
        else if (Tipo == 23) return ("P.RES IMPRIME");
        else if (Tipo == 50) return ("NÚMERO ENTERO");
        else if (Tipo == 51) return ("NÚMERO REAL");
        else if (Tipo == 52) return ("OP. REL. MAYOR");
        else if (Tipo == 53) return ("OP. REL. MENOR");
        else if (Tipo == 54) return ("OP. REL. MAYOR IGUAL");
        else if (Tipo == 55) return ("OP. REL. MENOR IGUAL");
        else if (Tipo == 56) return ("OP. REL. DIFERENTE");
        else if (Tipo == 57) return ("OP. REL. IGUAL");
        else if (Tipo == 58) return ("OP. ASIGNACION");
        else if (Tipo == 59) return ("PARENTESIS IZQ");
        else if (Tipo == 60) return ("PARENTESIS DER");
        else if (Tipo == 61) return ("OP. ARITM. MULT.");
        else if (Tipo == 62) return ("OP. ARITM. ADIC.");
        else if (Tipo == 63) return ("CADENA");
        else if (Tipo == 64) return ("CARACTER");
        else if (Tipo == 65) return ("PUNTO Y COMA");
        else if (Tipo == 66) return ("COMA");
        else if (Tipo == 98) return ("ERROR!! CARACTER NO VALIDO");
        else if (Tipo == 97) return ("ERROR!! CADENA NO TERMINADA");
        return "";

    }
}



