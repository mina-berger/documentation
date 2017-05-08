/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mina.documentation.site;

import com.mina.documentation.DocDriver;
import static com.mina.documentation.DocUtil.toBetacode;
import com.mina.documentation.DocWriter;
import com.mina.documentation.linguistique.Autre;
import com.mina.documentation.linguistique.Cas;
import com.mina.documentation.linguistique.Gendre;
import com.mina.documentation.linguistique.Lang;
import com.mina.documentation.linguistique.Mode;
import com.mina.documentation.linguistique.MotGeneral;
import com.mina.documentation.linguistique.MotPrincipal;
import com.mina.documentation.linguistique.Nature;
import com.mina.documentation.linguistique.Nombre;
import com.mina.documentation.linguistique.Personne;
import com.mina.documentation.linguistique.Temps;
import com.mina.documentation.linguistique.Voix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author minaberger
 */
public class PerseusDriver extends DocDriver {
  private static List<String> skipCategories = Arrays.asList(
    "nu_movable", "indeclform", "attic", "doric", "aeolic", "ionic", "poetic",
    "epic", "contr", "proclitic", "enclitic", "homeric", "unaugmented", "iota_intens",
    "redupl", "comp_only", "short_subj"
    
  );
  Lang lang;
  String name;
  public PerseusDriver(Lang lang){
    this.lang = lang;
    switch(lang){
      case LA:
        name = "la";
        break;
      case ON:
        name = "non";
        break;
      case GR:
        name = "greek";
        break;
      default:
        throw new IllegalArgumentException("Source unsupported:" + lang.name());
    }
    
  }

