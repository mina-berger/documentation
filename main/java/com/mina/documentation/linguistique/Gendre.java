package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Gendre implements Categorie{
  M("m."), F("f."), N("n."), UNKNOWN("?"), MFN("m.f.n."), MF("m.f.");

  String abbr;
  Gendre(String abbr){
    this.abbr = abbr;
  }
  public String getAbbr() {
    return abbr;
  }

}
