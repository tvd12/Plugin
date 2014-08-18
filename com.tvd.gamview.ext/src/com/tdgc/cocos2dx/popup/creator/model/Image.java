package com.tdgc.cocos2dx.popup.creator.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Point;
import com.tdgc.cocos2dx.popup.creator.model.basic.Size;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public class Image implements Comparable<Image> {
	
	public Image() {
		this.mId = null;
		this.mPhonyPath = null;
		this.mRealPath = null;
		this.mAnchorPoint = new Point(0.5f, 0.5f);
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
	
	public void setPhonyPath(String pPhonyPath) {
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
		BufferedImage image = ImageIO.read(new File(pImagePath + "/" + mRealPath));
	    int width = image.getWidth();
	    int height = image.getHeight();
	    mSize = new Size(width, height);
	    
		StringBuilder builder = new StringBuilder(StringUtils.tab(5));
		builder.append("<imageView opaque=\"NO\" clipsSubviews=\"YES\" ")
			.append("multipleTouchEnabled=\"YES\" contentMode=\"scaleToFill\" ")
			.append("image=\"" + mName + "\" id=\"" + getImageViewId() + "\">\n")
			.append(StringUtils.tab(6))
			.append("<rect key=\"frame\" x=\"10\" y=\"10\" ")
			.append("width=\"" + width/2 +"\" height=\""+ height/2 +"\" />\n")
			.append(StringUtils.tab(6))
			.append("<autoresizingMask key=\"autoresizingMask\"/>\n")
			.append(StringUtils.tab(5))
            .append("</imageView>\n");
		
		return builder.toString();
	}
	
	public String createImageTag() {
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
	}
	
	public void alignFollowParrent() {
		try {
			float x = mParent.getLocationInView().getX();
			float y = mParent.getLocationInView().getY();
			CommonObject parentOfParent = mParent.getParent();
			if(parentOfParent != null) {
				x = x - parentOfParent.getLocationInView().getX();
				y = y - parentOfParent.getLocationInView().getY();
				
				parentOfParent = parentOfParent.getParent();
			}
			x = x + mAnchorPoint.getX()*mSize.getWidth();
			if(mParent.getParent() != null) {
				y = mParent.getParent().getSize().getHeight() - 
					(y + mAnchorPoint.getY()*mSize.getHeight());
			} else {
				y = y + mAnchorPoint.getY()*mSize.getHeight();
			}
			
			this.mX = x;
			this.mY = y;
			
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
}