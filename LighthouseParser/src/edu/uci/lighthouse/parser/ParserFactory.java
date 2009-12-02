package edu.uci.lighthouse.parser;

import java.util.Properties;

public class ParserFactory {

	private final static String propertiesFile = "/parser.properties";
	
	public static IParser getDefaultParser() throws ParserException{
		IParser parser = null;
		try {
			Properties properties = new Properties();		
			properties.load(ParserFactory.class.getResourceAsStream(propertiesFile));
			String classFqn = properties.getProperty("parser");
			if (classFqn == null) {
				throw new ParserException("Parser class not found.");
			} else {
				Class<?> aClass = Class.forName(classFqn);
				parser = (IParser) aClass.newInstance();
			}
		} catch (Exception e) {
			throw new ParserException(e);
		}
		return parser;
	}
	
}
