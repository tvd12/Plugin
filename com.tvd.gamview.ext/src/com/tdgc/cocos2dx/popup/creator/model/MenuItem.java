package com.tdgc.cocos2dx.popup.creator.model;

import java.util.List;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.ModelType;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.AdvancedObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Parameter;
import com.tdgc.cocos2dx.popup.creator.model.basic.Point;
import com.tdgc.cocos2dx.popup.creator.model.basic.Size;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;
import com.tdgc.cocos2dx.popup.creator.utils.ViewUtils;

public class MenuItem extends CommonObject {
	
	public MenuItem() {
		super();
		mSuffix = "MenuItem";
		mDeclareObjectName = "CCMenuItem";
		mType = ModelType.MENUITEM;
		mXmlTagName = Tag.MENUITEM;
		
		mTemplateName = "CCMenuItemSprite";
		mTemplateFile = "menuitem.template";
	}
	
	public MenuItem(String pName) {
		super();
		this.mName = pName;
		mSuffix = "MenuItem";
	}
	
	@Override
	public AdvancedObject createAdvancedObject() {
		setAdvancedObject(new AdvancedMenuItem());
		return mAdvancedObject;
	}
	
	@Override
	public void setPositionName(String pPositionName) {
		super.setPositionName(pPositionName);
		mName = mName.replace("Menuitem", "MenuItem");
	}
	
	@Override
	public String declare() {
		if(mIsGenerateClass) {
			mAdvancedObject.exportSourceCode();
		}
		return super.declare();
	}
	
	@Override
	public String implement(boolean pInfunction) {
		String parentName = Config.getInstance()
				.getDefaultBackgroundOnSupers(mParent.getType());
		if(mParent != null) {
			parentName = mParent.getName();
		}
		if(mIsGenerateClass) {
			setTemplateName("Common");
			String template = fetchTemplate(pInfunction);
			template = template.replace("{var_name}", mName)
					.replace("{tab}", "\t")
					.replace("{custom_arguments}", "")
					.replace("{extend_class_name}", mAdvancedObject.getClassName())
					.replace("{position_name}", mPositionName)
					.replace("{parent_name}", parentName)
					.replace("{z-index}", mZIndex)
					.replace("{tag_name}", mTagName);
			
			return template;
		}
		
		StringBuilder builder = new StringBuilder("\n");
		String template = fetchTemplate(pInfunction);
		
		ViewUtils.implementObject(this, builder);
		template = template.replace("{var_name}", mName)
			.replace("{tab}", "\t")
			.replace("{normal_sprite}", getSpriteName("Normal"))
			.replace("{selected_sprite}", getSpriteName("Active"))
			.replace("{position_name}", mPositionName)
			.replace("{parent_name}", parentName)
			.replace("{z-index}", mZIndex)
			.replace("{tag_name}", mTagName);
		String disableSpriteName = getSpriteName("Disable");
		if(!disableSpriteName.equals("")) {
			template = template.replace("//{disable_sprite}", disableSpriteName);
		}
		builder.append(template);
		
		return builder.toString();
	}
	
	public String getSpriteName(String pKind) {
		for(int i = 0 ; i < mSpriteGroups.size() ; i++) {
			for(int j = 0 ; j < mSpriteGroups.get(i).getItems().size() ; j++) {
				CommonObject item = mSpriteGroups.get(i).getItems().get(j);
				if(item.getName().contains(pKind)) {
					return item.getName();
				}
			}
		}
		return "";
	}
	
	@Override
	public CommonObject clone() {
		MenuItem menuItem = new MenuItem();
		this.setAllPropertiesForObject(menuItem);
		
		return menuItem;
	}
	
	@Override
	public void addSpriteGroup(ItemGroup group) {
		super.addSpriteGroup(group);
	}
	
	@Override
	public void update() {
		if(validate()) {
			ItemGroup spriteGroup = this.mSpriteGroups.get(0);
			Sprite first = ((Sprite)spriteGroup.getItems().get(0));
			first.setIsUnlocated(true);
			first.getImage().setAddToInterfaceBuilder(true);
			for(int i = 1 ; i < spriteGroup.getItems().size() ; i++) {
				Sprite sprite = ((Sprite)spriteGroup.getItems().get(i));
				sprite.setLocationInView(first.getImage()
						.getLocationInInterfaceBuilder());
				sprite.setAnchorPoint(first.getAnchorPoint());
				sprite.setSize(first.getImage().getSize());
				sprite.setIsUnlocated(true);
				sprite.getImage().setAddToInterfaceBuilder(false);
			}
			spriteGroup.setAddToView(false);
		}
	}
	
	@Override
	public void addChild(CommonObject child) {
		child.setIsUnlocated(true);
	}
	
	@Override
	public Point getLocationInView() {
		return mParent.getLocationInView();
	}
	
	@Override
	public Size getSize() {
		return mParent.getSize();
	}
	
	public boolean validate() {
		boolean valid = true;
		if(mSpriteGroups.size() != 1) {
			valid = false;
		} else {
			int size = mSpriteGroups.get(0).getItems().size();
			if(size < 2 || size > 3) {
				valid = false;
			} else {
				
			}
		}
		
		return valid;
	}
	
	@Override
	public String toXML() {
		String tab = StringUtils.tab(mTabCount);
		StringBuilder builder = new StringBuilder(tab);
		String generateClassString = "";
		StringBuilder parameterTags = new StringBuilder();
		if(mIsGenerateClass) {
			generateClassString = " " + Attribute.GENERATE_CLASS + "=\"true\"";
			List<Parameter> params = mAdvancedObject.getParameters();
			for(int i = 0 ; i < params.size() ; i++) {
				parameterTags.append("\n" + tab)
					.append(params.get(i).toXML());
			}
		}
		builder.append("<" + mXmlTagName + generateClassString 
				+ " " + Attribute.VISIBLE + "=\"true\" ")
			.append(Attribute.COMMENT + "=\"\">");
		builder.append(parameterTags);
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
		.append("<" + Tag.Z_INDEX + " " + Attribute.VALUE 
				+ "=\"" + mZIndex + "\" />");
		
		builder.append("\n")
			.append(super.toXML())
			.append(tab)
			.append("</" + mXmlTagName + ">");
		
		builder.append("\n");
		
		return builder.toString();
	}
	
}
