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
package de.snertlab.xdccBee.tools;

import java.util.LinkedHashMap;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author snert
 * 
 */
public abstract class AbstractTableColumnSorterListener implements Listener {
	private Table table;
	private TableViewer tblViewer;
	private LinkedHashMap<String, Integer> mapColumnsToSort;
	private boolean isMultipleColumnSort;

	public AbstractTableColumnSorterListener(TableViewer tblViewer,
			boolean isMultipleColumnSort) {
		this.tblViewer = tblViewer;
		this.table = tblViewer.getTable();
		this.isMultipleColumnSort = isMultipleColumnSort;
		this.mapColumnsToSort = new LinkedHashMap<String, Integer>();
	}

	public void handleEvent(Event e) {
		TableColumn sortColumn = table.getSortColumn();
		TableColumn currentColumn = (TableColumn) e.widget;
		String columnText = currentColumn.getText();
		int dir = table.getSortDirection();
		if (sortColumn == currentColumn) {
			dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
		} else {
			table.setSortColumn(currentColumn);
			dir = SWT.UP;
		}
		if (!isMultipleColumnSort)
			clearSort();
		mapColumnsToSort.put(columnText, dir);
		table.setSortDirection(dir);
		tblViewer.setSorter(getTableColumnSorterAbstract(mapColumnsToSort));
		setScrollPosToSelectedItem();
	}

	protected abstract AbstractTableColumnSorter getTableColumnSorterAbstract(
			LinkedHashMap<String, Integer> mapColumnsToSort);

	private void setScrollPosToSelectedItem() {
		if (table.getSelection().length <= 0)
			return;
		TableItem tblItem = table.getSelection()[0];
		table.showItem(tblItem);
		// table.setTopIndex( table.indexOf(tblItem) );
	}

	public void clearSort() {
		table.setSortDirection(SWT.NONE);
		mapColumnsToSort.clear();
	}

}
