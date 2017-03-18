package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Nombre implements Categorie {
  SG("sg."), PL("pl.");

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
