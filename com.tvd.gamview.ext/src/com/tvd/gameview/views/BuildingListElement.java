package com.tvd.gameview.views;

import java.util.ArrayList;
import java.util.List;

public class BuildingListElement {
	public BuildingListElement() {
		
	}
	
	public BuildingListElement(String name) {
		this.mName = name;
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
		mChilds.add(new BuildingListElement(str));
	}
	
	public void addChild(BuildingListElement element) {
		mChilds.add(element);
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
	
	@Override
	public String toString() {
		return mName;
	}
	
	private List<BuildingListElement> mChilds = 
			new ArrayList<BuildingListElement>();
	private String mName;
}
