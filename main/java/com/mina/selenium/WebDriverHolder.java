package com.mina.selenium;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

class WebDriverHolder {

    public WebDriver driver;

    public WebDriverHolder() {
        this(null);
    }

    WebDriverHolder(WebDriver driver) {
        this.driver = driver;
    }

    /**
     *
     * @param locator
     * @return
     */
    public WebElement findElement(String locator) {
        return findElement(Target.locator(locator));
    }
    public WebElement findElement(WebElement element, String locator) {
        return findElement(element, Target.locator(locator));
    }

    /**
     *
     * @param locator
     * @param index
     * @return
     */
    public WebElement findElement(String locator, int index) {
        return findElement(Target.locator(locator, index));
    }

    /**
     *
     * @param target
     * @return
     */
    public WebElement findElement(Target target) {
        List<WebElement> elements = findElements(target);
        int size = elements.size();
        if (size <= target.getIndex()) {
            throw new SeleniumRuntimeException("要素のインデックスが要素数(" + size + ")を超えています。(" + target + ")");
        }
        return elements.get(target.getIndex());
    }
    public WebElement findElement(WebElement element, Target target) {
        List<WebElement> elements = findElements(element, target);
        int size = elements.size();
        if (size <= target.getIndex()) {
            throw new SeleniumRuntimeException("要素のインデックスが要素数(" + size + ")を超えています。(" + target + ")");
        }
        return elements.get(target.getIndex());
    }

    /**
     *
     * @param locator
     * @return
     */
    public List<WebElement> findElements(String locator) {
        return findElements(Target.locator(locator));
    }

    /**
     *
     * @param target
     * @return
     */
    public List<WebElement> findElements(Target target) {
        try {
            List<WebElement> elements = driver.findElements(target.getBy());
            if (elements.isEmpty()) {
                throw new NoSuchElementException("");
            }
            return elements;
        } catch (NoSuchElementException e) {
            throw new SeleniumRuntimeException("要素を特定できませんでした。(" + target + ")");
        }
    }
    public List<WebElement> findElements(WebElement element, String locator) {
        return findElements(element, Target.locator(locator));
    }
    public List<WebElement> findElements(WebElement element, Target target) {
        try {
            List<WebElement> elements = element.findElements(target.getBy());
            if (elements.isEmpty()) {
                throw new NoSuchElementException("");
            }
            return elements;
        } catch (NoSuchElementException e) {
            throw new SeleniumRuntimeException("要素を特定できませんでした。(" + target + ")");
        }
    }

