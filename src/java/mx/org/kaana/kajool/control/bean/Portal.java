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
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.reportes.FileSearch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Collections;
import org.primefaces.model.menu.DefaultMenuColumn;
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

  private MenuModel megaCategorias;
  private MenuModel menu;
  private MenuModel individual;
  private MenuModel categorias;
  private List<String> images;
  private List<String> nombres;
  private String pathImage;

  static {
    mutex = new Object();
  }

  private Portal() {
    String dns= Configuracion.getInstance().getPropiedadServidor("sistema.dns");
    this.pathImage= dns.substring(0, dns.lastIndexOf("/")+ 1).concat(Configuracion.getInstance().getEtapaServidor().name().toLowerCase()).concat("/galeria/");
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

  public MenuModel getCategorias() {
    return categorias;
  }
  
  public MenuModel getMegaCategorias() {
    return this.megaCategorias;
  }

  public MenuModel getIndividual() {
    return individual;
  }

  public List<String> getNombres() {
    return Collections.unmodifiableList(nombres);
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
      this.toLoadMenuContext();
      this.toLoadMegaCategorias();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
  }
  
	private void toLoadMenu() {
    List<Entity> marcas       = null;
    Map<String, Object> params= new HashMap<>();
    try {      
      this.nombres       = new ArrayList<>();
      params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);      
      params.put("sortOrder", "order by tc_mantic_productos_marcas.nombre");
      marcas = (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticProductosMarcasDto", "lazy", params);
      // MENU DINAMICO
      this.menu = new DefaultMenuModel();
      DefaultMenuItem item = new DefaultMenuItem("Inicio");
      item.setIcon("fa fa-home");
      item.setUrl("/indice.jsf".concat(Constantes.REDIRECIONAR));
      this.menu.addElement(item);      
      DefaultSubMenu sub= new DefaultSubMenu("Categorías");
      sub.setIcon("fa fa-language");
      this.toLoadCategorias(sub, "null", 2, "galeriaPrincipal");
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
      item = new DefaultMenuItem("Descargar");
      item.setIcon("fa fa-download");
      item.setUrl("/Control/descargar.jsf".concat(Constantes.REDIRECIONAR));
      sub.addElement(item);      
      item = new DefaultMenuItem("Generar");
      item.setIcon("fa fa-print");
      item.setUrl("/Control/generar.jsf".concat(Constantes.REDIRECIONAR));
      sub.addElement(item);      
      this.menu.addElement(sub);      
      LOG.error("Menu del portal: "+ this.images);
      this.toLoadIndividual(marcas);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(marcas);
    } // finally
	}  
  
  private void toLoadIndividual(List<Entity> marcas) {
    try {
      this.individual = new DefaultMenuModel();
      DefaultSubMenu sub= new DefaultSubMenu("Categorías");
      sub.setIcon("fa fa-language");
      this.toLoadCategorias(sub, "null", 2, "galeriaIndividual");
      this.individual.addElement(sub);
      sub= new DefaultSubMenu("Marcas");
      sub.setIcon("fa fa-picture-o");
      if(marcas!= null && !marcas.isEmpty()) {
        for (Entity entity: marcas) {
          DefaultMenuItem item= new DefaultMenuItem(entity.toString("descripcion"));
          item.setIcon("fa fa-picture-o");
          item.setOncomplete("galeriaIndividual('".concat(entity.toString("descripcion")).concat("', 'MARCA');"));
          sub.addElement(item);      
        } // for
      } // if  
      this.individual.addElement(sub);
      LOG.error("Menu individual: "+ this.individual);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
  }
  
  private void toLoadCategorias(DefaultSubMenu root, String padre, Integer nivel, String metodo) {
    List<Entity> items        = null;
    Map<String, Object> params= null;
    try {      
      params = new HashMap<>();      
      params.put("nivel", nivel);      
      params.put("nombre", padre);      
      params.put("sortOrder", "order by tc_mantic_productos_marcas.nombre");
      items= (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticProductosCategoriasDto", "menu", params);
      if(items!= null && !items.isEmpty())
        for (Entity categoria: items) {
          String nombre= Cadena.letraCapital(categoria.toString("categoria"));
          if(Objects.equals(categoria.toLong("ultimo"), 1L)) {
            DefaultMenuItem item= new DefaultMenuItem(nombre);
            item.setIcon("fa ".concat(categoria.toString("icon")));
            item.setOncomplete(metodo.concat("('").concat(categoria.toString("nombre")).concat("', 'CATEGORIA');"));
            root.addElement(item);      
          } // if
          else {
            DefaultSubMenu sub= new DefaultSubMenu(nombre);
            sub.setStyleClass("categoria janal-".concat(categoria.toString("nombre").replaceAll("[ ]", "_")));
            sub.setIcon("fa ".concat(categoria.toString("icon")));
            this.toLoadCategorias(sub, categoria.toString("nombre"), nivel+ 1, metodo);
            root.addElement(sub);
          } // else  
          this.nombres.add(categoria.toString("categoria"));
        } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(items);
    } // finally
  }
  
  private void toLoadMenuContext() {
    List<Entity> items= null;
    DefaultSubMenu sub= null; 
    try {      
      sub= new DefaultSubMenu("Categorías");
      sub.setIcon("fa fa-language");
      this.categorias= new DefaultMenuModel();
      items = (List<Entity>)DaoFactory.getInstance().toEntitySet("TcManticProductosCategoriasDto", "ultimo", java.util.Collections.EMPTY_MAP, Constantes.SQL_TODOS_REGISTROS);
      if(items!= null && !items.isEmpty()) {
        for (Entity categoria: items) {
          String nombre= Cadena.letraCapital(categoria.toString("categoria"));
          DefaultMenuItem item= new DefaultMenuItem(nombre);
          item.setIcon("fa ".concat(categoria.toString("icon")));
          item.setOncomplete("galeriaPrincipal('".concat(categoria.toString("nombre")).concat("', 'CATEGORIA');"));
          sub.addElement(item);      
        } // for
      } // if
      this.categorias.addElement(sub);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
    finally {
      Methods.clean(items);
    } // finally
  }

  private void toLoadMegaCategorias() {
    List<Entity> clasificacion= null;
    DefaultSubMenu categoria  = null;
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("nivel", 2);      
      params.put("nombre", "null");      
      // MENU DINAMICO
      this.megaCategorias= new DefaultMenuModel();
      clasificacion= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "categorias", params);
      if(clasificacion!= null && !clasificacion.isEmpty())
        for (Entity item: clasificacion) {
          String nombre= Cadena.letraCapital(item.toString("categoria"));
          categoria    = new DefaultSubMenu(nombre);
          categoria.setIcon("fa ".concat(item.toString("icon")));
          categoria.addElement(this.toLoadMarcas(item));
          categoria.addElement(this.toLoadDivisiones(item));
          categoria.addElement(this.toLoadImagen(item));
          this.megaCategorias.addElement(categoria);
        } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(clasificacion);
    } // finally    
  } 
  
  public DefaultMenuColumn toLoadMarcas(Entity categoria) {
    DefaultMenuColumn regresar= new DefaultMenuColumn();
    List<Entity> marcas       = null;
    Map<String, Object> params= null;
    try {      
      params= new HashMap<>();      
      params.put("categoria", categoria.toString("nombre"));      
      marcas= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "marcas", params);
      DefaultSubMenu subMenu= new DefaultSubMenu("MARCA");
      subMenu.setIcon("fa fa-language");
      if(marcas!= null && !marcas.isEmpty()) {
        int count= 0;
        for (Entity item: marcas) {      
          DefaultMenuItem marca= new DefaultMenuItem(item.toString("marca"));
          marca.setIcon("fa fa-twitter");
          marca.setOncomplete("galeriaPrincipal('".concat(item.toString("marca")).concat("', 'MARCA');"));
          subMenu.addElement(marca);
          if(count++> 9)
            break;
        } // for
//        if(marcas.size()> 9) {
          DefaultMenuItem marca= new DefaultMenuItem("VER MAS ... ");
          marca.setIcon("fa fa-fire");
          marca.setOncomplete("marcasPrincipal('".concat(categoria.toString("nombre")).concat("', 'MARCA');"));
          subMenu.addElement(marca);
//        } // if  
      } // if
      regresar.addElement(subMenu);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(marcas);
    } // finally
    return regresar;
  }
  
  public DefaultMenuColumn toLoadDivisiones(Entity categoria) {
    DefaultMenuColumn regresar= new DefaultMenuColumn();
    List<Entity> divisiones   = null;
    Map<String, Object> params= new HashMap<>();
    try {      
      params.put("categoria", categoria.toString("nombre").concat(Constantes.SEPARADOR));      
      divisiones= (List<Entity>)DaoFactory.getInstance().toEntitySet("VistaProductosDto", "divisiones", params);
      DefaultSubMenu subMenu= new DefaultSubMenu("DIVISION");
      subMenu.setIcon("fa fa-language");
      if(divisiones!= null && !divisiones.isEmpty()) {
        int count= 0;
        for (Entity item: divisiones) {      
          DefaultMenuItem division= new DefaultMenuItem(item.toString("categoria"));
          division.setIcon("fa fa-yelp");
          division.setOncomplete("galeriaPrincipal('".concat(item.toString("nombre")).concat("', 'CATEGORIA');"));
          subMenu.addElement(division);
          if(count++> 9)
            break;
        } // for
//        if(divisiones.size()> 9) {
          DefaultMenuItem division= new DefaultMenuItem("VER MAS ...");
          division.setIcon("fa fa-fire");
          division.setOncomplete("divisionesPrincipal('".concat(categoria.toString("nombre")).concat(Constantes.SEPARADOR).concat("', 'CATEGORIA');"));
          subMenu.addElement(division);
//        } // if  
      } // if  
      regresar.addElement(subMenu);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
      Methods.clean(divisiones);
    } // finally
    return regresar;
  }
  
  public DefaultMenuColumn toLoadImagen(Entity categoria) {
    DefaultMenuColumn regresar= new DefaultMenuColumn();
    try {      
      DefaultSubMenu subMenu= new DefaultSubMenu(categoria.toString("categoria"));
      subMenu.setIcon("fa fa-language");
      regresar.addElement(subMenu);
      DefaultMenuItem img= new DefaultMenuItem("<img src=\"".concat(this.pathImage.concat("1/categorias/").concat(categoria.toString("imagen"))).concat("?pfdrid_c=true\" width=\"290px\" height=\"220px\" style=\"cursor:pointer;\" onclick=\"galeriaPrincipal('").concat(categoria.toString("nombre")).concat("', 'CATEGORIA');\"/>"));
      img.setEscape(Boolean.FALSE);
      subMenu.addElement(img);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    return regresar;
  }
  
}
