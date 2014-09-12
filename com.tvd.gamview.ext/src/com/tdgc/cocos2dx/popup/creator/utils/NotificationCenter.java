package com.tdgc.cocos2dx.popup.creator.utils;

public class NotificationCenter {

	public static void notify(String message, int type) {
		switch (type) {
		case Type.INFO:		
			System.out.println("INFO::" + message);
			break;
		case Type.WARNING:
			System.out.println("WARNING::" + message);
			break;
		case Type.ERROR:
			System.err.println("ERROR::" + message);
		default:
			System.out.println("UNKNOWN::" + message);
			break;
		}
	}
	
	public static void i(String message) {
		notify(message, Type.INFO);
	}
	
	public static void w(String message) {
		notify(message, Type.WARNING);
	}
	
	public static void e(String message) {
		notify(message, Type.ERROR);
	}
	
	public static interface Type {
		public static final int INFO			= 1001;
		public static final int WARNING			= 1002;
		public static final int ERROR			= 1003;
	}
	
}
