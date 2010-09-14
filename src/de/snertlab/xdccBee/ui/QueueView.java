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

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import de.snertlab.xdccBee.irc.DccDownload;
import de.snertlab.xdccBee.irc.listener.INotifyDccDownload;
import de.snertlab.xdccBee.irc.listener.NotifyManagerDccDownload;
import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.tools.AutoResizeTableLayout;

/**
 * @author snert
 *
 */
public class QueueView extends Composite implements INotifyDccDownload {
	
	private TableViewer tblViewerDownloadQueue;
	
	public QueueView(Composite parent){
		super(parent, SWT.NONE);
		NotifyManagerDccDownload.getNotifyManager().register(this);
		createContentView();
		makeTableMenu();
	}
	
	private void createContentView(){
		Composite compQueue = this;
		compQueue.setLayout(new GridLayout());
		compQueue.setLayoutData( new GridData(SWT.FILL, SWT.FILL, true, true) );
		tblViewerDownloadQueue = new TableViewer(compQueue, SWT.BORDER | SWT.FULL_SELECTION);
		Table tblDownloadQueue = tblViewerDownloadQueue.getTable();
		AutoResizeTableLayout autoTableLayout = new AutoResizeTableLayout(tblDownloadQueue);
		autoTableLayout.addColumnData(new ColumnWeightData(10));
		autoTableLayout.addColumnData(new ColumnWeightData(30));
		autoTableLayout.addColumnData(new ColumnWeightData(20));
		autoTableLayout.addColumnData(new ColumnWeightData(30));
		autoTableLayout.addColumnData(new ColumnWeightData(10));
		autoTableLayout.addColumnData(new ColumnWeightData(10));
		TableColumn column0 = new TableColumn(tblDownloadQueue, SWT.NONE);
		TableColumn column1 = new TableColumn(tblDownloadQueue, SWT.NONE);
		TableColumn column2 = new TableColumn(tblDownloadQueue, SWT.NONE);
		TableColumn column3 = new TableColumn(tblDownloadQueue, SWT.NONE);
		TableColumn column4 = new TableColumn(tblDownloadQueue, SWT.NONE);
		TableColumn column5 = new TableColumn(tblDownloadQueue, SWT.NONE);
		column0.setText(XdccBeeMessages.getString("TABLE_DOWNLOADS_COL_BOT")); //$NON-NLS-1$
		column1.setText(XdccBeeMessages.getString("TABLE_DOWNLOADS_COL_PACKET")); //$NON-NLS-1$
		column2.setText(XdccBeeMessages.getString("TABLE_DOWNLOADS_COL_PROGRESS")); //$NON-NLS-1$
		column3.setText(XdccBeeMessages.getString("TABLE_DOWNLOADS_COL_DOWNLOADRATE"));//$NON-NLS-1$
		column4.setText(XdccBeeMessages.getString("TABLE_DOWNLOADS_COL_DURATION"));//$NON-NLS-1$
		column5.setText(XdccBeeMessages.getString("TABLE_DOWNLOADS_COL_STATE"));//$NON-NLS-1$
		tblDownloadQueue.setLayoutData( new GridData(SWT.FILL, SWT.FILL, true, true) );
		tblDownloadQueue.setHeaderVisible(true);
		tblDownloadQueue.setLinesVisible(true);
	}
	
	@Override
	public void notifyNewDccDownload(final DccDownload dccDownload) {
		new TableItemDownload(tblViewerDownloadQueue.getTable(), dccDownload);
	}
	
	private void makeTableMenu(){
		Menu mnuIrcServer = new Menu(tblViewerDownloadQueue.getTable());
		tblViewerDownloadQueue.getTable().setMenu(mnuIrcServer);

		final MenuItem mntmAbortDownload = new MenuItem(mnuIrcServer, SWT.CASCADE);
		mntmAbortDownload.setText(XdccBeeMessages.getString("TABLE_DOWNLOADS_MNTM_ABORT_DOWNLOAD")); //$NON-NLS-1$
		mntmAbortDownload.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getSelectedDccDownload().stop();
			}
		});

	}
	
	private DccDownload getSelectedDccDownload(){
		IStructuredSelection sel = (IStructuredSelection) tblViewerDownloadQueue.getSelection();
		return (DccDownload) sel.getFirstElement();
	}

}
