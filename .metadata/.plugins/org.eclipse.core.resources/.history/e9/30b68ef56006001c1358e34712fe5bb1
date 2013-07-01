package es.urjc.escet.gsyc.html.internal;

import java.util.*;

public class HtmlElement extends HtmlObject{
	protected String tag;
	protected Map<String, String> attributes;
	protected List<HtmlObject> children;
	
	HtmlElement(String tag){
		this.tag = tag;
		children = new ArrayList<HtmlObject>();
		attributes = new HashMap<String, String>();
	}
	
	protected void addChild(HtmlObject child){
		children.add(child);
	}
	
	protected void addAttribute(String name, String value){
		attributes.put(name, value);
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
		result.append(">\r\n");
		Iterator<HtmlObject> j = children.iterator();
		while(j.hasNext()){
			HtmlObject internal = j.next();
			result.append(internal.build());
		}
		result.append("</" + tag + ">\r\n");
		return result;
	}
}
