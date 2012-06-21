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
package de.snertlab.xdccBee.irc.listener;

import java.util.ArrayList;
import java.util.List;

import de.snertlab.xdccBee.irc.DccPacket;

/**
 * @author snert
 * 
 */
public class NotifyManagerDccPacket {

	private static NotifyManagerDccPacket notifyManager;
	private List<INotifyDccPacket> listNotifier;

	public static NotifyManagerDccPacket getNotifyManager() {
		if (notifyManager == null) {
			notifyManager = new NotifyManagerDccPacket();
		}
		return notifyManager;
	}

	public NotifyManagerDccPacket() {
		this.listNotifier = new ArrayList<INotifyDccPacket>();
	}

	public void register(INotifyDccPacket notifyDccPacket) {
		listNotifier.add(notifyDccPacket);
	}

	public void notifyNewPackage(DccPacket dccPacket) {
		for (INotifyDccPacket notifyDccPacket : listNotifier) {
			notifyDccPacket.notifyDccPacket(dccPacket);
		}
	}

}
