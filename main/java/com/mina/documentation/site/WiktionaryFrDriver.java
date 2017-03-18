package com.mina.documentation.site;

import com.mina.documentation.DocDriver;
import static com.mina.documentation.DocUtil.simple;
import com.mina.documentation.linguistique.Lang;
import com.mina.documentation.linguistique.MotPrincipal;
import com.mina.documentation.linguistique.Nature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.WebElement;

/**
 *
 * @author minaberger
 */
public class WiktionaryFrDriver extends DocDriver {
  static List<String> skipH3 = Arrays.asList(
    "Étymologie", "Références", "Prononciation", "Anagrammes",
    "Voir aussi", "Symbole");
  static HashMap<Lang, String> langAtt = new HashMap<>();
  static{
    langAtt.put(Lang.LA, "Latin");
    langAtt.put(Lang.GR, "Grec_ancien");
    langAtt.put(Lang.ES, "Espagnol");
  }
  Lang lang;
  public WiktionaryFrDriver(Lang lang){
    this.lang = lang;
  }
  
  public boolean updateMp(MotPrincipal mp){
    String att = langAtt.get(lang);
    open("https://fr.wiktionary.org/wiki/" + mp.getClef());
    String xpath = "//div[@id='mw-content-text']/*";
    List<WebElement> elements = elements(xpath);
    loop1:for(int j = 0;j < 2;j++){
      boolean on = false;
      loop2:for(int i = 0;i < elements.size();i++){
        WebElement element = elements.get(i);
        if(!on){
          if(!element.getTagName().equals("h2")){
            continue;
          }
          if(count(subPath(xpath, i) + "/span[@id='" + att + "']") == 0){
            continue;
          }
          //System.out.println("on");
          on = true;
          continue;
        }
        if(!element.getTagName().equals("h3")){
          continue;
        }
        if(element.getTagName().equals("h2")){
          continue loop1;
        }
        String category = text(subPath(xpath, i) + "/span[@class='mw-headline']");
        if(skipH3.contains(category)){
          continue;
        }
        //System.out.println(category);
        //Nature.
        boolean found;
        switch(mp.getNature()){
          case VERBE:
            found = category.startsWith("Verbe");
            break;
          case PART:
            found = category.startsWith("Forme de verbe");
            break;
          case NOUN:
            found = category.startsWith("Nom commun");
            break;
          case ADV:
            found = category.startsWith("Adverbe");
            break;
          case ADJ:
            found = category.startsWith("Adjectif");
            break;
          case PARTICLE:
            found = category.startsWith("Adverbe");
            break;
          case CONJ:
            found = category.startsWith("Conjonction");
            break;
          case INTERJ:
            found = category.startsWith("Interjection");
            break;
          case PREP:
            found = category.startsWith("Préposition") || category.startsWith("Forme de nom");
            break;
          case PRON:
            //Pronom interrogatif, Pronom relatif, Pronom démonstratif
            found = category.startsWith("Pronom") || category.startsWith("Adjectif possessif");
            break;
          case ART:
            found = category.startsWith("Article défini");
            break;
          case CONTR:
            found = category.startsWith("Forme d’article défini");
            break;
          case NUM:
            found = category.startsWith("Adjectif numéral");
            break;
           
            //
          default:
            System.out.println("[f] not found nature:" + mp.getNature().name() + "/mot=" + mp.getMot() 
              + "/category=" + category);
            found = false;
        }
        if(!found){
          continue;
        }
        int index = i + 1;
        while(true){
          if(index >= elements.size() - 1){
            continue loop2;
          }
          WebElement p = element(subPath(xpath, index));
          if(p.getTagName().equals("p")){
            break;
          }
          index++;
        }
        String titlePath = subPath(xpath, index) + "//b";
        String title = text(titlePath);
        //System.out.println(titlePath + ":" + title + ":" + mp.getMot());
        if(!(
          (j == 0 && title.equals(mp.getMot())) || 
          (j == 1 && (simple(title).contains(simple(mp.getMot())) || simple(mp.getMot()).contains(simple(title)))))) {
          continue;
        }
        update(subPath(xpath, index + 1), mp);
        return true;
      }
    }
    return false;
  }
  void update(String xpath, MotPrincipal mp){
    List<WebElement> elements = elements(xpath + "/li");
    ArrayList<String> list = new ArrayList<>();
    for(int i = 0;i < elements.size();i++){
      WebElement element = elements.get(i);
      String text = element.getText();
      for(String tag:new String[]{"ul", "ol", "dl"}){
        List<WebElement> removeElems = elements(subPath(xpath + "/li", i) + "/" + tag);
        for(WebElement removeElem:removeElems){
          String remove = removeElem.getText();
          if(remove != null){
            text = text.replace(remove, "").trim();
          }
        }
      }
      list.add(text.toLowerCase());
    }
    mp.setSens(String.join(" / ", list));
  }

  public static void main(String[] args){
    //imagines adeo similitudinis indiscretae pinxit, ut
    WiktionaryFrDriver driver = new WiktionaryFrDriver(Lang.ES);
    MotPrincipal mp = new MotPrincipal(Nature.ADV, "dónde", "");
    mp.setClef("dónde");
    System.out.println(driver.updateMp(mp));
    System.out.println(mp.getSens());
  }
  
}
