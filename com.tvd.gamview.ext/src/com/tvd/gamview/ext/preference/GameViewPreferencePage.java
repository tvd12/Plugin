package com.tvd.gamview.ext.preference;

import java.io.File;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tvd.gamview.ext.Activator;
import com.tvd.gamview.ext.constants.Key;
import com.tvd.gamview.ext.utils.MessageUtils;

public class GameViewPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public GameViewPreferencePage() {
	}

	public GameViewPreferencePage(int style) {
		super(style);
	}

	public GameViewPreferencePage(String title, int style) {
		super(title, style);
	}

	public GameViewPreferencePage(String title, ImageDescriptor image, int style) {
		super(title, image, style);
	}

	@Override
	public void init(IWorkbench workbench) {
		this.setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		final DirectoryFieldEditor directory = 
				new SdkDirectoryFieldEditor(Key.TEMPLATE_ROOT, "Template Root", 
						this.getFieldEditorParent());
		directory.setStringValue(
				this.getPreferenceStore().getString(Key.TEMPLATE_ROOT));
		this.addField(directory);
	}
	

	/**
	 * 
	 * @author Dzung Ta Van
	 *
	 * reference https://android.googlesource.com/platform/development/+/d2f2b1d7b77d06bce8cf9340171cf6c25cd720c4/tools/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/preferences/AndroidPreferencePage.java
	 */
	private static class SdkDirectoryFieldEditor extends DirectoryFieldEditor {
	    public SdkDirectoryFieldEditor(String name, String labelText, Composite parent) {
	        super(name, labelText, parent);
	        setEmptyStringAllowed(false);
	    }
	    /**
	     * Method declared on StringFieldEditor and overridden in DirectoryFieldEditor.
	     * Checks whether the text input field contains a valid directory.
	     *
	     * @return True if the apply/ok button should be enabled in the pref panel
	     */
	    @Override
	    protected boolean doCheckState() {
	        String fileName = getTextControl().getText();
	        fileName = fileName.trim();
	        
	        if (fileName.indexOf(',') >= 0 || fileName.indexOf(';') >= 0) {
	            this.setErrorMessage(MessageUtils.getString("invalid_location"));
	            return false;  // Apply/OK must be disabled
	        }
	        
	        File file = new File(fileName);
	        if (!file.isDirectory()) {
	        	this.setErrorMessage(JFaceResources.getString(
	                "DirectoryFieldEditor.errorMessage")); //$NON-NLS-1$
	            return false;
	        }
	        boolean ok = Activator.getDefault().checkSdkLocationAndId(fileName,
	                new Activator.CheckSdkErrorHandler() {
	            @Override
	            public boolean handleError(String message) {
	            	this.setErrorMessage(message.replaceAll("\n", " ")); //$NON-NLS-1$ //$NON-NLS-2$
	                return false;  // Apply/OK must be disabled
	            }
	
				@Override
	            public boolean handleWarning(String message) {
	            	this.showMessage(message.replaceAll("\n", " ")); //$NON-NLS-1$ //$NON-NLS-2$
	                return true;  // Apply/OK must be enabled
	            }
	        });
	        if (ok) clearMessage();
	        return ok;
	    }
	    @Override
	    public Text getTextControl(Composite parent) {
	        setValidateStrategy(VALIDATE_ON_KEY_STROKE);
	        return super.getTextControl(parent);
	    }
	}
}
