package com.tvd.gext.multipageeditor.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class LayoutOverviewPage extends FormPage {

	public LayoutOverviewPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	public LayoutOverviewPage(FormEditor editor) {
		this(editor, 
				LayoutOverviewPage.class.getName(), 
				Messages.getString("LayoutOverviewPage.title"));
	}

	public static LayoutOverviewPage create(FormEditor editor) 
			throws PartInitException {
		LayoutOverviewPage page = new LayoutOverviewPage(editor);
		editor.addPage(page);
		
		return page;
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		form.setText(Messages.getString("LayoutOverviewPage.title"));
		
		ColumnLayout layout = new ColumnLayout();
		layout.topMargin 			= 0;
		layout.bottomMargin 		= 10;
		layout.leftMargin 			= 10;
		layout.rightMargin 			= 10;
		layout.horizontalSpacing 	= 10;
		layout.verticalSpacing 		= 10;
		layout.maxNumColumns		= 4;
		layout.minNumColumns 		= 2;
		
		form.getBody().setLayout(layout);
		
		createGeneralSection(managedForm);
		createDocumentSection(managedForm);
	}
	
	private void createGeneralSection(IManagedForm pForm) {
		String title = text("general.title");
		String desc = text("general.desc");
		Composite client = createSection(pForm, title, desc, 1);
		FormToolkit toolkit = pForm.getToolkit();
		
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 2;
		Composite textCompos = toolkit.createComposite(client);
		
		textCompos.setLayout(layout);
		
		addTextField(textCompos, toolkit, "general.classname");
		addTextField(textCompos, toolkit, "general.author");
		addTextField(textCompos, toolkit, "general.type");
		addTextField(textCompos, toolkit, "general.templatename");
		
		addCheckbox(client, toolkit, "general.exitable");
		addCheckbox(client, toolkit, "general.exported");
		
	}
	
	private void createContentSection(IManagedForm pForm) {
		String title = text("content.title"); //$NON-NLS-1$
		String desc = text("content.desc"); //$NON-NLS-1$
		Composite client = createSection(pForm, title, desc, 1);
		FormToolkit toolkit = pForm.getToolkit();
		
		
	}
	
	private void createDocumentSection(IManagedForm pForm) {
		String title = text("document.title"); //$NON-NLS-1$
		String desc = text("document.desc"); //$NON-NLS-1$
		Composite client = createSection(pForm, title, desc, 1);
		FormToolkit toolkit = pForm.getToolkit();
		String hlinks[] = {
			"document.documentlink",
			"document.schemalink",
		};
		for(int i = 0 ; i < hlinks.length ; i++) {
			toolkit.createHyperlink(client, 
					text(hlinks[i]), //$NON-NLS-1$
					SWT.WRAP);
		}
	}
	
	private Composite createSection(IManagedForm pForm, 
			String title, String desc, int numberColumns) {
		final ScrolledForm form = pForm.getForm();
		FormToolkit toolkit = pForm.getToolkit();
		Section section = toolkit.createSection(form.getBody(), 
				Section.TWISTIE | Section.TITLE_BAR
				| Section.DESCRIPTION | Section.EXPANDED);
		section.setText(title);
		section.setDescription(desc);
		
		Composite client = toolkit.createComposite(section);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = numberColumns;
		
		client.setLayout(layout);
		
		section.setClient(client);
		section.addExpansionListener(new ExpansionAdapter() {
			
			@Override
			public void expansionStateChanged(ExpansionEvent event) {
				form.reflow(false);
			}
		});
				
		return client;
	}
	
	private GridData gridData() {
		GridData gd = new GridData();
		gd.widthHint = 150;
		
		return gd;
	}
	
	private void addTextField(Composite client, FormToolkit toolkit, 
			String titleKey) {
		toolkit.createLabel(client, text(titleKey)); //$NON-NLS-1$
		Text text = toolkit.createText(client, "", SWT.SINGLE);
		text.setLayoutData(gridData());
	}
	
	private void addHyperlink(Composite client, FormToolkit toolkit,
			String title, String content) {
		
	}
	
	private void addCheckbox(Composite client, FormToolkit toolkit, 
			String titleKey) {
		Button button = toolkit.createButton(client, 
				text(titleKey), SWT.CHECK); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		button.setLayoutData(gd);
	}
	
	private String text(String key) {
		String className = getClass().getSimpleName();
		return Messages.getString(className + "." + key);
	}
}
