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
package de.snertlab.xdccBee.ui.provider;

import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

import de.snertlab.xdccBee.irc.IrcChannel;
import de.snertlab.xdccBee.irc.IrcServer;
import de.snertlab.xdccBee.ui.Images;

/**
 * @author snert
 * 
 */
public class ContentLabelProviderTreeIrcServer implements ITreeContentProvider,
		ILabelProvider {

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getChildren(Object arg0) {
		if (arg0 instanceof List<?>) {
			return ((List<IrcServer>) arg0).toArray();
		} else if (arg0 instanceof IrcServer) {
			return ((IrcServer) arg0).getListChannels().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object arg0) {
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {
		if (arg0 instanceof IrcServer) {
			if (!((IrcServer) arg0).getListChannels().isEmpty())
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object arg0) {
		return ((List<IrcServer>) arg0).toArray();
	}

	@Override
	public void dispose() {
		// nothing
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// nothing
	}

	@Override
	public Image getImage(Object arg0) {
		boolean isConnected = false;
		if (arg0 instanceof IrcServer) {
			isConnected = ((IrcServer) arg0).isConnected();
		} else if (arg0 instanceof IrcChannel) {
			isConnected = ((IrcChannel) arg0).isConnected();
		} else {
			throw new RuntimeException("Undefined instance"); //$NON-NLS-1$
		}
		return isConnected ? Images.TREE_CONNECTED : Images.TREE_DISCONNECTED;
	}

	@Override
	public String getText(Object arg0) {
		if (arg0 instanceof IrcServer) {
			return ((IrcServer) arg0).getHostname();
		} else if (arg0 instanceof IrcChannel) {
			return ((IrcChannel) arg0).getChannelName();
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
		// nothing
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// nothing
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// nothing
	}

}
