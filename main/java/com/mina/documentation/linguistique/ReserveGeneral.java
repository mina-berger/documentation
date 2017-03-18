package com.mina.documentation.linguistique;

import static com.mina.documentation.DocUtil.simple;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author minaberger
 */
public class ReserveGeneral extends HashMap<String, ArrayList<MotGeneral>> {
  public void add(MotPrincipal mp){
    add(new MotGeneral(mp));
  }
  public void add(MotGeneral mg){
    String key = simple(mg.getMot());
    if(!containsKey(key)){
      put(key, new ArrayList<>());
    }
    get(key).add(mg);
  }
}
