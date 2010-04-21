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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

public class MyMessageDialog extends MessageDialog{

	public MyMessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
	}
	
	
	public static boolean openConfirm(Shell parent, String title, String message) {
		MessageDialog dialog = new MessageDialog( parent, 
        										  title, 
        										  null, 
									              message, QUESTION,
									              new String[]{"Ja", "Nein"},  //$NON-NLS-1$ //$NON-NLS-2$
								              0
								            );
		// ok is the default
		return dialog.open() == 0;
	}
	
	public static int openConfirm3Btn(Shell parent, String title, String message) {
		MessageDialog dialog = new MessageDialog( parent, 
        										  title, 
        										  null, 
									              message, QUESTION,
									              new String[]{"Ja", "Nein", "Abbrechen"},  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								              0
								            );
		// ok is the default
		return dialog.open();
	}

}
