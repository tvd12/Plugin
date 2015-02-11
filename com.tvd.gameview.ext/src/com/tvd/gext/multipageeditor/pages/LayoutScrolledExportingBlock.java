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
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
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

import com.tvd.gameview.ext.GameViewSdk;
import com.tvd.gext.multipageeditor.editors.constant.Img;
import com.tvd.gext.multipageeditor.exporting.pages.LayoutExportingElement;
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
			Composite parent) {
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
		Button b = toolkit.createButton(client, text("button.active"), 
				SWT.PUSH); //$NON-NLS-1$
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		b.setLayoutData(gd);
		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		
		TreeViewer treeViewer = new TreeViewer(tree);
		treeViewer.setContentProvider(new MasterContentProvider());
		treeViewer.setLabelProvider(new MasterLabelProvider());
		treeViewer.setInput(mFormPage.getEditor().getEditorInput());
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		treeViewer.expandAll();
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
//		detailsPart.registerPage(LayoutExportingElement.class, new Layoutex);
	}
	
	protected String text(String key) {
		String className = getClass().getSimpleName();
		return Messages.getString(className + "." + key);
	}
	
	private FormPage mFormPage;
}