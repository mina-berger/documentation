package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Conjugaison implements Categorie{
  CONJ1("conj1"), CONJ2("conj2");

  String abbr;
  Conjugaison(String abbr){
    this.abbr = abbr;
  }
  @Override
  public String getAbbr() {
    return abbr;
  }

}
