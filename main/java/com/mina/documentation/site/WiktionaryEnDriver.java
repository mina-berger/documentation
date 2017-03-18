package com.mina.documentation.site;

import com.mina.documentation.DocDriver;
import static com.mina.documentation.DocUtil.wash;
import com.mina.documentation.linguistique.Autre;
import static com.mina.documentation.linguistique.Autre.INDECL;
import com.mina.documentation.linguistique.Cas;
import com.mina.documentation.linguistique.Categorie;
import com.mina.documentation.linguistique.Conjugaison;
import com.mina.documentation.linguistique.Gendre;
import com.mina.documentation.linguistique.Lang;
import com.mina.documentation.linguistique.Mode;
import static com.mina.documentation.linguistique.Mode.IND;
import com.mina.documentation.linguistique.MotGeneral;
import com.mina.documentation.linguistique.MotPrincipal;
import com.mina.documentation.linguistique.Nature;
import static com.mina.documentation.linguistique.Nature.NOUN;
import static com.mina.documentation.linguistique.Nature.VERBE;
import com.mina.documentation.linguistique.Nombre;
import com.mina.documentation.linguistique.Personne;
import com.mina.documentation.linguistique.Temps;
import com.mina.documentation.linguistique.Voix;
import static com.mina.documentation.linguistique.Voix.ACT;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.WebElement;

/**
 *
 * @author minaberger
 */
public class WiktionaryEnDriver extends DocDriver {

  static List<String> skipH4 = Arrays.asList(
    "References", "See also", "Pronunciation", "Pronunciation 1", "Pronunciation 2",
    "Etymology 1", "Etymology 2", "Etymology 3", "Etymology 4", "Etymology",
    "Inflection", "Synonyms", "Descendants", "Related terms", "Alternative forms",
    "Usage notes", "Derived terms", "Quotations", "Letter", "Declension",
    "Antonyms", "Conjugation", "Anagrams", "External links", "Determiner");
  static List<String> skipCategory = Arrays.asList(
    "Alternative", "spelling", "of", "form", "inflection", "and", "the", "(",
    "name", "letter", "equivalent", "(tú)");
  static List<String> skipRootType = Arrays.asList(
    "(", ")", "infl-inline", "ib-brac qualifier-brac", "ib-brac", "tr", "mention-gloss-paren");
  static HashMap<String, Nature> mapNature = new HashMap<>();

  static {
    mapNature.put("Noun", Nature.NOUN);
    mapNature.put("Verb", Nature.VERBE);
    mapNature.put("Participle", Nature.PART);
    mapNature.put("Particle", Nature.PARTICLE);
    mapNature.put("Article", Nature.PARTICLE);
    mapNature.put("Adverb", Nature.ADV);
    mapNature.put("Adjective", Nature.ADJ);
    mapNature.put("Article", Nature.ART);
    mapNature.put("Preposition", Nature.PREP);
    mapNature.put("Pronoun", Nature.PRON);
    mapNature.put("Conjunction", Nature.CONJ);
    mapNature.put("Interjection", Nature.INTERJ);
    mapNature.put("Numeral", Nature.NUM);
    mapNature.put("Contraction", Nature.CONTR);
    

  }

  Lang langS;
  Lang langT;
  String mark;
  String att;
  //DocWriter writer;
  PrintStream out;
  //WiktionaryFrDriver wfrDriver;

  public WiktionaryEnDriver(Lang langS)  {
    //writer = new DocWriter(out);
    this.out = System.out;//new PrintStream("doc/log");
    this.langS = langS;
    this.langT = Lang.FR;
    switch (this.langS) {
      case LA:
        mark = "Latin";
        att = "la";
        break;
      case GR:
        mark = "Ancient Greek";
        att = "grc";
        break;
      case ES:
        mark = "Spanish";
        att = "es";
        break;
      case ON:
        mark = "Old Norse";
        att = "non";
        break;
      default:
        throw new IllegalArgumentException("Source unsupported:" + this.langS.name());
    }
    //wfrDriver = new WiktionaryFrDriver();
  }

  @Override
  public void quit() {
    //writer.quit();
    out.flush();
    //out.close();
    super.quit();
  }

  public boolean updateMp(MotPrincipal mp){
    open("https://en.m.wiktionary.org/wiki/" + mp.getClef());
    String contentPath = contentPath();
    if (contentPath == null) {
      return false;
    }
    return updateSens(contentPath, mp);
  }

