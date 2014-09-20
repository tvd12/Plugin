package com.tdgc.cocos2dx.popup.creator.model.basic;

import com.tdgc.cocos2dx.popup.creator.validate.Validator;

public class Point {

	public Point(float pX, float pY) {
		this.mX = pX;
		this.mY = pY;
	}
	
	@Override
	public String toString() {
		return mX + ", " + mY;
	}
	
	public float getX() {
		return this.mX;
	}
	
	public float getY() {
		return this.mY;
	}
	
	public void add(Point p) {
		this.mX = mX + p.getX();
		this.mY = mY + p.getY();
	}
	
	public static Point parsePoint(String str) {
		Point point = null;
		if(str != null && Validator.isValidDoubleValueString(str)) {
			String xy[] = str.split(",");
			point = new Point(Float.parseFloat(xy[0]), 
					Float.parseFloat(xy[1]));
		} else {
			System.err.println("ERROR::parsePoint " + str + " is invalid point");
		}
		
		return point;
	}
	
	private float mX, mY;
	
}
