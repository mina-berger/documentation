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
public class PostCreatorOn extends PostCreator {
  
  PerseusDriver pd;
  WiktionaryEnDriver wed;
  public PostCreatorOn(File src, PrintStream ps) {
    super(src, ps);
    pd = new PerseusDriver(Lang.ON);
    wed = new WiktionaryEnDriver(Lang.ON);
  }

  @Override
  public void quit() {
    pd.quit();
    wed.quit();
  }
  @Override
  public List<MotGeneral> find(String clef) {
    ArrayList<MotPrincipal> mpList = new ArrayList<>();
    ArrayList<MotGeneral> list = pd.getMgList(washBy(Lang.ON, clef), mpList);
    
    for (MotPrincipal mp : mpList) {
      wed.updateMp(mp);
    }
    return list;
  }
  @Override
 public void setReserve(ReserveGeneral rg, ReservePrincipal rp) {
   }
  public static void main(String[] args) throws FileNotFoundException{
    PostCreatorOn pcla = new PostCreatorOn(new File("doc/src_on"), new PrintStream(new File("doc/trg_on")));
    pcla.translate();
    //pcla.printLine("ex iis dixisse aut futurae mortis annos aut praeteritae vitae");
    pcla.quit();
  }

  
}
