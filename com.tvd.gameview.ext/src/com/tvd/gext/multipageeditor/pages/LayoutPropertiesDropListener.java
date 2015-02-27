package com.tvd.gext.multipageeditor.pages;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;

import com.tvd.cocos2dx.popup.creator.model.ItemGroup;
import com.tvd.cocos2dx.popup.creator.model.View;
import com.tvd.cocos2dx.popup.creator.model.basic.CommonObject;

public class LayoutPropertiesDropListener extends ViewerDropAdapter {

	protected LayoutPropertiesDropListener(Viewer pViewer,
			LayoutDetailsPage pFormPage) {
		super(pViewer);
		mFormPage = pFormPage;
	}

	@Override
	public boolean performDrop(Object pData) {
		CommonObject target = (CommonObject)getCurrentTarget();
		LayoutEditorInput input = (LayoutEditorInput)getViewer().getInput();
		if(target == null) {
			target = input.getView();
		}
		View view = input.getView();
		Object[] toDrops = (Object[])pData;
		CommonObject[] objects = new CommonObject[toDrops.length];
		
		for(int i = 0 ; i < toDrops.length ; i++) {
			CommonObject toDrop = view.getObject((String)toDrops[i]);
			objects[i] = toDrop;
			if(toDrop == target) {
				return false;
			}
		}
		
		for(int i = 0 ; i < toDrops.length ; i++) {
			ItemGroup oldGroup = objects[i].getGroup();
			ItemGroup newGroup = target.getGroup();
 			if(oldGroup == newGroup) {
 				changeLocation(target, objects[i]);
 			}
		}
		
		mFormPage.setDirty(true);
		
		return true;
	}

	@Override
	public boolean validateDrop(Object pTarget, int pOperation,
			TransferData pTransferType) {
		return LayoutPropertiesDragTransfer.getInstance()
				.isSupportedType(pTransferType);
	}
	
	private void changeLocation(CommonObject target, CommonObject obj) {
		ItemGroup group = target.getGroup();
		int index = indexByLocation(target);
		int targetIndex = group.getIndex(target);
		int objIndex = group.getIndex(obj);
		
		if(targetIndex > objIndex) {
			index --;
		}
		group.removeItem(obj);
		group.insert(obj, index);
		
		TreeViewer viewer = (TreeViewer)getViewer();
		viewer.remove(obj);
		if(index >= group.numberOfItems()) {
			viewer.add(group, obj);
		}
		else {
			viewer.insert(group, obj, index);
		}
	}
	
	private int indexByLocation(CommonObject target) {
		ItemGroup group = target.getGroup();
		int loc = getCurrentLocation();
		int index = group.getIndex(target);
		if(loc == LOCATION_AFTER 
				&& index < group.numberOfItems()) {
			index ++;
		}
		
		return index;
	}
	
	private LayoutDetailsPage mFormPage;
}