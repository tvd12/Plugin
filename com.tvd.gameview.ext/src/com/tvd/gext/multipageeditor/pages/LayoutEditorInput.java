package com.tvd.gext.multipageeditor.pages;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;

import com.tvd.cocos2dx.popup.creator.model.View;
import com.tvd.cocos2dx.popup.creator.xml.XmlFetcher;

public class LayoutEditorInput extends FileEditorInput {

	public LayoutEditorInput(IFile file) {
		super(file);
		XmlFetcher fetcher = new XmlFetcher();
		mView = fetcher.fetchView(file);
	}
	
	public View getView() {
		return mView;
	}
	
	protected View mView;
	
}
