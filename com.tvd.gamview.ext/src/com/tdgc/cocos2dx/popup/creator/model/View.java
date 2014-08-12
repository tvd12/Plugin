package com.tdgc.cocos2dx.popup.creator.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tdgc.cocos2dx.popup.creator.constants.Constants;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.file.ImageFileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Parameter;
import com.tdgc.cocos2dx.popup.creator.model.basic.Property;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;
import com.tdgc.cocos2dx.popup.creator.utils.XmlContentUtils;
import com.tdgc.cocos2dx.popup.creator.xml.ScreenFetcher;
import com.tdgc.cocos2dx.popup.creator.xml.XibFetcher;

public class View extends CommonObject {

	public View() {
		super();
		this.mSuffix = "popup";
		this.mName = Config.getInstance().getDefaultParentPopup();
		this.mImages = new ArrayList<Image>();
		this.mMenuItemTags = new ArrayList<String>();
		this.mParameters = new ArrayList<Parameter>();
		this.mProperties = new ArrayList<Property>();
		this.mResources = new ArrayList<Resources>();
		this.mScreenContainerPath = 
				Config.getInstance().getScreenContainerPath();
		this.mBackgroundImage = new Image("common_background", 
				"pop_common_bg.png", 
				"avatars_popup/background/pop_common_bg.png",
				true, this);
		this.mImages.add(mBackgroundImage);
		this.mPrefix = "";
	}
	
	@Override
	public String declare() {
		String superDeclare = super.declare();
		Date createdDate = Calendar.getInstance().getTime();
		String classNamePrefix = StringUtils.convertToClassName(
				mPrefix.substring(mPrefix.indexOf("_") + 1), "");
		char firstChar = ("" + classNamePrefix.charAt(0)).toLowerCase().charAt(0);
		classNamePrefix = classNamePrefix.replace(classNamePrefix.charAt(0), firstChar);
		
		String propertiesDeclare = StringUtils.standardizeCode(superDeclare
				+ declareProperties());
		String template = new FileUtils().readFromFile(
				"src/com/template/popup_default_header.template");
		String result = template.replace("{class_name}", mClassName)
				.replace("{author}", System.getProperty("user.name"))
				.replace("{project_name}", Config.getInstance().getProjectName())
				.replace("{created_date}", createdDate.toString())
				.replace("{super_name}", Config.getInstance().getDefautSuper(mSuffix))
				.replace("//{properties_declare}", propertiesDeclare)
				.replace("//{menuitem_tags}", createMenuItemTags())
				.replace("//{extend_functions}", StringUtils.standardizeCode(
						createExtendFunctions(true)))
				.replace("{class_name_prefix}", classNamePrefix)
				.replace("{background_id}", mBackgroundImage.getId())
				.replace("//{parameters}", declareParameters())
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
		
		String template = new FileUtils().fetchTemplate("implement", 
				"src/com/template/popup_default_implement.template");
		
		String result = template.replace("{author}", System.getProperty("user.name"))
				.replace("{project_name}", Config.getInstance().getProjectName())
				.replace("{created_date}", createdDate.toString())
				.replace("{super_name}", Config.getInstance().getDefautSuper(mSuffix))
				.replace("//{add_menuitems}", StringUtils.standardizeCode(
						implementObjects(mMenuItemsGroups, false)))
				.replace("//{add_sprites}", StringUtils.standardizeCode(
						implementObjects(mSpritesGroups, false)))
				.replace("{class_name_prefix}", classNamePrefix)
				.replace("//{add_labels}", StringUtils.standardizeCode(
						implementObjects(mLabelsGroups, false)))
				.replace("//{add_tables}", StringUtils.standardizeCode(
						implementObjects(mTablesGroups, false)))
				.replace("//{extend_functions}", StringUtils.standardizeCode(
						createExtendFunctions(false)))
				.replace("{callback_function}", classNamePrefix + "MenuItemCallback")
				.replace("{class_name}", mClassName)
				.replace("//{parameters}", declareParameters())
				.replace("//{importing_params}", importingParams())
				.replace("//{assigning_area}", assigningArea())
				.replace("//{n}", "\n");
		
		for(int i = 0 ; i < mProperties.size() ; i++) {
			String mark = "\"${param" + i + "}\"";
			result = result.replace(mark, mProperties.get(i).getName());
		}

		return StringUtils.standardizeCode(result);
	}
	
