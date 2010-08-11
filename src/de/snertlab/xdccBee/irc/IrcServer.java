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

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

import de.snertlab.xdccBee.ui.Application;

/**
 * @author snert
 *
 */
public class IrcServer implements IConnectedState{
	
	private String hostname;
	private String port;
	private String nickname;
	private Map<String, IrcChannel> mapIrcChannels;
	private DccBot dccBot;
	private Thread threadBotConnect;
	private Map <String, DccPacket> mapDccPackets;
	private boolean isDebug;
	private boolean autoconnect;
	
	//FIXME: If Server gets disconnect then disconnect all his channels otherwise they are marked as connected in the view and you cannot
	//reconnect them anymore only restart solves the problem
	
	public IrcServer(String botName, String botVersion){
		this.mapIrcChannels = new LinkedHashMap<String, IrcChannel>();
		this.mapDccPackets = new LinkedHashMap<String, DccPacket>();
		this.dccBot = new DccBot(this, Application.getSettings().getBotName(), Application.getSettings().getBotVersion());		
	}

	public IrcServer(String hostname, String nickname, String port, String botName, String botVersion){
		this(botName, botVersion);
		this.hostname 			= hostname;
		this.nickname 			= nickname;
		this.port 			 	= port;
	}
		
	public String getHostname() {
		return hostname;
	}
	
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname){
		this.nickname = nickname;
	}

	public String getPort() {
		return port;
	}
	
	public void setPort(String port){
		this.port = port;
	}

	public List<IrcChannel> getListChannels() {
		return new ArrayList<IrcChannel>(mapIrcChannels.values());
	}

	public void addIrcChannel(IrcChannel newIrcChannel) {
		if( containsIrcChannel(newIrcChannel.getChannelName()) ){
			throw new RuntimeException("Channel bereits vorhanden"); //$NON-NLS-1$
		}
		newIrcChannel.setIrcServer(this);
		mapIrcChannels.put(newIrcChannel.getChannelName(), newIrcChannel);	
	}
	
	public boolean containsIrcChannel(String channelName) {
		return mapIrcChannels.containsKey(channelName);
	}

	public void removeIrcChannel(IrcChannel selectedIrcChannel) {
		mapIrcChannels.remove(selectedIrcChannel.getChannelName());
	}
	
	public void connect(){
		dccBot.setVerbose(isDebug);
		threadBotConnect = new Thread( new Runnable() {
			@Override
			public void run() {
				try {
					dccBot.setNickname(nickname);
					dccBot.connect(hostname, Integer.parseInt(port));
				} catch (NumberFormatException e) {
					throw new RuntimeException(e);
				} catch (NickAlreadyInUseException e) {
					//TODO: In GUI anzeigen
					throw new RuntimeException(e);
				} catch (IOException e) {
					//TODO: Connection refused
					throw new RuntimeException(e);
				} catch (IrcException e) {
					//TODO: In GUI anzeigen
					throw new RuntimeException(e);
				}				
			}
		});
		threadBotConnect.start();
	}
	
	public DccBot getDccBot(){
		return dccBot;
	}

	public boolean isConnected() {
		if(isConnecting()) return false;
		return dccBot.isConnected();
	}

	public boolean isConnecting() {
		if(threadBotConnect == null) return false;
		return threadBotConnect.isAlive();
	}
	
	public void disconnect(){
		for (IrcChannel ircChannel : mapIrcChannels.values()) {
			if(ircChannel.isConnected()){
				ircChannel.disconnect();
			}
		}
		dccBot.disconnect();
		dccBot.dispose();
	}
	
	public void addDccPacket(DccPacket dccPacket){
		mapDccPackets.put(dccPacket.toString(), dccPacket);
	}

	public IrcChannel getChannelByName(String channel) {
		for (IrcChannel ircChannel : mapIrcChannels.values()) {
			if(channel.toUpperCase().equals(ircChannel.getChannelName().toUpperCase())){
				return ircChannel;
			}
		}
		return null;
	}

	public List<DccPacket> getListDccPackets() {
		//		return Collections.unmodifiableList(mapDccPackets);
		return new ArrayList<DccPacket>(mapDccPackets.values());
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof IrcServer)) return false;	
		IrcServer ircServer = (IrcServer) obj;
		if(!getHostname().equals(ircServer.getHostname())) return false;
		if(!getPort().equals(ircServer.getPort())) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getHostname().hashCode() + getPort().hashCode();
	}

	public void setAutoconnect(boolean autoconnect) {
		this.autoconnect = autoconnect;
	}

	public boolean isAutoconnect() {
		return autoconnect;
	}

	/**
	 * @param dccPacket
	 * @return
	 */
	public boolean containsPacket(DccPacket dccPacket) {
		return mapDccPackets.containsKey(dccPacket.toString());
	}
}
