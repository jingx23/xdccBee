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
package de.snertlab.xdccBee.messages;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XdccBeeMessages {

	private static final String BUNDLE_NAME = "de.snertlab.xdccBee.messages.messages_de"; //$NON-NLS-1$
	
	private static ResourceBundle loadedResourceBundle;
	private static Locale useLocale;

	private XdccBeeMessages() {
	}

	public static String getString(String key) {
		try {
			return getResourceBundle().getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	private static ResourceBundle getResourceBundle() {
		if (loadedResourceBundle==null) {
			if (useLocale==null) {
				loadedResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
			} else {
				loadedResourceBundle = ResourceBundle.getBundle(BUNDLE_NAME,useLocale);
			}
		}
		return loadedResourceBundle;
	}

	public static void setLocale(Locale locale) {
		useLocale = locale;
		loadedResourceBundle = null;
	}
	
}
