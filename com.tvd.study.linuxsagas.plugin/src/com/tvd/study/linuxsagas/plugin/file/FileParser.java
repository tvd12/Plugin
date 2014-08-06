package com.tvd.study.linuxsagas.plugin.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileParser {
	
	public FileParser() {
		this.mFilePaths = new ArrayList<String>();
		
	}
	
	public List<String> getFilePaths(String pRootPath, String pRootDirectory) {
		this.mRootPath = pRootPath;
		this.mRootDirectory = pRootDirectory;
		
		parseFilePaths(mRootPath + "/" + mRootDirectory);
		Collections.sort(mFilePaths);
		
		return mFilePaths;
	}
	
	private void parseFilePaths(String pFilePath) {
		  File file = new File(pFilePath);
		  if(file != null) {
			  String end = pFilePath.substring(
					  pFilePath.lastIndexOf(mRootDirectory)
					  + mRootDirectory.length()).trim();
			  if(end != null && !end.equals("") && !end.equals("/")) {
				  mFilePaths.add(new String(end));
			  }
		  }
		  
		  for(int i = 0 ; file != null && file.isDirectory() && i < file.listFiles().length ; i++) {
			  parseFilePaths(file.listFiles()[i].getAbsolutePath());
		  }
	}
	
	private String mRootPath;
	private String mRootDirectory;
	private List<String> mFilePaths;
}