  public boolean updateSens(String contentPath, MotPrincipal mp) {
    String xpath = contentPath + "/*";
    List<WebElement> elements = elements(xpath);
    for (int i = 0; i < elements.size(); i++) {
      WebElement element = elements.get(i);
      String tagName = element.getTagName();
      if (!tagName.equals("h4") && !tagName.equals("h3")) {
        continue;
      }
      String h4 = element.getText();
      if (!mapNature.containsKey(h4)) {
        continue;
      }
      if (mapNature.get(h4) != mp.getNature()) {
        continue;
      }
      if (i == elements.size() - 1) {
        return false;
      }
      element = elements.get(i + 1);
      if (!element.getTagName().equals("p")) {
        continue;
      }
      //title
      String motPath = subPath(xpath, i + 1) + "/*[name()='strong' or name()='span' or name()='b']";
      WebElement mot = element(motPath);
      if(mot == null){
        System.out.println("[e] cannot get mot : " + motPath);
      }
      if (!mot.getText().equals(mp.getMot())) {
        continue;
      }
      //System.out.println("mot:" + mp.getMot() + "/root:" + text(subPath(xpath, i + 1)));
      //mp.setMot(mot.getText());
      updateRoot(subPath(xpath, i + 1), mp);
      if (i == elements.size() - 2) {
        return false;
      }
      String olPath = subPath(xpath, i + 2);
      WebElement ol = element(olPath);
      if (ol == null) {
        System.err.println("no ol tag");
        continue;
      }
      if (!ol.getTagName().equals("ol")) {
        System.err.println("unexpected tag:" + ol.getTagName());
        continue;
      }
      mp.setSens(getSens(olPath));
      return true;

    }

    return false;
  }

  String contentPath() {
    String xpath = "//div[@id='mw-content-text']/*";
    List<WebElement> elements = elements(xpath);
    //System.out.println("count=" + elements.size());
    for (int i = 0; i < elements.size(); i++) {
      WebElement element = elements.get(i);
      if (!element.getTagName().equals("h2")) {
        continue;
      }
      WebElement headline = element(subPath(xpath, i) + "/span[@class='mw-headline']");
      if (headline == null) {
        continue;
      }
      String title = headline.getText();
      if (!title.equals(mark)) {
        continue;
      }
      String contentPath = subPath(xpath, i + 1);
      WebElement content = element(contentPath);
      if (content == null) {
        System.err.println("cannot find");
        continue;
      }
      return contentPath;
    }
    return null;
  }

  public ArrayList<MotGeneral> getMgList(String clef, MotPrincipal chercher, ArrayList<MotPrincipal> mpList){
    open("https://en.m.wiktionary.org/wiki/" + clef);
    String contentPath = contentPath();
    if (contentPath == null) {
      return new ArrayList<>();
    }
    ArrayList<MotGeneral> list = new ArrayList<>();
    String xpath = contentPath + "/*";
    List<WebElement> elements = elements(xpath);
    MotPrincipal mp = null;
    for (int i = 0; i < elements.size(); i++) {
      WebElement element = elements.get(i);
      String tagName = element.getTagName();
      if (tagName.equals("h4") || tagName.equals("h3")) {
        String h4 = element.getText();
        if (mapNature.containsKey(h4)) {
          Nature nature = mapNature.get(h4);
          if(chercher == null || nature.equals(chercher.getNature())){
           mp = new MotPrincipal(mapNature.get(h4));
          }
          continue;
        }
        if (!skipH4.contains(h4)) {
          out.println("[e] unknown title='" + h4 + "'");
        }
        continue;
      } else if (mp == null) {
        continue;
      }
      if (tagName.equals("p") && mp != null) {
        //boolean isRoot = count(subPath(xpath, i) + "/*") > 1;
        String olPath = subPath(xpath, i + 1);
        boolean isRoot = 
          count(olPath + "/li[1]/span/span[@class='form-of-definition-link']/i/a") == 0 &&
          count(olPath + "/li[1]/span[@class='use-with-mention']/i/a") == 0;
        WebElement ol = element(olPath);
        if (ol == null) {
          System.err.println("no ol tag");
          continue;
        }
        if (!ol.getTagName().equals("ol")) {
          System.err.println("unexpected tag:" + ol.getTagName());
          continue;
        }
        WebElement mot = element(subPath(xpath, i) + "/strong[@lang='" + att + "']");
        if (mot == null) {
          mot = element(subPath(xpath, i) + "/b");
        }
        if(chercher != null && !mot.getText().equals(chercher.getMot())){
          continue;
        }
        //Nature.PART => add Nature.Verb && Mode.PART
        MotGeneral mgPart = null;
        if (mp.getNature() == Nature.PART) {
          int index = i;
          while (index >= 0) {
            WebElement ety = elements.get(index);
            if (!ety.getTagName().equals("h3")) {
              index--;
              continue;
            }
            ety = element(subPath(xpath, index) + "/span[@id='Etymology']");
            if (ety == null) {
              index--;
              continue;
            }
            ety = element(subPath(xpath, index + 1) + "/i/a");
            if (ety == null) {
              break;
            }
            String _mot = ety.getText();
            String _clef = ety.getAttribute("title");
            if (_mot == null || _clef == null) {
              out.println("PART=>VERB failing:mot=" + _mot + "/clef=" + _clef);
              break;
            }
            String categories = elements.get(index + 1).getText().toLowerCase();
            int ofindex = categories.indexOf("of");
            if(ofindex >= 0){
              categories = categories.substring(0, ofindex);
            }
            //mp.setRoot(_mot);
            MotPrincipal _mp = new MotPrincipal(Nature.VERBE);
            //partCategories = categories.split(" ");
            _mp.setMot(_mot);
            _mp.setClef(_clef);
            mgPart = new MotGeneral(_mot, _mp);
            //for(String category:categories.split(" ")){
            //  updateCategory(mgPart, category);
            //}
            break;

          }
        }
        if(chercher == null){
          if (isRoot) {
            mp.setMot(mot.getText());
            mp.setClef(clef);

            list.add(new MotGeneral(mot.getText(), mp, true));
            updateRoot(subPath(xpath, i), mp);
            //if (langT == Lang.EN) {
              mp.setSens(getSens(olPath));
            //} else {
              mpList.add(mp);
            //}
          } else {
            if (mot == null) {
              System.err.println("no mot general!!");
            } else {
              ArrayList<MotGeneral> mgList = getMotGeneralList(olPath, mot.getText(), mp);
              list.addAll(mgList);
            }
            mpList.add(mp);
          }
        }
        if(mgPart != null){
          list.add(mgPart);
          mpList.add(mgPart.getPrincipal());
        }
        mp = null;
      }

    }

    return list;
  }

