package org.eclipse.ui.examples.gdt.clipboard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.ui.examples.gdt.Gadget;
import org.eclipse.ui.examples.gdt.dnd.GadgetTransfer;

/**
 * Action for pasting a selection of gadgets from the clipboard
 * into a table viewer.
 */
public class PasteTableGadgetAction extends Action {
   protected Clipboard clipboard;
   protected StructuredViewer viewer;
   public PasteTableGadgetAction(StructuredViewer viewer, Clipboard clipboard) {
      super("Paste");
      this.viewer = viewer;
      this.clipboard = clipboard;
   }
   public void run() {
      Gadget[] gadgets = (Gadget[])clipboard.getContents(GadgetTransfer.getInstance());
      if (gadgets == null)
         return;
      Gadget parent = (Gadget)viewer.getInput();
      for (int i = 0; i < gadgets.length; i++) {
         //get the flat list of all gadgets in this tree
         Gadget[] flatList = gadgets[i].flatten();
         for (int j = 0; j < flatList.length; j++) {
            flatList[j].setParent(parent);
         }
      }
      viewer.refresh();
   }
}
