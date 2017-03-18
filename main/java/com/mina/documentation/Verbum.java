package com.mina.documentation;

import com.mina.documentation.Verbum.Analysis;
import com.mina.documentation.linguistique.Categorie;
import com.mina.documentation.linguistique.CategorieGroupe;
import java.util.ArrayList;

/**
 *
 * @author minaberger
 */
public class Verbum extends ArrayList<Analysis>{
  private final String verbum;
  private String expl;
  public Verbum(String verbum){
    this.verbum = verbum;
    expl = null;
  }
  public Verbum(String verbum, String expl){
    this.verbum = verbum;
    this.expl = expl;
  }
  
  public boolean isReserve(){
    return expl != null;
  }
  public String getExplanation(){
    return expl;
  }

  public String getVerbum() {
    return verbum;
  }

  public static class Analysis extends ArrayList<String> {
    ArrayList<CategorieGroupe> categorieGroupes;

    String root;
    String traduction;
    public Analysis(String root, String traduction){
      this.root = root;
      this.traduction = traduction;
    }
    public String getRoot(){
      return root;
    }
    public String getTraduction(){
      return traduction;
    }

    public ArrayList<CategorieGroupe> getCategories() {
      return categorieGroupes;
    }

    public void setCategories(ArrayList<CategorieGroupe> categorieGroupes) {
      this.categorieGroupes = categorieGroupes;
    }
    

  }
  

}
