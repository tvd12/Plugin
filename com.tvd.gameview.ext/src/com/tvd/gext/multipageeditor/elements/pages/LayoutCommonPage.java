package com.tvd.gext.multipageeditor.elements.pages;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public abstract class LayoutCommonPage implements IDetailsPage {

	@Override
	public void initialize(IManagedForm pForm) {
		this.mForm = pForm;
		this.mColumn = 2;
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void commit(boolean pOnSave) {
		
	}

	@Override
	public boolean setFormInput(Object pInput) {
		return false;
	}

	@Override
	public void setFocus() {
		
	}

	@Override
	public boolean isStale() {
		return false;
	}

	@Override
	public void refresh() {
		
	}
	
	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection s = (IStructuredSelection)selection;
			update(s.getFirstElement());
		}
	}

	@Override
	public void createContents(Composite pParent) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 5;
		layout.leftMargin = 5;
		layout.rightMargin = 2;
		layout.bottomMargin = 2;
		pParent.setLayout(layout);
		
		FormToolkit toolkit = mForm.getToolkit();
		Section sectionOne = toolkit.createSection(pParent, 
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
		glayout.numColumns = mColumn;
		client.setLayout(glayout);
		
		toolkit.paintBordersFor(sectionOne);
		sectionOne.setClient(client);
		
		mClient = client;
		mSection = sectionOne;
	}
	
	protected abstract void update(Object model);
	
	protected Text createText(FormToolkit toolkit, Composite parent, 
			String label, String value, ModifyListener listener) {
		toolkit.createLabel(parent, label);
		Text text = toolkit.createText(parent, value, SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = 10;
		text.setLayoutData(gd);
		text.addModifyListener(listener);
		
		return text;
	}
	
	protected Button createButton(FormToolkit toolkit, Composite parent,
			String label, SelectionListener listener) {
		Button button = toolkit.createButton(parent, label, 
				SWT.PUSH); //$NON-NLS-1$
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		button.setLayoutData(gd);
		button.addSelectionListener(listener);
		
		return button;
	}
	
	protected Button createCheckbox(FormToolkit toolkit, Composite parent,
			String label, boolean isChecked, SelectionListener listener) {
		Button checkbox = toolkit.createButton(parent, label, SWT.CHECK);
		checkbox.addSelectionListener(listener);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		checkbox.setLayoutData(gd);
		
		return checkbox;
	}
	
	protected Text createTextArea(FormToolkit toolkit, Composite parent,
			String label, String value, ModifyListener listener) {
		return createTextArea(toolkit, parent, label, value, listener, 50);
	}
	
	protected Text createTextArea(FormToolkit toolkit, Composite parent,
			String label, String value, ModifyListener listener, int height) {
		toolkit.createLabel(parent, label);
		Text text = toolkit.createText(parent, value, 
				SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);

		final GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = height;
		text.setLayoutData(gridData);
		
		return text;
	}
	
	protected void createSpacer(FormToolkit toolkit, Composite parent, int span) {
		Label spacer = toolkit.createLabel(parent, ""); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.horizontalSpan = span;
		spacer.setLayoutData(gd);
	}
	
	protected String text(String key) {
		String className = getClass().getSimpleName();
		return Messages.getString(className + "." + key);
	}
	
	protected String commontext(String key) {
		return Messages.getString("Element" + "." + key);
	}

	protected IManagedForm mForm;
	protected Composite mClient;
	protected Section mSection;
	protected int mColumn;
}
