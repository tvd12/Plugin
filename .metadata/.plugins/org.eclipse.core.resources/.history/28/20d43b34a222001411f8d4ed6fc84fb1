package com.tdgc.cocos2dx.popup.creator.model;

import java.util.ArrayList;
import java.util.List;

import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public class ItemGroup {
	public ItemGroup(int pType) {
		super();
		this.mType = pType;
		this.mItems = new ArrayList<CommonObject>();
		this.mIsArray = false;
	}
	
	public String declare() {
		if(mItems.size() == 0) {
			return "";
		}
		
		StringBuilder builder = new StringBuilder("\n");
		String name = mItems.get(0).getName().replaceAll("[0-9]Sprite", "Sprites");
		if(mIsArray) {
			String template = new FileUtils().fetchTemplate("vector", 
					"src/com/template/array_declare.template");
			template = template.replace("{tab}", "\t")
					.replace("{type}", mItems.get(0).getDeclareObjectName() + "*")
					.replace("{name}", name);
			builder.append(template);
			mNodesArrayName = name;
		} else {
			for(int i = 0 ; i < mItems.size() ; i++) {
				builder.append("\t" + mItems.get(i).declare())
					.append("\n");
			}
		}
		
		return builder.toString();
	}
	
	public String implement(boolean pInfunction) {
		declare();
		StringBuilder builder = new StringBuilder(comment(1));
		for(int i = 0 ; i < mItems.size() ; i++) {
			if(mIsArray) {
				mItems.get(i).setName(mNodesArrayName + "[" + i + "]");
			}
			builder.append("\t" + mItems.get(i).implement(pInfunction).trim())
				.append("\n");
			if(mResources != null) {
				builder.append("\t" + mResources.getName() 
						+ "(" + mItems.get(i).getName() + ");");
			}
			builder.append("//{n}");
			
		}
		return builder.toString();
	}
	
	public String declarePosition() {
		StringBuilder builder = new StringBuilder("\n");
		if(mIsArray && mItems.size() > 0) {
			String declare = mItems.get(0).declarePositions()
					.replaceAll("_\\d{0,32768}_", "_")
					.replace(";", "S[" + mItems.size() + "];");
			builder.append(declare);
		} else {
			for(int i = 0 ; i < mItems.size() ; i++) {
				String pos = mItems.get(i).declarePositions();
				if(!pos.trim().equals("")) {
					builder.append(pos);
				}
			}
		}
		if(mResources != null) {
			String resourcePosition = mResources.declarePositions();
			if(resourcePosition != null && !resourcePosition.equals("")) {
				builder.append(resourcePosition);
			}
		}
		
		return builder.toString();
	}
	
	public String implementPosition() {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mItems.size() ; i++) {
			String positionName = mItems.get(i).getPositionName();
			if(mIsArray && positionName != null && !positionName.equals("")) {
				positionName = positionName.replaceAll("_\\d{0,32768}_", "_")
						.replace("POSITION", "POSITIONS[" + i + "]");
				mItems.get(i).replacePositionName(positionName);
			}
			String pos = mItems.get(i).implementPositions();
			if(!pos.trim().equals("")) {
				builder.append(pos);
			}
		}
		
		if(mResources != null) {
			String resourcePosition = mResources.implementPositions();
			if(resourcePosition != null && !resourcePosition.equals("")) {
				builder.append(resourcePosition);
			}
		}
		
		return builder.toString();
	}
	
	public void pushBack() {
		switch (mType) {
		case Type.MENU:
			mContainer.addMenuGroups(this);
			break;
			
		case Type.MENUITEM:
			mContainer.addMenuItemGroups(this);
			break;
			
		case Type.SPRITE:
			mContainer.addSpriteGroups(this);
			break;
			
		case Type.TABLE:
			mContainer.addTableGroups(this);
			break;
			
		case Type.LABLE:
			mContainer.addLabelGroups(this);
			break;
		default:
			break;
		}
	}
	
	public String createExtendFunction() {
		if(mResources != null) {
			return mResources.createExtendFunction();
		}
		return "";
	}
	
	public void addItem(CommonObject pObject) {
		this.mItems.add(pObject);
	}
	
	public void setBlockComment(String pBlockComment) {
		this.mBlockComment = pBlockComment;
	}
	
	public void setResources(Resources pResources) {
		this.mResources = pResources;
	}
	
	public String getBlockComment() {
		return this.mBlockComment;
	}
	
	public Resources getResources() {
		return this.mResources;
	}
	
	public void setIsArray(boolean pIsArray) {
		this.mIsArray = pIsArray;
	}
	
	public boolean isArray() {
		return this.mIsArray;
	}
	
	public void setType(int pType) {
		this.mType = pType;
	}
	
	public int getType() {
		return this.mType;
	}
	
	public void setContainer(CommonObject pContainer) {
		this.mContainer = pContainer;
	}
	
	public CommonObject getContainer() {
		return this.mContainer;
	}
	
	public void setBeforeGroup(ItemGroup pBeforeGroup) {
		this.mBeforeGroup = pBeforeGroup;
	}
	
	public ItemGroup getBeforeGroup() {
		if(this.mBeforeGroup != null) {
			return this.mBeforeGroup;
		} else {
			return this;
		}
	}
	
	public void setPrefix(String pPrefix) {
		for(int i = 0 ; i < mItems.size() ; i++) {
			mItems.get(i).setPrefix(pPrefix);
		}
	}
	
	public String getNodesArrayName() {
		return this.mNodesArrayName;
	}
	
	public List<CommonObject> getItems() {
		return this.mItems;
	}
	
	public interface Type {
		public static final int MENU 		= 1000;
		public static final int MENUITEM 	= 1001;
		public static final int SPRITE 		= 1002;
		public static final int TABLE 		= 1003;
		public static final int LABLE 		= 1004;
		public static final int VIEW 		= 1005;
		public static final int RESOURCE 	= 1006;
	}
	
	private String comment(int pNumberOfTab) {
		String tab = StringUtils.tab(pNumberOfTab);
		StringBuilder builder = new StringBuilder();
		builder.append(tab + "//" + mBlockComment + "\n");
		
		return builder.toString();
	}
	
	protected List<CommonObject> mItems;
	protected String mBlockComment;
	protected Resources mResources;
	protected boolean mIsArray;
	protected int mType;
	protected CommonObject mContainer;
	protected ItemGroup mBeforeGroup;
	protected String mNodesArrayName;
}
