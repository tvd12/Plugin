package com.tvd.gameview.ui.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;

import com.tvd.gamview.ext.utils.ProjectUtils;

public class SdkXMLFileVisitor implements IResourceProxyVisitor,
		IResourceDeltaVisitor {

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
//		System.out.println("SdkXMLFileVisitor::visit::delta");
		boolean deleted = (IResourceDelta.REMOVED & delta.getKind()) != 0;
		IResource resource = delta.getResource();
		String name = resource.getName();
		String fullPath = resource.getFullPath().toString();
		IProject project = resource.getProject();
		if(name.endsWith(".xml") && fullPath.contains("resources/xml")) {
			if(deleted) {
				System.out.println("file " + name + " has deleted");
				String devices[] = ProjectUtils.getDevices(project);
				if(devices != null && devices.length > 0) {
					for(String device : devices) {
						IFile file = project.getFile("resources/xml/" + device + "/" + name);
						if(file.exists()) {
							file.delete(true, null);
						}
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean visit(IResourceProxy proxy) throws CoreException {
//		System.out.println("SdkXMLFileVisitor::visit::proxy");
		return true;
	}
	
}
