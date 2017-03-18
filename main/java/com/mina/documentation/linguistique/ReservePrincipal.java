package com.mina.documentation.linguistique;

import static com.mina.documentation.DocUtil.simple;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author minaberger
 */
public class ReservePrincipal extends HashMap<String, ArrayList<MotPrincipal>> {
  public void add(MotPrincipal mp){
    String key = simple(mp.getMot());
    if(!containsKey(key)){
      put(key, new ArrayList<>());
    }
    get(key).add(mp);
  }
  public boolean update(MotPrincipal mp){
    String key = simple(mp.getMot());
    if(!containsKey(key)){
      return false;
    }
    Nature nature = mp.getNature();
    ArrayList<String> update = new ArrayList<>();
    for(MotPrincipal _mp:get(key)){
      if(!_mp.getNature().equals(nature)){
        continue;
      }
      update.add(_mp.getSens());
      for(String variation:_mp.getVariations()){
        mp.addVariation(variation);
      }
    }
    if(update.isEmpty()){
      return false;
    }
    mp.setSens(String.join(" // ", update));
    return true;
  }
}
