/**
 * Copyright (c) 2014 Dung Ta Van . All rights reserved.
 * 
 * This file is part of com.tvd.gameview.ext.
 * com.tvd.gameview.ext is free eclipse plug-in: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * com.tvd.gameview.ext is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with com.tvd.gameview.ext.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.tvd.cocos2dx.popup.creator.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.tvd.cocos2dx.popup.creator.log.Log;
import com.tvd.cocos2dx.popup.creator.model.Image;
import com.tvd.cocos2dx.popup.creator.model.Label;

public class XibFetcher extends XmlFetcher {
	
	public XibFetcher(List<Image> pImages, List<Label> pLabels) {
		mHandler = new XibFileParser(pImages, pLabels);
	}
	
	@Override
	public void fetchData(String pOutputPath) {
		try {
			File file = new File(pOutputPath);
			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			InputSource inputSource = new InputSource(reader);
			inputSource.setEncoding("UTF-8");
			
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
//			spf.setValidating(true);
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(mHandler);
			xr.setErrorHandler(mErrorHandler);
			xr.parse(inputSource);
			
		} catch (Exception e) {
			Log.e(e);
		}
	}
}
