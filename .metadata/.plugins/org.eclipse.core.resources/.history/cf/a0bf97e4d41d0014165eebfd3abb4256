package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.constants.ModelType;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;

public class Sprite extends CommonObject {
	
	public Sprite() {
		super();
		this.mSuffix = "Sprite";
		this.mDeclareObjectName = "CCSprite";
		this.mType = ModelType.SPRITE;
		this.mIsUnlocated = false;
		this.mImage = null;
	}
	
	@Override
	public String declare() {
		StringBuilder builder = new StringBuilder(super.declare());
		builder.append("\tCCSprite* " + mName + ";");
		
		return builder.toString();
	}
	
	@Override
	public void setPositionName(String pPositionName) {
		super.setPositionName(pPositionName);
	}

	@Override
	public String implement(boolean pInfunction) {
		if((pInfunction && isBackground())
				|| mName == null || mPositionName == null
				) {
			return "";
		}
		StringBuilder builder = new StringBuilder("\n");
		builder.append(super.implement(pInfunction));
		
		String templateName = "CCSprite";
		if(pInfunction) {
			templateName += " in function";
			mName = getInfunctionName();
		} else {
			if(mParent != null && mParent.getType().equals(ModelType.TABLE)) {
				templateName = "CCSprite non-add";
			}
		}
		if(mIsUnlocated) {
			templateName += " unlocated";
		}
		
		String template = new FileUtils().fetchTemplate(templateName, 
				"src/com/template/new_sprite.template");
		
		String imageName = "";
		if(mImage == null) {
			imageName = ((Sprite)mBackground).getImage().getId();
		} else {
			imageName = mImage.getId();
		}
		
		String parentName = Config.getInstance().getDefaultParentPopup();
		if(mParent != null) {
			parentName = mParent.getName();
		}
		
		template = template.replace("{var_name}", mName)
			.replace("{position_name}", mPositionName)
			.replace("{tab}", "\t")
			.replace("{parent_name}", parentName)
			.replace("{image_name}", imageName)
			.replace("{z-index}", mZIndex);
		builder.append(template);
		
		return builder.toString();
	}
	
	public void setImage(Image pImage) {
		this.mImage = pImage;
	}
	
	public Image getImage() {
		return this.mImage;
	}
	
	protected Image mImage;
}
