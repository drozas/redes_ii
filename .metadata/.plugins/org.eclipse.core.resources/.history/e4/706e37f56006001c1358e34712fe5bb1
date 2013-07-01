package es.urjc.escet.gsyc.http;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizadorHttpGet {
	
	private static final String fileExtractorRegEx = "GET (/\\S*)\\s+HTTP/1\\.[01]";
	private static final Pattern fileExtractorPattern = Pattern.compile(fileExtractorRegEx);
	
	private static final String varExtractorRegEx = "([^=&]+)=([^=&]+)";
	private static final Pattern varExtractorPattern = Pattern.compile(varExtractorRegEx);

	private static final String URL_ENCODING = "ISO-8859-1";
	
	private static URL getURL(String firstLine) throws MalformedURLException{
		Matcher m = fileExtractorPattern.matcher(firstLine);
		if(!m.matches())
			return null;
		return new URL ("http://dummy" + m.group(1));
	}

	public static Map<String, String> getVars(String firstLine){
		String query = getQuery(firstLine);
		if(query == null)
			return null;
		Matcher m = varExtractorPattern.matcher(query);

		Map<String, String> result = null;
		while(m.find()){
			if(m.groupCount() != 2)
				return null;
			try{
				String varName = URLDecoder.decode(m.group(1), URL_ENCODING);
				String varValue = URLDecoder.decode(m.group(2), URL_ENCODING);
				if(result == null)
					result = new HashMap<String, String>();
				result.put(varName, varValue);
			} catch(UnsupportedEncodingException e){
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return result;
	}
	
	public static String getPath(String firstLine){
		try{
			URL url = getURL(firstLine);
			return url.getPath();
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	public static String getQuery(String firstLine){
		try{
			URL url = getURL(firstLine);
			return url.getQuery();
		} catch (MalformedURLException e){
			return null;
		}
	}
	
	public static String getRef(String firstLine){
		try{
			URL url = getURL(firstLine);
			return url.getRef();
		} catch (MalformedURLException e){
			return null;
		}	
	}
}