	@Override
	public String declarePositions() {
		String declare = super.declarePositions();
		String strs[] = declare.split("\n");
		Arrays.sort(strs);
		StringBuilder builder = new StringBuilder("\n");
		createHeaderCommentTemplate(builder, " position declare");
		boolean isArray = false;
		for(int i = 0 ; i < strs.length ; i++) {
			if(!strs[i].trim().equals("")) {
				if(strs[i].contains("[")) {
					isArray = true;
					continue;
				} 
				if(isArray) {
					builder.append(strs[i -1] + "\n");
					isArray = false;
				}
				builder.append(strs[i] + "\n");
			}
		}
		builder.append("\n")
			.append("\t" + Constants.DONT_DELETE_THIS_LINE);
		return builder.toString();
	}
	
	@Override
	public String implementPositions() {
		String declare = super.implementPositions();
		String strs[] = declare.split("\n");
		Arrays.sort(strs);
		StringBuilder builder = new StringBuilder("\n");
		createHeaderCommentTemplate(builder, " position init");
		for(int i = 0 ; i < strs.length ; i++) {
			if(!strs[i].trim().equals("")) {
				builder.append(strs[i] + "\n");
			}
		}
		builder.append("\n")
			.append("\t" + Constants.DONT_DELETE_THIS_LINE);
		
		return builder.toString();
	}
	
	public String declareImageIds() {
		StringBuilder builder = new StringBuilder();
		createHeaderCommentTemplate(builder, "image ids declare");
		for(int i = 0 ; i < mImages.size(); i++) {
			builder.append("\tstring " + mImages.get(i).getId().trim() + ";")
				.append("\n");
		}
		builder.append("\n")
			.append("\t//don't modify or delete this line");
		
		return builder.toString();
	}
	
	public String implementImageIds() {
		StringBuilder builder = new StringBuilder();
		createHeaderCommentTemplate(builder, "image ids init");
		
		for(int i = 0 ; i < mImages.size(); i++) {
			builder.append("\t" + mImages.get(i).getId())
				.append("\t\t= ")
				.append("\"" + mImages.get(i).getRealPath() + "\";")
				.append("\n");
		}
		builder.append("\n")
			.append("\t//don't modify or delete this line");
		
		return builder.toString();
	}
	