  public void updateRoot(String xpath, MotPrincipal mp) {
    List<WebElement> elements = elements(xpath + "/*");
    //System.out.println("DEBUG root size=" + elements.size() + "/xpath=" + xpath);
    for (int i = 0; i < elements.size(); i++) {
      WebElement element = elements.get(i);
      String tagName = element.getTagName();
      if (tagName.equals("i")) {
        String link = subPath(xpath, i) + "/a";
        if (count(link) > 0) {
          updateInfo(element(subPath(xpath, i) + "/a").getText(), mp);
        } else if (i < elements.size() - 1) {
          //System.out.println("addVaridation 1");
          WebElement varidation = elements.get(i + 1);
          if(!varidation.getTagName().equals("i")){
            mp.addVariation(varidation.getText().trim());
            i++;
          }
        }
      } else if (tagName.equals("span")) {
        String type = element.getAttribute("class");
        String value = element.getText().trim();

        if (type.equals("gender")) {
          if (value.equals("?")) {
            mp.setGendre(Gendre.UNKNOWN);
          } else if (value.equals("m, f, n")) {
            mp.setGendre(Gendre.MFN);
          } else if (value.equals("m, f")) {
            mp.setGendre(Gendre.MF);
          } else if (value.equals("m")) {
            mp.setGendre(Gendre.M);
          } else if (value.equals("f")) {
            mp.setGendre(Gendre.F);
          } else if (value.equals("n")) {
            mp.setGendre(Gendre.N);
          } else if (value.equals("m pl") || value.equals("m sg")) {
            mp.setGendre(Gendre.M);
          } else if (value.equals("f pl") || value.equals("f sg")) {
            mp.setGendre(Gendre.F);
          } else if (value.equals("n pl") || value.equals("n sg")) {
            mp.setGendre(Gendre.N);
          } else {
            out.println("[e] unknown gender:" + value);
          }
        } else if (type.equals("polytonic")) { //gr
          mp.addVariation(value);
        } else if (type.startsWith("form-of lang-es ")) { //es
          //System.out.println("addVaridation 2");
          mp.addVariation(value);
        } else if (type.equals("ib-content")) {
          if (value.equals("relative adverb")) {
            mp.addAutre(Autre.REL);
          } else if (value.equals("demonstrative")) {
            mp.addAutre(Autre.DEMONST);
          } else {
            out.println("[e] unknown ib-content:" + value);
          }
        } else if (type.equals("ib-content qualifier-content")) {
          if (value.equals("postpositive")) {
            mp.addAutre(Autre.POSTP);
          } else {
            out.println("[e] unknown ib-content qualifier-content:" + value);
          }
          
        } else if (skipRootType.contains(type)) {

        } else {
          out.println("[e] unknown type:" + type + "/value:" + value);
        }

      }
    }

  }

