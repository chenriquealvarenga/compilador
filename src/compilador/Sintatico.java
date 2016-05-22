/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import javafx.util.Pair;

/**
 *
 * @author Matthaus
 */
public class Sintatico {
    
    private Lexico lexico;
    
    public Sintatico(Lexico lexico) {
        this.lexico = lexico;              
    }
    
    public void analiseSintatica() {
        Pair<String, String> token = null;
        do {
            token = lexico.proximoToken();
            System.out.println(token);
        } while(!token.getValue().equals("EOF"));
    }
    
}
