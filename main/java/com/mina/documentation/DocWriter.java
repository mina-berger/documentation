/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mina.documentation;

import static com.mina.documentation.DocUtil.simple;
import static com.mina.documentation.DocUtil.wash;
import com.mina.documentation.linguistique.Categorie;
import com.mina.documentation.linguistique.CategorieGroupe;
import com.mina.documentation.linguistique.MotGeneral;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author minaberger
 */
public class DocWriter {

  PrintStream out;

  public DocWriter(PrintStream out) {
    this.out = out;
  }
  public void quit(){
    out.flush();
  }

  public void println(String str) {
    out.println(str);
  }
  private String linkEn(String verbum){
    return "<a target='wiktionary_en' href='https://en.m.wiktionary.org/wiki/" + verbum + "'>(" + verbum + " en.)</a> ";
  }
  private String linkFr(String verbum){
    return "<a target='wiktionary_fr' href='https://fr.wiktionary.org/wiki/" + verbum + "'>(" + verbum + " fr.)</a> ";
  }

  public void print(Verbum verbum, List<Categorie> skipCategories) {
    String _verbum = verbum.getVerbum();
    if (verbum.isReserve()) {
      out.println("<li><strong>" + _verbum + "</strong> " + verbum.getExplanation());
    } else {
      ArrayList<String> list_en = new ArrayList<>();
      ArrayList<String> list_fr = new ArrayList<>();
      list_en.add(_verbum);
      
      //out.println("<li><strong>" + _verbum + "</strong> "
      //  + "<a target='wiktionary_en' href='https://en.m.wiktionary.org/wiki/" + _verbum + "'>word</a>");
      for (Verbum.Analysis analysis : verbum) {
        String root = analysis.getRoot();
        if(!list_en.contains(root)){
          list_en.add(root);
        }
        if(!list_fr.contains(root)){
          list_fr.add(root);
        }
        
        if (analysis.getCategories() != null) {
          for (CategorieGroupe categorieGroupe : analysis.getCategories()) {
            categorieGroupe.update();
            out.println("<li><strong>" + verbum.getVerbum() + "</strong> "
              + categorieGroupe.getInflectionAnte(skipCategories) + "<em>" + analysis.getRoot() + "</em> "
              + categorieGroupe.getInflectionPost(skipCategories) + analysis.getTraduction() + "</li>");
          }
        } else {
          for (String inflection : analysis) {
            out.println("<li><strong>" + verbum.getVerbum() + "</strong> "
              + inflection + " <em>" + analysis.getRoot() + "</em> " + analysis.getTraduction() + "</li>");
          }
        }

      }
      String link = "";
      for(String word:list_en){
        link += linkEn(word);
      }
      for(String word:list_fr){
        link += linkFr(word);
      }
      if(!link.isEmpty()){
        out.println("<li>" + link + "</li>");
      }
    }
  }
  public void printLost(ArrayList<String> lost){
      String link = "";
      for(String word:lost){
        link += linkEn(word);
        link += linkFr(word);
      }
      if(!link.isEmpty()){
        out.println("<li>" + link + "</li>");
      }    
  }
  public void print(List<MotGeneral> list, List<Categorie> skipCategories) {
      ArrayList<String> list_en = new ArrayList<>();
      for (MotGeneral mg : list) {
        //String root = ;
        String simple = simple(wash(mg.getRoot()));
        if(!simple.isEmpty() && !list_en.contains(simple)){
          list_en.add(simple);
        }
        String variation = String.join(", ", mg.getPrincipal().getVariations());
        String mpAutre = mg.getPrincipal().getAutres();
        String sens = mg.getPrincipal().getSens();
        sens = sens == null?"":sens;
        if(mg.isRoot()){
          out.println("<li><strong>" + mg.getMot()+ "</strong>"
            + (variation.isEmpty()?"":", <em>" + variation + "</em>") + " " 
            + mg.getPrincipal().getNature().getAbbr() + " "
            + (mpAutre.isEmpty()?"":mpAutre + " ")
            + mg.getInflectionPost(skipCategories) + sens + "</li>");
        }else{
          out.println("<li><strong>" + mg.getMot()+ "</strong> "
            + mg.getInflectionAnte(skipCategories) + "<em>" + mg.getRoot() 
            + (variation.isEmpty()?"":", " + variation) + "</em> "
            + (mpAutre.isEmpty()?"":mpAutre + " ")
            + mg.getInflectionPost(skipCategories) + sens + "</li>");
        }

      }
      String link = "";
      for(String word:list_en){
        link += linkEn(word);
        link += linkFr(word);
      }
      if(!link.isEmpty()){
        out.println("<li>" + link + "</li>");
      }
  }

}
