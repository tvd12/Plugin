package com.tvd.gameview.views;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.part.ViewPart;

public class XMLFileChangeListener implements IResourceChangeListener {

	public XMLFileChangeListener(ViewPart viewPart) {
		mViewPart = viewPart;
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if(event.getSource() != null) {
			IResource res = event.getResource();
			try {
		         switch (event.getType()) {
		            case IResourceChangeEvent.PRE_CLOSE:
		               System.out.print("Project ");
		               System.out.print(res.getFullPath());
		               System.out.println(" is about to close.");
		               break;
		            case IResourceChangeEvent.PRE_DELETE:
		               System.out.print("Project ");
		               System.out.print(res.getFullPath());
		               System.out.println(" is about to be deleted.");
		               break;
		            case IResourceChangeEvent.POST_CHANGE:
		               System.out.println("Resources have changed.");
		               event.getDelta().accept(new SdkResourceDeltaVisitor());
		               break;
		            case IResourceChangeEvent.PRE_BUILD:
		               System.out.println("Build about to run.");
		               event.getDelta().accept(new SdkResourceDeltaVisitor());
		               break;
		            case IResourceChangeEvent.POST_BUILD:
		               System.out.println("Build complete.");
		               event.getDelta().accept(new SdkResourceDeltaVisitor());
		               break;
		         }
			} catch(CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class SdkResourceDeltaVisitor implements IResourceDeltaVisitor {
	      public boolean visit(IResourceDelta delta) {
	         IResource res = delta.getResource();
	         switch (delta.getKind()) {
	            case IResourceDelta.ADDED:
	               System.out.print("Resource ");
	               System.out.print(res.getFullPath());
	               System.out.println(" was added.");
	               break;
	            case IResourceDelta.REMOVED:
	               System.out.print("Resource ");
	               System.out.print(res.getFullPath());
	               System.out.println(" was removed.");
	               String fullPath = res.getFullPath().toString();
	               if(fullPath == null 
	            		   || fullPath.equals("")
	            		   || fullPath.trim().equals("/")) {
	            	   return true;
	               }
	               if((fullPath.contains("resources/xml")
	            		   && res.getName().endsWith(".xml"))) {
	            	   updateTreeViewer();
	               }
	               if(fullPath.lastIndexOf('/') == 0
	            		   || !fullPath.contains("/")) {
	            	   updateTreeViewer();
	               }
	               break;
	            case IResourceDelta.CHANGED:
	               System.out.print("Resource ");
	               System.out.print(res.getFullPath());
	               System.out.println(" has changed.");
	               if(res.getName().equals("global.properties")) {
	            	   updateTreeViewer();
	               }
	               break;
	         }
	         return true; // visit the children
	      }
	}
	
	private void updateTreeViewer() {
		if(mViewPart instanceof BuildingTreeView) {
 		   final BuildingTreeView tree = (BuildingTreeView)mViewPart;
 		   tree.getTreeViewer().getControl().getDisplay().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					tree.update();
				}
			});
 	   }
	}
	
	private ViewPart mViewPart;
	
}
