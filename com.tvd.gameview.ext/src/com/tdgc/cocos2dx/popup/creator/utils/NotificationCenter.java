package com.tdgc.cocos2dx.popup.creator.utils;

public class NotificationCenter {
	
	public static NotificationCenter getInstance() {
		if(sInstance == null) {
			synchronized (NotificationCenter.class) {
				if(sInstance == null) {
					sInstance = new NotificationCenter();
				}
			}
		}
		
		return sInstance;
	}

	public static void notify(String message, int type, boolean isOpenDialog) {
		switch (type) {
		case Type.INFO:		
			System.out.println("INFO::" + message);
			break;
		case Type.WARNING:
			System.out.println("WARNING::" + message);
			break;
		case Type.ERROR:
			System.err.println("ERROR::" + message);
			break;
		default:
			System.out.println("UNKNOWN::" + message);
			break;
		}
	}
	
	public static void i(String message, boolean isOpenDialog) {
		notify(message, Type.INFO, isOpenDialog);
	}
	
	public static void w(String message, boolean isOpenDialog) {
		notify(message, Type.WARNING, isOpenDialog);
	}
	
	public static void e(String message, boolean isOpenDialog) {
		notify(message, Type.ERROR, isOpenDialog);
	}
	
	public static void i(String message) {
		notify(message, Type.INFO, false);
	}
	
	public static void w(String message) {
		notify(message, Type.WARNING, false);
	}
	
	public static void e(String message) {
		notify(message, Type.ERROR, false);
	}
	
	public static interface Type {
		public static final int INFO			= 1001;
		public static final int WARNING			= 1002;
		public static final int ERROR			= 1003;
	}
	
	public void pushError(String error) {
		if(mTopError == null) {
			this.mTopError = error;
		}
	}
	
	public void replaceTopError(String newError) {
		this.mTopError = newError;
	}
	
	public String getTopError() {
		String error = mTopError;
		mTopError = null;
		
		return error;
	}
	
	private String mTopError;
	private static NotificationCenter sInstance;
}
