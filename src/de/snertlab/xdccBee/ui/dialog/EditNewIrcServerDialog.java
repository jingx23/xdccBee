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
package de.snertlab.xdccBee.ui.dialog;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import de.snertlab.xdccBee.irc.IrcChannel;
import de.snertlab.xdccBee.irc.IrcServer;
import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.tools.AbstractSaveDialog;
import de.snertlab.xdccBee.ui.actions.ActionNewIrcChannel;
import de.snertlab.xdccBee.ui.actions.ActionRemoveIrcChannel;
import de.snertlab.xdccBee.ui.provider.ContentLabelProviderTableIrcChannels;

/**
 * @author snert
 * 
 */
public class EditNewIrcServerDialog extends AbstractSaveDialog {

	private static final int DIALOG_WIDTH = 500;
	private static final int DIALOG_HEIGHT = 350;

	private boolean isNew;
	private IrcServer ircServer;

	private Text txtHostname;
	private Text txtPort;
	private Text txtNickname;
	private TableViewer tblViewerChannels;
	private Button btnDebugMode;
	private Button btnAutoconnect;

	public EditNewIrcServerDialog(Shell parentShell, IrcServer ircServer,
			boolean isNew) {
		super(parentShell, true);
		this.ircServer = ircServer;
		this.isNew = isNew;
	}

	@Override
	protected String checkFields() {
		String msg = ""; //$NON-NLS-1$
		if (StringUtils.isEmpty(txtHostname.getText())) {
			msg = msg
					+ XdccBeeMessages
							.getString("EditNewIrcServerDialog_CHECK_ERROR_HOSTNAME_EMPTY") + "\n"; //$NON-NLS-1$//$NON-NLS-2$
		}
		if (StringUtils.isEmpty(txtPort.getText())) {
			msg = msg
					+ XdccBeeMessages
							.getString("EditNewIrcServerDialog_CHECK_ERROR_PORT_EMPTY") + "\n"; //$NON-NLS-1$//$NON-NLS-2$
		} else if (!StringUtils.isNumeric(txtPort.getText())) {
			msg = msg
					+ XdccBeeMessages
							.getString("EditNewIrcServerDialog_CHECK_ERROR_PORT_INTEGER") + "\n"; //$NON-NLS-1$//$NON-NLS-2$
		}
		return msg;

	}

	@Override
	protected void initFields() {
		txtHostname.setText(StringUtils.defaultString(ircServer.getHostname()));
		txtPort.setText(StringUtils.defaultString(ircServer.getPort()));
		txtNickname.setText(StringUtils.defaultString(ircServer.getNickname()));
		btnDebugMode.setSelection(ircServer.isDebug());
		if (isNew) {
			btnDebugMode.setSelection(true);
		}
		btnAutoconnect.setSelection(ircServer.isAutoconnect());
		fillTblChannels();
	}

	private void fillTblChannels() {
		tblViewerChannels.setInput(ircServer.getListChannels());
	}

	@Override
	protected void makeComponents(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		GridData gridDataComp = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridDataComp.widthHint = DIALOG_WIDTH;
		gridDataComp.heightHint = DIALOG_HEIGHT;
		comp.setLayoutData(gridDataComp);

		makeCompositeServerPreferences(comp);
		makeCompositeChannels(comp);
	}

	private void makeCompositeServerPreferences(Composite parent) {
		int numCols = 4;
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(numCols, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label lblHostname = new Label(comp, SWT.NONE);
		lblHostname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		lblHostname.setText(XdccBeeMessages
				.getString("EditNewIrcServerDialog_LABEL_NAME_HOSTNAME")); //$NON-NLS-1$

		txtHostname = new Text(comp, SWT.BORDER);
		txtHostname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		Label lblPort = new Label(comp, SWT.NONE);
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		lblPort.setText(XdccBeeMessages
				.getString("EditNewIrcServerDialog_LABEL_NAME_PORT")); //$NON-NLS-1$

		txtPort = new Text(comp, SWT.BORDER);
		GridData gridDataPort = new GridData(SWT.FILL, SWT.CENTER, false, false);
		gridDataPort.widthHint = 50;
		txtPort.setLayoutData(gridDataPort);

		Label lblNickname = new Label(comp, SWT.NONE);
		lblNickname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		lblNickname.setText(XdccBeeMessages
				.getString("EditNewIrcServerDialog_LABEL_NAME_NICKNAME")); //$NON-NLS-1$

		txtNickname = new Text(comp, SWT.BORDER);
		txtNickname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, numCols - 1, 0));

		Label lblAutoconnect = new Label(comp, SWT.NONE);
		lblAutoconnect.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		lblAutoconnect.setText(XdccBeeMessages
				.getString("EditNewIrcServerDialog_LABEL_NAME_AUTOCONNECT")); //$NON-NLS-1$

