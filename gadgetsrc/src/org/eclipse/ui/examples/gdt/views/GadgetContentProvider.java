package org.eclipse.ui.examples.gdt.views;

import org.eclipse.ui.examples.gdt.Gadget;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * For representation of gadgets in trees and tables
 */
class GadgetContentProvider implements ITreeContentProvider {
   private Viewer viewer;
   public void dispose() {
   }
   public Object[] getChildren(Object parentElement) {
      if (parentElement == null)
         return new Gadget[0];
      return ((Gadget)parentElement).getChildren();
   }
   public Object[] getElements(Object input) {
      return getChildren(input);
   }
   public Object getParent(Object element) {
      return ((Gadget)element).getParent();
   }
   public boolean hasChildren(Object element) {
      return getChildren(element).length > 0;
   }
   public void inputChanged(Viewer v, Object old, Object newt) {
      this.viewer = v;
   }
}