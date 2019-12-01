package com.compiler;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Compila {

	// tipos de token

    public static int TPROGRAMA = 0;
    public static int TETIQ = 1;
    public static int TVAR = 2;
    public static int TCONST = 3;
    public static int TENTERA = 4;
    public static int TREAL = 5;
    public static int TPI = 6;
    public static int TLEE = 7;
    public static int TESCRIBE = 8;
    public static int TSI = 9;
    public static int TENTONCES = 10;
    public static int TSINO = 11;
    public static int TFINSI = 12;
    public static int TMIENTRAS = 13;
    public static int THAZ = 14;
    public static int TFINM = 15;
    public static int TREPITE = 16;
    public static int THASTA = 17;
    public static int TOR = 18;
    public static int TAND = 19;
    public static int TNOT = 20;
    public static int TSALTA = 21;
    public static int TIMPRIME = 23;

    public static int TID = 22;

    public static int TNUM_ENTERO = 50;
    public static int TNUM_REAL = 51;
    public static int TMAYOR = 52;
    public static int TMENOR = 53;
    public static int TMAY_IG = 54;
    public static int TMEN_IG = 55;
    public static int TDISTINTO = 56;
    public static int TIGUAL = 57;
    public static int TASIGNACION = 58;
    public static int TPAR_IZQ = 59;
    public static int TPAR_DER = 60;
    public static int TPUNTOYCOMA = 65;
    public static int TCOMA = 66;

    public static int TARIT_MUL = 61;
    public static int TARIT_SUM = 62;
    public static int TSTRING = 63;
    public static int TCHAR = 64;

    public static int TERRORCADENA = 97;
    public static int TERROR = 98;
    public static int TEOF = 99;

    // variables privadas
    public TablaSimbolos[] tSimbolos;
    public TablaErrores[] tErrores;
    int iToken;   /* indica si todavia hay tokens en el archivo */
    StringBuffer token = new StringBuffer();
    private PalabraReservada[] reservadas;  /* contiene el arreglo de palabras reservadas no de los simbolos */
    private boolean EOF = false;
    private char cc = ' ';
    private char[] buffer;
    private int MAX_BUF = 1000;       // capacidad del buffer
    private int MAX_ERR = 100;       // Numero maximo de errores
    private int buf_size = MAX_BUF;   // tamano real del buffer
    private int err_size = MAX_ERR;   // Numero maximo de errores
    private int buf_idx = MAX_BUF;    // indice
    private int tabla_idx = 0;
    private int error_idx = 0;
    private InputStreamReader fd_in = null;

    public Compila(String filename) {

        //int iToken;   /* indica si todavia hay tokens en el archivo */
        //StringBuffer token = new StringBuffer ();
        buffer = new char[MAX_BUF];

        tSimbolos = new TablaSimbolos[MAX_BUF];
        tErrores = new TablaErrores[MAX_ERR];
        //Se definen las palabras reservadas del lenguaje
        reservadas = new PalabraReservada[23];
        reservadas[0] = new PalabraReservada("PROGRAMA", TPROGRAMA);
        reservadas[1] = new PalabraReservada("ETIQ", TETIQ);
        reservadas[2] = new PalabraReservada("VAR", TVAR);
        reservadas[3] = new PalabraReservada("CONST", TCONST);
        reservadas[4] = new PalabraReservada("ENTERA", TENTERA);
        reservadas[5] = new PalabraReservada("REAL", TREAL);
        reservadas[6] = new PalabraReservada("PI", TPI);
        reservadas[7] = new PalabraReservada("LEE", TLEE);
        reservadas[8] = new PalabraReservada("ESCRIBE", TESCRIBE);
        reservadas[9] = new PalabraReservada("SI", TSI);
        reservadas[10] = new PalabraReservada("ENTONCES", TENTONCES);
        reservadas[11] = new PalabraReservada("SINO", TSINO);
        reservadas[12] = new PalabraReservada("FIN_SI", TFINSI);
        reservadas[13] = new PalabraReservada("MIENTRAS", TMIENTRAS);
        reservadas[14] = new PalabraReservada("HAZ", THAZ);
        reservadas[15] = new PalabraReservada("FINM", TFINM);
        reservadas[16] = new PalabraReservada("REPITE", TREPITE);
        reservadas[17] = new PalabraReservada("HASTA", THASTA);
        reservadas[18] = new PalabraReservada("OR", TOR);
        reservadas[19] = new PalabraReservada("AND", TAND);
        reservadas[20] = new PalabraReservada("NOT", TNOT);
        reservadas[21] = new PalabraReservada("SALTA", TSALTA);
        reservadas[22] = new PalabraReservada("IMPRIME", TIMPRIME);

        try {
            fd_in = new InputStreamReader(new FileInputStream(filename));
//            fd_in = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename), "UTF-8");
        } catch (Exception e) {
            //System.out.println ("Error: " + e);
            System.exit(0);
        }
        /* COMIENZO DEL ANALISIS DEL ARCHIVO */

        if ((iToken = this.nextToken(token)) != Compila.TEOF) {
            ValidaToken(token.toString());
            if (Encabezado()) {
                if (Declara())
                    System.out.println("Hasta antes de este token esta correcto:  " + token);
                else {

                }
            } else
                insertaTError(2, "", "Error en Encabezado del programa", 0, 0);
        } else
            insertaTError(1, "<<Vacio>>", "Archivo fuente Vacio", 0, 0);

    }

    private boolean Declara() {
        if (Declara_Var())
            return true;
        else if (Declara_Etiq())
            return true;
        else return Declara_Const();
    }

    private boolean Declara_Var() {
        iToken = this.nextToken(token);
        ValidaToken(token.toString());
        if (iToken == TVAR) {
            if (Tipo()) {
                return true;
            }

        } else {
            insertaTError(1, "<<Vacio>>", "Se esperaba Declaracion de Variables", 0, 0);
            return false;
        }
        return true;
    }

    private boolean Tipo() {
        iToken = this.nextToken(token);
        ValidaToken(token.toString());
        if ((iToken == TENTERA) || (iToken == TREAL)) {
            if (ListaVar()) {
                return true;
            }

        } else {
            insertaTError(1, "<<Vacio>>", "Solo acepta enteros y reales", 0, 0);
            return false;
        }
        return true;
    }

    private boolean ListaVar() {
        iToken = this.nextToken(token);
        ValidaToken(token.toString());
        if ((iToken == TID)) {
            return sListaVar();
        } else
            return false;

    }

    private boolean sListaVar() {
        iToken = this.nextToken(token);
        ValidaToken(token.toString());
        if ((iToken == TCOMA)) {
            return ListaVar();
        }
        return false;
    }

    private boolean Declara_Etiq() {
        return true;
    }

    private boolean Declara_Const() {
        return true;
    }

    private boolean Encabezado() {
        if (iToken == TPROGRAMA) {
            iToken = this.nextToken(token);
            ValidaToken(token.toString());
            if (iToken == TID) {
                iToken = this.nextToken(token);
                ValidaToken(token.toString());
                if (iToken == TPUNTOYCOMA)
                    return true;
                else {
                    insertaTError(2, "", "Se esperaba un punto y coma", 0, 0);
                    return false;
                }
            } else {
                insertaTError(2, "", "Se esperaba el identificador PROGRAMA", 0, 0);
                return false;
            }
        } else {
            insertaTError(2, "", "Esperaba Palabra reservada PROGRAMA", 0, 0);
            return false;
        }
    }

    private void ValidaToken(String sToken) {  // Valida el token y lo inserta en la tabla de simbolos
        int xt;
        if ((xt = buscaTS(sToken)) == -1)    // busca en la tabla de simbolos e inserta si es necesario
            insertaTS(sToken, xt);
        else            //incrementa
            tSimbolos[xt].incrementa();
    }

    public int buscaTS(String sToken) {
        for (int i = 0; i < tabla_idx; i++) {
            if (tSimbolos[i].getPalabra().equalsIgnoreCase(sToken))
                return i;
        }
        return -1;
    }

    private void insertaTS(String sToken, int sTipo) {  // inserta en la tabla de simbolos
        tSimbolos[tabla_idx] = new TablaSimbolos(sToken, sTipo, 1);

        //System.out.println("Token    " +sToken + "    tipo:    " +sTipo);
        tabla_idx++;
    }

    private void insertaTError(int pNumError, String sToken, String pLinea, int pTipo, int pnLinea) {  // inserta en la tabla de errores
        tErrores[error_idx] = new TablaErrores(sToken, pLinea, pNumError, pTipo, pnLinea);
        error_idx++;
    }

    public int getNumErrores() {
        return error_idx;
    }

    public int nextToken(StringBuffer token) {
        skip_blanks();
        token.setLength(0);

// Fin de Archivo EOF

        if (EOF) {
            return TEOF;
        }

// Id o palabra reservada
        else if (Character.isLetter(cc) || cc == '_') {
            do {
                token.append(cc);
                next_char();
            } while (Character.isLetterOrDigit(cc) || cc == '_');
            int idx;
            if ((idx = buscaReservadas(token.toString())) != -1)
                return reservadas[idx].getTipo();
            else
                return TID;
        }

// N�mero Entero o real
        else if (Character.isDigit(cc)) {
            do {
                token.append(cc);
                next_char();
            } while (Character.isDigit(cc));
            if (cc == '.') {   // es real
                do {
                    token.append(cc);
                    next_char();
                } while (Character.isDigit(cc));
                return TNUM_REAL;
            } else
                return TNUM_ENTERO;
        }
//OPERADORES RELACIONALES
// Mayor o mayor-igual
        else if (cc == '>') {
            token.append(cc);
            next_char();
            if (cc == '=') {
                token.append(cc);
                next_char();
                return TMAY_IG;
            } else
                return TMAYOR;
        }

// Menor o menor o igual
        else if (cc == '<') {
            token.append(cc);
            next_char();
            if (cc == '=') {
                token.append(cc);
                next_char();
                return TMEN_IG;
            } else if (cc == '>') {
                token.append(cc);
                next_char();
                return TDISTINTO;
            } else
                return TMENOR;
        }

// Igual
        else if (cc == '=') {
            token.append(cc);
            next_char();
            return TIGUAL;
        }

// ASIGNACION
        else if (cc == ':') {
            token.append(cc);
            next_char();
            if (cc == '=') {
                token.append(cc);
                next_char();
                return TASIGNACION;
            } else {
                token.append(cc);
                Error(cc + ": caracter no valido");
                next_char();
                return TERROR;
            }
        }
//OPERADORES ARITMETICOS
// Multiplicacion-division
        else if (cc == '*' || cc == '/') {
            token.append(cc);
            next_char();
            return TARIT_MUL;
        }
// Suma o resta
        else if (cc == '+' || cc == '-') {
            token.append(cc);
            next_char();
            return TARIT_SUM;
        }

// Parentesis izquierdo
        else if (cc == '(' || cc == '[' || cc == '{') {
            token.append(cc);
            next_char();
            return TPAR_IZQ;
        }

// Parentesis derecho
        else if (cc == ')' || cc == ']' || cc == '}') {
            token.append(cc);
            next_char();
            return TPAR_DER;
        }

//cadenas
        else if (cc == '"') {
            token.append(cc);
            next_char();
            while (!EOF && (cc != '"')) {
                token.append(cc);
                next_char();
                if (cc == '\n') {
                    Error("cadena no terminada");
                    //token.append ('"');
                    return TERRORCADENA;
                }
            }
            if (cc == '"') {
                token.append('"');
                next_char();
                return TSTRING;
            } else
                return TERRORCADENA;
        }

// caracteres
        else if (cc == '\'') {
            next_char();
            token.append(cc);
            next_char();
            if (EOF || (cc != '\''))
                Error(token + ": tipo char acepta solo un caracter");
            next_char();
            return TCHAR;
        }
//PUNTUACION

        else if (cc == ';') {
            token.append(cc);
            next_char();
            return TPUNTOYCOMA;
        } else if (cc == ',') {
            token.append(cc);
            next_char();
            return TCOMA;
        }

// Error-- cualquier otro caracter no valido
        else {
            token.append(cc);
            Error(cc + ": caracter no valido");
            next_char();
            return TERROR;
        }
    }

    private void next_char() {
        if (buf_idx == buf_size) {
            try {
                buf_size = fd_in.read(buffer, 0, MAX_BUF);
            } catch (Exception e) {
                EOF = true;
                cc = '\u2122';
            }
            if (buf_size == -1) {
                EOF = true;
                cc = '\u2122';
            }
            buf_idx = 0;
        }
        cc = buffer[buf_idx++];
        if (cc == '\t')
            cc = ' ';
    }

    private void skip_blanks() {
        while (Character.isWhitespace(cc))
            next_char();
    }

    private int buscaReservadas(String t) {
        for (int i = 0; i < reservadas.length; i++)
            if (t.compareToIgnoreCase(reservadas[i].getPalabra()) == 0)
                return i;
        return -1;
    }

    private void Error(String mssg) {
        System.out.println("Error: " + mssg);
    }
}

class PalabraReservada {
    private String palabra;
    private int tipo;

    PalabraReservada(String p, int t) {
        palabra = p;
        tipo = t;
    }

    public String getPalabra() {
        return palabra;
    }

    public int getTipo() {
        return tipo;
    }
}
