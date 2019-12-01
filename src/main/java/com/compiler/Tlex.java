package com.compiler;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Tlex {

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
    private Reservada[] reservadas;  /* contiene el arreglo de las palabras reservadas.. no de los simbolos */
    private boolean EOF = false;
    private char cc = ' ';
    private char[] buffer;
    private int MAX_BUF = 1000;       // capacidad del buffer
    private int buf_size = MAX_BUF;   // tamano real del buffer
    private int buf_idx = MAX_BUF;    // indice
    private int tabla_idx = 0;
    private InputStreamReader fd_in = null;

    Tlex(String filename) {
        int t;   /* indica si todavia hay tokens en el archivo */
        int xt;
        StringBuffer token = new StringBuffer();
        buffer = new char[MAX_BUF];

        tSimbolos = new TablaSimbolos[MAX_BUF];

        reservadas = new Reservada[23];
        reservadas[0] = new Reservada("PROGRAMA", TPROGRAMA);
        reservadas[1] = new Reservada("ETIQ", TETIQ);
        reservadas[2] = new Reservada("VAR", TVAR);
        reservadas[3] = new Reservada("CONST", TCONST);
        reservadas[4] = new Reservada("ENTERA", TENTERA);
        reservadas[5] = new Reservada("REAL", TREAL);
        reservadas[6] = new Reservada("PI", TPI);
        reservadas[7] = new Reservada("LEE", TLEE);
        reservadas[8] = new Reservada("ESCRIBE", TESCRIBE);
        reservadas[9] = new Reservada("SI", TSI);
        reservadas[10] = new Reservada("ENTONCES", TENTONCES);
        reservadas[11] = new Reservada("SINO", TSINO);
        reservadas[12] = new Reservada("FIN_SI", TFINSI);
        reservadas[13] = new Reservada("MIENTRAS", TMIENTRAS);
        reservadas[14] = new Reservada("HAZ", THAZ);
        reservadas[15] = new Reservada("FINM", TFINM);
        reservadas[16] = new Reservada("REPITE", TREPITE);
        reservadas[17] = new Reservada("HASTA", THASTA);
        reservadas[18] = new Reservada("OR", TOR);
        reservadas[19] = new Reservada("AND", TAND);
        reservadas[20] = new Reservada("NOT", TNOT);
        reservadas[21] = new Reservada("SALTA", TSALTA);
        reservadas[22] = new Reservada("IMPRIME", TIMPRIME);

        try {
            fd_in = new InputStreamReader(new FileInputStream(filename));
        } catch (Exception e) {
            //System.out.println ("Error: " + e);
            System.exit(0);
        }
        /* empieza a analizar el archivo */
        while ((t = this.nextToken(token)) != Tlex.TEOF) {
            if ((xt = buscaTS(token.toString())) == -1)    /* busca en la tabla de simbolos e inserta si es necesario */
                insertaTS(token.toString(), t);
            else            //incrementa
                tSimbolos[xt].incrementa();
        }
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
        tabla_idx++;
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

// Número Entero o real
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
// ;
        else if (cc == ';') {
            token.append(cc);
            next_char();
            return TPUNTOYCOMA;
        }
// ,
        else if (cc == ',') {
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

class Reservada {
    private String palabra;
    private int tipo;

    Reservada(String p, int t) {
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


