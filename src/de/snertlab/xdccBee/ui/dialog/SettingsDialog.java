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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.snertlab.xdccBee.messages.XdccBeeMessages;
import de.snertlab.xdccBee.tools.AbstractSaveDialog;
import de.snertlab.xdccBee.ui.Application;

/**
 * @author snert
 *
 */
public class SettingsDialog extends AbstractSaveDialog{
	
	private static final int DIALOG_WIDTH  = 500;
	private static final int DIALOG_HEIGHT = 120;
	
	private Text txtBotName;
	private Text txtVersion;
	private Text txtNickname;
	private Text txtDownloadDir;
	
	public SettingsDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void myConfigureShell(Shell newShell) {
		newShell.setText(XdccBeeMessages.getString("SettingsDialog_TITLE")); //$NON-NLS-1$
	}
	
	@Override
	protected void initFields(){
		txtBotName.setText( Application.getSettings().getBotName() );
		txtVersion.setText( Application.getSettings().getBotVersion() );
		txtNickname.setText( Application.getSettings().getStandardNickname() );
		txtDownloadDir.setText( Application.getSettings().getDownloadFolder() );
		txtDownloadDir.setToolTipText( Application.getSettings().getDownloadFolder() );
	}
	
	@Override
	protected void makeComponents(Composite parent){
		int numCols=3;
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout( new GridLayout(numCols, false) );
		GridData gridDataComp = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridDataComp.widthHint = DIALOG_WIDTH;
		gridDataComp.heightHint = DIALOG_HEIGHT;
		comp.setLayoutData( gridDataComp );
		
		Label lblNickname = new Label(comp, SWT.NONE);
		lblNickname.setLayoutData( new GridData(SWT.RIGHT, SWT.CENTER, false, false) );
		lblNickname.setText(XdccBeeMessages.getString("SettingsDialog_LABEL_NAME_DEFAULT_NICKNAME")); //$NON-NLS-1$
		
		txtNickname = new Text(comp, SWT.BORDER);
		txtNickname.setLayoutData( new GridData(SWT.FILL, SWT.CENTER, true, false, numCols-1, 0) );
		
		Label Botname = new Label(comp, SWT.NONE);
		Botname.setLayoutData( new GridData(SWT.RIGHT, SWT.CENTER, false, false) );
		Botname.setText(XdccBeeMessages.getString("SettingsDialog_LABEL_NAME_BOT_NAME")); //$NON-NLS-1$
		
		txtBotName = new Text(comp, SWT.BORDER);
		txtBotName.setLayoutData( new GridData(SWT.FILL, SWT.CENTER, true, false, numCols-1, 0) );
		
		Label lblBotversion = new Label(comp, SWT.NONE);
		lblBotversion.setLayoutData( new GridData(SWT.RIGHT, SWT.CENTER, false, false) );
		lblBotversion.setText(XdccBeeMessages.getString("SettingsDialog_LABEL_NAME_BOT_VERSION")); //$NON-NLS-1$
		
		txtVersion = new Text(comp, SWT.BORDER);
		txtVersion.setLayoutData( new GridData(SWT.FILL, SWT.CENTER, true, false, numCols-1, 0) );
		
		Label lblDownloadDir = new Label(comp, SWT.NONE);
		lblDownloadDir.setLayoutData( new GridData(SWT.RIGHT, SWT.CENTER, false, false) );
		lblDownloadDir.setText(XdccBeeMessages.getString("SettingsDialog_LABEL_NAME_DOWNLOAD_DIR")); //$NON-NLS-1$
		
		txtDownloadDir = new Text(comp, SWT.BORDER);
		txtDownloadDir.setLayoutData( new GridData(SWT.FILL, SWT.CENTER, true, false) );
		txtDownloadDir.setEditable(false);
		txtDownloadDir.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Button btnDownloadDir  = new Button(comp, SWT.NONE);
		btnDownloadDir.setLayoutData( new GridData(SWT.FILL, SWT.CENTER, false, false) );
		btnDownloadDir.setText(XdccBeeMessages.getString("SettingsDialog_LABEL_NAME_DOWNLOAD_DIR_SEARCH")); //$NON-NLS-1$
		btnDownloadDir.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openDirectoryAuswahl();
			}
		});
		
	}
	
	@Override
	protected void writeFields(){
		Application.getSettings().setBotName(txtBotName.getText());
		Application.getSettings().setBotVersion(txtVersion.getText());
		Application.getSettings().setStandardNickname(txtNickname.getText());
		Application.getSettings().setDownloadFolder(txtDownloadDir.getText());
	}
	
	@Override
	protected String checkFields(){
		String msg = ""; //$NON-NLS-1$
		if( StringUtils.isEmpty(txtBotName.getText()) ){
			msg = msg + XdccBeeMessages.getString("SettingsDialog_CHECK_ERROR_BOT_NAME_EMPTY") + "\n";  //$NON-NLS-1$//$NON-NLS-2$
		}
		if( StringUtils.isEmpty(txtNickname.getText()) ){
			msg = msg + XdccBeeMessages.getString("SettingsDialog_CHECK_ERROR_NICKNAME_EMPTY") + "\n";  //$NON-NLS-1$//$NON-NLS-2$
		}
		if( StringUtils.isEmpty(txtVersion.getText()) ){
			msg = msg + XdccBeeMessages.getString("SettingsDialog_CHECK_ERROR_BOTVERSION_EMPTY") + "\n";  //$NON-NLS-1$//$NON-NLS-2$
		}
		if( StringUtils.isEmpty(txtDownloadDir.getText()) ){
			msg = msg + XdccBeeMessages.getString("SettingsDialog_CHECK_ERROR_DOWNLOAD_DIR_EMPTY") + "\n";  //$NON-NLS-1$//$NON-NLS-2$
		}
		return msg;
	}
	
	private void openDirectoryAuswahl(){
		DirectoryDialog dd = new DirectoryDialog(getShell(), SWT.OPEN);
		if( ! StringUtils.isEmpty(txtDownloadDir.getText()) ){
			dd.setFilterPath(txtDownloadDir.getText());
		}		
		String path = dd.open();
		if( ! StringUtils.isEmpty(path) ){
			txtDownloadDir.setText( path );   
	    	txtDownloadDir.setToolTipText( path );
		}
	}
	
}