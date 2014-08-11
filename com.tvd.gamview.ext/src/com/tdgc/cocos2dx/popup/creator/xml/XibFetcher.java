package com.tdgc.cocos2dx.popup.creator.xml;

import java.util.List;

import com.tdgc.cocos2dx.popup.creator.model.Image;

public class XibFetcher extends XmlFetcher {
	
	public XibFetcher(List<Image> pImages) {
		mHandler = new XibFileParser(pImages);
	}

}
