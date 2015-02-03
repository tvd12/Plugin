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
package org.eclipse.ui.forms.article.rcp;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.forms.article.OpenFormEditorAction;
/**
 * @see IWorkbenchWindowActionDelegate
 */
public class OpenSimpleFormEditorAction extends OpenFormEditorAction {
	public void run(IAction action) {
		openEditor(new SimpleFormEditorInput(Messages.getString("OpenSimpleFormEditorAction.name")), "org.eclipse.ui.forms.article.base-editor"); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
