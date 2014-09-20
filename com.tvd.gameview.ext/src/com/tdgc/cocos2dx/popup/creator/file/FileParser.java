package com.tdgc.cocos2dx.popup.creator.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileParser {
	
	public FileParser(String pRootPath) {
		this.mRootPath = pRootPath;
		this.mFilePaths = new ArrayList<String>();
		
	}
	
	private void parseFilePaths(String pFilePath) {
		  File file = new File(pFilePath);
		  if((file.isDirectory() || pFilePath.contains(".png"))
				  && !pFilePath.contains("spam")) {
			  String end = pFilePath.substring(
					  pFilePath.lastIndexOf(mRootDirectory)).trim();
			  mFilePaths.add(new String(end));
		  }
		  
		  for(int i = 0 ; file != null && file.isDirectory() && i < file.listFiles().length ; i++) {
			  parseFilePaths(file.listFiles()[i].getAbsolutePath());
		  }
	}
	
	public List<String> getFilePaths(boolean sort) {
		this.mRootDirectory = mRootPath.substring(mRootPath.lastIndexOf("/") + 1);
		parseFilePaths(mRootPath);
		if(sort) {
			Collections.sort(mFilePaths);
		}
		
		return mFilePaths;
	}
	
	public List<String> fetchFilePaths() {
		this.mRootDirectory = mRootPath.substring(mRootPath.lastIndexOf("/") + 1);
		fetchFilePaths(mRootPath);
		Collections.sort(mFilePaths);
		
		return mFilePaths;
	}
	
	private void fetchFilePaths(String pFilePath) {
		  File file = new File(pFilePath);
		  if((file.isDirectory() || pFilePath.contains(".png"))) {
			  String end = pFilePath.substring(
					  pFilePath.lastIndexOf(mRootDirectory)).trim();
			  mFilePaths.add(new String(end));
		  }
		  
		  for(int i = 0 ; file != null && file.isDirectory() && i < file.listFiles().length ; i++) {
			  fetchFilePaths(file.listFiles()[i].getAbsolutePath());
		  }
	}
	
	private String mRootPath;
	private String mRootDirectory;
	private List<String> mFilePaths;
}
