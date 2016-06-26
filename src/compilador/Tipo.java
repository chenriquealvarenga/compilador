/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author carlos
 */
public class Tipo {

    
    private String tipo;

    public Tipo(String tipo) {
        this.tipo = tipo;
    }
    
    public static Tipo inteiro(){
        return new Tipo("INTEIRO");
    }
    
    public static Tipo string(){
        return new Tipo("STRING");
    }
    
    public static Tipo valorLogico(){
        return new Tipo("VALOR_LOGICO");
    }
    
    public static Tipo erro(){
        return new Tipo("ERRO");
    }
    
    public static Tipo vazio(){
        return new Tipo("VAZIO");
    }
    
    public boolean isInteiro(){
        return tipo.equals("INTEIRO");
    }
    
    public boolean isString(){
        return tipo.equals("STRING");
    }
    
    public boolean isValorLogico(){
        return tipo.equals("VALOR_LOGICO");
    }
    
    public boolean isErro(){
        return tipo.equals("ERRO");
    }
    
    public boolean isVazio(){
        return tipo.equals("VAZIO");
    }
    
    public String getTipo(){
        return tipo;
    }
    
    public boolean isTipo(Tipo tipo){
        return this.tipo.equals(tipo.getTipo());
    }
}