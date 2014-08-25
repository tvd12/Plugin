package com.tdgc.cocos2dx.popup.creator.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Point;
import com.tdgc.cocos2dx.popup.creator.model.basic.Size;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;
import com.tdgc.cocos2dx.popup.creator.validate.Validator;

public class Image implements Comparable<Image> {
	
	public Image() {
		this.mId = null;
		this.mPhonyPath = null;
		this.mRealPath = null;
		this.mAnchorPoint = new Point(0.5f, 0.5f);
		this.mXmlTagName = "image";
		this.mIsAddToInterfaceBuilder = true;
		this.mLocationInInterfaceBuilder = new Point(10, 10);
	}
	
	public Image(String pId, String pRealPath, String pPhonyPath) {
		this();
		this.setId(pId);
		this.setRealPath(pRealPath);
		this.setPhonyPath(pPhonyPath);
	}
	
	public Image(String pId, String pRealPath, String pPhonyPath,
			boolean pIsBg, CommonObject pParent) {
		this();
		this.setId(pId);
		this.setRealPath(pRealPath);
		this.setPhonyPath(pPhonyPath);
		this.setIsBackground(true);
		this.setParent(pParent);
	}
	
	//change
	public void setPhonyPath(String pPhonyPath) {
		if(pPhonyPath == null) {
			return;
		}
		this.mPhonyPath = pPhonyPath.trim();
		if(this.mRealPath == null) {
			this.mRealPath = this.mPhonyPath;
		}
		if(this.mId == null) {
			this.mId = StringUtils.convertPhonyPathToId(this.mPhonyPath);
		}
		if(mRealPath != null) {
			mName = mRealPath;
			if(mRealPath.contains("/")) {
				mName = mRealPath.substring(mRealPath.lastIndexOf('/') + 1);
			}
		}
	}
	
	public String createImageViewTag(String pImagePath) 
			throws IOException {
		StringBuilder builder = new StringBuilder(StringUtils.tab(5));
		Point point = this.mLocationInInterfaceBuilder;
		if(mRealPath == null) {
			builder.append("<imageView userInteractionEnabled=\"NO\" contentMode=\"scaleToFill\" ")
				.append("horizontalHuggingPriority=\"251\" verticalHuggingPriority=\"251\" ")
				.append("id=\"" + getImageViewId() + "\">\n")
				.append(StringUtils.tab(6))
				.append("<rect key=\"frame\" x=\"" + point.getX() + "\" y=\"" + point.getY() + "\" ")
				.append("width=\"" + mSize.getWidth() +"\" height=\""+ mSize.getHeight() +"\" />\n")
				.append(StringUtils.tab(6))
				.append("<autoresizingMask key=\"autoresizingMask\" flexibleMaxX=\"YES\" " +
						"flexibleMaxY=\"YES\" />\n")
				.append(StringUtils.tab(5))
	            .append("</imageView>\n");
		} else {
			File file = new File(pImagePath + "/" + mRealPath);
			if(!file.exists()) {
				System.err.println("ERROR::createImageViewTag::file " + file + " not exists");
				return "";
			}
			BufferedImage image = ImageIO.read(file);
		    int width = image.getWidth();
		    int height = image.getHeight();
		    mSize = new Size(width, height);
		    
			builder.append("<imageView opaque=\"NO\" clipsSubviews=\"YES\" ")
				.append("multipleTouchEnabled=\"YES\" contentMode=\"scaleToFill\" ")
				.append("image=\"" + mName + "\" id=\"" + getImageViewId() + "\">\n")
				.append(StringUtils.tab(6))
				.append("<rect key=\"frame\" x=\"" + point.getX() + "\" y=\"" + point.getY() + "\" ")
				.append("width=\"" + width/2 +"\" height=\""+ height/2 +"\" />\n")
				.append(StringUtils.tab(6))
				.append("<autoresizingMask key=\"autoresizingMask\"/>\n")
				.append(StringUtils.tab(5))
	            .append("</imageView>\n");
		}
		return builder.toString();
	}
	
	public String createImageTag() {
		if(mSize == null) {
			System.err.println("ERROR::createImageTag image " 
					+ mRealPath + " has size = " + mSize);
			return "";
		}
		StringBuilder builder = new StringBuilder(StringUtils.tab(2));
		builder.append("<image name=\"" + mName + "\" ")
			.append("width=\"" + mSize.getWidth() + "\" ")
			.append("height=\"" + mSize.getHeight() + "\" />\n");
		
		return builder.toString();
	}
	
