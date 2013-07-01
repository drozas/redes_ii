package es.urjc.escet.gsyc.html.internal;

public class HtmlTextArea extends HtmlElement{
		
	HtmlTextArea(String name){
		super(TEXT_AREA_TAG);
		this.addAttribute(NAME, name);
	}
	
	public void addText(String text){
		this.addChild(new HtmlText(text));
	}
	public void setSize(int cols, int rows){
		this.addAttribute(COLS, new Integer(cols).toString());
		this.addAttribute(ROWS, new Integer(rows).toString());
	}
}
