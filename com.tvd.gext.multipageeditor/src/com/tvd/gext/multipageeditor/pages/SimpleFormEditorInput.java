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

public class SimpleFormEditorInput extends FormEditorInput {
	private LayoutViewModel model;
	
	public SimpleFormEditorInput(String name) {
		super(name);
		model = new LayoutViewModel();
	}
	
	public LayoutViewModel getModel() {
		return model;
	}
}