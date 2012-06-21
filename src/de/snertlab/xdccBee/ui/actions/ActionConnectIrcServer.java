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
import de.snertlab.xdccBee.messages.XdccBeeMessages;

/**
 * @author snert
 * 
 */
public class ActionConnectIrcServer extends Action {

	public static final String ID = "de.snertlab.xdccBee.ui.actions.actionConnectIrcServer"; //$NON-NLS-1$

	private IrcServer ircServer;

	public ActionConnectIrcServer(IrcServer ircServer) {
		super(XdccBeeMessages.getString("ActionConnectIrcServer_NAME")); //$NON-NLS-1$
		this.ircServer = ircServer;
		setId(ID);
	}

	@Override
	public void run() {
		ircServer.connect();
	}

}
