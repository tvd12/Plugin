package com.tdgc.cocos2dx.popup.creator.xml;

import java.util.List;

import com.tdgc.cocos2dx.popup.creator.model.Image;
import com.tdgc.cocos2dx.popup.creator.model.Label;

public class XibFetcher extends XmlFetcher {
	
	public XibFetcher(List<Image> pImages, List<Label> pLabels) {
		mHandler = new XibFileParser(pImages, pLabels);
	}

}
