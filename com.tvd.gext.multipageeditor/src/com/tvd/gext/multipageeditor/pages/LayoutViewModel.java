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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;

import com.tvd.cocos2dx.popup.creator.model.View;
import com.tvd.cocos2dx.popup.creator.xml.XmlFetcher;
import com.tvd.gext.multipageeditor.elements.LayoutElement;
import com.tvd.gext.multipageeditor.elements.LayoutViewElement;
/**
 * @author dejan
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class LayoutViewModel {
	
	public LayoutViewModel() {
		modelListeners = new ArrayList<IModelListener>();
		initialize();
	}
	
	public LayoutViewModel(IFile file) {
		this.mFile = file;
	}
	
	public void addModelListener(IModelListener listener) {
		if (!modelListeners.contains(listener))
			modelListeners.add(listener);
	}
	public void removeModelListener(IModelListener listener) {
		modelListeners.remove(listener);
	}
	public void fireModelChanged(Object[] objects, String type, String property) {
		for (int i = 0; i < modelListeners.size(); i++) {
			((IModelListener) modelListeners.get(i)).modelChanged(objects,
					type, property);
		}
	}
	public Object[] getContents() {
		return mObjects.toArray();
	}
	private void initialize() {
		XmlFetcher xmlFetcher = new XmlFetcher();
		mView = xmlFetcher.fetchView(mFile);
		mObjects = new ArrayList<LayoutElement>();
		LayoutElement[] objects = {
			new LayoutViewElement(this),	
		};
		add(objects, false);
	}
	public void add(LayoutElement[] objs, boolean notify) {
		for (int i = 0; i < objs.length; i++) {
			mObjects.add(objs[i]);
			objs[i].setModel(this);
		}
		if (notify)
			fireModelChanged(objs, IModelListener.ADDED, ""); //$NON-NLS-1$
	}
	public void remove(LayoutElement[] objs, boolean notify) {
		for (int i = 0; i < objs.length; i++) {
			mObjects.remove(objs[i]);
			objs[i].setModel(null);
		}
		if (notify)
			fireModelChanged(objs, IModelListener.REMOVED, ""); //$NON-NLS-1$
	}
	
	protected IFile mFile;
	protected View mView;
	private List<IModelListener> modelListeners;
	private List<LayoutElement> mObjects;
}