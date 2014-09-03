package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public class Interface {
	
	public Interface() {
		this.mXmlTagName = Tag.NEXT;
	}
	
	public Interface(CommonObject core) {
		this();
		this.mCore = core;
		this.mCore.setInterface(this);
	}
	
	public Interface(CommonObject core, Image img) {
		this(core);
		this.mImage = img;
		this.mCore.setInterface(this);
		this.mImage.setTabCount(mCore.getTabCount() + 1);
		this.mImage.setParent(mCore);
	}
	
	public void setImage(Image image) {
		this.mImage = image;
	}
	
	public Image getImage() {
		return this.mImage;
	}
	
	public void setCore(CommonObject core) {
		this.mCore = core;
	}
	
	public CommonObject getCore() {
		return this.mCore;
	}
	
	public String toXML() {
		String tab = StringUtils.tab(mCore.getTabCount());
		StringBuilder builder = new StringBuilder(tab);
		builder.append("<" + mXmlTagName + " ")
			.append(Attribute.VISIBLE + "=\"true\" ")
			.append(Attribute.COMMENT + "=\"\">");
		builder.append("\n" + tab + "\t");
		builder.append("<" + Tag.POSITION + " " + Attribute.VALUE 
				+ "=\"" + mCore.getPositionString() + "\" />");

		builder.append("\n" + mImage.toXML());
		builder.append("\n")
			.append(tab)
			.append("</" + mXmlTagName + ">");
		
		builder.append("\n");
		
		return builder.toString();
	}
	
	private CommonObject mCore;
	private String mXmlTagName;
	private Image mImage;
}
