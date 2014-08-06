 package com.tvd.study.linuxsagas.plugin.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.tvd.study.linuxsagas.plugin.page.NewGameViewFilePage;
import com.tvd.study.linuxsagas.plugin.utils.MessageUtils;

public class NewGameViewWizard extends Wizard implements INewWizard {

	public NewGameViewWizard() {
		this.setWindowTitle(MessageUtils.getString("window_title"));
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
//		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		return true;
	}
	
	@Override
	public void addPages() {
		super.addPages();
		
		_pageOne = new NewGameViewFilePage("Game View", this.selection);
		_pageOne.setTitle("From Game View Project");
		_pageOne.setDescription("Creating something from scratch");
		
		this.addPage(_pageOne);
	}
	
	private WizardPage _pageOne;
//	private IWorkbench workbench;
	private IStructuredSelection selection;

}
