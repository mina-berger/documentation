package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Nombre implements Categorie {
  SG("sg."), PL("pl."), DU("du.");

  String abbr;
  Nombre(String abbr){
    this.abbr = abbr;
  }

  /**
   *
   * @return
   */
  @Override
  public String getAbbr() {
    return abbr;
  }

}
