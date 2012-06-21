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

/**
 * @author snert
 * 
 */
public class DccPacket {
	DccBot dccBot;
	private int packetNr;
	private String name;
	private String size;
	private String sender;
	private IrcChannel ircChannel;

	public DccPacket() {
	}

	public DccPacket(int packetNr, String name, String size) {
		this.packetNr = packetNr;
		this.name = name;
		this.size = size;
	}

	public DccBot getDccBot() {
		return dccBot;
	}

	public void setDccBot(DccBot dccBot) {
		this.dccBot = dccBot;
	}

	public int getPacketNr() {
		return packetNr;
	}

	public String getName() {
		return name;
	}

	public String getSize() {
		return size;
	}

	public String getSender() {
		return sender;
	}

	public void setPacketNr(int packetNr) {
		this.packetNr = packetNr;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(packetNr);
		sb.append(name);
		sb.append(size);
		sb.append(sender);
		sb.append(getHostname());
		sb.append(getChannelName());
		return sb.toString();
	}

	public String getChannelName() {
		return ircChannel.getChannelName();
	}

	public void setIrcChannel(IrcChannel ircChannel) {
		this.ircChannel = ircChannel;
	}

	public String getHostname() {
		return getIrcServer().getHostname();
	}

	private IrcServer getIrcServer() {
		return dccBot.getIrcServer();
	}

	public IrcChannel getIrcChannel() {
		return ircChannel;
	}

	public void setChannel(IrcChannel ircChannel) {
		this.ircChannel = ircChannel;
	}
}
