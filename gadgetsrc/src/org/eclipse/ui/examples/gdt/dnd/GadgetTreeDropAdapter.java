package org.eclipse.ui.examples.gdt.dnd;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.examples.gdt.Gadget;
/**
 * Supports dropping gadgets into a tree viewer.
 */
public class GadgetTreeDropAdapter extends ViewerDropAdapter {
   public GadgetTreeDropAdapter(TreeViewer viewer) {
      super(viewer);
   }
   /**
    * Method declared on ViewerDropAdapter
    */
   public boolean performDrop(Object data) {
      Gadget target = (Gadget)getCurrentTarget();
      if (target == null)
         target = (Gadget)getViewer().getInput();
      Gadget[] toDrop = (Gadget[])data;
      TreeViewer viewer = (TreeViewer)getViewer();
      //cannot drop a gadget onto itself or a child
      for (int i = 0; i < toDrop.length; i++)
         if (toDrop[i].equals(target) || target.hasParent(toDrop[i]))
            return false;
      for (int i = 0; i < toDrop.length; i++) {
         toDrop[i].setParent(target);
         viewer.add(target, toDrop[i]);
         viewer.reveal(toDrop[i]);
      }
      return true;
   }
   /**
    * Method declared on ViewerDropAdapter
    */
   public boolean validateDrop(Object target, int op, TransferData type) {
      return GadgetTransfer.getInstance().isSupportedType(type);
   }
}