package compilador;

/**
 *
 * @author carlos
 */

import java.io.*;
import java.util.StringTokenizer;

public class Parser {
  private FileReader file;
  private BufferedReader data;
  private String line;
  private StringTokenizer st;
  private static final String delim= "\t,;+-*/%(){} ";
  private boolean bracket= false;

  public Parser(String name) {

    try {
      file= new FileReader(name);
      data= new BufferedReader (file);
      line= data.readLine();
      if (line == null)
         st= null;
      else st= new StringTokenizer(line, delim, true);
    }
    catch (Exception e) {
	  System.out.println(e);
    }
  }
  

  public String proximaPalavra() {

    if (st.hasMoreTokens()) {
       String s= st.nextToken();
       if (s.equals("{"))
          bracket = true;
       if(s.equals("}"))
           bracket = false;
       if (s.equals(" ") && !bracket){
          return proximaPalavra();
       }
       else return (st.hasMoreTokens() ? s : s + "\\n");
    }
    else {
     try {
        line= data.readLine();
        if (line == null)
           return "EOF";
        else {
		  st = new StringTokenizer(line, delim, true);
		  return proximaPalavra();
        }
      }
      catch (Exception e) {
		System.out.println(e);
      }
      return "EOF";
    }
  }
}