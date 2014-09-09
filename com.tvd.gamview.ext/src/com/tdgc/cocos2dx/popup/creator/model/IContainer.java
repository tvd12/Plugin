package com.tdgc.cocos2dx.popup.creator.model;

public interface IContainer {
	public void addImage(Image image);
	public void setContainerParent(IContainer parent);
	public IContainer getContainerParent();
}
