package org.eclipse.ui.examples.gdt.clipboard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.examples.gdt.Gadget;
import org.eclipse.ui.examples.gdt.dnd.GadgetTransfer;

/**
 * Class for cutting a selection of gadgets from a view,
 * and placing them on the clipboard.
 */
public class CutGadgetAction extends Action {
   protected Clipboard clipboard;
   protected StructuredViewer viewer;
   public CutGadgetAction(StructuredViewer viewer, Clipboard clipboard) {
      super("Cut");
      this.viewer = viewer;
      this.clipboard = clipboard;
   }
   public void run() {
      IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
      Gadget[] gadgets = (Gadget[])sel.toList().toArray(new Gadget[sel.size()]);
      clipboard.setContents(
         new Object[] { gadgets },
         new Transfer[] { GadgetTransfer.getInstance()});
      for (int i = 0; i < gadgets.length; i++) {
         gadgets[i].setParent(null);
      }
      viewer.refresh();
   }
}