package com.tdgc.cocos2dx.popup.creator.model.basic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.ItemGroup;
import com.tdgc.cocos2dx.popup.creator.model.Menu;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;
import com.tdgc.cocos2dx.popup.creator.utils.ViewUtils;

public class AdvancedObject extends CommonObject {
	
	public AdvancedObject() {
		super();
		this.mParameters = new ArrayList<Parameter>();
		this.mProperties = new ArrayList<Property>();
		this.mLabelGroupInView = new ArrayList<ItemGroup>();
		this.mSpriteGroupInView = new ArrayList<ItemGroup>();
		this.mMenuItemGroupInView = new ArrayList<ItemGroup>();
		this.mMenuGroupInView = new ArrayList<ItemGroup>();
		this.mTableGroupInView = new ArrayList<ItemGroup>();
		
		this.mImplementingTplPath = "src/com/template/popup_default_implementing.template";
		this.mHeaderTplPath = "src/com/template/popup_default_header.template";
		this.mImplementingTemplate = "implement";
		this.mHeaderTemplate = "declare";
		mSuper = Config.getInstance().getDefautSuper(mSuffix);
		
	}
	
	@Override
	public String declare() {
		String superDeclare = new StringBuilder()
			.append(ViewUtils.declareGroups(mLabelGroupInView))
			.append("\n")
			.append(ViewUtils.declareGroups(mSpriteGroupInView))
			.append("\n")
			.append(ViewUtils.declareGroups(mMenuItemGroupInView))
			.append("\n")
			.append(ViewUtils.declareGroups(mMenuGroupInView))
			.append("\n")
			.append(ViewUtils.declareGroups(mTableGroupInView))
			.toString();
		
		Date createdDate = Calendar.getInstance().getTime();
		String classNamePrefix = StringUtils.convertToClassName(
				mPrefix.substring(mPrefix.indexOf("_") + 1), "");
		char firstChar = ("" + classNamePrefix.charAt(0)).toLowerCase().charAt(0);
		classNamePrefix = classNamePrefix.replace(classNamePrefix.charAt(0), firstChar);
		
		String propertiesDeclare = StringUtils.standardizeCode(superDeclare
				+ declareProperties());
//		String template = new FileUtils().fetchTemplate(mHeaderTemplate,
//				mHeaderTplPath);
		String template = new FileUtils().fetchTemplate(mHeaderTemplate,
				mHeaderTplPath, getProject());
		String result = template.replace("{class_name}", mClassName)
				.replace("{author}", System.getProperty("user.name"))
				.replace("{project_name}", Config.getInstance().getProjectName())
				.replace("{created_date}", createdDate.toString())
				.replace("{super_name}", mSuper)
				.replace("//{properties_declare}", propertiesDeclare)
				.replace("//{menuitem_tags}", createMenuItemTags())
				.replace("//{extend_functions}", StringUtils.standardizeCode(
						createExtendFunctions(true)))
				.replace("{class_name_prefix}", classNamePrefix)
				.replace("//{parameters},", declareParameters())
				.replace("//{n}", "\n");
	
		return StringUtils.standardizeCode(result);
	}

	@Override
	public String implement(boolean pInfunction) {
		this.setParentForMenuItems();
		
		Date createdDate = Calendar.getInstance().getTime();
		String classNamePrefix = StringUtils.convertToClassName(
				mPrefix.substring(mPrefix.indexOf("_") + 1), "");
		char firstChar = ("" + classNamePrefix.charAt(0)).toLowerCase().charAt(0);
		classNamePrefix = classNamePrefix.replace(classNamePrefix.charAt(0), firstChar);
		
//		String template = new FileUtils().fetchTemplate(mImplementingTemplate, 
//				mImplementingTplPath);
		String template = new FileUtils().fetchTemplate(mImplementingTemplate, 
				mImplementingTplPath, getProject());		
		
		String result = template.replace("{author}", System.getProperty("user.name"))
				.replace("{project_name}", Config.getInstance().getProjectName())
				.replace("{created_date}", createdDate.toString())
				.replace("{super_name}", mSuper)
				.replace("//{add_menuitems}", StringUtils.standardizeCode(
						ViewUtils.implementGroups(mMenuItemGroupInView)))
				.replace("//{add_sprites}", StringUtils.standardizeCode(
						ViewUtils.implementGroups(mSpriteGroupInView)))
				.replace("{class_name_prefix}", classNamePrefix)
				.replace("//{add_labels}", StringUtils.standardizeCode(
						ViewUtils.implementGroups(mLabelGroupInView)))
				.replace("//{add_tables}", StringUtils.standardizeCode(
						ViewUtils.implementGroups(mTableGroupInView)))
				.replace("//{extend_functions}", StringUtils.standardizeCode(
						createExtendFunctions(false)))
				.replace("{callback_function}", classNamePrefix + "MenuItemCallback")
				.replace("{class_name}", mClassName)
				.replace("//{parameters},", declareParameters())
				.replace("//{importing_params},", importingParams())
				.replace("//{assigning_area}", assigningArea())
				.replace("//{n}", "\n");
		
		for(int i = 0 ; i < mProperties.size() ; i++) {
			String mark = "\"${param" + i + "}\"";
			result = result.replace(mark, mProperties.get(i).getName());
		}

		return StringUtils.standardizeCode(result);
	}
	
