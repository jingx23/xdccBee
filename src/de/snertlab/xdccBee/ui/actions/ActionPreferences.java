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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.ui.Application;
import de.snertlab.xdccBee.ui.dialog.SettingsDialog;

/**
 * @author snert
 *
 */
public class ActionPreferences extends Action {
	
	public static final String ID="de.snertlab.xdccBee.ui.actions.actionPreferences"; //$NON-NLS-1$
	
	private Shell parentShell;
	
	public ActionPreferences(Shell parentShell){
		super(XdccBeeMessages.getString("ActionPreferences_NAME")); //$NON-NLS-1$
		this.parentShell = parentShell;
	}

	@Override
	public void run() {
		SettingsDialog settingsDialog = new SettingsDialog(parentShell);
		int ret = settingsDialog.open();
		if(Window.OK == ret){
			Application.getSettings().saveSettings();
		}
	}
	
	@Override
	public String getId() {
		return ID;
	}

}
