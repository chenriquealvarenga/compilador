/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import javafx.util.Pair;

/**
 *
 * @author Natália
 */
public class Sintatico {
    
    Integer linhaAtual = 1;
    private Lexico lexico;
    
    public Sintatico(Lexico lexico) {
        this.lexico = lexico;              
    }
    
    public void analiseSintatica() throws Exception {
        Pair<String, String> token = lexico.proximoToken();
        conferirToken(token);
    }
    
    public void conferirToken(Pair<String, String> token) throws Exception {
        Pair<String, String> proximoToken = null;
        try {                  
            switch(token.getValue()) {
                case "var":
                    proximoToken = pularEspacosQuebras();
                    if(!proximoToken.getValue().equals("identifier")) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                case "identifier":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("is") && 
                        !proximoToken.getValue().equals("comma") && 
                        //!proximoToken.getValue().equals("rparenthesis") &&
                        !proximoToken.getValue().equals("assign")
                    ) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                case "comma":
                    proximoToken = pularEspacosQuebras();
                    if(!proximoToken.getValue().equals("identifier")) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                case "is":
                    proximoToken = pularEspacosQuebras();
                    if(!proximoToken.getValue().equals("type")) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                case "type":
                    proximoToken = pularEspacosQuebras();
                    if(!proximoToken.getValue().equals("semicolon") && !proximoToken.getValue().equals("begin")) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                case "semicolon":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("identifier") &&
                        !proximoToken.getValue().equals("var") &&
                        !proximoToken.getValue().equals("if") &&
                        !proximoToken.getValue().equals("else") &&    
                        !proximoToken.getValue().equals("do") &&
                        !proximoToken.getValue().equals("while") &&
                        !proximoToken.getValue().equals("read") &&
                        !proximoToken.getValue().equals("write") &&  
                        !proximoToken.getValue().equals("begin") && 
                        !proximoToken.getValue().equals("end")
                    ) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                case "begin":
                case "else":
                case "do":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("identifier") &&
                        !proximoToken.getValue().equals("if") &&
                        !proximoToken.getValue().equals("do") &&
                        !proximoToken.getValue().equals("read") &&
                        !proximoToken.getValue().equals("write")
                    ) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                case "read":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("lparenthesis")
                    ) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                case "write":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("lparenthesis")
                    ) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                case "lparenthesis":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("identifier") &&
                        !proximoToken.getValue().equals("integer_const")
                    ) {
                        throw new Exception();
                    } else {
                        stmtLogicalExpression(proximoToken);                        
                        proximoToken = pularEspacosQuebras();                        
                        conferirToken(proximoToken);
                    }
                    break;
                case "rparenthesis":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("semicolon")
                    ) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                case "assign":
                    stmtAssign(token);
                    proximoToken = pularEspacosQuebras();
                    conferirToken(proximoToken);
                    break;
                case "end":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("identifier") &&
                        !proximoToken.getValue().equals("if") &&
                        !proximoToken.getValue().equals("do") &&
                        !proximoToken.getValue().equals("read") &&
                        !proximoToken.getValue().equals("write") &&
                        !proximoToken.getValue().equals("end") &&
                        !proximoToken.getValue().equals("EOF")
                    ) {
                        throw new Exception();
                    } else {
                        if(proximoToken.getValue().equals("EOF")) {
                            System.out.println("Arquivo analisado sintáticamente com sucesso");
                        } else {
                            conferirToken(proximoToken);
                        }
                    }
                    break;
                case "if":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("lparenthesis")
                    ) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                case "while":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("lparenthesis")
                    ) {
                        throw new Exception();
                    } else {
                        conferirToken(proximoToken);
                    }
                    break;
                default:
                    throw new Exception();
            }   
        } catch(Exception ex) {
            String erro = ex.getMessage();
            if(erro == null) {
                System.err.println(String.format("Erro linha %1$d: %2$s %3$s", linhaAtual, token.getKey(), proximoToken.getKey()));
            } else {
                System.err.println(erro);
            }
            return;
        }
    }
    
    public Pair<String, String> pularEspacosQuebras() {                 
        Pair<String, String> token = proximoToken();        
        if(!token.getValue().equals("space") && !token.getValue().equals("LB")) return token;
        return pularEspacosQuebras();
    }
    
    public Pair<String, String> proximoToken() {
        Pair<String, String> proximoToken = lexico.proximoToken();
        if(proximoToken.getValue().equals("LB")) linhaAtual++;
        return proximoToken;
    }
    
    private void stmtAssign(Pair<String, String> token) throws Exception {
        Pair<String, String> proximoToken = null;        
        try {
            switch(token.getValue()) {
                case "assign":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("identifier") &&
                        !proximoToken.getValue().equals("integer_const") &&
                        !proximoToken.getValue().equals("lparenthesis")
                    ) {
                        throw new Exception();
                    } else {
                        stmtAssign(proximoToken);
                    }
                    break;
                case "identifier":
                case "integer_const":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("semicolon") &&
                        !proximoToken.getValue().equals("mulop") &&
                        !proximoToken.getValue().equals("addop")
                    ) {
                        throw new Exception();
                    } else {
                        stmtAssign(proximoToken);
                    }
                    break;
                case "lparenthesis":
                    stmtExpression(token);
                    break;
                case "mulop":
                case "addop":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("identifier") &&
                        !proximoToken.getValue().equals("integer_const") &&
                        !proximoToken.getValue().equals("lparenthesis")
                    ) {
                        throw new Exception();
                    } else {
                        stmtAssign(proximoToken);
                    }
                    break;
            }
        } catch(Exception ex) {
            String erro = ex.getMessage();
            if(erro == null) {
                throw new Exception(String.format("Erro linha %1$d: %2$s %3$s", linhaAtual, token.getKey(), proximoToken.getKey()));
            } else {
                throw new Exception(erro);
            }
        }
        
    }
    
    private void stmtExpression(Pair<String, String> token) throws Exception {
        Pair<String, String> proximoToken = null;    
        
        try {
        
            switch(token.getValue()) {
                case "lparenthesis":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("identifier") &&
                        !proximoToken.getValue().equals("integer_const")
                    ) {
                        throw new Exception();
                    } else {
                        stmtExpression(proximoToken);
                    }
                    break;
                case "integer_const":
                case "identifier":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("mulop") &&
                        !proximoToken.getValue().equals("addop") &&
                        !proximoToken.getValue().equals("rparenthesis")
                    ) {
                        throw new Exception();
                    } else {
                        stmtExpression(proximoToken);
                    }
                    break;
                case "mulop":
                case "addop":
                    proximoToken = pularEspacosQuebras();
                    if(                
                        !proximoToken.getValue().equals("identifier") &&
                        !proximoToken.getValue().equals("lparenthesis") &&
                        !proximoToken.getValue().equals("integer_const")
                    ) {
                        throw new Exception();
                    } else {
                        stmtExpression(proximoToken);
                    }
                    break;
                case "rparenthesis":
                    proximoToken = pularEspacosQuebras();
                    if(                
                        !proximoToken.getValue().equals("semicolon") &&
                        !proximoToken.getValue().equals("mulop") &&
                        !proximoToken.getValue().equals("addop")
                    ) {
                        throw new Exception();
                    } else {
                        stmtExpression(proximoToken);
                    }
                    break;
            }
        } catch(Exception ex) {
            String erro = ex.getMessage();
            if(erro == null) {
                throw new Exception(String.format("Erro linha %1$d: %2$s %3$s", linhaAtual, token.getKey(), proximoToken.getKey()));
            } else {
                throw new Exception(erro);
            }
        }
        
    }
    
    private void stmtLogicalExpression(Pair<String, String> token) throws Exception {
        Pair<String, String> proximoToken = null;    
        
        try {
        
            switch(token.getValue()) {
                case "lparenthesis":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("identifier") &&
                        !proximoToken.getValue().equals("integer_const")
                    ) {
                        throw new Exception();
                    } else {
                        stmtLogicalExpression(proximoToken);
                    }
                    break;
                case "integer_const":
                case "identifier":
                    proximoToken = pularEspacosQuebras();
                    if(
                        !proximoToken.getValue().equals("relop") &&
                        !proximoToken.getValue().equals("mulop") &&
                        !proximoToken.getValue().equals("rparenthesis")
                    ) {
                        throw new Exception();
                    } else {
                        stmtLogicalExpression(proximoToken);
                    }
                    break;
                case "relop":
                case "mulop":
                    proximoToken = pularEspacosQuebras();
                    if(                
                        !proximoToken.getValue().equals("identifier") &&
                        !proximoToken.getValue().equals("integer_const")
                    ) {
                        throw new Exception();
                    } else {
                        stmtLogicalExpression(proximoToken);
                    }
                    break;
                case "rparenthesis":
                    proximoToken = pularEspacosQuebras();
                    if(                
                        !proximoToken.getValue().equals("semicolon") &&
                        !proximoToken.getValue().equals("then")
                    ) {
                        throw new Exception();
                    } else {
                        stmtLogicalExpression(proximoToken);
                    }
                    break;
            }
        } catch(Exception ex) {
            String erro = ex.getMessage();
            if(erro == null) {
                throw new Exception(String.format("Erro linha %1$d: %2$s %3$s", linhaAtual, token.getKey(), proximoToken.getKey()));
            } else {
                throw new Exception(erro);
            }
        }
        
    }
    
}
