package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Personne implements Categorie{
  PREM("1."), DEUX("2."), TROI("3.");

  String abbr;
  Personne(String abbr){
    this.abbr = abbr;
  }
  public String getAbbr() {
    return abbr;
  }

}
