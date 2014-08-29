package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.ModelType;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Point;
import com.tdgc.cocos2dx.popup.creator.model.basic.Size;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public class Label extends CommonObject {
	
	public Label() {
		super();
		this.mSuffix = "Label";
		this.mDeclareObjectName = "CCLabelTTF";
		this.mType = ModelType.LABEL;
		this.setPosition(10, 10);
		this.mXmlTagName = Tag.LABEL;
		this.mLocationInView = new Point(10, 10);
		this.mFontName = "HelveticaNeue";
		this.mFontFamily = "Helvetica Neue";
		this.mFontSizeFloat = 17.0f;
		
		this.mTemplateName = "CCLabelTTF";
		this.mTemplateFile = "label.template";
	}
	
	@Override
	public String implement(boolean pInfunction) {
		
		if(pInfunction && isBackground()) {
			return "";
		}
		
		StringBuilder builder = new StringBuilder("\n");
		String template = fetchTemplate(pInfunction);
		
		String parentName = Config.getInstance()
				.getDefaultBackgroundOnSupers(mParent.getType());
		if(mParent != null) {
			parentName = mParent.getName();
		}
		String fontSizeVar = (mFontSizeVar == null) 
				? ("" + mFontSizeFloat) : mFontSizeVar;
		String fontNameVar = (mFontNameVar == null) 
				? ("\"" + mFontName + "\"") : mFontName;
		
		template = template.replace("{var_name}", mName)
			.replace("{tab}", "\t")
			.replace("{parent_name}", parentName)
			.replace("{anchorpoint}", mAnchorPointString)
			.replace("{position_name}", mPositionName)
			.replace("{label_text}", mText)
			.replace("{label_font}", fontNameVar)
			.replace("{label_font_size}", fontSizeVar)
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
		float width = (mText.length()*mFontSizeFloat)/2;
		builder.append("\n" + StringUtils.tab(5))
			.append("<rect key=\"frame\" x=\""+ mLocationInView.getX() 
					+"\" y=\""+ mLocationInView.getY() + "\" "
					+ "width=\"" + width + "\" "
					+ "height=\"" + mFontSizeFloat + "\" />");
		builder.append("\n" + StringUtils.tab(5))
			.append("<autoresizingMask key=\"autoresizingMask\""
				+ " flexibleMaxX=\"YES\" flexibleMaxY=\"YES\" />");
		builder.append("\n" + StringUtils.tab(5))
			.append("<fontDescription key=\"fontDescription\" "
				+ "name=\""+ mFontName +"\" family=\""+ mFontFamily + "\" pointSize=\"" 
					+ mFontSizeFloat + "\" />");
		builder.append("\n" + StringUtils.tab(5))
			.append("<color key=\"textColor\" white=\"1\" "
				+ "alpha=\"1\" colorSpace=\"calibratedWhite\" />");
		builder.append("\n" + StringUtils.tab(5))
			.append("<nil key=\"highlightedColor\" />\n");
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
			
			float anchorpointY = 1 - mAnchorPoint.getY();
			x = x + mAnchorPoint.getX()*mSize.getWidth();
			if(mParent != null) {
				y = mParent.getSize().getHeight() - 
					(y + anchorpointY*mSize.getHeight());
			} else {
				y = y + anchorpointY*mSize.getHeight();
			}
			
			this.setPosition(x, y);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setText(String pText) {
		this.mText = pText;
	}
	
	public void setFontName(String pFontName) {
		this.mFontName = pFontName;
	}
	
	public String getFontName() {
		return this.mFontName;
	}
	
	public String getText() {
		return this.mText;
	}
	
	public float getFontSizeFloat() {
		return this.mFontSizeFloat;
	}
	
	public String getFontSizeVar() {
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
	
	public void setFontSizeVar(String pFontSize) {
		this.mFontSizeVar = pFontSize;
	}
	
	public void setLabelViewId(String id) {
		this.mLabelViewId = id;
	}
	
	public String getLabelViewId() {
		return this.mLabelViewId;
	}
	
	public void setFontFamily(String family) {
		this.mFontFamily = family;
	}
	
	public void setFontNameVar(String varname) {
		this.mFontNameVar = varname;
	}
	
	@Override
	public CommonObject clone() {
		Label label = new Label();
		this.setAllPropertiesForObject(label);
		return label;
	}
	
	@Override
	public String toXML() {
		String tab = StringUtils.tab(mTabCount);
		StringBuilder builder = new StringBuilder(tab);
		builder.append("<" + mXmlTagName + " " + Attribute.VISIBLE + "=\"true\" ")
			.append(Attribute.COMMENT + "=\"\">");
		
		builder.append("\n" + tab + "\t")
		.append("<" + Tag.TEXT + " " + Attribute.VALUE 
				+ "=\"" + mText + "\" />");
		builder.append("\n" + tab + "\t")
		.append("<" + Tag.FONT + " " + Attribute.NAME 
				+ "=\"" + mFontName + "\" ")
				.append(Attribute.FAMILY + "=\"" + mFontFamily + "\" />");
		builder.append("\n" + tab + "\t")
		.append("<" + Tag.FONT_SIZE + " " + Attribute.VALUE 
				+ "=\"" + mFontSizeFloat + "\" />");
		builder.append("\n" + tab + "\t")
			.append("<" + Tag.POSITION_NAME + " " + Attribute.VALUE 
					+ "=\"" + mXmlPositionName + "\" />");
		builder.append("\n" + tab + "\t")
			.append("<" + Tag.ANCHORPOINT + " " + Attribute.VALUE 
					+ "=\"" + mAnchorPointString + "\" />");
		builder.append("\n" + tab + "\t")
			.append("<" + Tag.POSITION + " " + Attribute.VALUE 
					+ "=\"" + mPosition + "\" />");
		builder.append("\n" + tab + "\t")
			.append("<" + Tag.LOCATION_IN_INTERFACEBUILDER + " " + Attribute.VALUE 
					+ "=\"" + mLocationInView + "\" />");
		builder.append("\n" + tab + "\t")
			.append("<" + Tag.Z_INDEX + " " + Attribute.VALUE 
					+ "=\"" + mZIndex + "\" />");
		
		builder.append("\n")
			.append(tab)
			.append("</" + mXmlTagName + ">");
		
		builder.append("\n");
		
		return builder.toString();
	}
	
	protected String mFontFamily;
	protected String mLabelViewId;
	protected Size mDimension;
	protected boolean mIsShadow;
	protected String mText;
	protected String mFontName;
	protected String mFontSizeVar;
	protected float mFontSizeFloat;
	protected String mFontNameVar;
	
}
