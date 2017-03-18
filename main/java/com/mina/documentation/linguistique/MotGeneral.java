package com.mina.documentation.linguistique;

import static com.mina.documentation.linguistique.Nature.ADJ;
import static com.mina.documentation.linguistique.Nature.ADV;
import static com.mina.documentation.linguistique.Nature.CONJ;
import static com.mina.documentation.linguistique.Nature.INTERJ;
import static com.mina.documentation.linguistique.Nature.NOUN;
import static com.mina.documentation.linguistique.Nature.PREP;
import static com.mina.documentation.linguistique.Nature.PREVERBE;
import static com.mina.documentation.linguistique.Nature.PRON;
import static com.mina.documentation.linguistique.Nature.VERBE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author minaberger
 */
public class MotGeneral {
  Cas cas;
  Gendre gendre;
  Personne personne;
  Nombre nombre;
  Comparatif comparatif;
  Mode mode;
  Temps temps;
  Voix voix;
  ArrayList<Autre> autres;
  
  String mot;
  MotPrincipal principal;
  boolean isRoot;
  public MotGeneral(MotPrincipal mp){
    this(mp.getMot(), mp, true);
  }
  public MotGeneral(String mot, MotPrincipal principal, Categorie... categories){
    this(mot, principal, false, categories);
  }
  public MotGeneral(String mot, MotPrincipal principal, boolean isRoot, Categorie... categories){
    this.mot = mot;
    this.principal = principal;
    this.isRoot = isRoot;
    cas = null;
    gendre = null;
    personne = null;
    nombre = null;
    comparatif = null;
    mode = null;
    temps = null;
    voix = null;
    autres = new ArrayList<>();
    for(Categorie categorie:categories){
      if(categorie instanceof Cas){
        cas = (Cas)categorie;
      }else if(categorie instanceof Gendre){
        gendre = (Gendre)categorie;
      }else if(categorie instanceof Personne){
        personne = (Personne)categorie;
      }else if(categorie instanceof Nombre){
        nombre = (Nombre)categorie;
      }else if(categorie instanceof Comparatif){
        comparatif = (Comparatif)categorie;
      }else if(categorie instanceof Mode){
        mode = (Mode)categorie;
      }else if(categorie instanceof Temps){
        temps = (Temps)categorie;
      }else if(categorie instanceof Voix){
        voix = (Voix)categorie;
      }else if(categorie instanceof Autre){
        autres.add((Autre)categorie);
      }else{
        System.out.println(categorie.getClass().getName() + ":" + categorie.toString());
      }
    }
    
  }

  public boolean isRoot() {
    return isRoot;
  }
  
  

  public Cas getCas() {
    return cas;
  }

  public void setCas(Cas cas) {
    this.cas = cas;
  }

  public Gendre getGendre() {
    return gendre;
  }

  public void setGendre(Gendre gendre) {
    this.gendre = gendre;
  }

  public Personne getPersonne() {
    return personne;
  }

  public void setPersonne(Personne personne) {
    this.personne = personne;
  }

  public Nombre getNombre() {
    return nombre;
  }

  public void setNombre(Nombre nombre) {
    this.nombre = nombre;
  }

  public Comparatif getComparatif() {
    return comparatif;
  }

  public void setComparatif(Comparatif comparatif) {
    this.comparatif = comparatif;
  }

  public Mode getMode() {
    return mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

  public Temps getTemps() {
    return temps;
  }

  public void setTemps(Temps temps) {
    this.temps = temps;
  }

  public Voix getVoix() {
    return voix;
  }

  public void setVoix(Voix voix) {
    this.voix = voix;
  }

  public String getMot() {
    return mot;
  }

  public void setMot(String mot) {
    this.mot = mot;
  }
  public void addAutre(Autre autre) {
    autres.add(autre);
  }
  public Autre getLastAutre() {
    if(autres.isEmpty()){
      return null;
    }
    return autres.get(autres.size() - 1);
  }

  public MotPrincipal getPrincipal() {
    return principal;
  }

  public void setPrincipal(MotPrincipal principal) {
    this.principal = principal;
  }
  public String getRoot(){
    return principal.getMot();
  }
  public String getInflectionPost(List<Categorie> skip){
    Nature nature = principal.getNature();
    if(nature == null){
      return "";
    }
    switch(nature){
      case NOUN:
        return getInflection(skip, false, gendre == null?principal.gendre:gendre);
      default:
        return "";
    }
  }
  public String getInflectionAnte(List<Categorie> skip){
    Nature nature = principal.getNature();
    if(nature == null){
      return getInflection(skip, true, cas, gendre, personne, nombre, comparatif, temps, mode, voix);
    }
    switch(nature){
      case ADJ:
        return getInflection(skip, true, nature, cas, gendre, personne, nombre, comparatif);
      case ADV:
        return getInflection(skip, true, nature, comparatif);
      case CONJ:
      case INTERJ:
      case PREP:
        return getInflection(skip, true, nature);
      case NOUN:
        return getInflection(skip, true, nature, cas, personne, nombre, comparatif);
      case PREVERBE:
      case VERBE:
        return getInflection(skip, true, nature, cas, gendre, personne, nombre, comparatif, temps, mode, voix);
      case PRON:
        return getInflection(skip, true, nature, cas, gendre, personne, nombre, comparatif);
      default:
        return getInflection(skip, true, nature, cas, gendre, personne, nombre, comparatif, temps, mode, voix);
    }
  }  
  private String getInflection(List<Categorie> skip, boolean add, Categorie... categories){
    String str = "";
    ArrayList<Categorie> list = new ArrayList<>(Arrays.asList(categories));
    if(add){
      list.addAll(autres);
    }
    for(Categorie categorie:list){
      if(categorie == null || skip.contains(categorie)){
        continue;
      }
      str += categorie.getAbbr() + " ";
    }
    return str;
  }
  
}
