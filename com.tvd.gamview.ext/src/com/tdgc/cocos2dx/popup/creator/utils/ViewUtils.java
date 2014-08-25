package com.tdgc.cocos2dx.popup.creator.utils;

import java.util.ArrayList;
import java.util.List;

import com.tdgc.cocos2dx.popup.creator.model.ItemGroup;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;

public class ViewUtils {
	
	public static String implementGroups(List<ItemGroup> groups) {
		StringBuilder builder = new StringBuilder();
		for(int i = groups.size() - 1 ; i >= 0 ; i--) {
			if(groups.get(i).isAddToView()) {
				builder.append(groups.get(i).implement(false))
					.append("\n");
			}
		}
		
		return builder.toString();
	}
	
	public static String declareGroups(List<ItemGroup> groups) {
		StringBuilder builder = new StringBuilder();
		for(ItemGroup group : groups) {
			builder.append(group.declare())
				.append("\n");
		}
		
		return builder.toString();
	}
	
	public static String declarePositionGroups(List<ItemGroup> groups) {
		StringBuilder builder = new StringBuilder();
		for(ItemGroup group : groups) {
			builder.append(group.declarePosition());
		}
		
		return builder.toString();
	}
	
	public static String implementPositionGroups(List<ItemGroup> groups) {
		StringBuilder builder = new StringBuilder();
		for(ItemGroup group : groups) {
			builder.append(group.implementPosition());
		}
		
		return builder.toString();
	}
	
	public static String createElementTags(List<ItemGroup> groups, int begin) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < groups.size() ; i++) {
			int tag = (i > 0) ? (groups.size() + i + 2) : (i + 1);
			builder.append(groups.get(i).createElementTags(tag));
		}
		
		return builder.toString();
	}
	
	public static void implementObject(CommonObject obj, StringBuilder builder) {
		if(obj == null) {
			return;
		}
		
		List<List<ItemGroup>> groups = new ArrayList<List<ItemGroup>>();
		groups.add(obj.getSpriteGroups());
		groups.add(obj.getMenuGroups());
		groups.add(obj.getMenuItemGroups());
		groups.add(obj.getLabelGroups()); 
		for(int k = 0 ; k < groups.size() ; k++) {
			List<ItemGroup> group = groups.get(k);
			for(int i = group.size() - 1 ; i >= 0  ; i--) {
				builder.append(group.get(i).implement(false))
					.append("\n");
				if(group.get(i) == null) {
					continue;
				}
				for(int j = group.get(i).getItems().size() - 1 ; 
						 j >= 0 ; j--) {
					implementObject(group.get(i).getItems().get(j), builder);
				}
				group.get(i).setReferenceCount(1);
			}
		}
	}
	
	public static void blockAddingGroupToView(CommonObject obj) {
		if(obj == null) {
			return;
		}
		
		List<List<ItemGroup>> groups = new ArrayList<List<ItemGroup>>();
		groups.add(obj.getSpriteGroups());
		groups.add(obj.getMenuGroups());
		groups.add(obj.getMenuItemGroups());
		groups.add(obj.getLabelGroups()); 
		for(int k = 0 ; k < groups.size() ; k++) {
			List<ItemGroup> group = groups.get(k);
			for(int i = group.size() - 1 ; i >= 0  ; i--) {
				if(group.get(i) == null) {
					continue;
				}
				for(int j = group.get(i).getItems().size() - 1 ; 
						 j >= 0 ; j--) {
					blockAddingGroupToView(group.get(i).getItems().get(j));
				}
				group.get(i).setAddToView(false);
			}
		}
	}
	
}
