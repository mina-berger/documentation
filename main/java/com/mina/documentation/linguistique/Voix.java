package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Voix implements Categorie{
  ACT("act."), PASS("pass."), MOY("moy."), MP("m.p.");

  String abbr;
  Voix(String abbr){
    this.abbr = abbr;
  }
  public String getAbbr() {
    return abbr;
  }

}
