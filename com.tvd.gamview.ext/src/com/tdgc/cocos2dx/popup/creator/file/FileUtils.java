package com.tdgc.cocos2dx.popup.creator.file;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.tdgc.cocos2dx.popup.creator.log.Log;

public class FileUtils {
	
	public FileUtils() {
		mContent = new String();
	}
	
	public String readFromFile(String pFilePath) {
		FileInputStream fstream = null;
		DataInputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			fstream = new FileInputStream(new File(pFilePath));
			inputStream = new DataInputStream(fstream);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			StringBuilder builder = new StringBuilder();
			String lineContent = bufferedReader.readLine();
			while(lineContent != null) {
				builder.append(lineContent).append("\n");
				lineContent = bufferedReader.readLine();
			}
			mContent = builder.toString();
			fstream.close();
			inputStream.close();
			bufferedReader.close();
		} catch(IOException e) {
			Log.e(e);
		} finally {

		}
		return mContent;
	}

	public void writeToFile(String pFilePath, boolean pCreateCopy) {
		try {
			
		File file = new File(pFilePath);
		
		//if file doesn't exists, then create it
		if(!file.exists()) {
			file.createNewFile();
		} else if(pCreateCopy) {
			int lastIndex = pFilePath.lastIndexOf('/') + 1;
			String newName = pFilePath.substring(lastIndex);
			newName = "copy of " + newName;
			pFilePath = pFilePath.substring(0, lastIndex)
					+ newName;
			file = new File(pFilePath);
		}
		
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(mContent);
		
		bufferedWriter.close();
		} catch(IOException e) {
			Log.e(e);
		}
	}
	
	public void replaceContent(String pFilePath) {
		try {
			
		File file = new File(pFilePath);
		
		//if file doesn't exists, then create it
		if(!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write(mContent);
		
		bufferedWriter.close();
		} catch(IOException e) {
			Log.e(e);
		}
	}
	
	public String fetchTemplate(String pTemplateName, String pTemplateFilePath) {
		StringBuilder builder = new StringBuilder();
		String contentLines[] = readFromFile(pTemplateFilePath).split("\n");
		pTemplateName = "#" + pTemplateName.trim() + " template";
		for(int i = 0 ; i < contentLines.length ; i++) {
			if(contentLines[i].trim().equals(pTemplateName)) {
				while(!contentLines[++i].trim().equals("#end")) {
					if(!contentLines[i].equals("")) {
						builder.append(contentLines[i])
							.append("\n");
					}
				}
				break;
			}
		}
		return builder.toString();
	}
	
	public FileUtils setContent(String pContent) {
		this.mContent = pContent;
		return this;
	}
	
	public void createDirectory(String pRootPath, String pDirectoryName) {
		File directory = new File(pRootPath + "/" + pDirectoryName);
		if(!directory.exists()) {
			directory.mkdir();
		}
	}
	
	public void copyFile(String pSource, String pDestPath, String pDesFileName)
			throws IOException {
	    InputStream input = null;
	    OutputStream output = null;
	    try {
	        input = new FileInputStream(new File(pSource));
	        
	        File des = new File(pDestPath + "/" + pDesFileName);
	        if(des.exists()) {
	        	des = new File(pDestPath + "/copy of " + pDesFileName);
	        }
	        output = new FileOutputStream(des);
	        byte[] buf = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = input.read(buf)) > 0) {
	            output.write(buf, 0, bytesRead);
	        }
	    } finally {
	        input.close();
	        output.close();
	    }
	}
	
	private String mContent;
	
}
