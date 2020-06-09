import com.compiler.Compila;

public class Main {

    public static void main(String args[]) {
        int t;
        StringBuffer token = new StringBuffer();

        System.out.println("compiling");
        String file1 = "E:\\IdeaProjects\\Compiler_Java\\src\\main\\resources\\Programa.txt";
        String file2 = "E:\\IdeaProjects\\Compiler_Java\\src\\main\\resources\\miPrograma1.txt";
        String file3 = "E:\\IdeaProjects\\Compiler_Java\\src\\main\\resources\\miPrograma2.txt";
        Compila x = new Compila(file3);

        System.out.println("Fin de análisis lexico.. separa los tokens y los almacena en una tabla");

        System.out.println(x.tSimbolos[0].getNumRegistros());
        for (int i = 0; i < x.tSimbolos[0].getNumRegistros(); i++) {
            System.out.println("Palabra: " + x.tSimbolos[i].getPalabra() +
                    "\tOcurrencias: " + x.tSimbolos[i].getnVeces());
        }


        System.out.println("prueba de analisis sintactico...checa que la sintaxis de las instrucciones esten correctas");
        System.out.println("Fin de análisis sintactico");


        // Muestra la tabla de errores generados

        System.out.println("Errores generados    " + x.getNumErrores());
        for (int i = 0; i < x.getNumErrores(); i++) {
            System.out.println("Error: " + x.tErrores[i].getsLinea());
        }
    }
}

//\tTipo: " + x.tSimbolos[i].getTipoS() +