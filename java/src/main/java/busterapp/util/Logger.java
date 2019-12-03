package busterapp.util;

import org.slf4j.LoggerFactory;

public abstract class Logger {

    public static void info(String message) {
        final org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);

        log.info(message);
    }

	public static void error(String message) {
        final org.slf4j.Logger log = LoggerFactory.getLogger(Logger.class);

        log.error(message);
	}
}