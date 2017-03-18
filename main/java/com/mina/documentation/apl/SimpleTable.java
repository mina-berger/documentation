package com.mina.documentation.apl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author minaberger
 */
public class SimpleTable {
  public static void main(String[] args) throws FileNotFoundException, IOException{
    String text = "";
    BufferedReader reader = new BufferedReader(new FileReader("doc/input"));
    while(reader.ready()){
      text += reader.readLine() + "\n";
    }
    clean(text);
  }
  public static void clean(String str){
    str = replaceAttribute(str, "style");
    str = replaceAttribute(str, "class");
    str = replaceAttribute(str, "width");
    str = replaceAttribute(str, "border");
    str = replaceAttribute(str, "bgcolor");
    str = replaceAttribute(str, "cellspacing");
    str = replaceAttribute(str, "data-sheets-value");
    //str = replaceAttribute(str, "dir");
    
    str = removeTag(str, "span");
    str = removeTag(str, "a");
    str = removeTag(str, "b");
    str = removeTag(str, "i");
    str = removeTag(str, "strong");
    str = removeTag(str, "div");
    System.out.println(str);
  }
  public static String replaceAttribute(String str, String att){
    while(true){
      int index1 = str.indexOf(att);
      if(index1 < 0){
        return str;
      }
      int index2 = str.indexOf("\"", index1);
      index2 = str.indexOf("\"", index2 + 1);
      str = str.substring(0, index1).trim() + str.substring(index2 + 1, str.length());
    }
  }
  public static String removeTag(String str, String tag){
    while(true){
      int index1 = str.indexOf("<" + tag + " ");
      if(index1 < 0){
        index1 = str.indexOf("<" + tag + ">");
        if(index1 < 0){
          return str;
        }
      }
      int index2 = str.indexOf(">", index1);
      str = str.substring(0, index1).trim() + str.substring(index2 + 1, str.length());
      int index3 = str.indexOf("</" + tag);
      int index4 = str.indexOf(">", index3);
      str = str.substring(0, index3).trim() + str.substring(index4 + 1, str.length());
    }
  }
  
}
