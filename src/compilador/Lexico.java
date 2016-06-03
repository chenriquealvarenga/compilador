package compilador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.*;
import javafx.util.Pair;

/**
 *
 * @author carlos
 */
public class Lexico {
    //HashMap com os tokens e suas respectivas expressōes regulares.
    private static final LinkedHashMap<String, Pattern> grammar;
    static
    {
        grammar = new LinkedHashMap<String, Pattern>();
        grammar.put("space", Pattern.compile(" +"));
        grammar.put("LB", Pattern.compile("[\r\n]"));
        grammar.put("EOF", Pattern.compile("EOF"));
        grammar.put("var", Pattern.compile("^var$"));
        grammar.put("begin", Pattern.compile("^begin$"));
        grammar.put("end",Pattern.compile("^end$"));
        grammar.put("is",Pattern.compile("^is$"));
        grammar.put("not",Pattern.compile("^not$"));
        grammar.put("type",Pattern.compile("(^int$)|(^string$)"));
        grammar.put("if",Pattern.compile("^if$"));
        grammar.put("then",Pattern.compile("^then$"));
        grammar.put("else",Pattern.compile("^else$"));
        grammar.put("do",Pattern.compile("^do$"));
        grammar.put("while",Pattern.compile("^while$"));
        grammar.put("read",Pattern.compile("^in$"));
        grammar.put("write",Pattern.compile("^out$"));
        grammar.put("assign",Pattern.compile("^:=$"));
        grammar.put("relop",Pattern.compile("(^=$)|(^>$)|(^<$)|(^>=$)|(^<=$)|(^<>$)"));
        grammar.put("addop",Pattern.compile("(^\\+$)|(^-$)|(^or$)"));
        grammar.put("mulop",Pattern.compile("(^\\*$)|(^/$)|(^and$)"));
        grammar.put("lparenthesis",Pattern.compile("(^[(]$)"));
        grammar.put("rparenthesis",Pattern.compile("(^[)]$)"));
        grammar.put("lbracket",Pattern.compile("^[{]$"));
        grammar.put("rbracket",Pattern.compile("^[}]$"));
        grammar.put("comma",Pattern.compile("^,$"));
        grammar.put("semicolon",Pattern.compile("^;$"));
        grammar.put("comment",Pattern.compile("^%$"));
        grammar.put("integer_const",Pattern.compile("(^[1-9]+[0-9]*$)|(^0$)"));
        grammar.put("identifier",Pattern.compile("(^[a-zA-Z][a-zA-Z0-9]{0,14}$)|(^_[a-zA-Z0-9]{1,14})"));
    }
    
    //Tabela de símbolos contendo identificador e tipo
    protected static HashMap<String, String> symbol_table = new HashMap<String, String>();
    
    //Lista com os tokens capturados pelo parser
    private ArrayList <Pair<String,String>> tokens;
    private String filename;
    private int cabecaFila = 1;
    
    public Lexico(String filename){
        tokens = new ArrayList<Pair<String,String>>();
        this.filename = filename;
    }
    
    public void analiseLexica() throws Exception{
        LeArquivo parser = new LeArquivo(filename);        
        int linha = 1;
        String lexema = "";
        do {
            boolean match = false;
            lexema = parser.proximaPalavra();           
            
            for(HashMap.Entry<String, Pattern> entry : grammar.entrySet()){
              Matcher m = entry.getValue().matcher(lexema);
              if(m.matches()){
                  String localKey = entry.getKey();
                  if(localKey.equals("identifier")) {
                    if(symbol_table.get(lexema) == null) {
                        symbol_table.put(lexema, "identifier");
                    }
                  }
                  else if(localKey.equals("LB")){
                      linha++;
                  }
                  else if(localKey.equals("lbracket")){
                      String proxima = parser.proximaPalavra();
                      while(!proxima.equals("}")){
                          lexema += proxima;
                          if(proxima.equals("EOF")||proxima.equals("{")
                                  ||proxima.equals("\r")){
                              throw new Exception("Token não reconhecido em: " + lexema + ", linha " + linha);
                          }
                          else{
                              proxima = parser.proximaPalavra();
                          }
                      }
                      lexema += proxima;
                      localKey = "literal_const";
                      
                  }
                  tokens.add(new Pair<>(lexema, localKey));
//                  System.out.println("Token: " + lexema + " -> " + localKey);
                  match = true;
                  break;
              }
            }
            
             if(!match) {
                 throw new Exception("Token não reconhecido em: " + lexema + ", linha " + linha);
             }
            
        } while(!lexema.equals("EOF"));
        
    }

    public Pair<String, String> proximoToken() {
        if(cabecaFila > tokens.size()) return new Pair<>("EOF", "EOF");
        
        Pair<String, String> retObj =  tokens.get((cabecaFila-1));        
        cabecaFila++;
        return retObj;               
    }
    
    public String buscaTabelaSimbolos(String chave) {
        return symbol_table.get(chave);
    }
    
}