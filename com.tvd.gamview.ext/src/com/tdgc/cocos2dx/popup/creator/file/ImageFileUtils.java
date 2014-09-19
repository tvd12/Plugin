package com.tdgc.cocos2dx.popup.creator.file;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.tdgc.cocos2dx.popup.creator.log.Log;

public class ImageFileUtils {
	public void writeImagesFromTo(String pFromPath, String pToPath, List<String> pImages) {
		BufferedImage image = null;
        try {
//            URL url = new URL("http://www.mkyong.com/image/mypic.jpg");
        	for(int i = 0 ; i < pImages.size() ; i++) {
        		image = ImageIO.read(new File(pFromPath + "/" + pImages.get(i)));
        		
        		File outFile = new File(pToPath + "/" + pImages.get(i));
        		if(!outFile.exists()) {
        			System.out.println(outFile + " does not exist");
        			ImageIO.write(image, "png", outFile);
        		}
        	}
 
        } catch (IOException e) {
        	e.printStackTrace();
        }
        System.out.println("Done");
	}
	
	public void writeImagesFromTo(String pFromPath, String pToPath) {
		if(pFromPath == null || pToPath == null) {
			return;
		}
		File toFile = new File(pToPath);
		if(!toFile.exists()) {
			toFile.mkdirs();
		}
		
		List<String> filePaths = new FileParser(pFromPath).fetchFilePaths();
		pFromPath = pFromPath.substring(0, pFromPath.lastIndexOf('/'));
		BufferedImage image = null;
		String rootPath = pToPath + "/" + filePaths.get(0);
		if(new File(rootPath).exists()) {
			FileUtils.deleteFolder(rootPath);
		}
		for(int i = 0 ; i < filePaths.size() ; i++) {
			File outFile = new File(pToPath + "/" + filePaths.get(i));
			if(filePaths.get(i).contains(".png")) {
				try {
					image = ImageIO.read(new File(pFromPath + "/" + filePaths.get(i)));
	        		if(!outFile.exists()) {
	        			ImageIO.write(image, "png", outFile);
	        		}
				} catch(IOException e) {
					Log.e(e);
				}
			}
			else {
				if(!outFile.exists()) {
					outFile.mkdir();
				}
			}
		}
	}
}
