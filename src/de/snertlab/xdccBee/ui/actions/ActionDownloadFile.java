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

import de.snertlab.xdccBee.irc.DccPacket;
import de.snertlab.xdccBee.irc.IrcServer;
import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.ui.Application;
import de.snertlab.xdccBee.ui.ViewMain;

/**
 * @author holgi
 * 
 */
public class ActionDownloadFile extends Action {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#getText()
	 */
	@Override
	public String getText() {
		return XdccBeeMessages.getString("ActionDownloadFile_NAME"); //$NON-NLS-1$
	}

	@Override
	public String getToolTipText() {
		return XdccBeeMessages.getString("ActionDownloadFile_TOOLTIP"); //$NON-NLS-1$
	}

	@Override
	public void run() {
		DccPacket dccPacket = getViewMain().getSelectedDccPacket();
		IrcServer ircServer = Application.getServerSettings().getServerByName(
				dccPacket.getHostname());
		ircServer.getChannelByName(dccPacket.getChannelName()).dccSendFile(
				dccPacket); //$NON-NLS-1$
	}

	private ViewMain getViewMain() {
		return Application.getWindow().getViewMain();
	}
}
