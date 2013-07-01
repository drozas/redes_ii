package es.urjc.escet.gsyc.html.internal;

public class HtmlTable extends HtmlElement {	
	
	HtmlTable(){
		super(TABLE_TAG);
		this.addAttribute(WIDTH, "100%");
		this.addAttribute(BORDER, "0");
		this.addAttribute(BORDERCOLOR, "#000000");
		this.addAttribute(CELLPADDING, "4");
		this.addAttribute(CELLSPACING, "0");
	}

	public void setWidth(int width){
		this.addAttribute(WIDTH, new Integer(width).toString() + "%");
	}
	public void setBorder(int border){
		this.addAttribute(BORDER, new Integer(border).toString());
	}
	public void setBorderColor(String borderColor){
		this.addAttribute(BORDERCOLOR, borderColor);
	}
	public void setCellPadding(int cellPadding){
		this.addAttribute(CELLPADDING, new Integer(cellPadding).toString());
	}
	public void setCellSpacing(int cellSpacing){
		this.addAttribute(CELLSPACING, new Integer(cellSpacing).toString());
	}
	public HtmlTableTr addTr(){
		HtmlTableTr tr = new HtmlTableTr();
		this.addChild(tr);
		return tr;
	}
	
}
