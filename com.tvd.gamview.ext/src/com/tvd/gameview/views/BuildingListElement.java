package com.tvd.gameview.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;

public class BuildingListElement {
	public BuildingListElement() {
		
	}
	
	public BuildingListElement(String name) {
		this.mName = name;
	}
	
	public BuildingListElement(String name, IProject project) {
		this.mName = name;
		this.mProject = project;
	}
	
	public BuildingListElement(String name, String device) {
		this.mName = name;
		this.mDevice = device;
	}
	
	public BuildingListElement(String name, String[] strs) {
		this(name);
		this.addChilds(strs);
	}
	
	public BuildingListElement(String name, BuildingListElement[] elements) {
		this(name);
		this.addChilds(elements);
	}

	public void addChilds(String[] strs) {
		for(String str : strs) {
			this.addChild(str);
		}
	}
	
	public void addChilds(BuildingListElement[] elements) {
		for(BuildingListElement e : elements) {
			this.addChild(e);
		}
	}
	
	public void addChild(String str) {
		this.addChild(new BuildingListElement(str));
	}
	
	public void addChild(BuildingListElement element) {
		mChilds.add(element);
		element.setParent(this);
	}
	
	public List<BuildingListElement> getChilds() {
		return mChilds;
	}
	
	public BuildingListElement getChildAt(int index) {
		return mChilds.get(index);
	}
	public String getName() {
		return mName;
	}
	
	public IProject getProject() {
		IProject project = mProject;
		BuildingListElement parent = this.getParent();
		while(project == null && parent != null) {
			project = parent.mProject;
			parent = parent.getParent();
		}
		return project;
	}
	
	public BuildingListElement getParent() {
		return mParent;
	}
	
	public void setParent(BuildingListElement parent) {
		mParent = parent;
	}
	
	public String getDevice() {
		return mDevice;
	}
	
	public String getFilePath() {
		String filePath = mFilePath;
		BuildingListElement parent = this.getParent();
		while(filePath == null && parent != null) {
			filePath = parent.mFilePath;
			parent = parent.getParent();
		}
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.mFilePath = filePath;
	}
	
	@Override
	public String toString() {
		return mName;
	}
	
	public BuildingListElement copy() {
		BuildingListElement copy = new BuildingListElement();
		copy.mChilds = mChilds;
		copy.mName = mName;
		copy.mDevice = mDevice;
		copy.mFilePath = mFilePath;
		
		return copy;
	}
	
	private List<BuildingListElement> mChilds = 
			new ArrayList<BuildingListElement>();
	
	private BuildingListElement mParent;
	private IProject mProject;
	private String mName;
	private String mDevice;
	private String mFilePath;
}