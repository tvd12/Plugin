package com.tvd.gext.multipageeditor.pages;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import com.tvd.gameview.ext.GameViewSdk;
import com.tvd.gext.multipageeditor.editors.constant.Img;

public class LayoutExportingPage extends FormPage 
		implements ILayoutUpdateable {
	
	public LayoutExportingPage(FormEditor pEditor) {
		super(pEditor, LayoutExportingPage.class.getName(), 
				text("title"));
		mBlock = new LayoutScrolledExportingBlock(this);
	}
	
	public static LayoutExportingPage create(FormEditor editor) 
			throws PartInitException {
		LayoutExportingPage page = new LayoutExportingPage(editor);
		editor.addPage(page);
		
		return page;
	}
	
	@Override
	public void update() {
		mBlock.update();
	}
	
	
	@Override
	protected void createFormContent(final IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		form.setText(text("title"));
		form.setBackgroundImage(GameViewSdk.getDefault()
				.getImageRegistry().get(Img.LAYOUT_FORM_BG));
		mBlock.createContent(managedForm);
	}
	
	private static String text(String key) {
		String className = LayoutExportingPage.class.getSimpleName();
		return Messages.getString(className + "." + key);
	}
	
	private LayoutScrolledExportingBlock mBlock;
}
