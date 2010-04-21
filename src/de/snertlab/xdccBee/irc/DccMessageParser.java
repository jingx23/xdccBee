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

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * @author snert
 *
 */
public class DccMessageParser {

	//[^\w]?#([0-9]*).*?[0-9]+[xX]{1}.*?[:space:]?\[?([:space:]?[0-9]+[\.,]?[0-9]*[ ]*[a-zA-ZA-Za-z]*?)\]?[^\w]{1}(.*)
	private static final Pattern DCC_MESSAGE_PATTER = Pattern.compile("[^\\w]?#" + //$NON-NLS-1$
																	  "([0-9]*)" + //Packet Nummer group(1) //$NON-NLS-1$
																	  ".*?[0-9]+[xX]{1}.*?[:space:]?\\[?" + //$NON-NLS-1$
																	  "([:space:]?[0-9]+[\\.,]?[0-9]*[ ]*[a-zA-ZA-Za-z]*?)" + //Packet Groeße group(2) //$NON-NLS-1$
																	  "\\]?[^\\w]{1}" + //$NON-NLS-1$
																	  "(.*)" //Packet Name group(3) //$NON-NLS-1$
																	 );
	
	/* Dcc Message Examples:
	 * #1 97x [ 42K] [CAT]_Releases.txt
	 * #2 22x [123M] [CAT]_Cooking_fish_howto_video.avi
	 * #3 14x [123M] [CAT]_Meow_Mix_advertisement.avi
	 */
	
	public static boolean isDccMessage(String message){
		Matcher m = DCC_MESSAGE_PATTER.matcher(message);
		if (m.find()) {
			return true;
		}
		return false;
	}

	public static DccPacket buildDccPacket(String sender, String dccMessage){
		try {
			DccPacket dccPacket = new DccPacket();
			dccPacket.setSender(sender);
		
			String s = new String(dccMessage.getBytes(), "UTF8"); //$NON-NLS-1$
			Matcher m = DCC_MESSAGE_PATTER.matcher(s);
			if(m.find()){
				dccPacket.setPacketNr( Integer.parseInt( StringUtils.trim( m.group(1) ) ) );
				dccPacket.setSize( StringUtils.trim( m.group(2) ) );
				String packetName 	= m.group(3);
				packetName 			= delete_control_codes(packetName);
				dccPacket.setName( packetName );
			}
			return dccPacket;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* Delete any color codes, font modes from the given str
	* These control characters are based on the unofficial mirc implementation
	* Returns clean version of message
	* \002 => Bold
	* \003[??][,??] => Forground color, background color
	* \x0F => Switch back to plain mode
	* \x16 => Reverse mode
	* 0x1F => Underline
	* thanks to Ruby xdccfetch
	*/
	private static String delete_control_codes(String packetName){
		String s = packetName;
		s = s.replaceAll("\\002|\\003(\\d{1,2})?(,\\d{1,2})?|\\x0F|\\x16|\\x1F", "");
		s = StringUtils.trim(s);
		return s;
	}
	
}
