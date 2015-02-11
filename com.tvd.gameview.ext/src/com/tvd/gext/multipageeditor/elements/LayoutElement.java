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

/**
 *
 */
public	class LayoutElement {
	
	public LayoutElement() {
	}
	
	public String toString() {
		if(mViewObject instanceof String) {
			return mViewObject.toString();
		}
		else if(mViewObject instanceof CommonObject) {
			return ((CommonObject)mViewObject).getXMLTagName();
		}
		return "unknown";
	}
	
	public void setViewObject(Object obj) {
		this.mViewObject = obj;
	}
	
	public Object getViewObject() {
		return this.mViewObject;
	}
	
	protected Object mViewObject;
}