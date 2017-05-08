package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Autre implements Categorie{
  POET("poét."), INDECL("indecl."), ARCH("arch."), RARE("rare"), REL("rél."), 
  ATTIC("attic"), AEOLIC("aeolic"), CONTR("contr."), DORIC("doric"), EPIC("epic"), 
  IONIC("ionic"), HOMERIC("homeric"), PROCL("procl."), ENCL("encl."),
  UNAUG("unaug."), NUMOUV("nu-mouv."), APOCOPIC("apocopic"), DEMONST("demonst."),
  ADVERBIAL("adverbial"), INFORMAL("informal"), FORMAL("formal"), ALT("alt."),
  FAUTES("fautes"), POSTP("postp."), MEDIEVAL("Médiéval"), IMPERS("impers.");

  String abbr;
  Autre(String abbr){
    this.abbr = abbr;
  }
  public String getAbbr() {
    return abbr;
  }

}