		btnAutoconnect = new Button(comp, SWT.CHECK);
		btnAutoconnect.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, numCols - 1, 0));
		btnAutoconnect.setText(""); //$NON-NLS-1$

		Label lblDebugMode = new Label(comp, SWT.NONE);
		lblDebugMode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		lblDebugMode.setText(XdccBeeMessages
				.getString("EditNewIrcServerDialog_LABEL_NAME_DEBUG_MODE")); //$NON-NLS-1$

		btnDebugMode = new Button(comp, SWT.CHECK);
		btnDebugMode.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, numCols - 1, 0));
		btnDebugMode
				.setText(XdccBeeMessages
						.getString("EditNewIrcServerDialog_LABEL_NAME_DEBUG_MODE_DESC")); //$NON-NLS-1$

	}

	private void makeCompositeChannels(Composite parent) {
		int numCols = 2;
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(numCols, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label lblTblChannels = new Label(comp, SWT.NONE);
		lblTblChannels.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, numCols, 0));
		lblTblChannels.setText(XdccBeeMessages
				.getString("EditNewIrcServerDialog_LABEL_NAME_IRC_CHANNELS")); //$NON-NLS-1$

		Composite compTblChannel = new Composite(comp, SWT.NONE);
		compTblChannel.setLayout(new GridLayout());
		compTblChannel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true));

		tblViewerChannels = new TableViewer(compTblChannel, SWT.BORDER);
		Table tblChannels = tblViewerChannels.getTable();
		ContentLabelProviderTableIrcChannels contentLabelProviderTableIrcChannels = new ContentLabelProviderTableIrcChannels(
				tblChannels);
		tblViewerChannels
				.setContentProvider(contentLabelProviderTableIrcChannels);
		tblViewerChannels
				.setLabelProvider(contentLabelProviderTableIrcChannels);
		tblChannels.setHeaderVisible(true);
		tblChannels.setLinesVisible(true);
		GridData gridDataTblChannels = new GridData(SWT.FILL, SWT.FILL, true,
				true);
		tblChannels.setLayoutData(gridDataTblChannels);
		contentLabelProviderTableIrcChannels.makeColumns();

		Composite compChannelButtons = new Composite(comp, SWT.NONE);
		compChannelButtons.setLayout(new GridLayout());
		compChannelButtons.setLayoutData(new GridData(SWT.LEFT, SWT.FILL,
				false, true));

		Button btnNewChannel = new Button(compChannelButtons, SWT.NONE);
		btnNewChannel
				.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		btnNewChannel.setText(XdccBeeMessages
				.getString("EditNewIrcServerDialog_BUTTON_NAME_NEW")); //$NON-NLS-1$
		btnNewChannel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				callActionNewIrcChannel();
			}
		});

		Button btnRemoveChannel = new Button(compChannelButtons, SWT.NONE);
		btnRemoveChannel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false));
		btnRemoveChannel.setText(XdccBeeMessages
				.getString("EditNewIrcServerDialog_BUTTON_NAME_REMOVE")); //$NON-NLS-1$
		btnRemoveChannel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				callActionRemoveIrcChannel();
			}
		});
	}

	@Override
	protected void myConfigureShell(Shell newShell) {
		if (isNew) {
			newShell.setText(XdccBeeMessages
					.getString("EditNewIrcServerDialog_TITLE_NEW")); //$NON-NLS-1$
		} else {
			newShell.setText(XdccBeeMessages
					.getString("EditNewIrcServerDialog_TITLE_EDIT")); //$NON-NLS-1$
		}
	}

	@Override
	protected void writeFields() {
		ircServer.setHostname(txtHostname.getText());
		ircServer.setPort(txtPort.getText());
		ircServer.setNickname(txtNickname.getText());
		ircServer.setDebug(btnDebugMode.getSelection());
		ircServer.setAutoconnect(btnAutoconnect.getSelection());
		if (!ircServer.isAutoconnect()) {
			for (IrcChannel ircChannel : ircServer.getListChannels()) {
				ircChannel.setAutoconnect(false);
			}
		}
	}

	private void callActionNewIrcChannel() {
		new ActionNewIrcChannel(getShell(), ircServer).run();
		fillTblChannels();
	}

	private void callActionRemoveIrcChannel() {
		if (getSelectedIrcChannel() == null)
			return;
		new ActionRemoveIrcChannel(getSelectedIrcChannel()).run();
		fillTblChannels();
	}

	private IrcChannel getSelectedIrcChannel() {
		IStructuredSelection selection = (IStructuredSelection) tblViewerChannels
				.getSelection();
		return (IrcChannel) selection.getFirstElement();
	}

}
