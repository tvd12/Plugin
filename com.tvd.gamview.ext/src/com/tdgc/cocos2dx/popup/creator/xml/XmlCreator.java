package com.tdgc.cocos2dx.popup.creator.xml;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.Constants;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.file.FileParser;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public class XmlCreator {
	
	public XmlCreator(String pInputPath) {
		this.mInputPath = pInputPath;
	}
	
	public XmlCreator(String pInputPath, String pDevice) {
		this(pInputPath);
		this.mDevice = pDevice;
		this.mInterfaceBuilder = "";
	}
	
	public XmlCreator(String pInputPath, String pDevice, String pInterfaceBuilder) {
		this(pInputPath, pDevice);
		this.mInterfaceBuilder = pInterfaceBuilder;
	}
	
	public Document parseFilePaths() {
		FileParser parser = new FileParser(mInputPath);
		mFilePaths = parser.getFilePaths(true);
		mRootPath = mFilePaths.get(0);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;

		try {
			docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			mDocument = docBuilder.newDocument();
			Element rootElement = createRootElement(mFilePaths.get(0));
			mDocument.appendChild(rootElement);
			
			createBackgroundImageElement(rootElement);
			createNextElements(rootElement, 0, true);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
 
		return mDocument;
	}
	
	private Element createRootElement(String pStr) {
		Element rootElement = mDocument.createElement(Tag.VIEW);
		
		
		rootElement.setAttribute(Attribute.CLASS_NAME, 
				StringUtils.convertToClassName(pStr, "View"));
		rootElement.setAttribute(Attribute.SUPER, "default");
		String type = pStr.substring(pStr.lastIndexOf("_") + 1);
		rootElement.setAttribute(Attribute.PREFIX,
				StringUtils.detectPrefix(type) + "_" +
				pStr.substring(0, pStr.lastIndexOf("_")));
		rootElement.setAttribute(Attribute.TYPE, 
				type);
		
		rootElement.setAttribute(Attribute.COMMENT, "");
		
//		rootElement.setAttribute("xmlns", "http://www.tvd.com/tools" );
//		rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance" );
//		rootElement.setAttribute("xsi:schemaLocation", "http://www.tvd.com/tools ../xsd/view.xsd");
		
		Element positionName = mDocument.createElement(Tag.POSITION_NAME);
		positionName.appendChild(mDocument.createTextNode("default"));
		rootElement.appendChild(positionName);
		
		Element position = mDocument.createElement(Tag.POSITION);
		position.appendChild(mDocument.createTextNode("default"));
		rootElement.appendChild(position);
		
		Element definePath = mDocument.createElement(Tag.DEFINE_PATH);
		definePath.appendChild(mDocument.createTextNode(
				Config.getInstance().getDefinePath()));
		rootElement.appendChild(definePath);
		
		Element parametersPath = mDocument.createElement(Tag.PARAMETERS_PATH);
		parametersPath.appendChild(mDocument.createTextNode(
				Config.getInstance().getParametersPath()));
		rootElement.appendChild(parametersPath);
		
		Element imagesInputPath = mDocument.createElement(Tag.IMAGES_INPUTPATH);
		imagesInputPath.appendChild(mDocument.createTextNode(
				Config.getInstance().getImagesInputPath()));
		rootElement.appendChild(imagesInputPath);
		
		Element imagesPath = mDocument.createElement(Tag.IMAGES_PATH);
		imagesPath.appendChild(mDocument.createTextNode(
				Config.getInstance().getImagesPath()));
		rootElement.appendChild(imagesPath);
		
		Element xibContainerPath = mDocument.createElement(Tag.XIBCONTAINER_PATH);
		xibContainerPath.appendChild(mDocument.createTextNode(
				Config.getInstance().getXibContainerPath() + "/" + mDevice));
		rootElement.appendChild(xibContainerPath);
		
		Element screenContainerPath = mDocument.createElement(Tag.SCREENCONTAINER_PATH);
		screenContainerPath.appendChild(mDocument.createTextNode(
				Config.getInstance().getScreenContainerPath() + "/" + mDevice));
		rootElement.appendChild(screenContainerPath);
		
		Element androidContainerPath = mDocument.createElement(Tag.ANDROIDCONTAINER_PATH);
		androidContainerPath.appendChild(mDocument.createTextNode(
				Config.getInstance().getAndroidContainerPath() + "/" + mDevice));
		rootElement.appendChild(androidContainerPath);
		
		if(mInterfaceBuilder.equals(Constants.XIB)) {
			xibContainerPath.setAttribute("used", "true");
		} else if(mInterfaceBuilder.equals(Constants.SCREEN)) {
			screenContainerPath.setAttribute("used", "true");
		} else if(mInterfaceBuilder.equals(Constants.ANDROID)) {
			androidContainerPath.setAttribute("used", "true");
		}
		
		Element classPath = mDocument.createElement(Tag.CLASS_PATH);
		classPath.appendChild(mDocument.createTextNode(
				Config.getInstance().getClassPath()));
		rootElement.appendChild(classPath);
		
		return rootElement;
	}
	
	private void createBackgroundImageElement(Element pRoot) {
		String backgroundPath = mRootPath + "/background";
		for(int i = 1 ; i < mFilePaths.size() ; i++) {
			String fullPath = mFilePaths.get(i).trim();
			if(fullPath.equals(backgroundPath)) {
				mFilePaths.remove(i);
				Element image = mDocument.createElement("image");
				pRoot.appendChild(image);
				image.setAttribute("id", StringUtils.convertPhonyPathToId(mFilePaths.get(i)));
				image.appendChild(mDocument.createTextNode(createTextNodeWithTabs(
						image, mFilePaths.get(i))));
				mFilePaths.remove(i);
				return;
			}
		}
	}
	
	/**
	 * 
	 * @param pParent
	 * @param pParentPath
	 * @param pSetPosition
	 * 
	 * sprites
	 * 		sprite
	 * 			image
	 * 			sprites
	 */
	private void createSpriteElements(Element pParent, String pParentPath,
			boolean pSetPosition) {
		Element items = createGroupElement(pParent, Tag.SPRITES, pParentPath);
		if(items == null) {
			return;
		}
		for(int i = 0 ; i < mFilePaths.size() ;) {
			String fullPath = mFilePaths.get(i);
			System.out.println("createSpriteElements::fullpath = " + fullPath);
			if(!fullPath.contains(pParentPath)) {
				break;
			}
			String posName = StringUtils.getSpritePostionName(fullPath);
			Element sprite = mDocument.createElement(Tag.SPRITE);
			items.appendChild(sprite);
			sprite.setAttribute(Attribute.VISIBLE, "true");
			sprite.setAttribute(Attribute.BACKGROUND, "false");
			sprite.setAttribute(Attribute.COMMENT, "");
			
			Element positionName = mDocument.createElement(Tag.POSITION_NAME);
			positionName.appendChild(mDocument.createTextNode(posName));
			if(pSetPosition) {
				Element anchorPoint = mDocument.createElement(Tag.ANCHORPOINT);
				anchorPoint.appendChild(mDocument.createTextNode("default"));
				sprite.appendChild(anchorPoint);
				
				Element position = mDocument.createElement(Tag.POSITION);
				position.appendChild(mDocument.createTextNode("0, 0"));
				sprite.appendChild(position);
			} 
			else  {
				positionName.setAttribute(Attribute.UNLOCATED, "true");
			}
			
			sprite.appendChild(positionName);
			
			Element zindex = mDocument.createElement(Tag.Z_INDEX);
			zindex.appendChild(mDocument.createTextNode("0"));
			sprite.appendChild(zindex);
			
			
			if(!fullPath.contains(".png")) {
				createImageTag(sprite, fullPath);
				createNextElements(sprite, i, true);
			} else if(fullPath.contains(".png")) {
				Element image = mDocument.createElement("image");
				sprite.appendChild(image);
				image.setAttribute("id", StringUtils.convertPhonyPathToId(fullPath));
				image.appendChild(mDocument.createTextNode(
						createTextNodeWithTabs(image, fullPath)));
				mFilePaths.remove(i);
				
			}
		}
		createNextElements(pParent, 0, false);
	}
	
	private void createMenuItemElements(Element pParent, String pParentPath) {
		System.out.println("createMenuItemElements::parentpath = " + pParentPath);
		Element items = createGroupElement(pParent, Tag.MENUITEMS, pParentPath);
		for(int i = 0 ; i < mFilePaths.size() ; ) {
			String fullPath = mFilePaths.get(i);
			System.out.println("createMenuItemElements::fullpath = " + fullPath);
			if(!fullPath.contains(pParentPath)) {
				break;
			}
			System.out.println("call from 1");
			Element element = createElement(Tag.MENUITEM, fullPath, items);
			createNextElements(element, i, true, false);
		}
		System.out.println("call from 2");
		createNextElements(pParent, 0, false);
	}
	
	private void createMenuElements(Element pParent, String pParentPath) {
		Element items = createGroupElement(pParent, Tag.MENUS, pParentPath);
		for(int i = 0 ; i < mFilePaths.size() ; ) {
			String fullPath = mFilePaths.get(i);
			System.out.println("createMenuElements::fullpath = " + fullPath);
			if(!fullPath.contains(pParentPath)) {
				break;
			}
			Element element = createElement(Tag.MENU, fullPath, items);
			createNextElements(element, i, true);
		}
		createNextElements(pParent, 0, false);
	}
	
	private void createTableElements(Element pParent, String pParentPath) {
		Element items = createGroupElement(pParent, Tag.TABLES, pParentPath);
		for(int i = 0 ; i < mFilePaths.size() ; ) {
			String fullPath = mFilePaths.get(i);
			System.out.println("createTableElements::fullpath = " + fullPath);
			if(!fullPath.contains(pParentPath)) {
				break;
			}
			
			Element element = createElement(Tag.TABLE, fullPath, items);
			element.setAttribute(Attribute.ROWS, "1");
			element.setAttribute(Attribute.COLUMNS, "1");
			element.setAttribute(Attribute.SIZE, "100, 150");
			element.setAttribute("comment", "");
			mFilePaths.remove(i);
			createCellElements(element, fullPath);
			break;
		}
		createNextElements(pParent, 0, false);
	}
	
	private void createCellElements(Element pParent, String pParentPath) {
		Element element = mDocument.createElement(Tag.CELL);
		element.setAttribute("comment", "");
		pParent.appendChild(element);
		for(int i = 0 ; i < mFilePaths.size() ; ) {
			String fullPath = mFilePaths.get(i);
			System.out.println("createCellElements::fullpath = " + fullPath);
			if(!fullPath.contains(pParentPath)) {
				break;
			}
			createNextElements(element, i, true);
			break;
		}
	}
	
	@SuppressWarnings("unused")
	private void createResourceElement(Element pParent, String pParentPath) {
		Element items = mDocument.createElement(Tag.RESOURCES);
		items.setAttribute(Attribute.NAME, "ItemGroup");
		items.setAttribute(Attribute.COMMENT, "create resources");
		pParent.appendChild(items);
	}
	
	public String getOutputFileName() {
		return mRootPath + ".xml";
	}
	
	private String createTextNodeWithTabs(Node pParent, String pText) {
		StringBuilder builder = new StringBuilder();
		int numberOfParent = 0;
		while(pParent != null) {
			numberOfParent ++;
			pParent = pParent.getParentNode();
		}
		String tabs = StringUtils.tab(numberOfParent - 2);
		builder.append("\n")
			.append(tabs)
			.append("\t" + pText)
			.append("\n")
			.append(tabs);
		
		return builder.toString();
	}
	
	private Element createGroupElement(Element pParent, String pGroupName, String pParentPath) {
		Element group = mDocument.createElement(pGroupName);
		group.setAttribute("array", "false");
		group.setAttribute(Attribute.COMMENT, "create group of elements");
		pParent.appendChild(group);
		return group;
	}
	
	private Element createElement(String pTag, String pFullPath, Element pGroup) {
		Element element = mDocument.createElement(pTag);
		element.setAttribute("comment", "");
		
		String posName = StringUtils.getObjectPositioName(pFullPath);
		Element positionName = mDocument.createElement(Tag.POSITION_NAME);
		positionName.appendChild(mDocument.createTextNode(posName));
		element.appendChild(positionName);
			
		Element anchorPoint = mDocument.createElement(Tag.ANCHORPOINT);
		anchorPoint.appendChild(mDocument.createTextNode("default"));
		element.appendChild(anchorPoint);
			
		Element position = mDocument.createElement(Tag.POSITION);
		position.appendChild(mDocument.createTextNode("0, 0"));
		element.appendChild(position);
		
		Element zindex = mDocument.createElement(Tag.Z_INDEX);
		zindex.appendChild(mDocument.createTextNode("0"));
		element.appendChild(zindex);
		
		pGroup.appendChild(element);
		
		return element;
	}
	
	private boolean createNextElements(Element pParent, int pIndexOfPath, 
			boolean pDeleteParentPath, boolean pSetPosition) {
		if(mFilePaths.size() == 0) {
			return false;
		}
		String parentPath = mFilePaths.get(pIndexOfPath);
		if(pDeleteParentPath) {
			mFilePaths.remove(pIndexOfPath);
		}
		for(int i = 0 ; i < mFilePaths.size() ;) {
			String fullPath = mFilePaths.get(i).trim();
			System.out.println("createNextElements::fullpath = " + fullPath
					+ " " + pDeleteParentPath);
			if(fullPath.equals(parentPath + "/" + Tag.SPRITES)) {
				this.mFilePaths.remove(i);
				createSpriteElements(pParent, fullPath, pSetPosition);
			} 
			else if(fullPath.equals(parentPath + "/" + Tag.TABLES)) {
				this.mFilePaths.remove(i);
				createTableElements(pParent, fullPath);
			} 
			else if(fullPath.equals(parentPath + "/" + Tag.MENUS)) {
				this.mFilePaths.remove(i);
				createMenuElements(pParent, fullPath);
			} 
			else if(fullPath.equals(parentPath + "/" + Tag.MENUITEMS)) {
				this.mFilePaths.remove(i);
				createMenuItemElements(pParent, fullPath);
			}
			else {
				i++;
			}
		}
		return true;
	}
	
	protected boolean createNextElements(Element pParent, int pIndexOfPath, 
			boolean pDeleteParentPath) {
		return createNextElements(pParent, pIndexOfPath, pDeleteParentPath, true);
	}
	
	protected void createImageTag(Element parent, String parentPath) {
		for(int i = 1 ; i < mFilePaths.size() ; i++) {
			String fullPath = mFilePaths.get(i);
			int lastIndexOfParentPath = fullPath.indexOf(parentPath) 
					+ parentPath.length() + 1;
			String imageName = fullPath.substring(lastIndexOfParentPath);
			System.out.println("imageName = " + imageName);
			if(!imageName.contains("/") && imageName.endsWith(".png")) {
				Element image = mDocument.createElement("image");
				parent.appendChild(image);
				image.setAttribute("id", StringUtils.convertPhonyPathToId(fullPath));
				image.appendChild(mDocument.createTextNode(createTextNodeWithTabs(image, fullPath)));
				mFilePaths.remove(i);
				
				return;
			}
		}
	}
	
	@SuppressWarnings("unused")
	private String checkGroup(String fullPath) {
		if(fullPath.endsWith(Tag.SPRITES)) {
			return Tag.SPRITES;
		} else if(fullPath.endsWith(Tag.TABLES)) {
			return Tag.TABLES;
		}
		
		return null;
	}
	
	private List<String> mFilePaths;
	private Document mDocument;
	private String mInputPath;
	private String mDevice;
	private String mInterfaceBuilder;
	private String mRootPath;
}






























//version 1.0
//package com.tdgc.cocos2dx.popup.creator.xml;
//
//import java.util.List;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//
//import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
//import com.tdgc.cocos2dx.popup.creator.constants.Constants;
//import com.tdgc.cocos2dx.popup.creator.constants.Tag;
//import com.tdgc.cocos2dx.popup.creator.file.FileParser;
//import com.tdgc.cocos2dx.popup.creator.global.Config;
//import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;
//
//public class XmlCreator {
//	
//	public XmlCreator(String pInputPath) {
//		this.mInputPath = pInputPath;
//	}
//	
//	public XmlCreator(String pInputPath, String pDevice) {
//		this(pInputPath);
//		this.mDevice = pDevice;
//		this.mInterfaceBuilder = "";
//	}
//	
//	public XmlCreator(String pInputPath, String pDevice, String pInterfaceBuilder) {
//		this(pInputPath, pDevice);
//		this.mInterfaceBuilder = pInterfaceBuilder;
//	}
//	
//	public Document parseFilePaths() {
//		FileParser parser = new FileParser(mInputPath);
//		mFilePaths = parser.getFilePaths();
//		
//		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder docBuilder;
//
//		try {
//			docBuilder = docFactory.newDocumentBuilder();
//			
//			// root elements
//			mDocument = docBuilder.newDocument();
//			Element rootElement = createRootElement(mFilePaths.get(0));
//			mDocument.appendChild(rootElement);
//			
//			createBackgroundImageElement(rootElement);
//			
//			createSpriteElements(rootElement, mFilePaths.get(0), true);
//			createMenuItemElements(rootElement, mFilePaths.get(0));
//			createTableElements(rootElement, mFilePaths.get(0));
//			createMenuElements(rootElement, mFilePaths.get(0));
//			
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		}
// 
//		return mDocument;
//	}
//	
//	private Element createRootElement(String pStr) {
//		Element rootElement = mDocument.createElement(Tag.VIEW);
//		
//		boolean isContainUnderscore = pStr.contains("_");
//		
//		rootElement.setAttribute(Attribute.CLASS_NAME, 
//				StringUtils.convertToClassName(pStr, "View"));
//		rootElement.setAttribute(Attribute.SUPER, "default");
//		String type = (isContainUnderscore) 
//				? pStr.substring(pStr.lastIndexOf("_") + 1)
//				: "";
//		rootElement.setAttribute(Attribute.PREFIX,
//				(isContainUnderscore)
//				? StringUtils.detectPrefix(type) + "_" +
//					pStr.substring(0, pStr.lastIndexOf("_"))
//				: "");
//		rootElement.setAttribute(Attribute.TYPE, 
//				type);
//		
//		rootElement.setAttribute(Attribute.COMMENT, "");
////		rootElement.setAttribute("xmlns", "http://www.tvd.com/tools" );
////		rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance" );
////		rootElement.setAttribute("xsi:schemaLocation", "http://www.tvd.com/tools ../xsd/view.xsd");
//		
//		Element positionName = mDocument.createElement(Tag.POSITION_NAME);
//		positionName.appendChild(mDocument.createTextNode("default"));
//		rootElement.appendChild(positionName);
//		
//		Element position = mDocument.createElement(Tag.POSITION);
//		position.appendChild(mDocument.createTextNode("default"));
//		rootElement.appendChild(position);
//		
//		Element definePath = mDocument.createElement(Tag.DEFINE_PATH);
//		definePath.appendChild(mDocument.createTextNode(
//				Config.getInstance().getDefinePath()));
//		rootElement.appendChild(definePath);
//		
//		Element parametersPath = mDocument.createElement(Tag.PARAMETERS_PATH);
//		parametersPath.appendChild(mDocument.createTextNode(
//				Config.getInstance().getParametersPath()));
//		rootElement.appendChild(parametersPath);
//		
//		Element imagesInputPath = mDocument.createElement(Tag.IMAGES_INPUTPATH);
//		imagesInputPath.appendChild(mDocument.createTextNode(
//				Config.getInstance().getImagesInputPath()));
//		rootElement.appendChild(imagesInputPath);
//		
//		Element imagesPath = mDocument.createElement(Tag.IMAGES_PATH);
//		imagesPath.appendChild(mDocument.createTextNode(
//				Config.getInstance().getImagesPath() + "/" + mDevice));
//		rootElement.appendChild(imagesPath);
//		
//		Element xibContainerPath = mDocument.createElement(Tag.XIBCONTAINER_PATH);
//		xibContainerPath.appendChild(mDocument.createTextNode(
//				Config.getInstance().getXibContainerPath() + "/" + mDevice));
//		rootElement.appendChild(xibContainerPath);
//		
//		Element screenContainerPath = mDocument.createElement(Tag.SCREENCONTAINER_PATH);
//		screenContainerPath.appendChild(mDocument.createTextNode(
//				Config.getInstance().getScreenContainerPath() + "/" + mDevice));
//		rootElement.appendChild(screenContainerPath);
//		
//		Element androidContainerPath = mDocument.createElement(Tag.ANDROIDCONTAINER_PATH);
//		androidContainerPath.appendChild(mDocument.createTextNode(
//				Config.getInstance().getAndroidContainerPath() + "/" + mDevice));
//		rootElement.appendChild(androidContainerPath);
//		
//		if(mInterfaceBuilder.equals(Constants.XIB)) {
//			xibContainerPath.setAttribute("used", "true");
//		} else if(mInterfaceBuilder.equals(Constants.SCREEN)) {
//			screenContainerPath.setAttribute("used", "true");
//		} else if(mInterfaceBuilder.equals(Constants.ANDROID)) {
//			androidContainerPath.setAttribute("used", "true");
//		}
//		
//		Element classPath = mDocument.createElement(Tag.CLASS_PATH);
//		classPath.appendChild(mDocument.createTextNode(
//				Config.getInstance().getClassPath()));
//		rootElement.appendChild(classPath);
//		
//		return rootElement;
//	}
//	
//	private void createBackgroundImageElement(Element pRoot) {
//		String rootPath = mFilePaths.get(0);
//		String backgroundPath = rootPath + "/background";
//		int i = 0;
//		boolean contain = false;
//		for( ; i < mFilePaths.size() ; i++) {
//			if(mFilePaths.get(i).contains(backgroundPath)) {
//				mFilePaths.remove(i);
//				contain = true;
//				break;
//			}
//		}
//		if(contain) {
//			Element imageElement = mDocument.createElement(Tag.IMAGE);
//			imageElement.setAttribute(Attribute.BACKGROUND, "true");
//			imageElement.appendChild(mDocument.createTextNode(mFilePaths.get(i)));
//			pRoot.appendChild(imageElement);
//			mFilePaths.remove(i);
//		}
//	}
//	
//	private void createSpriteElements(Element pParent, String pParentPath,
//			boolean pSetPosition) {
//		Element items = createGroupElement(pParent, Tag.SPRITES, pParentPath);
//		if(items == null) {
//			return;
//		}
//		String path = pParentPath + "/" + Tag.SPRITES;
//		
//		for(int i = 1 ; i < mFilePaths.size() ; i++) {
//			String fullPath = mFilePaths.get(i);
//			if(fullPath.equals(path)
//					|| !fullPath.contains(path)) {
//				continue;
//			}
//			if(fullPath.equals(path + "/" + Tag.RESOURCES)) {
//				createResourceElement(items, fullPath);
//				break;
//			}
//			String posName = StringUtils.getSpritePostionName(fullPath);
//			
//			if(posName.equals(Tag.SPRITES)) {
//				continue;
//			}
//			
//			Element sprite = mDocument.createElement(Tag.SPRITE);
//			sprite.setAttribute(Attribute.VISIBLE, "true");
//			sprite.setAttribute(Attribute.BACKGROUND, "false");
//			sprite.setAttribute(Attribute.COMMENT, "");
//			
//			Element positionName = mDocument.createElement(Tag.POSITION_NAME);
//			positionName.appendChild(mDocument.createTextNode(posName));
//			if(pSetPosition) {
//				Element anchorPoint = mDocument.createElement(Tag.ANCHORPOINT);
//				anchorPoint.appendChild(mDocument.createTextNode("default"));
//				sprite.appendChild(anchorPoint);
//				
//				Element position = mDocument.createElement(Tag.POSITION);
//				position.appendChild(mDocument.createTextNode("0, 0"));
//				sprite.appendChild(position);
//			} 
//			else  {
//				positionName.setAttribute(Attribute.UNLOCATED, "true");
//			}
//			
//			sprite.appendChild(positionName);
//			
//			Element zindex = mDocument.createElement(Tag.Z_INDEX);
//			zindex.appendChild(mDocument.createTextNode("0"));
//			sprite.appendChild(zindex);
//			
//			items.appendChild(sprite);
//			if(!fullPath.contains(".png")) {
//				mFilePaths.remove(i--);
//				createSpriteElements(sprite, fullPath, true);
//				continue;
//			}
//			
//			else if(fullPath.contains(".png")) {
//				mFilePaths.remove(i--);
//				Element image = mDocument.createElement("image");
//				image.setAttribute("id", StringUtils.convertPhonyPathToId(fullPath));
//				image.appendChild(mDocument.createTextNode(createTextNodeWithTabs(fullPath)));
//				sprite.appendChild(image);
//			}
//		}
//	}
//	
//	private void createMenuItemElements(Element pParent, String pParentPath) {
//		Element items = createGroupElement(pParent, Tag.MENUITEMS, pParentPath);
//		if(items == null) {
//			return;
//		}
//		String path = pParentPath + "/" + Tag.MENUITEMS;
//		
//		for(int i = 1 ; i < mFilePaths.size() ; i++) {
//			String fullPath = mFilePaths.get(i);
//			if(fullPath.equals(path)
//					|| !fullPath.contains(path)) {
//				continue;
//			}
//			
//			Element element = createElement(Tag.MENUITEM, fullPath, items);
//
//			mFilePaths.remove(i--);
//			createSpriteElements(element, fullPath, false);
//		}
//	}
//	
//	private void createMenuElements(Element pParent, String pParentPath) {
//		Element items = createGroupElement(pParent, Tag.MENUS, pParentPath);
//		if(items == null) {
//			return;
//		}
//		String path = pParentPath + "/" + Tag.MENUS;
//		
//		for(int i = 1 ; i < mFilePaths.size() ; i++) {
//			String fullPath = mFilePaths.get(i);
//			if(fullPath.equals(path)
//					|| !fullPath.contains(path)) {
//				continue;
//			}
//			Element element = createElement(Tag.MENU, fullPath, items);
//			
//			mFilePaths.remove(i--);
//			createMenuItemElements(element, fullPath);
//		}
//	}
//	
//	private void createTableElements(Element pParent, String pParentPath) {
//		Element items = createGroupElement(pParent, Tag.TABLES, pParentPath);
//		if(items == null) {
//			return;
//		}
//		String path = pParentPath + "/" + Tag.TABLES;
//		
//		for(int i = 1 ; i < mFilePaths.size() ; i++) {
//			String fullPath = mFilePaths.get(i);
//			if(fullPath.equals(path)
//					|| !fullPath.contains(path)
//					|| fullPath.contains(Tag.SPRITES)
//					|| fullPath.contains(Tag.MENUITEMS)) {
//				continue;
//			}
//			
//			Element element = createElement(Tag.TABLE, fullPath, items);
//			element.setAttribute(Attribute.ROWS, "1");
//			element.setAttribute(Attribute.COLUMNS, "1");
//			element.setAttribute(Attribute.SIZE, "100, 150");
//			element.setAttribute("comment", "");
//			
//			createCellElements(pParent, pParentPath);
//			break;
//		}
//	}
//	
//	private void createCellElements(Element pParent, String pParentPath) {
//		Element items = createGroupElement(pParent, Tag.TABLES, pParentPath);
//		if(items == null) {
//			return;
//		}
//		String path = pParentPath + "/" + Tag.CELL;
//		
//		for(int i = 1 ; i < mFilePaths.size() ; i++) {
//			String fullPath = mFilePaths.get(i);
//			if(fullPath.equals(path)
//					|| !fullPath.contains(path)
//					|| !fullPath.contains(Tag.CELL)) {
//				continue;
//			}
//			
//			Element element = createElement(Tag.CELL, fullPath, items);
//			element.setAttribute("comment", "");
//			
//			createSpriteElements(element, fullPath, true);
//			break;
//		}
//	}
//	
//	private void createResourceElement(Element pParent, String pParentPath) {
//		Element items = mDocument.createElement(Tag.RESOURCES);
//		items.setAttribute(Attribute.NAME, "ItemGroup");
//		items.setAttribute(Attribute.COMMENT, "create resources");
//		pParent.appendChild(items);
//		createSpriteElements(items, pParentPath, true);
//		//createMenuItemElements(items, pParentPath);
//		createMenuElements(items, pParentPath);
//	}
//	
//	public String getOutputFileName() {
//		return mFilePaths.get(0) + ".xml";
//	}
//	
//	private boolean hasPath(String pPath) {
//		for(int i = 0 ; i < mFilePaths.size() ; i++) {
//			if(mFilePaths.get(i).equals(pPath)) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	private String createTextNodeWithTabs(String pText) {
//		StringBuilder builder = new StringBuilder();
//		String tabs = StringUtils.tab(StringUtils.numberOfLetter(pText, '/') + 1);
//		builder.append("\n")
//			.append(tabs + "\t")
//			.append(pText)
//			.append("\n")
//			.append(tabs);
//		
//		return builder.toString();
//	}
//	
//	private Element createGroupElement(Element pParent, String pGroupName, String pParentPath) {
//		String path = pParentPath + "/" + pGroupName;
//		
//		if(!hasPath(path)) {
//			return null;
//		}
//		
//		Element group = mDocument.createElement(pGroupName);
//		group.setAttribute("array", "false");
//		group.setAttribute(Attribute.COMMENT, "create group of elements");
//		pParent.appendChild(group);
//		
//		mFilePaths.remove(path);
//		
//		return group;
//	}
//	
//	private Element createElement(String pTag, String pFullPath, Element pGroup) {
//		Element element = mDocument.createElement(pTag);
//		element.setAttribute("comment", "");
//		
//		String posName = StringUtils.getObjectPositioName(pFullPath);
//		Element positionName = mDocument.createElement(Tag.POSITION_NAME);
//		positionName.appendChild(mDocument.createTextNode(posName));
//		element.appendChild(positionName);
//			
//		Element anchorPoint = mDocument.createElement(Tag.ANCHORPOINT);
//		anchorPoint.appendChild(mDocument.createTextNode("default"));
//		element.appendChild(anchorPoint);
//			
//		Element position = mDocument.createElement(Tag.POSITION);
//		position.appendChild(mDocument.createTextNode("0, 0"));
//		element.appendChild(position);
//		
//		Element zindex = mDocument.createElement(Tag.Z_INDEX);
//		zindex.appendChild(mDocument.createTextNode("0"));
//		element.appendChild(zindex);
//		
//		pGroup.appendChild(element);
//		
//		return element;
//	}
//	
//	public void setDevice(String pDevice) {
//		this.mDevice = pDevice;
//	}
//	
//	public String getDevice() {
//		return mDevice;
//	}
//	
//	private List<String> mFilePaths;
//	private Document mDocument;
//	private String mInputPath;
//	private String mDevice;
//	private String mInterfaceBuilder;
//}