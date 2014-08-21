package com.tdgc.cocos2dx.popup.creator.model;

import java.util.ArrayList;
import java.util.List;

import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;
import com.tdgc.cocos2dx.popup.creator.utils.ViewUtils;

public class ItemGroup {
	public ItemGroup(int pType) {
		super();
		this.mType = pType;
		this.mItems = new ArrayList<CommonObject>();
		this.mIsArray = false;
		this.mLength = 1;
		this.mValidArray = false;
		this.mReferenceCount = 0;
	}
	
	public String declare() {
		if(mItems.size() == 0) {
			return "";
		}
		StringBuilder builder = new StringBuilder("\n");
		if(!mValidArray) {
			checkArray();
		}
		if(mValidArray) {
			String declareObjetName = mItems.get(0).getDeclareObjectName();
			builder.append("\t" + declareObjetName + "* " + mArrayName)
			.append(";\n");
		}
		else {
			for(int i = 0 ; i < mItems.size() ; i++) {
				builder.append("\t" + mItems.get(i).declare())
					.append("\n");
			}
		}
		return builder.toString();
	}
	
	public String implement(boolean pInfunction) {
		declare();
		if(mReferenceCount > 0) {
			return "";
		}
		
		StringBuilder builder = new StringBuilder(comment(1));
		if(mValidArray) {
			String name = mItems.get(0).getName();
			String newName = name.substring(0, name.lastIndexOf('[')) + "[i]";
			String positionName = mItems.get(0).getPositionName();
			String newPositionName = positionName.substring(0, positionName.lastIndexOf('[')) + "[i]";
			for(int i = 0 ; i < mLength ; i++) {
				mItems.get(i).setName(newName);
				mItems.get(i).setNewPositionName(newPositionName);
			}
			
			builder.append("\tfor(int i = 0 ; i < " + mLength + " ; i++) {");
			
			StringBuilder builderForObj = new StringBuilder();
			builderForObj.append(mItems.get(0).implement(false))
				.append("\n");
			ViewUtils.implementObject(mItems.get(0), builderForObj);
			
			String strs[] = builderForObj.toString().split("\n");
			for(int i = 0 ; i < strs.length ; i++) {
				builder.append("\t" + strs[i] + "\n");
			}
			builder.append("\t}");
			
			for(int i = 0 ; i < mLength ; i++) {
				String oldName = mItems.get(i).getName().replace("[i]", 
						"[" + i + "]");
				String oldPositionName = mItems.get(i).getPositionName().replace("[i]", 
						"[" + i + "]");
				mItems.get(i).setName(oldName);
				mItems.get(i).setNewPositionName(oldPositionName);
			}
			
			processArray();
		} 
		else {
			for(int i = 0  ; i < mItems.size() ; i++) {
				builder.append("\t" + mItems.get(i).implement(pInfunction).trim())
					.append("\n");
				builder.append("//{n}");
				
			}
		}
		return builder.toString();
	}
	
	public String declarePosition() {
		StringBuilder builder = new StringBuilder();
		if(!mValidArray) {
			checkArray();
		}
		if(mValidArray) {
			builder.append("\tCCPoint " + mPositionArrayName).append(";\n");
		} else {
			for(int i = 0 ; i < mItems.size() ; i++) {
				String pos = mItems.get(i).declarePositions();
				if(!pos.trim().equals("")) {
					builder.append("\t" + pos).append("\n");
				}
			}
		}
		return builder.toString();
	}
	
	public String implementPosition() {
		if(!mValidArray) {
			checkArray();
		}
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < mItems.size() ; i++) {
			String pos = mItems.get(i).implementPositions();
			if(!pos.trim().equals("")) {
				builder.append("\t" + pos).append("\n");
			}
		}
		
		return builder.toString();
	}
	
	public String createElementTags(int begin) {
		StringBuilder builder = new StringBuilder("\n");
		for(int i = 0 ; i < mItems.size() ; i++) {
			builder.append("\t\t" + mItems.get(i).getTagName())
			.append(" = " + (begin + i))
			.append(",\n");
		}
		return builder.toString();
	}
	
	public void pushBack() {
		switch (mType) {
		case Type.MENU:
			mContainer.addMenuGroup(this);
			break;
			
		case Type.MENUITEM:
			mContainer.addMenuItemGroup(this);
			break;
			
		case Type.SPRITE:
			mContainer.addSpriteGroup(this);
			break;
			
		case Type.TABLE:
			mContainer.addTableGroup(this);
			break;
			
		case Type.LABLE:
			mContainer.addLabelGroup(this);
			break;
		default:
			break;
		}
	}
	
	protected boolean checkArray() {
		if(mIsArray && mLength > 1 && mItems.size() == 1) {
			mValidArray = true;
			processArray();
		}
		
		return mValidArray;
	}
	
	private void processArray() {
		if(mValidArray) {
			CommonObject obj = mItems.get(0);
			this.mArrayName = obj.getName() + "s[" + mLength + "]";
			this.mPositionArrayName = obj.getPositionName()
					+ "S[" + mLength + "]";
			for(int i = 1 ; i < mLength ; i++) {
				CommonObject newObj = obj.clone();
				newObj.setName(newObj.getName() + "s[" + i + "]");
				newObj.setNewPositionName(newObj.getPositionName() + "S[" + i + "]");
				newObj.setTagName(newObj.getTagName() + i);
				mItems.add(newObj);
			}
			obj.setName(obj.getName() + "s[0]");
			obj.setNewPositionName(obj.getPositionName() + "S[0]");
			mValidArray = true;
			
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
	
	public void setIsArray(String isArray) {
		if(isArray != null) {
			setIsArray(Boolean.parseBoolean(isArray));
		}
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
	
	public void setArrayLength(int length) {
		mLength = length;
	}
	
	public void setArrayLength(String length) {
		if(length != null) {
			setArrayLength(Integer.parseInt(length));
		}
	}
	
	public void setReferenceCount(int rfc) {
		mReferenceCount = rfc;
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
	protected int mLength;
	protected boolean mValidArray;
	protected String mArrayName;
	protected String mPositionArrayName;
	private int mReferenceCount;
}
