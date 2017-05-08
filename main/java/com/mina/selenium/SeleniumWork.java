package com.mina.selenium;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.mina.selenium.Inspectable.Attribute;
import com.mina.selenium.Inspectable.CurrentUrl;
import com.mina.selenium.Inspectable.ElementPresent;
import com.mina.selenium.Inspectable.ElementSize;
import com.mina.selenium.Inspectable.Enabled;
import com.mina.selenium.Inspectable.Not;
import com.mina.selenium.Inspectable.Selected;
import com.mina.selenium.Inspectable.SelectedText;
import com.mina.selenium.Inspectable.SelectedValue;
import com.mina.selenium.Inspectable.Text;
import com.mina.selenium.Inspectable.Title;
//import com.mina.util.Util;
//import com.thoughtworks.selenium.Wait;
//import com.thoughtworks.selenium.Wait.WaitTimedOutException;
import org.openqa.selenium.safari.SafariDriver;

public abstract class SeleniumWork extends WebDriverHolder {

  private static final String CHROME_DRIVER = "/Users/minaberger/drive/lib/chromedriver";

  private static final String EVIDENCE_DIR = "/Users/minaberger/drive/selenium/evidence";
  private Capabilities capabilities;

  public static enum Browser {
    Chrome, Firefox, HtmlUnit, Safari;
  }

  private static enum Command {
    Comment("info"), Done("exec"), Success("OK"), Failed("NG"), Exception("exception");
    private final String name;

    Command(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  private enum SelectOptionKey {
    index, label, value;
    /*public String getValue(String locator){
			if(!locator.toLowerCase().startsWith(key + "=")){
				return null;
			}
			return l例外発生時刻ocator.substring(key.length() + 1);
		}*/
  }

  private final Browser browser;
  //private String test_name;
  private final File evidence_dir;
  private int screen_shot_number;

  /**
   * sleep time(ms)
   */
  private long speed;
  /**
   * waitFor time (ms)
   */
  private long timeout;

  public SeleniumWork(Browser browser, String test_name) {
    super();
    this.browser = browser;
    //this.test_name = test_name;
    evidence_dir = new File(EVIDENCE_DIR, test_name);
    evidence_dir.mkdirs();
    screen_shot_number = 0;

    speed = 0;
    timeout = 30000;
  }

  @Before
  public void before() {
    System.out.println("[info] activate " + browser.toString());
    switch (browser) {
      case Chrome:
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, CHROME_DRIVER);
        ChromeDriverService service = ChromeDriverService.createDefaultService();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--ignore-certificate-errors");
        ChromeDriver chrome_driver = new ChromeDriver(service, options);
        capabilities = chrome_driver.getCapabilities();
        driver = chrome_driver;
        break;
      case Firefox:
        System.setProperty("webdriver.gecko.driver", "/Users/minaberger/drive/lib/geckodriver");

        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(false);
        FirefoxDriver firefox_driver = new FirefoxDriver(profile);

        //System.setProperty("webdriver.firefox.bin", "/Applications/Firefox.app/Contents/MacOS/firefox-bin");
        //FirefoxDriver firefox_driver = new FirefoxDriver();
        capabilities = firefox_driver.getCapabilities();
        driver = firefox_driver;
        break;
      case Safari:
        SafariDriver safari_driver = new SafariDriver();
        capabilities = safari_driver.getCapabilities();
        driver = safari_driver;
        break;
      case HtmlUnit:
        HtmlUnitDriver htmlunit_driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_45);
        htmlunit_driver.setJavascriptEnabled(true);
        capabilities = htmlunit_driver.getCapabilities();
        driver = htmlunit_driver;
        break;
      default:
        throw new SeleniumRuntimeException("ブラウザの指定が不正です。");
    }
    //Opera1 NG
    //DesiredCapabilities capabilities = new DesiredCapabilities();
    //capabilities.setBrowserName("opera");
    //driver = new RemoteWebDriver(new URL("http://www.google.co.jp/"), capabilities);

    //Opera2 NG
    //driver = new OperaDriver();
    //IE NG
    //System.setProperty("webdriver.ie.driver", "c:/mina/lib/IEDriverServer.exe");
    //DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
    //ieCapabilities.setCapability(
    //    InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
    //    true
    //);
    //driver = new InternetExplorerDriver(ieCapabilities);
  }

  @Test
  public void test() {
    try {
      execute();
    } catch (Throwable t) {
      commentTime("exception occured");
      screenShot("screen shot");
      printCommand(Command.Exception, null, t.getClass().getSimpleName(), t.getMessage());
      t.printStackTrace(System.err);
      throw getSeleniumRuntimeException(t);
    }
  }

  @After
  public void after() {
    if(driver != null){
      driver.quit();
    }
  }

  private SeleniumRuntimeException getSeleniumRuntimeException(Throwable t) {
    if (t instanceof SeleniumRuntimeException) {
      return (SeleniumRuntimeException) t;
    } else {
      return new SeleniumRuntimeException(t);
    }
  }

  public abstract void execute();

  /**
   * ブラウザタイプを返す。
   *
   * @return
   */
  public Browser getBrowser() {
    return browser;
  }

  /**
   * ブラウザ情報を返す。
   *
   * @return
   */
  public Capabilities getCapabilities() {
    return capabilities;
  }

  /**
   * javascriptを実行する。
   *
   * @param command
   * @return
   */
  public Object javascript(String command) {
    try {
      //System.out.println("debug:javascript1");
      return ((JavascriptExecutor) driver).executeScript(command);
    } catch (Exception e) {
      throw new SeleniumRuntimeException(e);
    }
  }

  /**
   * タイムアウトを設定する。
   *
   * @param ms
   */
  public void setTimeout(long ms) {
    timeout = ms;
    printCommand(Command.Done, "setTimeout", Long.toString(timeout));
  }

  /**
   * タイムアウトを返す。
   *
   * @return
   */
  public long getTimeout() {
    return timeout;
  }

