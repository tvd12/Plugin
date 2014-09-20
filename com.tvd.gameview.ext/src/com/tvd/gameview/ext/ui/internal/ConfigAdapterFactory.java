package com.tvd.gameview.ext.ui.internal;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import com.tvd.gameview.ext.views.BuildingListElement;

public class ConfigAdapterFactory implements IAdapterFactory {

	public ConfigAdapterFactory() {
		System.out.println("construction adapter");
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class[] getAdapterList() {
		return new Class[] {
				IPropertySource.class
		};
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Object o, Class type) {
		if(type == IPropertySource.class && o instanceof BuildingListElement) {
			return new ConfigPropertySource((BuildingListElement)o);
		} else {
			return null;
		}
	}
}
