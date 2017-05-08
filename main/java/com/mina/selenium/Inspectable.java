package com.mina.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

interface Inspectable {
	boolean inspect();
	String getMethod();
	String getParam1();
	String getParam2();

	abstract class Inspector extends WebDriverHolder implements Inspectable {
		String method;
		String param1;
		String param2;
		Inspector(WebDriver driver, String method, String param1, String param2){
			super(driver);
			this.method = method;
			this.param1 = param1;
			this.param2 = param2;
		}
		public String getMethod(){
			return method;
		}
		public String getParam1(){
			return param1;
		}
		public String getParam2(){
			return param2;
		}
	}
	abstract class OneTargetInspector extends Inspector {
		protected Target target;
		OneTargetInspector(WebDriver driver, String method, Target target, String param){
			this(driver, method, target, target.toString(), param);
		}
		OneTargetInspector(WebDriver driver, String method, Target target, String param1, String param2){
			super(driver, method, param1, param2);
			this.target = target;
		}
	}

	class Not implements Inspectable {
		Inspectable inspectable;
		Not(Inspectable inspectable){
			this.inspectable = inspectable;
		}
		@Override
		public boolean inspect() {
			return !inspectable.inspect();
		}
		@Override
		public String getMethod() {
			return inspectable.getMethod();
		}
		@Override
		public String getParam1() {
			return inspectable.getParam1();
		}
		@Override
		public String getParam2() {
			return inspectable.getParam2();
		}
	}
	class Title extends Inspector {
		String regex;
		Title(WebDriver driver, String method, String regex){
			super(driver, method, regex, null);
			this.regex = regex;
		}
		@Override
		public boolean inspect() {
			String actual = getTitle();
			boolean match = SeleniumUtil.match(regex, actual);
			if(!match){
				param2 = "タイトル(" + actual + ")が想定(" + regex + ")とマッチしませんでした。";
			}
			return match;
		}
	}
	class CurrentUrl extends Inspector {
		String regex;
		CurrentUrl(WebDriver driver, String method, String regex){
			super(driver, method, regex, null);
			this.regex = regex;
		}
		@Override
		public boolean inspect() {
			String actual = getCurrentUrl();
			boolean match = SeleniumUtil.match(regex, actual);
			if(!match){
				param2 = "URL(" + actual + ")が想定(" + regex + ")とマッチしませんでした。";
			}
			return match;
		}
	}
	class ElementSize extends OneTargetInspector {
		int size;
		ElementSize(WebDriver driver, String method, Target target, int size) {
			super(driver, method, target, Integer.toString(size));
			this.size = size;
		}

		@Override
		public boolean inspect() {
			int actual;
			try{
				actual = findElements(target).size();
			}catch(SeleniumRuntimeException e){
				actual = 0;
			}
			boolean match = actual == size;
			if(!match){
				param2 = "要素数(" + actual + ")が想定(" + size + ")と一致しませんでした。";
			}
			return match;
		}

	}
	class Enabled extends OneTargetInspector {
		Enabled(WebDriver driver, String method, Target target) {
			super(driver, method, target, null);
		}

		@Override
		public boolean inspect() {
			try{
				return isEnabled(target);
			}catch(SeleniumRuntimeException e){
				param2 = e.getMessage();
				throw e;
			}
		}
	}

	class Selected extends OneTargetInspector {
		Selected(WebDriver driver, String method, Target target) {
			super(driver, method, target, null);
		}

		@Override
		public boolean inspect() {
			try{
				return isSelected(target);
			}catch(SeleniumRuntimeException e){
				param2 = e.getMessage();
				throw e;
			}
		}
	}
	class ElementPresent extends OneTargetInspector {
		ElementPresent(WebDriver driver, String method, Target target) {
			super(driver, method, target, null);
		}

		@Override
		public boolean inspect() {
			//System.out.println("debug:" + isElementPresent(target));
			return isElementPresent(target);
		}
	}
	class Text extends OneTargetInspector {
		String regex;
		Text(WebDriver driver, String method, Target target, String regex) {
			super(driver, method, target, regex);
			this.regex = regex;
		}

		@Override
		public boolean inspect() {
			String actual;
			try{
				actual = getText(target);
				boolean match = SeleniumUtil.match(regex, actual);
				if(!match){
					param2 = "取得テキスト(" + actual + ")が想定(" + regex + ")とマッチしませんでした。";
				}
				return match;
			}catch(SeleniumRuntimeException e){
				param2 = e.getMessage();
				throw e;
			}
		}
	}
	class Attribute extends OneTargetInspector {
		String regex;
		String attribute_name;
		Attribute(WebDriver driver, String method, Target target, String attribute_name, String regex) {
			super(driver, method, target, target.toString() + "@" + attribute_name, regex);
			this.attribute_name = attribute_name;
			this.regex = regex;
		}

		@Override
		public boolean inspect() {
			String actual;
			try{
				actual = findElement(target).getAttribute(attribute_name);
				boolean match = SeleniumUtil.match(regex, actual);
				if(!match){
					param2 = "属性値(" + actual + ")が想定(" + regex + ")とマッチしませんでした。";
				}
				return match;
			}catch(SeleniumRuntimeException e){
				param2 = e.getMessage();
				throw e;
			}
		}
	}
	abstract class AbstractSelected extends OneTargetInspector {
		private String regex;
		AbstractSelected(WebDriver driver, String method, Target target, String regex){
			super(driver, method, target, regex);
			this.regex = regex;
		}
		abstract String getActual(WebElement element);
		abstract String getInspecteeName();
		@Override
		public boolean inspect() {
			String actual;
			try{
				actual = getActual(getFirstSelectedOption(target));
				boolean match = SeleniumUtil.match(regex, actual);
				if(!match){
					param2 = getInspecteeName() + "(" + actual + ")が想定(" + regex + ")とマッチしませんでした。";
				}
				return match;
			}catch(SeleniumRuntimeException e){
				param2 = e.getMessage();
				throw e;
			}
		}
	}
	class SelectedText extends AbstractSelected {
		SelectedText(WebDriver driver, String method, Target target, String regex){
			super(driver, method, target, regex);
		}

		@Override
		String getActual(WebElement element) {
			return element.getText();
		}

		@Override
		String getInspecteeName() {
			return "選択オプションのテキスト";
		}
	}
	class SelectedValue extends AbstractSelected {
		SelectedValue(WebDriver driver, String method, Target target, String regex){
			super(driver, method, target, regex);
		}

		@Override
		String getActual(WebElement element) {
			return element.getAttribute("value");
		}

		@Override
		String getInspecteeName() {
			return "選択オプションの値";
		}
	}

}
