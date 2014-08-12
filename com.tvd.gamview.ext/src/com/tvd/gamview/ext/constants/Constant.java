package com.tvd.gamview.ext.constants;

public interface Constant {

	public static final String SOURCE_CODE_DIRECTORY 		= "source-code";
	public static final String GLOBAL_PROJECT_CONFIG_FILE 	= ".tdgproject";
	
	public static interface Name {
		public static final String LOCATION				= "Location";
		
		public static final String DEFINE_PATH 			= "Define path";
		public static final String PARAMETERS_PATH 		= "Paramters path";
		public static final String IMAGES_INPUT_PATH 	= "Input images path";
		public static final String IMAGES_PATH			= "Images path";
		public static final String XIBCONTAINER_PATH	= "Xib container path";
		public static final String SCREEN_CONTAINER_PATH			= "Screen container path";
		public static final String CLASS_PATH			= "Class path";
	}
	
	public static interface Id {
		public static final String LOCATION				= "location";
		public static final String DEFINE_PATH 			= "define-path";
		public static final String PARAMETERS_PATH		 = "parameters-path";
		public static final String IMAGES_INPUT_PATH 	= "input_images_path";
		public static final String IMAGES_PATH			= "images_path";
		public static final String XIBCONTAINER_PATH	= "xib_container_path";
		public static final String SCREEN_CONTAINER_PATH			= "screen_container_path";
		public static final String CLASS_PATH			= "class_path";
	}
	
	public static interface File {
		public static final String GLOBAL				= "global.properties";
	}
	
	public static interface Folder {
		public static final String PROPERTIES			= "src/com/properties";
	}
	
	public static interface TreeElement {
		public static final String EXPORT_IMAGES 		= "export images";
		public static final String EXPORT_XIB_TPL		= "export xib template";
		public static final String EXPORT_SCREEN_TPL	= "export screen template";
		public static final String EXPORT_ANDROID_TPL	= "export android template";
		public static final String EXPORT_POSITIONS		= "export positions";
		public static final String EXPORT_SRC_CODE		= "export source code";
		public static final String EXPORT_IDS			= "export identifiers";
		
		public static final String DECLARE_POSITIONS	= "declare positions";
		public static final String IMPLEMENT_POSITIONS	= "implement positions";
		public static final String DECLARE_CLASS		= "declare class";
		public static final String IMPLEMENT_CLASS		= "implement class";
		public static final String DECLARE_IDS			= "declare identifiers";
		public static final String IMPLEMENT_IDS		= "implement identifiers";
	}
}
