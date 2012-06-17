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
package de.snertlab.xdccBee.ui.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import de.snertlab.xdccBee.irc.DccPacket;

/**
 * @author snert
 * 
 */
public class FileFilter extends ViewerFilter {

	public static final String MODE_ALL = "modeAll"; //$NON-NLS-1$
	public static final String MODE_FILENAME = "modeFilename"; //$NON-NLS-1$
	public static final String MODE_SERVER = "modeServer"; //$NON-NLS-1$
	public static final String MODE_CHANNEL = "modeChannel"; //$NON-NLS-1$

	private String mode;
	private String hostname;
	private String channelName;
	private String fileName;
	private boolean ignoreCase;
	private boolean regExp;

	public FileFilter() {
		this.mode = MODE_ALL;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		DccPacket dccPacket = (DccPacket) element;
		if (MODE_ALL.equals(mode)) {
			return true;
		} else if (MODE_SERVER.equals(mode)) {
			if (dccPacket.getHostname().equals(hostname)) {
				return true;
			}
		} else if (MODE_CHANNEL.equals(mode)) {
			if (dccPacket.getChannelName().equals(channelName)) {
				return true;
			}
		} else if (MODE_FILENAME.equals(mode)) {
			String fileNameToSearch = fileName;
			String dccPacketName = dccPacket.getName();
			if (ignoreCase) {
				fileNameToSearch = fileName.toLowerCase();
				dccPacketName = dccPacketName.toLowerCase();
			}
			if (regExp) {
				try {
					Pattern fileNamePattern = Pattern.compile(fileName);
					Matcher m = fileNamePattern.matcher(dccPacket.getName());
					if (m.find()) {
						return true;
					}
				} catch (PatternSyntaxException e) {
					// ignore
					return false;
				}
			} else {
				String[] keywords = fileNameToSearch.split(" ");
				int matchCount = 0;
				for (String keyword : keywords) {
					if (dccPacketName.contains(keyword)) {
						matchCount++;
					}
				}
				return (matchCount == keywords.length);
			}

		} else {
			throw new RuntimeException("Unknown Mode " + mode); //$NON-NLS-1$
		}
		return false;
	}

	public void setHostname(String hostname) {
		this.mode = MODE_SERVER;
		this.hostname = hostname;
	}

	public void setChannelName(String channelName) {
		this.mode = MODE_CHANNEL;
		this.channelName = channelName;
	}

	public void setFileName(String fileName) {
		this.mode = MODE_FILENAME;
		this.fileName = fileName;
	}

	public void setAll() {
		this.mode = MODE_ALL;
	}

	public void setFilterIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public void setFilterRegExp(boolean regExp) {
		this.regExp = regExp;
	}

}
