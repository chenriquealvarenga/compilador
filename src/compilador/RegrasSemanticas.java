/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;

/**
 *
 * @author carlos
 */
public class RegrasSemanticas {
    public static final int DECLARACAO = 1;
    public static final int ATRIBUICAO = 2;
    public static final int EXPRESSAO = 3;
    public static final int ESCRITA = 4;
    public static final int LEITURA = 5;
    private ArrayList<String> listaDeclaracoes;
    private int regra;
    private Tipo tipo;
    private boolean operacaoLogica;
    private boolean operacao;

    public RegrasSemanticas(int regra) {
        this.regra = regra;
        this.tipo = new Tipo("VAZIO");
    }
    
    public Tipo getTipo(){
        return tipo;
    }
    
    public static RegrasSemanticas declaracao(){
        RegrasSemanticas r = new RegrasSemanticas(DECLARACAO);
        r.listaDeclaracoes = new ArrayList<String>();
        return r;
    }
    
    public static RegrasSemanticas atribuicao (Tipo tipo){
        RegrasSemanticas r = new RegrasSemanticas(ATRIBUICAO);
        r.tipo = tipo;
        return r;
    }
    
    public static RegrasSemanticas expressao (Tipo tipo){
        RegrasSemanticas r = new RegrasSemanticas(EXPRESSAO);
        r.tipo = tipo;
        return r;
    }
    
    public static RegrasSemanticas escrita (){
        RegrasSemanticas r = new RegrasSemanticas(ESCRITA);
        r.tipo = Tipo.vazio();
        return r;
    }
    
    public static RegrasSemanticas leitura (){
        RegrasSemanticas r = new RegrasSemanticas(LEITURA);
        r.tipo = Tipo.vazio();
        return r;
    }
    
    public void addId(String id){
        listaDeclaracoes.add(id);
    }
    
    public void setTiposDeclaracoes(Sintatico sintatico){
        if(tipo.isErro()) return;
        for(String id: listaDeclaracoes){
            sintatico.symbol_table.put(id, tipo);
        }
    }
    
    public boolean verificarTipo(Tipo tipo){
        if(this.tipo.isTipo(Tipo.valorLogico()) && this.operacaoLogica) return true;
        if(this.tipo.isTipo(Tipo.vazio())) return true;
        return this.tipo.isTipo(tipo);
    }
    
    public void verificarAtribuicao(RegrasSemanticas r1, RegrasSemanticas r2, int numeroLinha){
         if(r1.verificarTipo(r2.getTipo())){
             this.setTipo(r1.getTipo());
         }else{
             this.setTipo(Tipo.erro());
             System.out.println("ERRO: tipo da expressao incompativel com identificador (Linha:"+numeroLinha+")");
         }
     }
    
    public boolean isDeclaracao(){
        return regra == DECLARACAO;
    }
    
    public boolean isAtribuicao(){
        return regra == ATRIBUICAO;
    }
    
    public boolean isExpressao(){
        return regra == EXPRESSAO;
    }
    
    public boolean isEscrita(){
        return regra == ESCRITA;
    }
    
    public boolean isLeitura(){
        return regra == LEITURA;
    }
    
    public void setTipo(Tipo tipo){
        if(!this.tipo.isErro() && !tipo.isVazio()){
            this.tipo = tipo;
        }
    }

    public void operacaoLogica(){
        this.operacaoLogica = true;
    }
    public void operacao(){
        this.operacao = true;
    }
    
    public boolean isOperacaoLogica(){
        return operacaoLogica;
    }
    
    public boolean isOperacao(){
        return operacao;
    }
    
    public void verificarLeitura(RegrasSemanticas regra, int numeroLinha){
        if(!regra.verificarTipo(Tipo.inteiro())&&!regra.verificarTipo(Tipo.string())){
            this.setTipo(Tipo.erro());
            System.out.println("ERRO: argumento de leitura incorreto (Linha:"+numeroLinha+")");
        }else{
            this.setTipo(regra.getTipo());
        }
    }
    
    public void verificarWhileIf(int numeroLinha){
        if(!this.verificarTipo(Tipo.valorLogico())){
            this.setTipo(Tipo.erro());
            System.out.println("ERRO: esperada expressao logica (Linha:"+numeroLinha+")");
        }
    }
    
    public void verificarCondicao(){
        if(this.operacaoLogica){
            this.tipo = Tipo.valorLogico();
        }else{
            this.tipo = Tipo.erro();
        }
    }
    
    public void verificarLiteral(Sintatico sintatico){
        if(this.isExpressao()){
            if(this.verificarTipo(Tipo.string())){
                this.setTipo(Tipo.string());
            }else{
                this.setTipo(Tipo.erro());
                System.out.println("ERRO: operandos incompativeis (Linha:"+sintatico.linhaAtual+")");
            }
        }else{ //caso do read e da atribuição
            this.setTipo(Tipo.string());
        }
    }
    
     public void verificarNumero(Sintatico sintatico){
        if(this.isExpressao()){
            if(this.verificarTipo(Tipo.inteiro())){
                this.setTipo(Tipo.inteiro());
            }else{
                this.setTipo(Tipo.erro());
                System.out.println("ERRO: operandos incompativeis (Linha:"+sintatico.linhaAtual+")");
            }
        }else{ //caso do read e da atribuição
            this.setTipo(Tipo.inteiro());
        }
    }
     
    public void verificarId(Sintatico sintatico, Pair tkn){
        if(this.isDeclaracao()){
            if(!sintatico.symbol_table.containsKey((String)tkn.getKey())){
                this.addId((String)tkn.getKey());
            }else{
                this.setTipo(Tipo.erro());
                System.out.println("ERRO: identificador "+tkn.getKey()+" ja foi declarado. (Linha:"+sintatico.linhaAtual+")");
            }
        }else if(!sintatico.symbol_table.containsKey((String)tkn.getKey())){
            this.setTipo(Tipo.erro());
            System.out.println("ERRO: identificador " +tkn.getKey()+ " nao foi declarado (Linha:"+sintatico.linhaAtual+")");
        }else if(this.isExpressao()){
            if(this.verificarTipo(sintatico.symbol_table.get((String)tkn.getKey()))){
                this.setTipo(sintatico.symbol_table.get((String)tkn.getKey()));
            }else{
                this.setTipo(Tipo.erro());
                System.out.println("ERRO: operandos incompativeis (Linha:"+sintatico.linhaAtual+")");
            }
        }else{ //caso do read e da atribuição
            this.setTipo(sintatico.symbol_table.get((String)tkn.getKey()));
        }
    }
    
    public void verificarExpressao(RegrasSemanticas r1, RegrasSemanticas r2, int numeroLinha){
        if((r1.isOperacao() || r1.isOperacaoLogica()) && !r1.getTipo().isInteiro()){ //operações podem apenas possuir operandos numéricos
            this.setTipo(Tipo.erro());
            System.out.println("ERRO: operadores nao sao do tipo int (Linha:"+numeroLinha+")");
        }else if((r2.isOperacaoLogica() || r2.isOperacao()) && !r2.getTipo().isInteiro()){//operações podem apenas possuir operandos numéricos
            this.setTipo(Tipo.erro());
            System.out.println("ERRO: operadores nao sao do tipo int (Linha:"+numeroLinha+")");
        }else{ 
            if(r1.isOperacao() || r2.isOperacao()){
                this.operacao();
            }
            if(r1.isOperacaoLogica() || r2.isOperacaoLogica()){
                this.operacaoLogica();
            }
            this.setTipo(r1.getTipo());
        }
        
    }
}
