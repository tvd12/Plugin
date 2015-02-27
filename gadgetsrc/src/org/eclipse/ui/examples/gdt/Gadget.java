package org.eclipse.ui.examples.gdt;

import java.util.ArrayList;
/**
 * Standard gadget implementation.  Gadgets live in trees, 
 * but are equally comfortable on tables.  They 
 * know how to attach themselves to a parent, and if you ask
 * nicely they can tell you their names and who their children are.  
 * They're not much good for anything else.
 */
public class Gadget {
   private ArrayList children;
   private String name;
   private Gadget parent;

   public Gadget(Gadget parent, String name) {
      this.name = name;
      this.children = new ArrayList();
      this.parent = parent;
      if (parent != null)
         parent.addChild(this);
   }
   private void addChild(Gadget child) {
      if (child == null)
         throw new NullPointerException();
      children.add(child);
   }
   private void doFlatten(Gadget gadget, ArrayList allGadgets) {
      //add the gadget and its children to the list
      allGadgets.add(gadget);
      Gadget[] children = gadget.getChildren();
      for (int i = 0; i < children.length; i++) {
         doFlatten(children[i], allGadgets);
      }
   }
   public boolean equals(Object object) {
      if (!(object instanceof Gadget))
         return false;
      if (this == object)
         return true;
      Gadget gadget = ((Gadget)object);
      return name.equals(gadget.name) && children.equals(gadget.children);
   }
   /**
    * Returns a flat list of all gadgets in this gadget tree.
    */
   public Gadget[] flatten() {
      ArrayList result = new ArrayList();
      doFlatten(this, result);
      return (Gadget[])result.toArray(new Gadget[result.size()]);
   }
   public Gadget[] getChildren() {
      return (Gadget[])children.toArray(new Gadget[children.size()]);
   }
   public String getName() {
      return name;
   }
   public Gadget getParent() {
      return parent;
   }
   public int hashCode() {
      return name.hashCode() + children.hashCode();
   }
   /**
    * Returns true if this gadget has a parent equal to
    * the given gadget, and false otherwise.
    */
   public boolean hasParent(Gadget gadget) {
      if (parent == null)
         return false;
      return parent.equals(gadget) || parent.hasParent(gadget);
   }
   private void removeChild(Gadget gadget) {
      children.remove(gadget);
   }
   public void setParent(Gadget newParent) {
      if (parent != null)
         parent.removeChild(this);
      this.parent = newParent;
      if (newParent != null)
         newParent.addChild(this);
   }
   public String toString() {
      return name;
   }
}