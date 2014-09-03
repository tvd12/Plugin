package com.tdgc.cocos2dx.popup.creator.model;

import java.util.ArrayList;
import java.util.List;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.Constants;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.AdvancedObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Point;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;
import com.tdgc.cocos2dx.popup.creator.utils.ViewUtils;

public class Cell extends AdvancedObject {

	public Cell() {
		super();
		mSuper = "ITableCellView";
		mSuffix = "cell";
		mType = "cell";
		mViewType = Constants.ViewType.CELL;
		mSuper = Config.getInstance()
				.getDefaultSupers().get(mSuffix);
		mXmlTagName = Tag.CELL;
		mIsAddToGroup = false;
		
		mTemplateName = "ITableCellView";
		mTemplateFile = "cell.template";
	}
	
	@Override
	public String declare() {
		String srcCode = super.declare();
		StringBuilder tagBuilder = new StringBuilder()
			.append(ViewUtils.createElementTags(mLabelGroupInView, 100))
			.append(ViewUtils.createElementTags(mMenuGroupInView, 200))
			.append(ViewUtils.createElementTags(mMenuItemGroupInView, 300))
			.append(ViewUtils.createElementTags(mSpriteGroupInView, 400));
		srcCode = srcCode.replace("//{element_tags}", tagBuilder.toString());
		
		return srcCode;
	}

	@Override
	public String implement(boolean pInfunction) {
		return super.implement(pInfunction);
	}
	
	@Override
	public String declarePositions() {
		return super.declarePositions();
	}
	
	@Override
	public String implementPositions() {
		return super.implementPositions();
	}
	public String declareAndImplement() {
		StringBuilder builder = new StringBuilder(declare())
			.append(implement(false));
		return builder.toString();
	}
	
	@Override
	public String getName() {
		return "this";
	}
	
	public void setImage(Image image) {
		this.mImage = image;
		this.mImage.setTabCount(mTabCount + 1);
	}
	
	@Override
	public void setParent(CommonObject parent) {
		super.setParent(parent);
		((Table)parent).addCell(this);
	}
	
	@Override
	public void update() {
		checkColumnArray();
	}
	
	public Image getImage() {
		return this.mImage;
	}
	
	@Override
	public String toXML() {
		String tab = StringUtils.tab(mTabCount);
		StringBuilder builder = new StringBuilder(tab);
		
		//set attribute
		builder.append("<" + mXmlTagName + " ")
			.append(Attribute.CLASS_NAME + "=\"" + mClassName + "\" ")
			.append(Attribute.PREFIX + "=\"" + mPrefix + "\"")
			.append("\n" + tab + "\t\t" + Attribute.SUPER + "=\"" + mSuper + "\" ")
			.append(Attribute.VISIBLE + "=\"true\" ")
			.append("\n" + tab + "\t\t" + Attribute.COMMENT + "=\"\">");
		
		//add elements
		builder.append("\n" + tab + "\t")
		.append("<" + Tag.POSITION + " " + Attribute.VALUE 
				+ "=\"" + mPosition + "\" />");
		builder.append("\n" + tab + "\t")
		.append("<" + Tag.SIZE + " " + Attribute.VALUE 
				+ "=\"" + mSize + "\" />");
		
		builder.append("\n" + mImage.toXML());
		
		builder.append("\n")
			.append(super.toXML())
			.append(tab)
			.append("</" + mXmlTagName + ">");
		
		builder.append("\n");
		
		return builder.toString();
	}
	
	private void checkColumnArray() {
		List<List<ItemGroup>> groups = new ArrayList<List<ItemGroup>>();
		groups.add(mLabelGroupInView);
		groups.add(mSpriteGroupInView);
		groups.add(mMenuGroupInView);
		groups.add(mMenuItemGroupInView);
		groups.add(mTableGroupInView);
		
		Table table = (Table)getParent();
		
		for(int k = 0 ; k < groups.size() ; k++) {
			for(int i = 0 ; i < groups.get(k).size() ; i++) {
				ItemGroup itemGroup = groups.get(k).get(i);
				if(!itemGroup.isArray()
						|| itemGroup.getArrayLength() != table.getColumns()) {
					continue;
				}
				if(!itemGroup.mValidArray) {
					itemGroup.checkArray();
				}
				if(itemGroup != null) {
					int length = itemGroup.getArrayLength();
					CommonObject itemAt0 = itemGroup.getItems().get(0);
					float widthAt0 = itemAt0.getSize().getWidth();
					float width = this.getSize().getWidth()
							- (itemAt0.getLocationInView().getX()
							- this.getLocationInView().getX());
					float margin = (width - length*widthAt0)/(length - 1);
					for(int j = 1 ; j < length; j++) {
						float x = (int)(itemAt0.getPosition().getX() 
								+ (j*widthAt0 + j*margin));
						float y = itemAt0.getPosition().getY();
						CommonObject item = itemGroup.getItems().get(j);
						item.setPosition(new Point(x, y));
						item.locationInViewWithPosition();
					}
				}
			}
		}
	}

	private Image mImage;
}
