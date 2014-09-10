package com.tdgc.cocos2dx.popup.creator.model.basic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IProject;

import com.tdgc.cocos2dx.popup.creator.constants.Strings;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.ItemGroup;
import com.tdgc.cocos2dx.popup.creator.model.Menu;
import com.tdgc.cocos2dx.popup.creator.model.Progressbar;
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
		this.mProgressGroup = new ItemGroup(ItemGroup.Type.PROGRESSBAR);
		
		this.mSuper = Config.getInstance().getDefautSuper(mSuffix);
		this.mBackgroundName = Strings.DEFAULT;
		this.mIsNewClass = true;
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
			.append("\n")
			.append(mProgressGroup.declare())
			.toString();
		
		Date createdDate = Calendar.getInstance().getTime();
		String classNamePrefix = StringUtils.convertToClassName(
				mPrefix.substring(mPrefix.indexOf("_") + 1), "");
		char firstChar = ("" + classNamePrefix.charAt(0)).toLowerCase().charAt(0);
		classNamePrefix = classNamePrefix.replace(classNamePrefix.charAt(0), firstChar);
		
		String propertiesDeclare = StringUtils.standardizeCode(superDeclare
				+ declareProperties());
		FileUtils fileUtils = new FileUtils();
		String customSourceCode = fileUtils.findCustomSourceCode(
				mClassPath + "/" + mDirectoryName + "/" + mClassName + ".h");
		String template = fileUtils.fetchTemplate(
				getClassDeclaringTemplateName(),
				getClassTemplateFilePath(), getProject());
		String result = template.replace("{class_name}", mClassName)
				.replace("{author}", System.getProperty("user.name"))
				.replace("{project_name}", getProject().getName())
				.replace("{created_date}", createdDate.toString())
				.replace("{super_name}", mSuper)
				.replace("//{properties_declare}", propertiesDeclare)
				.replace("//{menuitem_tags}", createMenuItemTags())
				.replace("//{extend_functions}", StringUtils.standardizeCode(
						createExtendFunctions(true)))
				.replace("{class_name_prefix}", classNamePrefix)
				.replace("//{parameters}", declareParameters())
				.replace("//{n}", "\n")
				.replace("//{custom_source_code}", customSourceCode.trim())
				.replace("//{importings}", createImportDirectives());
	
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
		
		FileUtils fileUtils = new FileUtils();
		String customSourceCode = fileUtils.findCustomSourceCode(
				mClassPath + "/" + mDirectoryName + "/" + mClassName + ".cpp");
		String template = fileUtils.fetchTemplate(
				getClassImplementingTemplateName(), 
				getClassTemplateFilePath(), getProject());	
		
		String result = template.replace("{author}", System.getProperty("user.name"))
				.replace("{project_name}", getProject().getName())
				.replace("{created_date}", createdDate.toString())
				.replace("{super_name}", mSuper)
				.replace("//{add_menus}", StringUtils.standardizeCode(
						ViewUtils.implementGroups(mMenuGroupInView)))
				.replace("//{add_menuitems}", StringUtils.standardizeCode(
						ViewUtils.implementGroups(mMenuItemGroupInView)))
				.replace("//{add_sprites}", StringUtils.standardizeCode(
						ViewUtils.implementGroups(mSpriteGroupInView)))
				.replace("{class_name_prefix}", classNamePrefix)
				.replace("//{add_labels}", StringUtils.standardizeCode(
						ViewUtils.implementGroups(mLabelGroupInView)))
				.replace("//{add_tables}", StringUtils.standardizeCode(
						ViewUtils.implementGroups(mTableGroupInView)))
				.replace("//{add_progressbars}", mProgressGroup.implement(false))
				.replace("//{extend_functions}", StringUtils.standardizeCode(
						createExtendFunctions(false)))
				.replace("{callback_function}", classNamePrefix + "MenuItemCallback")
				.replace("{class_name}", mClassName)
				.replace("//{parameters}", declareParameters())
				.replace("//{importing_params},", importingParams())
				.replace("//{assigning_area}", assigningArea())
				.replace("//{n}", "\n")
				.replace("//{custom_source_code}", customSourceCode.trim())
				.replace("//{importings}", createImportDirectives());
		
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
	
	public String createImportDirectives() {
		String template = new FileUtils().fetchTemplate("Import", 
				"src/com/template/import.template", getProject());
		StringBuilder builder = new StringBuilder();
		AdvancedObject child = getAdvancedChild();
		while(child != null) {
			if(child.getBacsicObject() != null
					&& child.getBacsicObject().isGenerateClass()) {
				builder.append(template.replace("{class_name}", 
						child.getClassName()))
					.append("\n");
			}
			child = child.getAdvancedChild();
		}
		
		return builder.toString();
	}
	
	public String createImportDirective() {
		String template = new FileUtils().fetchTemplate("Import", 
				"src/com/template/import.template", getProject());
		template = template.replace("{class_name}", mClassName);
		
		return template;
	}
	
	public void exportHeaderCode() {
		String path = getClassPath() + "/" 
				+ getDirectoryName() + "/" + getClassName()
				+ ".h";
		String fileContent = declare();
		new FileUtils().setContent(fileContent).writeToFile(path, false);
	}
	
	public void exportImplementedCode() {
		String path = getClassPath() + "/" 
				+ getDirectoryName() + "/" + getClassName()
				+ ".cpp";
		String fileContent = implement(false);
		new FileUtils().setContent(fileContent).writeToFile(path, false);
	}
	
	public void exportSourceCode() {
		exportHeaderCode();
		exportImplementedCode();
	}
	
	protected String createMenuItemTags() {
		return ViewUtils.createElementTags(mMenuItemGroupInView, 1001);
	}
	
	public void addParameter(Parameter pParameter) {
		pParameter.setTabCount(getTabCount() + 1);
		this.mParameters.add(pParameter);
		this.mProperties.add(new Property(pParameter));
	}
	
	protected String declareParameters() {
		if(mParameters.size() == 0) {
			return "//{parameters}";
		}
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
		parent.setName(Config.getInstance().getDefaultMenuOnSuper(mType));
		parent.setProject(getProject());
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
	
	public void setBackgroundName(String bgName) {
		this.mBackgroundName = bgName;
	}
	
	public String getBackgroundName() {
		if(mBackgroundName == null) {
			mBackgroundName = Strings.DEFAULT;
		}
		return mBackgroundName;
	}
	
	public void addProgressbars(Progressbar bar) {
		this.mProgressGroup.addItem(bar);
	}
	
	public ItemGroup getProgressbars() {
		return this.mProgressGroup;
	}
	
	public void setAdvancedParent(AdvancedObject parent) {
		parent.setAdvancedChild(this);
		this.mAdvancedParent = parent;
		this.setDirectoryName(parent.getDirectoryName());
		this.setClassPath(parent.getClassPath());
	}
	
	public void setAdvancedChild(AdvancedObject child) {
		this.mAdvancedChild = child;
	}
	
	public AdvancedObject getAdvancedParent() {
		return this.mAdvancedParent;
	}
	
	public AdvancedObject getAdvancedChild() {
		return this.mAdvancedChild;
	}
	
	public String getClassDeclaringTemplateName() {
		return this.mTemplateName + " class declaring";
	}
	
	public String getClassImplementingTemplateName() {
		return this.mTemplateName + " class implementing";
	}
	
	public String getClassTemplateFilePath() {
		return this.getTemplateFilePath();
	}
	
	@Override
	public void setType(String type) {
		super.setType(type);
		if(type != null && type.trim().length() > 0
				&& mTemplateFile == null) {
			mTemplateFile = type + ".template";
		}
	}
	
	public void setClassPath(String mClassPath) {
		this.mClassPath = mClassPath;
	}

	public void setDirectoryName(String mDirectoryName) {
		this.mDirectoryName = mDirectoryName;
	}
	
	public void setBasicObject(CommonObject obj) {
		this.mBasicObject = obj;
	}
	
	public CommonObject getBacsicObject() {
		return this.mBasicObject;
	}
	
	public List<Parameter> getParameters() {
		return this.mParameters;
	}
	
	public String getDirectoryName() {
		return this.mDirectoryName;
	}
	
	public String getClassPath() {
		return mClassPath;
	}
	
	@Override
	public IProject getProject() {
		IProject project = mProject;
		AdvancedObject parent = mAdvancedParent;
		while(mProject == null && parent != null) {
			project = parent.mProject;
			parent = parent.getAdvancedParent();
		}
		
		return project;
	}
	
	@Override
	public CommonObject clone() {
		AdvancedObject obj = new AdvancedObject();
		this.setAllPropertiesForObject(obj);
		obj.mBackgroundName = mBackgroundName;
		obj.mClassName = mClassName;
		
		obj.mParameters = mParameters;
		obj.mProperties = mProperties;
		obj.mLabelGroupInView = mLabelGroupInView;
		obj.mSpriteGroupInView = mSpriteGroupInView;
		obj.mMenuGroupInView = mMenuGroupInView;
		obj.mMenuItemGroupInView = mMenuItemGroupInView;
		obj.mTableGroupInView = mTableGroupInView;
		
		obj.mProgressGroup = mProgressGroup;
		
		obj.mAdvancedParent = mAdvancedParent;
		
		return obj;
	}
	
	protected String mBackgroundName;
	protected String mClassName;
	protected String mClassPath;
	protected String mDirectoryName;
	
	protected List<Parameter> mParameters;
	protected List<Property> mProperties;
	protected List<ItemGroup> mLabelGroupInView;
	protected List<ItemGroup> mSpriteGroupInView;
	protected List<ItemGroup> mMenuItemGroupInView;
	protected List<ItemGroup> mMenuGroupInView;
	protected List<ItemGroup> mTableGroupInView;
	protected CommonObject mBasicObject;
	
	private ItemGroup mProgressGroup;
	
	private AdvancedObject mAdvancedParent;
	private AdvancedObject mAdvancedChild;
}
