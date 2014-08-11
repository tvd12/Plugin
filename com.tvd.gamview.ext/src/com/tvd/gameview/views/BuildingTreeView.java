package com.tvd.gameview.views;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.part.ViewPart;

import com.tvd.gameview.plugin.model.ViewModel;
import com.tvd.gamview.ext.utils.MessageUtils;
import com.tvd.gamview.ext.utils.ProjectUtils;

/***
 * 
 * @author Dung Ta Van
 *
 * @class BuildingListView: create a view in category Game view
 * Design tree:
 * 	export images
 *	export xib template
 *		export for iphone
 *		export for ipad
 *	export screen template
 *		export for iphone
 *		export for ipad
 *	export positions
 *		declare positions
 *		implement positions
 *	export source code
 *		declare class
 *		implement class
 */
public class BuildingTreeView extends ViewPart {
	
	public BuildingTreeView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		ResourceManager resourceManager = JFaceResources.getResources();
		LocalResourceManager localResourceManager = 
				new LocalResourceManager(resourceManager, parent);
		
		ImageRegistry imageRegistry = new ImageRegistry(localResourceManager);
		URL iconURL = this.getClass().getResource("/icons/sample.gif");
		imageRegistry.put("iconURL", ImageDescriptor.createFromURL(iconURL));
		FontRegistry fontRegistry = JFaceResources.getFontRegistry();
		
		mTreeViewer = new TreeViewer(parent,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		mTreeViewer.setData("REVERSE", Boolean.TRUE);
		mTreeViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new BuildingListLabelProvider(imageRegistry, fontRegistry)));
		
		mTreeViewer.setContentProvider(new BuildingListContentProvider());
		mTreeViewer.setInput(createInput());
		
		
		this.getSite().setSelectionProvider(mTreeViewer);
		mSelectionListener = new BuildingListSelectionListener(mTreeViewer, 
				getSite().getPart());
		this.getSite().getWorkbenchWindow().getSelectionService()
			.addSelectionListener(mSelectionListener);
		ResourcesPlugin.getWorkspace()
			.addResourceChangeListener(new XMLFileChangeListener(this));
	}

	@Override
	public void setFocus() {
		
	}

	@Override
	public void dispose() {
		if(mSelectionListener != null) {
			this.getSite().getWorkbenchWindow().getSelectionService()
				.removeSelectionListener(mSelectionListener);
			mSelectionListener = null;
		}
		super.dispose();
	}
	
	private Object[] createInput() {
		String[] strs = {
				"export images", //0 
					//devices
				"export xib template", //1
					//devices
				"export screen template", //2
					//devices
				"export android template", //3
					//devices
				"export positions", //4 
					//"declare positions", 4.1
					//"implement positions", 4.2
				"export source code", //5 
					//"declare class", //5.1
					//"implement class", //5.2	
		};
		
		List<IProject> sdkProjects = ProjectUtils.getSdkProjects();
		Object input[] = new Object[sdkProjects.size()];
		for(int i = 0 ; i < sdkProjects.size() ; i++) {
			
			//project menuitem in treeview
			BuildingListElement projectElement = 
					new BuildingListElement("export views of " 
							+ sdkProjects.get(i).getName() + " project");
			
			//check exists views
			ProjectUtils.checkViewsInProject();
			
			//get all view in project
			List<ViewModel> viewModels = 
					ProjectUtils.getViewInProject(sdkProjects.get(i)); 
			
			//get all devices
			String devicesStr = null;
			try {
				InputStream inputStream = 
						sdkProjects.get(i).getFile("src/com/properties/global.properties")
						.getContents();
				devicesStr = MessageUtils.getString(inputStream, "devices");
			} catch (CoreException e) {
				e.printStackTrace();
			}
			
			//if have no device
			if(devicesStr == null || devicesStr.equals("")) {
				continue;
			}
			
			//device array
			String devices[] = (devicesStr.contains(","))
					? (devicesStr.split(",")) : new String[] {devicesStr};
			BuildingListElement elements[] = 
					new BuildingListElement[strs.length];
			
		    for(int j = 0 ; j < elements.length ; j++) {
		    	elements[j] = new BuildingListElement(strs[j]);
		    }
		    for(int j = 0 ; j < devices.length ; j++) {
		    	BuildingListElement deviceElement = 
		    			new BuildingListElement("export for " + devices[j]);
		    	elements[0].addChild(deviceElement);
		    	elements[1].addChild(deviceElement);
		    	elements[2].addChild(deviceElement);
		    	elements[3].addChild(deviceElement);
		    }
		    
		    elements[4].addChild(new BuildingListElement("declare positions"));
		    elements[4].addChild(new BuildingListElement("implement positions"));
		    elements[5].addChild(new BuildingListElement("declare class"));
		    elements[5].addChild(new BuildingListElement("implement class"));
			for(int k = 0 ; k < viewModels.size() ; k++) {
				if(!viewModels.get(k).isDone()) {
					String name = "export building list of " + 
							viewModels.get(k).getName() + " view";
					projectElement.addChild(
							new BuildingListElement(name, elements));
				}
			}
			input[i] = projectElement;
		}
		
		return input;
	}
	
	public void update() {
		mTreeViewer.setInput(createInput());
		mTreeViewer.refresh();
	}
	
	public TreeViewer getTreeViewer() {
		return mTreeViewer;
	}
	
	private ISelectionListener mSelectionListener;
	private TreeViewer mTreeViewer;
}
