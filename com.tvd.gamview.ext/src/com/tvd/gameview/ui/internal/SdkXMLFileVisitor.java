package com.tvd.gameview.ui.internal;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;

public class SdkXMLFileVisitor implements IResourceProxyVisitor,
		IResourceDeltaVisitor {

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		System.out.println("SdkXMLFileVisitor::visit::delta");
		boolean deleted = (IResourceDelta.REMOVED & delta.getKind()) != 0;
		IResource resource = delta.getResource();
		String name = resource.getName();
		if(name.endsWith(".xml")) {
			if(deleted) {
				System.out.println("file " + name + " has deleted");
			}
		}
		return true;
	}
	
	@Override
	public boolean visit(IResourceProxy proxy) throws CoreException {
		System.out.println("SdkXMLFileVisitor::visit::proxy");
		return true;
	}
	
}
