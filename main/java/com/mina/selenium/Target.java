package com.mina.selenium;

import org.openqa.selenium.By;

public class Target {

    private enum LocateKey {
        id, name, xpath, link;

        public String getValue(String locator) {
            if (!locator.toLowerCase().startsWith(toString() + "=")) {
                return null;
            }
            return locator.substring(toString().length() + 1);
        }
        /*ID   ("id"),
		NAME ("name"),
		XPATH("xpath"),
		LINK ("link");

		private String key;
		private LocateKey(String key){
			this.key = key;
		}
		public String getKey(){return key;}
		public String getValue(String locator){
			if(!locator.toLowerCase().startsWith(key + "=")){
				return null;
			}
			return locator.substring(key.length() + 1);
		}*/
    }
    private LocateKey locate_key;
    private String value;
    private By by;
    private int index;

    private Target(LocateKey locate_key, String value, By by, int index) {
        this.locate_key = locate_key;
        this.value = value.replaceAll("'", "\"");
        this.by = by;
        this.index = index;
    }

    public static Target locator(String locator) {
        return locator(locator, 0);
    }

    public static Target locator(String locator, int index) {
        String value;
        if ((value = LocateKey.id.getValue(locator)) != null) {
            return new Target(LocateKey.id, value, By.id(value), index);
        } else if ((value = LocateKey.name.getValue(locator)) != null) {
            return new Target(LocateKey.name, value, By.name(value), index);
        } else if ((value = LocateKey.xpath.getValue(locator)) != null) {
            return new Target(LocateKey.xpath, value, By.xpath(value), index);
        } else if ((value = LocateKey.link.getValue(locator)) != null) {
            return new Target(LocateKey.link, value, By.linkText(value), index);
        } else if (locator.startsWith("/")) {
            return new Target(LocateKey.xpath, locator, By.xpath(locator), index);
        } else {
            //return new Target(LocateKey.id, locator, By.id(locator), index);
            return new Target(LocateKey.xpath, locator, By.xpath(locator), index);
        }
    }

    public int getIndex() {
        return index;
    }

    public String toString() {
        return locate_key.toString() + "=" + value + (index == 0 ? "" : "[" + index + "]");
    }

    public By getBy() {
        return by;
    }

    public String getJavaScriptNodeExpression() {
        switch (locate_key) {
            case id:
                if (index != 0) {
                    throw new SeleniumRuntimeException("ID指定の場合インデックスに対応した項目の取得はできません。");
                }
                return "document.getElementById('" + value + "')";
            case name:
                return "document.getElementByName('" + value + "')[" + index + "]";
            case xpath:
                return "document.evaluate('" + value + "', document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null).snapshotItem(" + index + ")";
            default:
                throw new SeleniumRuntimeException("このロケーターの指定方法ではJavascriptの利用ができません。(" + toString() + ")");
        }
    }
}
