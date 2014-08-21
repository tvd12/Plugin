package com.tdgc.cocos2dx.popup.creator.utils;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.model.Image;
import com.tdgc.cocos2dx.popup.creator.model.Label;

public class XmlContentUtils {
	public static String replaceSpritePosition(String pFileContent, Image pImg) {
		if(pImg == null || pImg.getSize() == null) {
			return pFileContent;
		}
		
		String contents[] = pFileContent.split("\n");
		String newPosition = pImg.getX() + ", " + pImg.getY();
		
		String newSize = pImg.getSize().toString();
		String sizeOpenTag = "<" + Tag.SIZE + ">";
 		String sizeCloseTag = "</" + Tag.SIZE + ">";
		
		for(int i = 0 ; i < contents.length ; i++) {
			if(contents[i].contains(pImg.getId())) {
				while(!(contents[--i].contains(Tag.POSITION) && contents[i].contains(Attribute.VALUE))) {
					if(contents[i].contains(sizeOpenTag)) {
						String currentSize = contents[i].substring(
								contents[i].indexOf(sizeOpenTag) + sizeOpenTag.length(),
								contents[i].indexOf(sizeCloseTag));
						contents[i] = contents[i].replace(currentSize, newSize);
					}
				}
				String currentPosition = contents[i].substring(
						contents[i].indexOf('"') + 1,
						contents[i].lastIndexOf('"'));
				if(!currentPosition.equals("default")) {
					contents[i] = contents[i].replace(currentPosition, newPosition);
				}
				break;
			}
		}
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < contents.length ; i++) {
			builder.append(contents[i])
				.append("\n");
		}
		
		return builder.toString();
	}
	
	public static String replaceSpritePosition(String pFileContent, Label pLabel) {
		String contents[] = pFileContent.split("\n");
		String newPosition = pLabel.getPosition().getX() + ", " 
				+ pLabel.getPosition().getY();
		String openTag = "<" + Tag.POSITION + ">";
		String closeTag = "</" + Tag.POSITION + ">";
		for(int i = 0 ; i < contents.length ; i++) {
			if(contents[i].contains(pLabel.getXmlPositionName())) {
				while(!contents[++i].contains(openTag));
				String currentPosition = contents[i].substring(
						contents[i].indexOf(openTag) + openTag.length(),
						contents[i].indexOf(closeTag));
				if(!currentPosition.equals("default")) {
					contents[i] = contents[i].replace(currentPosition, newPosition);
				}
				break;
			}
		}
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < contents.length ; i++) {
			builder.append(contents[i])
				.append("\n");
		}
		
		return builder.toString();
	}
}
