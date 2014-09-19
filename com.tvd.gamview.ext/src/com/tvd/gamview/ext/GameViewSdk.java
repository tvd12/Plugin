package com.tvd.gamview.ext;

import java.io.File;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.tvd.gamview.ext.constants.Constant;

/**
 * The activator class controls the plug-in life cycle
 */
public class GameViewSdk extends AbstractUIPlugin {

	public static abstract class CheckSdkErrorHandler {
		public abstract boolean handleError(String message);
        public abstract boolean handleWarning(String message);
        protected void setErrorMessage(String replaceAll) {
			
		}
        protected void showMessage(String replaceAll) {
			
		}
	}

	// The plug-in ID
	public static final String PLUGIN_ID = "com.tvd.gameview.ext.plugin"; //$NON-NLS-1$

	// The shared instance
	private static GameViewSdk plugin;
	
	/**
	 * The constructor
	 */
	public GameViewSdk() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static GameViewSdk getDefault() {
		return plugin;
	}

	public boolean checkSdkLocationAndId(String fileName,
			CheckSdkErrorHandler checkSdkErrorHandler) {
		File file = new File(fileName + "/" 
				+ Constant.GLOBAL_PROJECT_CONFIG_FILE);
		if(!file.exists()) {
			checkSdkErrorHandler.handleError("Invalid location");
			checkSdkErrorHandler.handleWarning("Invalid location");
		}
		
		return file.exists();
	}

}
