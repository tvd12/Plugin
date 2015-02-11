package com.tvd.gext.multipageeditor.elements.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class LayoutViewPage extends LayoutCommonPage {

	@Override
	public void createContents(Composite parent) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 5;
		layout.leftMargin = 5;
		layout.rightMargin = 2;
		layout.bottomMargin = 2;
		parent.setLayout(layout);
		
		FormToolkit toolkit = mForm.getToolkit();
		Section sectionOne = toolkit.createSection(parent, 
				Section.DESCRIPTION | Section.TITLE_BAR);
		sectionOne.marginWidth = 10;
		sectionOne.setText(text("title"));
		sectionOne.setDescription(text("desc"));
		
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		sectionOne.setLayoutData(td);
		
		Composite client = toolkit.createComposite(sectionOne);
		GridLayout glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		glayout.numColumns = 2;
		client.setLayout(glayout);
		
		SelectionListener checkListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				System.out.println("choiceListener.data = " + e.widget.getData());
			}
		};
		
		mCheckboxes = new ArrayList<Button>();
		for(int i = 0 ; i < 1 ; i++) {
			Button checkbox = toolkit.createButton(client, "Exported", SWT.CHECK);
			checkbox.addSelectionListener(checkListener);
			GridData gd = new GridData();
			gd.horizontalSpan = 2;
			checkbox.setLayoutData(gd);
			mCheckboxes.add(checkbox);
		}
		createSpacer(toolkit, client, 2);
		mFlag = toolkit.createButton(client, "Value of the flag property", SWT.CHECK);
		
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		mFlag.setLayoutData(gd);
		createSpacer(toolkit, client, 2);
		
		toolkit.createLabel(client, "Text property:");
		mText = toolkit.createText(client, "", SWT.SINGLE);
		mText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = 10;
		mText.setLayoutData(gd);
		
		createSpacer(toolkit, client, 2);
		FormText rtext = toolkit.createFormText(parent, true);
		rtext.setText(RTEXT_DATA, true, false);
		td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		rtext.setLayoutData(td);
		
		toolkit.paintBordersFor(sectionOne);
		sectionOne.setClient(client);
	}
	
	protected String commontext(String key) {
		return Messages.getString("Element" + "." + key);
	}
	
	@Override
	protected void update(Object pModel) {
		
	}
	
	protected List<Button> mCheckboxes;
	protected Button mFlag;
	protected Text mText;
	
	private static final String RTEXT_DATA =
			"<form><p>An example of a free-form text that should be "+ //$NON-NLS-1$
			"wrapped below the section with widgets.</p>"+ //$NON-NLS-1$
			"<p>It can contain simple tags like <a>links</a> and <b>bold text</b>.</p></form>"; //$NON-NLS-1$

}
