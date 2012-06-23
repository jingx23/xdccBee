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
package de.snertlab.xdccBee.settings;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import de.snertlab.xdccBee.AppConfig;
import de.snertlab.xdccBee.irc.IrcChannel;
import de.snertlab.xdccBee.irc.IrcServer;
import de.snertlab.xdccBee.tools.XmlTool;
import de.snertlab.xdccBee.ui.Application;

/**
 * @author snert
 * 
 */
public class ServerSettings {
	private static ServerSettings serverSettings;

	private static final String SETTINGS_FILENAME = AppConfig.SETTINGS_FOLDER_PATH
			+ "/" + "ircServer.xml"; //$NON-NLS-1$ //$NON-NLS-2$

	private Map<String, IrcServer> mapIrcServer;

	public static ServerSettings getInstance() {
		if (serverSettings == null) {
			serverSettings = new ServerSettings();
		}
		return serverSettings;
	}

	public ServerSettings() {
		this.mapIrcServer = new LinkedHashMap<String, IrcServer>();
		Document docSettings = makeSettingsFile();
		fillMapIrcServer(docSettings);
	}

	@SuppressWarnings("unchecked")
	private void fillMapIrcServer(Document docSettings) {
		Element nodeServer = docSettings.getRootElement()
				.getChild("IRC_SERVER"); //$NON-NLS-1$
		List<Element> listChildren = (List<Element>) nodeServer.getChildren();
		for (Element child : listChildren) {
			IrcServer ircServer = new IrcServer(child.getAttributeValue("id"),
					child.getAttributeValue("hostname"), //$NON-NLS-1$
					child.getAttributeValue("nickname"), //$NON-NLS-1$
					child.getAttributeValue("port"), //$NON-NLS-1$
					Application.getSettings().getBotName(), Application
							.getSettings().getBotVersion());
			ircServer
					.setDebug(child.getAttributeValue("isDebug").equals("1") ? true : false); //$NON-NLS-1$ //$NON-NLS-2$
			ircServer
					.setAutoconnect(child
							.getAttributeValue("isAutoconnect").equals("1") ? true : false); //$NON-NLS-1$ //$NON-NLS-2$
			List<Element> listChildrenServer = (List<Element>) child
					.getChildren();
			for (Element childChannel : listChildrenServer) {
				IrcChannel ircChannel = new IrcChannel(ircServer);
				ircChannel.setChannelName(childChannel
						.getAttributeValue("channelName")); //$NON-NLS-1$
				ircChannel.setAutoconnect(childChannel.getAttributeValue(
						"isAutoconnect").equals("1") ? true : false); //$NON-NLS-1$ //$NON-NLS-2$
				ircServer.addIrcChannel(ircChannel);
			}
			mapIrcServer.put(ircServer.getId(), ircServer);
		}
	}

	private Document makeSettingsFile() {
		File settingsFile = new File(SETTINGS_FILENAME);
		if (!settingsFile.exists()) {
			Document doc = saveSettings();
			return doc;
		} else {
			try {
				SAXBuilder builder = new SAXBuilder();
				Document anotherDocument = builder.build(new File(settingsFile
						.getPath()));
				return anotherDocument;
			} catch (JDOMException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private Document buildSettingXml() {
		Document doc = new Document();
		Element nodeRoot = new Element("ROOT"); //$NON-NLS-1$
		doc.setRootElement(nodeRoot);
		Element nodeIrcServer = XmlTool.addChildNode(nodeRoot, "IRC_SERVER"); //$NON-NLS-1$
		for (IrcServer ircServer : mapIrcServer.values()) {
			Element nodeServer = new Element("IRC_SERVER"); //$NON-NLS-1$
			nodeServer.setAttribute(
					"id", StringUtils.defaultString(ircServer.getId())); //$NON-NLS-1$
			nodeServer
					.setAttribute(
							"hostname", StringUtils.defaultString(ircServer.getHostname())); //$NON-NLS-1$
			nodeServer
					.setAttribute(
							"nickname", StringUtils.defaultString(ircServer.getNickname())); //$NON-NLS-1$
			nodeServer.setAttribute(
					"port", StringUtils.defaultString(ircServer.getPort())); //$NON-NLS-1$
			nodeServer.setAttribute("isDebug", ircServer.isDebug() ? "1" : "0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			nodeServer.setAttribute(
					"isAutoconnect", ircServer.isAutoconnect() ? "1" : "0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			for (IrcChannel ircChannel : ircServer.getListChannels()) {
				Element nodeChannel = new Element("IRC_CHANNEL"); //$NON-NLS-1$
				nodeChannel
						.setAttribute(
								"channelName", StringUtils.defaultString(ircChannel.getChannelName())); //$NON-NLS-1$
				nodeChannel
						.setAttribute(
								"isAutoconnect", ircChannel.isAutoconnect() ? "1" : "0"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				nodeServer.addContent(nodeChannel);
			}
			nodeIrcServer.addContent(nodeServer);
		}
		return doc;
	}

	public Document saveSettings() {
		try {
			File settingsFile = new File(SETTINGS_FILENAME);
			Document doc = buildSettingXml();
			XMLOutputter xmlOut = new XMLOutputter();
			xmlOut.output(doc, new BufferedOutputStream(new FileOutputStream(
					settingsFile)));
			return doc;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addServer(IrcServer ircServer) {
		if (getServerByName(ircServer.getHostname()) != null) {
			throw new RuntimeException("Irc Server bereits vorhanden"); //$NON-NLS-1$
		}
		mapIrcServer.put(ircServer.getId(), ircServer);
	}

	public List<IrcServer> getListServer() {
		return new ArrayList<IrcServer>(mapIrcServer.values());
	}

	public void removeServer(IrcServer ircServer) {
		mapIrcServer.remove(ircServer.getId());
	}

	public void removeIrcChannel(IrcChannel ircChannel) {
		ircChannel.getIrcServer().removeIrcChannel(ircChannel);
	}

	public IrcServer getServerByName(String hostname) {
		for (IrcServer ircServer : getListServer()) {
			if (StringUtils.contains(hostname, ircServer.getHostname())) {
				return ircServer;
			}
		}
		return null;
	}

	public List<IrcChannel> getListAllChannels() {
		List<IrcChannel> listAllChannels = new ArrayList<IrcChannel>();
		List<IrcServer> listAllServer = getListServer();
		for (IrcServer ircServer : listAllServer) {
			listAllChannels.addAll(ircServer.getListChannels());
		}
		return listAllChannels;
	}
}