	public String createWidgetsTag(String pId, String pImagePath) throws IOException {
		mImageViewId = pId;
		BufferedImage image = ImageIO.read(new File(pImagePath + "/" + mRealPath));
	    int width = image.getWidth();
	    int height = image.getHeight();
	    String imageDirectory = pImagePath.substring(
	    		pImagePath.lastIndexOf('/') + 1);
	    mSize = new Size(width, height);
		StringBuilder builder = new StringBuilder(StringUtils.tab(1));
		builder.append("<widgets xsi:type=\"model:Image\" ")
			.append("id=\"" + mImageViewId + "\" ")
			.append("x=\"" + 100 + "\" ")
			.append("y=\"" + 100 + "\" ")
			.append("width=\"" + width/2 + "\" ")
			.append("height=\"" + height/2 + "\" ")
			.append("measuredWidth=\"" + width/2 + "\" ")
			.append("measuredHeight=\"" + height/2 + "\" ")
			.append("src=\"assets/" + imageDirectory + "/" + mRealPath + "\" ")
			.append("/>\n");
		return builder.toString();
	}
	
	public void fetchSize(String physicalPath) {
		File file = new File(physicalPath);
		if(!file.exists()) {
			System.err.println("ERROR::fetchSize::file " + file + " not exists");
			return;
		}
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
			int width = image.getWidth();
		    int height = image.getHeight();
		    setSize(width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	@Override
	public int compareTo(Image o) {
		return Integer.parseInt(this.mParent.getZIndex())
				- Integer.parseInt(o.mParent.getZIndex());
	}
	
	@Override
	public String toString() {
		return mName + " position(" + mX + ", " + ", " + mY + ")";
	}
	
	public void replaceWithAnother(Image another) {
		mId = another.mId;
		mImageViewId = another.mImageViewId;
		mRealPath = another.mRealPath;
		mPhonyPath = another.mPhonyPath;
		mName = another.mName;
		mIsBackground = another.mIsBackground;
		mSize = another.mSize;
		mX = another.mX;
		mY = another.mY;
		mParent = another.mParent;
		mAnchorPoint = another.mAnchorPoint;
	}

	public void setId(String pId) {
		this.mId = pId;
	}
	
	public void setRealPath(String pRealPath) {
		this.mRealPath = pRealPath;
	}
	
	public String getId() {
		return this.mId;
	}
	
	public String getRealPath() {
		return this.mRealPath;
	}
	
	public String getPhonyPath() {
		return this.mPhonyPath;
	}
	
	public boolean isBackground() {
		return this.mIsBackground;
	}
	
	public void setIsBackground(boolean pBackground) {
		this.mIsBackground = pBackground;
	}
	
	public void setX(float pX) {
		this.mX = pX + mAnchorPoint.getX()*mSize.getWidth()/2;
	}
	
	public float getX() {
		return this.mX;
	}
	
	public void setY(float pY) {
		this.mY = pY + mAnchorPoint.getY()*mSize.getHeight()/2;
	}
	
	public void setXY(float pX, float pY) {
		this.mX = pX;// + mAnchorPoint.getX()*mSize.getWidth();
		this.mY = pY;// + mAnchorPoint.getY()*mSize.getHeight();
		this.mParent.setLocationInView(pX, pY);
		this.setLocationInInterfaceBuilder(pX, pY);
	}
	
	public void alignFollowParrent() {
		try {
			if(mParent.getLocationInView() == null || !mIsAddToInterfaceBuilder) {
				System.err.println("ERROR:: name = " + this.getRealPath() + " has "
						+ "parent.getLocationInView == null");
				return;
			}
			float x = mParent.getLocationInView().getX();
			float y = mParent.getLocationInView().getY();
			CommonObject parentOfParent = mParent.getParent();
			if(parentOfParent != null) {
				x = x - parentOfParent.getLocationInView().getX();
				y = y - parentOfParent.getLocationInView().getY();
				
				parentOfParent = parentOfParent.getParent();
			}
			mAnchorPoint = mParent.getAnchorPoint();
			float anchorpointY = 1 - mAnchorPoint.getY();
			x = x + mAnchorPoint.getX()*mSize.getWidth();
			if(mParent.getParent() != null) {
				y = mParent.getParent().getSize().getHeight() - 
					(y + anchorpointY*mSize.getHeight());
			} else {
				y = y + anchorpointY*mSize.getHeight();
			}
			this.mX = x;
			this.mY = y;
			mParent.setPosition(x, y);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fetchLocationInViewFromParent() {
		try {
			if(mParent.getLocationInView() == null || !mIsAddToInterfaceBuilder) {
				System.err.println("ERROR:: name = " + this.getRealPath() + " has "
						+ "parent.getLocationInView == null");
				return;
			}
			float x = mParent.getLocationInView().getX();
			float y = mParent.getLocationInView().getY();
			CommonObject parentOfParent = mParent.getParent();
			if(parentOfParent != null) {
				x = x - parentOfParent.getLocationInView().getX();
				y = y - parentOfParent.getLocationInView().getY();
				
				parentOfParent = parentOfParent.getParent();
			}
			mAnchorPoint = mParent.getAnchorPoint();
			float anchorpointY = 1 - mAnchorPoint.getY();
			x = x + mAnchorPoint.getX()*mSize.getWidth();
			if(mParent.getParent() != null) {
				y = mParent.getParent().getSize().getHeight() - 
					(y + anchorpointY*mSize.getHeight());
			} else {
				y = y + anchorpointY*mSize.getHeight();
			}
			this.mX = x;
			this.mY = y;
			mParent.setPosition(x, y);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public float getY() {
		return this.mY;
	}
	
	public String getImageViewId() {
		return this.mImageViewId;
	}
	
	public void setParent(CommonObject pParent) {
		
		this.mTabCount = pParent.getTabCount() + 1;
		
		if(pParent instanceof Sprite) {
			((Sprite)pParent).setImage(this);
		} else if(pParent instanceof Table) {
			((Table)pParent).setImage(this);
		} else if(pParent instanceof Cell) {
			((Cell)pParent).setImage(this);
		}
		
		this.mParent = pParent;
		String anchopointString = null;
		while (pParent != null && anchopointString == null) {
			anchopointString = pParent.getAnchorPointString();
			pParent = pParent.getParent();
		}
		if(anchopointString == null || anchopointString.equals("default")) {
			return;
		}

		String values[] = anchopointString.split(",");
		mAnchorPoint = new Point(Float.parseFloat(values[0]),
				Float.parseFloat(values[1]));
	}
	
	public Size getSize() {
		return this.mSize;
	}
	
	public void setSize(String size) {
		if(Validator.isValidDoubleValueString(size)) {
			String wh[] = size.split(",");
			this.setSize(Float.parseFloat(wh[0]), 
					Float.parseFloat(wh[1]));
		}
	}
	
	public void setSize(Size size) {
		this.mSize = size;
		this.mParent.setSize(size);
	}
	
	public void setSize(float w, float h) {
		setSize(new Size(w, h));
	}
	
	public void setImageViewId(String id) {
		mImageViewId = id;
	}
	
	public CommonObject getParent() {
		return this.mParent;
	}
	
	public void setTabCount(int tabCount) {
		this.mTabCount = tabCount;
	}
	
	public int getTabCount() {
		return this.mTabCount;
	}
	
	public String toXML(boolean includeSize) {
		String tab = StringUtils.tab(mTabCount);
		StringBuilder builder = new StringBuilder(tab);
		builder.append("<" + mXmlTagName + " ")
			.append(Attribute.ID + "=\"" + mId + "\" ")
			.append("\n" + tab + "\t");
		builder.append(Attribute.LOCATION_IN_INTERFACEBUILDER + "=\"") 
			.append(mLocationInInterfaceBuilder + "\"");
			
		if(includeSize && mSize != null) {
			builder.append("\n" + tab + "\t");
			builder.append(Attribute.WIDTH + "=\"" + mSize.getWidth() + "\" ")
				.append(Attribute.HEIGHT + "=\"" + mSize.getHeight() + "\"");
		} 
		if(mPhonyPath != null) {
			builder.append("\n" + tab + "\t")
				.append(Attribute.PHONY_PATH + "=\"" + mPhonyPath + "\" ");
		}
		builder.append(" />");
		
		return builder.toString();
	}
	
	public String toXML() {
		return toXML(true);
	}
	
	public void setAddToInterfaceBuilder(boolean value) {
		this.mIsAddToInterfaceBuilder = value;
	}
	
	public boolean isAddToInterfaceBuilder() {
		return this.mIsAddToInterfaceBuilder;
	}
	
	public void setLocationInInterfaceBuilder(String location) {
		if(Validator.isValidDoubleValueString(location)) {
			String strs[] = location.split(", ");
			this.setLocationInInterfaceBuilder(
					Float.parseFloat(strs[0]), Float.parseFloat(strs[1]));
		}
	}
	
	public void setLocationInInterfaceBuilder(float x, float y) {
		this.setLocationInInterfaceBuilder(new Point(x, y));
	}
	
	public void setLocationInInterfaceBuilder(Point location) {
		this.mLocationInInterfaceBuilder = location;
		this.mParent.setLocationInView(location);
	}
	
	public Point getLocationInInterfaceBuilder() {
		return this.mLocationInInterfaceBuilder;
	}
	
	protected boolean mIsAddToInterfaceBuilder;
	
	private String mId;
	private String mImageViewId;
	private String mRealPath;
	private String mPhonyPath;
	private String mName;
	private boolean mIsBackground;
	private Size mSize;
	private float mX;
	private float mY;
	private CommonObject mParent;
	private Point mAnchorPoint;
	private int mTabCount;
	private String mXmlTagName;
	private Point mLocationInInterfaceBuilder;
}
