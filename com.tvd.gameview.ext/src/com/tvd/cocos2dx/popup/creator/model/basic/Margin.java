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
	
	public Float getCenterLeft() {
		return mCenterLeft;
	}
	public void setCenterLeft(Float pCenterLeft) {
		mCenterLeft = pCenterLeft;
	}
	public Float getCenterTop() {
		return mCenterTop;
	}
	public void setCenterTop(Float pCenterTop) {
		mCenterTop = pCenterTop;
	}
	public Float getCenterRight() {
		return mCenterRight;
	}
	public void setCenterRight(Float pCenterRight) {
		mCenterRight = pCenterRight;
	}
	public Float getCenterBottom() {
		return mCenterBottom;
	}
	public void setCenterBottom(Float pCenterBottom) {
		mCenterBottom = pCenterBottom;
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
	
	public String getCenterLeftString() {
		return (getCenterLeft() != null) 
				? ("" + getCenterLeft().floatValue()) : "";
				
	}
	
	public String getCenterTopString() {
		return (getCenterTop() != null) 
				? ("" + getCenterTop().floatValue()) : "";
				
	}
	
	public String getCenterRightString() {
		return (getCenterRight() != null) 
				? ("" + getCenterRight().floatValue()) : "";
				
	}
	
	public String getCenterBottomString() {
		return (getCenterBottom() != null) 
				? ("" + getCenterBottom().floatValue()) : "";
				
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
		if(getCenterLeft() != null) {
			builder.append(" ")
			.append(Attribute.CENTER_LEFT + "=")
			.append("\"" + getCenterLeft().floatValue() + "\"");
		}
		if(getCenterTop() != null) {
			builder.append(" ")
			.append(Attribute.CENTER_TOP + "=")
			.append("\"" + getCenterTop().floatValue() + "\"");
		}
		if(getCenterRight() != null) {
			builder.append(" ")
			.append(Attribute.CENTER_RIGHT + "=")
			.append("\"" + getCenterRight().floatValue() + "\"");
		}
		if(getCenterBottom() != null) {
			builder.append(" ")
			.append(Attribute.CENTER_BOTTOM + "=")
			.append("\"" + getCenterBottom().floatValue() + "\"");
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
		if(getCenterLeft() != null) {
			builder.append("-Center-Top");
		}
		if(getCenterTop() != null) {
			builder.append("-Center-Top");
		}
		if(getCenterRight() != null) {
			builder.append("-Center-Right");
		}
		if(getCenterBottom() != null) {
			builder.append("-Center-Bottom");
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
	
	protected Float mCenterLeft;
	protected Float mCenterTop;
	protected Float mCenterRight;
	protected Float mCenterBottom;
}
