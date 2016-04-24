package compilador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.*;

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
        grammar.put("var", Pattern.compile("^var$"));
        grammar.put("begin", Pattern.compile("^begin$"));
        grammar.put("end",Pattern.compile("^end$"));
        grammar.put("is",Pattern.compile("^is$"));
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
        grammar.put("parenthesis",Pattern.compile("(^[()]$)"));
//        grammar.put("bracket",Pattern.compile("^[{}]$"));
        grammar.put("comma",Pattern.compile("^,$"));
        grammar.put("semicolon",Pattern.compile("^;$"));
        grammar.put("bracket",Pattern.compile("(^\\{$)|(^\\}$)"));
        grammar.put("comment",Pattern.compile("^%$"));
        grammar.put("newline",Pattern.compile("\\n"));
        grammar.put("integer_const",Pattern.compile("(^[1-9]+[0-9]*$)|(^0$)"));
        grammar.put("identifier",Pattern.compile("(^[a-zA-Z][a-zA-Z0-9]{0,14}$)|(^_[a-zA-Z0-9]{1,14})"));
        grammar.put("literal_const",Pattern.compile("^\\{[^\\{\n]*\\}$"));
        grammar.put("unknown",Pattern.compile(".*"));
    }
    
    //Tabela de símbolos contendo identificador e tipo
    protected static HashMap<String, String> symbol_table = new HashMap<String, String>();
    
    //Lista com os lexemas capturados pelo parser
    protected static ArrayList <String> lexemes;
    
    public Lexico(){
        lexemes = new ArrayList<String>();
    }
    
    public void analiseLexica(){
        Parser parser = new Parser("teste.txt");
        String palavra = parser.proximaPalavra();
        
        while(!palavra.equals("EOF")){
            //Verifica se a palavra é a última da linha para separá-la da quebra de linha
            if(palavra.length() > 2 && palavra.substring(palavra.length()-2).equals("\\n")){
                lexemes.add(palavra.substring(0, palavra.length()-2));
                lexemes.add("\n");
            }
            //Verifica se é uma constante literal e concatena as palavras
            //para formar a constante, caso haja espaço (" ") entre elas
            else if(palavra.equals("{")){
                String prox_palavra = parser.proximaPalavra();
                while(!prox_palavra.equals("}") && !prox_palavra.equals("EOF")){
                    palavra += prox_palavra;
                    prox_palavra = parser.proximaPalavra();
                }
                lexemes.add(palavra+prox_palavra);
                //lexemes.add(prox_palavra);
            }
            else
                lexemes.add(palavra);
            palavra = parser.proximaPalavra();
        }
        
        Iterator<String> i = lexemes.iterator();
        String lex;
        
        //Verifica o padrões ao qual cada lexema pertence
        System.out.println("Tokens encontrados");
        while(i.hasNext()){
            lex = i.next();
            for(HashMap.Entry<String, Pattern> entry : grammar.entrySet()){
              Matcher m = entry.getValue().matcher(lex);
              if(m.matches()){
                  String localKey = entry.getKey();
                  if(localKey.equals("identifier")) {
                    if(symbol_table.get(lex) == null) {
                        symbol_table.put(lex, "identifier");
                    }   
                  }
                  System.out.println("Token: " + lex + " -> " + entry.getKey());
                  break;
              }
            }
        }
        System.out.println("Tabela de símbolos:");
        
        Iterator<Entry<String,String>> is = symbol_table.entrySet().iterator();
        
        while (is.hasNext()) {
            Entry<String, String> symbol = is.next();
            System.out.println("Name: " + symbol.getKey() + ", Type: " + symbol.getValue());
        }
    }
}