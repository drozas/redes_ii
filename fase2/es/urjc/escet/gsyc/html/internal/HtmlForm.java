package es.urjc.escet.gsyc.html.internal;

public class HtmlForm extends HtmlElement {
	
	HtmlForm(String action){
		super(FORM_TAG);
		this.addAttribute(ACTION, action);
		this.addAttribute(METHOD, GET);
	}
	
	public void addText(String text){
		this.addChild(new HtmlText(text));
	}
	public HtmlP addP(){
		HtmlP p = new HtmlP();
		this.addChild(p);
		return p;
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
	public void addInputHidden(String name, String value){
		HtmlClosedElement input = new HtmlClosedElement(INPUT);
		input.addAttribute(TYPE, HIDDEN);
		input.addAttribute(NAME, name);
		input.addAttribute(VALUE, value);
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
	public HtmlTextArea addTextArea(String name){
		HtmlTextArea textArea = new HtmlTextArea(name);
		this.addChild(textArea);
		return textArea;
	}
	public void addBr(){
		this.addChild(new HtmlBr());
	}
	public HtmlTable addTable(){
		HtmlTable table = new HtmlTable();
		this.addChild(table);
		return table;
	}
}