	public String createMenuItemTags() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mMenuItemTags.size() ; i++) {
			builder.append("\t\t")
				.append(mMenuItemTags.get(i))
				.append("\t= " + (1000 + i))
				.append(",\n");
		}
		return builder.toString();
	}
	
	public void export() {
		this.exportDeclaringImageIds();
		this.exportImplementedImageIds();
		this.exportDeclaringPositions();
		this.exportImplementedPositions();
		this.exportDirectory();
		this.exportHeaderCode();
		this.exportImplementedCode();
	}
	
	public void exportImages() {
		ImageFileUtils imageFileUtils = new ImageFileUtils();
		imageFileUtils.writeImagesFromTo(mImagesInputPath, 
				mImagesPath);
	}
	
	public void exportXibTemplate(String pDevice) {
			FileUtils fileUtils = new FileUtils();
			String xibContent = fileUtils.readFromFile("resources/xib/" 
					+ pDevice + "/template.xib");
			xibContent = xibContent.replace("<!--{imageviews_tag}-->", createImageViewsTag())
				.replace("<!--{images_tag}-->", createImagesTag());
			
			final String fileName = mXibContainerPath + "/" + mClassName 
					+ "_" + pDevice + "" 
					+ ".xib";
			fileUtils.setContent(xibContent).writeToFile(fileName, true);
			String xibFilePath = fileName;
			XibFetcher xibFetcher = new XibFetcher(mImages);
			xibFetcher.fetchData(xibFilePath);
			String fileContent = fileUtils.readFromFile(mXmlFilePath);
			for(int i = 0 ; i < mImages.size() ; i++) {
				fileContent = XmlContentUtils.replaceSpritePosition(
					fileContent, mImages.get(i));
			}
	 		fileUtils.setContent(fileContent).replaceContent(mXmlFilePath);
							
	}
	
	public void exportScreenTemplate(String pDevice) {
		FileUtils fileUtils = new FileUtils();
		String screenContent = fileUtils.readFromFile("resources/screen/" 
				+ pDevice + "/template.screen");
		screenContent = screenContent.replace("<!--{widgets}-->", createWidgetsTag());
		final String fileName = mScreenContainerPath + "/" + mClassName 
				+ "_" + pDevice + "" 
				+ ".screen";
		fileUtils.setContent(screenContent).writeToFile(fileName, true);
		String screenPath = fileName;
		ScreenFetcher screenFetcher = new ScreenFetcher(mImages);
		screenFetcher.fetchData(screenPath);
		String fileContent = fileUtils.readFromFile(mXmlFilePath);
		for(int i = 0 ; i < mImages.size() ; i++) {
			fileContent = XmlContentUtils.replaceSpritePosition(
				fileContent, mImages.get(i));
		}
 		fileUtils.setContent(fileContent).replaceContent(mXmlFilePath);
		
	}
	public void addImage(Image pImage) {
		if(pImage.isBackground()) {
			mBackgroundImage = pImage;
			this.mImages.set(0, pImage);
		} else {
			this.mImages.add(pImage);
		}
	}
	
	public void addParameter(Parameter pParameter) {
		this.mParameters.add(pParameter);
		this.mProperties.add(new Property(pParameter));
	}
	
	public void addMenuItemTag(String pTag) {
		this.mMenuItemTags.add(pTag);
	}
	
	@Override
	public void setPrefix(String pPrefix) {
		if(pPrefix == null) {
			System.out.println("TVD-DEBUG: prefix is null");
			return;
		}
		this.mPrefix = pPrefix;
		String directoryName = pPrefix.substring(pPrefix.indexOf('_') + 1);
		this.mDirectoryName = StringUtils.convertToClassName(directoryName, "");
	}

	@Override
	public void setPositionName(String pPositionName) {
		
	}
	
	public void setClassName(String pClassName) {
		this.mClassName = pClassName;
	}
	
	public String getClassName() {
		return this.mClassName;
	}
	
	public void setDefinePath(String mDefinePath) {
		this.mDefinePath = mDefinePath;
	}

	public void setParametersPath(String mParametersPath) {
		this.mParametersPath = mParametersPath;
	}

	public void setImagesInputPath(String mImagesInputPath) {
		this.mImagesInputPath = mImagesInputPath;
	}

	public void setImagesPath(String mImagesPath) {
		this.mImagesPath = mImagesPath;
	}

	public void setXibContainerPath(String mXibContainerPath) {
		this.mXibContainerPath = mXibContainerPath;
	}

	public void setClassPath(String mClassPath) {
		this.mClassPath = mClassPath;
	}

	public void setDirectoryName(String mDirectoryName) {
		this.mDirectoryName = mDirectoryName;
	}
	
	public void setXmlFilePath(String pXmlFilePath) {
		this.mXmlFilePath = pXmlFilePath;
	}
	
	public String getScreenContainerPath() {
		return this.mScreenContainerPath;
	}
	
	public List<Image> getImages() {
		return this.mImages;
	}
	
	public void setScreenContainerPath(String pScreenContainerPath) {
		this.mScreenContainerPath = pScreenContainerPath;
	}
	
	public void addResource(Resources pResources) {
		this.mResources.add(pResources);
	}

	private void createHeaderCommentTemplate(StringBuilder pBuilder,
			String pComment) {
		pBuilder.append("\n\t// " + mClassName + " " + pComment)
		.append("\n\t// by " + System.getProperty("user.name"))
		.append(" " + new Date().toString())
		.append("\n");
	}
	
	public void exportDeclaringImageIds() {
		FileUtils fileUtils = new FileUtils();
		String content = fileUtils.readFromFile(mDefinePath + ".h");
		String replaceContent = this.declareImageIds();
		content = content.replace(Constants.DONT_DELETE_THIS_LINE, 
				replaceContent);
		
		fileUtils.setContent(content)
			.writeToFile(mDefinePath + ".h", false);
	}
	
	public void exportImplementedImageIds() {
		FileUtils fileUtils = new FileUtils();
		String content = fileUtils.readFromFile(mDefinePath + ".cpp");
		String replaceContent = this.implementImageIds();
		content = content.replace(Constants.DONT_DELETE_THIS_LINE, 
				replaceContent);
		fileUtils.setContent(content)
			.writeToFile(mDefinePath + ".cpp", false);
	}
	
	private void exportDeclaringPositions() {
		FileUtils fileUtils = new FileUtils();
		String content = fileUtils.readFromFile(mParametersPath + ".h");
		String replaceContent = this.declarePositions();
		content = content.replace(Constants.DONT_DELETE_THIS_LINE, 
				replaceContent);
		fileUtils.setContent(content)
			.writeToFile(mParametersPath + ".h", false);
	}
	
	private void exportImplementedPositions() {
		FileUtils fileUtils = new FileUtils();
		String content = fileUtils.readFromFile(
				mParametersPath + ".cpp");
		String replaceContent = this.implementPositions();
		content = content.replace(Constants.DONT_DELETE_THIS_LINE, 
				replaceContent);
		fileUtils.setContent(content)
			.writeToFile(mParametersPath + ".cpp", false);
	}
	
	private void exportDirectory() {
		new FileUtils().createDirectory(mClassPath, 
				mDirectoryName);
	}
	
	private void exportHeaderCode() {
		String content = declare();
		new FileUtils().setContent(content)
			.writeToFile(mClassPath + "/"
				+ mDirectoryName + "/" + mClassName + ".h", false);
				
	}
	
	private void exportImplementedCode() {
		String content = implement(false);
		new FileUtils().setContent(content)
			.writeToFile(mClassPath + "/"
				+ mDirectoryName + "/" + mClassName + ".cpp", false);
	}
	
	private void setParentForMenuItems() {
		CommonObject parent = new CommonObject();
		parent.setName(Config.getInstance().getDefaultMenuItemParent());
		for(int i = 0 ; i < mMenuItemsGroups.size() ; i++) {
			for(int j = 0 ; j < mMenuItemsGroups.get(i).getItems().size() ; j++) {
				mMenuItemsGroups.get(i).getItems().get(j).setParent(parent);
			}
		}
	}
	
	private String declareParameters() {
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
	
	private String declareProperties() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mProperties.size() ; i++) {
			builder.append("\t" + mProperties.get(i))
				.append("\n");
		}
		
		return builder.toString();
	}
	
	private String importingParams() {
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
	
	private String assigningArea() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mProperties.size() ; i++) {
			builder.append("\tthis->" + mProperties.get(i).getName())
				.append(" = ")
				.append(mParameters.get(i).getName())
				.append(";\n");
		}
		
		return builder.toString();
	}
	
	private String createImageViewsTag() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mImages.size() ; i++) {
			if(mImages.get(i).getRealPath().endsWith("active.png")) {
				continue;
			}
			String id = "img-png-" + StringUtils.generateString(i);
			try {
				builder.append(mImages.get(i).createImageViewTag(id, mImagesPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return builder.toString();
	}
	
	private String createImagesTag() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mImages.size() ; i++) {
			if(mImages.get(i).getRealPath().endsWith("active.png")) {
				continue;
			}
			builder.append(mImages.get(i).createImageTag());
		}
		
		return builder.toString();
	}
	
	private String createWidgetsTag() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mImages.size() ; i++) {
			if(mImages.get(i).getRealPath().endsWith("active.png")) {
				continue;
			}
			String id = (i + 1) + "";
			try {
				builder.append(mImages.get(i).createWidgetsTag(id, mImagesPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return builder.toString();
	}
	
	private String createExtendFunctions(boolean pInHeader) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < mResources.size() ; i++) {
			if(pInHeader) {
				builder.append(mResources.get(i).declareExtendFunction());
			} else {
				builder.append(mResources.get(i).createExtendFunction());
			}
			
			builder.append("//{n}");
		}
		
		return builder.toString();
	}
	
	private String mDefinePath;
	private String mParametersPath;
	private String mImagesInputPath;
	private String mImagesPath;
	private String mXibContainerPath;
	private String mClassPath;
	
	private String mClassName;
	private String mDirectoryName;
	private String mXmlFilePath;
	
	private Image mBackgroundImage;
	private List<Image> mImages;
	private List<String> mMenuItemTags;
	private List<Parameter> mParameters;
	private List<Property> mProperties;
	private List<Resources> mResources;
	private String mScreenContainerPath;
}
