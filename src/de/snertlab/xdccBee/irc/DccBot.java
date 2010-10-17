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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;

import de.snertlab.xdccBee.irc.listener.LogMessage;
import de.snertlab.xdccBee.irc.listener.NotifyManagerConnectedState;
import de.snertlab.xdccBee.irc.listener.NotifyManagerDccBotLogging;
import de.snertlab.xdccBee.irc.listener.NotifyManagerDccPacket;

/**
 * @author snert
 *
 */
public class DccBot extends IRCConnection implements IRCEventListener{
	
	public List<String> listChannelsJoined;
	private IrcServer ircServer;
	private String nickname;
	
	public DccBot(IrcServer ircServer, String botName, String botVersion) {
		super(  ircServer.getHostname(), 
				new int[]{Integer.parseInt(ircServer.getPort())}, 
				null, 
				ircServer.getNickname(), 
				ircServer.getNickname(), 
				ircServer.getNickname()
			 );
		this.listChannelsJoined = new ArrayList<String>();
		this.ircServer = ircServer;
		setEncoding("UTF-8");
		setPong(true);
		setColors(false);
		this.addIRCEventListener(this);
	}
	
	public void setNickname(String nickname){
		this.nickname = nickname;
	}
	
	public void log(String message) {
		log( new LogMessage("[" + getLogTime() + "]" + " " + message , LogMessage.LOG_COLOR_DCCBOT_MESSAGE)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
		
	
	private void log(LogMessage message) {
		NotifyManagerDccBotLogging.getNotifyManager().notify(ircServer, message);
	}
	
	//FIXME
	protected void onConnect() {
		ircServer.setConnected(true);
		List<IrcChannel> listIrcChannels = ircServer.getListChannels();
		for (IrcChannel ircChannel : listIrcChannels) {
			if(ircChannel.isAutoconnect()){
				//FIXME: joinChannel(ircChannel.getChannelName()); //TODO: Nicht ganz sauber, aber ircChannel.connect() kann nicht aufgerufen werden
				                                          //da IRC Server threadBotConnect noch laeuft
			}
		}
		NotifyManagerConnectedState.getNotifyManager().notify(ircServer);
	}
	
	//TODO: kick oder ban abfangen??
	
	public boolean isChannelJoined(String channel){
		//TODO: Was passiert wenn es den Channel nicht gibt??
		if( listChannelsJoined.contains(channel.toUpperCase()) ){
			return true;
		}
		return false;
	}
	
//	FIXME:
//	protected void onIncomingFileTransfer(DccFileTransfer transfer) {
//		DccDownload dccDownload = DccDownloadQueue.getInstance().getDccDownload(transfer);
//		transfer.receive(dccDownload.getDestinationFile(), true);
//		dccDownload.setDccFileTransfer(transfer);
//		dccDownload.start();
//	}
	
	private String getLogTime(){
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //$NON-NLS-1$
	    return sdf.format(cal.getTime());	
	}

	public void xdccSend(DccPacket dccPacket, File target) {
		//FIXME:
//		DccDownloadQueue downloadQueue = DccDownloadQueue.getInstance();
//		DccDownload dccDownload = new DccDownload(dccPacket, target);
//		if( downloadQueue.getDccDownload(dccDownload.getKey()) != null ){
//			sendCTCPCommand(dccPacket.getSender(), "xdcc send #" + dccPacket.getPacketNr());		 //$NON-NLS-1$
//			dccDownload = downloadQueue.getDccDownload(dccDownload.getKey());
//			dccDownload.setState(DccDownload.STATE_DOWNLOAD_WAITING);
//		}else{
//			downloadQueue.addToQueue(dccDownload);
//			sendCTCPCommand(dccPacket.getSender(), "xdcc send #" + dccPacket.getPacketNr());		 //$NON-NLS-1$
//			NotifyManagerDccDownload.getNotifyManager().notifyNewDccDownload(dccDownload);
//		}
	}
	
	public IrcServer getIrcServer() {
		return ircServer;
	}

	public void removeChannelFromJoinedChannelList(String channelName) {
		listChannelsJoined.remove(channelName.toUpperCase());
	}

	@Override
	public void onRegistered() {
		log("connected");
		onConnect();
	}

	@Override
	public void onDisconnected() {
		log("disconnect");
		ircServer.disconnect();
		NotifyManagerConnectedState.getNotifyManager().notify(ircServer);
	}

	@Override
	public void onError(String msg) {
		log(msg);
	}

	@Override
	public void onError(int num, String msg) {
		log(num + " " + msg);	
	}

	@Override
	public void onInvite(String chan, IRCUser user, String passiveNick) {
		log("onInvite: channel:" + chan + "nick: " + user.getNick() + "passiveNick:" + passiveNick);
	}

	@Override
	public void onJoin(String chan, IRCUser user) {
		log("join " + chan + " user: " + user);
		if( nickname.equals(user.getNick()) ){
			listChannelsJoined.add(chan.toUpperCase());
			NotifyManagerConnectedState.getNotifyManager().notify(ircServer.getChannelByName(chan));
		}
	}

	@Override
	public void onKick(String chan, IRCUser user, String passiveNick, String msg) {
		log("onKick" + user.getNick() + " " + msg);
		
	}

	@Override
	public void onMode(String chan, IRCUser user, IRCModeParser modeParser) {
		//nothing		
	}

	@Override
	public void onMode(IRCUser user, String passiveNick, String mode) {
		log(mode);		
	}

	@Override
	public void onNick(IRCUser user, String newNick) {
		log(newNick);		
	}

	@Override
	public void onNotice(String target, IRCUser user, String msg) {
		log(msg);		
	}

	@Override
	public void onPart(String chan, IRCUser user, String msg) {
		log(msg);
		if( nickname.equals(user.getNick()) ){
			removeChannelFromJoinedChannelList(chan);
			NotifyManagerConnectedState.getNotifyManager().notify(ircServer.getChannelByName(chan));
		}
	}

	@Override
	public void onPing(String ping) {
		log(ping);
		doPong(ping);
		
	}

	@Override
	public void onPrivmsg(String target, IRCUser user, String msg) {
		log(user.getNick() + msg);
		//TODO: Wenn sich ein bot deconnected dann dies in Tabelle anzeigen => onQuit
		if( DccMessageParser.isDccMessage(msg) ){
			DccPacket dccPacket = DccMessageParser.buildDccPacket(user.getNick(), msg);
			dccPacket.setDccBot(this);
			IrcChannel ircChannel = ircServer.getChannelByName(target);
			dccPacket.setIrcChannel(ircChannel);
			if( ! ircServer.containsPacket(dccPacket) ){
				ircServer.addDccPacket(dccPacket);
				ircChannel.addDccPacket(dccPacket);
				log(new LogMessage(dccPacket.getName(), LogMessage.LOG_COLOR_DCC_MESSAGE));
				NotifyManagerDccPacket.getNotifyManager().notifyNewPackage(dccPacket);
			}
		}else{
			log(new LogMessage("KEINE DCC MESSAGE: " + msg, LogMessage.LOG_COLOR_NO_DCC_MESSAGE)); //$NON-NLS-1$
		}		
		
	}

	@Override
	public void onQuit(IRCUser user, String msg) {
		log("Quit " + user.getNick() + msg);
	}

	@Override
	public void onReply(int num, String value, String msg) {
		log(value + " " + msg);
	}

	@Override
	public void onTopic(String chan, IRCUser user, String topic) {
		log(user.getNick() + " " + topic);
	}

	@Override
	public void unknown(String prefix, String command, String middle, String trailing) {
		log("unknown");
	}

}
