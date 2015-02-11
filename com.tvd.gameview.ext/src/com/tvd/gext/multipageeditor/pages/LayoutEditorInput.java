package com.tvd.gext.multipageeditor.pages;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;

import com.tvd.cocos2dx.popup.creator.model.View;
import com.tvd.cocos2dx.popup.creator.xml.XmlFetcher;
import com.tvd.gameview.ext.constants.Constant.TreeElement;
import com.tvd.gext.multipageeditor.exporting.pages.LayoutExportingElement;

public class LayoutEditorInput extends FileEditorInput {

	public LayoutEditorInput(IFile file) {
		super(file);
		this.init(file);
	}
	
	public LayoutExportingElement getExportingElement() {
		return mExportingElement;
	}
	
	public View getView() {
		return mView;
	}
	
	protected void init(IFile file) {
		XmlFetcher fetcher = new XmlFetcher();
		mView = fetcher.fetchView(file);
		mExportingElement = new LayoutExportingElement(TreeElement.EXPORTING_LIST 
				+ " (" + mView.getDevice() + ")");
		mExportingElement.addChilds(new LayoutExportingElement[] {
				new LayoutExportingElement(TreeElement.EXPORT_IMAGES),
				new LayoutExportingElement(TreeElement.EXPORT_XIB_TPL),
				new LayoutExportingElement(TreeElement.EXPORT_IDS),
				new LayoutExportingElement(TreeElement.EXPORT_POSITIONS),
				new LayoutExportingElement(TreeElement.EXPORT_SRC_CODE),
				new LayoutExportingElement(TreeElement.RELOAD_SIZE),
		});
	}
	
	protected View mView;
	protected LayoutExportingElement mExportingElement;
	
}
