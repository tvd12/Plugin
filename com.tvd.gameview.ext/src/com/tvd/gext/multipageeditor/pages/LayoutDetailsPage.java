/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.tvd.gext.multipageeditor.pages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import com.tvd.gameview.ext.GameViewSdk;
import com.tvd.gext.multipageeditor.editors.constant.Img;
/**
 * @author dejan
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class LayoutDetailsPage extends FormPage {
	
	public LayoutDetailsPage(FormEditor editor) {
		super(editor, LayoutDetailsPage.class.getName(), text("title")); //$NON-NLS-1$ //$NON-NLS-2$
		mBlock = new LayoutScrolledPropertiesBlock(this);
	}
	
	public static LayoutDetailsPage create(FormEditor editor) 
			throws PartInitException {
		LayoutDetailsPage page = new LayoutDetailsPage(editor);
		editor.addPage(page);
		
		return page;
	}
	
	protected void createFormContent(final IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		//FormToolkit toolkit = managedForm.getToolkit();
		form.setText(text("title")); //$NON-NLS-1$
		form.setBackgroundImage(GameViewSdk.getDefault()
				.getImageRegistry().get(Img.LAYOUT_FORM_BG));
		mBlock.createContent(managedForm);
	}
	
	private static String text(String key) {
		String className = LayoutDetailsPage.class.getSimpleName();
		return Messages.getString(className + "." + key);
	}
	
	private LayoutScrolledPropertiesBlock mBlock;
}