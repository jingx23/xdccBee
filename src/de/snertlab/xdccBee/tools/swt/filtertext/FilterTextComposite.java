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
package de.snertlab.xdccBee.tools.swt.filtertext;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
 
 
public class FilterTextComposite extends Composite {
 
    protected Text filterText;
    protected Control clearButtonControl;

    protected Composite filterComposite;
    
    protected String initialText = ""; //$NON-NLS-1$ 
    protected Composite parent;
 
    private static final String CLEAR_ICON = "icons/cross.png"; //$NON-NLS-1$ 
    private static final String DISABLED_CLEAR_ICON = "icons/cross.png"; //$NON-NLS-1$
    private static final String SEARCH_ICON = "icons/zoom.png"; //$NON-NLS-1$
 
    public FilterTextComposite(Composite parent) {
        super(parent, SWT.NONE);
        this.parent = parent;
        init();
    }
 
    protected void init() {
        createControl(parent);
        setInitialText("InitialText");
        setFont(parent.getFont()); 
    }
 
    protected void createControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        filterComposite = new Composite(this, SWT.BORDER);
        filterComposite.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
 
        GridLayout filterLayout = new GridLayout(3, false);
        filterLayout.marginHeight = 0;
        filterLayout.marginWidth = 0;
        filterComposite.setLayout(filterLayout);
        filterComposite.setFont(parent.getFont());
 