  /**
   * スピードを設定する。(各処理の間に挟むスリープ時間(ms))
   *
   * @param ms
   */
  public void setSpeed(long ms) {
    speed = ms;
    printCommand(Command.Done, "setSpeed", Long.toString(speed));
  }

  /**
   * スピードを返す。(各処理の間に挟むスリープ時間(ms))
   */
  public long getSpeed() {
    return speed;
  }

  private void pause() {
    pause(speed, false);
  }

  /**
   * 指定時間の間、処理を止める。(ログ出力あり)
   *
   * @param ms
   */
  public void pause(long ms) {
    pause(ms, true);
  }

  /**
   * 指定時間の間、処理を止める。(ログ出力指定)
   *
   * @param ms
   * @param log
   */
  public void pause(long ms, boolean log) {
    try {
      //long current = System.currentTimeMillis();
      //System.out.println("DEBUG:sleep");
      Thread.sleep(ms);
      //System.out.println("DEBUG:sleep:" + (System.currentTimeMillis() - current));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (log) {
      printCommand(Command.Done, "pause", Long.toString(ms));
    }
  }

  /**
   * 指定のURLを表示する。
   *
   * @param url
   */
  public void open(String url) {
    pause();
    driver.get(url);
    printCommand(Command.Done, "open", url);
  }

  /**
   * ブラウザの戻るボタン
   */
  public void goBack() {
    driver.navigate().back();
    printCommand(Command.Done, "goBack", null);
  }

  /**
   * ブラウザの進むボタン
   */
  public void goForward() {
    driver.navigate().forward();
    printCommand(Command.Done, "goForward", null);
  }

  /**
   * セレクトボックスのオプションを選択する。 オプションの指定方法は以下の通り index=＜0ベースのインデックス＞, label=＜表示文字列＞,
   * value=＜value属性値＞;
   *
   * @param element_locator
   * @param option_locator
   */
  public void select(String element_locator, String option_locator) {
    select(element_locator, option_locator, true);
  }

  /**
   * セレクトボックスのオプションを選択する。 オプションの指定方法は以下の通り index=＜0ベースのインデックス＞, label=＜表示文字列＞,
   * value=＜value属性値＞;
   *
   * @param element_locator
   * @param option_locator
   * @param wait
   */
  public void select(String element_locator, String option_locator, boolean wait) {
    if (wait) {
      waitForElementPresent(element_locator);
    }
    pause();
    SelectOptionKey key = null;
    String value = null;
    for (SelectOptionKey temp_key : SelectOptionKey.values()) {
      if (option_locator.toLowerCase().startsWith(temp_key.toString() + "=")) {
        key = temp_key;
        value = option_locator.substring(temp_key.toString().length() + 1);
        break;
      }
    }
    if (key == null) {
      key = SelectOptionKey.label;
      value = option_locator;
    }
    //index, label, value;
    Target target = Target.locator(element_locator);
    Select select = new Select(findElement(target));
    switch (key) {
      case index:
        int index;
        try {
          index = Integer.parseInt(value);
        } catch (NumberFormatException e) {
          printCommand(Command.Failed, "select", target.toString(), "オプションの指定方法が不正です。(" + option_locator + ")");
          throw new SeleniumRuntimeException(e);
        }
        select.selectByIndex(index);
        break;
      case label:
        select.selectByVisibleText(value);
        break;
      case value:
        select.selectByValue(value);
        break;

    }
    printCommand(Command.Done, "select", target.toString(), option_locator);

  }

  /**
   * 複数選択可能なセレクトボックスのオプションの選択を全解除する。
   *
   * @param locator
   */
  public void deselectAll(String locator) {
    deselectAll(locator, true);
  }

  /**
   * 複数選択可能なセレクトボックスのオプションの選択を全解除する。
   *
   * @param locator
   * @param wait
   */
  public void deselectAll(String locator, boolean wait) {
    if (wait) {
      waitForElementPresent(locator);
    }
    pause();
    Target target = Target.locator(locator);
    Select select = new Select(findElement(target));
    if (select.isMultiple()) {
      select.deselectAll();
      printCommand(Command.Done, "deselectAll", target.toString());
    } else {
      printCommand(Command.Done, "deselectAll", target.toString(), "複数選択でないためスキップします。");
    }
  }

  /**
   * キーボードの特定のキーを押す。
   *
   * @param locator
   * @param keys
   */
  public void keyUp(String locator, Keys keys) {
    pause();
    Target target = Target.locator(locator);
    new Actions(driver).keyUp(findElement(target), keys).perform();
    printCommand(Command.Done, "keyUp", target.toString(), keys.name());
  }

  /**
   * キーボードの特定のキーを離す。
   *
   * @param locator
   * @param keys
   */
  public void keyDown(String locator, Keys keys) {
    pause();
    Target target = Target.locator(locator);
    new Actions(driver).keyDown(findElement(target), keys).perform();
    printCommand(Command.Done, "keyDown", target.toString(), keys.name());
  }

  /**
   * ドラッグアンドドロップをする。
   *
   * @param locator_source ドラッグ元要素
   * @param locator_target ドラッグ先要素
   * @param offset_x ドラッグ先の横軸オフセット
   * @param offset_y ドラッグ先の縦軸オフセット
   * @param wait
   */
  public void dragAndDrop(String locator_source, String locator_target, int offset_x, int offset_y, boolean wait) {
    boolean no_target = locator_target == null;
    boolean no_offset = offset_x == 0 && offset_y == 0;
    if (no_target && no_offset) {
      throw new SeleniumRuntimeException("ドラッグ・アンド・ドロップの指定が不正です。");
    }
    if (wait) {
      waitForElementPresent(locator_source);
      if (!no_target) {
        waitForElementPresent(locator_target);
      }
    }
    pause();
    Target source = Target.locator(locator_source);
    Target target = no_target ? null : Target.locator(locator_target);
    String offset_str = no_offset ? "" : "offset(" + offset_x + ", " + offset_y + ")";
    String param2_str = no_offset ? target.toString() : no_target ? offset_str : target.toString() + " & " + offset_str;
    try {
      WebElement source_element = findElement(source);
      Point source_point = source_element.getLocation();
      Point target_point = no_target ? null : findElement(target).getLocation();
      int total_offset_x = (no_target ? 0 : target_point.getX() - source_point.getX()) + offset_x;
      int total_offset_y = (no_target ? 0 : target_point.getY() - source_point.getY()) + offset_y;
      new Actions(driver).dragAndDropBy(source_element, total_offset_x, total_offset_y).perform();
      printCommand(Command.Done, "dragAndDrop", target.toString(), param2_str);
    } catch (Exception e) {
      printCommand(Command.Failed, "dragAndDrop", target.toString(), param2_str);
      throw getSeleniumRuntimeException(e);
    }
  }

