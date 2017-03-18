/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mina.documentation.apl;

import com.mina.documentation.DocDriver;
import com.mina.documentation.DocWriter;
import com.mina.documentation.Verbum;
import static com.mina.documentation.linguistique.Autre.ARCH;
import static com.mina.documentation.linguistique.Autre.INDECL;
import static com.mina.documentation.linguistique.Autre.POET;
import static com.mina.documentation.linguistique.Autre.RARE;
import static com.mina.documentation.linguistique.Cas.ABL;
import static com.mina.documentation.linguistique.Cas.ACC;
import static com.mina.documentation.linguistique.Cas.DAT;
import static com.mina.documentation.linguistique.Cas.GEN;
import static com.mina.documentation.linguistique.Cas.NOM;
import static com.mina.documentation.linguistique.Cas.VOC;
import com.mina.documentation.linguistique.Categorie;
import com.mina.documentation.linguistique.CategorieGroupe;
import static com.mina.documentation.linguistique.Comparatif.COMPAR;
import static com.mina.documentation.linguistique.Comparatif.SUPERL;
import static com.mina.documentation.linguistique.Gendre.F;
import static com.mina.documentation.linguistique.Gendre.M;
import static com.mina.documentation.linguistique.Gendre.N;
import static com.mina.documentation.linguistique.Mode.GER;
import static com.mina.documentation.linguistique.Mode.IMPER;
import static com.mina.documentation.linguistique.Mode.IND;
import static com.mina.documentation.linguistique.Mode.INF;
import static com.mina.documentation.linguistique.Mode.PART;
import static com.mina.documentation.linguistique.Mode.SUBJ;
import static com.mina.documentation.linguistique.Mode.SUPIN;
import com.mina.documentation.linguistique.MotPrincipal;
import static com.mina.documentation.linguistique.Nature.ADJ;
import static com.mina.documentation.linguistique.Nature.ADV;
import static com.mina.documentation.linguistique.Nature.CONJ;
import static com.mina.documentation.linguistique.Nature.INTERJ;
import static com.mina.documentation.linguistique.Nature.NOUN;
import static com.mina.documentation.linguistique.Nature.PREP;
import static com.mina.documentation.linguistique.Nature.PREVERBE;
import static com.mina.documentation.linguistique.Nature.PRON;
import static com.mina.documentation.linguistique.Nature.VERBE;
import static com.mina.documentation.linguistique.Nombre.PL;
import static com.mina.documentation.linguistique.Nombre.SG;
import static com.mina.documentation.linguistique.Personne.DEUX;
import static com.mina.documentation.linguistique.Personne.PREM;
import static com.mina.documentation.linguistique.Personne.TROI;
import static com.mina.documentation.linguistique.Temps.FUT;
import static com.mina.documentation.linguistique.Temps.FUTANT;
import static com.mina.documentation.linguistique.Temps.IMPF;
import static com.mina.documentation.linguistique.Temps.PARF;
import static com.mina.documentation.linguistique.Temps.PRES;
import static com.mina.documentation.linguistique.Voix.ACT;
import static com.mina.documentation.linguistique.Voix.PASS;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static com.mina.documentation.linguistique.Temps.PQPF;

/**
 *
 * @author minaberger
 */
public class LatinTranslator extends DocDriver {
  DocWriter writer;
  static List<String> skips = Arrays.asList("non", "in", "et", "nec", "de");
  
  static class ReservesPrincipal extends HashMap<String, MotPrincipal> {
    public void add(MotPrincipal mot){
      put(mot.getMot(), mot);
    }
  }

