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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.json.JSONObject;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.log.Log;
import com.tdgc.cocos2dx.popup.creator.model.Image;

public class FileUtils {
	
	public FileUtils() {
		mContent = new String();
	}
	
	public String readFromFile(String pFilePath) {
		FileInputStream fstream = null;
		DataInputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			File file = new File(pFilePath);
			if(!file.exists()) {
				return null;
			}
			fstream = new FileInputStream(file);
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
	
	public String readFromFile(IFile file) {
		DataInputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			if(!file.exists()) {
				System.err.println("ERROR::readFromFile file " 
						+ file.getFullPath() + " is not exists");
				return "";
			}
			inputStream = new DataInputStream(file.getContents());
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			StringBuilder builder = new StringBuilder();
			String lineContent = bufferedReader.readLine();
			while(lineContent != null) {
				builder.append(lineContent).append("\n");
				lineContent = bufferedReader.readLine();
			}
			mContent = builder.toString();
			inputStream.close();
			bufferedReader.close();
		} catch(IOException e) {
			Log.e(e);
		} catch(CoreException e) {
			Log.e(e);
		}
		finally {

		}
		return mContent;
	}

	public void writeToFile(String pFilePath, boolean pCreateCopy) {
		try {
			String folderPath = pFilePath.substring(0, pFilePath.lastIndexOf('/'));
			File container = new File(folderPath);
			if(!container.exists()) {
				container.mkdirs();
			}
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
			String folderPath = pFilePath.substring(0, pFilePath.lastIndexOf('/'));
			File container = new File(folderPath);
			if(!container.exists()) {
				container.mkdirs();
			}
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
		boolean isNotExistsTemplate = true;
		for(int i = 0 ; i < contentLines.length ; i++) {
			isNotExistsTemplate = false;
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
		if(isNotExistsTemplate) {
			System.err.println("ERROR::fetchTemplate template '" 
					+ pTemplateName + "' is not exists!");
		}
		return builder.toString();
	}
	
	public String fetchDefaultValue(String pKey, String pGroupName, 
			String pFileContent) {
		String contentLines[] = pFileContent.split("\n");
		String groupRealName = "#" + pGroupName + " default";
		for(int i = 0 ; i < contentLines.length ; i++) {
			contentLines[i] = contentLines[i].trim();
			if(contentLines[i].equals(groupRealName)) {
				while(!contentLines[++i].trim().equals("#end")) {
					contentLines[i] = contentLines[i].trim();
					if(contentLines[i].length() > 0 
							&& contentLines[i].contains("=")) {
						String keyValue[] = contentLines[i].split("=");
						String key = keyValue[0].trim();
						String value = keyValue[1].trim();
						if(pKey.equals(key)) {
							return value;
						}
					}
				}
				break;
			}
		}
		
		System.err.println("ERROR::fetchTemplate defaultValue key '" 
				+ pKey + "' or group '" + pGroupName + "' is not exists!");
		return "default";
	}
	
	public Map<String, Image> fetchDefaultBackgroundImages(String pGroupName,
			String pFileContent) {
		Map<String, Image> result = new HashMap<String, Image>();
		Map<String, String> pairs = fetchDefaultKeyValues(pGroupName, pFileContent);
		String keys[] = pairs.keySet().toArray(new String[0]);
		String values[] = pairs.values().toArray(new String[0]);
		for(int i = 0 ; i < pairs.size() ; i++) {
			JSONObject jsonObj = new JSONObject(values[i]);
			String realPath = jsonObj.getString(Attribute.REAL_PATH);
			Image image = new Image();
			image.setPhonyPath(realPath);
			result.put(keys[0], image);
			System.out.println(image);
		}
		
		return result;
	}
	
	public Map<String, String> fetchDefaultKeyValues(String pGroupName, 
			String pFileContent) {
		Map<String, String> result = new HashMap<String, String>();
		String contentLines[] = pFileContent.split("\n");
		String groupRealName = "#" + pGroupName + " default";
		for(int i = 0 ; i < contentLines.length ; i++) {
			contentLines[i] = contentLines[i].trim();
			if(contentLines[i].equals(groupRealName)) {
				while(!contentLines[++i].trim().equals("#end")) {
					contentLines[i] = contentLines[i].trim();
					if(contentLines[i].length() > 0 
							&& contentLines[i].contains("=")) {
						String keyValue[] = contentLines[i].split("=");
						String key = keyValue[0].trim();
						String value = keyValue[1].trim();
						if(key.length() > 0 && value.length() > 0) {
							result.put(key, value);
						}
					}
				}
				break;
			}
		}
		
		return result;
	}
	
	public String fetchTemplate(String pTemplateName, String pTemplatePath, 
			IProject pProject) {
		StringBuilder builder = new StringBuilder();
		IFile file = pProject.getFile(pTemplatePath);
		String contentLines[] = readFromFile(file).split("\n");
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

	public String findSourceCode(String fileContent, String beginStr, String endStr) {
		String strs[] = fileContent.split("\n");
		int lineNumberEndStr = -1;
		int lineNumberOfBeginStr = -1;
		
		for(int i = 0 ; i < strs.length ; i++) {
			String str = strs[i].trim();
			if(str.equals(beginStr)) {
				lineNumberOfBeginStr = i;
			}
			if(str.equals(endStr)) {
				lineNumberEndStr = i;
			}
			
			if(lineNumberOfBeginStr != -1 && lineNumberEndStr != -1) {
				StringBuilder result = new StringBuilder();
				for(int k = lineNumberOfBeginStr ; k < lineNumberEndStr ; k++) {
					result.append(strs[k]);
					if(k < lineNumberEndStr) {
						result.append("\n");
					}
				}
				return result.toString();
				
			}
		}
		
		return null;
	}
	
	public String deleteSourceCode(String fileContent, String beginStr, String endStr) {
		String srcCode = findSourceCode(fileContent, beginStr, endStr);
		if(srcCode != null) {
			fileContent = fileContent.replace(srcCode, "");
		}
		
		return fileContent;
	}
	
	private String mContent;
	
}
