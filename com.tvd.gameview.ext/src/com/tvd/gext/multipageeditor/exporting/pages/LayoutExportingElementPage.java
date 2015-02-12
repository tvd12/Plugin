package com.tvd.gext.multipageeditor.exporting.pages;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.tvd.cocos2dx.popup.creator.model.View;
import com.tvd.gameview.ext.constants.Constant.TreeElement;

public class LayoutExportingElementPage 
		extends LayoutExportingCommonPage {
	@Override
	public void initialize(IManagedForm pForm) {
		this.mForm = pForm;
		this.mColumn = 1;
	}
	
	@Override
	public void createContents(Composite parent) {
		super.createContents(parent);
		FormToolkit toolkit = mForm.getToolkit();
		mEnableCheckbox = createCheckbox(toolkit, mClient, text("enable"), true);
		mExportButton = createButton(toolkit, mClient, text("active"));
		mCodeText = createTextArea(toolkit, mClient, text("source_code"), "");
	}
	
	@Override
	protected void update(Object model) {
		if(model == null) {
			return ;
		}
		LayoutExportingElement element = (LayoutExportingElement)model;
		String name = element.getName();
		View view = element.getView();
		mView = view;
		mName = name;
		
		if(name.equals(TreeElement.DECLARE_POSITIONS)) {
			mCodeText.setText(view.declarePositions());
		}
		else if(name.equals(TreeElement.IMPLEMENT_POSITIONS)) {
			mCodeText.setText(view.implementPositions());
		}
		else if(name.equals(TreeElement.DECLARE_IDS)) {
			mCodeText.setText(view.declareImageIds());
		}
		else if(name.equals(TreeElement.IMPLEMENT_IDS)) {
			mCodeText.setText(view.implementImageIds());
		}
		else if(name.equals(TreeElement.DECLARE_CLASS)) {
			mCodeText.setText(view.declare());
		}
		else if(name.equals(TreeElement.IMPLEMENT_CLASS)) {
			mView.refresh();
			mCodeText.setText(view.implement(false));
		}
		else if(name.equals(TreeElement.EXPORT_XIB_TPL)) {
			mCodeText.setText(view.xibContent());
		}
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
	
	private Button createButton(final FormToolkit toolkit, 
			final Composite parent,
			String label) {
		SelectionListener listener = new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent event) {
				exportWithProgress(parent);
			}
		};
		
		Button button = super.createButton(toolkit, parent, label, listener);
		
		return button;
	}
	
	private Text createTextArea(FormToolkit toolkit, Composite parent,
			String label, String value) {
		ModifyListener listener = new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent event) {
				
			}
		};
		
		return super.createTextArea(toolkit, parent, label, 
				value, listener, 250);
	}
	
	private void exportWithProgress(final Composite parent) {
		ProgressMonitorDialog dialog =
				new ProgressMonitorDialog(parent.getShell());
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor pMonitor)
						throws InvocationTargetException, 
						InterruptedException {
					export();
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	private void export() {
		if(mName.equals(TreeElement.EXPORT_POSITIONS)) {
			mView.exportDeclaringPositions();
			mView.exportPositions();
		}
		if(mName.equals(TreeElement.DECLARE_POSITIONS)) {
			mView.exportDeclaringPositions();
		}
		else if(mName.equals(TreeElement.IMPLEMENT_POSITIONS)) {
			mView.exportImplementedPositions();
		}
		else if(mName.equals(TreeElement.EXPORT_IDS)) {
			mView.exportIdentifiers();
		}
		else if(mName.equals(TreeElement.DECLARE_IDS)) {
			mView.exportDeclaringImageIds();
		}
		else if(mName.equals(TreeElement.IMPLEMENT_IDS)) {
			mView.exportImplementedImageIds();
		}
		else if(mName.equals(TreeElement.EXPORT_SRC_CODE)) {
			mView.refresh();
			mView.exportSourceCode();
		}
		else if(mName.equals(TreeElement.DECLARE_CLASS)) {
			mView.exportHeaderCode();
		}
		else if(mName.equals(TreeElement.IMPLEMENT_CLASS)) {
			mView.refresh();
			mView.exportImplementedCode();
		}
		else if(mName.equals(TreeElement.EXPORT_XIB_TPL)) {
			mView.exportXibTemplate(false);
		}
		else if(mName.equals(TreeElement.RELOAD_SIZE)) {
				mView.reloadImageSizes();
		}
		else if(mName.equals(TreeElement.REFRESH_CONTENT)) {
			try {
				mView.refreshXMLFile();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	protected Button mEnableCheckbox;
	protected Button mExportButton;
	protected Text mCodeText;
	
	private View mView;
	private String mName;
}
