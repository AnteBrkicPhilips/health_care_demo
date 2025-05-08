package com.example.healthcare;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReaderUtils {

	public static Properties readProperties(String fileName) {
		Properties properties = new Properties();
		try (InputStream input = DatabaseHikariCPUtils.class.getClassLoader().getResourceAsStream(fileName)) {
			properties.load(input);
		}
		catch (IOException e) {
			System.err.println("Unable to load " + fileName);
		}
		return properties;
	}

}
