package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Temps implements Categorie{
  PRES("prés."), IMPF("impf."), FUT("fut."), PARF("parf."), PQPF("p.q.pf."), FUTANT("fut.ant."),
  AOR("aor."), PRETER("prétér.");

  String abbr;
  Temps(String abbr){
    this.abbr = abbr;
  }
  @Override
  public String getAbbr() {
    return abbr;
  }

}
