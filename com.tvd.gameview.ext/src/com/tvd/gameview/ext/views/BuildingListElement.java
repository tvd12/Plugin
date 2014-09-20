package com.tvd.gameview.ext.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.View;
import com.tdgc.cocos2dx.popup.creator.xml.XmlFetcher;

public class BuildingListElement {
	public BuildingListElement() {
		mChilds = new ArrayList<BuildingListElement>();
	}
	
	public BuildingListElement(String name) {
		this();
		this.mName = name;
	}
	
	public BuildingListElement(String name, IProject project) {
		this(name);
		this.mProject = project;
	}
	
	public BuildingListElement(String name, String device) {
		this(name);
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
	
	public void removeChildAt(int index) {
		mChilds.remove(index);
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
	
	public BuildingListElement copy() {
		BuildingListElement copy = new BuildingListElement();
		copy.mChilds = mChilds;
		copy.mName = mName;
		copy.mDevice = mDevice;
		copy.mFilePath = mFilePath;
		
		return copy;
	}
	
	@Override
	public String toString() {
		return mName;
	}
	
	public String info() {
		StringBuilder builder = new StringBuilder();
		builder.append("name = " + mName)
			.append(",parent = " + mParent.getName())
			.append(",number of child = " + mChilds.size())
			.append(",device = " + mDevice)
			.append(",file path = " + mFilePath);
		
		return builder.toString();
	}
	
	public View getViewForRootElement() {
		View view = null;
		if(mParent.getParent() == null) {
			Config.getInstance().setProject(this.getProject());
			final IFile xmlFile = this.getProject().getFile(this.getFilePath());
			final XmlFetcher xmlFetcher = new XmlFetcher();
			view = xmlFetcher.fetchView(xmlFile);
			view.statisticsNumberOfItems();
		}
		return view;
	}
	
	public void update() {
		View view = getViewForRootElement();
		if(view != null) {
//			removeChildAt(2);
//			removeChildAt(3);
		}
	}
	
	private List<BuildingListElement> mChilds;	
	private BuildingListElement mParent;
	private IProject mProject;
	private String mName;
	private String mDevice;
	private String mFilePath;
}
