package com.tvd.gext.multipageeditor.elements.pages;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.tvd.cocos2dx.popup.creator.model.ItemGroup;

public class LayoutGroupPage extends LayoutCommonPage {

	@Override
	public void createContents(Composite parent) {
		super.createContents(parent);
		FormToolkit toolkit = mForm.getToolkit();
		
		mIsArrayCheckbox = createCheckbox(toolkit, mClient, 
				commontext("exported"), false);
		mLengthText = createText(toolkit, mClient, 
				commontext("array_length"), "0");
		mCommentText = createTextArea(toolkit, mClient, 
				commontext("comment"), "");
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
			return;
		}
		
		ItemGroup group = (ItemGroup)pModel;
		boolean isArray = group.isArray();
		int numberOfItem = group.numberOfItems();
		int length = group.getArrayLength();
		String comment = group.getBlockComment();
		
		mCommentText.setText(comment);
		
		if(!isArray || numberOfItem != 1) {
			mIsArrayCheckbox.setSelection(false);
			mLengthText.setText("");
			mLengthText.setEnabled(false);
			
			if(numberOfItem != 1) {
				mIsArrayCheckbox.setEnabled(false);
			}
		}
		else {
			mIsArrayCheckbox.setSelection(true);
			mIsArrayCheckbox.setEnabled(true);
			mLengthText.setText("" + length);
			mLengthText.setEnabled(true);
		}
	}

	protected Button mIsArrayCheckbox;
	protected Text mLengthText;
	protected Text mCommentText;
}
