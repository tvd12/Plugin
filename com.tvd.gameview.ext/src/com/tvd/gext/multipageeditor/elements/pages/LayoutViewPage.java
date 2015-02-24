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
import com.tvd.gext.multipageeditor.pages.LayoutDetailsPage;

public class LayoutViewPage extends LayoutCommonPage {
	
	public LayoutViewPage(LayoutDetailsPage page) {
		this.mFormPage = page;
	}

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
		
		mXibPathSelector = createFileSelector(toolkit, mClient,
				text("xib_path"), "", 
				LayoutFileSelector.DIRECTORY);
		mParamPathSelector = createFileSelector(toolkit, mClient,
				text("param_path"), "",
				LayoutFileSelector.FILE);
		mDefinePathSelector = createFileSelector(toolkit, mClient,
				text("define_path"), "", 
				LayoutFileSelector.FILE);
		mImagePathSelector = createFileSelector(toolkit, 
				mClient, text("image_path"), "", 
				LayoutFileSelector.DIRECTORY);
		
		mClassPathSelector = createFileSelector(toolkit, mClient,
				text("class_path"), "",
				LayoutFileSelector.DIRECTORY);
		
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
	
	@SuppressWarnings("unused")
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
				Text text = (Text)event.getSource();
				if(!text.isFocusControl()) {
					return ;
				}
				if(text == mCommentText) {
					mView.setComment(text.getText());
				}
				
				mFormPage.setDirty(true);
			}
			
		};
		
		return super.createTextArea(toolkit, parent, label, 
				value, listener);
	}
	
	private LayoutFileSelector createFileSelector(FormToolkit toolkit, Composite parent,
			String label, String value, int type) {
		LayoutFileSelector selector = LayoutFileSelector.create(toolkit, 
				parent, label, value, type);
		selector.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent event) {
				Text text = (Text)event.getSource();
				LayoutFileSelector container = (LayoutFileSelector)
						text.getData();
				if(container == mXibPathSelector) {
					mView.setXibContainerPath(text.getText());
				}
				else if(container == mParamPathSelector) {
					String txt = standardizedPath(
							mParamPathSelector.getText());
					if(txt != null) {
						text.setText(txt);
						mView.setParametersPath(text.getText());
					}
				}
				else if(container == mDefinePathSelector) {
					String txt = standardizedPath(
							mDefinePathSelector.getText());
					if(txt != null) {
						text.setText(txt);
						mView.setDefinePath(text.getText());
					}
				}
				else if(container == mImagePathSelector) {
					mView.setImagesPath(text.getText());
				}
				else if(container == mClassPathSelector) {
					mView.setClassPath(text.getText());
				}
			}
		});
		
		return selector;
	}
	
	@Override
	protected void update(Object pModel) {
		if(pModel == null) {
			return ;
		}
		
		View view = (View)pModel;
		mView = view;

		mExportedCheckbox.setSelection(view.isExported());
		
		mXibPathSelector.setText(view.getXibContainerPath());
		mParamPathSelector.setText(view.getParametersPath());
		mDefinePathSelector.setText(view.getDefinePath());
		mImagePathSelector.setText(view.getImagePath());
		mClassPathSelector.setText(view.getClassPath());
		mCommentText.setText(view.getComment());
	}
	
	private String standardizedPath(String text) {
		if(text == null || text.trim().length() == 0
				|| !text.contains(".")) {
			return null;
		}
		
		if(!text.contains(".")) {
			return text;
		}
		
		int lastIndexOf = text.lastIndexOf(".");
		if(lastIndexOf > text.lastIndexOf("/")) {
			return text.substring(0, lastIndexOf);
		}
		
		return null;
	}
	
	protected View mView;
	
	protected LayoutDetailsPage mFormPage;
	
	protected List<Button> mCheckboxes;
	protected Button mFlag;
	protected Text mText;
	
	protected LayoutFileSelector mXibPathSelector;
	protected LayoutFileSelector mParamPathSelector;
	protected LayoutFileSelector mDefinePathSelector;
	protected LayoutFileSelector mImagePathSelector;
	protected LayoutFileSelector mClassPathSelector;
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
