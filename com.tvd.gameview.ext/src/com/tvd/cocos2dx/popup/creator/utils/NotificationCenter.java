/**
 * Copyright (c) 2014 Dung Ta Van . All rights reserved.
 * 
 * This file is part of com.tvd.gameview.ext.
 * com.tvd.gameview.ext is free eclipse plug-in: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * com.tvd.gameview.ext is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with com.tvd.gameview.ext.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tvd.cocos2dx.popup.creator.utils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationCenter {
	
	private NotificationCenter() {
		mListeners = new CopyOnWriteArrayList<NotificationCenter
				.INotificationListener>();
	}
	
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
	
	public void putError(String error) {

		notifyErrorToListeners(error);
		
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
	
	public void addListener(INotificationListener listener) {
		if(!mListeners.contains(listener)) {
			mListeners.add(listener);
		}
	}
	
	public void removeListener(INotificationListener listener) {
		mListeners.remove(listener);
	}
	
	public void notifyErrorToListeners(String error) {
		for(int i = 0 ; i < mListeners.size() ; i++) {
			if(mListeners.get(i) != null) {
				mListeners.get(i).onError(error);
			}
		}
	}
	
	public void notifyWarningToListeners(String warning) {
		for(int i = 0 ; i < mListeners.size() ; i++) {
			mListeners.get(i).onWarning(warning);
		}
	}
	
	private String mTopError;
	private List<INotificationListener> mListeners;
	private static NotificationCenter sInstance;
	
	public interface INotificationListener {
		
		public void onError(String error);
		
		public void onWarning(String warning);
	}
}
