package com.tvd.gext.multipageeditor.elements.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class LayoutFileSelector {
	
	public static LayoutFileSelector create(FormToolkit toolkit,
			Composite parent, String label, 
			String value, int type) {
		LayoutFileSelector selector = new LayoutFileSelector();
		selector.init(toolkit, parent, label, value, type);
		
		return selector;
	}
	
	public boolean init(FormToolkit toolkit,
			Composite parent, String label, 
			String value, int type) {
		this.mToolkit = toolkit;
		this.mParent = parent;
		
		this.mLabel = label;
		this.mValue = value;
		
		this.mType = type;
		
		toolkit.createLabel(parent, label);
		
		Composite composite = toolkit.createComposite(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);

		mText = toolkit.createText(composite, value);
		mText.setData(this);
		
		mButton = toolkit.createButton(composite, "Browse...", SWT.NONE);
		
		if(mType == DIRECTORY) {
			mDirectoryDialog = new DirectoryDialog(composite.getShell(), SWT.OPEN);
		}
		else {
			mFileDialog = new FileDialog(composite.getShell(), SWT.OPEN);
		}
		
		mButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = null;
				if(mType == DIRECTORY) {
					path = mDirectoryDialog.open();
				}
				else {
					path = mFileDialog.open();
				}
				if(path != null) {
					mText.setFocus();
					mText.setText(path);
				}
			}
		});
		
		return true;
	}
	
	public void addModifyListener(ModifyListener listener) {
		mText.addModifyListener(listener);
	}
	
	public boolean isFocusControl() {
		return mText.isFocusControl()
				|| mButton.isFocusControl();
	}
	
	public void setText(String value) {
		mText.setText(value);
	}
	
	public String getText() {
		return mText.getText();
	}

	protected String mLabel;
	protected String mValue;
	
	protected Text mText;
	protected Button mButton;
	protected DirectoryDialog mDirectoryDialog;
	protected FileDialog mFileDialog;
	
	protected FormToolkit mToolkit;
	protected Composite mParent;
	
	protected int mType;
	
	public static final int FILE 		= 0x0;
	public static final int DIRECTORY 	= 0x1;
	
}
