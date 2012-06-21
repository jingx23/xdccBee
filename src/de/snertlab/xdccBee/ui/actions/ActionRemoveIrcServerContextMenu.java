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

import de.snertlab.xdccBee.irc.IrcServer;
import de.snertlab.xdccBee.irc.listener.NotifyManagerIrcServerEditNew;
import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.ui.Application;

/**
 * @author snert
 * 
 */
public class ActionRemoveIrcServerContextMenu extends Action {

	public static final String ID = "de.snertlab.xdccBee.ui.actions.actionRemoveIrcChannelContextMenu"; //$NON-NLS-1$

	private IrcServer selectedIrcServer;

	public ActionRemoveIrcServerContextMenu(IrcServer selectedIrcServer) {
		super(XdccBeeMessages
				.getString("ActionRemoveIrcServerContextMenu_NAME")); //$NON-NLS-1$
		this.selectedIrcServer = selectedIrcServer;
		setId(ID);
	}

	@Override
	public void run() {
		Application.getServerSettings().removeServer(selectedIrcServer);
		Application.getServerSettings().saveSettings();
		NotifyManagerIrcServerEditNew.getNotifyManager().notify(null);
	}

}
