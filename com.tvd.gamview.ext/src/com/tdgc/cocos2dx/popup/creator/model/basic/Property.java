package com.tdgc.cocos2dx.popup.creator.model.basic;

import com.tdgc.cocos2dx.popup.creator.global.Config;

public class Property extends Parameter {
	
	public Property() {
		super();
	}
	
	public Property(Parameter pParameter) {
		super();
		this.mName = pParameter.mName;
		this.mType = pParameter.mType;
		this.mKind = pParameter.mKind;
	}
	
	@Override
	public String toString() {
		if(mType == null || mName == null) {
			return "";
		}
		String result = Config.getInstance().getDefaultNormalPrefix();

		if(mKind.equals(Kind.POINTER)
				|| mKind.equals(Kind.CONSTANT_POINTER)) {
			result = mType + "* " + result + mName;
		}
		else {
			result = mType + " " + result + mName;
		}
		
		return result + ";";
	}
	
	@Override
	public String getName() {
		return Config.getInstance().getDefaultNormalPrefix() 
				+ this.mName;
	}
}
