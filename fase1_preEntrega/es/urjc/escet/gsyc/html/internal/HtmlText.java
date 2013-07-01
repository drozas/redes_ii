package es.urjc.escet.gsyc.html.internal;

public class HtmlText extends HtmlObject {

	private String text;
	HtmlText(String text){
		this.text = text;
	}
	public void addText(String text){
		if(text != null)
			this.text = text;
		else
			this.text = "";
	}
	public StringBuilder build(){
		return new StringBuilder(text);
	}
}
