package com.tvd.gameview.ui.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.tvd.gameview.ui.internal.SdkXMLFileVisitor;

public class SdkXMLFileBuilder extends IncrementalProjectBuilder {

	@Override
	protected IProject[] build(int kind, Map<String, String> args,
			IProgressMonitor monitor) throws CoreException {
		System.out.println("SdkFileBuilder::build kind = " + kind);
		if(kind == FULL_BUILD) {
			fullBuild(getProject(), monitor);
		} else {
			incrementalBuild(getProject(), 
					monitor, getDelta(getProject()));
		}
		return null;
	}
	
	private void incrementalBuild(IProject project, 
			IProgressMonitor monitor, IResourceDelta delta) throws CoreException {
		System.out.println("SdkFileBuilder::incrementalBuild kind = " + delta);
		if(delta == null) {
			fullBuild(project, monitor);
		} else {
			delta.accept(new SdkXMLFileVisitor());
		}
	}
	
	private void fullBuild(IProject project, IProgressMonitor monitor) 
			throws CoreException {
		project.accept(new SdkXMLFileVisitor(), IResource.NONE);
	}
	
	public static final String ID = "com.tvd.gamview.ext.SdkXMLFileBuilder";
}
