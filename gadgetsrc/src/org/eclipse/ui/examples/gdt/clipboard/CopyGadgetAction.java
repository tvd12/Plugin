package org.eclipse.ui.examples.gdt.clipboard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.examples.gdt.Gadget;
import org.eclipse.ui.examples.gdt.dnd.GadgetTransfer;

/**
 * Action for copying a selection of gadgets to the clipboard
 */
public class CopyGadgetAction extends Action {
   protected Clipboard clipboard;
   protected StructuredViewer viewer;
   public CopyGadgetAction(StructuredViewer viewer, Clipboard clipboard) {
      super("Copy");
      this.viewer = viewer;
      this.clipboard = clipboard;
   }
   public void run() {
      IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
      Gadget[] gadgets = (Gadget[])sel.toList().toArray(new Gadget[sel.size()]);
      clipboard.setContents(new Object[] {gadgets}, new Transfer[] {GadgetTransfer.getInstance()});
   }
}