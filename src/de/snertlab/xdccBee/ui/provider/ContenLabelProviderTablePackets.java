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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import de.snertlab.xdccBee.irc.DccPacket;
import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.tools.AbstractTableColumnSorter;
import de.snertlab.xdccBee.tools.AbstractTableColumnSorterListener;
import de.snertlab.xdccBee.tools.AutoResizeTableLayout;

/**
 * @author snert
 * 
 */
public class ContenLabelProviderTablePackets implements
		IStructuredContentProvider, ITableLabelProvider {

	private static final String COLUMN_NAME_SIZE = XdccBeeMessages
			.getString("ContenLabelProviderTablePackets_COL_SIZE"); //$NON-NLS-1$
	private static final String COLUMN_NAME_NAME = XdccBeeMessages
			.getString("ContenLabelProviderTablePackets_COL_NAME"); //$NON-NLS-1$
	private static final String COLUMN_NAME_PACKETNR = XdccBeeMessages
			.getString("ContenLabelProviderTablePackets_COL_PACKETNR"); //$NON-NLS-1$
	private static final String COLUMN_NAME_SENDER = XdccBeeMessages
			.getString("ContenLabelProviderTablePackets_COL_SENDER"); //$NON-NLS-1$
	private static final String COLUMN_NAME_CHANNEL = XdccBeeMessages
			.getString("ContenLabelProviderTablePackets_COL_CHANNEL"); //$NON-NLS-1$
	private static final String COLUMN_NAME_SERVER = XdccBeeMessages
			.getString("ContenLabelProviderTablePackets_COL_SERVER"); //$NON-NLS-1$

	private TableViewer tableViewer;
	private Table table;
	private Map<String, DccPacket> mapDccPackets;

	public ContenLabelProviderTablePackets(TableViewer tableViewer) {
		this.mapDccPackets = new HashMap<String, DccPacket>();
		this.tableViewer = tableViewer;
		this.table = tableViewer.getTable();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object arg0) {
		List<DccPacket> listDccPackets = (List<DccPacket>) arg0;
		for (DccPacket dccPacket : listDccPackets) {
			mapDccPackets.put(dccPacket.toString(), dccPacket);
		}
		return mapDccPackets.values().toArray();
	}

	public void makeColumns() {
		MyTableColumnSorterListener myTableColumnSorterListener = new MyTableColumnSorterListener(
				tableViewer, false);
		makeColumn(COLUMN_NAME_SENDER, myTableColumnSorterListener);
		makeColumn(COLUMN_NAME_PACKETNR, myTableColumnSorterListener);
		makeColumn(COLUMN_NAME_NAME, myTableColumnSorterListener);
		makeColumn(COLUMN_NAME_SIZE, myTableColumnSorterListener);
		makeColumn(COLUMN_NAME_CHANNEL, myTableColumnSorterListener);
		makeColumn(COLUMN_NAME_SERVER, myTableColumnSorterListener);
		AutoResizeTableLayout autoTableLayout = new AutoResizeTableLayout(table);
		autoTableLayout.addColumnData(new ColumnWeightData(10)); // Get 15% of
																	// space
		autoTableLayout.addColumnData(new ColumnWeightData(10)); // Get 10% of
																	// space
		autoTableLayout.addColumnData(new ColumnWeightData(45)); // Get 55% of
																	// space
		autoTableLayout.addColumnData(new ColumnWeightData(5)); // Get 10% of
																// space
		autoTableLayout.addColumnData(new ColumnWeightData(15)); // Get 10% of
																	// space
		autoTableLayout.addColumnData(new ColumnWeightData(15)); // Get 10% of
																	// space
	}

	private void makeColumn(String name,
			AbstractTableColumnSorterListener tableColumnSorter) {
		TableColumn col = new TableColumn(table, SWT.NONE);
		// col.setAlignment(alignment);
		col.setText(name);
		col.setToolTipText(name);
		if (tableColumnSorter != null) {
			col.addListener(SWT.Selection, tableColumnSorter);
		}
	}

	public AbstractTableColumnSorterListener getSortListener() {
		Listener[] listener = table.getColumn(0).getListeners(SWT.Selection);
		if (listener.length <= 0)
			return null;
		return (AbstractTableColumnSorterListener) listener[0];
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
		DccPacket packet = (DccPacket) arg0;
		switch (arg1) {
		case 0:
			return " " + packet.getSender(); //$NON-NLS-1$
		case 1:
			return " #" + packet.getPacketNr(); //$NON-NLS-1$
		case 2:
			return " " + packet.getName(); //$NON-NLS-1$
		case 3:
			return " " + packet.getSize(); //$NON-NLS-1$
		case 4:
			return " " + packet.getChannelName(); //$NON-NLS-1$
		case 5:
			return " " + packet.getHostname(); //$NON-NLS-1$
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

	class MyTableColumnSorterListener extends AbstractTableColumnSorterListener {

		public MyTableColumnSorterListener(TableViewer tblViewer,
				boolean isMultipleColumnSort) {
			super(tblViewer, isMultipleColumnSort);
		}

		@Override
		protected AbstractTableColumnSorter getTableColumnSorterAbstract(
				LinkedHashMap<String, Integer> mapColumnsToSort) {
			return new MyTableColumnSorter(mapColumnsToSort);
		}

	}

	class MyTableColumnSorter extends AbstractTableColumnSorter {
		public MyTableColumnSorter(
				LinkedHashMap<String, Integer> mapColumnsToSort) {
			super(mapColumnsToSort);
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			DccPacket kunde1 = ((DccPacket) e1);
			DccPacket kunde2 = ((DccPacket) e2);
			int returnValue = 0;
			for (Entry<String, Integer> entry : mapColumnsToSort.entrySet()) {
				String column = entry.getKey();
				int direction = entry.getValue();
				int i = 0;
				i++;
				// TODO: Sortierung noch pruefen
				if (COLUMN_NAME_SENDER.equals(column)) {
					returnValue = kunde1.getSender().compareTo(
							kunde2.getSender());
				} else if (COLUMN_NAME_PACKETNR.equals(column)) {
					if (kunde1.getPacketNr() > kunde2.getPacketNr()) {
						returnValue = 1;
					} else if (kunde1.getPacketNr() < kunde2.getPacketNr()) {
						returnValue = -1;
					} else {
						returnValue = 0;
					}
				} else if (COLUMN_NAME_NAME.equals(column)) {
					returnValue = kunde1.getName().compareTo(kunde2.getName());
				} else if (COLUMN_NAME_SIZE.equals(column)) {
					returnValue = kunde1.getSize().compareTo(kunde2.getSize());
				} else if (COLUMN_NAME_CHANNEL.equals(column)) {
					returnValue = kunde1.getChannelName().compareTo(
							kunde2.getChannelName());
				} else if (COLUMN_NAME_SERVER.equals(column)) {
					returnValue = kunde1.getHostname().compareTo(
							kunde2.getHostname());
				} else {
					throw new RuntimeException("Column not defined " + column); //$NON-NLS-1$
				}
				if (direction == SWT.DOWN) {
					returnValue = returnValue * -1;
				}
				if (returnValue != 0)
					break;
			}
			return returnValue;
		}
	}

	/**
	 * @param element
	 */
	public void add(DccPacket element) {
		mapDccPackets.put(element.toString(), element);
	}

}
