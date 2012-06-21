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
package de.snertlab.xdccBee.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.ui.Application;

/**
 * @author snert
 * 
 */
public class ActionQuit extends Action {

	public static final String ID = "de.snertlab.xdccBee.ui.actions.actionQuit"; //$NON-NLS-1$
	private boolean doDisplayDispose;

	public ActionQuit(boolean doDisplayDispose) {
		this.doDisplayDispose = doDisplayDispose;
		setText(XdccBeeMessages.getString("ActionQuit_NAME")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		Point location = Display.getCurrent().getActiveShell().getLocation();
		Point size = Display.getCurrent().getActiveShell().getSize();
		Application.getSettings().setMainWindowPosition(location);
		Application.getSettings().setMainWindowSize(size);
		Application.getSettings().saveSettings();
		if (doDisplayDispose) {
			Display.getCurrent().dispose();
		}
	}

	@Override
	public String getId() {
		return ID;
	}
}
