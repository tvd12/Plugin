package com.tvd.gext.multipageeditor.pages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.tvd.cocos2dx.popup.creator.model.View;
import com.tvd.gameview.ext.GameViewSdk;
import com.tvd.gext.multipageeditor.editors.constant.Img;

public class LayoutOverviewPage extends FormPage 
		implements ILayoutUpdateable {

	public LayoutOverviewPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	public LayoutOverviewPage(FormEditor editor) {
		this(editor, 
				LayoutOverviewPage.class.getName(), 
				text("title"));
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
		form.setText(text("title"));
		
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
		createContentSection(managedForm);
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
		
		mClassNameText = addTextField(textCompos, toolkit, 
				"general.classname", "");
		mAuthorText = addTextField(textCompos, toolkit, 
				"general.author", "");
		mTypeText = addTextField(textCompos, toolkit, 
				"general.type", "");
		mTemplateNameText = addTextField(textCompos, toolkit, 
				"general.templatename", "");
		
		mExiableCheckbox = addCheckbox(client, toolkit, 
				"general.exitable", false);
		mExportedCheckbox = addCheckbox(client, toolkit, 
				"general.exported", false);
		
		update();
		
		setDirty(false);
	}
	
	private void createContentSection(IManagedForm pForm) {
		String title = text("content.title"); //$NON-NLS-1$
		String desc = text("content.desc"); //$NON-NLS-1$
		Composite client = createSection(pForm, title, desc, 1);
		FormToolkit toolkit = pForm.getToolkit();
		
		addHyperlink(client, toolkit, text("content.xml.title"), 
				text("content.xml.desc"));
		addHyperlink(client, toolkit, text("content.element.title"), 
				text("content.element.desc"));
		addHyperlink(client, toolkit, text("content.building.title"), 
				text("content.building.desc"));
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
	
	private Text addTextField(Composite client, FormToolkit toolkit, 
			String titleKey, String value) {
		toolkit.createLabel(client, text(titleKey)); //$NON-NLS-1$
		final Text text = toolkit.createText(client, value, SWT.SINGLE);
		text.setLayoutData(gridData());
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent event) {
				Object source = event.getSource();
				if(source == mClassNameText) {
					mView.setClassName(text.getText());
				}
				else if(source == mAuthorText) {
					mView.setAuthor(text.getText());
				}
				else if(source == mTypeText) {
					mView.setType(text.getText());
				}
				else if(source == mTemplateNameText) {
					mView.setTemplateName(text.getText());
				}
//				setDirty(true);
			}
		});
		
		return text;
	}
	
	private void addHyperlink(Composite client, FormToolkit toolkit,
			String title, String content) {
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 2;
		Composite composite = toolkit.createComposite(client);
		composite.setLayout(layout);
		
		ImageHyperlink link = toolkit.createImageHyperlink(composite, SWT.NULL);
		Image image = GameViewSdk.getDefault()
				.getImageRegistry().get(Img.LAYOUT_ICON);
		link.setImage(image);
		link.setText(title);
		toolkit.createLabel(composite, content); //$NON-NLS-1$
	}
	
	private Button addCheckbox(Composite client, FormToolkit toolkit, 
			String titleKey, boolean isChecked) {
		final Button button = toolkit.createButton(client, 
				text(titleKey), SWT.CHECK); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		button.setLayoutData(gd);
		button.setSelection(isChecked);
		
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object source = e.getSource();
				if(source == mExiableCheckbox) {
					mView.setExitable(button.getSelection());
				}
				else if(source == mExportedCheckbox) {
					mView.setExported(button.getSelection());
				}
				setDirty(true);
			}
		});
		
		return button;
	}
	
	private static String text(String key) {
		String className = LayoutOverviewPage.class.getSimpleName();
		return Messages.getString(className + "." + key);
	}
	
	@Override
	public boolean isDirty() {
		return mIsDirty;
	}
	
	public void setDirty(boolean isDirty) {
		this.mIsDirty = isDirty;
		getManagedForm().dirtyStateChanged();
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			mView.writeXMLToFile();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		super.doSave(monitor);
		setDirty(false);
	}
	
	@Override
	public void update() {
		if(!(getEditor().getEditorInput() instanceof LayoutEditorInput)) {
			return;
		}
		LayoutEditorInput input = (LayoutEditorInput)getEditor()
					.getEditorInput();
		View view = input.getView();
		mView = view;
		
		mClassNameText.setText(view.getClassName());
		mAuthorText.setText(view.getAuthor());
		mTypeText.setText(view.getType());
		mTemplateNameText.setText(mView.getTemplateName());
		
		mExportedCheckbox.setSelection(view.isExported());
		mExiableCheckbox.setSelection(view.isExitable());
			
	}
	
	protected boolean mIsDirty;
	
	protected Text mClassNameText;
	protected Text mAuthorText;
	protected Text mTypeText;
	protected Text mTemplateNameText;
	
	protected Button mExportedCheckbox;
	protected Button mExiableCheckbox;
	
	private View mView;
}
