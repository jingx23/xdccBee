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
package de.snertlab.xdccBee.irc;

import java.util.ArrayList;
import java.util.List;

import de.snertlab.xdccBee.ui.Application;

/**
 * @author holgi
 *
 */
public class ServerList {
	public static boolean isAnyServerConnected(){
		return ! getListConnectedServer().isEmpty();
	}

	public static List<IrcServer> getListConnectedServer() {
		List<IrcServer> listConnectedServer = new ArrayList<IrcServer>();
		List<IrcServer> listAlleServer = Application.getServerSettings().getListServer();
		for (IrcServer ircServer : listAlleServer) {
			if(ircServer.isConnected()){
				listConnectedServer.add(ircServer);
			}
		}

		return listConnectedServer;
	}

}
