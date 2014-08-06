package com.tvd.study.linuxsagas.plugin.constants;

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
	
}
