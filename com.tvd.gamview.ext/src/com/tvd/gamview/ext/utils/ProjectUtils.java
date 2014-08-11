package com.tvd.gamview.ext.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.tvd.gameview.plugin.model.ViewModel;
import com.tvd.gameview.views.BuildingTreeView;

public class ProjectUtils {

	public static IProject[] getProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		
		return projects;
	}
	
	public static IResource extractSelection(ISelection sel) {
	      if (!(sel instanceof IStructuredSelection))
	          return null;
	       IStructuredSelection ss = (IStructuredSelection) sel;
	       Object element = ss.getFirstElement();
	       if (element instanceof IResource)
	          return (IResource) element;
	       if (!(element instanceof IAdaptable))
	          return null;
	       IAdaptable adaptable = (IAdaptable)element;
	       Object adapter = adaptable.getAdapter(IResource.class);
	       
	       return (IResource) adapter;
	   }
	
	public static IResource extractResource(IEditorPart editor) {
	      IEditorInput input = editor.getEditorInput();
	      if (!(input instanceof IFileEditorInput)) {
	         return null;
	      }
	      
	      return ((IFileEditorInput)input).getFile();
	   }
	
	public static IProject getActiveProject() {
		IWorkbenchWindow workbenchWindow = null;
		IWorkbenchPage workbenchPage = null;
		IWorkbench workbench = PlatformUI.getWorkbench();
		if(workbench != null) {
			workbenchWindow = workbench.getActiveWorkbenchWindow();
			IResource resource = extractSelection(workbenchWindow
						.getSelectionService()
						.getSelection());
			if(resource != null) {
				return resource.getProject();
			}
		}
		if(workbenchWindow != null) {
			workbenchPage = workbenchWindow.getActivePage();
		}
		if(workbenchPage != null) {
			IEditorPart editorPart = workbenchPage.getActiveEditor();
			if(editorPart != null) {
				IResource resource = extractResource(editorPart).getProject();
				if(resource != null) {
					return resource.getProject();
				}
			}
		}
		
		return null;
	}
	
	public static IProject getActiveSdkProject() {
		IProject project = getActiveProject();
		if(project != null) {
			if(findSdkProject(project.getName()) != null) {
				return project;
			}
		}
		
		return null;
	}
	
	public static List<IProject> getSdkProjects() {
		IProject[] projects = getProjects();
		List<IProject> result = new ArrayList<IProject>();
		for(IProject project : projects) {
			try {
				String persistentProperty = 
						project.getPersistentProperty(new QualifiedName("tvd", "author"));
				if(persistentProperty != null && persistentProperty.equals("tvd")) {
					result.add(project);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static List<String> getSdkProjectNames() {
		IProject[] projects = getProjects();
		List<String> result = new ArrayList<String>();
		for(IProject project : projects) {
			try {
				String persistentProperty = 
						project.getPersistentProperty(new QualifiedName("tvd", "author"));
				if(persistentProperty != null && persistentProperty.equals("tvd")) {
					result.add(project.getName());
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static IProject findSdkProject(String pName) {
		List<IProject> projects = getSdkProjects();
		IProject result = null;
		for(IProject project : projects) {
			if(project.getName().equals(pName)) {
				result = project;
			}
		}
		
		return result;
	}
	
	public static List<ViewModel> getViewInProject(IProject project) {
		List<ViewModel> results = new ArrayList<ViewModel>();
		try {
			String propertyValue = project.getPersistentProperty(new QualifiedName("tvd", "views"));
			if(propertyValue != null && !propertyValue.equals("")) {
				String strs[] = {propertyValue};
				if(propertyValue.contains(";")) {
					strs = propertyValue.split(";");
				}
				for(int i = 0 ; i < strs.length ; i++) {
					String name = strs[i];
					if(name.trim().equals("")) {
						continue;
					}
					boolean isDone = false;
					if(strs[i].contains(":done")) {
						name = strs[i].split(":")[0];
						isDone = true;
					}
					results.add(new ViewModel(name, isDone));
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	public static void deleteXMLViewFileInProject(IProject project, String fileName) {
		try {
			String propertyValue = project.getPersistentProperty(
					new QualifiedName("tvd", "views"));
			if(propertyValue != null && !propertyValue.equals("")) {
				if(propertyValue.contains(fileName + ":done")) {
					propertyValue = propertyValue.replace(fileName + ":done;", "");
				} else if(propertyValue.contains(fileName)) {
					propertyValue = propertyValue.replace(fileName + ";", "");
				}
				project.setPersistentProperty(
						new QualifiedName("tvd", "views"), propertyValue);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public static void checkViewsInProject() {
		List<IProject> projects = getSdkProjects();
		for(int i = 0 ; i < projects.size() ; i++) {
			IProject project = projects.get(i);
			List<ViewModel> models = getViewInProject(project);
			String listFile = "";
			for(int k = 0 ; k < models.size() ; k++) {
				IFile file = project.getFile(models.get(k).getRealName());
//				System.out.println("checkViewsInProject::file ("
//						+ models.get(k).getRealName() + ") exists = " + file.exists()
//						+ " size = " + models.size());
				if(file.exists()) {
					listFile += models.get(k) + ";";
				}
			}
			if(listFile.length() > 0 && listFile.contains(";")) {
				listFile = listFile.substring(0, listFile.length() - 1);
			}
			try {
				project.setPersistentProperty(new QualifiedName("tvd", "views"), listFile);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * after create new xml file, add it to file list 
	 * and update building tree on view part
	 */
	public static void updateBuildingTreeViewPart() {
		System.out.println("updateBuildingTreeViewPart = 1");
		IWorkbench workbench = PlatformUI.getWorkbench();
		if(workbench == null) {
			return;
		}
		System.out.println("updateBuildingTreeViewPart = 2");
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		if(workbenchWindow == null) {
			return;
		}
		System.out.println("updateBuildingTreeViewPart = 3");
		IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
		if(workbenchPage == null) {
			return;
		}
		System.out.println("updateBuildingTreeViewPart = 4");
		IViewPart viewPart = workbenchPage
				.findView(BuildingTreeView.class.getName());
		if(viewPart == null) {
			return;
		}
		System.out.println("updateBuildingTreeViewPart = 5");
		BuildingTreeView buildingTreeView = (BuildingTreeView)viewPart;
		buildingTreeView.update();
	}
}
