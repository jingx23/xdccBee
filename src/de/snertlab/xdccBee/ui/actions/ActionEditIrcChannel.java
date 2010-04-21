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

import de.snertlab.xdccBee.irc.IrcChannel;
import de.snertlab.xdccBee.irc.listener.NotifyManagerIrcServerEditNew;
import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.ui.Application;
import de.snertlab.xdccBee.ui.dialog.EditNewIrcChannelDialog;

/**
 * @author snert
 *
 */
public class ActionEditIrcChannel extends Action {
	
public static final String ID="de.snertlab.xdccBee.ui.actions.actionEditIrcServer"; //$NON-NLS-1$
	
	private Shell parentShell;
	private IrcChannel ircChannel;
	
	public ActionEditIrcChannel(Shell parentShell, IrcChannel ircChannel){
		super(XdccBeeMessages.getString("ActionEditIrcChannel_NAME")); //$NON-NLS-1$
		this.parentShell = parentShell;
		this.ircChannel = ircChannel;
		setId(ID);
	}

	@Override
	public void run() {
		EditNewIrcChannelDialog editNewIrcServerDialog = new EditNewIrcChannelDialog(parentShell, ircChannel, false);
		int ret = editNewIrcServerDialog.open();
		if(Window.OK == ret){
			Application.getServerSettings().saveSettings();
			if(ircChannel.isAutoconnect()){
				if(! ircChannel.getIrcServer().isConnected()){
					ircChannel.getIrcServer().connect();
				}else{
					ircChannel.connect();
				}
			}
			NotifyManagerIrcServerEditNew.getNotifyManager().notify(ircChannel.getIrcServer());
		}
	}

}
