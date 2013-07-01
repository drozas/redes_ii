package es.urjc.escet.gsyc.html.internal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HtmlA extends HtmlElement {
	private static final String URL_ENCODING = "ISO-8859-1";
	
	private boolean hasVars;
	HtmlA(String destination, String text){
		super(A_TAG);
		this.addAttribute(HREF, destination);
		this.addChild(new HtmlText(text));
		this.hasVars = false;
	}
	
	public void addVar(String varName, String varValue){
		String destination = this.attributes.get(HREF);
		if(!hasVars){
			hasVars = true;
			destination = destination + "?";
		} else {
			destination = destination + "&"; 
		}
		try{
			destination = destination + URLEncoder.encode(varName, URL_ENCODING) + "=" + URLEncoder.encode(varValue, URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		this.attributes.put(HREF, destination);
	}
	
	
	
	
}
