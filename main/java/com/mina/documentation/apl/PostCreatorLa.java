package com.mina.documentation.apl;

import com.mina.documentation.linguistique.Lang;
import com.mina.documentation.linguistique.MotGeneral;
import com.mina.documentation.linguistique.MotPrincipal;
import static com.mina.documentation.linguistique.Nature.ADJ;
import static com.mina.documentation.linguistique.Nature.ADV;
import static com.mina.documentation.linguistique.Nature.CONJ;
import static com.mina.documentation.linguistique.Nature.PREP;
import static com.mina.documentation.linguistique.Nature.VERBE;
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
public class PostCreatorLa extends PostCreator {
  
  WiktionaryEnDriver wed;
  WiktionaryFrDriver wfd;
  public PostCreatorLa(File src, PrintStream ps) {
    super(src, ps);
    wed = new WiktionaryEnDriver(Lang.LA);
    wfd = new WiktionaryFrDriver(Lang.LA);
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
      if(updateMp(mp)){
        continue;
      }
      wed.updateMp(mp);
      wfd.updateMp(mp);
      if(mp.findRoot()){
        list.addAll(findWord(mp.getClef(), mp));
      }
    }
    return list;
  }
  @Override
 public void setReserve(ReserveGeneral rg, ReservePrincipal rp) {
    rg.add(new MotPrincipal(PREP, "ad", "+acc. direction out proximité, vers, pour"));
    rg.add(new MotPrincipal(ADV,  "adhuc", "jusqu'à ce point-ci, de plus"));
    rg.add(new MotPrincipal(CONJ, "cum", "de sub., lorsque +ind, (cause) parce que +subj"));
    rg.add(new MotPrincipal(CONJ, "donec", "de sub., aussi lontemps que +ind., jusqu'à ce que +subj."));
    rg.add(new MotPrincipal(CONJ, "dum", "pendent que, aussi longtemps que"));
    rg.add(new MotPrincipal(ADV,  "enim", "assurément"));
    rg.add(new MotPrincipal(CONJ, "ergō", "de coord., donc"));
    rg.add(new MotPrincipal(ADV,  "etiam", "encore, ainsi, également"));
    rg.add(new MotPrincipal(ADV,  "ibi", "ici, en ce lieu"));
    rg.add(new MotPrincipal(ADV,  "inde", "de là, de ce lieu, à partir de ce moment, dès lors, après cela"));
    rg.add(new MotPrincipal(ADV,  "ita", "de même, encore"));
    rg.add(new MotPrincipal(ADJ,  "jam", "désormais, déjà"));
    rg.add(new MotPrincipal(CONJ, "nam", "causale, car"));
    rg.add(new MotPrincipal(ADV,  "nonne", "interr, est-ce que... ne pas, (indir.)si... ne... pas"));
    rg.add(new MotPrincipal(CONJ, "quia", "parce que"));
    rg.add(new MotPrincipal(ADV,  "quod", "rél. quant ce fait que"));
    rg.add(new MotPrincipal(CONJ, "si", "de coord.,"));
    rg.add(new MotPrincipal(ADV,  "sic", "ainsi, de cette manière"));
    rg.add(new MotPrincipal(ADV,  "simul", "ensemble, en même temps"));
    rg.add(new MotPrincipal(ADV,  "tamen", "pourtant, cependant"));
    
    rp.add(new MotPrincipal(VERBE, "exeō", "sortir, dépasser (sortir des limites)", "exīre", "exiī", "exitum"));
  }
  
  public static void main(String[] args) throws FileNotFoundException{
    //PostCreatorLa pcla = new PostCreatorLa(new File("doc/src_la"), new PrintStream(new File("doc/trg_la")));
    PostCreatorLa pcla = new PostCreatorLa(new File("doc/src_test"), System.out);
    pcla.translate();
    //pcla.printLine("ex iis dixisse aut futurae mortis annos aut praeteritae vitae");
    pcla.quit();
  }

  
}
