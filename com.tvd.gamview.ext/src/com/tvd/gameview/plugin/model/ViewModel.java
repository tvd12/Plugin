package com.tvd.gameview.plugin.model;

public class ViewModel {
	
	public ViewModel(String name, boolean isDone) {
		this.mName = name;
		this.mIsDone = isDone;
	}
	
	public String getName() {
		String result = mName;
		if(mName.contains("/")) {
			result = mName.substring(mName.lastIndexOf('/') + 1);
		}
		return result;
	}
	
	public String getRealName() {
		return mName;
	}
	
	public boolean isDone() {
		return mIsDone;
	}
	
	@Override
	public String toString() {
		String str = mName;
		if(mIsDone) {
			str += ":done";
		}
		
		return str;
	}

	private String mName;
	private boolean mIsDone;
}
