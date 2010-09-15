/*
 * Project: xdccBee
 * Copyright (C) 2009 snert@snert-lab.de,
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.snertlab.xdccBee.controlling;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import de.snertlab.xdccBee.AppConfig;

/**
 * @author snert
 *
 */
public final class BeeLogger {
	
	private static Logger LOGGER;
	public static final String LOGGER_NAME = "XdccBee";
	private static ConsoleHandler console;
	private static String logpath;
	
	public static Logger getLogger(){
		if(LOGGER==null){
			LOGGER = Logger.getLogger(LOGGER_NAME);
            final Formatter formatter = new SimpleFormatter();
            LOGGER.setUseParentHandlers(false);

            console = new ConsoleHandler();
            console.setLevel(Level.ALL);
            console.setFormatter(formatter);
            LOGGER.addHandler(console);

            LOGGER.setLevel(Level.ALL); //TODO: setLevel to log in file only errors and info

            try {
                logpath = AppConfig.SETTINGS_FOLDER_PATH + "/xdccBee.log";
                FileHandler filehandler = new FileHandler(logpath);
                filehandler.setFormatter(formatter);
                LOGGER.addHandler(filehandler);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

		}
		return LOGGER;
	}
	
	public static void removeConsoleHandler() {
        if (console != null) {
            getLogger().removeHandler(console);
        }
        System.err.println("Removed Consolehandler. Start with -debug to see console output");
    }
	
	public static void exception(final Level level, final Throwable e) {
		getLogger().log(level, level.getName() + " Exception occurred", e);
	}
	
	public static void exception(final Throwable e) {
        exception(Level.SEVERE, e);
    }
	
	public static String getStackTrace(final Throwable thrown) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		thrown.printStackTrace(pw);
		pw.close();
		return sw.toString();
	 }
}
