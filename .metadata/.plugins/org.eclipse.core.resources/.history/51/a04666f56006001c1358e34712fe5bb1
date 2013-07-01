package es.urjc.escet.gsyc.html.internal;

public class HtmlTableTr extends HtmlElement {
	
	public enum ValignStyle {TOP, MIDDLE, CENTER, BASELINE, BOTTOM};
	
	HtmlTableTr(){
		super(TR_TAG);
	}
	public void setWidth(int width){
		this.addAttribute(WIDTH, new Integer(width).toString() + "%");
	}
	public void setHeight(int height){
		this.addAttribute(HEIGHT, new Integer(height).toString());
	}
	public void setValign(ValignStyle style){
		this.addAttribute(VALIGN, style.toString());
	}
	public HtmlTableTd addTd(){
		HtmlTableTd td = new HtmlTableTd();
		this.addChild(td);
		return td;
	}
}