  /**
   * マウスオーバーをする。
   *
   * @param locator
   * @param wait
   */
  public void mouseOver(String locator, boolean wait) {
    if (wait) {
      waitForElementPresent(locator);
    }
    pause();
    Target target = Target.locator(locator);
    try {
      new Actions(driver).moveToElement(findElement(target)).perform();
      printCommand(Command.Done, "mouseOver", target.toString());
    } catch (Exception e) {
      printCommand(Command.Failed, "mouseOver", target.toString());
      throw getSeleniumRuntimeException(e);
    }
  }

  /**
   * 入力フィールドをクリアする。
   *
   * @param locator
   */
  public void clear(String locator) {
    abstractClearAndSendKeys(locator, 0, null, true, true, false);
  }

  /**
   * 入力フィールドをクリアする。
   *
   * @param locator
   * @param wait
   */
  public void clear(String locator, boolean wait) {
    abstractClearAndSendKeys(locator, 0, null, wait, true, false);
  }

  /**
   * 入力フィールドに文字列を打ち込む。（アペンド）
   *
   * @param locator
   * @param value
   */
  public void sendKeys(String locator, String value) {
    abstractClearAndSendKeys(locator, 0, value, true, false, true);
  }

  /**
   * 入力フィールドに文字列を打ち込む。（アペンド）
   *
   * @param locator
   * @param value
   * @param wait
   */
  public void sendKeys(String locator, String value, boolean wait) {
    abstractClearAndSendKeys(locator, 0, value, wait, false, true);
  }

  /**
   * 入力フィールドをクリアして文字列を打ち込む。
   *
   * @param locator
   * @param value
   */
  public void clearAndSendKeys(String locator, String value) {
    abstractClearAndSendKeys(locator, 0, value, true, true, true);
  }

  /**
   * 入力フィールドをクリアして文字列を打ち込む。
   *
   * @param locator
   * @param value
   * @param wait
   */
  public void clearAndSendKeys(String locator, String value, boolean wait) {
    abstractClearAndSendKeys(locator, 0, value, wait, true, true);
  }

  private void abstractClearAndSendKeys(String locator, int index, String value, boolean wait, boolean clear, boolean send_keys) {
    if (wait) {
      waitForElementPresent(locator, index);
    }
    pause();
    Target target = Target.locator(locator, index);
    if (!clear && !send_keys) {
      throw new SeleniumRuntimeException("clearかsend_keysのどちらか一つは必ず指定してください。");
    }
    String method = clear ? send_keys ? "clearAndSendKeys" : "clear" : "sendKeys";
    WebElement element = findElement(target);
    if (clear) {
      element.clear();
    }
    if (send_keys) {
      element.sendKeys(value);
      printCommand(Command.Done, method, target.toString(), value);
    } else {
      printCommand(Command.Done, method, target.toString());
    }
  }

  /**
   * タイプする。（javascript利用により画面から入力困難な文字を入力可能、ただしsendKeysのほうが実動作に近い）
   *
   * @param locator
   * @param value
   */
  public void type(String locator, String value) {
    abstractType(locator, 0, value, true, false);
  }

  /**
   * タイプする。（javascript利用により画面から入力困難な文字を入力可能、ただしsendKeysのほうが実動作に近い）
   *
   * @param locator
   * @param index
   * @param value
   */
  public void type(String locator, int index, String value) {
    abstractType(locator, index, value, true, false);
  }

  /**
   * タイプする。（javascript利用により画面から入力困難な文字を入力可能、ただしsendKeysのほうが実動作に近い）
   *
   * @param locator
   * @param value
   * @param wait
   */
  public void type(String locator, String value, boolean wait) {
    abstractType(locator, 0, value, wait, false);
  }

  /**
   * タイプする。（javascript利用により画面から入力困難な文字を入力可能、ただしsendKeysのほうが実動作に近い）
   *
   * @param locator
   * @param index
   * @param value
   * @param wait
   */
  public void type(String locator, int index, String value, boolean wait) {
    abstractType(locator, index, value, wait, false);
  }

