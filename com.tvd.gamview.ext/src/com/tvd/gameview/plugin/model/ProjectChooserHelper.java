package com.tvd.gameview.plugin.model;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.tvd.gamview.ext.utils.ProjectUtils;

public class ProjectChooserHelper {

	public static class ProjectCombo extends Combo implements SelectionListener {

		public ProjectCombo(Composite parent) {
			super(parent, SWT.BORDER | SWT.FLAT | SWT.READ_ONLY);
			mInitialProject = ProjectUtils.getActiveSdkProject();
			mProject = mInitialProject;
			mAvailableProjects = ProjectUtils.getSdkProjects();
			String items[] = new String[mAvailableProjects.size() + 1];
			items[0] = "--- Choose Project ---";
			int selectionIndex = 0;
			for (int i = 0, n = mAvailableProjects.size(); i < n; i++) {
                IProject project = mAvailableProjects.get(i).getProject();
                items[i + 1] = project.getName();
                if (project == mInitialProject) {
                    selectionIndex = i + 1;
                }
            }
			this.setItems(items);
			this.select(selectionIndex);
			
			this.addSelectionListener(this);
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			int selectionIndex = getSelectionIndex();
            if (selectionIndex > 0 && mAvailableProjects != null
                    && selectionIndex <= mAvailableProjects.size()) {
                // selection index 0 is "Choose Project", all other projects are offset
                // by 1 from the selection index
                mProject = mAvailableProjects.get(selectionIndex - 1).getProject();
            } else {
                mProject = null;
            }
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			
		}
		
		public void setSelectedProject(IProject project) {
            mProject = project;

            int selectionIndex = 0;
            for (int i = 0, n = mAvailableProjects.size(); i < n; i++) {
                if (project == mAvailableProjects.get(i).getProject()) {
                    selectionIndex = i + 1; // +1: Slot 0 is reserved for "Choose Project"
                    select(selectionIndex);
                    break;
                }
            }
        }
		
		public IProject getSelectedProject() {
            return mProject;
        }
		
		@Override
        protected void checkSubclass() {
            // Disable the check that prevents subclassing of SWT components
        }
		
		protected IProject mInitialProject;
		protected List<IProject> mAvailableProjects;
		protected IProject mProject;
	}
}
