package com.tdgc.cocos2dx.popup.creator.xml;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.log.Log;
import com.tdgc.cocos2dx.popup.creator.model.Image;

public class ScreenFileParser extends DefaultHandler {
	
	public ScreenFileParser(List<Image> mImages) {
		this.mImages = mImages;
	}
	
	@Override
	public void startDocument() throws SAXException {
	}
	
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if(qName.equals(Tag.WIDGETS)) {
			String id = getAttributeValue(Attribute.ID, atts);
			for(int i = 0 ; i < mImages.size() ; i++) {
				if( mImages.get(i).getImageViewId() != null
						&& mImages.get(i).getImageViewId().equals(id)) {
					mCurrentImage = mImages.get(i);
					
					float x = getNumber(getAttributeValue(Attribute.X, atts));
					float y = getNumber(getAttributeValue(Attribute.Y, atts));
					mCurrentImage.setX(x);
					mCurrentImage.setY(y);
					break;
				}
			}
			
		}
	}
		
		
	@Override
	public void endDocument() throws SAXException {
	}
	
	@Override
	public void endElement(String namespaceURI, String localName, 
			String qName) throws SAXException {
	}
	
	@Override
	public void characters(char[] ch, int start, int length) {
	}
	
	private String getAttributeValue(String attName, Attributes atts) {
		String result = null;
		for(int i = 0 ; i < atts.getLength() ; i++) {
			String thisAtt = atts.getLocalName(i);
			if(attName.equals(thisAtt)) {
				result = atts.getValue(i);
				return result.trim();
			}
		}
		return result;
	}
	
	private float getNumber(String value) {
		if(value == null) {
			return 0;
		}
		
		float result = 0;
		try {
			result = Float.parseFloat(value);
		} catch(NumberFormatException e) {
			Log.e(e);
		} catch (NullPointerException e) {
			Log.e(e);
		}
		return result;
		
	}

	private List<Image> mImages;
	private Image mCurrentImage;
}
