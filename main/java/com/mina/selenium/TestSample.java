package com.mina.selenium;

public class TestSample extends SeleniumWork {

	public TestSample(){
		super(Browser.Firefox, "test_sample");
		//super(Browser.Chrome, "test_sample");
	}
	@Override
	public void execute() {
		comment("yahoo!トップを開く");
		open("http://www.yahoo.co.jp/");


		comment("リンクをクリックする");
		click("link=路線");

		comment("入力フォームに値を設定する。");
		comment("入力：type：半角カナなど特殊な文字を入力する場合に利用する。");
		type("sfrom", "四谷");

		comment("入力：sendKeys：入力フィールドに文字をアペンドする。");
		sendKeys("//input[@id='sfrom']", "三丁目");

		comment("消去：入力文字列を削除する。");
		clear("sfrom");
		comment("入力");
		sendKeys("sfrom", "四谷");

		comment("入力：clearAndSendKeys：入力でjavascriptが動作する画面の場合などに利用する。");
		clearAndSendKeys("//input[@id='sto']", "木場");

		comment("実行間隔を1000ミリ秒にする。");
		setSpeed(1000);

		click("distr-info");

		comment("いったんチェックをはずす。");
		uncheck("sexp");
		uncheck("exp");
		uncheck("air");
		uncheck("hbus");
		uncheck("bus");
		uncheck("fer");

		comment("チェックする。");
		check("sexp");
		check("exp");

		comment("セレクトボックスの表示文字を取得する。");
		int selYear_length = getSelectOptionSize("selYear");
		for(int i = 0;i < selYear_length;i++){
			comment((i + 1) + "番目のオプション", getSelectOptionText("selYear", i));
		}
		String option_text = getSelectOptionText("selYear", 1);
		select("selYear", "index=1");
		comment("現在選択されているオプションの表示文字", getFirstSelectedOption("selYear").getText());
		verifySelectedText("selYear", option_text);

		comment("ラジオボタンの選択");
		check("totime");
		comment("ラジオボタンの非選択");
		uncheck("totime");
		comment("ラジオボタンの選択");
		check("totime");

		click("//input[@value='探索']");

		createElement("//input[@id='sto']/..", "input", "test_input");
		setAttribute("test_input", "type", "text");
		setAttribute("test_input", "name", "test_name");
		setAttribute("test_input", "value", "テストの文字列です。");
		removeElement("test_input");


		pause(20000);

	}

}
