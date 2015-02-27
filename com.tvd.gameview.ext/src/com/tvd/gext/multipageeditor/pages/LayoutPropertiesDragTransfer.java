package com.tvd.gext.multipageeditor.pages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import com.tvd.cocos2dx.popup.creator.model.Identifier;
import com.tvd.cocos2dx.popup.creator.model.ItemGroup;
import com.tvd.cocos2dx.popup.creator.model.basic.CommonObject;

public class LayoutPropertiesDragTransfer extends ByteArrayTransfer {
	
	private LayoutPropertiesDragTransfer() {
		
	}

	public static LayoutPropertiesDragTransfer getInstance() {
		if(sInstance == null) {
			synchronized (LayoutPropertiesDragTransfer.class) {
				if(sInstance == null) {
					sInstance = new LayoutPropertiesDragTransfer();
				}
			}
		}
		
		return sInstance;
	}
	
	@Override
	protected int[] getTypeIds() {
		return new int[] {
			registerType(TYPE_NAME),
		};
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] {
				TYPE_NAME,
		};
	}
	
	@Override
	protected void javaToNative(Object object, 
			TransferData transferData) {
		byte[] bytes = toByteArray((Object[])object);
		if(bytes != null) {
			super.javaToNative(bytes, transferData);
		}
	}
	
	@Override
	protected Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[])super.nativeToJava(transferData);
		return fromByteArray(bytes);
	}
	
	protected byte[] toByteArray(Object[] objects) {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteOut);
		
		byte[] bytes = null;
		try {
			out.writeInt(objects.length);
			
			for(int i = 0 ; i < objects.length ; i++) {
				writeObject(objects[i], out);
			}
			out.close();
			bytes = byteOut.toByteArray();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return bytes;
	}
	
	private void writeObject(Object object, DataOutputStream dataOut)
			throws IOException {
		
		Object[] children = null;
		
		if(object instanceof ItemGroup) {
			ItemGroup group = (ItemGroup)object;
			children = group.getItems().toArray();
		}
		else if(object instanceof CommonObject) {
			CommonObject cobj = (CommonObject)object;
			children =  cobj.getAllGroups().toArray();
		}
		
		if(object instanceof Identifier) {
			Identifier id = (Identifier)object;
			dataOut.writeUTF(id.getId());
			System.out.println(id);
		}
		
		if(children != null) {
			dataOut.writeInt(children.length);
			for(int i = 0 ; i < children.length ; i++) {
				writeObject(children[i], dataOut);
			}
		}
	}
	
	private Object readObject(Object obj, DataInputStream dataIn) 
			throws IOException {
		String id = dataIn.readUTF();
		int n = dataIn.readInt();
		
		for(int i = 0 ; i < n ; i ++) {
			readObject(obj, dataIn);
		}
		
		return id;
	}
	
	protected Object[] fromByteArray(byte[] bytes) {
		DataInputStream in = new DataInputStream(
				new ByteArrayInputStream(bytes));
		try {
			int n = in.readInt();
			Object[] objects = new Object[n];
			for(int i = 0 ; i < n ; i ++) {
				Object object = readObject(null, in);
				if(object == null) {
					return null;
				}
				objects[i] = object;
			}
			return objects;
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static LayoutPropertiesDragTransfer sInstance;
	
	private static final String TYPE_NAME 
			= "exporting-tree-transfer-format";
}