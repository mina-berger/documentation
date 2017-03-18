package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Mode implements Categorie{
  IND("ind."), SUBJ("subj."), IMPER("imper."), INF("inf."), PART("part."), GER("gér"), SUPIN("supin.");

  String abbr;
  Mode(String abbr){
    this.abbr = abbr;
  }
  public String getAbbr() {
    return abbr;
  }

}
