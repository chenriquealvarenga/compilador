package compilador;

import javafx.util.Pair;

/**
 *
 * @author Natália
 */
public class Sintatico {
    
    Integer linhaAtual = 1;
    private Lexico lexico;
    private Pair<String, String> token = null;
    private Pair<String, String> previous_token = null;
    
    public Sintatico(Lexico lexico) {
        this.lexico = lexico;              
    }
    
    public void analiseSintatica() throws Exception {
        token = lexico.proximoToken();
        program();
    }
    
    public void program() throws Exception{
        try {
            if(token.getValue().equals("var")){
//                System.out.println("var");
                previous_token = token; token = pularEspacosQuebras();
                decl_list();

            }
            else{
                throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + token.getKey());
            }
            
            if(token.getValue().equals("begin")){
//                System.out.println("begin");
                previous_token = token; token = pularEspacosQuebras();
                stmt_list("stmt_program");

            }
            else throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
            
            if(token.getValue().equals("end")){
                
                System.out.println("Codigo analisado sintaticamente com sucesso.");

            }
            else throw new Exception();
        }
        catch(Exception ex) {
            String erro = ex.getMessage();
            System.out.println(erro);
    }
    }
    
    public void decl_list() throws Exception{
//        System.out.println("decl_list");
        while(!token.getValue().equals("begin")){
            if(token.getValue().equals("EOF")){
                throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
            }
            else{
                decl();
                previous_token = token; token = pularEspacosQuebras();
            }
        }
    }
    
    public void decl() throws Exception{
//        System.out.println("decl");
        ident_list();
        if(token.getValue().equals("is")){
            previous_token = token; token = pularEspacosQuebras();
            type();
        }
        else{
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
    }
    
    public void type() throws Exception{
//        System.out.println("type");
        if(!token.getValue().equals("type")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
    }
    
    public void ident_list() throws Exception{
//        System.out.println("ident_list");
        while(token.getValue().equals("identifier") && !token.getValue().equals("is")){
            previous_token = token; token = pularEspacosQuebras();
            if(token.getValue().equals("comma")){
                previous_token = token; token = pularEspacosQuebras();
            }
            else if(!token.getValue().equals("is")){
                throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
            }
        }
    }
    
    public void stmt_list(String tipo) throws Exception{
//        System.out.println("STMTLIST");
        if(tipo.equals("stmt_if")){
            while(!token.getValue().equals("end")&&!token.getValue().equals("else")){
                if(token.getValue().equals("EOF")){
                    throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
                }
                else{
                    stmt();
                    previous_token = token; token = pularEspacosQuebras();
                }
            }
        }
        else if(tipo.equals("stmt_program")){
            while(!token.getValue().equals("end")){
                if(token.getValue().equals("EOF")){
                    throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
                }
                else{
                    stmt();
                    previous_token = token; token = pularEspacosQuebras();
                }
            }
        }
        else if(tipo.equals("stmt_while")){
            while(!token.getValue().equals("while")){
                if(token.getValue().equals("EOF")){
                    throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
                }
                else{
                    stmt();
                    previous_token = token; token = pularEspacosQuebras();
                }
            }
        }
    }
    
    public void stmt() throws Exception{
//        System.out.println("STMT");
        switch(token.getValue()){
            case "identifier":
                previous_token = token; token = pularEspacosQuebras();
                assign_stmt();
                break;
            case "if":
                previous_token = token; token = pularEspacosQuebras();
                if_stmt();
                break;
            case "do":
                previous_token = token; token = pularEspacosQuebras();
                do_stmt();
                break;
            case "read":
                previous_token = token; token = pularEspacosQuebras();
                read_stmt();
                break;
            case "write":
                previous_token = token; token = pularEspacosQuebras();
                write_stmt();
                break;
        }
    }
    
    public void write_stmt() throws Exception{
//        System.out.println("WRITE_STMT");
        writable();
        if(!token.getValue().equals("semicolon")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
    }
    
    public void writable() throws Exception{
//        System.out.println("WRITABLE");
        simple_expr();
        previous_token = token; token = pularEspacosQuebras();
        
    }
    
    public void read_stmt() throws Exception{
//        System.out.println("READ_STMT");
        if(!token.getValue().equals("lparenthesis")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
        if(!token.getValue().equals("identifier")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
        if(!token.getValue().equals("rparenthesis")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
        if(!token.getValue().equals("semicolon")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
    }
    
    public void do_stmt() throws Exception{
//        System.out.println("DO_STMT");
        stmt_list("stmt_while");
        stmt_suffix();
    }
    
    public void stmt_suffix() throws Exception{
        if(!token.getValue().equals("while")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
        condition();
    }
    
    public void if_stmt() throws Exception{
//        System.out.println("IF_STMT");
        condition();
        if(!token.getValue().equals("then")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
        stmt_list("stmt_if");
        if_stmt_2();
    }
    
    public void if_stmt_2() throws Exception{
//        System.out.println("IF_STMT_2");
        if(token.getValue().equals("end")){
//            previous_token = token; token = pularEspacosQuebras();
        }
        else if(token.getValue().equals("else")){
            previous_token = token; token = pularEspacosQuebras();
            stmt_list("stmt_program");
        }
        else{
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
    }
    
    public void condition() throws Exception{
//        System.out.println("CONDITION");
        expression();
        previous_token = token; token = pularEspacosQuebras();
    }
    
    public void assign_stmt() throws Exception{
//        System.out.println("ASSIGN_STMT");
        if(!token.getValue().equals("assign")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
        simple_expr();
    }
    
    public void simple_expr() throws Exception{
//        System.out.println("SIMPLE_EXPR");
        term();
        simple_expr_2();
    }
    
    public void term() throws Exception{
//        System.out.println("TERM");
        factor_a();
        term_2();
    }
    
    public void factor_a() throws Exception{
//        System.out.println("FACTOR_A");
        if(token.getValue().equals("not")||token.getKey().equals("-")){
            previous_token = token; token = pularEspacosQuebras();
        }
        factor();
    }
    
    public void factor() throws Exception{
//        System.out.println("FACTOR");
        if(token.getValue().equals("identifier")||token.getValue().equals("integer_const")
                ||token.getValue().equals("literal_const")){
            previous_token = token; token = pularEspacosQuebras();
        }
        else if(token.getValue().equals("lparenthesis")){
            previous_token = token; token = pularEspacosQuebras();
            while(!token.getValue().equals("rparenthesis")){
                if(token.getValue().equals("EOF")){
                    throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
                }
                expression();
            }
        }
        else{
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
    }
    
    public void expression() throws Exception{
        simple_expr();
        expression_2();
    } 
    
    public void expression_2() throws Exception{
        if(token.getValue().equals("relop")){
            previous_token = token; token = pularEspacosQuebras();
            simple_expr();
        }
    }
    
    public void term_2() throws Exception{
        if(token.getValue().equals("mulop")){
            previous_token = token; token = pularEspacosQuebras();
            factor_a();
            term_2();
        }
    }
    
    public void simple_expr_2() throws Exception{
        if(token.getValue().equals("addop")){
            previous_token = token; token = pularEspacosQuebras();
            term();
            simple_expr_2();
        }
        else if(!token.getValue().equals("semicolon")&&!token.getValue().equals("rparenthesis")
                &&!token.getValue().equals("relop")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
    }
    
    
    
    public Pair<String, String> pularEspacosQuebras() {                 
        Pair<String, String> token = proximoToken();        
        if(!token.getValue().equals("space") && !token.getValue().equals("LB")){
//            System.out.println(token.getKey());
            return token;}
        return pularEspacosQuebras();
    }
    
    public Pair<String, String> proximoToken() {
        Pair<String, String> proximoToken = lexico.proximoToken();
        if(proximoToken.getValue().equals("LB")) linhaAtual++;
        return proximoToken;
    }
    
}