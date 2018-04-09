package com.coolservlets.beans.menu;

import java.util.List;
import org.primefaces.model.menu.BaseMenuModel;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuGroup;

/**
 * @company KAANA
 * @project KAJOOL (Control system polls)
 * @date 22/10/2013
 * @time 01:36:29 PM
 * @author Team Developer 2016 <team.developer@kaana.org.mx>
 */
public class MenuModel extends BaseMenuModel {

  private boolean generated = true;

  @Override
  public void generateUniqueIds() {
    if (!generated) {
      generateUniqueIds(getElements(), "_");
      generated = true;
    } // if
  }

  private void generateUniqueIds(List<MenuElement> elements, String seed) {
    if (elements == null || elements.isEmpty()) {
      return;
    } // if
    int counter = 0;
    for (MenuElement element : elements) {
      String id = (seed == null) ? String.valueOf(counter++) : seed + "_" + counter++;
      element.setId(id);
      if (element instanceof MenuGroup) {
        generateUniqueIds(((MenuGroup) element).getElements(), id);
      } // if
    } // for
  }
}
