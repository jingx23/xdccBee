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

import java.util.ArrayList;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import de.snertlab.xdccBee.irc.DccPacket;
import de.snertlab.xdccBee.irc.IrcChannel;
import de.snertlab.xdccBee.irc.IrcServer;
import de.snertlab.xdccBee.irc.listener.INotifyDccPacket;
import de.snertlab.xdccBee.irc.listener.NotifyManagerDccPacket;
import de.snertlab.xdccBee.ui.actions.ActionDownloadFile;
import de.snertlab.xdccBee.ui.filter.FileFilter;
import de.snertlab.xdccBee.ui.provider.ContenLabelProviderTablePackets;

/**
 * @author holgi
 *
 */
public class FileViewer extends TableViewer implements INotifyDccPacket {

	private FileFilter fileFilter = new FileFilter();
	
	public FileViewer(Composite parent, int style) {
		super(parent, style);
		NotifyManagerDccPacket.getNotifyManager().register(this);
		Table tblPackets = getTable();
		ContenLabelProviderTablePackets contenLabelProviderTablePackets = new ContenLabelProviderTablePackets(this);
		setContentProvider(contenLabelProviderTablePackets);
		setLabelProvider(contenLabelProviderTablePackets);
		tblPackets.setHeaderVisible(true);
		tblPackets.setLinesVisible(true);
		contenLabelProviderTablePackets.makeColumns();
		setInput(new ArrayList<DccPacket>());
		new FileViewerMenu(this);
		addFilter(fileFilter);
		addDoubleClickListener( new IDoubleClickListener() {			
			@Override
			public void doubleClick(DoubleClickEvent arg0) {
				new ActionDownloadFile().run();
			}
		});
	}
	
	public DccPacket getSelectedDccPacket() {
		IStructuredSelection selectedItem = (IStructuredSelection) getSelection();
		return (DccPacket)selectedItem.getFirstElement();
	}

	public void setFileNameFilterText(String filterText) {
		fileFilter.setFileName(filterText);
		refresh();
	}
	
	public void setFilterIrcChannel(IrcChannel ircChannel){
		fileFilter.setChannelName(ircChannel.getChannelName());
		refresh();
	}
	
	public void setFilterIrcServer(IrcServer ircServer){
		fileFilter.setHostname(ircServer.getHostname());
		refresh();
	}
	
	public void setFilterAll(){
		fileFilter.setAll();
		refresh();
	}
	
	@Override
	public void notifyDccPacket(final DccPacket dccPacket) {
		getTable().getDisplay().asyncExec( new Runnable() {			
			@Override
			public void run() {
				add(dccPacket);
				refresh();
			}
		});
	}
	
	@Override
	public void add(Object element) {
		((ContenLabelProviderTablePackets) getContentProvider()).add((DccPacket)element);
	}

	public void setFilterIgnoreCase(boolean ignoreCase) {
		fileFilter.setFilterIgnoreCase(ignoreCase);
	}

	public void setFilterRegExp(boolean regExp) {
		fileFilter.setFilterRegExp(regExp);
	}
}
