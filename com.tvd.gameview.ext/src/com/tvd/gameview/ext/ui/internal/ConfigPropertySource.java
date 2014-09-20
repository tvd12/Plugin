package com.tvd.gameview.ext.ui.internal;

import java.util.Arrays;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.tdgc.cocos2dx.popup.creator.model.View;
import com.tvd.gameview.ext.views.BuildingListElement;

public class ConfigPropertySource implements IPropertySource {

	public ConfigPropertySource(BuildingListElement element) {
		this.mElement = element;
	}
	
	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor propertyDescriptor[] = new IPropertyDescriptor[] {
				new PropertyDescriptor(NAME, "name"),
				new PropertyDescriptor(DEVICE, "device"),
				new PropertyDescriptor(FILEPATH, "file path"),
				new PropertyDescriptor(PROJECT, "project"),
				new PropertyDescriptor(NUMBER_OF_CHILDS, "number of childs"),
				new PropertyDescriptor(PARENT, "parent"),
				new PropertyDescriptor(VIEW, "view")
			};
		if(mElement.getParent() != null) {
			if(mElement.getParent().getParent() == null) {
				return propertyDescriptor;
			} else {
				return Arrays.copyOf(propertyDescriptor, 
						propertyDescriptor.length - 1);
			}
		}
		
		return Arrays.copyOf(propertyDescriptor, 
				propertyDescriptor.length - 2);
	}

	@Override
	public Object getPropertyValue(Object id) {
		if(id.equals(NAME)) {
			return mElement.getName();
		}
		else if(id.equals(PARENT)) {
			return mElement.getParent();
		}
		else if(id.equals(DEVICE)) {
			return mElement.getDevice();
		}
		else if(id.equals(FILEPATH)) {
			return mElement.getFilePath();
		}
		else if(id.equals(NUMBER_OF_CHILDS)) {
			return mElement.getChilds().size();
		} 
		else if(id.equals(PROJECT)) {
			return mElement.getProject();
		}
		
		View view = mElement.getViewForRootElement();
		if(view != null) {
			return new ViewPropertySource(view);
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		
	}
	
	public static final Object INFO 	= new Object();
	public static final Object NAME 	= new Object();
	public static final Object PARENT	= new Object();
	public static final Object DEVICE	= new Object();
	public static final Object FILEPATH	= new Object();
	public static final Object PROJECT	= new Object();
	public static final Object NUMBER_OF_CHILDS = new Object();
	public static final Object VIEW		= new Object();
	private BuildingListElement mElement;
}
