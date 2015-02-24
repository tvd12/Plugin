package com.tvd.gext.multipageeditor.exporting.pages;

import java.util.ArrayList;
import java.util.List;

import com.tvd.cocos2dx.popup.creator.model.View;

public class LayoutExportingElement {
	
	public LayoutExportingElement(String name) {
		mName = name;
	}

	public LayoutExportingElement(String name, View view) {
		this(name);
		mView = view;
	}
	
	public void addChilds(LayoutExportingElement[] elements) {
		mChilds = new ArrayList<LayoutExportingElement>();
		for(int i = 0 ; i < elements.length ; i++) {
			elements[i].setParent(this);
			elements[i].mView = getView();
			mChilds.add(elements[i]);
		}
	}
	
	public boolean hasChildren() {
		return mChilds != null 
				&& mChilds.size() > 0;
	}
	
	public LayoutExportingElement getParent() {
		return this.mParent;
	}
	
	public View getView() {
		return this.mView;
	}
	
	public String getName() {
		return this.mName;
	}
	
	public List<LayoutExportingElement> getChilds() {
		return mChilds;
	}
	
	protected void setParent(LayoutExportingElement parent) {
		mParent = parent;
		mView = parent.mView;
	}
	
	public void update(View view) {
		mView = view;
		if(mChilds == null) {
			return;
		}
		for(int i = 0 ; i < mChilds.size() ; i++) {
			mChilds.get(i).update(view);
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	protected LayoutExportingElement mParent;
	protected List<LayoutExportingElement> mChilds;
	protected View mView;
	protected String mName;
}
