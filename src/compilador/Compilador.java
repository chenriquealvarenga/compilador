package compilador;

/**
 *
 * @author carlos
 */

public class Compilador {

    /**
     * @param args the command line arguments
     */

    
    public static void main(String[] args) {
        
        Lexico lexico = new Lexico();
        lexico.analiseLexica();
    }
}
