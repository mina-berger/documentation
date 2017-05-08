package com.mina.documentation.apl;

import static com.mina.documentation.DocUtil.washBy;
import static com.mina.documentation.linguistique.Gendre.F;
import static com.mina.documentation.linguistique.Gendre.M;
import com.mina.documentation.linguistique.Lang;
import static com.mina.documentation.linguistique.Mode.OPT;
import com.mina.documentation.linguistique.MotGeneral;
import com.mina.documentation.linguistique.MotPrincipal;
import com.mina.documentation.linguistique.Nature;
import static com.mina.documentation.linguistique.Nature.ADJ;
import static com.mina.documentation.linguistique.Nature.ADV;
import static com.mina.documentation.linguistique.Nature.CONJ;
import static com.mina.documentation.linguistique.Nature.INTERJ;
import static com.mina.documentation.linguistique.Nature.PARTICLE;
import static com.mina.documentation.linguistique.Nature.PREP;
import static com.mina.documentation.linguistique.Nature.PRON;
import static com.mina.documentation.linguistique.Nature.VERBE;
import static com.mina.documentation.linguistique.Nombre.PL;
import static com.mina.documentation.linguistique.Personne.DEUX;
import com.mina.documentation.linguistique.ReserveGeneral;
import com.mina.documentation.linguistique.ReservePrincipal;
import static com.mina.documentation.linguistique.Temps.PRES;
import static com.mina.documentation.linguistique.Voix.ACT;
import com.mina.documentation.site.PerseusDriver;
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
public class PostCreatorGr extends PostCreator {

  PerseusDriver pd;
  WiktionaryEnDriver wed;
  WiktionaryFrDriver wfd;

  public PostCreatorGr(File src, PrintStream ps) {
    super(Lang.GR, src, ps);
    pd = new PerseusDriver(Lang.GR);
    wed = new WiktionaryEnDriver(Lang.GR);
    wfd = new WiktionaryFrDriver(Lang.GR);
  }

  @Override
  public void quit() {
    pd.quit();
    wed.quit();
    wfd.quit();
  }

  @Override
  public List<MotGeneral> find(String clef) {
    ArrayList<MotPrincipal> mpList = new ArrayList<>();
    ArrayList<MotGeneral> list = pd.getMgList(washBy(Lang.GR, clef), mpList);

    for (MotPrincipal mp : mpList) {
      //System.out.println("DEBUG:" + mp.getMot() + ";" + mp.getNature().name());
      if(updateMp(mp)){
        continue;
      }
      if(mp.getNature() == Nature.PART){
        MotPrincipal temp_mp = new MotPrincipal(Nature.VERBE, mp.getMot(), mp.getSens());
        if(updateMp(temp_mp)){
          mp.setSens(temp_mp.getSens());
          continue;
        }
      }
      wed.updateMp(mp);
      wfd.updateMp(mp);
    }
    return list;
  }

