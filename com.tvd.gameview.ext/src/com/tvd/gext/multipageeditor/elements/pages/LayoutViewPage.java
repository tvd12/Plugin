package com.tvd.gext.multipageeditor.elements.pages;

import java.util.List;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.tvd.cocos2dx.popup.creator.model.View;

public class LayoutViewPage extends LayoutCommonPage {

	@Override
	public void createContents(Composite parent) {
		super.createContents(parent);
		FormToolkit toolkit = mForm.getToolkit();
		
		mExportedCheckbox = createCheckbox(toolkit, mClient, 
				text("exported"), false);
		mImageExportedCheckbox = createCheckbox(toolkit,
				mClient, text("image_exported"), false);
		mXibExportedCheckbox = createCheckbox(toolkit,
				mClient, text("xib_exported"), false);
		mIdExportedCheckbox = createCheckbox(toolkit,
				mClient, text("id_exported"), false);
		mPosExportedCheckbox = createCheckbox(toolkit,
				mClient, text("pos_exported"), false);
		mCodeExportedCheckbox = createCheckbox(toolkit,
				mClient, text("code_exported"), false);
		mSizeReloadedCheckbox = createCheckbox(toolkit, 
				mClient, text("size_reloaded"), false);
		
		mXibPathText = createText(toolkit, mClient,
				text("xib_path"), "");
		mParamPathText = createText(toolkit, mClient,
				text("param_path"), "");
		mDefinePathText = createText(toolkit, mClient,
				text("define_path"), "");
		mClassPathText = createText(toolkit, mClient,
				text("class_path"), "");
		
		mCommentText = createTextArea(toolkit, mClient, 
				commontext("comment"), "");
				
		createSpacer(toolkit, mClient, 2);
		FormText rtext = toolkit.createFormText(parent, true);
		rtext.setText(RTEXT_DATA, true, false);
		TableWrapData td = new TableWrapData(
				TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		rtext.setLayoutData(td);
	}
	
	private Button createCheckbox(FormToolkit toolkit, Composite parent,
			String label, boolean isChecked) {
		SelectionListener listener = new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		};
		
		Button checkbox = super.createCheckbox(toolkit, parent, label, 
				isChecked, listener);
		
		return checkbox;
	}
	
	private Text createText(FormToolkit toolkit, Composite parent,
			String label, String value) {
		ModifyListener listener = new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent event) {
				
			}
		};
		
		return super.createText(toolkit, parent, label, 
				value, listener);
	}
	
	private Text createTextArea(FormToolkit toolkit, Composite parent,
			String label, String value) {
		ModifyListener listener = new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent event) {
				
			}
		};
		
		return super.createTextArea(toolkit, parent, label, 
				value, listener);
	}
	
	@Override
	protected void update(Object pModel) {
		if(pModel == null) {
			return ;
		}
		
		View view = (View)pModel;
		boolean exported = view.isExported();
		
		String xibPath = view.getXibContainerPath();
		String paramPath = view.getParametersPath();
		String definePath = view.getDefinePath();
		String classPath = view.getClassPath();
		String comment = view.getComment();
		
		mExportedCheckbox.setSelection(exported);
		
		mXibPathText.setText(xibPath);
		mParamPathText.setText(paramPath);
		mDefinePathText.setText(definePath);
		mClassPathText.setText(classPath);
		mCommentText.setText(comment);
	}
	
	protected List<Button> mCheckboxes;
	protected Button mFlag;
	protected Text mText;
	
	protected Text mXibPathText;
	protected Text mParamPathText;
	protected Text mDefinePathText;
	protected Text mClassPathText;
	protected Text mCommentText; 
	
	protected Button mExportedCheckbox;
	protected Button mImageExportedCheckbox;
	protected Button mXibExportedCheckbox;
	protected Button mIdExportedCheckbox;
	protected Button mPosExportedCheckbox;
	protected Button mCodeExportedCheckbox;
	protected Button mSizeReloadedCheckbox;
	
	private static final String RTEXT_DATA =
			"<form><p>An example of a layout that should be "+ //$NON-NLS-1$
			"wrapped below the section with widgets.</p>"+ //$NON-NLS-1$
			"<p>It can contain simple tags like <a>links</a> and <b>bold text</b>.</p></form>"; //$NON-NLS-1$

}
