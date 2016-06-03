package compilador;

/**
 *
 * @author carlos
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class LeArquivo {
  private FileReader file;
  private BufferedReader data;
  private String line;
  private StringTokenizer st;
  private static final String delim = "\r\n\t,;+-*/%(){} ";
  private boolean bracket= false;

  public LeArquivo(String name) {

    try {
      //Conteudo do arquivo considerando encode em UTF-8 (padrao)
      String contentArchive = new String(Files.readAllBytes(Paths.get(name)), StandardCharsets.UTF_8);
      
      //Transformando as possibilidade de separador alem de operacoes em espacos      
      //contentArchive = contentArchive.replace(",", " ");
      
      //Adicionando espaco antes e depois de relops e assigns para separa-los
      contentArchive = contentArchive.replaceAll("<=", " <= ");
      contentArchive = contentArchive.replaceAll(">=", " >= ");
      contentArchive = contentArchive.replaceAll("<>", " <> ");
      contentArchive = contentArchive.replaceAll(":=", " := ");
      contentArchive = contentArchive.replaceAll("<", " < ");
      contentArchive = contentArchive.replaceAll(">", " > ");
      contentArchive = contentArchive.replaceAll("<  >", "<>");
      contentArchive = contentArchive.replaceAll("< =", "<=");
      contentArchive = contentArchive.replaceAll("> =", ">=");
      
      
      //Condensando espacos multiplos em unificados
      contentArchive = contentArchive.replaceAll(" +", " ");
      
      st= new StringTokenizer(contentArchive, delim, true);
      
    }
    catch (Exception e) {
	  System.out.println(e);
    }
  }
  
  public String proximaPalavra() {
      if(!st.hasMoreTokens()) return "EOF";      
      return st.nextToken();      
  }
  
}