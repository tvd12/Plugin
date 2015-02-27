package org.eclipse.ui.examples.gdt.views;

import org.eclipse.ui.examples.gdt.Gadget;
import org.eclipse.ui.examples.gdt.clipboard.*;
import org.eclipse.ui.examples.gdt.dnd.*;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.PluginTransfer;
import org.eclipse.ui.part.ViewPart;

public class GadgetTreeView extends ViewPart {
   private Clipboard clipboard;
   private TreeViewer viewer;
   private GadgetTreeDropAdapter dropAdapter;
   private Gadget createGadgets() {
      Gadget invisibleRoot = new Gadget(null, "");
      Gadget root = new Gadget(invisibleRoot, "Uber Gadget");
      Gadget p1 = new Gadget(root, "Parent Gadget 1");
      new Gadget(p1, "Leaf Gadget 1");
      new Gadget(p1, "Leaf Gadget 2");
      new Gadget(p1, "Leaf Gadget 3");
      Gadget p2 = new Gadget(root, "Parent Gadget 2");
      new Gadget(p2, "Leaf Gadget 4");
      return invisibleRoot;
   }
   /**
    * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
    */
   public void createPartControl(Composite parent) {
      viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
      viewer.setContentProvider(new GadgetContentProvider());
      viewer.setLabelProvider(new LabelProvider());
      viewer.setInput(createGadgets());

      //add drag and drop support
      int ops = DND.DROP_COPY | DND.DROP_MOVE;
      Transfer[] transfers = new Transfer[] { GadgetTransfer.getInstance(), PluginTransfer.getInstance()};
      viewer.addDragSupport(ops, transfers, new GadgetDragListener(viewer));
      transfers = new Transfer[] {GadgetTransfer.getInstance()};
      dropAdapter = new GadgetTreeDropAdapter(viewer);
      viewer.addDropSupport(ops, transfers, dropAdapter);

      //add cut and paste support
      clipboard = new Clipboard(getSite().getShell().getDisplay());
      IActionBars bars = getViewSite().getActionBars();
      bars.setGlobalActionHandler(IWorkbenchActionConstants.CUT, new CutGadgetAction(viewer, clipboard));
      bars.setGlobalActionHandler(IWorkbenchActionConstants.COPY, new CopyGadgetAction(viewer, clipboard));
      bars.setGlobalActionHandler(IWorkbenchActionConstants.PASTE, new PasteTreeGadgetAction(viewer, clipboard));
   }
   public void dispose() {
      super.dispose();
      clipboard.dispose();
      clipboard = null;
   }
   public void setFocus() {
      viewer.getControl().setFocus();
   }
}