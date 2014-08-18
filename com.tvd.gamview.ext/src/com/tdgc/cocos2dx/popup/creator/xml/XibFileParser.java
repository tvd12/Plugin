package com.tdgc.cocos2dx.popup.creator.xml;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.log.Log;
import com.tdgc.cocos2dx.popup.creator.model.Image;
import com.tdgc.cocos2dx.popup.creator.model.Label;

public class XibFileParser extends DefaultHandler {
	
	public XibFileParser(List<Image> pImages, List<Label> pLabels) {
		this.mImages = pImages;
		this.mLabels = pLabels;
	}
	
	@Override
	public void startDocument() throws SAXException {
		mParentQName = "";
	}
	
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		try {
		if(qName.equals(Tag.IMAGE_VIEW)) {
			String id = getAttributeValue(Attribute.ID, atts);
			for(int i = 0 ; i < mImages.size() ; i++) {
				if( mImages.get(i).getImageViewId() != null
						&& mImages.get(i).getImageViewId().equals(id)) {
					mCurrentImage = mImages.get(i);
					break;
				}
			}
			mParentQName = qName;
		}
		else if(qName.equals(Tag.LABEL)) {
			String id = getAttributeValue(Attribute.ID, atts);
			for(int i = 0 ; i < mLabels.size() ; i++) {
				if(mLabels.get(i).getLabelViewId().equals(id)) {
					mCurrentLabel = mLabels.get(i);
					break;
				}
			}
			mParentQName = qName;
		}
		else if(qName.equals(Tag.RECT) && mCurrentImage != null) {
			float x = getNumber(getAttributeValue(Attribute.X, atts));
			float y = getNumber(getAttributeValue(Attribute.Y, atts));
			float width = getNumber(getAttributeValue(Attribute.WIDTH, atts));
			float height = getNumber(getAttributeValue(Attribute.HEIGHT, atts));
			if(mParentQName.equals(Tag.IMAGE_VIEW)) {
				mCurrentImage.setSize(width, height);
				mCurrentImage.setXY(x, y);
			} 
			else if(mParentQName.equals(Tag.LABEL)) {
				mCurrentLabel.setSize(width, height);
				mCurrentLabel.setLocationInView(x, y);
			}
		}

		} catch (Exception e) {
			e.printStackTrace();
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
			String thisAtt = atts.getQName(i);
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
	private List<Label> mLabels;
	private Image mCurrentImage;
	private Label mCurrentLabel;
	private String mParentQName;
}