  private void abstractType(String locator, int index, String value, boolean wait, boolean hide_value) {
    if (wait) {
      //System.out.println("debug:type:1");
      waitForElementPresent(locator, index);
      //System.out.println("debug:type:2");

    }
    pause();
    Target target = Target.locator(locator, index);
    String escaped_value = value.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"").replaceAll("'", "\\\\'").replaceAll("/", "\\\\/");
    String script = null;
    //System.out.println("debug:type:3");
    try {
      script = "return " + target.getJavaScriptNodeExpression() + ".value='" + escaped_value + "'";
      javascript(script);
      printCommand(Command.Done, "type", target.toString(), hide_value ? "***" : value);
      //System.out.println("debug:type:4");
    } catch (Exception e) {
      printCommand(Command.Failed, "type", script == null ? target.toString() : script, hide_value ? "***" : value);
      throw getSeleniumRuntimeException(e);
    }

  }

  /**
   * 指定の要素の属性値を設定する。
   *
   * @param locator
   * @param attribute_name
   * @param value
   */
  public void setAttribute(String locator, String attribute_name, String value) {
    setAttribute(locator, attribute_name, value, true);
  }

  /**
   * 指定の要素の属性値を設定する。
   *
   * @param locator
   * @param attribute_name
   * @param value
   * @param wait
   */
  public void setAttribute(String locator, String attribute_name, String value, boolean wait) {
    if (wait) {
      waitForElementPresent(locator);
    }
    pause();
    Target target = Target.locator(locator);
    String script = null;
    try {
      script = "return " + target.getJavaScriptNodeExpression() + ".setAttribute('" + attribute_name + "', '" + value + "')";
      javascript(script);
      printCommand(Command.Done, "setAttribute", target.toString() + "@" + attribute_name, value);
    } catch (Exception e) {
      printCommand(Command.Failed, "setAttribute", script == null ? target.toString() + "@" + attribute_name : script, value);
      throw getSeleniumRuntimeException(e);
    }
  }

  /**
   * 指定の要素の属性を削除する。
   *
   * @param locator
   * @param attribute_name
   */
  public void removeAttribute(String locator, String attribute_name) {
    removeAttribute(locator, attribute_name, true);
  }

  /**
   * 指定の要素の属性を削除する。
   *
   * @param locator
   * @param attribute_name
   * @param wait
   */
  public void removeAttribute(String locator, String attribute_name, boolean wait) {
    if (wait) {
      waitForElementPresent(locator);
    }
    pause();
    Target target = Target.locator(locator);
    String script = null;
    try {
      script = "return " + target.getJavaScriptNodeExpression() + ".removeAttribute('" + attribute_name + "')";
      javascript(script);
      printCommand(Command.Done, "setAttribute", target.toString() + "@" + attribute_name);
    } catch (Exception e) {
      printCommand(Command.Failed, "setAttribute", script == null ? target.toString() + "@" + attribute_name : script);
      throw getSeleniumRuntimeException(e);
    }
  }

  /**
   * 指定の要素を削除する。
   *
   * @param locator
   */
  public void removeElement(String locator) {
    removeElement(locator, true);
  }

  /**
   * 指定の要素を削除する。
   *
   * @param locator
   * @param wait
   */
  public void removeElement(String locator, boolean wait) {
    if (wait) {
      waitForElementPresent(locator);
    }
    pause();
    Target target = Target.locator(locator);
    String script = null;
    try {
      script = "var delNode = " + target.getJavaScriptNodeExpression() + ";return delNode.parentNode.removeChild(delNode)";
      javascript(script);
      printCommand(Command.Done, "removeElement", target.toString());
    } catch (Exception e) {
      printCommand(Command.Failed, "removeElement", script == null ? target.toString() : script);
      throw getSeleniumRuntimeException(e);
    }
  }

  /**
   * 要素を作成する。
   *
   * @param locator
   * @param tag_name
   * @param id
   */
  public void createElement(String locator, String tag_name, String id) {
    createElement(locator, tag_name, id, true);
  }

  /**
   * 要素を作成する。
   *
   * @param locator
   * @param tag_name
   * @param id
   * @param wait
   */
  public void createElement(String locator, String tag_name, String id, boolean wait) {
    if (wait) {
      waitForElementPresent(locator);
    }
    pause();
    Target target = Target.locator(locator);
    String script = null;
    try {
      script = "var new_element = document.createElement('" + tag_name + "');new_element.id = '" + id + "';"
        + "var appendNode = " + target.getJavaScriptNodeExpression() + ";return appendNode.appendChild(new_element)";
      javascript(script);
      printCommand(Command.Done, "createElement", target.toString() + "/" + tag_name + "@id=" + id);
    } catch (Exception e) {
      printCommand(Command.Failed, "createElement", target.toString() + "/" + tag_name + "@id=" + id);
      throw getSeleniumRuntimeException(e);
    }
  }

  /**
   * 要素をクリックする。
   *
   * @param locator
   */
  public void click(String locator) {
    clickCheckUncheck(null, locator, 0, true);
  }

  /**
   * 要素をクリックする。
   *
   * @param locator
   * @param index
   */
  public void click(String locator, int index) {
    clickCheckUncheck(null, locator, index, true);
  }

  /**
   * 要素をクリックする。
   *
   * @param locator
   * @param wait
   */
  public void click(String locator, boolean wait) {
    clickCheckUncheck(null, locator, 0, wait);
  }

  /**
   * 要素にチェックを入れる。（チェックボックス、ラジオボタン対象）
   *
   * @param locator
   */
  public void check(String locator) {
    clickCheckUncheck(Boolean.TRUE, locator, 0, true);
  }

  /**
   * 要素にチェックを入れる。（チェックボックス、ラジオボタン対象）
   *
   * @param locator
   * @param index
   */
  public void check(String locator, int index) {
    clickCheckUncheck(Boolean.TRUE, locator, index, true);
  }

  /**
   * 要素にチェックを入れる。（チェックボックス、ラジオボタン対象）
   *
   * @param locator
   * @param wait
   */
  public void check(String locator, boolean wait) {
    clickCheckUncheck(Boolean.TRUE, locator, 0, wait);
  }

  /**
   * 要素のチェックをはずす。（チェックボックス、ラジオボタン対象）
   *
   * @param locator
   */
  public void uncheck(String locator) {
    clickCheckUncheck(Boolean.FALSE, locator, 0, true);
  }

  /**
   * 要素のチェックをはずす。（チェックボックス、ラジオボタン対象）
   *
   * @param locator
   * @param index
   */
  public void uncheck(String locator, int index) {
    clickCheckUncheck(Boolean.FALSE, locator, index, true);
  }

  /**
   * 要素のチェックをはずす。（チェックボックス、ラジオボタン対象）
   *
   * @param locator
   * @param wait
   */
  public void uncheck(String locator, boolean wait) {
    clickCheckUncheck(Boolean.FALSE, locator, 0, wait);
  }

  private void clickCheckUncheck(Boolean check, String locator, int index, boolean wait) {
    String method = check == null ? "click" : check.equals(Boolean.TRUE) ? "check" : "uncheck";
    if (wait) {
      waitForElementPresent(locator, index);
    }
    pause();
    Target target = Target.locator(locator, index);
    WebElement element = findElement(target);
    if (check != null) {
      try {
        element.isSelected();
      } catch (UnsupportedOperationException e) {
        throw new SeleniumRuntimeException("この要素はチェック対象ではありません。(" + locator + ")");
      }
    }
    if (check == null || (check && !element.isSelected())) {
      if (wait) {
        waitForEnabled(locator, index);
      }
      findElement(target).click();
      printCommand(Command.Done, method, target.toString());
    } else if (!check && element.isSelected()) {
      if (wait) {
        waitForEnabled(locator, index);
      }
      if (element.getAttribute("type").equalsIgnoreCase("radio")) {
        String script = null;
        try {
          script = "return " + target.getJavaScriptNodeExpression() + ".checked=false";
          javascript(script);
        } catch (Exception e) {
          printCommand(Command.Failed, method, script == null ? target.toString() : script);
          throw getSeleniumRuntimeException(e);
        }
      } else {
        findElement(target).click();
      }
      printCommand(Command.Done, method, target.toString());
    }
  }

  /**
   * 指定のタイトルを待つ。
   *
   * @param regex
   * @return
   */
  public boolean waitForTitle(String regex) {
    return doWaitFor(new Title(driver, "waitForTitle", regex));
  }

  /**
   * 指定のタイトルか確認する。
   *
   * @param regex
   * @return
   */
  public boolean verifyTitle(String regex) {
    return verifyTitle(regex, true);
  }

  /**
   * 指定のタイトルか確認する。
   *
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyTitle(String regex, boolean wait) {
    if (wait) {
      waitForTitle(regex);
    }
    return doVerify(new Title(driver, "verifyTitle", regex));
  }

  /**
   * 指定のURLを待つ。
   *
   * @param regex
   * @return
   */
  public boolean waitForCurrentUrl(String regex) {
    return doWaitFor(new CurrentUrl(driver, "waitForCurrentUrl", regex));
  }

  /**
   * 指定のURLか確認する。
   *
   * @param regex
   * @return
   */
  public boolean verifyCurrentUrl(String regex) {
    return verifyCurrentUrl(regex, true);
  }

  /**
   * 指定のURLか確認する。
   *
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyCurrentUrl(String regex, boolean wait) {
    if (wait) {
      waitForCurrentUrl(regex);
    }
    return doVerify(new CurrentUrl(driver, "verifyCurrentUrl", regex));
  }

  /**
   * 指定の要素が指定サイズになるまで待つ。
   *
   * @param locator
   * @param size
   * @return
   */
  public boolean waitForElementSize(String locator, int size) {
    return doWaitFor(new ElementSize(driver, "waitForElementSize", Target.locator(locator), size));
  }

  /**
   * 指定の要素が指定サイズか確認する。
   *
   * @param locator
   * @param size
   * @return
   */
  public boolean verifyElementSize(String locator, int size) {
    return verifyElementSize(locator, size, true);
  }

  /**
   * 指定の要素が指定サイズか確認する。
   *
   * @param locator
   * @param size
   * @param wait
   * @return
   */
  public boolean verifyElementSize(String locator, int size, boolean wait) {
    if (wait) {
      waitForElementSize(locator, size);
    }
    return doVerify(new ElementSize(driver, "verifyElementSize", Target.locator(locator), size));
  }

  /**
   * 指定の要素が操作可能になるまで待つ。
   *
   * @param locator
   * @return
   */
  public boolean waitForEnabled(String locator) {
    return waitForEnabled(locator, 0);
  }

  public boolean waitForEnabled(String locator, int index) {
    return doWaitFor(new Enabled(driver, "waitForEnabled", Target.locator(locator, index)));
  }

  /**
   * 指定の要素が操作可能か確認する。
   *
   * @param locator
   * @return
   */
  public boolean verifyEnabled(String locator) {
    return verifyEnabled(locator, 0, true);
  }

  /**
   * 指定の要素が操作可能か確認する。
   *
   * @param locator
   * @param wait
   * @return
   */
  public boolean verifyEnabled(String locator, boolean wait) {
    return verifyEnabled(locator, 0, wait);
  }

  /**
   * 指定の要素が操作可能か確認する。
   *
   * @param locator
   * @param index
   * @param wait
   * @return
   */
  public boolean verifyEnabled(String locator, int index, boolean wait) {
    if (wait) {
      waitForEnabled(locator, index);
    }
    return doVerify(new Enabled(driver, "verifyEnabled", Target.locator(locator, index)));
  }

  /**
   * 指定の要素が操作不可能になるまで待つ。
   *
   * @param locator
   * @return
   */
  public boolean waitForNotEnabled(String locator) {
    return doWaitFor(new Not(new Enabled(driver, "waitForNotEnabled", Target.locator(locator))));
  }

  /**
   * 指定の要素が操作不可能か確認する。
   *
   * @param locator
   * @return
   */
  public boolean verifyNotEnabled(String locator) {
    return verifyNotEnabled(locator, true);
  }

  /**
   * 指定の要素が操作不可能か確認する。
   *
   * @param locator
   * @param wait
   * @return
   */
  public boolean verifyNotEnabled(String locator, boolean wait) {
    if (wait) {
      waitForNotEnabled(locator);
    }
    return doVerify(new Not(new Enabled(driver, "verifyNotEnabled", Target.locator(locator))));
  }

  /**
   * 指定の要素が選択されるまで待つ。
   *
   * @param locator
   * @return
   */
  public boolean waitForSelected(String locator) {
    return doWaitFor(new Selected(driver, "waitForSelected", Target.locator(locator)));
  }

  /**
   * 指定の要素が選択されているか確認する。
   *
   * @param locator
   * @return
   */
  public boolean verifySelected(String locator) {
    return verifySelected(locator, true);
  }

  /**
   * 指定の要素が選択されているか確認する。
   *
   * @param locator
   * @param wait
   * @return
   */
  public boolean verifySelected(String locator, boolean wait) {
    if (wait) {
      waitForSelected(locator);
    }
    return doVerify(new Selected(driver, "verifySelected", Target.locator(locator)));
  }

  /**
   * 指定の要素が未選択になるまで待つ。
   *
   * @param locator
   * @return
   */
  public boolean waitForNotSelected(String locator) {
    return doWaitFor(new Not(new Selected(driver, "waitForNotSelected", Target.locator(locator))));
  }

  /**
   * 指定の要素が未選択であるか確認する。
   *
   * @param locator
   * @return
   */
  public boolean verifyNotSelected(String locator) {
    return verifyNotSelected(locator, true);
  }

  /**
   * 指定の要素が未選択であるか確認する。
   *
   * @param locator
   * @param wait
   * @return
   */
  public boolean verifyNotSelected(String locator, boolean wait) {
    if (wait) {
      waitForNotSelected(locator);
    }
    return doVerify(new Not(new Selected(driver, "verifyNotSelected", Target.locator(locator))));
  }

  /**
   * 指定の要素が存在するまで待つ。
   *
   * @param locator
   * @return
   */
  public boolean waitForElementPresent(String locator) {
    return waitForElementPresent(locator, 0);
  }

  /**
   * 指定の要素が存在するまで待つ。
   *
   * @param locator
   * @param index
   * @return
   */
  public boolean waitForElementPresent(String locator, int index) {
    return doWaitFor(new ElementPresent(driver, "waitForElementPresent", Target.locator(locator, index)));
  }

  /**
   * 指定の要素が存在するか確認する。
   *
   * @param locator
   * @return
   */
  public boolean verifyElementPresent(String locator) {
    return verifyElementPresent(locator, 0, true);
  }

  /**
   * 指定の要素が存在するか確認する。
   *
   * @param locator
   * @param index
   * @return
   */
  public boolean verifyElementPresent(String locator, int index) {
    return verifyElementPresent(locator, index, true);
  }

  /**
   * 指定の要素が存在するか確認する。
   *
   * @param locator
   * @param wait
   * @return
   */
  public boolean verifyElementPresent(String locator, boolean wait) {
    return verifyElementPresent(locator, 0, wait);
  }

  /**
   * 指定の要素が存在するか確認する。
   *
   * @param locator
   * @param index
   * @param wait
   * @return
   */
  public boolean verifyElementPresent(String locator, int index, boolean wait) {
    if (wait) {
      waitForElementPresent(locator, index);
    }
    return doVerify(new ElementPresent(driver, "verifyElementPresent", Target.locator(locator, index)));
  }

  /**
   * 指定の要素が存在しなくなるまで待つ。
   *
   * @param locator
   * @return
   */
  public boolean waitForElementNotPresent(String locator) {
    return doWaitFor(new Not(new ElementPresent(driver, "waitForElementNotPresent", Target.locator(locator))));
  }

  /**
   * 指定の要素が存在しないことを確認する。
   *
   * @param locator
   * @return
   */
  public boolean verifyElementNotPresent(String locator) {
    return verifyElementNotPresent(locator, true);
  }

  /**
   * 指定の要素が存在しないことを確認する。
   *
   * @param locator
   * @param wait
   * @return
   */
  public boolean verifyElementNotPresent(String locator, boolean wait) {
    if (wait) {
      waitForElementNotPresent(locator);
    }
    return doVerify(new Not(new ElementPresent(driver, "verifyElementNotPresent", Target.locator(locator))));
  }

  /**
   * 指定の要素のテキストが指定通りになるまで待つ。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean waitForText(String locator, String regex) {
    return waitForText(locator, 0, regex);
  }

  /**
   * 指定の要素のテキストが指定通りになるまで待つ。
   *
   * @param locator
   * @param index
   * @param regex
   * @return
   */
  public boolean waitForText(String locator, int index, String regex) {
    return doWaitFor(new Text(driver, "waitForText", Target.locator(locator, index), regex));
  }

  /**
   * 指定の要素のテキストが指定通りであるか確認する。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean verifyText(String locator, String regex) {
    return verifyText(locator, 0, regex, true);
  }

  /**
   * 指定の要素のテキストが指定通りであるか確認する。
   *
   * @param locator
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyText(String locator, String regex, boolean wait) {
    return verifyText(locator, 0, regex, wait);
  }

  /**
   * 指定の要素のテキストが指定通りであるか確認する。
   *
   * @param locator
   * @param index
   * @param regex
   * @return
   */
  public boolean verifyText(String locator, int index, String regex) {
    return verifyText(locator, index, regex, true);
  }

  /**
   * 指定の要素のテキストが指定通りであるか確認する。
   *
   * @param locator
   * @param index
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyText(String locator, int index, String regex, boolean wait) {
    if (wait) {
      waitForText(locator, index, regex);
    }
    return doVerify(new Text(driver, "verifyText", Target.locator(locator, index), regex));
  }

  /**
   * 指定の要素のテキストが指定外になるまで待つ。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean waitForNotText(String locator, String regex) {
    return waitForNotText(locator, 0, regex);
  }

  /**
   * 指定の要素のテキストが指定外になるまで待つ。
   *
   * @param locator
   * @param index
   * @param regex
   * @return
   */
  public boolean waitForNotText(String locator, int index, String regex) {
    return doWaitFor(new Not(new Text(driver, "waitForNotText", Target.locator(locator, index), regex)));
  }

  /**
   * 指定の要素のテキストが指定外であるか確認する。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean verifyNotText(String locator, String regex) {
    return verifyNotText(locator, 0, regex, true);
  }

  /**
   * 指定の要素のテキストが指定外であるか確認する。
   *
   * @param locator
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyNotText(String locator, String regex, boolean wait) {
    return verifyNotText(locator, 0, regex, wait);
  }

  /**
   * 指定の要素のテキストが指定外であるか確認する。
   *
   * @param locator
   * @param index
   * @param regex
   * @return
   */
  public boolean verifyNotText(String locator, int index, String regex) {
    return verifyNotText(locator, index, regex, true);
  }

  /**
   * 指定の要素のテキストが指定外であるか確認する。
   *
   * @param locator
   * @param index
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyNotText(String locator, int index, String regex, boolean wait) {
    if (wait) {
      waitForNotText(locator, index, regex);
    }
    return doVerify(new Not(new Text(driver, "verifyNotText", Target.locator(locator, index), regex)));
  }

  /**
   * 指定の要素の属性値が指定通りになるまで待つ。
   *
   * @param locator
   * @param attribute_name
   * @param regex
   * @return
   */
  public boolean waitForAttribute(String locator, String attribute_name, String regex) {
    return waitForAttribute(locator, 0, attribute_name, regex);
  }

  /**
   * 指定の要素の属性値が指定通りになるまで待つ。
   *
   * @param locator
   * @param index
   * @param attribute_name
   * @param regex
   * @return
   */
  public boolean waitForAttribute(String locator, int index, String attribute_name, String regex) {
    return doWaitFor(new Attribute(driver, "waitForAttribute", Target.locator(locator, index), attribute_name, regex));
  }

  /**
   * 指定の要素の属性値が指定通りであるか確認する。
   *
   * @param locator
   * @param attribute_name
   * @param regex
   * @return
   */
  public boolean verifyAttribute(String locator, String attribute_name, String regex) {
    return verifyAttribute(locator, 0, attribute_name, regex, true);
  }

  /**
   * 指定の要素の属性値が指定通りであるか確認する。
   *
   * @param locator
   * @param attribute_name
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyAttribute(String locator, String attribute_name, String regex, boolean wait) {
    return verifyAttribute(locator, 0, attribute_name, regex, wait);
  }

  /**
   * 指定の要素の属性値が指定通りであるか確認する。
   *
   * @param locator
   * @param index
   * @param attribute_name
   * @param regex
   * @return
   */
  public boolean verifyAttribute(String locator, int index, String attribute_name, String regex) {
    return verifyAttribute(locator, index, attribute_name, regex, true);
  }

  /**
   * 指定の要素の属性値が指定通りであるか確認する。
   *
   * @param locator
   * @param index
   * @param attribute_name
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyAttribute(String locator, int index, String attribute_name, String regex, boolean wait) {
    if (wait) {
      waitForAttribute(locator, index, attribute_name, regex);
    }
    return doVerify(new Attribute(driver, "verifyAttribute", Target.locator(locator, index), attribute_name, regex));
  }

  /**
   * 指定の要素の属性値が指定外になるまで待つ。
   *
   * @param locator
   * @param attribute_name
   * @param regex
   * @return
   */
  public boolean waitForNotAttribute(String locator, String attribute_name, String regex) {
    return waitForNotAttribute(locator, 0, attribute_name, regex);
  }

  /**
   * 指定の要素の属性値が指定外になるまで待つ。
   *
   * @param locator
   * @param index
   * @param attribute_name
   * @param regex
   * @return
   */
  public boolean waitForNotAttribute(String locator, int index, String attribute_name, String regex) {
    return doWaitFor(new Not(new Attribute(driver, "waitForNotAttribute", Target.locator(locator, index), attribute_name, regex)));
  }

  /**
   * 指定の要素の属性値が指定外であるか確認する。
   *
   * @param locator
   * @param attribute_name
   * @param regex
   * @return
   */
  public boolean verifyNotAttribute(String locator, String attribute_name, String regex) {
    return verifyNotAttribute(locator, 0, attribute_name, regex, true);
  }

  /**
   * 指定の要素の属性値が指定外であるか確認する。
   *
   * @param locator
   * @param attribute_name
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyNotAttribute(String locator, String attribute_name, String regex, boolean wait) {
    return verifyNotAttribute(locator, 0, attribute_name, regex, wait);
  }

  /**
   * 指定の要素の属性値が指定外であるか確認する。
   *
   * @param locator
   * @param index
   * @param attribute_name
   * @param regex
   * @return
   */
  public boolean verifyNotAttribute(String locator, int index, String attribute_name, String regex) {
    return verifyNotAttribute(locator, index, attribute_name, regex, true);
  }

  /**
   * 指定の要素の属性値が指定外であるか確認する。
   *
   * @param locator
   * @param index
   * @param attribute_name
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyNotAttribute(String locator, int index, String attribute_name, String regex, boolean wait) {
    if (wait) {
      waitForNotAttribute(locator, index, attribute_name, regex);
    }
    return doVerify(new Not(new Attribute(driver, "verifyNotAttribute", Target.locator(locator, index), attribute_name, regex)));
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定通りになるまで待つ。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean waitForValue(String locator, String regex) {
    return waitForValue(locator, 0, regex);
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定通りになるまで待つ。
   *
   * @param locator
   * @param index
   * @param regex
   * @return
   */
  public boolean waitForValue(String locator, int index, String regex) {
    return doWaitFor(new Attribute(driver, "waitForValue", Target.locator(locator, index), "value", regex));
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定通りか確認する。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean verifyValue(String locator, String regex) {
    return verifyValue(locator, 0, regex, true);
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定通りか確認する。
   *
   * @param locator
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyValue(String locator, String regex, boolean wait) {
    return verifyValue(locator, 0, regex, wait);
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定通りか確認する。
   *
   * @param locator
   * @param index
   * @param regex
   * @return
   */
  public boolean verifyValue(String locator, int index, String regex) {
    return verifyValue(locator, index, regex, true);
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定通りか確認する。
   *
   * @param locator
   * @param index
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyValue(String locator, int index, String regex, boolean wait) {
    if (wait) {
      waitForValue(locator, index, regex);
    }
    return doVerify(new Attribute(driver, "verifyValue", Target.locator(locator, index), "value", regex));
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定外になるまで待つ。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean waitForNotValue(String locator, String regex) {
    return waitForNotValue(locator, 0, regex);
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定外になるまで待つ。
   *
   * @param locator
   * @param index
   * @param regex
   * @return
   */
  public boolean waitForNotValue(String locator, int index, String regex) {
    return doWaitFor(new Not(new Attribute(driver, "waitForNotValue", Target.locator(locator, index), "value", regex)));
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定外か確認する。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean verifyNotValue(String locator, String regex) {
    return verifyNotValue(locator, 0, regex, true);
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定外か確認する。
   *
   * @param locator
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyNotValue(String locator, String regex, boolean wait) {
    return verifyNotValue(locator, 0, regex, wait);
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定外か確認する。
   *
   * @param locator
   * @param index
   * @param regex
   * @return
   */
  public boolean verifyNotValue(String locator, int index, String regex) {
    return verifyNotValue(locator, index, regex, true);
  }

  /**
   * 指定の要素のvalue属性値（入力フィールドの場合は入力値）が指定外か確認する。
   *
   * @param locator
   * @param index
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifyNotValue(String locator, int index, String regex, boolean wait) {
    if (wait) {
      waitForNotValue(locator, index, regex);
    }
    return doVerify(new Not(new Attribute(driver, "verifyNotValue", Target.locator(locator, index), "value", regex)));
  }

  /**
   * 指定セレクトボックスの選択されたオプションのテキストが指定通りになるまで待つ。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean waitForSelectedText(String locator, String regex) {
    return doWaitFor(new SelectedText(driver, "waitForSelectedText", Target.locator(locator), regex));
  }

  /**
   * 指定セレクトボックスの選択されたオプションのテキストが指定通りか確認する。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean verifySelectedText(String locator, String regex) {
    return verifySelectedText(locator, regex, true);
  }

  /**
   * 指定セレクトボックスの選択されたオプションのテキストが指定通りか確認する。
   *
   * @param locator
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifySelectedText(String locator, String regex, boolean wait) {
    if (wait) {
      waitForSelectedText(locator, regex);
    }
    return doVerify(new SelectedText(driver, "verifySelectedText", Target.locator(locator), regex));
  }

  /**
   * 指定セレクトボックスの選択されたオプションのvalue属性値が指定通りになるまで待つ。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean waitForSelectedValue(String locator, String regex) {
    return doWaitFor(new SelectedValue(driver, "waitForSelectedValue", Target.locator(locator), regex));
  }

  /**
   * 指定セレクトボックスの選択されたオプションのvalue属性値が指定通りか確認する。
   *
   * @param locator
   * @param regex
   * @return
   */
  public boolean verifySelectedValue(String locator, String regex) {
    return verifySelectedValue(locator, regex, true);
  }

  /**
   * 指定セレクトボックスの選択されたオプションのvalue属性値が指定通りか確認する。
   *
   * @param locator
   * @param regex
   * @param wait
   * @return
   */
  public boolean verifySelectedValue(String locator, String regex, boolean wait) {
    if (wait) {
      waitForSelectedValue(locator, regex);
    }
    return doVerify(new SelectedValue(driver, "verifySelectedValue", Target.locator(locator), regex));
  }

  private boolean doVerify(Inspectable inspectable) {
    pause();
    boolean result;
    try {
      result = inspectable.inspect();
    } catch (SeleniumRuntimeException e) {
      printCommand(Command.Failed, inspectable.getMethod(), inspectable.getParam1(), inspectable.getParam2());
      throw e;
    }
    printCommand(result ? Command.Success : Command.Failed, inspectable.getMethod(), inspectable.getParam1(), inspectable.getParam2());
    return result;
  }

  private synchronized boolean doWaitFor(final Inspectable inspectable) {
    //pause();
    try {
      /*new Wait() {
                public boolean until() {
                    //System.out.println("DEBUG:");
                    return inspectable.inspect();
                }
            }.wait("Timed out after " + timeout + " ms.", timeout, 1000);
       */
      //printCommand(Command.Done, inspectable.getMethod(), inspectable.getParam1(), inspectable.getParam2());
      return true;
      /*} catch (WaitTimedOutException e) {
            printCommand(Command.Failed, inspectable.getMethod(), inspectable.getParam1(), "タイムアウト(" + timeout + " ms)しました。");
            return false;
        } catch (InvalidElementStateException e) {
            printCommand(Command.Failed, inspectable.getMethod(), inspectable.getParam1(), "waitForに失敗しました。(" + e.getMessage() + ")");
            return false;
            /*} catch (InterruptedException e) {
			printCommand(Command.Failed, inspectable.getMethod(), inspectable.getParam1(), "waitForが中断されました。(" + e.getMessage() + ")");
			return false;*/
    } catch (Exception e) {
      throw getSeleniumRuntimeException(e);
    }

  }

  /**
   * スクリーンショットを取得する。
   *
   * @param comment
   */
  public File screenShot(String comment) {
    if (driver instanceof TakesScreenshot) {
      File temp_file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      File evidence = new File(evidence_dir, "screen_shot_" + (screen_shot_number++) + ".png");
      evidence.delete();
      temp_file.renameTo(evidence);
      printCommand(Command.Done, "screenShot", comment, evidence.getAbsolutePath());
      return evidence;
    } else {
      printCommand(Command.Done, "screenShot", comment, "取得できません。");
      return null;
    }
  }

  /**
   * 現在時刻をログ出力する。
   */
  public void commentTime() {
    commentTime("現在時刻");
  }

  /**
   * 現在時刻をログ出力する。
   *
   * @param comment
   */
  public void commentTime(String comment) {
    comment(comment, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
  }

  /**
   * コメントをログ出力する。
   *
   * @param comment1
   * @param comment2
   */
  public void comment(String comment) {
    printCommand(Command.Comment, null, comment, null);
  }

  /**
   * コメントをログ出力する。
   *
   * @param comment1
   * @param comment2
   */
  public void comment(String comment1, String comment2) {
    printCommand(Command.Comment, null, comment1, comment2);
  }

  private void printCommand(Command command, String method, String param) {
    printCommand(command, method, param, null);
  }

  private void printCommand(Command command, String method, String param1, String param2) {
    String message = "[" + command.getName() + "] " + passNull(method) + "\t" + passNull(param1) + "\t" + passNull(param2);
    if (command == Command.Failed || command == Command.Exception) {
      System.err.println(message);
    } else {
      System.out.println(message);
    }
  }

  private String passNull(String text) {
    return text == null ? "" : text;
  }
}
