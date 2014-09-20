package com.tdgc.cocos2dx.popup.creator.validate;

public class Validator {
	
	public static boolean isNumeric(String str) {
		//match a number with optional '-' and decimal.
		boolean valid = str.matches("-?\\d+(\\.\\d+)?");
		if(!valid) {
			System.err.println("ERROR::isNumeric " 
					+ str + " is invalid value");
		}
		
		return valid;
	}
	
	public static boolean isValidDoubleValueString(String doubleValue) {
		boolean valid = true;
		if(doubleValue == null || doubleValue.equals("")
				|| !doubleValue.contains(",")) {
			valid = false;
		} else {
			String values[] = doubleValue.split(",");
			values[0] = values[0].trim();
			values[1] = values[1].trim();
			if(values[0].equals("") || !isNumeric(values[0])) {
				valid = false;
			}
			if(values[1].equals("") || !isNumeric(values[1])) {
				valid = false;
			}
		}
		
		if(!valid) {
			System.err.println("ERROR::validateDoubleValueString " 
					+ doubleValue + " is invalid value");
		}
		return valid;
	}
}
