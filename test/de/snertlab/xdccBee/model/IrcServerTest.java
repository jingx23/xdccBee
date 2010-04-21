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
package de.snertlab.xdccBee.model;

import java.io.File;

import junit.framework.TestCase;
import de.snertlab.xdccBee.AppConfig;
import de.snertlab.xdccBee.irc.IrcChannel;
import de.snertlab.xdccBee.irc.IrcServer;
import de.snertlab.xdccBee.settings.ServerSettings;

/**
 * @author snert
 *
 */
public class IrcServerTest extends TestCase{
	
	public void test1_createServerAndSave(){
		File config = new File( AppConfig.SETTINGS_FOLDER_PATH + "/" + "ircServer.xml");
		config.delete();
		IrcServer ircServer = new IrcServer("test", "nickname", "6443", "botName", "botVersion");
		assertEquals("test", ircServer.getHostname());
		assertEquals(AppConfig.DEFAULT_IRC_PORT, ircServer.getPort());
		
		IrcChannel ircChannel = new IrcChannel(ircServer);
		ircChannel.setChannelName("#test");
		assertEquals("#test", ircChannel.getChannelName());
		
		ServerSettings.getInstance().addServer(ircServer);
		ServerSettings.getInstance().saveSettings();
	}

}
