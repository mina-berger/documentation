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
public class VoluspaPostCreator {
  int number;
  public VoluspaPostCreator(int number){
    this.number = number;
  }
  public ArrayList<String> readEn(){
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader("doc/Völuspá.en"));
      ArrayList<String> list = new ArrayList<>();
      boolean start = false;
      while(true){
        String line = in.readLine();
        String numStr = number + ".";
        if(!start){
          if(line.startsWith(numStr)){
            start = true;
            line = line.substring(numStr.length()).trim();
          }else{
            continue;
          }
        }
        numStr = (number + 1) + ".";
        if(start && line.startsWith(numStr)){
          break;
        }
        line = line.trim();
        if(line.isEmpty()){
          continue;
        }
        //System.out.println(line);
        list.add(line);
      }
      return list;
    } catch (IOException ex) {
        throw new IllegalStateException(ex);
    }
  }
  public ArrayList<String> readOn(){
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader("doc/Völuspá.on"));
      ArrayList<String> list = new ArrayList<>();
      boolean start = false;
      while(true){
        String line = in.readLine();
        String numStr = number + ".";
        if(!start){
          if(line.startsWith(numStr)){
            start = true;
          }
          continue;
        }
        numStr = (number + 1) + ".";
        if(start && line.startsWith(numStr)){
          break;
        }
        line = line.trim();
        if(line.isEmpty()){
          continue;
        }
        //System.out.println(line);
        list.add(line);
      }
      return list;
    } catch (IOException ex) {
        throw new IllegalStateException(ex);
    }
  }
  public void print(PrintStream out){
    NorseTranslator nt = new NorseTranslator(out);
    try {
      Thread.sleep(10000);
    } catch (InterruptedException ex) {
    }
    out.println("Völuspá " + number);
    out.println("");

    out.println("<h1>" + number + ".</h1>");
    ArrayList<String> listEn = readEn();
    ArrayList<String> listOn = readOn();

    while(true){
      String lineOn = listOn.remove(0) + " / " + listOn.remove(0);
      String lineEn = listEn.remove(0) + " / " + listEn.remove(0);
      out.println("<h3>" + lineOn + "</h3>");
      out.println(lineEn);
      nt.translate(lineOn);
      if(listOn.isEmpty()){
        break;
      }
    }
    nt.quit();
  }
  public static void main(String[] args) throws FileNotFoundException{
    PrintStream out = new PrintStream(new File("doc/voluspa_post"));
    
    VoluspaPostCreator creator = new VoluspaPostCreator(33);
    creator.print(out);

  }
  
}
