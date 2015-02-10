package com.tvd.gext.multipageeditor.pages;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;

public class LayoutEditorInput extends FileEditorInput {

	public LayoutEditorInput(IFile file) {
		super(file);
		mModel = new LayoutViewModel();
	}
	
	public LayoutViewModel getModel() {
		return mModel;
	}
	
	private LayoutViewModel mModel;
	
}
