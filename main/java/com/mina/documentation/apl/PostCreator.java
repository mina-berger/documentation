package com.mina.documentation.apl;

import static com.mina.documentation.DocUtil.wash;
import com.mina.documentation.DocWriter;
import static com.mina.documentation.linguistique.Autre.INDECL;
import com.mina.documentation.linguistique.Categorie;
import static com.mina.documentation.linguistique.Mode.IND;
import com.mina.documentation.linguistique.MotGeneral;
import com.mina.documentation.linguistique.MotPrincipal;
import static com.mina.documentation.linguistique.Nature.NOUN;
import static com.mina.documentation.linguistique.Nature.VERBE;
import com.mina.documentation.linguistique.ReserveGeneral;
import com.mina.documentation.linguistique.ReservePrincipal;
import static com.mina.documentation.linguistique.Voix.ACT;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author minaberger
 */
public abstract class PostCreator {
  static List<Categorie> skipCategories = Arrays.asList(
    VERBE, NOUN, IND, ACT, INDECL);
  File src;
  DocWriter out;
  ReserveGeneral rg;
  ReservePrincipal rp;
  public PostCreator(File src, PrintStream ps){
    this.src = src;
    this.out = new DocWriter(ps);
    rg = new ReserveGeneral();
    rp = new ReservePrincipal();
  }
  protected boolean updateMp(MotPrincipal mp){
    return rp.update(mp);
  }
  
  protected ArrayList<String> readFile(){
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader(src));
      ArrayList<String> list = new ArrayList<>();
      while(in.ready()){
        String line = in.readLine();
        line = line.trim();
        if(line.startsWith("//")){
          continue;
        }
        list.add(line);
      }
      in.close();
      return list;
    } catch (IOException ex) {
        throw new IllegalStateException(ex);
    }
  }
  public abstract List<MotGeneral> find(String clef); 
  public abstract void quit(); 
  public abstract void setReserve(ReserveGeneral rg, ReservePrincipal rp);
  public void translate(){
    setReserve(rg, rp);
    ArrayList<String> lines = readFile();

    while(!lines.isEmpty()){
      String line = lines.remove(0);
      if(line.isEmpty()){
        continue;
      }
      out.println("<h3>" + line + "</h3>");
      printLine(line);
    }
  }
  public void printLine(String text) {
    String[] words = text.split(" ");
    //ArrayList<Verbum> verbi = new ArrayList<>();
    ArrayList<MotGeneral> list = new ArrayList<>();
    ArrayList<String> lost = new ArrayList<>();
    for (String word : words) {
      word = wash(word).toLowerCase();
      //word = washBy(langS, word);
      if (word.isEmpty()) {
        continue;
      }
      System.out.println("[find] : " + word);
      List<MotGeneral> mots = rg.get(word);
      if (mots == null || mots.isEmpty()) {
        mots = find(word);
      }
      if (mots == null || mots.isEmpty()) {
        lost.add(word);
      } else {
        list.addAll(mots);
      }
    }
    out.println("<ul>");
    out.printLost(lost);
    out.print(list, skipCategories);
    out.println("</ul>");
  }
  
  /*public static void main(String[] args) throws FileNotFoundException{
    PrintStream out = new PrintStream(new File("doc/post_trg"));
    
    PostCreator2 creator = new PostCreator2();
    creator.print(out);

  }*/
  
}
