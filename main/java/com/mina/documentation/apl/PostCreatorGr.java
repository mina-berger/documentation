package com.mina.documentation.apl;

import static com.mina.documentation.DocUtil.washBy;
import com.mina.documentation.linguistique.Lang;
import com.mina.documentation.linguistique.MotGeneral;
import com.mina.documentation.linguistique.MotPrincipal;
import com.mina.documentation.linguistique.ReserveGeneral;
import com.mina.documentation.linguistique.ReservePrincipal;
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
    super(src, ps);
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
      wed.updateMp(mp);
      wfd.updateMp(mp);
    }
    return list;
  }
  @Override
 public void setReserve(ReserveGeneral rg, ReservePrincipal rp) {
   }
  public static void main(String[] args) throws FileNotFoundException{
    PostCreatorGr pcla = new PostCreatorGr(new File("doc/src_gr"), new PrintStream(new File("doc/trg_gr")));
    pcla.translate();
    //pcla.printLine("ex iis dixisse aut futurae mortis annos aut praeteritae vitae");
    pcla.quit();
  }

  
}
