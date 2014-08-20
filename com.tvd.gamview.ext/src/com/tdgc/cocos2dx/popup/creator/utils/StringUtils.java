package com.tdgc.cocos2dx.popup.creator.utils;

import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.global.Config;

public class StringUtils {
	
	public static String tab(int pNumTab) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < pNumTab ; i++) {
			builder.append("\t");
		}
		
		return builder.toString();
	}
	
	public static String convertPhonyPathToId(String pImagePhonyPath) {
		String result = pImagePhonyPath.replace("/", "_");
		result = result
				.replace(Tag.MENUITEMS, "")
				.replace(Tag.MENUS, "")
				.replace(Tag.SPRITES, "")
				.replace(Tag.TABLES, "")
				.replace(".png", "")
				.replace("[", "")
				.replace("]", "")
				.replace("__", "_");
		
		return result;
	}

	public static String getNameFromPositionName(String pPositionName) {
		String strs[] = pPositionName.split("_");
		String variableName = "m";
		for(int k = 0 ; k < strs.length ; k++) {
			variableName += strs[k].charAt(0)
					+ strs[k].substring(1).toLowerCase();
		}
		
		return variableName;
	}
	
	public static String splitAndSubString(String pStr, String pRegex, int pBegin, int pEnd) {
		String strs[] = pStr.split(pRegex);
		String result = "";
		for(int i = pBegin ; i < strs.length - pEnd ; i++) {
			result += strs[i] + "_";
		}
		
		return result;
	}
	
	public static String convertToClassName(String pRawName, String pSuffix) {
		String result = "";
		String strs[] = pRawName.split("_");
		for(int i = 0 ; i < strs.length ; i++) {
			result += strs[i].toUpperCase().charAt(0)
					+ strs[i].substring(1);
		}
		result += pSuffix;
		
		return result;
	}
	
	public static String convertToNormalProperty(String pStr) {
		pStr = pStr.toLowerCase();
		String result = Config.getInstance().getNormalPropertyPrefix();
		String strs[] = pStr.split("_");
		for(int i = 0 ; i < strs.length ; i++) {
			result += strs[i].toUpperCase().charAt(0)
					+ strs[i].substring(1);
		}
		
		return result;
	}
	
	public static String convertToTagName(String pStr, String pType) {
		pStr = pStr.toLowerCase();
		String strs[] = pStr.split("_");
		String result = strs[0].toLowerCase();
		for(int i = 1 ; i < strs.length ; i++) {
			result += strs[i].toUpperCase().charAt(0)
					+ strs[i].substring(1);
		}
		
		return result + pType + "Tag";
	}
	
	public static int numberOfLetter(String pStr, char pLetter) {
		int result = 0;
		for(int i = 0 ; i < pStr.length() ; i++) {
			if(pStr.charAt(i) == pLetter) {
				result ++;
			}
		}
		
		return result;
	}
	
	public static String getParentName(String pChildName, String pChildPath) {
		String temp = 
				pChildPath.substring(0, pChildPath.indexOf("/" + pChildName));
		return temp.substring(temp.lastIndexOf("/" + 1));
	}
	
	public static String getSpritePostionName(String pImagePath) {
		String result = pImagePath.substring(pImagePath.lastIndexOf("/") + 1);
		if(pImagePath.contains(".")) {
			result = result.substring(0, (result.lastIndexOf(".")));
		}
		
		return result;
	}
	
	public static String getObjectPositioName(String pPath) {
		return pPath.substring(pPath.lastIndexOf("/") + 1);
	}
	
	public static String detectPrefix(String pViewType) {
		return pViewType.toLowerCase();
	}
	
	public static String standardizeCode(String pCode) {
		String strs[] = pCode.split("\n");
		StringBuilder builder = new StringBuilder();
		
		int count = 0;
		for(int i = 0 ; i < strs.length ; i++) {
			if(strs[i].trim().equals("")) {
				count ++;
			}
			else {
				count = 0;
			}
			if(count < 2) {
				builder.append(strs[i])
					.append("\n");
			}
			
		}
		return builder.toString();
	}
	
	public static String generateString(int pNumber) {
		String result = String.valueOf(pNumber);
		for(int i = 0 ; i <= 3 - result.length() ; i++) {
			result = "0" + result;
		}
		
		return result;
	}
	
//	public static void main(String[] args) {
//		System.out.println("dung_1234".replaceAll("_\\d{0,32768}", "ha"));
//	}
	
}
