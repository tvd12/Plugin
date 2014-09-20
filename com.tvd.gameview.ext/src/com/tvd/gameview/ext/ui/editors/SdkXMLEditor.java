package com.tvd.gameview.ext.ui.editors;

import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

public class SdkXMLEditor extends AbstractDecoratedTextEditor {

	public SdkXMLEditor() {
		setDocumentProvider(new TextFileDocumentProvider());
	}
}
