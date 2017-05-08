package com.mina.selenium.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import com.mina.selenium.SeleniumWork;

public class WiktionaryGreekVerb extends SeleniumWork {
	public WiktionaryGreekVerb() {
		super(Browser.Chrome, "greek_verb");
	}
	BufferedWriter writer;
	String hide = "\\[hide ▲\\]\n";
	@Override
	public void execute() {
		try {
			File file = new File("c:/doc/greek/verb.html");
			file.getParentFile().mkdirs();
			writer = new BufferedWriter(new FileWriter(file));
			writer.write("<HTML>");
			writer.write("<HEAD>");
			writer.write("<STYLE type=\"text/css\">");
			writer.write("<!--");
			writer.write("body {font-family: \"Palatino Linotype\"; }");
			writer.write("-->");
			writer.write("</STYLE>");
			writer.write("</HEAD>");
			writer.write("<BODY>");
			writer.newLine();
			open("http://en.wiktionary.org/w/index.php?title=Category:Ancient_Greek_verbs&subcatfrom=%CE%92&filefrom=%CE%92&pageuntil=%CE%92#mw-pages");
		//getPage();
			while(true){
				getPage();
        
				if(isElementPresent("//a[text()='next 200']")){
					click("//a[text()='next 200']");
				}else{
					break;
				}
			}
			/*getVerb("http://en.wiktionary.org/wiki/%E1%BC%80%CE%B5%CE%AF%CE%B4%CF%89#Ancient%20Greek");
			getVerb("http://en.wiktionary.org/wiki/%E1%BC%94%CF%87%CF%89#Ancient%20Greek");
			getVerb("http://en.wiktionary.org/wiki/%CE%B2%CE%B1%CE%AF%CE%BD%CF%89#Ancient%20Greek");
			getVerb("http://en.wiktionary.org/wiki/%CE%BB%CF%8D%CF%89#Ancient%20Greek");
			getVerb("http://en.wiktionary.org/wiki/%CF%86%CE%AD%CF%81%CF%89#Ancient%20Greek");
			getVerb("http://en.wiktionary.org/wiki/%CF%80%CE%AD%CE%BC%CF%80%CF%89#Ancient%20Greek");
			*/

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				writer.write("<BODY></HTML>");
				writer.newLine();
				writer.flush();
				writer.close();
			}catch(Exception e){}
		}
	}
	public void getPage(){
		String col = "//div[@id='mw-pages']//td";
		int count_col = getElementSize(col);
    System.out.println("col_ul=" + count_col);
		for(int i = 0;i < count_col;i++){
			String col_ul = col + "[" + (i + 1) + "]//ul";
			int count_col_ul = getElementSize(col_ul);
      //count_col_ul = 1;
			System.out.println("col_ul=" + count_col_ul);
			for(int j = 0;j < count_col_ul;j++){
				String col_ul_li = col_ul + "[" + (j + 1) + "]/li";
				int count_col_ul_li = getElementSize(col_ul_li);
				for(int k = 0;k < count_col_ul_li;k++){
					click(col_ul_li + "[" + (k + 1) + "]//a");
					pause(1000);
					getVerb();
					goBack();
					pause(1000);
				}
			}
		}
		//System.out.println("col=" + count_col);


	}
	public void getVerb() {
		Verb verb = new Verb();
		comment(getCurrentUrl());
		int count_tradict = getElementSize("//div[@id='mw-content-text']/ol[1]/li[1]/a");
		comment("DEBUG:tradict=" + count_tradict);
		verb.tradict = new String[count_tradict];
		for(int i = 0;i < count_tradict;i++){
			verb.tradict[i] = getText("//div[@id='mw-content-text']/ol[1]/li[1]/a[" + (i + 1) + "]");
		}

		int count_frame = getElementSize("//div[@class='NavFrame']");
		comment("DEBUG:frame=" + count_frame);
		for(int i = 0;i < count_frame;i++){
			String frame = "//div[@class='NavFrame'][" + (i + 1) + "]";
			click(frame + "//span[@class='NavToggle']");
			Conjugation conj = new Conjugation();
			conj.title = getText(frame + "/div[@class='NavHead']").replaceAll(hide, "").trim();
			if(getConjugation(frame, conj)){
				verb.add(conj);
			}
		}
		print(verb);
	}
	boolean getConjugation(String frame, Conjugation conj){
		pause(1000);
		//comment(getText(frame + "/div[@class='NavContent']//tr[3]"));
		for(int i = 0;i < CONJUGATION_LENGTH;i++){
			String td = frame + "/div[@class='NavContent']//tr[3]/td[" + (i + 1) + "]";
			if(!isElementPresent(td)){
				return false;
			}
			conj.texts[i] = getText(td).replaceAll("/\n", "\n");
		}
		return true;
	}
	static final int CONJUGATION_LENGTH = 8;
	class Verb extends ArrayList<Conjugation> {
		String[] tradict = null;
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;}
	class Conjugation {
		String title;
		String[] texts = new String[CONJUGATION_LENGTH];

	}
	void print(Verb verb){
		try{
			writer.write("<P>");
			writer.newLine();
			for(String tradict:verb.tradict){
				writer.write(tradict + " ");
			}
			writer.write("<BR>");
			writer.newLine();
			writer.write("<UL>");
			writer.newLine();

			for(Conjugation conj:verb){
				int index = conj.title.indexOf(":");
				writer.write("<LI>" + conj.title.substring(0, index) + "</LI>");
				writer.write("<UL>");
				writer.newLine();
				for(int i = 0;i < conj.texts.length;i++){
					writer.write("<LI>" + conj.texts[i].replace("\n", "/") + "</LI>");
				}
				writer.write("</UL>");
				writer.newLine();
			}
			writer.write("</UL>");
			writer.newLine();
			writer.write("</P>");
			writer.newLine();

		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
  public static void main(String[] arg){
    WiktionaryGreekVerb wgv = new WiktionaryGreekVerb();
    wgv.before();
    wgv.test();
    wgv.after();
  }

}
