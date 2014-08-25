 package com.tvd.gamview.ext.wizards;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.xml.XmlFileBuilder;
import com.tvd.gameview.plugin.model.ProjectChooserHelper.ProjectCombo;
import com.tvd.gamview.ext.pages.NewGameViewFilePage;
import com.tvd.gamview.ext.utils.MessageUtils;
import com.tvd.gamview.ext.utils.ProjectUtils;

public class NewGameViewWizard extends Wizard implements INewWizard {

	public NewGameViewWizard() {
		this.setWindowTitle(MessageUtils.getString("window_title"));
		this.mType = "popup";
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
//		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		createXMLFile();
		return true;
	}
	
	@Override
	public void addPages() {
		super.addPages();
		
		mNewGameViewFilePage = new NewGameViewFilePage("Game View", this.selection);
		mNewGameViewFilePage.setTitle("From Game View Project");
		mNewGameViewFilePage.setDescription("Creating something from scratch");
		
		this.addPage(mNewGameViewFilePage);
	}
	
	private void createXMLFile() {
		try {
			NewGameViewFilePage gameViewPage = (NewGameViewFilePage)mNewGameViewFilePage;
			
			//update global config for this xml file
			updateXMLGlobalConfig(gameViewPage);
			
			//create new xml file corresponding to game viw
			XmlFileBuilder xmlBuilder = new XmlFileBuilder(gameViewPage
					.getImageInputPathField().getText().getText(),
					gameViewPage.getInterfaceBuilder());
			
			//build for device
			IProject project = ((ProjectCombo)gameViewPage.getProjectCombo()).getSelectedProject();
			String devices[] = ProjectUtils.getDevices(project);
			if(devices == null || devices.length == 0) {
				return;
			}
			String fileName = gameViewPage.getFileNameTextField().getText();
			IFile newFile = project.getFile(
					"resources/xml/" + devices[0] + "/" + fileName);
			File file = newFile.getLocation().toFile();
			
			boolean duplicate = false;
			
			//if file exist
			if(file.exists()) {
				
				//confirm for overriding file
				String message = "Do you want override exists view?";
				if(!MessageDialog.openConfirm(this.getShell(), "Duplicate view", message)) {
					return;
				}
				//delete old file
				newFile.delete(false, null);
				duplicate = true;
			}
			
			ProjectUtils.createXMLFileWithBuilder(project, xmlBuilder, mType, true);
			
			//update tree view part
			if(!duplicate) {
				updateBuildingTreeViewPart();
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
	}
	
	private void updateXMLGlobalConfig(NewGameViewFilePage gameViewPage) {
		Config config = Config.getInstance();
		config.setClassPath(gameViewPage.getClassPathField().getText().getText());
		config.setDefinePath(gameViewPage.getDefineSelectionField().getText().getText());
		config.setImagesInputPath(gameViewPage.getImageInputPathField().getText().getText());
		config.setImagesPath(gameViewPage.getImageOutputPathField().getText().getText());
		config.setScreenContainerPath(gameViewPage.getScreenContainerPathField().getText().getText());
		config.setXibContainerPath(gameViewPage.getXibContainerPathField().getText().getText());
		config.setParametersPath(gameViewPage.getParamsSelectionField().getText().getText());
		config.setAndroidContainerPath(gameViewPage.getAndroidContainerPathField().getText().getText());
	}
	
	/**
	 * after create new xml file, add it to file list 
	 * and update building tree on view part
	 */
	private void updateBuildingTreeViewPart() {
		ProjectUtils.updateBuildingTreeViewPart();
	}
	
	private WizardPage mNewGameViewFilePage;
//	private IWorkbench workbench;
	private IStructuredSelection selection;
	
	protected String mType;

}
