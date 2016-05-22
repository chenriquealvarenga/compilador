package compilador;

/**
 *
 * @author carlos
 */

public class Compilador {

    /**
     * @param args the command line arguments
     */

    
    public static void main(String[] args) throws Exception {
        String filename = args[0];
        Lexico lexico = new Lexico(filename);
        lexico.analiseLexica();
        
        Sintatico sintatico = new Sintatico(lexico);
        
    }
}