  @Override
  public void setReserve(ReserveGeneral rg, ReservePrincipal rp) {
    rg.add(new MotPrincipal(PARTICLE, "ἄν", "indiquant le conditionnel, le futur, lagénéralité ou la répétition"));
    rg.add(new MotPrincipal(CONJ, "ἄν", "=ἐάν si +subj."));
    rg.add(new MotPrincipal(CONJ, "ἀλλά", "mais"));
    rg.add(new MotPrincipal(CONJ, "γάρ", "car, en effet"));
    rg.add(new MotPrincipal(PARTICLE, "γε", "du moins, certes, précisément"));
    rg.add(new MotPrincipal(PARTICLE, "δέ", "d'autre part, s'oppose à μέν"));
    rg.add(new MotPrincipal(ADV, "δήπου", "sans doute, assurément"));
    rg.add(new MotPrincipal(CONJ, "ἐάν", "si +subj."));
    rg.add(new MotPrincipal(CONJ, "εἴτε", "soit ... soit ..."));
    rg.add(new MotGeneral("εἴτε", new MotPrincipal(VERBE, "εἰμί", "être, exister"), DEUX, PL, PRES, OPT, ACT));
    rg.add(new MotPrincipal(CONJ, "εἴπερ", "si toutefois, quoique"));
    rg.add(new MotPrincipal(ADV, "ἐνταῦθα", "là, alors"));
    rg.add(new MotPrincipal(ADV, "ἐνταυθί", "ici"));
    rg.add(new MotPrincipal(ADV, "ἐνταυθοῖ", "ici"));
    rg.add(new MotPrincipal(ADV, "ἔπειτα", "ensuite, dans la suite"));
    rg.add(new MotPrincipal(ADV, "ἔτι", "encore, de plus, désormais"));
    rg.add(new MotPrincipal(ADV, "καί", "même, aussi"));
    rg.add(new MotPrincipal(CONJ, "καί", "et"));
    rg.add(new MotPrincipal(ADV, "κἄν", "καὶ+ἄν, même"));
    rg.add(new MotPrincipal(PARTICLE, "μέν", "d'une part, en vérité"));
    rg.add(new MotPrincipal(ADV, "μή", "ne pas"));
    rg.add(new MotPrincipal(CONJ, "μή", "+subj. de peur que"));
    rg.add(new MotPrincipal(ADV, "νῦν", "maintenant, désormais, à l'instant"));
    rg.add(new MotPrincipal(ADV, "ὁτιοῦν", "quel que soit, du tout"));
    rg.add(new MotPrincipal(ADV, "ὅπως", "comme, de même que"));
    rg.add(new MotPrincipal(CONJ, "ὅπως", "afin que, quand"));
    rg.add(new MotPrincipal(ADV, "οὐδέ", "et ne pas, ni ... ni"));
    
    rg.add(new MotPrincipal(ADV, "πάντως", "tout à fait, en tout cas, totalement"));
    rg.add(new MotPrincipal(ADV, "πρίν", "avant"));
    rg.add(new MotPrincipal(CONJ, "πρίν", "avant que"));
    rg.add(new MotPrincipal(ADV, "τοίνυν", "donc"));
    rg.add(new MotPrincipal(ADV, "τότε", "alors, autrefois, en ce temps-là"));
    rg.add(new MotPrincipal(ADV, "τοτέ", "unefois, parfois, τοτὲ μὲν ... τοτὲ δὲ tantôt ... tantôt"));
    rg.add(new MotPrincipal(ADV, "ὑπό", "en dessous"));
    rg.add(new MotPrincipal(PREP, "ὑπό", "[+gén.] sous, avec [+dat.] sous, avec [+acc.] sous, au pied de"));
    
    rp.add(new MotPrincipal(VERBE, "ἀγανακτέω", "s'indigner, s'irriter contre qqn +dat.,  de chose +acc."));
    rp.add(new MotPrincipal(ADJ, "ἀηδής", "désagréable"));
    rp.add(new MotPrincipal(VERBE, "ἀκούω", "entendre, écouter"));
    rp.add(new MotPrincipal(VERBE, "ἀληθεύω", "dire la vérité"));
    rp.add(new MotPrincipal(VERBE, "ἀμελέω", "négliger +gén."));
    rp.add(new MotPrincipal(VERBE, "ἀναβαίνω", "monter, s'avancer, croître"));
    rp.add(new MotPrincipal(ADJ, "ἀνέλπιστος", "inespéré, qui désespère de +gén."));
    rp.add(new MotPrincipal(VERBE, "ἀποτίνω", "payer, (moy.) venger"));
    
    rp.add(new MotPrincipal(VERBE, "ἀποφεύγω", "échapper à +acc., être absous"));
    rp.add(new MotPrincipal(ADJ, "αὐτός", "lui-même, soi-même"));
    rp.add(new MotPrincipal(VERBE, "βοηθέω", "secourir +dat. ou prép."));
    rp.add(new MotPrincipal(VERBE, "γίγνομαι", "naître, devenir"));
    rp.add(new MotPrincipal(PARTICLE, "δή", "certes, precisément, donc"));
    rp.add(new MotPrincipal(VERBE, "διαφθείρω", "perdre, corrompre, détruire"));
    rp.add(new MotPrincipal(F, "διατριβή", "occupation, genre de vie, étude, conversation"));
    rp.add(new MotPrincipal(VERBE, "διατρίβω", "broyer, retarder"));
    rp.add(new MotPrincipal(M, "διδάσκαλος", "maître"));
    rp.add(new MotPrincipal(VERBE, "διδάσκω", "enseigner, instruire, entraîner"));
    rp.add(new MotPrincipal(ADJ, "ἑαυτοῦ", "de soi-même"));
    rp.add(new MotPrincipal(CONJ, "εἰ", "si"));
    rp.add(new MotPrincipal(PARTICLE, "εἶεν", "soit !"));
    rp.add(new MotPrincipal(VERBE, "εἶμι", "aller"));
    
    rp.add(new MotPrincipal(PRON, "ἐμαυτοῦ", "de moi-même"));
    rp.add(new MotPrincipal(VERBE, "ἐπιλανθάνομαι", "oublier +(acc. ou gén.)"));
    rp.add(new MotPrincipal(VERBE, "ἐπιμελέομαι", "s'occuper de +gén."));
    rp.add(new MotPrincipal(VERBE, "ἐπιχειρέω", "+dat. ou +acc. prendre, entre, attaquer, essayer de prouver"));
    // 
    
    rp.add(new MotPrincipal(VERBE, "ἐργάζομαι", "travailler, faire, exécuter, gagner qqch(+acc.) à qqn(+acc)"));
    rp.add(new MotPrincipal(VERBE, "ἐρωτάω", "interroger"));
    rp.add(new MotPrincipal(VERBE, "ἔρχομαι", "venir, aller"));
    rp.add(new MotPrincipal(VERBE, "ἐξετάζω", "rechercher, examiner, constaterx "));
    rp.add(new MotPrincipal(ADJ, "εὐέλεγκτος", "facile à verifier"));
    rp.add(new MotPrincipal(VERBE, "ἡγέομαι", "conduire +gén. +dat., marcher devant"));
    // 
    rp.add(new MotPrincipal(ADV, "ἤδη", "déjà, desormais, maintenant, bientôt"));
    rp.add(new MotPrincipal(INTERJ, "ἤν", "voilà"));
    rp.add(new MotPrincipal(VERBE, "καταδέω", "manquer de +gén."));
    rp.add(new MotPrincipal(VERBE, "καταψηφίζομαι", "(moy.) voter contre +gén. (pass.)être condamné à +gén"));
    rp.add(new MotPrincipal(VERBE, "κατηγορέω", "accuser qqn(gén.) de (acc.)"));
    rp.add(new MotPrincipal(M, "ἡλικιώτης", "camarade, qui a le même âge"));
    rp.add(new MotPrincipal(VERBE, "λαμβάνω", "saisir, tirer, recevoir"));
    rp.add(new MotPrincipal(VERBE, "μανθάνω", "apprendre, étudier, comprendre"));
    // 
    rp.add(new MotPrincipal(VERBE, "μεταλαμβάνω", "participer à +acc. ou +gén (moy.)revendiquer"));
    rp.add(new MotPrincipal(VERBE, "μιμνήσκω", "rappeler +acc ou +gén, (m.p.) se rappeler, songer à +acc ou +gén"));
    rp.add(new MotPrincipal(PRON, "ὅδε", "celui-ci, ceci, ce, cette"));
    rp.add(new MotPrincipal(VERBE, "οἴομαι", "penser, présager"));
    rp.add(new MotPrincipal(ADV, "ὁμοίως", "semblablement"));
    rp.add(new MotPrincipal(ADJ, "οὐδείς", "aucun, personne, rien"));
    rp.add(new MotPrincipal(CONJ, "ὅτι", "que, parce que, pourquoi"));
    rp.add(new MotPrincipal(ADV, "οὐ", "non, ne pas"));
    rp.add(new MotPrincipal(VERBE, "ὀφλισκάνω", "devoir, avoir une dette ou une amende"));
    rp.add(new MotPrincipal(VERBE, "παραχωρέω", "s'éloigner de +gén., obéir à, concéder à +dat."));
    rp.add(new MotPrincipal(VERBE, "πάρειμι", "être présent, assister, être à la disposition de +dat."));
    rp.add(new MotPrincipal(VERBE, "παρέχω", "fournir, donner"));
    rp.add(new MotPrincipal(VERBE, "πάσχω", "éprouver, ressentir (joie ou douleur)"));
    rp.add(new MotPrincipal(VERBE, "πράσσω", "executer, faire payer"));
    rp.add(new MotPrincipal(VERBE, "προσήκω", "venir à, avoir rapport à, appartenir à, être parent"));
    rp.add(new MotPrincipal(VERBE, "προστάσσω", "commander, imposer, attribuer"));
    rp.add(new MotPrincipal(ADV, "πώποτε", "jamais jusqu'ici"));
    rp.add(new MotPrincipal(ADJ, "σοφός", "habile, savant, sage"));
    rp.add(new MotPrincipal(VERBE, "στρατηγέω", "être général +gén.+dat.+acc, (moy) être commandé"));
    rp.add(new MotPrincipal(VERBE, "σύνοιδα", "sens prés., savoir +acc., être confident +dat."));
    rp.add(new MotPrincipal(F, "συνωμοσία", "ligue, conjuration"));
    rp.add(new MotPrincipal(VERBE, "σώζω", "=σῴζω sauver, garder (moy.)garder pour soi"));
    
    
    rp.add(new MotPrincipal(ADV, "τάχα", "promptement, (avec ἄν) peut-être"));
    rp.add(new MotPrincipal(PARTICLE, "τε", "à la fois ... et ..., (après conj. et rél) précisément"));
    rp.add(new MotPrincipal(VERBE, "τιμωρέω", "[act.] punir qqn(+acc.) de (gén.), [moy.] dommander la punition [pass.] être puni"));
    rp.add(new MotPrincipal(PRON, "τίς", "qui?, lequel?, laquelle"));
    rp.add(new MotPrincipal(ADJ, "τοιοῦτος", "tel"));
    rp.add(new MotPrincipal(VERBE, "ὑπέχω", "mettre sous, prétexter"));
    rp.add(new MotPrincipal(VERBE, "φθονέω", "envier, refuser"));
    rp.add(new MotPrincipal(VERBE, "χαίρω", "se rejouir de +dat."));
    rp.add(new MotPrincipal(M, "χρηματισμός", "maniement d'affaire, négoce"));
    rp.add(new MotPrincipal(ADJ, "χρηστός", "bonne qualité, utile, vertueux"));
    rp.add(new MotPrincipal(VERBE, "ψεύδω", "falsifier [moy] mentir"));
  }

  public static void main(String[] args) throws FileNotFoundException {
    PostCreatorGr pcla = new PostCreatorGr(new File("doc/src_gr"), new PrintStream(new File("doc/trg_gr")));
    //PostCreatorGr pcla = new PostCreatorGr(new File("doc/src_gr"), System.out);
    pcla.translate();
    //pcla.printLine("ex iis dixisse aut futurae mortis annos aut praeteritae vitae");
    pcla.quit();
  }

}
