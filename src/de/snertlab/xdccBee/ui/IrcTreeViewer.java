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
package de.snertlab.xdccBee.ui;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

import de.snertlab.xdccBee.irc.IConnectedState;
import de.snertlab.xdccBee.irc.listener.INotifyConnectedState;
import de.snertlab.xdccBee.irc.listener.NotifyManagerConnectedState;
import de.snertlab.xdccBee.ui.provider.ContentLabelProviderTreeIrcServer;

/**
 * @author holgi
 *
 */
public class IrcTreeViewer extends TreeViewer implements INotifyConnectedState{

	/**
	 * @param parent
	 * @param style
	 */
	public IrcTreeViewer(Composite parent, int style) {
		super(parent, style);
		NotifyManagerConnectedState.getNotifyManager().register(this);
		ContentLabelProviderTreeIrcServer contentLabelProviderTreeIrcServer = new ContentLabelProviderTreeIrcServer(); 
		setContentProvider(contentLabelProviderTreeIrcServer);
		setLabelProvider(contentLabelProviderTreeIrcServer);
		setInput(Application.getServerSettings().getListServer());
		expandAll();
	}

	@Override
	public void notifyConnectedState(final IConnectedState obj) {
		this.getTree().getDisplay().asyncExec( new Runnable() {
			@Override
			public void run() {
				refresh(obj);
			}
		});
	}

}
