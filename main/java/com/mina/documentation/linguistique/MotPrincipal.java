package com.mina.documentation.linguistique;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author minaberger
 */
public class MotPrincipal {

  Nature nature;
  Gendre gendre;
  Nombre nombre;
  Conjugaison conjugaison;
  String mot;
  String clef;
  String sens;
  boolean findRoot;

  ArrayList<String> variations;
  ArrayList<Autre> autres;

  public MotPrincipal(Nature nature) {
    this(nature, null, null, null);
  }

  public MotPrincipal(Nature nature, String mot, String sens, String... variations) {
    this(nature, null, mot, sens, variations);
  }
  public MotPrincipal(Gendre gendre, String mot, String sens, String... variations) {
    this(Nature.NOUN, gendre, mot, sens, variations);
  }
  public MotPrincipal(Nature nature, Gendre gendre, String mot, String sens, String... variations) {
    this.nature = nature;
    this.gendre = gendre;
    conjugaison = null;
    findRoot = false;
    this.variations = variations == null?new ArrayList<>():new ArrayList<>(Arrays.asList(variations));
    autres = new ArrayList<>();
    this.mot = mot;
    this.sens = sens;
  }

  public void addAutre(Autre autre) {
    if(!autres.contains(autre)){
      autres.add(autre);
    }
  }
  public String getAutres() {
    String[] join = new String[autres.size()];
    int i = 0;
    for(Autre autre:autres){
      join[i++] = autre.getAbbr();
    }
    return String.join(", ", join);
  }
  
  public void addVariation(String variation) {
    if(variations.contains(variation)){
      return;
    }
    variations.add(variation);
  }

  public ArrayList<String> getVariations() {
    return variations;
  }

  public Conjugaison getConjugaison() {
    return conjugaison;
  }

  public void setConjugaison(Conjugaison conjugaison) {
    this.conjugaison = conjugaison;
  }

  public Nature getNature() {
    return nature;
  }

  public void setNature(Nature nature) {
    this.nature = nature;
  }

  public void setGendre(Gendre gendre) {
    this.gendre = gendre;
  }

  public void setMot(String mot) {
    this.mot = mot;
  }

  public void setSens(String sens) {
    this.sens = sens;
  }

  public String getMot() {
    return mot;
  }

  public String getSens() {
    return sens;
  }

  public String getClef() {
    return clef;
  }

  public void setClef(String clef) {
    this.clef = clef;
  }
  public boolean findRoot() {
    return findRoot;
  }

  public void setFindRoot(boolean findRoot) {
    this.findRoot = findRoot;
  }

}
