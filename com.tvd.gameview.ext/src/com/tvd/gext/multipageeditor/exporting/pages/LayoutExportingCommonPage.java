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
package com.tvd.gext.multipageeditor.exporting.pages;

import com.tvd.gext.multipageeditor.elements.pages.LayoutCommonPage;
/**
 *
 */
public class LayoutExportingCommonPage extends LayoutCommonPage {
	
	@Override
	protected String text(String key) {
		String className = getClass().getSimpleName();
		return Messages.getString(className + "." + key);
	}
	
	@Override
	protected String commontext(String key) {
		return Messages.getString("Element" + "." + key);
	}

	@Override
	protected void update(Object pModel) {
		
	}
	
}