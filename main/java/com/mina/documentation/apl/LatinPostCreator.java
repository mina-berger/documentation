package com.mina.documentation.apl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 *
 * @author minaberger
 */
public class LatinPostCreator {
  public LatinPostCreator(){
  }
  public ArrayList<String> readFile(){
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader("doc/latin"));
      ArrayList<String> list = new ArrayList<>();
      while(in.ready()){
        String line = in.readLine();
        line = line.trim();
        list.add(line);
      }
      in.close();
      return list;
    } catch (IOException ex) {
        throw new IllegalStateException(ex);
    }
  }
  public void print(PrintStream out){
    LatinTranslator lt = new LatinTranslator(out);
    try {
      Thread.sleep(10000);
    } catch (InterruptedException ex) {
    }
    ArrayList<String> lines = readFile();

    while(!lines.isEmpty()){
      String line = lines.remove(0);
      if(line.isEmpty()){
        continue;
      }
      out.println("<h3>" + line + "</h3>");
      lt.translate(line);
    }
    lt.quit();
  }
  public static void main(String[] args) throws FileNotFoundException{
    PrintStream out = new PrintStream(new File("doc/latin.trans"));
    
    LatinPostCreator creator = new LatinPostCreator();
    creator.print(out);

  }
  
}
