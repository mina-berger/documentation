/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mina.documentation;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 *
 * @author minaberger
 */
public class DocDriver {

  WebDriver driver;
  
  protected DocDriver(WebDriver driver) {
    this.driver = driver;
  }


  public DocDriver() {
    //System.setProperty("webdriver.gecko.driver", "/Users/minaberger/drive/lib/geckodriver");

    // Create a new instance of the Firefox driver
    // Notice that the remainder of the code relies on the interface, 
    // not the implementation.
    //WebDriver driver = new FirefoxDriver();
    boolean isChrome = false;
    if(isChrome){
      System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "/Users/minaberger/drive/lib/chromedriver");
      ChromeDriverService service = ChromeDriverService.createDefaultService();
      ChromeOptions options = new ChromeOptions();
      options.addArguments("--ignore-certificate-errors");
      driver = new ChromeDriver(service, options);
    }else{
      //System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "error");
      Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
      Logger.getLogger("org.apache.http.impl.execchain").setLevel(Level.OFF);
      HtmlUnitDriver htmlunit_driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_45);
      htmlunit_driver.setJavascriptEnabled(true);
      driver = htmlunit_driver;
    }
    
  }

  public void open(String url) {
    System.out.println("open:" + url);
    driver.get(url);
    // Alternatively the same thing can be done like this
    // driver.navigate().to("http://www.google.com");
  }
  public String text(String xpath, boolean print) {
    if(print){
      System.out.println(xpath);
    }
    return text(xpath);
  }
  
  public String text(String xpath) {
    WebElement element = element(xpath);
    return element == null?null:element.getText();
  }
  public String lastText(String xpath){
    WebElement element = lastElement(xpath);
    return element == null?null:element.getText();
  }
  public WebElement lastElement(String xpath){
    List<WebElement> elements = elements(xpath);
    if(elements == null || elements.isEmpty()){
      return null;
    }
    return elements.get(elements.size() - 1);
  }

  public WebElement element(String xpath) {
    try{
      return driver.findElement(By.xpath(xpath));
    }catch(NoSuchElementException e){
      //System.err.println("no element:" + xpath);
      return null;
    }
  }
  public String subPath(String xpath, int index, String subpath){
    return subPath(xpath, index) + (subpath.startsWith("/")?"":"/") + subpath;
  }
  public String subPath(String xpath, int index){
    return xpath + "[" + (index + 1) + "]";
  }
  public int count(String xpath) {
    return elements(xpath).size();
  }
  public List<WebElement> elements(String xpath) {
    try{
      return driver.findElements(By.xpath(xpath));
    }catch(NoSuchElementException e){
      System.err.println("no element:" + xpath);
      return new ArrayList<>();
    }
  }
  public void sleep(long millis){
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ex) {
      System.err.println(ex.getMessage());
    }
  }
  public void quit(long millis){
    sleep(millis);
    quit();
  }
  public void quit(){
    //Close the browser
    driver.quit();
  }

  /*public boolean isElementPresent(Target target) {
    try {
      findElement(target);
      return true;
    } catch (Exception e) {
      return false;
    }
  }*/
  //function xpath(path, index){ return document.evaluate(path, document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null).snapshotItem(index);}

  public static void main(String[] args) {
    //DocDriver driver = new DocDriver();
    //driver.open("http://www.perseus.tufts.edu/hopper/morph?l=Hljóðs&la=non");
    String str = "Third-person plural (ellos, ellas, also used with ustedes?) present indicative form of esperar.";
    str = str.replaceAll("\\(.*\\)", "");
    System.out.println(str);
 
  }
}
