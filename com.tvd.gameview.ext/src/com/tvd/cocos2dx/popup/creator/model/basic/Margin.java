package com.tvd.cocos2dx.popup.creator.model.basic;

import com.tvd.cocos2dx.popup.creator.constants.Attribute;
import com.tvd.cocos2dx.popup.creator.constants.Tag;

public class Margin {

	public Float getLeft() {
		return mLeft;
	}
	public void setLeft(Float pLeft) {
		mLeft = pLeft;
	}
	
	public Float getTop() {
		return mTop;
	}
	public void setTop(Float pTop) {
		mTop = pTop;
	}
	public Float getRight() {
		return mRight;
	}
	public void setRight(Float pRight) {
		mRight = pRight;
	}
	public Float getBottom() {
		return mBottom;
	}
	public void setBottom(Float pBottom) {
		mBottom = pBottom;
	}
	
	public String getLeftString() {
		return (getLeft() != null) 
				? ("" + getLeft().floatValue()) : "";
				
	}
	
	public String getTopString() {
		return (getTop() != null) 
				? ("" + getTop().floatValue()) : "";
				
	}
	
	public String getRightString() {
		return (getRight() != null) 
				? ("" + getRight().floatValue()) : "";
				
	}
	
	public String getBottomString() {
		return (getBottom() != null) 
				? ("" + getBottom().floatValue()) : "";
				
	}
	
	public String toXML() {
		StringBuilder builder = new StringBuilder()
			.append("<" + Tag.MARGIN);
		if(getLeft() != null) {
			builder.append(" ").append(Attribute.LEFT + "=")
			.append("\"" + getLeft().floatValue() + "\"");
		}
		if(getTop() != null) {
			builder.append(" ")
			.append(Attribute.TOP + "=")
			.append("\"" + getTop().floatValue() + "\"");
		}
		if(getRight() != null) {
			builder.append(" ")
			.append(Attribute.RIGHT + "=")
			.append("\"" + getRight().floatValue() + "\"");	
		}
		if(getBottom() != null) {
			builder.append(" ")
			.append(Attribute.BOTTOM + "=")
			.append("\"" + getBottom().floatValue() + "\"");
		}
		return builder.append(" />").toString();
	}
	
	public String toTemplateName() {
		StringBuilder builder = new StringBuilder("Margin");
		if(getLeft() != null) {
			builder.append("-Left");
		}
		if(getTop() != null) {
			builder.append("-Top");
		}
		if(getRight() != null) {
			builder.append("-Right");
		}
		if(getBottom() != null) {
			builder.append("-Bottom");
		}
		
		return builder.toString();
	}
	
	@Override
	public String toString() {
		return mLeft + ", " + mTop + ", " + mRight + ", " + mBottom;
	}

	protected Float mLeft;
	protected Float mTop;
	protected Float mRight;
	protected Float mBottom;
}
