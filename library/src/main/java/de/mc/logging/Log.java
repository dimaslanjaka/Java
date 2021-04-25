package de.mc.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Log {
	private static Logger LOGGER = null;

	static {
		InputStream stream = Log.class.getClassLoader().
						getResourceAsStream("logging.properties");
		try {
			LogManager.getLogManager().readConfiguration(stream);
			LOGGER = Logger.getLogger(Log.class.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("-- main method starts --");
		LOGGER.info("in MyClass");
		LOGGER.warning("a test warning");
		LOGGER.severe("Severe");
	}
}
