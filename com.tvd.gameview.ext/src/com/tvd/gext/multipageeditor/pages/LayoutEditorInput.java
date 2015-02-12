package com.tvd.gext.multipageeditor.pages;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;

import com.tvd.cocos2dx.popup.creator.global.Config;
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
		Config.getInstance().setProject(file.getProject());
		mView = fetcher.fetchView(file);
		mExportingElement = new LayoutExportingElement(TreeElement.EXPORTING_LIST 
				+ " (" + mView.getDevice() + ")", mView);
		
		LayoutExportingElement exportPosElement
			= new LayoutExportingElement(TreeElement.EXPORT_POSITIONS);
		LayoutExportingElement exportIdElement
			= new LayoutExportingElement(TreeElement.EXPORT_IDS);
		LayoutExportingElement exportScElement
			= new LayoutExportingElement(TreeElement.EXPORT_SRC_CODE);
		
		mExportingElement.addChilds(new LayoutExportingElement[] {
				new LayoutExportingElement(TreeElement.EXPORT_IMAGES),
				new LayoutExportingElement(TreeElement.EXPORT_XIB_TPL),
				exportIdElement,
				exportPosElement,
				exportScElement,
				new LayoutExportingElement(TreeElement.RELOAD_SIZE),
				new LayoutExportingElement(TreeElement.REFRESH_CONTENT),
		});
		
		exportIdElement.addChilds(new LayoutExportingElement[] {
				new LayoutExportingElement(TreeElement.DECLARE_IDS),
				new LayoutExportingElement(TreeElement.IMPLEMENT_IDS),
			});
		exportPosElement.addChilds(new LayoutExportingElement[] {
				new LayoutExportingElement(TreeElement.DECLARE_POSITIONS),
				new LayoutExportingElement(TreeElement.IMPLEMENT_POSITIONS),
			});
		exportScElement.addChilds(new LayoutExportingElement[] {
				new LayoutExportingElement(TreeElement.DECLARE_CLASS),
				new LayoutExportingElement(TreeElement.IMPLEMENT_CLASS),
			});
	}
	
	protected View mView;
	protected LayoutExportingElement mExportingElement;
	
}
