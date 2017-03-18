package com.mina.documentation.linguistique;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author minaberger
 */
public class CategorieGroupe extends ArrayList<Categorie>{
  Nature nature;
  Cas cas;
  Gendre gendre;
  Personne personne;
  Nombre nombre;
  Comparatif comparatif;
  Mode mode;
  Temps temps;
  Voix voix;
  public CategorieGroupe(){
    nature = null;
    cas = null;
    gendre = null;
    personne = null;
    nombre = null;
    comparatif = null;
    mode = null;
    temps = null;
    voix = null;
  }

  public Nature getNature() {
    return nature;
  }

  public Cas getCas() {
    return cas;
  }

  public Gendre getGendre() {
    return gendre;
  }

  public Personne getPersonne() {
    return personne;
  }

  public Nombre getNombre() {
    return nombre;
  }

  public Comparatif getComparatif() {
    return comparatif;
  }

  public Mode getMode() {
    return mode;
  }

  public Temps getTemps() {
    return temps;
  }

  public Voix getVoix() {
    return voix;
  }
  
  public void update(){
    ArrayList<Categorie> list = new ArrayList<>(Arrays.asList(toArray(new Categorie[]{})));
    clear();
    for(int i = list.size() - 1;i >= 0;i--){
      if(list.get(i) instanceof Nature){
        if(nature == null){
          nature = (Nature)list.remove(i);
        }else{
          list.remove(i);
        }
      }
    }
    for(int i = list.size() - 1;i >= 0;i--){
      if(list.get(i) instanceof Cas){
        if(cas == null){
          cas = (Cas)list.remove(i);
        }else{
          list.remove(i);
        }
      }
    }
    for(int i = list.size() - 1;i >= 0;i--){
      if(list.get(i) instanceof Gendre){
        if(gendre == null){
          gendre = (Gendre)list.remove(i);
        }else{
          list.remove(i);
        }
      }
    }
    for(int i = list.size() - 1;i >= 0;i--){
      if(list.get(i) instanceof Personne){
        if(personne == null){
          personne = (Personne)list.remove(i);
        }else{
          list.remove(i);
        }
      }
    }
    for(int i = list.size() - 1;i >= 0;i--){
      if(list.get(i) instanceof Nombre){
        if(nombre == null){
          nombre = (Nombre)list.remove(i);
        }else{
          list.remove(i);
        }
      }
    }
    for(int i = list.size() - 1;i >= 0;i--){
      if(list.get(i) instanceof Comparatif){
        if(comparatif == null){
          comparatif = (Comparatif)list.remove(i);
        }else{
          list.remove(i);
        }
      }
    }
    for(int i = list.size() - 1;i >= 0;i--){
      if(list.get(i) instanceof Mode){
        if(mode == null){
          mode = (Mode)list.remove(i);
        }else{
          list.remove(i);
        }
      }
    }
    for(int i = list.size() - 1;i >= 0;i--){
      if(list.get(i) instanceof Temps){
        if(temps == null){
          temps = (Temps)list.remove(i);
        }else{
          list.remove(i);
        }
      }
    }
    for(int i = list.size() - 1;i >= 0;i--){
      if(list.get(i) instanceof Voix){
        if(voix == null){
          voix = (Voix)list.remove(i);
        }else{
          list.remove(i);
        }
      }
    }
    for(int i = list.size() - 1;i >= 0;i--){
      if(list.get(i) instanceof Autre){
        add(list.remove(i));
      }
    }
    for(int i = list.size() - 1;i >= 0;i--){
      System.err.println("uncategorié:" + list.get(i).toString());
    }
    
  }
  public String getInflectionPost(List<Categorie> skip){
    if(nature == null){
      return "";
    }
    switch(nature){
      case NOUN:
        return getInflection(skip, false, gendre);
      default:
        return "";
    }
  }
  public String getInflectionAnte(List<Categorie> skip){
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
      list.addAll(this);
    }
    for(Categorie categorie:categories){
      if(categorie == null || skip.contains(categorie)){
        continue;
      }
      str += categorie.getAbbr() + " ";
    }
    return str;
  }
  public static void main(String[] args){
    System.out.println("dico2".matches(".*[0-9]"));
  }
  
}
