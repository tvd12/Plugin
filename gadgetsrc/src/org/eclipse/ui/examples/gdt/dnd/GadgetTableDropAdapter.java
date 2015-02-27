package org.eclipse.ui.examples.gdt.dnd;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.ui.examples.gdt.Gadget;
/**
 * Supports dropping gadgets into a table viewer.
 */
public class GadgetTableDropAdapter extends ViewerDropAdapter {
   public GadgetTableDropAdapter(TableViewer viewer) {
      super(viewer);
   }
   /**
    * Method declared on ViewerDropAdapter
    */
   public boolean performDrop(Object data) {
      //all gadgets in a table are children of the root
      Gadget parent = (Gadget)getViewer().getInput();
      Gadget[] toDrop = (Gadget[])data;
      for (int i = 0; i < toDrop.length; i++) {
         //get the flat list of all gadgets in this tree
         Gadget[] flatList = toDrop[i].flatten();
         for (int j = 0; j < flatList.length; j++) {
            flatList[j].setParent(parent);
         }
         ((TableViewer)getViewer()).add(flatList);
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