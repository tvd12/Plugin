package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.AdvancedObject;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;
import com.tdgc.cocos2dx.popup.creator.utils.ViewUtils;

public class AdvancedSprite extends AdvancedObject {
	public AdvancedSprite() {
		super();
		mTemplateFile = "sprite.template";
		mTemplateName = "CCSprite";
		mSuper = "CCSprite";
	}
	
	@Override
	public void update() {
		super.update();
		mClassName = StringUtils.convertToClassName(
				mBasicObject.getXmlPositionName() + "sprite",
				Config.getInstance().getDefaultViewSuffix());
		if(mPrefix == null) {
			setPrefix("sprite_" + mBasicObject.getXmlPositionName());
		}
	}
	
	@Override
	public String implement(boolean infunction) {
		ViewUtils.unlockAddingGroupToView(mBasicObject);
		String result = super.implement(infunction);
		
		return result;
	}
}
