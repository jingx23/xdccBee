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

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.graphics.Point;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import de.snertlab.xdccBee.AppConfig;
import de.snertlab.xdccBee.tools.XmlTool;


/**
 * @author snert
 *
 */
public class Settings {
	
	private static final String SETTINGS_FILENAME = AppConfig.SETTINGS_FOLDER_PATH + "/" + "settings.xml"; //$NON-NLS-1$ //$NON-NLS-2$
	private static final String DEFAULT_BOTNAME = "c0c0nut"; //$NON-NLS-1$
	private static final String DEFAULT_BOTVERSION = "1.0"; //$NON-NLS-1$
	private static final String DEFAULT_NICKNAME = "c0c0"; //$NON-NLS-1$
	private static final String DEFAULT_DOWNLOADFOLDER = AppConfig.USERHOME;
	
	private static Settings settings;
	
	private String botName;
	private String botVersion;
	private String standardNickname;
	private String downloadFolder;
	private String mainWindowPositionX;
	private String mainWindowPositionY;
	private String mainWindowSizeX;
	private String mainWindowSizeY;

	
	public static Settings getInstance(){
		if(settings==null){
			settings = new Settings();
		}
		return settings;
	}
	
	public Settings(){
		Document docSettings = makeSettingFolderWithEmptySettingsFile();
		initFields(docSettings);
	}
	
	private Document makeSettingFolderWithEmptySettingsFile(){
		File settingsFile = new File(SETTINGS_FILENAME);
		if(! settingsFile.exists() ){			
			this.botName 			= DEFAULT_BOTNAME;
			this.botVersion 		= DEFAULT_BOTVERSION;
			this.standardNickname 	= DEFAULT_NICKNAME;
			this.downloadFolder 	= DEFAULT_DOWNLOADFOLDER;
			this.mainWindowPositionX= "0"; //$NON-NLS-1$
			this.mainWindowPositionY= "0"; //$NON-NLS-1$
			this.mainWindowSizeX = "800"; //$NON-NLS-1$
			this.mainWindowSizeY = "600"; //$NON-NLS-1$

			Document doc = saveSettings();
			return doc;
		}else{
			try {
				SAXBuilder builder = new SAXBuilder();
				Document anotherDocument = builder.build(new File(settingsFile.getPath()));
				return anotherDocument;
			} catch (JDOMException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private Document buildSettingXml(){
		Document doc = new Document();
		Element nodeRoot = new Element("ROOT"); //$NON-NLS-1$
		doc.setRootElement(nodeRoot);
		
		Element nodeBot = XmlTool.addChildNode(nodeRoot, "BOT"); //$NON-NLS-1$
		nodeBot.setAttribute("botName", StringUtils.defaultString(botName)); //$NON-NLS-1$
		nodeBot.setAttribute("botVersion", StringUtils.defaultString(botVersion)); //$NON-NLS-1$
		nodeBot.setAttribute("standardNickname", StringUtils.defaultString(standardNickname)); //$NON-NLS-1$
		nodeBot.setAttribute("downloadFolder", StringUtils.defaultString(downloadFolder)); //$NON-NLS-1$
		nodeBot.setAttribute("mainWindowPositionX", StringUtils.defaultString(mainWindowPositionX)); //$NON-NLS-1$
		nodeBot.setAttribute("mainWindowPositionY", StringUtils.defaultString(mainWindowPositionY)); //$NON-NLS-1$
		nodeBot.setAttribute("mainWindowSizeX", StringUtils.defaultString(mainWindowSizeX)); //$NON-NLS-1$
        nodeBot.setAttribute("mainWindowSizeY", StringUtils.defaultString(mainWindowSizeY)); //$NON-NLS-1$

		return doc;
	}
		
	private void initFields(Document docSettings){
		Element nodeBot 	= docSettings.getRootElement().getChild("BOT"); //$NON-NLS-1$
		botName 			= StringUtils.defaultString( nodeBot.getAttributeValue("botName") ); //$NON-NLS-1$
		botVersion 			= StringUtils.defaultString( nodeBot.getAttributeValue("botVersion") ); //$NON-NLS-1$
		standardNickname 	= StringUtils.defaultString( nodeBot.getAttributeValue("standardNickname") ); //$NON-NLS-1$
		downloadFolder		= StringUtils.defaultString( nodeBot.getAttributeValue("downloadFolder") ); //$NON-NLS-1$
		mainWindowPositionX = StringUtils.defaultString( nodeBot.getAttributeValue("mainWindowPositionX") ); //$NON-NLS-1$
		mainWindowPositionY = StringUtils.defaultString( nodeBot.getAttributeValue("mainWindowPositionY") ); //$NON-NLS-1$
		mainWindowSizeX 	= StringUtils.defaultString( nodeBot.getAttributeValue("mainWindowSizeX") ); //$NON-NLS-1$
		mainWindowSizeY 	= StringUtils.defaultString( nodeBot.getAttributeValue("mainWindowSizeY") ); //$NON-NLS-1$

	}
	
	public Document saveSettings(){
		try{
			File settingsFile = new File(SETTINGS_FILENAME);
			Document doc = buildSettingXml();
			XMLOutputter xmlOut = new XMLOutputter();
			xmlOut.output(doc, new BufferedOutputStream(new FileOutputStream(settingsFile)));
			return doc;
		}catch(IOException e){
			throw new RuntimeException(e);
		}

	}

	public String getBotName() {
		return botName;
	}

	public void setBotName(String botName) {
		this.botName = botName;
	}

	public String getBotVersion() {
		return botVersion;
	}

	public void setBotVersion(String botVersion) {
		this.botVersion = botVersion;
	}

	public String getStandardNickname() {
		return standardNickname;
	}

	public void setStandardNickname(String standardNickname) {
		this.standardNickname = standardNickname;
	}

	public String getDownloadFolder() {
		return downloadFolder;
	}

	public void setDownloadFolder(String downloadFolder) {
		this.downloadFolder = downloadFolder;
	}
	
	public Point getMainWindowPosition(){
		return new Point(Integer.parseInt(mainWindowPositionX), Integer.parseInt(mainWindowPositionY));
	}

	public Point getMainWindowSize(){
		return new Point(Integer.parseInt(mainWindowSizeX), Integer.parseInt(mainWindowSizeY));
	}

	public void setMainWindowPosition(Point location) {
		this.mainWindowPositionX = location.x+""; //$NON-NLS-1$
		this.mainWindowPositionY = location.y+""; //$NON-NLS-1$
	} 
	
	public void setMainWindowSize(Point size) {
		this.mainWindowSizeX = size.x+""; //$NON-NLS-1$
		this.mainWindowSizeY = size.y+""; //$NON-NLS-1$
	}
}
