package es.urjc.escet.gsyc.html.internal;

public class HtmlTableTd extends HtmlElement {
		
	public enum AlignStyle {LEFT, CENTER, MIDDLE, RIGHT};

	HtmlTableTd(){
		super(TD_TAG);
	}
	
	public void setAlign(AlignStyle style){
		this.addAttribute(ALIGN, style.toString());
	}
	public void setWidth(int width){
		this.addAttribute(WIDTH, new Integer(width).toString() + "%");
	}
	public void setHeight(int height){
		this.addAttribute(HEIGHT, new Integer(height).toString());
	}
	public void setBgColor(String bgColor){
		this.addAttribute(BGCOLOR, bgColor);
	}
	
	public void addText(String text){
		this.addChild(new HtmlText(text));
	}
	public HtmlA addA(String destination, String text){
		HtmlA anchor = new HtmlA(destination, text);
		this.addChild(anchor);
		return anchor;
	}
	public HtmlP addP(){
		HtmlP p = new HtmlP();
		this.addChild(p);
		return p;
	}
	public void addBr(){
		this.addChild(new HtmlBr());
	}
	public HtmlForm addForm(String target){
		HtmlForm f = new HtmlForm(target);
		this.addChild(f);
		return f;
	}
	public HtmlTable addTable(){
		HtmlTable t = new HtmlTable();
		this.addChild(t);
		return t;
	}
	public void addInputText(String name){
		HtmlClosedElement input = new HtmlClosedElement(INPUT);
		input.addAttribute(TYPE, TEXT);
		input.addAttribute(NAME, name);
		this.addChild(input);
	}
	public void addInputText(String name, String value){
		HtmlClosedElement input = new HtmlClosedElement(INPUT);
		input.addAttribute(TYPE, TEXT);
		input.addAttribute(NAME, name);
		input.addAttribute(VALUE, value);
		this.addChild(input);
	}
	public void addInputPassword(String name){
		HtmlClosedElement input = new HtmlClosedElement(INPUT);
		input.addAttribute(TYPE, PASSWORD);
		input.addAttribute(NAME, name);
		this.addChild(input);
	}
	public void addInputSubmit(String value){
		HtmlClosedElement input = new HtmlClosedElement(INPUT);
		input.addAttribute(TYPE, SUBMIT);
		input.addAttribute(VALUE, value);
		this.addChild(input);
	}
	public void addInputReset(String value){
		HtmlClosedElement input = new HtmlClosedElement(INPUT);
		input.addAttribute(TYPE, RESET);
		input.addAttribute(VALUE, value);
		this.addChild(input);
	}
}
