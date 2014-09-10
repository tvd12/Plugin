package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.AdvancedObject;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public class AdvancedMenuItem extends AdvancedObject {
	public AdvancedMenuItem() {
		super();
		mTemplateFile = "menuitem.template";
		mTemplateName = "CCMenuItemSprite";
		mSuper = "CCMenuItemSprite";
	}
	
	@Override
	public void update() {
		super.update();
		mClassName = StringUtils.convertToClassName(
				mBasicObject.getXmlPositionName() + "_menuItem",
				Config.getInstance().getDefaultViewSuffix());
		if(mPrefix == null) {
			setPrefix("menuItem_" + mBasicObject.getXmlPositionName());
		}
	}
	
	@Override
	public String implement(boolean infunction) {
		for(int i = 0 ; i < mSpriteGroupInView.size() ; i++) {
			mSpriteGroupInView.get(i).setAddToView(true);
		}
		MenuItem basicMenuItem = (MenuItem)mBasicObject;
		String result = super.implement(infunction)
				.replace("{normal_sprite}", basicMenuItem.getSpriteName("Normal"))
				.replace("{selected_sprite}", basicMenuItem.getSpriteName("Active"));
		String disableSpriteName = basicMenuItem.getSpriteName("Disable");
		if(!disableSpriteName.equals("")) {
			result = result.replace("//{disable_sprite}", disableSpriteName);
		}
		
		return result;
	}
}
