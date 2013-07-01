package es.urjc.escet.gsyc.html.internal;

public class HtmlP extends HtmlElement {
	
	public enum AlignType {LEFT, RIGHT, CENTER, JUSTIFY};
	
	HtmlP(){
		super(P_TAG);
	}
	
	public void addText(String text){
		this.addChild(new HtmlText(text));
	}
	public void setAlign(AlignType type){
		this.addAttribute(ALIGN, type.toString());
		
	}
	public HtmlA addA(String destination, String text){
		HtmlA anchor = new HtmlA(destination, text);
		this.addChild(anchor);
		return anchor;
	}
	public HtmlP addP(){
		HtmlP paragraph = new HtmlP();
		this.addChild(paragraph);
		return paragraph;
	}
	public void addBr(){
		HtmlBr br = new HtmlBr();
		this.addChild(br);
	}
}