  public ArrayList<MotGeneral> getMgList(String word, ArrayList<MotPrincipal> mpList){
    String clef = lang == Lang.GR?toBetacode(word):word;
    open("http://www.perseus.tufts.edu/hopper/morph?l=" + clef + "&la=" + name);
    int analysisCount = count("//div[@class='analysis']");
    ArrayList<MotGeneral> mgList = new ArrayList<>();
    if(analysisCount == 0){
      //System.out.println("empty:" + word);
    }
    for(int i = 1;i <= analysisCount;i++){
      //MotPrincipal mp = new 
      String root = text("//div[@class='analysis'][" + i + "]/div/div/h4");
      String sens = text("//div[@class='analysis'][" + i + "]/div/div/span");
      while(root.matches(".*[0-9]")){
        root = root.substring(0, root.length() - 1);
      }
      if(sens.equals("[definition unavailable]")){
        sens = "";
      }

      //System.out.println(analysis.getRoot() + ":" + analysis.getTraduction());
      int countInflection = count("//div[@class='analysis'][" + i + "]//tr");
      if(countInflection == 0){
        continue;
      }
      HashMap<Nature, MotPrincipal> map = new HashMap<>();
      for(int j = 1;j <= countInflection;j++){
        String mot = text("//div[@class='analysis'][" + i + "]//tr[" + j + "]/td[1]");
        String[] categories = text("//div[@class='analysis'][" + i + "]//tr[" + j + "]/td[2]").split(" ");
        Nature nature = getNature(categories[0]);
        MotPrincipal mp;
        if(!map.containsKey(nature)){
          mp = new MotPrincipal(nature, root, sens);
          mp.setClef(root);
          map.put(nature, mp);
        }else{
          mp = map.get(nature);
        }
        MotGeneral mg = new MotGeneral(mot, mp);
        for(int k = 1;k < categories.length;k++){
          update(mg, categories[k]);
        }
        mgList.add(mg);
      }
      for(Nature nature:map.keySet()){
        mpList.add(map.get(nature));
      }
    }
    return mgList;
  }
  void update(MotGeneral mg, String category){
    //System.out.println(category);
    if (category.equals("1st")) {
      mg.setPersonne(Personne.PREM);
    } else if (category.equals("2nd")) {
      mg.setPersonne(Personne.DEUX);
    } else if (category.equals("3rd")) {
      mg.setPersonne(Personne.TROI);
    } else if (category.equals("sg")) {
      mg.setNombre(Nombre.SG);
    } else if (category.equals("dual")) {
      mg.setNombre(Nombre.DU);
    } else if (category.equals("pl")) {
      mg.setNombre(Nombre.PL);
    } else if (category.equals("pres")) {
      mg.setTemps(Temps.PRES);
    } else if (category.equals("imperf")) {
      mg.setTemps(Temps.IMPF);
    } else if (category.equals("fut")) {
      mg.setTemps(Temps.FUT);
    } else if (category.equals("perf")) {
      mg.setTemps(Temps.PARF);
    } else if (category.equals("plup")) {
      mg.setTemps(Temps.PQPF);
    } else if (category.equals("futperf")) {
      mg.setTemps(Temps.FUTANT);
    } else if (category.equals("aor")) {
      mg.setTemps(Temps.AOR);
    } else if (category.equals("act")) {
      mg.setVoix(Voix.ACT);
    } else if (category.equals("pass")) {
      mg.setVoix(Voix.PASS);
    } else if (category.equals("mid")) {
      mg.setVoix(Voix.MOY);
    } else if (category.equals("mp")) {
      mg.setVoix(Voix.MP);
    } else if (category.equals("ind")) {
      mg.setMode(Mode.IND);
    } else if (category.equals("subj")) {
      mg.setMode(Mode.SUBJ);
    } else if (category.equals("opt")) {
      mg.setMode(Mode.OPT);
    } else if (category.equals("imperat")) {
      mg.setMode(Mode.IMPER);
    } else if (category.equals("inf")) {
      mg.setMode(Mode.INF);
    } else if (category.equals("nom")) {
      mg.setCas(Cas.NOM);
    } else if (category.equals("voc")) {
      mg.setCas(Cas.VOC);
    } else if (category.equals("acc")) {
      mg.setCas(Cas.ACC);
    } else if (category.equals("gen")) {
      mg.setCas(Cas.GEN);
    } else if (category.equals("dat")) {
      mg.setCas(Cas.DAT);
    } else if (category.equals("masc")) {
      mg.setGendre(Gendre.M);
    } else if (category.equals("fem")) {
      mg.setGendre(Gendre.F);
    } else if (category.equals("neut")) {
      mg.setGendre(Gendre.N);
    } else if (category.equals("impersonal")) {
      mg.getPrincipal().addAutre(Autre.IMPERS);
    } else if(skipCategories.contains(category)){
      // faire rien
    } else{
      System.out.println("[p] unknown category:" + category);
    }
  }
  Nature getNature(String str){
    if(str.equals("verb")){
      return Nature.VERBE;
    }else if(str.equals("noun")){
      return Nature.NOUN;
    }else if(str.equals("adj")){
      return Nature.ADJ;
    }else if(str.equals("pron")){
      return Nature.PRON;
    }else if(str.equals("prep")){
      return Nature.PREP;
    }else if(str.equals("adv")){
      return Nature.ADV;
    }else if(str.equals("conj")){
      return Nature.CONJ;
    }else if(str.equals("part")){
      return Nature.PART;
    }else if(str.equals("partic")){
      return Nature.PARTICLE;
    }else if(str.equals("numeral")){
      return Nature.NUM;
    }else if(str.equals("article")){
      return Nature.ART;
    }else if(str.equals("exclam")){
      return Nature.INTERJ;
      //exclam
    }else if(str.equals("irreg")){
      //ad hoc
      //http://www.perseus.tufts.edu/hopper/morph?l=ti/s&la=greek
      return Nature.PRON;
    }else{
      System.out.println("[p]unknown nature:" + str);
    }
    return null;
  }
  
  public static void main(String[] args){
    PerseusDriver nt = new PerseusDriver(Lang.LA);
    DocWriter writer = new DocWriter(System.out);
    //writer.print(nt.find("At Alypius affixus lateri meo inusitati motus mei exitum tacitus opperiebatur."), new ArrayList<>());
  }
  
}
