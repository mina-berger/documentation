package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Lang {
  LA("la"), ON("on"), ES("es"), FR("fr"), EN("en"), GR("gr"), IT("it");

  String abbr;
  Lang(String abbr){
    this.abbr = abbr;
  }
  public String getAbbr() {
    return abbr;
  }
}
