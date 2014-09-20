package com.tdgc.cocos2dx.popup.creator.xml;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLErrorHandler extends DefaultHandler {
	public XMLErrorHandler() {
		mWarningCount = 0;
		mFatalErrorCount = 0;
		mNonFatalErrorCount = 0;
	}
	
	@Override
	public void fatalError(SAXParseException exception) 
			throws SAXException {
		System.out.println("**Parsing Fatal Error**\n" + "Line: " +
				exception.getLineNumber( ) + "\n" + "URI: "+
				                              exception.getSystemId( ) + "\n" +
				                           "  Message: " +
				                              exception.getMessage( ));
		mFatalErrorCount ++;
	}
	
	@Override
	public void error(SAXParseException exception) 
			throws SAXException {
		System.out.println("**Parsing Non-Fatal Error**\n" + "Line: " +
				exception.getLineNumber( ) + "\n" + "URI: "+
				                              exception.getSystemId( ) + "\n" +
				                           "  Message: " +
				                              exception.getMessage( ));
		mNonFatalErrorCount ++;
	}
	
	@Override
	public void warning(SAXParseException exception)
			throws SAXException{
		System.out.println("**Parsing Warning**\n" + "Line: " +
				exception.getLineNumber( ) + "\n" + "URI: "+
				                              exception.getSystemId( ) + "\n" +
				                           "  Message: " +
				                              exception.getMessage( ));
		mWarningCount ++;
	}
	
	public int getWarningCount() {
		return this.mWarningCount;
	}
	
	public int getFatalErrorCount() {
		return mFatalErrorCount;
	}
	
	public int getNonFatalErrorCount() {
		return this.mNonFatalErrorCount;
	}
	
	private int mWarningCount;
	private int mFatalErrorCount;
	private int mNonFatalErrorCount;
	
}
