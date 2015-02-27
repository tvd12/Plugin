package org.eclipse.ui.examples.gdt.dnd;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.examples.gdt.Gadget;
import org.eclipse.ui.part.IDropActionDelegate;

/**
 * Performs dropping of gadgets into views that contain resources.
 */
public class GadgetPluginDropAdapter implements IDropActionDelegate {
   /**
    * Method declared on IDropActionDelegate
    */
   public boolean run(Object source, Object target) {
      if (target instanceof IContainer) {
         GadgetTransfer transfer = GadgetTransfer.getInstance();
         Gadget[] gadgets = transfer.fromByteArray((byte[])source);
         IContainer parent = (IContainer)target;
         for (int i = 0; i < gadgets.length; i++) {
            writeGadgetFile(parent, gadgets[i]);
         }
         return true;
      }
      //drop was not successful so return false
      return false;
   }
   private void writeGadgetFile(IContainer parent, Gadget gadget) {
      try {
         IFile file = parent.getFile(new Path(gadget.getName()));
         ByteArrayInputStream in = createFileContents(gadget);
         if (file.exists()) {
            file.setContents(in, IResource.NONE, null);
         } else {
            file.create(in, IResource.NONE, null);
         }
      } catch (CoreException e) {
         e.printStackTrace();
      }
   }
   private ByteArrayInputStream createFileContents(Gadget gadget) {
      //write the hierarchy of gadgets to string
      StringBuffer buf = new StringBuffer();
      writeGadgetString(gadget, buf, 0);
      return new ByteArrayInputStream(buf.toString().getBytes());
   }
   private void writeGadgetString(Gadget gadget, StringBuffer buf, int depth) {
      for (int i = 0; i < depth; i++)
         buf.append('\t');
      buf.append(gadget.getName());
      buf.append('\n');
      Gadget[] children = gadget.getChildren();
      for (int i = 0; i < children.length; i++)
         writeGadgetString(children[i], buf, depth + 1);
   }
}