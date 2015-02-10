package com.tvd.gext.multipageeditor.elements;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	
	private Messages() {
	
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	private static final String BUNDLE_NAME = 
			"com.tvd.gext.multipageeditor.elements.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = 
			ResourceBundle.getBundle(BUNDLE_NAME);
}
