package com.tdgc.cocos2dx.popup.creator.model.basic;

public class BasicObject {
	
	public BasicObject() {
//		mPositionTemplateName = "CCPoint";
//		mPositionTemplateFile = "point.template";
//		mSizeTemplateName = "CCSizeMake";
//		mSizeTemplateFile = "size.template";
	}
	
	public void setTemplateName(String template) {
		if(template != null && !template.trim().equals("")) {
			this.mTemplateName = template;
		}
	}
	
	public void setTemplateFile(String file) {
		if(file != null && !file.trim().equals("")) {
			this.mTemplateFile = file;
		}
	}
	
	public String getTemplateFile() {
		return mTemplateFile;
	}
	
	public String getTemplateFilePath() {
		return "src/com/template/" + mTemplateFile;
	}
	
	public String getTemplateName() {
		return mTemplateName;
	}
	
	public String getDeclaringTemplateName() {
		return this.mTemplateName + " declaring";
	}
	
	public String getImplementingTemplateName() {
		return this.mTemplateName + " implementing";
	}
	
	public void setPositionTemplateName(String tplName) {
		if(tplName != null && !tplName.trim().equals("")) {
			mPositionTemplateName = tplName;
		}
	}
	
	public String getPositionTemplateName() {
		return mPositionTemplateName;
	}
	
	public String getDeclaringPositionTemplateName() {
		return this.mPositionTemplateName + " declaring";
	}
	
	public String getImplementingPositionTemplateName() {
		return this.mPositionTemplateName + " implementing";
	}
	
	public void setPostionTemplateFile(String tplFile) {
		if(tplFile != null && !tplFile.trim().equals("")) {
			mPositionTemplateFile = tplFile;
		}
	}
	
	public String getPositionTemplateFile() {
		return this.mPositionTemplateFile;
	}
	
	public String getPostionTemplateFilePath() {
		return "src/com/template/" + mPositionTemplateFile;
	}
	
	public void setSizeTemplateName(String tplName) {
		if(tplName != null && !tplName.trim().equals("")) {
			mSizeTemplateName = tplName;
		}
	}
	
	public String getSizeTemplateName() {
		return mSizeTemplateName;
	}
	
	public String getDeclaringSizeTemplateName() {
		return this.mSizeTemplateName + " declaring";
	}
	
	public String getImplementingSizeTemplateName() {
		return this.mSizeTemplateName + " implementing";
	}
	
	public void setSizeTemplateFile(String tplFile) {
		if(tplFile != null && !tplFile.trim().equals("")) {
			mSizeTemplateFile = tplFile;
		}
	}
	
	public String getSizeTemplateFile() {
		return this.mSizeTemplateFile;
	}
	
	public String getSizeTemplateFilePath() {
		return "src/com/template/" + mSizeTemplateFile;
	}

	public void setAllPropertiesForObject(BasicObject obj) {
		obj.mTemplateName = mTemplateName;
		obj.mTemplateFile = mTemplateFile;
		obj.mPositionTemplateName = mPositionTemplateName;
		obj.mPositionTemplateFile = mPositionTemplateFile;
		obj.mSizeTemplateFile = mSizeTemplateFile;
		obj.mSizeTemplateName = mSizeTemplateName;
	}
	
	protected String mTemplateName;
	protected String mTemplateFile;
	protected String mPositionTemplateName;
	protected String mPositionTemplateFile;
	protected String mSizeTemplateName;
	protected String mSizeTemplateFile;
}
