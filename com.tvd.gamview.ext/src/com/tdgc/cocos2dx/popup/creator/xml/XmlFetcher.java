package com.tdgc.cocos2dx.popup.creator.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.tdgc.cocos2dx.popup.creator.log.Log;
import com.tdgc.cocos2dx.popup.creator.model.View;

public class XmlFetcher {
	
	public XmlFetcher() {
		this.mHandler = new XmlFileParser();
	}

	public void fetchData(String pOutputPath) {
		try {
			File file = new File(pOutputPath);
			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			InputSource inputSource = new InputSource(reader);
			inputSource.setEncoding("UTF-8");
			
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(mHandler);
			xr.parse(inputSource);
			
		} catch (Exception e) {
			Log.e(e);
		}
		
	}
	
	public View fetchView(String pOutputPath) {
		fetchData(pOutputPath);
		View view = ((XmlFileParser)mHandler).getView();
		view.setXmlFilePath(pOutputPath);
		
		return view;
	}
	
	protected DefaultHandler mHandler;
	
}
