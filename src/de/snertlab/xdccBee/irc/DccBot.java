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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;

import de.snertlab.xdccBee.irc.listener.LogMessage;
import de.snertlab.xdccBee.irc.listener.NotifyManagerConnectedState;
import de.snertlab.xdccBee.irc.listener.NotifyManagerDccBotLogging;
import de.snertlab.xdccBee.irc.listener.NotifyManagerDccDownload;
import de.snertlab.xdccBee.irc.listener.NotifyManagerDccPacket;

/**
 * @author snert
 *
 */
public class DccBot extends IRCConnection implements IRCEventListener{
	
	private static final String DCC_SEND_LEADING = "DCC SEND ";
	
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
		setEncoding("ISO-8859-1");
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
	
	protected void onConnect() {
		ircServer.setConnected(true);
		List<IrcChannel> listIrcChannels = ircServer.getListChannels();
		for (IrcChannel ircChannel : listIrcChannels) {
			if(ircChannel.isAutoconnect()){
				doJoin(ircChannel.getChannelName());  //TODO: Nicht ganz sauber, aber ircChannel.connect() kann nicht aufgerufen werden
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
		
	private String getLogTime(){
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //$NON-NLS-1$
	    return sdf.format(cal.getTime());	
	}

	public void xdccSend(DccPacket dccPacket, String downloadDirFilename) {
		DccDownloadQueue downloadQueue = DccDownloadQueue.getInstance();
		DccDownload dccDownload = new DccDownload(dccPacket, downloadDirFilename);
		if( downloadQueue.getDccDownload(dccDownload.getKey()) != null ){
			doPrivmsg(dccPacket.getSender(), "xdcc send #" + dccPacket.getPacketNr());
			dccDownload = downloadQueue.getDccDownload(dccDownload.getKey());
			dccDownload.setState(DccDownload.STATE_DOWNLOAD_WAITING);
		}else{
			downloadQueue.addToQueue(dccDownload);
			doPrivmsg(dccPacket.getSender(), "xdcc send #" + dccPacket.getPacketNr());
			NotifyManagerDccDownload.getNotifyManager().notifyNewDccDownload(dccDownload);
		}
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
		String nick = user.getNick();
		log(nick + msg);
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
		}else if(validDccSendMessage(nick, msg)){
			DccFileTransfer dccFileTransfer = parseDccFileTransfer(nick, msg);
			DccDownload dccDownload = DccDownloadQueue.getInstance().getDccDownload(dccFileTransfer);
			dccDownload.setDccFileTransfer(dccFileTransfer);
			dccFileTransfer.start(dccDownload.getDownloadDirFilename());
			dccDownload.start();
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

	private DccFileTransfer parseDccFileTransfer(String sender, String msg) {
	    try {
		    String tmpfile;
	    	StringTokenizer st = new StringTokenizer(msg);
	    	st.nextToken();
	    	st.nextToken();
	    	tmpfile = st.nextToken();
	    	if (tmpfile.charAt(0) == '\"') {
	    		do {
	    			tmpfile += " "+ st.nextToken();
		        } while (tmpfile.charAt(tmpfile.length() - 1) != '\"');
		        tmpfile = tmpfile.substring(1, tmpfile.length() - 1);
	    	}
	    	String tmphost = st.nextToken();
	    	if (tmphost.charAt(0) == '\"'){
	    		tmphost = tmphost.substring(1);
	    	}
	    	if (tmphost.charAt(tmphost.length() - 1) == '\"'){
	    		tmphost = tmphost.substring(0, tmphost.length() - 1);
	    	}
	    	String xhost = getInetAddress(Long.parseLong(tmphost)).getHostAddress();
	    	int xport = Integer.parseInt(st.nextToken());
	    	long xsize = Long.parseLong(st.nextToken());
	    	DccFileTransfer dccFileTransfer = new DccFileTransfer(xhost, xport, xsize, sender, tmpfile);
	    	return dccFileTransfer;
	    } catch (Exception exc) {
	    	throw new RuntimeException(exc);
	    }
	}
	
	private boolean validDccSendMessage(String sender, String msg){
	    if (msg.length() <= DCC_SEND_LEADING.length()){
	    	return false;
	    }
	    if (!msg.substring(0, DCC_SEND_LEADING.length()).equalsIgnoreCase(DCC_SEND_LEADING)){
	    	return false;	
	    }
	    return true;
	}
	
	private static InetAddress getInetAddress(long address) throws UnknownHostException {
		byte[] addr = new byte[4];
		addr[0] = (byte)((address >>> 24) & 0xFF);
		addr[1] = (byte)((address >>> 16) & 0xFF);
		addr[2] = (byte)((address >>> 8) & 0xFF);
		addr[3] = (byte)(address & 0xFF);
		return InetAddress.getByAddress(addr);
	}
}
