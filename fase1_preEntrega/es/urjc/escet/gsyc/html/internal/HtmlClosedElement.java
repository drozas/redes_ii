package es.urjc.escet.gsyc.html.internal;

import java.util.Iterator;

public class HtmlClosedElement extends HtmlElement{
	
	HtmlClosedElement(String tag){
		super(tag);
	}
	
	StringBuilder build(){
		StringBuilder result = new StringBuilder("");
		result.append("<" + tag);
		Iterator<String> i = attributes.keySet().iterator();
		while(i.hasNext()){
			String name = i.next();
			String value = attributes.get(name);
			result.append(" " + name  + "=\"" + value + "\"");
		}
		result.append("/>\r\n");
		return result;
	}
}
