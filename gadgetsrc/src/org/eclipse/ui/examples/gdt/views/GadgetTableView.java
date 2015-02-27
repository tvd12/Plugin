package org.eclipse.ui.examples.gdt.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.examples.gdt.Gadget;
import org.eclipse.ui.examples.gdt.clipboard.*;
import org.eclipse.ui.examples.gdt.dnd.*;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.part.ViewPart;

public class GadgetTableView extends ViewPart {
   private Clipboard clipboard;
   private TableViewer viewer;
   private Gadget createGadgets() {
      //return a degenerate tree of gadgets
      Gadget parent = new Gadget(null, "");
      new Gadget(parent, "Gadget 1");
      new Gadget(parent, "Gadget 2");
      new Gadget(parent, "Gadget 3");
      new Gadget(parent, "Gadget 4");
      return parent;
   }
   /**
    * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
    */
   public void createPartControl(Composite parent) {
      viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
      viewer.setContentProvider(new GadgetContentProvider());
      viewer.setLabelProvider(new LabelProvider());
      viewer.setInput(createGadgets());

      //add drag and drop support
      int ops = DND.DROP_COPY | DND.DROP_MOVE;
      Transfer[] transfers = new Transfer[] { GadgetTransfer.getInstance(), PluginTransfer.getInstance()};
      viewer.addDragSupport(ops, transfers, new GadgetDragListener(viewer));
      transfers = new Transfer[] {GadgetTransfer.getInstance()};
      viewer.addDropSupport(ops, transfers, new GadgetTableDropAdapter(viewer));

      //add cut and paste support
      clipboard = new Clipboard(getSite().getShell().getDisplay());
      IActionBars bars = getViewSite().getActionBars();
      bars.setGlobalActionHandler(IWorkbenchActionConstants.CUT, new CutGadgetAction(viewer, clipboard));
      bars.setGlobalActionHandler(IWorkbenchActionConstants.COPY, new CopyGadgetAction(viewer, clipboard));
      bars.setGlobalActionHandler(IWorkbenchActionConstants.PASTE, new PasteTableGadgetAction(viewer, clipboard));
   }
   public void dispose() {
      super.dispose();
      if (clipboard != null)
         clipboard.dispose();
      clipboard = null;
   }
   public void setFocus() {
      viewer.getControl().setFocus();
   }
}