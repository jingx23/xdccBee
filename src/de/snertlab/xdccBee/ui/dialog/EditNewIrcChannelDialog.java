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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.snertlab.xdccBee.irc.IrcChannel;
import de.snertlab.xdccBee.irc.IrcServer;
import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.tools.AbstractSaveDialog;

/**
 * @author snert
 * 
 */
public class EditNewIrcChannelDialog extends AbstractSaveDialog {

	private static final int DIALOG_WIDTH = 220;
	private static final int DIALOG_HEIGHT = 55;

	private IrcChannel ircChannel;
	private IrcServer ircServer;
	private Text txtChannelName;
	private Button btnAutoconnect;
	private boolean isNew;

	public EditNewIrcChannelDialog(Shell parentShell, IrcChannel ircChannel,
			boolean isNew) {
		super(parentShell);
		this.isNew = isNew;
		this.ircChannel = ircChannel;
		this.ircServer = ircChannel.getIrcServer();
	}

	@Override
	protected String checkFields() {
		String msg = ""; //$NON-NLS-1$
		if (StringUtils.isEmpty(txtChannelName.getText())) {
			msg = msg
					+ XdccBeeMessages
							.getString("EditNewIrcChannelDialog_CHECK_ERROR_EMPTY_NAME") + "\n"; //$NON-NLS-1$//$NON-NLS-2$
		} else if (isNew
				&& ircServer.containsIrcChannel(txtChannelName.getText())) {
			msg = msg
					+ XdccBeeMessages
							.getString("EditNewIrcChannelDialog_CHECK_ERROR_CHANNEL_EXIST") + "\n"; //$NON-NLS-1$//$NON-NLS-2$
		}
		return msg;
	}

	@Override
	protected void initFields() {
		txtChannelName.setText(StringUtils.defaultString(ircChannel
				.getChannelName()));
		btnAutoconnect.setSelection(ircChannel.isAutoconnect());
	}

	@Override
	protected void makeComponents(Composite parent) {
		int numCols = 2;
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(numCols, false));
		GridData gridDataComp = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridDataComp.widthHint = DIALOG_WIDTH;
		gridDataComp.heightHint = DIALOG_HEIGHT;
		comp.setLayoutData(gridDataComp);

		Label lblChannelName = new Label(comp, SWT.NONE);
		lblChannelName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		lblChannelName.setText(XdccBeeMessages
				.getString("EditNewIrcChannelDialog_LABEL_NAME_NAME")); //$NON-NLS-1$

		txtChannelName = new Text(comp, SWT.BORDER);
		txtChannelName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		Label lblAutoconnect = new Label(comp, SWT.NONE);
		lblAutoconnect.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		lblAutoconnect.setText(XdccBeeMessages
				.getString("EditNewIrcChannelDialog_LABEL_NAME_AUTOCONNECT")); //$NON-NLS-1$

		btnAutoconnect = new Button(comp, SWT.CHECK);
		btnAutoconnect.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				false, numCols - 1, 0));
		if (!ircServer.isAutoconnect()) {
			btnAutoconnect.setEnabled(false);
		}
	}

	@Override
	protected void myConfigureShell(Shell newShell) {
		if (isNew) {
			newShell.setText(XdccBeeMessages
					.getString("EditNewIrcChannelDialog_LABEL_NAME_NEW_IRC_CHANNEL")); //$NON-NLS-1$
		} else {
			newShell.setText(XdccBeeMessages
					.getString("EditNewIrcChannelDialog_LABEL_NAME_EDIT_IRC_CHANNEL")); //$NON-NLS-1$
		}
	}

	@Override
	protected void writeFields() {
		String channel = txtChannelName.getText();
		if (!channel.startsWith("#")) {
			channel = "#" + channel;
		}
		ircChannel.setChannelName(channel);
		ircChannel.setAutoconnect(btnAutoconnect.getSelection());
	}

}
