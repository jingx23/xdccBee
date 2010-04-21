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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.ui.Application;

/**
 * @author snert
 *
 */
public class AboutDialog extends Dialog{

	public AboutDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		//no Buttons
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		makeComponents(composite);
		composite.pack();
		return composite;
	}                 
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(XdccBeeMessages.getString("AboutDialog_TITLE")); //$NON-NLS-1$
		newShell.setSize(200, 200);
		Application.placeDialogInCenter(getParentShell(), newShell);
	}
	
	private void makeComponents(Composite parent){
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout( new GridLayout() );
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label lblAppName = new Label(comp, SWT.NONE);
		lblAppName.setText(XdccBeeMessages.getString("Application_TITLE"));
		GridData a = new GridData(SWT.CENTER, SWT.CENTER, true, false);
		a.verticalIndent = 40;
		lblAppName.setLayoutData(a);
		
		Label lblVersion = new Label(comp, SWT.NONE);
		lblVersion.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		lblVersion.setText(Application.VERSION_STRING);
		
		Label lblAuthor = new Label(comp, SWT.NONE);
		lblAuthor.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		lblAuthor.setText(XdccBeeMessages.getString("AboutDialog_COPYRIGHT")); //$NON-NLS-1$
	}

}
