package com.tvd.gameview.views;

import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IFile;
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
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.part.ViewPart;

import com.tdgc.cocos2dx.popup.creator.model.View;
import com.tdgc.cocos2dx.popup.creator.xml.XmlFetcher;
import com.tvd.gameview.plugin.model.ViewModel;
import com.tvd.gamview.ext.constants.Constant;
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
public class BuildingTreeView extends ViewPart implements IDoubleClickListener {
	
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
		mTreeViewer.addDoubleClickListener(this);
		
		this.getSite().setSelectionProvider(mTreeViewer);
		mSelectionListener = new BuildingListSelectionListener(mTreeViewer, 
				getSite().getPart());
		this.getSite().getWorkbenchWindow().getSelectionService()
			.addSelectionListener(mSelectionListener);
		ResourcesPlugin.getWorkspace()
			.addResourceChangeListener(new XMLFileChangeListener(this));
	}
	
	@Override
	public void doubleClick(DoubleClickEvent event) {
		Viewer viewer = event.getViewer();
		ISelection sel = viewer.getSelection();
		Object selectedValue = null;
		if(!(sel instanceof IStructuredSelection) || sel.isEmpty()) {
			selectedValue = null;
		} else {
			selectedValue = ((IStructuredSelection)sel).getFirstElement();
			if(selectedValue instanceof BuildingListElement) {
				BuildingListElement element = (BuildingListElement)selectedValue;
				System.out.println("name of element = " + element.getName()
						+ " device = " + element.getDevice()
						+ " parent = " + element.getParent()
						+ " filePath = " + element.getFilePath());
				if(element.getFilePath() == null 
						|| element.getFilePath().equals("")) {
					return;
				}
				IFile xmlFile = element.getProject().getFile(element.getFilePath());
				XmlFetcher xmlFetcher = new XmlFetcher();
				View view = xmlFetcher.fetchView(xmlFile);
				exportImages(element, view);
				declareIdentifiers(element, view);
				implementIdentifiers(element, view);
				exportIdentifiers(element, view);
			}
		}
	}

	private void exportImages(BuildingListElement element, View view) {
		String device = element.getDevice();
		if(device != null && !device.equals("")) {
			BuildingListElement parentOfDeviceElement = element.getParent();
			if(parentOfDeviceElement.getName()
					.equals(Constant.TreeElement.EXPORT_IMAGES)) {
				System.out.println("Exporting images...");
				view.exportImages();
			}
		}
	}
	
	private void declareIdentifiers(BuildingListElement element, View view) {
		if(element.getName().equals(Constant.TreeElement.DECLARE_IDS)) {
			System.out.println("declaring identifier...");
			view.exportDeclaringImageIds();
		}
	}
	
	private void implementIdentifiers(BuildingListElement element, View view) {
		if(element.getName().equals(Constant.TreeElement.IMPLEMENT_IDS)) {
			System.out.println("declaring identifier...");
			view.exportImplementedImageIds();
		}
	}
	
	private void exportIdentifiers(BuildingListElement element, View view) {
		if(element.getName().equals(Constant.TreeElement.EXPORT_IDS)) {
			System.out.println("declaring identifier...");
			view.exportDeclaringImageIds();
			view.exportImplementedImageIds();
		}
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
				"export identifiers", //4
					//declare identifiers 4.1
					//implement identifiers 4.2
				"export positions", //5
					//"declare positions", 5.1
					//"implement positions", 5.2
				"export source code", //6 
					//"declare class", //6.1
					//"implement class", //6.2	
		};
		
		List<IProject> sdkProjects = ProjectUtils.getSdkProjects();
		Object input[] = new Object[sdkProjects.size()];
		for(int i = 0 ; i < sdkProjects.size() ; i++) {
			
			//project menuitem in treeview
			BuildingListElement projectElement = 
					new BuildingListElement("export views of " 
							+ sdkProjects.get(i).getName() + " project",
							sdkProjects.get(i));
			
			//check exists views
			ProjectUtils.checkViewsInProject();
			
			//get all view in project
			List<ViewModel> viewModels = 
					ProjectUtils.getViewInProject(sdkProjects.get(i)); 
			
			//device array
			String devices[] = null;
			try {
				devices = ProjectUtils.getDevices(sdkProjects.get(i));
			} catch(CoreException e) {
				e.printStackTrace();
			}
			if(devices == null || devices.length == 0) {
				return new Object[] {new BuildingListElement("iphone")};
			}
			BuildingListElement elements[] = 
					new BuildingListElement[strs.length];
			
		    for(int j = 0 ; j < elements.length ; j++) {
		    	elements[j] = new BuildingListElement(strs[j]);
		    }
		    
		    elements[4].addChild(new BuildingListElement(Constant.TreeElement.DECLARE_IDS));
		    elements[4].addChild(new BuildingListElement(Constant.TreeElement.IMPLEMENT_IDS));
		    elements[5].addChild(new BuildingListElement("declare positions"));
		    elements[6].addChild(new BuildingListElement("implement positions"));
		    elements[6].addChild(new BuildingListElement("declare class"));
		    elements[6].addChild(new BuildingListElement("implement class"));
			for(int k = 0 ; k < viewModels.size() ; k++) {
				if(!viewModels.get(k).isDone()) {
					String name = "export building list of " + 
							viewModels.get(k).getName() + " view";
					for(int j = 0 ; j < devices.length ; j++) {
				    	String device = devices[j].trim();
				    	BuildingListElement deviceElement = 
				    			new BuildingListElement("export for " + device, device);
				    	deviceElement.setFilePath("resources/xml/" + device
				    			+ "/" + viewModels.get(k).getName());
				    	elements[0].addChild(deviceElement.copy());
				    	elements[1].addChild(deviceElement.copy());
				    	elements[2].addChild(deviceElement.copy());
				    	elements[3].addChild(deviceElement.copy());
				    }
					BuildingListElement viewElement = new BuildingListElement(name, elements);
					viewElement.setFilePath("resources/xml/" + devices[0]
				    			+ "/" + viewModels.get(k).getName());
					projectElement.addChild(viewElement);
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