    /**
     * 指定された要素のリストから指定のテキストを持つ要素のインデックスを返す。 どの要素にも該当しない場合は-1を返す。
     *
     * @param locator
     * @param regex
     * @return
     */
    public int getElementIndexByText(String locator, String regex) {
        List<WebElement> elements = findElements(locator);
        for (int i = 0; i < elements.size(); i++) {
            if (SeleniumUtil.match(regex, elements.get(i).getText())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 指定のセレクトボックスの指定のインデックスのオプション要素を返す。
     *
     * @param locator
     * @param index
     * @return
     */
    public WebElement getSelectOption(String locator, int index) {
        List<WebElement> options = new Select(findElement(Target.locator(locator))).getOptions();
        int size = options.size();
        if (size <= index) {
            throw new SeleniumRuntimeException("セレクトオプションのインデックス(" + index + ")が要素数(" + size + ")を超えています。");
        }
        return options.get(index);
    }

    /**
     * セレクトボックスの選択されたオプションを返す。（複数選択可能なセレクトボックスの場合は最初のもの）
     *
     * @param locator
     * @return
     */
    public WebElement getFirstSelectedOption(String locator) {
        return getFirstSelectedOption(Target.locator(locator));
    }

    /**
     * セレクトボックスの選択されたオプションを返す。（複数選択可能なセレクトボックスの場合は最初のもの）
     *
     * @param target
     * @return
     */
    public WebElement getFirstSelectedOption(Target target) {
        return new Select(findElement(target)).getFirstSelectedOption();
    }

    /**
     * 現在のページのタイトル文字列を返す。
     *
     * @return
     */
    public String getTitle() {
        return driver.getTitle();
    }

    /**
     * 現在のURLを返す。
     *
     * @return
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTagName(String locator) {
        return findElement(Target.locator(locator)).getTagName().toLowerCase();
    }

    /**
     * タグで囲まれた文字列を返す。
     *
     * @param locator
     * @return
     */
    public String getText(String locator) {
        return getText(Target.locator(locator));
    }

    /**
     * タグで囲まれた文字列を返す。
     *
     * @param locator
     * @param index
     * @return
     */
    public String getText(String locator, int index) {
        return getText(Target.locator(locator, index));
    }

    /**
     * タグで囲まれた文字列を返す。
     *
     * @param target
     * @return
     */
    public String getText(Target target) {
        return findElement(target).getText();
    }

    /**
     * 入力フィールドの文字列またはvalue属性値を返す。
     *
     * @param locator
     * @return
     */
    public String getValue(String locator) {
        return getValue(Target.locator(locator));
    }

    /**
     * 入力フィールドの文字列またはvalue属性値を返す。
     *
     * @param target
     * @return
     */
    public String getValue(Target target) {
        return getAttribute(target, "value");
    }

    /**
     * 属性値を返す。
     *
     * @param locator
     * @param attribute_name
     * @return
     */
    public String getAttribute(String locator, String attribute_name) {
        return getAttribute(Target.locator(locator), attribute_name);
    }

    /**
     * 属性値を返す。
     *
     * @param target
     * @param attribute_name
     * @return
     */
    public String getAttribute(Target target, String attribute_name) {
        return findElement(target).getAttribute(attribute_name);
    }

    /**
     * 要素が操作可能かを返す。
     *
     * @param locator
     * @return
     */
    public boolean isEnabled(String locator) {
        return isEnabled(Target.locator(locator));
    }

    /**
     * 要素が操作可能かを返す。
     *
     * @param target
     * @return
     */
    public boolean isEnabled(Target target) {
        return findElement(target).isEnabled();
    }

    /**
     * 項目が選択されているかを返す。（チェックボックスやラジオボックスの場合はチェックされているかを返す。）
     *
     * @param locator
     * @return
     */
    public boolean isSelected(String locator) {
        return isSelected(Target.locator(locator));
    }

    /**
     * 項目が選択されているかを返す。（チェックボックスやラジオボックスの場合はチェックされているかを返す。）
     *
     * @param target
     * @return
     */
    public boolean isSelected(Target target) {
        return findElement(target).isSelected();
    }

    /**
     * 要素が存在するかを返す。
     *
     * @param locator
     * @return
     */
    public boolean isElementPresent(String locator) {
        return isElementPresent(Target.locator(locator));
    }

    /**
     * 要素が存在するかを返す。
     *
     * @param target
     * @return
     */
    public boolean isElementPresent(Target target) {
        try {
            findElement(target);
            return true;
        } catch (SeleniumRuntimeException e) {
            return false;
        }
    }
    public boolean isElementPresent(WebElement element, String locator) {
        return isElementPresent(element, Target.locator(locator));
    }
    public boolean isElementPresent(WebElement element, Target target) {
        try {
            findElement(element, target);
            return true;
        } catch (SeleniumRuntimeException e) {
            return false;
        }
    }
 
    /**
     * セレクトボックスのオプションの数を返す。
     *
     * @param locator
     * @return
     */
    public int getSelectOptionSize(String locator) {
        return new Select(findElement(locator)).getOptions().size();
    }

    /**
     * セレクトボックスの指定のオプションのテキストを返す。
     *
     * @param locator
     * @param index
     * @return
     */
    public String getSelectOptionText(String locator, int index) {
        return getSelectOption(locator, index).getText();

    }

    /**
     * セレクトボックスの指定のオプションのvalue属性値を返す。
     *
     * @param locator
     * @param index
     * @return
     */
    public String getSelectOptionValue(String locator, int index) {
        return getSelectOption(locator, index).getAttribute("value");
    }

    /**
     * 指定の要素の数を返す。
     *
     * @param locator
     * @return
     */
    public int getElementSize(String locator) {
        return isElementPresent(locator) ? findElements(locator).size() : 0;
    }

}
