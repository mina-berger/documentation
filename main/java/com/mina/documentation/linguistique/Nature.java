package com.mina.documentation.linguistique;

/**
 *
 * @author minaberger
 */
public enum Nature implements Categorie {
  NOUN("noun."),
  VERBE("verb."),
  CONJ("conj."),
  ADJ("adj."),
  PART("part."),
  ADV("adv."),
  PREP("prép."),
  PRON("pron."),
  INTERJ("interj."),
  PREVERBE("preverbe."),
  PARTICLE("particle."),
  NUM("num."),
  ART("art."),
  CONTR("contr"),
  
  ;
  
  String abbr;
  Nature(String abbr){
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