  static ReservesPrincipal reserves = new ReservesPrincipal();
  static {
    reserves.add(new MotPrincipal(ADJ, "jam", "désormais, déjà"));
    reserves.add(new MotPrincipal(CONJ, "ergo", "de coord., donc"));
    reserves.add(new MotPrincipal(CONJ, "nam", "causale, car"));
    reserves.add(new MotPrincipal(ADV, "enim", "assurément"));
    reserves.add(new MotPrincipal(ADV, "etiam", "encore, ainsi, également"));
    reserves.add(new MotPrincipal(ADV, "ita", "de même, encore"));
    reserves.add(new MotPrincipal(ADV, "tamen", "pourtant, cependant"));
    reserves.add(new MotPrincipal(ADV, "sic", "ainsi, de cette manière"));
    reserves.add(new MotPrincipal(PREP, "ad", "+acc. direction out proximité, vers, pour"));
    reserves.add(new MotPrincipal(CONJ, "si", "de coord.,"));
    reserves.add(new MotPrincipal(CONJ, "cum", "de sub., lorsque +ind, (cause) parce que +subj"));
    reserves.add(new MotPrincipal(ADV, "nonne", "interr, est-ce que... ne pas, (indir.)si... ne... pas"));
    reserves.add(new MotPrincipal(CONJ, "donec", "de sub., aussi lontemps que +ind., jusqu'à ce que +subj."));
    reserves.add(new MotPrincipal(ADV, "inde", "de là, de ce lieu, à partir de ce moment, dès lors, après cela"));
    reserves.add(new MotPrincipal(ADV, "simul", "ensemble, en même temps"));
    reserves.add(new MotPrincipal(CONJ, "dum", "pendent que, aussi longtemps que"));
    reserves.add(new MotPrincipal(CONJ, "quia", "parce que"));
    reserves.add(new MotPrincipal(ADV, "adhuc", "jusqu'à ce point-ci, de plus"));
    reserves.add(new MotPrincipal(ADV, "quod", "rél. quant ce fait que"));
    reserves.add(new MotPrincipal(ADV, "ibi", "ici, en ce lieu"));
    
  }

  public LatinTranslator(PrintStream out){
    writer = new DocWriter(out);
  }
  public void translate(String text){
    String[] words = text.split(" ");
    ArrayList<Verbum> verbi = new ArrayList<>();
    for(String word:words){
      if(word.equals("/")){
        continue;
      }
      if(word.startsWith("\"")){
        word = word.substring(1);
      }
      if(word.endsWith("\"")){
        word = word.substring(0, word.length() - 1);
      }
      word = word.trim();
      if(word.endsWith(".") || word.endsWith(",") || word.endsWith("?") || 
         word.endsWith("!") || word.endsWith(";") || word.endsWith(":") || word.endsWith("-")){
        word = word.substring(0, word.length() - 1);
      }
      if(word.isEmpty()){
        continue;
      }
      word = word.toLowerCase();
      if(skips.contains(word)){
        continue;
      }
      if(reserves.containsKey(word)){
        verbi.add(new Verbum(word, reserves.get(word).getSens()));
      }else{
        verbi.add(findWord(word));
      }
    }
    writer.println("<ul>");
    for(Verbum verbum:verbi){
      //updateVerbum(verbum);
      writer.print(verbum, skipCategories);
    }
    writer.println("</ul>");
  }
    /*static List<String> skipsInflection = Arrays.asList(
    "verb", "3rd", "2nd", "1st", "noun", "ind", "act", "indeclform");*/
 static List<Categorie> skipCategories = Arrays.asList(
    VERBE, NOUN, IND, ACT, INDECL);
  static HashMap<String, Categorie> reserveInflection = new HashMap<>();
  static {
    reserveInflection.put("prep", PREP);
    reserveInflection.put("verb", VERBE);
    reserveInflection.put("noun", NOUN);
    reserveInflection.put("pron", PRON);
    reserveInflection.put("exclam", INTERJ);
    reserveInflection.put("ind", IND);
    reserveInflection.put("subj", SUBJ);
    reserveInflection.put("inf", INF);
    reserveInflection.put("act", ACT);
    reserveInflection.put("pass", PASS);
    reserveInflection.put("perf", PARF);
    reserveInflection.put("plup", PQPF);
    reserveInflection.put("fut", FUT);
    reserveInflection.put("futperf", FUTANT);
    reserveInflection.put("pres", PRES);
    reserveInflection.put("imperat", IMPER);
    reserveInflection.put("gerundive", GER);
    reserveInflection.put("supine", SUPIN);
    reserveInflection.put("adj", ADJ);
    reserveInflection.put("imperf", IMPF);
    reserveInflection.put("masc", M);
    reserveInflection.put("fem", F);
    reserveInflection.put("neut", N);
    reserveInflection.put("1st", PREM);
    reserveInflection.put("2nd", DEUX);
    reserveInflection.put("3rd", TROI);
    reserveInflection.put("sg", SG);
    reserveInflection.put("pl", PL);
    reserveInflection.put("adv", ADV);
    reserveInflection.put("nom", NOM);
    reserveInflection.put("voc", VOC);
    reserveInflection.put("acc", ACC);
    reserveInflection.put("gen", GEN);
    reserveInflection.put("dat", DAT);
    reserveInflection.put("abl", ABL);
    reserveInflection.put("prep", PREP);
    reserveInflection.put("conj", CONJ);
    reserveInflection.put("poetic", POET);
    reserveInflection.put("indeclform", INDECL);
    reserveInflection.put("early", ARCH);
    reserveInflection.put("rare", RARE);
    reserveInflection.put("raw_preverb", PREVERBE);
    reserveInflection.put("irreg_comp", COMPAR);
    reserveInflection.put("comp_only", COMPAR);
    reserveInflection.put("comp", COMPAR);
    reserveInflection.put("superl", SUPERL);
    reserveInflection.put("part", PART);
  }

