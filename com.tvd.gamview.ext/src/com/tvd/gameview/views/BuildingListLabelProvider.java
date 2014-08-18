package com.tvd.gameview.views;


import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

public class BuildingListLabelProvider extends LabelProvider
		implements IStyledLabelProvider, IFontProvider {
	
	public BuildingListLabelProvider(ImageRegistry mImageRegistry, FontRegistry mFontRegistry) {
		this.mImageRegistry = mImageRegistry;
		this.mFontRegistry = mFontRegistry;
	}
	
	@Override
	public String getText(Object element) {
		if(element instanceof BuildingListElement) {
			return element.toString();
		}
		else {
			return "Unknown type: " + element.getClass();
		}
	}
	
	public Image getImage(Object element) {
		if(element instanceof BuildingListElement) {
			return mImageRegistry.get("iconURL");
		} else {
			return super.getImage(element);
		}
	}

	@Override
	public StyledString getStyledText(Object element) {
		String text = getText(element);
		StyledString ss = new StyledString(text);
		if(element instanceof BuildingListElement) {
			ss.append(" (" + element.toString() + ")",
					StyledString.DECORATIONS_STYLER);
		}
		return ss;
	}

	@Override
	public Font getFont(Object element) {
		Font italic = mFontRegistry.getItalic(
				JFaceResources.DEFAULT_FONT);
		return italic;
	}
	
	private final ImageRegistry mImageRegistry;
	private final FontRegistry mFontRegistry;
	
}