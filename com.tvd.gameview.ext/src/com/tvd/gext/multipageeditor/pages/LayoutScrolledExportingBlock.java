/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.tvd.gext.multipageeditor.pages;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.tvd.cocos2dx.popup.creator.model.View;
import com.tvd.gameview.ext.GameViewSdk;
import com.tvd.gameview.ext.constants.Constant.TreeElement;
import com.tvd.gext.multipageeditor.editors.constant.Img;
import com.tvd.gext.multipageeditor.exporting.pages.LayoutExportingElement;
import com.tvd.gext.multipageeditor.exporting.pages.LayoutExportingElementPage;
/**
 *
 */
public class LayoutScrolledExportingBlock extends MasterDetailsBlock {
	
	public LayoutScrolledExportingBlock(FormPage page) {
		this.mFormPage = page;
	}
	/**
	 * @param id
	 * @param title
	 */
	class MasterContentProvider implements ITreeContentProvider {
		
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof LayoutEditorInput) {
				LayoutEditorInput input = (LayoutEditorInput) mFormPage
						.getEditor().getEditorInput();

				return new Object[] {
					input.getExportingElement()	
				};
			}
			return new Object[0];
		}
		
		public void dispose() {
		}
		
		public void inputChanged(Viewer viewer, Object oldInput, 
				Object newInput) {
		}

		@Override
		public Object[] getChildren(Object pParentElement) {
			if(pParentElement instanceof LayoutExportingElement) {
				LayoutExportingElement element = 
						(LayoutExportingElement)pParentElement;
				return element.getChilds().toArray();
			}
			
			return new Object[0];
		}

		@Override
		public Object getParent(Object pElement) {
			if(pElement instanceof LayoutExportingElement) {
				LayoutExportingElement element = 
						(LayoutExportingElement)pElement;
				return element.getParent();
			}
			
			return null;
		}

		@Override
		public boolean hasChildren(Object pElement) {
			if(pElement instanceof LayoutExportingElement) {
				LayoutExportingElement element = 
						(LayoutExportingElement)pElement;
				return element.hasChildren();
			}
			return false;
		}
	}
	class MasterLabelProvider extends LabelProvider {
		
		@Override
		public Image getImage(Object obj) {
			if (obj instanceof LayoutExportingElement) {
				LayoutExportingElement element = 
						(LayoutExportingElement)obj;
				if(element.getParent() == null) {
					return GameViewSdk.getDefault()
							.getImageRegistry().get(Img.LAYOUT_EXPORTING_ROOT);
				}
				else {
					return GameViewSdk.getDefault()
							.getImageRegistry().get(Img.LAYOUT_EXPORTING_ELEMENT);
				}
			}
			return null;
		}
		
		@Override
		public String getText(Object element) {
			if(element instanceof LayoutExportingElement) {
				return element.toString();
			}
			
			return "unknown";
		}
		
		@Override
		public void dispose() {
			super.dispose();
		}
	}
	
	@Override
	protected void createMasterPart(final IManagedForm managedForm,
			final Composite parent) {
		//final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, 
				Section.DESCRIPTION | Section.TITLE_BAR);
		section.setText(text("name")); //$NON-NLS-1$
		section.setDescription(text("desc")); //$NON-NLS-1$
		section.marginWidth = 10;
		section.marginHeight = 5;
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);
		Tree tree = toolkit.createTree(client, SWT.NULL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 20;
		gd.widthHint = 100;
		tree.setLayoutData(gd);
		toolkit.paintBordersFor(client);
		
		mActiveButton = createButton(toolkit, client, text("button.active"));
		
		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		
		TreeViewer treeViewer = new TreeViewer(tree);
		treeViewer.setContentProvider(new MasterContentProvider());
		treeViewer.setLabelProvider(new MasterLabelProvider());
		treeViewer.setInput(mFormPage.getEditor().getEditorInput());
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent pEvent) {
				ISelection selection = pEvent.getSelection();
				if(selection instanceof IStructuredSelection) {
					IStructuredSelection s = (IStructuredSelection)selection;
					String name = s.getFirstElement().toString();
					exportWithProgress(parent, name);
				}
			}
		});
		
		treeViewer.expandAll();
		LayoutEditorInput input = (LayoutEditorInput) mFormPage
				.getEditor().getEditorInput();
		mView = input.getView();
		mTreeViewer = treeViewer;
	}
	
	protected void createToolBarActions(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		Action haction = new Action("hor", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.HORIZONTAL);
				form.reflow(true);
			}
		};
		haction.setChecked(true);
		haction.setToolTipText(text("horizontal")); //$NON-NLS-1$
		haction.setImageDescriptor(GameViewSdk.getDefault()
				.getImageRegistry()
				.getDescriptor(Img.LAYOUT_HORIZONTAL));
		Action vaction = new Action("ver", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		vaction.setChecked(false);
		vaction.setToolTipText(text("vertical")); //$NON-NLS-1$
		vaction.setImageDescriptor(GameViewSdk.getDefault()
				.getImageRegistry().getDescriptor(Img.LAYOUT_VERTICAL));
		form.getToolBarManager().add(haction);
		form.getToolBarManager().add(vaction);
	}
	
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(LayoutExportingElement.class, 
				new LayoutExportingElementPage());
	}
	
	protected String text(String key) {
		String className = getClass().getSimpleName();
		return Messages.getString(className + "." + key);
	}
	
	protected Button createButton(final FormToolkit toolkit, 
			final Composite parent,
			String label) {
		Button button = toolkit.createButton(parent, label, 
				SWT.PUSH); //$NON-NLS-1$
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		button.setLayoutData(gd);
		button.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent event) {
				ISelection selection = mTreeViewer.getSelection();
				IStructuredSelection s = (IStructuredSelection)selection;
				String name = s.getFirstElement().toString();
				exportWithProgress(parent, name);
			}
		});
		
		return button;
	}
	
	private void exportWithProgress(final Composite parent, 
			final String name) {
		ProgressMonitorDialog dialog =
				new ProgressMonitorDialog(parent.getShell());
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor pMonitor)
						throws InvocationTargetException, 
						InterruptedException {
					export(name);
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	private void export(String name) {
		if(name.equals(TreeElement.EXPORT_IMAGES)) {
			mView.exportImages();
		}
		else if(name.equals(TreeElement.EXPORT_POSITIONS)) {
			mView.exportDeclaringPositions();
			mView.exportPositions();
		}
		if(name.equals(TreeElement.DECLARE_POSITIONS)) {
			mView.exportDeclaringPositions();
		}
		else if(name.equals(TreeElement.IMPLEMENT_POSITIONS)) {
			mView.exportImplementedPositions();
		}
		else if(name.equals(TreeElement.EXPORT_IDS)) {
			mView.exportIdentifiers();
		}
		else if(name.equals(TreeElement.DECLARE_IDS)) {
			mView.exportDeclaringImageIds();
		}
		else if(name.equals(TreeElement.IMPLEMENT_IDS)) {
			mView.exportImplementedImageIds();
		}
		else if(name.equals(TreeElement.EXPORT_SRC_CODE)) {
			mView.exportSourceCode();
		}
		else if(name.equals(TreeElement.DECLARE_CLASS)) {
			mView.exportHeaderCode();
		}
		else if(name.equals(TreeElement.IMPLEMENT_CLASS)) {
			mView.exportImplementedCode();
		}
		else if(name.equals(TreeElement.EXPORT_XIB_TPL)) {
			mView.exportXibTemplate(false);
		}
		else if(name.equals(TreeElement.RELOAD_SIZE)) {
				mView.reloadImageSizes();
		}
		else if(name.equals(TreeElement.REFRESH_CONTENT)) {
			try {
				mView.refreshXMLFile();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update() {
		LayoutEditorInput input = (LayoutEditorInput) mFormPage
				.getEditor().getEditorInput();
		mView = input.getView();
		if(mTreeViewer != null) {
			mTreeViewer.refresh();
		}
	}

	protected TreeViewer mTreeViewer;
	protected Button mActiveButton;
	
	private FormPage mFormPage;
	private View mView;
	
}