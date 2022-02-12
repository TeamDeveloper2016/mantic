package mx.org.kaana.kajool.control.bean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.FileSearch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Collections;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 6/02/2022
 *@time 01:04:30 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public final class Portal implements Serializable {

  private static final Log LOG = LogFactory.getLog(Portal.class);
  private static final long serialVersionUID = 6003054262706290104L;

  private static Portal instance;
  private static Object mutex;

  private MenuModel menu;
  private List<String> images;

  static {
    mutex = new Object();
  }

  private Portal() {
    this.reload();
  }

  public static Portal getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        instance = new Portal();
      }
    } // if
    return instance;
  }

  public List<String> getImages() {
    return Collections.unmodifiableList(this.images);
  }
  
  public MenuModel getMenu() {
    return menu;
  }
  
  public void reload() {
    try {
      this.images= new ArrayList<>();
      String portal= Configuracion.getInstance().getPropiedadSistemaServidor("portal");
      FileSearch fileSearch = new FileSearch();
      fileSearch.searchDirectory(new File(portal), "*");
      if(fileSearch.getResult().size()> 0)
        for (String matched: fileSearch.getResult()) {
          String name= matched.substring((matched.lastIndexOf("/")< 0? matched.lastIndexOf("\\"): matched.lastIndexOf("/"))+ 1);
          if(!name.endsWith("ftpquota"))
            this.images.add(name);
        } // for 
      LOG.error("Imagenes del portal: "+ this.images);
      this.toLoadMenu();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
  }
  
	private void toLoadMenu() {
    List<Entity> marcas       = null;
    List<Columna> columns     = null;    
    Map<String, Object> params= null;
    try {      
      params = new HashMap<>();      
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);      
      params.put("sortOrder", "order by tc_mantic_productos_marcas.nombre");
      columns= new ArrayList<>();
      columns.add(new Columna("descripcion", EFormatoDinamicos.MAYUSCULAS));
      marcas = (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticProductosMarcasDto", "lazy", params);
      // MENU DINAMICO
      this.menu = new DefaultMenuModel();
      DefaultMenuItem item = new DefaultMenuItem("Inicio");
      item.setIcon("fa fa-home");
      item.setUrl("/indice.jsf".concat(Constantes.REDIRECIONAR));
      this.menu.addElement(item);      
      DefaultSubMenu sub= new DefaultSubMenu("Categorías");
      sub.setIcon("fa fa-language");
      this.toLoadCategorias(sub, "null", 2);
      this.menu.addElement(sub);
      sub= new DefaultSubMenu("Marcas");
      sub.setIcon("fa fa-picture-o");
      if(marcas!= null && !marcas.isEmpty()) {
        for (Entity entity: marcas) {
          item= new DefaultMenuItem(entity.toString("descripcion"));
          item.setIcon("fa fa-picture-o");
          item.setOncomplete("galeriaPrincipal('".concat(entity.toString("descripcion")).concat("', 'MARCA');"));
          sub.addElement(item);      
        } // for
      } // if  
      this.menu.addElement(sub);
      sub= new DefaultSubMenu("Facturación");
      sub.setIcon("fa fa-qrcode");
      item = new DefaultMenuItem("Desargar");
      item.setIcon("fa fa-download");
      item.setUrl("/Control/descargar.jsf".concat(Constantes.REDIRECIONAR));
      sub.addElement(item);      
      item = new DefaultMenuItem("Generar");
      item.setIcon("fa fa-print");
      item.setUrl("/Control/generar.jsf".concat(Constantes.REDIRECIONAR));
      sub.addElement(item);      
      this.menu.addElement(sub);      
      LOG.error("Menu del portal: "+ this.images);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(columns);
      Methods.clean(marcas);
    } // finally
	}  
  
  private void toLoadCategorias(DefaultSubMenu root, String padre, Integer nivel) {
    List<Entity> categorias   = null;
    Map<String, Object> params= null;
    try {      
      params = new HashMap<>();      
      params.put("nivel", nivel);      
      params.put("nombre", padre);      
      params.put("sortOrder", "order by tc_mantic_productos_marcas.nombre");
      categorias= (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticProductosCategoriasDto", "menu", params);
      if(categorias!= null && !categorias.isEmpty())
        for (Entity categoria: categorias) {
          String nombre= Cadena.letraCapital(categoria.toString("categoria"));
          if(Objects.equals(categoria.toLong("ultimo"), 1L)) {
            DefaultMenuItem item= new DefaultMenuItem(nombre);
            item.setIcon("fa ".concat(categoria.toString("icon")));
            item.setOncomplete("galeriaPrincipal('".concat(categoria.toString("nombre")).concat("', 'CATEGORIA');"));
            root.addElement(item);      
          } // if
          else {
            DefaultSubMenu sub= new DefaultSubMenu(nombre);
            sub.setIcon("fa ".concat(categoria.toString("icon")));
            this.toLoadCategorias(sub, categoria.toString("nombre"), nivel+ 1);
            root.addElement(sub);
          } // else  
        } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(categorias);
    } // finally
  }
}