	public String declarePositions() {
		
		StringBuilder builder = new StringBuilder("\n");
		builder.append(ViewUtils.declarePositionGroups(mLabelGroupInView))
			.append("\n")
			.append(ViewUtils.declarePositionGroups(mSpriteGroupInView))
			.append("\n")
			.append(ViewUtils.declarePositionGroups(mMenuItemGroupInView))
			.append("\n")
			.append(ViewUtils.declarePositionGroups(mMenuGroupInView))
			.append("\n")
			.append(ViewUtils.declarePositionGroups(mTableGroupInView));
		
		return builder.toString();
	}
	
	@Override
	public String implementPositions() {
		StringBuilder builder = new StringBuilder("\n");
		builder.append(ViewUtils.implementPositionGroups(mLabelGroupInView))
			.append("\n")
			.append(ViewUtils.implementPositionGroups(mSpriteGroupInView))
			.append("\n")
			.append(ViewUtils.implementPositionGroups(mMenuItemGroupInView))
			.append("\n")
			.append(ViewUtils.implementPositionGroups(mMenuGroupInView))
			.append("\n")
			.append(ViewUtils.implementPositionGroups(mTableGroupInView));
		
		return builder.toString();
	}
	
	protected String createMenuItemTags() {
		return ViewUtils.createElementTags(mMenuItemGroupInView, 1001);
	}
	
	public void addParameter(Parameter pParameter) {
		this.mParameters.add(pParameter);
		this.mProperties.add(new Property(pParameter));
	}
	
	protected String declareParameters() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < mParameters.size() ; i++) {
			if(i > 0) {
				builder.append("\t\t\t\t\t\t\t");
			}
			builder.append(mParameters.get(i))
				.append("\n");
		}
		
		return builder.toString().trim();
	}
	
	protected String declareProperties() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mProperties.size() ; i++) {
			builder.append("\t" + mProperties.get(i))
				.append("\n");
		}
		
		return builder.toString();
	}
	
	protected String importingParams() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < mParameters.size() ; i++) {
			if(i > 0) {
				builder.append("\t\t\t");
			}
			builder.append(mParameters.get(i).getName() + ",")
				.append("\n");
			
		}
		
		return builder.toString().trim();
	}
	
	protected String assigningArea() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mProperties.size() ; i++) {
			builder.append("\tthis->" + mProperties.get(i).getName())
				.append(" = ")
				.append(mParameters.get(i).getName())
				.append(";\n");
		}
		
		return builder.toString();
	}
	
	protected void setParentForMenuItems() {
		CommonObject parent = new Menu();
		parent.setName(Config.getInstance().getDefaultMenuItemParent());
		for(int i = 0 ; i < mMenuItemGroups.size() ; i++) {
			for(int j = 0 ; j < mMenuItemGroups.get(i).getItems().size() ; j++) {
				mMenuItemGroups.get(i).getItems().get(j).setParent(parent);
			}
		}
	}
	
	private String createExtendFunctions(boolean pInHeader) {
		StringBuilder builder = new StringBuilder();
		return builder.toString();
	}
	
	//for items group management
	public void pushBackGroup(ItemGroup group) {
		switch (group.getType()) {
		case ItemGroup.Type.LABLE:
			pushBackLabelGroup(group);
			break;
		case ItemGroup.Type.MENU:
			pushBackMenuGroup(group);
			break;
		case ItemGroup.Type.MENUITEM:
			pushBackMenuItemGroup(group);
			break;
		case ItemGroup.Type.SPRITE:
			pushBackSpriteGroup(group);
			break;
		case ItemGroup.Type.TABLE:
			pushBackTableGroup(group);
			break;
		default:
			break;
		}
	}
	
	protected void pushBackLabelGroup(ItemGroup group) {
		this.mLabelGroupInView.add(group);
	}
	protected void pushBackSpriteGroup(ItemGroup group) {
		this.mSpriteGroupInView.add(group);
	}
	protected void pushBackMenuItemGroup(ItemGroup group) {
		this.mMenuItemGroupInView.add(group);
	}
	protected void pushBackMenuGroup(ItemGroup group) {
		this.mMenuGroupInView.add(group);
	}
	protected void pushBackTableGroup(ItemGroup group) {
		this.mTableGroupInView.add(group);
	}
	
	public void setClassName(String pClassName) {
		this.mClassName = pClassName;
	}
	
	public String getClassName() {
		return this.mClassName;
	}
	
	protected String mImplementingTemplate;
	protected String mHeaderTemplate;
	protected String mImplementingTplPath;
	protected String mHeaderTplPath;
	protected String mClassName;
	protected List<Parameter> mParameters;
	protected List<Property> mProperties;
	protected List<ItemGroup> mLabelGroupInView;
	protected List<ItemGroup> mSpriteGroupInView;
	protected List<ItemGroup> mMenuItemGroupInView;
	protected List<ItemGroup> mMenuGroupInView;
	protected List<ItemGroup> mTableGroupInView;
	
}
