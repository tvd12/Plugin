package com.tvd.gext.multipageeditor.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.tvd.gext.multipageeditor.editors.LayoutMultiPageEditor;

public class LayoutOverviewPage {

	public static LayoutOverviewPage create(LayoutMultiPageEditor parent) {
		LayoutOverviewPage page = new LayoutOverviewPage();
		
		return page;
	}
	
	public boolean init(LayoutMultiPageEditor parent) {
		this.mParent = parent;
		
		Composite composite = new Composite(mParent.getContainer(), SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 2;

		Button fontButton = new Button(composite, SWT.NONE);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = 2;
		fontButton.setLayoutData(gd);
		fontButton.setText("Change Font...");
		
		fontButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				mParent.setFont();
			}
		});

		int index = mParent.addPage(composite);
		mParent.setPageText(index, "Properties");
		
		return true;
	}
	
	protected LayoutMultiPageEditor mParent;
}