        createFilterControls(filterComposite);
        filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
    }
 
    protected Composite createFilterControls(Composite parent) {
    	createSearchTextNew(parent);
        createFilterText(parent);
        createClearTextNew(parent);
        if (clearButtonControl != null) {
            // initially there is no text to clear
            clearButtonControl.setVisible(false);
        }
 
        return parent;
    }
 
    protected void updateToolbar(boolean visible) {
        if (clearButtonControl != null) {
            clearButtonControl.setVisible(visible);
        }
    }
 
    protected void createFilterText(Composite parent) {
        filterText = doCreateFilterText(parent);
 
        filterText.addModifyListener(new ModifyListener() {
 
            @Override
            public void modifyText(ModifyEvent e) {
                if (filterText.getText().length() > 0) {
                    updateToolbar(true);
                } else {
                    updateToolbar(false);
                }
 
            }
        });
 
        filterText.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
 
            }
 
            public void focusLost(FocusEvent e) {
                if (filterText.getText().equals(initialText)) {
                    setFilterText(""); //$NON-NLS-1$
                }
            }
        });
 
        filterText.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent e) {
                if (filterText.getText().equals(initialText)) {
                    // XXX: We cannot call clearText() due to
                    // [url]https://bugs.eclipse.org/bugs/show_bug.cgi?id=260664[/url]
                    setFilterText(""); //$NON-NLS-1$
                }
            }
        });
 
        // if we're using a field with built in cancel we need to listen for
        // default selection changes (which tell us the cancel button has been
        // pressed)
        if ((filterText.getStyle() & SWT.ICON_CANCEL) != 0) {
            filterText.addSelectionListener(new SelectionAdapter() {
                public void widgetDefaultSelected(SelectionEvent e) {
                    if (e.detail == SWT.ICON_CANCEL)
                        clearText();
                }
            });
        }
 
        GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        // if the text widget supported cancel then it will have it's own
        // integrated button. We can take all of the space.
        if ((filterText.getStyle() & SWT.ICON_CANCEL) != 0)
            gridData.horizontalSpan = 2;
        filterText.setLayoutData(gridData);
    }
 
    protected Text doCreateFilterText(Composite parent) {
        return new Text(parent, SWT.SINGLE | SWT.ICON_CANCEL);
    }
 
    public void setBackground(Color background) {
        super.setBackground(background);
    }
 
    private void createClearTextNew(Composite parent) {
        // only create the button if the text widget doesn't support one
        // natively
        if ((filterText.getStyle() & SWT.ICON_CANCEL) == 0) {
            final Image inactiveImage = new Image(this.getDisplay(), FilterTextComposite.class.getResourceAsStream(DISABLED_CLEAR_ICON));
            final Image activeImage = new Image(this.getDisplay(), FilterTextComposite.class.getResourceAsStream(CLEAR_ICON));
            final Image pressedImage = new Image(getDisplay(), activeImage, SWT.IMAGE_GRAY);
 
            final Label clearButton = new Label(parent, SWT.NONE);
            clearButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
                    false, false));
            clearButton.setImage(inactiveImage);
            clearButton.setBackground(parent.getDisplay().getSystemColor(
                    SWT.COLOR_LIST_BACKGROUND));
            clearButton.setToolTipText("Tooltip");
            clearButton.addMouseListener(new MouseAdapter() {
                private MouseMoveListener fMoveListener;
 
                public void mouseDown(MouseEvent e) {
                    clearButton.setImage(pressedImage);
                    fMoveListener = new MouseMoveListener() {
                        private boolean fMouseInButton = true;
 
                        public void mouseMove(MouseEvent e) {
                            boolean mouseInButton = isMouseInButton(e);
                            if (mouseInButton != fMouseInButton) {
                                fMouseInButton = mouseInButton;
                                clearButton
                                        .setImage(mouseInButton ? pressedImage
                                                : inactiveImage);
                            }
                        }
                    };
                    clearButton.addMouseMoveListener(fMoveListener);
                }
 
                public void mouseUp(MouseEvent e) {
                    if (fMoveListener != null) {
                        clearButton.removeMouseMoveListener(fMoveListener);
                        fMoveListener = null;
                        boolean mouseInButton = isMouseInButton(e);
                        clearButton.setImage(mouseInButton ? activeImage
                                : inactiveImage);
                        if (mouseInButton) {
                            clearText();
                            filterText.setFocus();
                        }
                    }
                }
 
                private boolean isMouseInButton(MouseEvent e) {
                    Point buttonSize = clearButton.getSize();
                    return 0 <= e.x && e.x < buttonSize.x && 0 <= e.y
                            && e.y < buttonSize.y;
                }
            });
            clearButton.addMouseTrackListener(new MouseTrackListener() {
                public void mouseEnter(MouseEvent e) {
                    clearButton.setImage(activeImage);
                }
 
                public void mouseExit(MouseEvent e) {
                    clearButton.setImage(inactiveImage);
                }
 
                public void mouseHover(MouseEvent e) {
                }
            });
            clearButton.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                    inactiveImage.dispose();
                    activeImage.dispose();
                    pressedImage.dispose();
                }
            });
 
            this.clearButtonControl = clearButton;
        }
    }
    
    private void createSearchTextNew(Composite parent) {
        // only create the button if the text widget doesn't support one
        // natively
        if ( true) {
            final Image activeImage = new Image(this.getDisplay(), FilterTextComposite.class.getResourceAsStream(SEARCH_ICON)); 
            final Label clearButton = new Label(parent, SWT.NONE);
            clearButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
            clearButton.setImage(activeImage);
            clearButton.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        }
    }
 
    protected void clearText() {
        setFilterText(""); //$NON-NLS-1$
    }
 
    protected void setFilterText(String string) {
        if (filterText != null) {
            filterText.setText(string);
            selectAll();
        }
    }
  
    public Text getFilterControl() {
        return filterText;
    }
 
    protected String getFilterString() {
        return filterText != null ? filterText.getText() : null;
    }
 
    public void setInitialText(String text) {
        initialText = text;
        if (filterText != null) {
            filterText.setMessage(text);
            if (filterText.isFocusControl()) {
                setFilterText(initialText);
            } else {
                getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        if (!filterText.isDisposed()
                                && filterText.isFocusControl()) {
                            setFilterText(initialText);
 
                        }
                    }
                });
            }
        } else {
            setFilterText(initialText);
        }
    }
 
    protected void selectAll() {
        if (filterText != null) {
            filterText.selectAll();
        }
    }
 
    protected String getInitialText() {
        return initialText;
    }
 
}
