package mx.org.kaana.libs.pagina;

import java.util.Iterator;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import org.primefaces.component.inputtext.InputText;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Componente {

  private static final Log LOG= LogFactory.getLog(Componente.class);

  private Componente() {
  }

  public static void printTree(UIComponent component, int level) {
    StringBuffer sb= new StringBuffer();
    if (component == null)
      return;
    Object value= null;
    if (component instanceof EditableValueHolder)
      value = ((EditableValueHolder)component).getValue();
    for (int i = 0; i < level; i++)
      sb.append("    ");
    sb.append(component.getFamily());
    sb.append(" - ");
    sb.append(component.getRendererType());
    sb.append(" - [");
    sb.append(component.getId());
    sb.append("]");
    if (value != null) {
      sb.append(" - ");
      if (value instanceof Object[]) {
        Object array[] = (Object[])value;
        for (int i = 0; i < array.length; i++) {
          sb.append(array[i]);
          sb.append(" ");
        } // for
      }
      else
        sb.append(value);
    } // if
    LOG.debug(sb.toString());
    Iterator<UIComponent> children = component.getChildren().iterator();
    while (children.hasNext()) {
      UIComponent child = children.next();
      printTree(child, level + 1);
    } // while
  }

  public static void cleanComponent(UIComponent component) {
    if (component == null)
      return;
    Object value = null;
    if (component instanceof InputText) {
      value = ((EditableValueHolder)component).getValue();
      if (value!= null) {
        if (value instanceof Object[])
          ((EditableValueHolder)component).setValue(null);
        else
          if (value instanceof String)
            ((EditableValueHolder)component).setValue("");
          else
            ((EditableValueHolder)component).setValue(null);
      } // if
    } // if
    Iterator<UIComponent> children = component.getChildren().iterator();
    while (children.hasNext()) {
      UIComponent child = children.next();
      cleanComponent(child);
    } // while
  }

  public static UIComponent searchComponent(UIComponent component, String id) {
    UIComponent found= null;

    if (component != null && component.getId() != null && component.getId().equalsIgnoreCase(id)) {
      found = component;
    } // if
    else {
      Iterator<UIComponent> children = component.getChildren().iterator();
      while (children.hasNext() && found== null) {
        UIComponent child= children.next();
        found= searchComponent(child, id);
      } // while
    } // else

    return found;
  } // searchComponent

   public static UIComponent searchComponentFacets(UIComponent component, String name) {
     UIComponent found= null;
     if (component== null)
       found= null;
     else
       if(component.getId()!= null && component.getId().equalsIgnoreCase(name))
         found= component;
       else {
         //Iterator children = component.getChildren().iterator();
        Iterator<UIComponent> children = component.getFacetsAndChildren();
         while (children.hasNext() && found== null) {
           UIComponent child= children.next();
           found= searchComponentFacets(child, name);
         } // while
       } // if
     return found;
   } // searchComponent

  public static void removeComponent(UIComponent component, String name, boolean parcial) {
    if (component!= null) {
      Iterator<UIComponent> children = component.getChildren().iterator();
      while (children.hasNext()) {
        UIComponent child= children.next();
        if(child.getId().equalsIgnoreCase(name) || (parcial && child.getId().indexOf(name)>= 0))
          children.remove();
        else
          removeComponent(child, name, parcial);
      } // while
    } // if
  } // removeComponent

}
