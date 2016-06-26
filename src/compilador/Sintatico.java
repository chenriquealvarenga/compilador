package compilador;

import javafx.util.Pair;
import java.util.HashMap;

/**
 *
 * @author Natália
 */
public class Sintatico {
    
    Integer linhaAtual = 1;
    private Lexico lexico;
    private Pair<String, String> token = null;
    private Pair<String, String> previous_token = null;
    public HashMap<String,Tipo> symbol_table = null;
    
    public Sintatico(Lexico lexico) {
        this.lexico = lexico;    
        this.symbol_table = new HashMap<String,Tipo>();
    }
    
    public void analiseSintaticaeSemantica() throws Exception {
        token = lexico.proximoToken();
        RegrasSemanticas regra = new RegrasSemanticas(0);
        program(regra);
    }
    
    public void program(RegrasSemanticas regra) throws Exception{
        try {
            if(token.getValue().equals("var")){
//                System.out.println("var");
                previous_token = token; token = pularEspacosQuebras();
                decl_list(regra);

            }
            else{
                throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + token.getKey());
            }
            if(token.getValue().equals("begin")){
//                System.out.println("begin");
                previous_token = token; token = pularEspacosQuebras();
                stmt_list("stmt_program", regra);

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

    public void decl_list(RegrasSemanticas regra) throws Exception{
//        System.out.println("decl_list");
        while(!token.getValue().equals("begin")){
            if(token.getValue().equals("EOF")){
                throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
            }
            else{
                decl(regra);
                previous_token = token; token = pularEspacosQuebras();
            }
        }
    }
    

    public void decl(RegrasSemanticas regra) throws Exception{
//        System.out.println("decl");
        RegrasSemanticas r = RegrasSemanticas.declaracao();
        ident_list(r);
        if(token.getValue().equals("is")){
            previous_token = token; token = pularEspacosQuebras();
            type(r);
            r.setTiposDeclaracoes(this);
            regra.setTipo(r.getTipo());
        }
        else{
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
    }
    

    public void type(RegrasSemanticas regra) throws Exception{
//        System.out.println("type");
        if(!token.getValue().equals("type")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        else if(token.getKey().equals("int")){
            regra.setTipo(Tipo.inteiro());
        }
        else{
            regra.setTipo(Tipo.string());
        }
        previous_token = token; token = pularEspacosQuebras();
    }
    

    public void ident_list(RegrasSemanticas regra) throws Exception{
//        System.out.println("ident_list");
        while(token.getValue().equals("identifier") && !token.getValue().equals("is")){
            regra.verificarId(this, token);
            previous_token = token; token = pularEspacosQuebras();
            if(token.getValue().equals("comma")){
                previous_token = token; token = pularEspacosQuebras();
            }
            else if(!token.getValue().equals("is")){
                throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
            }
        }
    }
    

    public void stmt_list(String tipoStmt, RegrasSemanticas regra) throws Exception{
//        System.out.println("STMTLIST");
        if(tipoStmt.equals("stmt_if")){
            while(!token.getValue().equals("end")&&!token.getValue().equals("else")){
                if(token.getValue().equals("EOF")){
                    throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
                }
                else{
                    stmt(regra);
                    previous_token = token; token = pularEspacosQuebras();
                }
            }
        }
        else if(tipoStmt.equals("stmt_program")){
            while(!token.getValue().equals("end")){
                if(token.getValue().equals("EOF")){
                    throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
                }
                else{
                    stmt(regra);
                    previous_token = token; token = pularEspacosQuebras();
                }
            }
        }
        else if(tipoStmt.equals("stmt_while")){
            while(!token.getValue().equals("while")){
                if(token.getValue().equals("EOF")){
                    throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
                }
                else{
                    stmt(regra);
                    previous_token = token; token = pularEspacosQuebras();
                }
            }
        }
    }
    

    public void stmt(RegrasSemanticas regra) throws Exception{
//        System.out.println("STMT");
        switch(token.getValue()){
            case "identifier":
                previous_token = token; token = pularEspacosQuebras();
                assign_stmt(regra);
                break;
            case "if":
                previous_token = token; token = pularEspacosQuebras();
                if_stmt(regra);
                break;
            case "do":
                previous_token = token; token = pularEspacosQuebras();
                do_stmt(regra);
                break;
            case "read":
                previous_token = token; token = pularEspacosQuebras();
                read_stmt(regra);
                break;
            case "write":
                previous_token = token; token = pularEspacosQuebras();
                write_stmt(regra);
                break;
        }
    }
    

    public void write_stmt(RegrasSemanticas regra) throws Exception{
//        System.out.println("WRITE_STMT");
        RegrasSemanticas r = RegrasSemanticas.escrita();
        writable(r);
        regra.setTipo(r.getTipo());
        if(!token.getValue().equals("semicolon")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
    }
    

    public void writable(RegrasSemanticas regra) throws Exception{
//        System.out.println("WRITABLE");
        simple_expr(regra);
        previous_token = token; token = pularEspacosQuebras();
        
    }
    

    public void read_stmt(RegrasSemanticas regra) throws Exception{
//        System.out.println("READ_STMT");
        RegrasSemanticas r = RegrasSemanticas.leitura();
        if(!token.getValue().equals("lparenthesis")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
        if(!token.getValue().equals("identifier")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        
        r.verificarId(this, token);
        regra.verificarLeitura(r, linhaAtual);
        
        previous_token = token; token = pularEspacosQuebras();
        if(!token.getValue().equals("rparenthesis")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
        if(!token.getValue().equals("semicolon")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
    }
    

    public void do_stmt(RegrasSemanticas regra) throws Exception{
//        System.out.println("DO_STMT");
        stmt_list("stmt_while", regra);
        stmt_suffix(regra);
    }
    

    public void stmt_suffix(RegrasSemanticas regra) throws Exception{
        if(!token.getValue().equals("while")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
        condition(regra);
        regra.verificarWhileIf(linhaAtual);
    }
    

    public void if_stmt(RegrasSemanticas regra) throws Exception{
//        System.out.println("IF_STMT");
        condition(regra);
        regra.verificarWhileIf(linhaAtual);
        if(!token.getValue().equals("then")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
        stmt_list("stmt_if", regra);
        if_stmt_2(regra);
    }
    
//    FEITO
    public void if_stmt_2(RegrasSemanticas regra) throws Exception{
//        System.out.println("IF_STMT_2");
        if(token.getValue().equals("end")){
//            previous_token = token; token = pularEspacosQuebras();
        }
        else if(token.getValue().equals("else")){
            previous_token = token; token = pularEspacosQuebras();
            stmt_list("stmt_program", regra);
        }
        else{
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
    }
    
//    FEITO
    public void condition(RegrasSemanticas regra) throws Exception{
//        System.out.println("CONDITION");
        regra.setTipo(Tipo.inteiro());
        expression(regra);
        regra.verificarCondicao();
        previous_token = token; token = pularEspacosQuebras();
    }
    
//    FEITO
    public void assign_stmt(RegrasSemanticas regra) throws Exception{
//        System.out.println("ASSIGN_STMT");
        RegrasSemanticas r1 = RegrasSemanticas.atribuicao(Tipo.vazio());
        RegrasSemanticas r2 = RegrasSemanticas.atribuicao(Tipo.vazio());
        r1.verificarId(this, previous_token);
        if(!token.getValue().equals("assign")){
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
        previous_token = token; token = pularEspacosQuebras();
        simple_expr(r2);
        regra.verificarAtribuicao(r1, r2, linhaAtual);
    }
    
//    FEITO
    public void simple_expr(RegrasSemanticas regra) throws Exception{
//        System.out.println("SIMPLE_EXPR");
        term(regra);
        simple_expr_2(regra);
    }
    
//    FEITO
    public void term(RegrasSemanticas regra) throws Exception{
//        System.out.println("TERM");
        factor_a(regra);
        term_2(regra);
    }
    
//    FEITO
    public void factor_a(RegrasSemanticas regra) throws Exception{
//        System.out.println("FACTOR_A");
        if(token.getValue().equals("not")||token.getKey().equals("-")){
            previous_token = token; token = pularEspacosQuebras();
        }
        factor(regra);
    }
    
//    FEITO
    public void factor(RegrasSemanticas regra) throws Exception{
//        System.out.println("FACTOR");
        if(token.getValue().equals("identifier")){
            regra.verificarId(this,token);
            previous_token = token; token = pularEspacosQuebras();
        }
        else if(token.getValue().equals("integer_const")){
            regra.verificarNumero(this);
            previous_token = token; token = pularEspacosQuebras();
        }   
        else if(token.getValue().equals("literal_const")){
            regra.verificarLiteral(this);
            previous_token = token; token = pularEspacosQuebras();
        }
        else if(token.getValue().equals("lparenthesis")){
            previous_token = token; token = pularEspacosQuebras();
            while(!token.getValue().equals("rparenthesis")){
                if(token.getValue().equals("EOF")){
                    throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
                }
                expression(regra);
            }
        }
        else{
            throw new Exception("ERRO NA LINHA " + linhaAtual + ": " + previous_token.getKey() + " " + token.getKey());
        }
    }
    
//    FEITO
    public void expression(RegrasSemanticas regra) throws Exception{
        RegrasSemanticas r1 = RegrasSemanticas.expressao(regra.getTipo());
        RegrasSemanticas r2 = RegrasSemanticas.expressao(regra.getTipo());
        simple_expr(r1);
        expression_2(r2);
        regra.verificarExpressao(r1, r2, linhaAtual);
    } 
    
//    FEITO
    public void expression_2(RegrasSemanticas regra) throws Exception{
        if(token.getValue().equals("relop")){
            previous_token = token; token = pularEspacosQuebras();
            regra.operacao();
            regra.operacaoLogica();
            simple_expr(regra);
        }
    }
    
//    FEITO (CONFERIR)
    public void term_2(RegrasSemanticas regra) throws Exception{
        if(token.getValue().equals("mulop")){
            previous_token = token; token = pularEspacosQuebras();
            regra.operacao();
            if(previous_token.getKey().equals("and")){
                regra.operacaoLogica();
            }
            factor_a(regra);
            term_2(regra);
        }
    }
    
//    FEITO (CONFERIR)
    public void simple_expr_2(RegrasSemanticas regra) throws Exception{
        if(token.getValue().equals("addop")){
            previous_token = token; token = pularEspacosQuebras();
            regra.operacao();
            if(previous_token.getKey().equals("or")){
                regra.operacaoLogica();
            }
            term(regra);
            simple_expr_2(regra);
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