  public Verbum findWord(String word){
    open("http://www.perseus.tufts.edu/hopper/morph?l=" + word + "&la=la");
    int analysisCount = count("//div[@class='analysis']");
    Verbum verbum = new Verbum(word);
    if(analysisCount == 0){
      //System.out.println("empty:" + word);
    }
    for(int i = 1;i <= analysisCount;i++){
      String root = text("//div[@class='analysis'][" + i + "]/div/div/h4");
      while(root.matches(".*[0-9]")){
        root = root.substring(0, root.length() - 1);
      }

      Verbum.Analysis analysis = new Verbum.Analysis(
        root,
        text("//div[@class='analysis'][" + i + "]/div/div/span")
      );
      //System.out.println(analysis.getRoot() + ":" + analysis.getTraduction());
      int countInflection = count("//div[@class='analysis'][" + i + "]//tr");
      if(countInflection == 0){
        //System.out.println("empty:" + word + ":" + analysis.getRoot() + ":" + analysis.getTraduction());
      }
      ArrayList<CategorieGroupe> groupe = new ArrayList<>();
      for(int j = 1;j <= countInflection;j++){
        String aText = text("//div[@class='analysis'][" + i + "]//tr[" + j + "]/td[2]");
        //System.out.println("analysis=" + aText);
        analysis.add(aText);
        groupe.add(parseCategories(aText));
      }
      analysis.setCategories(groupe);
      verbum.add(analysis);
    }
    return verbum;
  }
  CategorieGroupe parseCategories(String text){
    CategorieGroupe categories = new CategorieGroupe();
    String[] splits = text.split(" ");
    for(String split:splits){
      if(reserveInflection.containsKey(split)){
        categories.add(reserveInflection.get(split));
        //System.err.println("categorie oui:" + split);
      }else{
        System.err.println("categorie non:" + split);
      }
    }
    return categories;
  }
  
  public static void main(String[] args){
    LatinTranslator nt = new LatinTranslator(System.out);
    nt.translate("tria");
    nt.quit(10);
    
    //new LatinTranslator(System.out).parseCategories("verb 3rd sg pres subj act");
  }
  
}
