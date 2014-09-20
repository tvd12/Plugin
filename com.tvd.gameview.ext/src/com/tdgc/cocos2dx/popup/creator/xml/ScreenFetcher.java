package com.tdgc.cocos2dx.popup.creator.xml;

import java.util.List;

import com.tdgc.cocos2dx.popup.creator.model.Image;

public class ScreenFetcher extends XmlFetcher {
	
	public ScreenFetcher(List<Image> pImages) {
		mHandler = new ScreenFileParser(pImages);
	}

}
