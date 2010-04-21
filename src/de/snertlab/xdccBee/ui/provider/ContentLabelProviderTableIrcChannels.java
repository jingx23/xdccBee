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

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import de.snertlab.xdccBee.irc.IrcChannel;
import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.tools.AutoResizeTableLayout;

/**
 * @author snert
 *
 */
public class ContentLabelProviderTableIrcChannels implements IStructuredContentProvider, ITableLabelProvider {

	private Table table;
	
	public ContentLabelProviderTableIrcChannels(Table table) {
		this.table = table;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object arg0) {
		List<IrcChannel> listIrcChannels = (List<IrcChannel>) arg0;
		return listIrcChannels.toArray();
	}

	public void makeColumns(){
		makeColumn(XdccBeeMessages.getString("ContentLabelProviderTableIrcChannels_COL_NAME")); //$NON-NLS-1$
		AutoResizeTableLayout autoTableLayout = new AutoResizeTableLayout(table);
		autoTableLayout.addColumnData(new ColumnWeightData(1));
	}
	
	private void makeColumn(String name){
		TableColumn col = new TableColumn(table, SWT.NONE);
//		col.setAlignment(alignment);		
		col.setText(name);
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	@Override
	public String getColumnText(Object arg0, int arg1) {
		IrcChannel ircChannel = (IrcChannel) arg0;
		switch (arg1) {
			case 0:
				return " " + ircChannel.getChannelName(); //$NON-NLS-1$
			default:
				throw new RuntimeException("columnIndex: " + arg1 + " not defined"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {		
	}
}
