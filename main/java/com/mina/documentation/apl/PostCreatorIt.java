package com.mina.documentation.apl;

import com.mina.documentation.linguistique.Lang;
import com.mina.documentation.linguistique.MotGeneral;
import com.mina.documentation.linguistique.MotPrincipal;
import com.mina.documentation.linguistique.ReserveGeneral;
import com.mina.documentation.linguistique.ReservePrincipal;
import com.mina.documentation.site.WiktionaryEnDriver;
import com.mina.documentation.site.WiktionaryFrDriver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author minaberger
 */
public class PostCreatorIt extends PostCreator {
  
  WiktionaryEnDriver wed;
  WiktionaryFrDriver wfd;
  public PostCreatorIt(File src, PrintStream ps) {
    super(Lang.IT, src, ps);
    wed = new WiktionaryEnDriver(Lang.IT);
    wfd = new WiktionaryFrDriver(Lang.IT);
  }

  @Override
  public void quit() {
    wed.quit();
    wfd.quit();
  }
  @Override
  public List<MotGeneral> find(String clef) {
    return findWord(clef, null);
  }

  ArrayList<MotGeneral> findWord(String word, MotPrincipal chercher) {
    ArrayList<MotPrincipal> mpList = new ArrayList<>();
    ArrayList<MotGeneral> list = wed.getMgList(word, chercher, mpList);
    
    for (MotPrincipal mp : mpList) {
      //wed.updateMp(mp);
      wfd.updateMp(mp);
      if(mp.findRoot()){
        list.addAll(findWord(mp.getClef(), mp));
      }
    }
    return list;
  }
  @Override
  public void setReserve(ReserveGeneral rg, ReservePrincipal rp) {
  }
  public static void main(String[] args) throws FileNotFoundException{
    PostCreatorIt pcla = new PostCreatorIt(new File("doc/src_it"), new PrintStream(new File("doc/trg_it")));
    //PostCreatorIt pcla = new PostCreatorIt(new File("doc/src_it"), System.out);
    pcla.translate();
    //pcla.printLine("ex iis dixisse aut futurae mortis annos aut praeteritae vitae");
    pcla.quit();
  }
}
