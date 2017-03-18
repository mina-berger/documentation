/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mina.documentation.apl;

import com.mina.documentation.DocDriver;
import com.mina.documentation.DocWriter;
import com.mina.documentation.Verbum;
import com.mina.documentation.linguistique.Categorie;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author minaberger
 */
public class NorseTranslator extends DocDriver {
  DocWriter writer;
  static List<Categorie> skipCategories = Arrays.asList();
  public NorseTranslator(PrintStream out){
    writer = new DocWriter(out);
  }
  public void translate(String text){
    String[] words = text.split(" ");
    ArrayList<Verbum> verbi = new ArrayList<>();
    for(String word:words){
      if(word.equals("/")){
        continue;
      }
      if(word.endsWith(".") || word.endsWith(",") || word.endsWith("?") || 
         word.endsWith("!") || word.endsWith(";")){
        word = word.substring(0, word.length() - 1);
      }
      verbi.add(findWord(word));
    }
    writer.println("<ul>");
    for(Verbum verbum:verbi){
      writer.print(verbum, skipCategories);
    }
    writer.println("</ul>");
  }
  public Verbum findWord(String word){
    open("http://www.perseus.tufts.edu/hopper/morph?l=" + word + "&la=non");
    int analysisCount = count("//div[@class='analysis']");
    Verbum verbum = new Verbum(word);
    if(analysisCount == 0){
      //System.out.println("empty:" + word);
    }
    for(int i = 1;i <= analysisCount;i++){
      Verbum.Analysis analysis = new Verbum.Analysis(
        text("//div[@class='analysis'][" + i + "]/div/div/h4"),
        text("//div[@class='analysis'][" + i + "]/div/div/span")
      );
      //System.out.println(analysis.getRoot() + ":" + analysis.getTraduction());
      int countInflection = count("//div[@class='analysis'][" + i + "]//tr");
      if(countInflection == 0){
        //System.out.println("empty:" + word + ":" + analysis.getRoot() + ":" + analysis.getTraduction());
      }
      for(int j = 1;j <= countInflection;j++){
        analysis.add(text("//div[@class='analysis'][" + i + "]//tr[" + j + "]/td[2]"));
      }
      verbum.add(analysis);
    }
    return verbum;
  }
  
  public static void main(String[] args){
    NorseTranslator nt = new NorseTranslator(System.out);
    nt.translate("fregnið");
    nt.quit(10);
  }
  
}
