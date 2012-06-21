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
package de.snertlab.xdccBee;

import java.io.File;

/**
 * @author snert
 * 
 */
public class AppConfig {

	public static final String USERHOME = System.getProperty("user.home"); //$NON-NLS-1$
	public static final String SETTINGS_FOLDER_PATH = USERHOME
			+ "/" + ".xdccBee"; //$NON-NLS-1$ //$NON-NLS-2$
	public static final String DEFAULT_IRC_PORT = "6667"; //$NON-NLS-1$
	public static final int MOUSE_BUTTON_RIGHT = 3;

	static {
		File settingsFolder = new File(AppConfig.SETTINGS_FOLDER_PATH);
		if (!settingsFolder.exists()) {
			settingsFolder.mkdir();
		}
		settingsFolder = null;
	}

	private AppConfig() {
		// prevent instantion
	}

}