  public void updateInfo(String info, MotPrincipal mp) {
    if (info.equals("first conjugation")) {
      mp.setConjugaison(Conjugaison.CONJ1);
    } else {
      System.out.println("[e] unknown info:" + info);
    }
  }

  public String getSens(String olPath) {
    String liPath = olPath + "/li";
    int count = count(liPath);
    ArrayList<String> list = new ArrayList<>();
    List<WebElement> elements = elements(liPath);
    for (int i = 0; i < elements.size(); i++) {
      String text = elements.get(i).getText();
      for (WebElement element : elements(subPath(liPath, i) + "/ul")) {
        text = text.replace(element.getText(), "");
      }
      for (WebElement element : elements(subPath(liPath, i) + "/dl")) {
        text = text.replace(element.getText(), "");
      }
      list.add(text.trim());
    }
    return String.join(" / ", list);
  }

  public ArrayList<MotGeneral> getMotGeneralList(String olPath, String mot, MotPrincipal mp) {
    String liPath = olPath + "/li";
    int count = count(liPath);
    ArrayList<MotGeneral> list = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      ArrayList<MotGeneral> mgList = getMotGeneral(subPath(liPath, i), mot, mp);
      list.addAll(mgList);
    }
    return list;
  }

  public ArrayList<MotGeneral> getMotGeneral(String liPath, String mot, MotPrincipal mp) {
    ArrayList<MotGeneral> list = new ArrayList<>();
    MotGeneral mg = new MotGeneral(mot, mp);
    String str = text(liPath + "/span[@class='form-of-definition']");
    String tail;
    if(str == null){
      str = text(liPath + "/span[@class='use-with-mention']");
      //int countIA = count(liPath + "/span[@class='use-with-mention']/i/a");
      //System.out.println("count=" + countIA);
      tail = lastText(liPath + "/span[@class='use-with-mention']/i/a");
    }else{
      tail = text(liPath + "/span[@class='form-of-definition']/span[@class='form-of-definition-link']");
    }
    if (str == null || tail == null) {
      out.println("cannot get mot general:" + mot + "/" + str + "/" + tail);
      return list;
    }
    //System.out.println("str=" + str + "/tail=" + tail);
    str = str.substring(0, str.lastIndexOf(tail)).trim();
    if (str.endsWith("of")) {
      str = str.substring(0, str.length() - 2).trim();
    }
    str = str.replaceAll("\\(.*\\)", "");
    
    boolean foundPart = false;
    for (String category : str.split(" ")) {
      if(updateCategory(mg, category).equals(Mode.PART)){
        foundPart = true;
      }
    }
    //System.out.println("lipath=" + text(liPath));
    WebElement rootElement = lastElement(liPath + "//i/a");
    mp.setMot(rootElement.getText());
    mp.setClef(rootElement.getAttribute("title"));
    if (mp.getMot() == null || mp.getClef() == null) {
      System.out.println("failing:mot=" + mp.getMot() + "/clef=" + mp.getClef());
    }
    list.add(mg);
    //System.out.println("nature=" + mp.getNature() + "/mot=" + mp.getMot() + "/clef=" + mp.getClef());
    //Nature.PART => add Nature.Verb && Mode.PART
    if (mp.getNature() == Nature.PART) {
      mp.setFindRoot(true);
      if(foundPart){
        mp.setNature(VERBE);
      }
    }

    return list;
  }

  Categorie updateCategory(MotGeneral mg, String category) {
    category = wash(category).toLowerCase();
    if (category.equals("first-person")) {
      mg.setPersonne(Personne.PREM);
      return mg.getPersonne();
    } else if (category.equals("second-person")) {
      mg.setPersonne(Personne.DEUX);
      return mg.getPersonne();
    } else if (category.equals("third-person")) {
      mg.setPersonne(Personne.TROI);
      return mg.getPersonne();
    } else if (category.equals("singular")) {
      mg.setNombre(Nombre.SG);
      return mg.getNombre();
    } else if (category.equals("plural")) {
      mg.setNombre(Nombre.PL);
      return mg.getNombre();
    } else if (category.equals("present")) {
      mg.setTemps(Temps.PRES);
      return mg.getTemps();
    } else if (category.equals("imperfect")) {
      mg.setTemps(Temps.IMPF);
      return mg.getTemps();
    } else if (category.equals("future")) {
      mg.setTemps(Temps.FUT);
      return mg.getTemps();
    } else if (category.equals("perfect")) {
      mg.setTemps(Temps.PARF);
      return mg.getTemps();
    } else if (category.equals("pluperfect")) {
      mg.setTemps(Temps.PQPF);
      return mg.getTemps();
    } else if (category.equals("past")) {
      mg.setTemps(Temps.PRETER);
      return mg.getTemps();
    } else if (category.equals("active")) {
      mg.setVoix(Voix.ACT);
      return mg.getVoix();
    } else if (category.equals("passive")) {
      mg.setVoix(Voix.PASS);
      return mg.getVoix();
    } else if (category.equals("indicative")) {
      mg.setMode(Mode.IND);
      return mg.getMode();
    } else if (category.equals("subjunctive")) {
      mg.setMode(Mode.SUBJ);
      return mg.getMode();
    } else if (category.equals("infinitive")) {
      mg.setMode(Mode.INF);
      return mg.getMode();
    } else if (category.equals("imperative")) {
      mg.setMode(Mode.IMPER);
      return mg.getMode();
    } else if (category.equals("participle")) {
      mg.setMode(Mode.PART);
      return mg.getMode();
    } else if (category.equals("supine")) {
      mg.setMode(Mode.SUPIN);
      return mg.getMode();
    } else if (category.equals("nominative")) {
      mg.setCas(Cas.NOM);
      return mg.getCas();
    } else if (category.equals("vocative")) {
      mg.setCas(Cas.VOC);
      return mg.getCas();
    } else if (category.equals("accusative")) {
      mg.setCas(Cas.ACC);
      return mg.getCas();
    } else if (category.equals("genitive")) {
      mg.setCas(Cas.GEN);
      return mg.getCas();
    } else if (category.equals("dative")) {
      mg.setCas(Cas.DAT);
      return mg.getCas();
    } else if (category.equals("ablative")) {
      mg.setCas(Cas.ABL);
      return mg.getCas();
    } else if (category.equals("masculine")) {
      mg.setGendre(Gendre.M);
      return mg.getGendre();
    } else if (category.equals("feminine")) {
      mg.setGendre(Gendre.F);
      return mg.getGendre();
    } else if (category.equals("neuter")) {
      mg.setGendre(Gendre.N);
      return mg.getGendre();
    } else if (category.equals("apocopic")) {
      mg.addAutre(Autre.APOCOPIC);
      return mg.getLastAutre();
    } else if (category.equals("adverbial")) {
      mg.addAutre(Autre.ADVERBIAL);
      return mg.getLastAutre();
    } else if (category.equals("informal")) {
      mg.addAutre(Autre.INFORMAL);
      return mg.getLastAutre();
    } else if (category.equals("formal")) {
      mg.addAutre(Autre.FORMAL);
      return mg.getLastAutre();
    } else if (category.equals("alternative")) {
      mg.addAutre(Autre.ALT);
      return mg.getLastAutre();
    } else if (category.equals("misspelling")) {
      mg.addAutre(Autre.FAUTES);
      return mg.getLastAutre();
    } else if (category.equals("medieval")) {
      mg.addAutre(Autre.MEDIEVAL);
      return mg.getLastAutre();
    } else if (skipCategory.contains(category)) {
      return null;
      //ignore
    } else {
      out.println("[e] unexpected category:" + category);
      return null;
    }
  }
  static List<Categorie> skipCategories = Arrays.asList(
    VERBE, NOUN, IND, ACT, INDECL);

  /*public static void main(String[] args) {
    //imagines adeo similitudinis
    //apio grammaticus scriptum reliquerit
    WiktionaryEnDriver driver;
    try {
      driver = new WiktionaryEnDriver(Lang.LA);
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
      return;
    }
    //DocWriter writer = new DocWriter(driver.out);
    //String[] words = "annos".split(" ");//"ex iis dixisse aut futurae mortis annos aut praeteritae vitae".split(" ");
    driver.translate("ex iis dixisse aut futurae mortis annos aut praeteritae vitae");
    //driver.translate("[Ὡς δ’ἀφῃρέθησαν αἱ τράπεζαι] ἔρχεται αὐτοῖς [ἐπι κῶμον] Συρακόσιός τις ἄνθρωπος,");
    //for(String word:words){
    //  ArrayList<MotGeneral> list = driver.findWord(word);
    //  driver.out.println(list.size());
    //  writer.print(list, skipCategories);
    //}
    driver.quit();
  }*/

}
/*
  function findXPath(xpath){
    var tags = document.evaluate(xpath, document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
    if(tags.snapshotLength == 0){
      return null;
    }
    return tags.snapshotItem(0);
  }
*/
