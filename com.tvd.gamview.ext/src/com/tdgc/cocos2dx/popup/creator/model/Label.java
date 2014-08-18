package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.constants.ModelType;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Size;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public class Label extends CommonObject {
	
	public Label() {
		super();
		this.mSuffix = "Label";
		this.mDeclareObjectName = "CCLabelTTF";
		this.mType = ModelType.LABEL;
	}
	
	@Override
	public String declare() {
		StringBuilder builder = new StringBuilder();
		builder.append("CCLabelTTF* " + mName + ";");
		
		return builder.toString();
	}
	
	@Override
	public String implement(boolean pInfunction) {
		
		if(pInfunction && isBackground()) {
			return "";
		}
		
		StringBuilder builder = new StringBuilder("\n");
		
		String templateName = "CCLabelTTF";
		if(pInfunction) {
			templateName += " in function";
			mName = getInfunctionName();
		} else {
			if(mParent != null && mParent.getType().equals(ModelType.TABLE)) {
				templateName = "CCMenu non-add";
			}
		}
		
		String template = new FileUtils().fetchTemplate(templateName, 
				"src/com/template/new_label.template", getProject());
		
		String parentName = Config.getInstance().getDefaultParentPopup();
		if(mParent != null) {
			parentName = mParent.getName();
		}
		if(mFontSizeString == null) {
			mFontSizeString = "" + mFontSizeFloat;
		}
		template = template.replace("{var_name}", mName)
			.replace("{tab}", "\t")
			.replace("{parent_name}", parentName)
			.replace("{anchorpoint}", mAnchorPointString)
			.replace("{position_name}", mPositionName)
			.replace("{label_text}", mText)
			.replace("{label_font}", mFont)
			.replace("{label_font_size}", mFontSizeString)
			.replace("{z-index}", mZIndex);
		builder.append(template);
		
		return builder.toString();
	}
	
	public String createLabelTagForXib() {
		StringBuilder builder = new StringBuilder(StringUtils.tab(4) + "<label")
			.append(" opaque=\"NO\" clipsSubviews=\"YES\"")
			.append(" userInteractionEnabled=\"NO\"")
			.append(" contentMode=\"left\"")
			.append(" horizontalHuggingPriority=\"251\"")
			.append(" verticalHuggingPriority=\"251\"")
			.append(" text=\"" + mText + "\"")
			.append(" lineBreakMode=\"tailTruncation\"")
			.append(" baselineAdjustment=\"alignBaselines\"")
			.append(" adjustsFontSizeToFit=\"NO\"")
			.append(" id=\""+ mLabelViewId + "\">");
		float width = mText.length()*mFontSizeFloat;
		builder.append(StringUtils.tab(5))
			.append("\n<rect key=\"frame\" x=\"171\" y=\"162\" "
				+ "width=\"" + width + "\" "
				+ "height=\"" + mFontSizeFloat + "\" />");
		builder.append(StringUtils.tab(5))
			.append("\n<autoresizingMask key=\"autoresizingMask\""
				+ " flexibleMaxX=\"YES\" flexibleMaxY=\"YES\" />");
		builder.append(StringUtils.tab(5))
			.append("\n<fontDescription key=\"fontDescription\" "
				+ "name=\"HelveticaNeue\" family=\"Helvetica Neue\" pointSize=\"17\" />");
		builder.append(StringUtils.tab(5))
			.append("\n<color key=\"textColor\" white=\"1\" "
				+ "alpha=\"1\" colorSpace=\"calibratedWhite\" />");
		builder.append(StringUtils.tab(5))
			.append("\n<nil key=\"highlightedColor\" />\n");
		builder.append(StringUtils.tab(4) + "</label>");
		
		return builder.toString();
	}
	
	public void alignFollowParrent() {
		try {
			float x = this.getLocationInView().getX();
			float y = this.getLocationInView().getY();
			CommonObject parent = getParent();
			if(parent != null) {
				x = x - parent.getLocationInView().getX();
				y = y - parent.getLocationInView().getY();
				
				parent = parent.getParent();
			}
			
			x = x + mAnchorPoint.getX()*mSize.getWidth();
			if(mParent != null) {
				y = mParent.getSize().getHeight() - 
					(y + mAnchorPoint.getY()*mSize.getHeight());
			} else {
				y = y + mAnchorPoint.getY()*mSize.getHeight();
			}
			
			this.setPosition(x, y);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setText(String pText) {
		this.mText = pText;
	}
	
	public void setFont(String pFont) {
		this.mFont = pFont;
	}
	
	public String getText() {
		return this.mText;
	}
	
	public String getFont() {
		return this.mFont;
	}
	
	public float getFontSizeFloat() {
		return this.mFontSizeFloat;
	}
	
	public String getFontSizeString() {
		return this.mSizeString;
	}
	
	public void setShadow(boolean pIsShadow) {
		this.mIsShadow = pIsShadow;
	}
	
	public void setDimension(Size pDimension) {
		this.mDimension = pDimension;
	}
	
	public void setFontSizeFloat(String pFontSize) {
		mFontSizeFloat = Float.parseFloat(pFontSize);
	}
	
	public void setFontSizeString(String pFontSize) {
		this.mFontSizeString = pFontSize;
	}
	
	public void setLabelViewId(String id) {
		this.mLabelViewId = id;
	}
	
	public String getLabelViewId() {
		return this.mLabelViewId;
	}
	
	protected String mLabelViewId;
	protected Size mDimension;
	protected boolean mIsShadow;
	protected String mText;
	protected String mFont;
	protected String mFontSizeString;
	protected float mFontSizeFloat;
}