package com.mina.documentation.apl;

import static com.mina.documentation.linguistique.Cas.ABL;
import static com.mina.documentation.linguistique.Cas.ACC;
import static com.mina.documentation.linguistique.Cas.NOM;
import static com.mina.documentation.linguistique.Cas.VOC;
import static com.mina.documentation.linguistique.Gendre.F;
import static com.mina.documentation.linguistique.Gendre.N;
import com.mina.documentation.linguistique.Lang;
import com.mina.documentation.linguistique.MotGeneral;
import com.mina.documentation.linguistique.MotPrincipal;
import static com.mina.documentation.linguistique.Nature.ADJ;
import static com.mina.documentation.linguistique.Nature.ADV;
import static com.mina.documentation.linguistique.Nature.CONJ;
import static com.mina.documentation.linguistique.Nature.PREP;
import static com.mina.documentation.linguistique.Nature.PRON;
import static com.mina.documentation.linguistique.Nature.VERBE;
import static com.mina.documentation.linguistique.Nombre.PL;
import static com.mina.documentation.linguistique.Nombre.SG;
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
    super(Lang.LA, src, ps);
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
      //System.out.println(mp.getMot() + ":" + mp.findRoot());
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
   MotPrincipal mp;
    rg.add(new MotPrincipal(PREP, "ad", "[+acc.] (direction ou proximité) à, vers, chez, dans la direction de, jusqu'à (but) pour, en vue de"));
    rg.add(new MotPrincipal(ADV,  "adhuc", "jusqu'à ce point-ci, de plus"));
    mp = new MotPrincipal(ADJ, "alius", "autre");
    rg.add(new MotGeneral("alia", mp, NOM, F, SG));
    rg.add(new MotGeneral("alia", mp, VOC, F, SG));
    rg.add(new MotGeneral("aliā", mp, ABL, F, SG));
    rg.add(new MotGeneral("alia", mp, NOM, N, PL));
    rg.add(new MotGeneral("alia", mp, VOC, N, PL));
    rg.add(new MotGeneral("alia", mp, ACC, N, PL));
    
    
    rg.add(new MotPrincipal(PREP, "cum", "[+abl.] avec, an même temp que"));
    rg.add(new MotPrincipal(CONJ, "cum", "de sub., [+ind.] lorsque, quand, [+subj] (cause) puisque, parce que, lorsque"));
    rg.add(new MotPrincipal(CONJ, "dōnec", "de sub., [+ind.] aussi lontemps que [+subj.] jusqu'à ce que"));
    rg.add(new MotPrincipal(PREP, "dē", "[+abl.] (lieu) de, venant de (temps) au moment de (manière, cause, relation) d'après, sur"));
    rg.add(new MotPrincipal(ADV,  "deinde", "ensuite, après"));
    rg.add(new MotPrincipal(ADV,  "dum", "jusquà ce point, maintenant, encore"));
    rg.add(new MotPrincipal(CONJ, "dum", "[+ind.] pendent que, aussi longtemps que [+subj.] jusqu'à ce que"));
    rg.add(new MotPrincipal(ADV,  "ecce", "voici, voici que"));
    rg.add(new MotPrincipal(ADV,  "enim", "assurément"));
    rg.add(new MotPrincipal(CONJ, "enim", "de coord., car, en effet"));
    rg.add(new MotPrincipal(CONJ, "ergō", "de coord., donc, par condéquent"));
    rg.add(new MotPrincipal(ADV,  "et", "aussi, même"));
    rg.add(new MotPrincipal(CONJ, "et", "de coord., et"));
    rg.add(new MotPrincipal(ADV,  "etiam", "encore, ainsi, également"));
    rg.add(new MotPrincipal(PREP, "ex", "[+abl.] de, hors de, en sortant de, après, venant de, à partir de, depuis, de la part de, par suite de, d’après"));
    rg.add(new MotPrincipal(ADV,  "hinc", "d'ici, de là, à partir d'ici"));
    rg.add(new MotPrincipal(ADV,  "ibi", "ici, en ce lieu"));
    rg.add(new MotPrincipal(PREP, "in", "à l'interieur de [+acc.] (avec mvt) dans [+abl.] (sans mvt.) dans, parmi, chez"));
    rg.add(new MotPrincipal(ADV,  "inde", "de là, de ce lieu, à partir de ce moment, dès lors, après cela"));
    rg.add(new MotPrincipal(ADV,  "ita", "ainsi, de cette manière-là, encore"));
    rg.add(new MotPrincipal(ADJ,  "jam", "désormais, déjà"));
    
    rg.add(new MotPrincipal(ADV,  "maximē", "au plus haut degré"));
    rg.add(new MotPrincipal(ADV,  "minimē", "très peu"));
    rg.add(new MotPrincipal(ADV,  "multō", "beaucoup"));
    //
    
    rg.add(new MotPrincipal(CONJ, "nam", "(causale) car"));
    rg.add(new MotPrincipal(CONJ, "nec", "de coord., et ne pas"));
    rg.add(new MotPrincipal(ADV,  "nonne", "interr, est-ce que... ne pas, (indir.)si... ne... pas"));
    rg.add(new MotPrincipal(ADV, "pariter", "également"));
    rg.add(new MotPrincipal(CONJ, "quia", "parce que"));
    rg.add(new MotPrincipal(ADV,  "quārē", "interr. pourquoi ?, rél. par quoi"));
    rg.add(new MotPrincipal(CONJ, "quārē", "c'est pourquoi"));
    rg.add(new MotPrincipal(ADV,  "quod", "rél., quant ce fait que"));
    rg.add(new MotPrincipal(CONJ, "quod", "[+ind. +subj] ce fait que"));
    rg.add(new MotPrincipal(CONJ, "si", "de coord., si"));
    rg.add(new MotPrincipal(ADV,  "sic", "ainsi, de cette manière"));
    rg.add(new MotPrincipal(ADV,  "simul", "ensemble, en même temps"));
    rg.add(new MotPrincipal(ADV,  "tamen", "pourtant, cependant"));
    
    rp.add(new MotPrincipal(VERBE, "exeō", "sortir, dépasser (sortir des limites)", "exīre", "exiī", "exitum"));
    rp.add(new MotPrincipal(ADV,   "fōrte", "par hasard"));
    rp.add(new MotPrincipal(VERBE, "sum", "être, exister, vivre"));
    rp.add(new MotPrincipal(PRON, "ūnusquisque", "chaque"));
    //
    rp.add(new MotPrincipal(F,  "vīcīna", "voisine,celle qui est à proximité", "vīcīnae"));
    
  }
  
  public static void main(String[] args) throws FileNotFoundException{
    PostCreatorLa pcla = new PostCreatorLa(new File("doc/src_la"), new PrintStream(new File("doc/trg_la")));
    //PostCreatorLa pcla = new PostCreatorLa(new File("doc/src_test"), System.out);
    pcla.translate();
    //pcla.printLine("ex iis dixisse aut futurae mortis annos aut praeteritae vitae");
    pcla.quit();
  }

  
}
