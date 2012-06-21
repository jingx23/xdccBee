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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author snert
 * 
 */
public abstract class AbstractSaveDialog extends Dialog {

	public AbstractSaveDialog(Shell parentShell, boolean resizable) {
		super(parentShell);
		if (resizable) {
			setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER
					| SWT.APPLICATION_MODAL | SWT.RESIZE);
		}
	}

	public AbstractSaveDialog(Shell parentShell) {
		this(parentShell, false);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Speichern", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "Abbrechen", true);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		myConfigureShell(newShell);
	}

	protected abstract void myConfigureShell(Shell newShell);

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			String msg = checkFields();
			if (StringUtils.isEmpty(msg)) {
				writeFields();
				super.buttonPressed(buttonId);
			} else {
				MessageDialog.openWarning(getShell(), "Fehler", msg);
			}
		} else {
			super.buttonPressed(buttonId);
		}
	}

	protected abstract String checkFields();

	protected abstract void writeFields();

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		makeComponents(composite);
		initFields();
		composite.pack();
		return composite;
	}

	protected abstract void makeComponents(Composite parent);

	protected abstract void initFields();

}
