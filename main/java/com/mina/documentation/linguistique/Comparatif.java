package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Comparatif implements Categorie{
  COMPAR("compar."), SUPERL("superl.");

  String abbr;
  Comparatif(String abbr){
    this.abbr = abbr;
  }
  public String getAbbr() {
    return abbr;
  }

}
