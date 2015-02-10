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
package com.tvd.gext.multipageeditor.elements;

import com.tvd.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tvd.gext.multipageeditor.pages.IModelListener;
import com.tvd.gext.multipageeditor.pages.LayoutViewModel;

/**
 *
 */
public	class LayoutElement {
	
	public LayoutElement() {
	}
	
	public LayoutElement(LayoutViewModel model) {
		setModel(model);
	}
	
	public void setModel(LayoutViewModel model) {
		this.mModel = model;
	}

	public String toString() {
		if(mViewObject instanceof String) {
			return mViewObject.toString();
		}
		else if(mViewObject instanceof CommonObject) {
			return ((CommonObject)mViewObject).getTagName();
		}
		return "element";
	}
	public void setName(String name) {
		mModel.fireModelChanged(new Object [] {this}, IModelListener.CHANGED, ""); //$NON-NLS-1$
	}
	
	public void setViewObject(Object obj) {
		this.mViewObject = obj;
	}
	
	public Object getViewObject() {
		return this.mViewObject;
	}
	
	protected Object mViewObject;
	protected LayoutViewModel mModel;
}