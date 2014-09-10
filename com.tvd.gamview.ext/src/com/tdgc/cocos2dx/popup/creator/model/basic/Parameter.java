package com.tdgc.cocos2dx.popup.creator.model.basic;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public class Parameter {
	
	public Parameter() {
		this.mType = null;
		this.mName = null;
		this.mKind = Kind.NORMAL;
		this.mXmlTagName = Tag.PARAMETER;
	}
	
	public Parameter(String pType, String pName) {
		this.mType = pType;
		this.mName = pName;
	}

	public void setType(String pType) {
		this.mType = pType;
	}
	
	public void setName(String pName) {
		this.mName = pName;
	}
	
	public void setKind(String pKind) {
		if(pKind == null) {
			this.mKind = Kind.NORMAL;
		} else {
			this.mKind = pKind;
		}
	}
	
	public String getType() {
		return this.mType;
	}
	
	public String getName() {
		return "p" + this.mName;
	}
	
	public String getKind() {
		return this.mKind;
	}
	
	@Override
	public String toString() {
		if(mType == null || mName == null) {
			return "";
		}
		String result = "p";
		if(mKind.equals(Kind.REF)) {
			result = this.mType + "& " + result + this.mName;
		}
		else if(mKind.equals(Kind.POINTER)) {
			result = this.mType + "* " + result + this.mName; 
		}
		else if(mKind.equals(Kind.CONSTANT_NORMAL)) {
			result = "const " + this.mType + " " + result + this.mName;
		}
		else if(mKind.equals(Kind.CONSTANT_REF)) {
			result = "const " + this.mType + "& " + result + this.mName;
		}
		else if(mKind.equals(mKind.equals(Kind.CONSTANT_POINTER))) {
			result = "const " + this.mType + "* " + result + this.mName;
		}
		else {
			result = this.mType + " " + result + this.mName;
		}
		return result + ",";
	}
	
	public void setTabCount(int tabCount) {
		this.mTabCount = tabCount;
	}
	
	public String toXML() {
		String tab = StringUtils.tab(mTabCount);
		StringBuilder builder = new StringBuilder(tab);
		builder.append("<" + mXmlTagName + " ")
			.append(Attribute.NAME + "=\"" + mName + "\" ")
			.append(Attribute.TYPE + "=\"" + mType + "\" ")
			.append(Attribute.KIND + "=\"" + mKind + "\" />");
		
		return builder.toString();
	}
	
	public static interface Kind {
		public static final String NORMAL 		= "normal";
		public static final String REF 			= "ref";
		public static final String POINTER 		= "pointer";
		public static final String CONSTANT_NORMAL		= "const-normal";
		public static final String CONSTANT_REF			= "const-ref";
		public static final String CONSTANT_POINTER		= "const-pointer";
	}
	
	protected String mType;
	protected String mName;
	protected String mKind;
	protected String mXmlTagName;
	
	protected int mTabCount;
	
}
