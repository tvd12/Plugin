package com.tdgc.cocos2dx.popup.creator.model.basic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;

import com.tdgc.cocos2dx.popup.creator.constants.Strings;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.ItemGroup;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public abstract class CommonObject {
	
	public CommonObject() {
		mMenuGroups = new ArrayList<ItemGroup>();
		mSpriteGroups = new ArrayList<ItemGroup>();
		mLabelGroups = new ArrayList<ItemGroup>();
		mMenuItemGroups = new ArrayList<ItemGroup>();
		mTableGroups = new ArrayList<ItemGroup>();
		
		mSuper = Config.getInstance().getDefautSuper(mPrefix);
		mZIndex = "0";
		mAnchorPoint = new Point(0.5f, 0.5f);
	}
	
	public abstract String declare();
	
	public abstract String implement(boolean pInfunction);
	
	public abstract CommonObject clone();
	
	public String declarePositions() {
		StringBuilder builder = new StringBuilder();
		if(mPositionName != null 
				&& mPositionString != null
				&& !mPositionString.equals(Strings.DEFAULT)) {
			builder.append("CCPoint " + mPositionName + ";");
		}
		return builder.toString();
	}
	
	public String implementPositions() {
		
		String template = new FileUtils().fetchTemplate("MakePointCommon", 
				"src/com/template/new_point.template", getProject());
		StringBuilder builder = new StringBuilder();
		
		if(mPositionString != null 
				&& mPositionName != null
				&& !mPositionString.equals(Strings.DEFAULT)) {
			System.out.println("positionString " + mPositionString);
			template = template.replace("{var_name}", mPositionName)
					.replace("{position}", mPositionString);
			builder.append(template);
		}
		
		return builder.toString().trim();
	}
	
	public void setPositionName(String pPositionName) {
		
		//name of position in xml file, it must not change
		mXmlPositionName = pPositionName;
		
		mTagName = StringUtils.convertToTagName(pPositionName, this.getType());
		
		mName = StringUtils.convertToNormalProperty(pPositionName + "_" + mSuffix);
		if(pPositionName.trim().equals(Strings.DEFAULT)
				|| this.mIsBackground) {
			mPositionName = null;
			return;
		}
		if(pPositionName.contains(Tag.POSITION)) {
			pPositionName = pPositionName.replace(Tag.POSITION, "place");
		}
		this.mPositionName = pPositionName.toUpperCase() + "_" + mSuffix.toUpperCase();
		this.mPositionName = Config.getInstance().getPrefix().toUpperCase() + "_" + mPositionName 
				+ "_POSITION";
	}
	
	public void setNewPositionName(String pPositionName) {
		mPositionName = pPositionName;
	}
	
	public String toAnchorPointString() {
		String result = "";
		if(mAnchorPoint != null) {
			result = mName + "->setAnchorPoint(" + mAnchorPoint + ");";
		}
		return result;
	}
	
	public String getInfunctionName() {
		return mName.substring(0, 2).toLowerCase().charAt(1)
				+ mName.substring(2);
	}
	
	public void replacePositionName(String pNewPosName) {
		this.mPositionName = pNewPosName;
	}
	
	public void setName(String pName) {
		this.mName = pName;
	}
	
	public String getName() {
		return this.mName;
	}
	
	public String getPositionName() {
		return this.mPositionName;
	}
	
	public void setPrefix(String pPrefix) {
		this.mPrefix = pPrefix;
	}
	
	public String getPrefix() {
		return this.mPrefix;
	}
	
	public void setSuffix(String pSuffix) {
		this.mSuffix = pSuffix;
	}
	
	public String getSuffix() {
		return this.mSuffix;
	}
	
	public void setAnchorPoint(Point pPoint) {
		this.mAnchorPoint = pPoint;
	}
	
	public void setAnchorPoint(float pX, float pY) {
		this.mAnchorPoint  = new Point(pX, pY);
	}
	
	
	public void addMenuItemGroup(ItemGroup pMenuItemGroup) {
		this.mMenuItemGroups.add(pMenuItemGroup);
	}
	
	public void addMenuGroup(ItemGroup pMenuGroup) {
		this.mMenuGroups.add(pMenuGroup);
	}
	
	public void addSpriteGroup(ItemGroup pSpriteGroup) {
		this.mSpriteGroups.add(pSpriteGroup);
	}
	
	public void addLabelGroup(ItemGroup pLabelGroup) {
		this.mLabelGroups.add(pLabelGroup);
	}
	
	public void addTableGroup(ItemGroup pTableGroup) {
		this.mTableGroups.add(pTableGroup);
	}
	
	public void setComment(String pComment) {
		this.mComment = pComment;
	}
	
	public String getComment() {
		return this.mComment;
	}
	
	public void setSuper(String pSuper) {
		if(!pSuper.equals("default")) {
			this.mSuper = pSuper;
		}
	}
	
	public String getSuper() {
		return this.mSuper;
	}
	
	public void setType(String pType) {
		this.mType = pType;
	}
	
	public String getType() {
		return this.mType;
	}
	
	public void setPosition(Point pPosition) {
		this.mPosition = pPosition;
	}
	
	public void setPosition(float pX, float pY) {
		this.mPosition = new Point(pX, pY);
	}
	
	public void setPosition(String pPosition) {
		this.mPositionString = pPosition;
		
		if(pPosition != null && !pPosition.equals(Strings.DEFAULT)) {
			String strs[] = pPosition.split(",");
			setPosition(Float.parseFloat(strs[0]), 
					Float.parseFloat(strs[1]));
		}
	}
	
	public Point getPosition() {
		return this.mPosition;
	}
	
	public String getPositionString() {
		return this.mPositionString;
	}
	
	public void setAnchorPoint(String pAnchorPoint) {
		this.mAnchorPointString = pAnchorPoint;
		
		if(pAnchorPoint == null || pAnchorPoint.equals("default")) {
			return;
		}

		String values[] = pAnchorPoint.split(",");
		mAnchorPoint = new Point(Float.parseFloat(values[0]),
				Float.parseFloat(values[1]));
	}
	
	public String getAnchorPointString() {
		return this.mAnchorPointString;
	}
	
	public String getDeclareObjectName() {
		return this.mDeclareObjectName;
	}
	
	public void setParent(CommonObject pParent) {
		this.mParent = pParent;
	}
	
	public CommonObject getParent() {
		return this.mParent;
	}
	
	public void setSize(String pSize) {
		this.mSizeString = pSize;
		
	}
	
	public String getSizeString() {
		return this.mSizeString;
	}
	
	public CommonObject getBackground() {
		return this.mBackground;
	}
	
	public void setBackground(CommonObject pBackground)	{
		if(this.mBackground == null) {
			this.mBackground = pBackground;
		}
	}
	
	public void setIsBackground(boolean pIsBackground) {
		this.mIsBackground = pIsBackground;
	}
	
	public boolean isBackground() {
		return this.mIsBackground;
	}
	
	public void setIsUnlocated(boolean pIsUnlocated) {
		this.mIsUnlocated = pIsUnlocated;
	}
	
	public void setZIndex(String pZIndex) {
		this.mZIndex = pZIndex;
	}
	
	public String getZIndex() {
		return this.mZIndex;
	}
	
	protected String declareObjects(List<ItemGroup> pObjs) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < pObjs.size() ; i++) {
			builder.append("\t");
			builder.append(pObjs.get(i).declare())
				.append("\n");
		}
		
		return builder.toString();
	}
	
	protected String implementObjects(List<ItemGroup> pObjs, boolean pInfunction) {
		StringBuilder builder = new StringBuilder("\n");
		if(mComment != null && !mComment.equals("")) {
			builder.append("\t//" + mComment + "\n");
		}
		for(int i = 0 ; i < pObjs.size() ; i++) {
			builder.append(pObjs.get(i).implement(pInfunction))
				.append("\n");
		}
		
		return builder.toString();
	}
	
	protected String declarePosition(List<ItemGroup> pObjs) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < pObjs.size() ; i++) {
			builder.append("\t");
			builder.append(pObjs.get(i).declarePosition());
		}
		return builder.toString();
	}
	
	protected String implementPosition(List<ItemGroup> pObjs) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < pObjs.size() ; i++) {
			builder.append("\t");
			builder.append(pObjs.get(i).implementPosition());
		}
		return builder.toString();
	}
	
	protected String creatExtendFunctionsByGroup(List<ItemGroup> pObjs) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < pObjs.size() ; i++) {
			builder.append(pObjs.get(i).createExtendFunction())
				.append("\n");
		}
		return builder.toString();
	}
	
	protected void setPrefixForItemsGroup(List<ItemGroup> pObjs) {
		for(int i = 0 ; i < pObjs.size() ; i++) {
			pObjs.get(i).setPrefix(mPrefix);
		}
	}
	
	public void setProject(IProject project) {
		this.mProject = project;
	}
	
	public IProject getProject() {
		IProject project = mProject;
		CommonObject parent = mParent;
		while(mProject == null && parent != null) {
			project = parent.mProject;
			parent = parent.getParent();
		}
		
		return project;
	}
	
	public void setLocationInView(Point p) {
		mLocationInView = p;
	}
	
	public void setLocationInView(float x, float y) {
		setLocationInView(new Point(x, y));
	}
	
	public Point getLocationInView() {
		return mLocationInView;
	}
	
	public void setSize(Size size) {
		this.mSize = size;
	}
	
	public void setSize(float w, float h) {
		setSize(new Size(w, h));
	}
	
	public Size getSize() {
		if(mSizeString != null) {
			String strs[] = mSizeString.split(", ");
			setSize(Float.parseFloat(strs[0]), 
					Float.parseFloat(strs[1]));
		}
		return mSize;
	}
	
	public String getXmlPositionName() {
		return mXmlPositionName;
	}
	
	public int getViewType() {
		return mViewType;
	}
	
	public String getTagName() {
		return this.mTagName;
	}
	
	public void setTagName(String tagName) {
		this.mTagName = tagName;
	}
	
	public Point getAnchorPoint() {
		return mAnchorPoint;
	}
	
	public List<ItemGroup> getLabelGroups() {
		return this.mLabelGroups;
	}
	
	public List<ItemGroup> getMenuGroups() {
		return this.mMenuGroups;
	}
	
	public List<ItemGroup> getSpriteGroups() {
		return this.mSpriteGroups;
	}
	
	public List<ItemGroup> getMenuItemGroups() {
		return this.mMenuItemGroups;
	}
	
	public List<ItemGroup> getTableGroups() {
		return this.mTableGroups;
	}
	
	public void setAllPropertiesForObject(CommonObject obj) {
		obj.mTagName = mTagName;
		obj.mViewType = mViewType;
		obj.mXmlPositionName = mXmlPositionName;
		obj.mLocationInView = mLocationInView;
		obj.mSize = mSize;
		
		obj.mMenuItemGroups = mMenuItemGroups;
		obj.mLabelGroups = mLabelGroups;
		obj.mSpriteGroups = mSpriteGroups;
		obj.mMenuGroups = mMenuGroups;
		obj.mTableGroups = mTableGroups;
		
		obj.mPrefix = mPrefix;
		obj.mSuffix = mSuffix;
		obj.mPositionName = mPositionName;
		obj.mName = mName;
		obj.mAnchorPoint = mAnchorPoint;
		obj.mAnchorPointString = mAnchorPointString;
		obj.mSuper = mSuper;
		obj.mComment = mComment;
		obj.mType = mType;
		obj.mPositionString = mPositionString;
		obj.mPosition = mPosition;
		obj.mSizeString = mSizeString;
		obj.mBackground = mBackground;
		
		obj.mParent = mParent;
		
		obj.mDeclareObjectName = mDeclareObjectName;
		
		obj.mIsBackground = mIsBackground;
		obj.mIsUnlocated = mIsUnlocated;
		obj.mZIndex = mZIndex;
		
		obj.mProject = mProject;
	}
	
 	
	protected String mTagName;
	protected int mViewType;
	public String mXmlPositionName;
	public Point mLocationInView;
	public Size mSize;
	
	protected List<ItemGroup> mMenuItemGroups;
	protected List<ItemGroup> mLabelGroups;
	protected List<ItemGroup> mSpriteGroups;
	protected List<ItemGroup> mMenuGroups;
	protected List<ItemGroup> mTableGroups;
	
	protected String mPrefix;
	protected String mSuffix;
	protected String mPositionName;
	protected String mName;
	protected Point mAnchorPoint;
	protected String mAnchorPointString;
	protected String mSuper;
	protected String mComment;
	protected String mType;
	protected String mPositionString;
	protected Point mPosition;
	protected String mSizeString;
	protected CommonObject mBackground;
	
	protected CommonObject mParent;
	
	protected String mDeclareObjectName;
	
	protected boolean mIsBackground;
	protected boolean mIsUnlocated;
	protected String mZIndex;
	
	protected IProject mProject;
}
