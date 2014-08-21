package com.tdgc.cocos2dx.popup.creator.global;

import java.util.HashMap;
import java.util.Map;

import com.tdgc.cocos2dx.popup.creator.constants.Constants;

public class Config {
	
	private Config() {
		mNormalPropertyPrefix = "m";
		mDefaultSupers = new HashMap<String, String>();
		mProjectName = "DragonVideoPoker";
		mDefaultSupers.put("popup", "PopUpLayer");
		mDefaultSupers.put("cell", "ITableCellView");
		mDefaultSupers.put("table", "ITableViewBuilder");
	}

	public static Config getInstance() {
		if(sInstance == null) {
			synchronized (Config.class) {
				if(sInstance == null) {
					sInstance = new Config();
				}
			}
		}
		
		return sInstance;
	}
	
	public String getDefautSuper(String pClassName) {
		return mDefaultSupers.get(pClassName);
	}
	
	public String getDefaultParentPopup() {
		return "mBackgroundSprite";
	}
	
	public String getProjectName() {
		return mProjectName;
	}

	public void setProjectName(String mProjectName) {
		this.mProjectName = mProjectName;
	}

	public String getNormalPropertyPrefix() {
		return mNormalPropertyPrefix;
	}

	public void setNormalPropertyPrefix(String mNormalPropertyPrefix) {
		this.mNormalPropertyPrefix = mNormalPropertyPrefix;
	}

	public Map<String, String> getDefaultSupers() {
		return mDefaultSupers;
	}

	public void setDefaultSupers(Map<String, String> mDefaultSupers) {
		this.mDefaultSupers = mDefaultSupers;
	}

	public String getPrefix() {
		return mPrefix;
	}

	public void setPrefix(String mPrefix) {
		this.mPrefix = mPrefix;
	}

	public String getDefinePath() {
		return mDefinePath;
	}

	public void setDefinePath(String mDefinePath) {
		this.mDefinePath = mDefinePath;
	}

	public String getParametersPath() {
		return mParametersPath;
	}

	public void setParametersPath(String mParametersPath) {
		this.mParametersPath = mParametersPath;
	}

	public String getImagesInputPath() {
		return mImagesInputPath;
	}

	public void setImagesInputPath(String mImagesInputPath) {
		this.mImagesInputPath = mImagesInputPath;
	}

	public String getImagesPath() {
		return mImagesPath;
	}

	public void setImagesPath(String mImagesPath) {
		this.mImagesPath = mImagesPath;
	}

	public String getXibContainerPath() {
		return mXibContainerPath;
	}

	public void setXibContainerPath(String mXibContainerPath) {
		this.mXibContainerPath = mXibContainerPath;
	}

	public String getClassPath() {
		return mClassPath;
	}

	public void setClassPath(String mClassPath) {
		this.mClassPath = mClassPath;
	}

	public String getDefaultMenuItemParent() {
		return mDefaultMenuItemParent;
	}

	public void setDefaultMenuItemParent(String mDefaultMenuItemParent) {
		this.mDefaultMenuItemParent = mDefaultMenuItemParent;
	}

	public String getScreenContainerPath() {
		return mScreenContainerPath;
	}

	public void setScreenContainerPath(String mScreenContainerPath) {
		this.mScreenContainerPath = mScreenContainerPath;
	}

	public void setAndroidContainerPath(String path) {
		this.mAndroidContainerPath = path;
	}
	
	public String getAndroidContainerPath() {
		return this.mAndroidContainerPath;
	}
	
	public String getTableViewName() {
		return "TableView";
	}
	
	public int getProgrammingType() {
		return Constants.PLUGIN;
	}

	private String mProjectName;
	private String mNormalPropertyPrefix;
	private Map<String, String> mDefaultSupers;
	private String mPrefix;
	private String mDefinePath;
	private String mParametersPath;
	private String mImagesInputPath;
	private String mImagesPath;
	private String mXibContainerPath;
	private String mClassPath;
	private String mDefaultMenuItemParent;
	private String mScreenContainerPath;
	private String mAndroidContainerPath;
	
	private static Config sInstance;
	
}
