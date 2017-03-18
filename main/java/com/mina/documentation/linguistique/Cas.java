package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Cas implements Categorie{
  NOM("nom."), VOC("voc."), ACC("acc."), GEN("gén."), DAT("dat."), ABL("abl."), LOC("loc.");

  String abbr;
  Cas(String abbr){
    this.abbr = abbr;
  }
  @Override
  public String getAbbr() {
    return abbr;
  }

}
