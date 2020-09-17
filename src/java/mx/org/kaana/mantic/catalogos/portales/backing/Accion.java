package mx.org.kaana.mantic.catalogos.portales.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelect;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.pagina.UISelectItem;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.portales.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticPortalesDto;

@Named(value = "manticCatalogosPortalesAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = -7668104942302148046L;
  
  private TcManticPortalesDto portal;
  private UISelectEntity ikEmpresa;
  private EAccion eaccion;
  
  public TcManticPortalesDto getPortal() {
    return portal;
  }

  public void setPortal(TcManticPortalesDto portal) {
    this.portal = portal;
  }

  public UISelectEntity getIkEmpresa() {
    return ikEmpresa;
  }

  public void setIkEmpresa(UISelectEntity ikEmpresa) {
    this.ikEmpresa= ikEmpresa;
    if(this.ikEmpresa!= null)
      this.portal.setIdEmpresa(this.ikEmpresa.getKey());
  }
  
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.eaccion= (EAccion)(JsfBase.getFlashAttribute("accion")== null? EAccion.AGREGAR: JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idPortal", JsfBase.getFlashAttribute("idPortal")== null? -1L: JsfBase.getFlashAttribute("idPortal"));
			this.attrs.put("isMatriz", false);
      this.doLoad();      					
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    try {
      this.attrs.put("nombreAccion", Cadena.letraCapital(this.eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.portal= new TcManticPortalesDto();
          break;
        case MODIFICAR:
        case CONSULTAR:
          this.portal= (TcManticPortalesDto)DaoFactory.getInstance().findById(TcManticPortalesDto.class, (Long)this.attrs.get("idPortal"));
          this.setIkEmpresa(new UISelectEntity(this.portal.getIdEmpresa()));
          break;
      } // switch 			
      this.toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {
    Transaccion transaccion = null;
    String regresar = null;
    try {
      if("OTRA".equals(this.portal.getHerramienta()))
        this.portal.setHerramienta((String)this.attrs.get("otraHerramienta"));
      if("OTRA".equals(this.portal.getMarca()))
        this.portal.setMarca((String)this.attrs.get("otraMarca"));
      if("OTRO".equals(this.portal.getModelo()))
        this.portal.setModelo((String)this.attrs.get("otraModelo"));
      this.portal.setIdUsuario(JsfBase.getIdUsuario());
      transaccion = new Transaccion(this.portal);
      if (transaccion.ejecutar(this.eaccion)) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro la empresa de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al registrar la empresa", ETipoMensaje.ERROR);
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar.concat(Constantes.REDIRECIONAR);
  } // doAccion

  public String doCancelar() {
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doAccion

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      List<UISelectEntity> empresas= (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns);
      this.attrs.put("empresas", empresas);
      if(!empresas.isEmpty())
        if(this.eaccion.equals(EAccion.AGREGAR)) 
			    this.setIkEmpresa(new UISelectEntity(-1L));
        else
          this.setIkEmpresa(empresas.get(empresas.indexOf(this.getIkEmpresa())));
      this.doLoadHerramientas();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}
  
  public void doLoadHerramientas() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      List<UISelectItem> herramientas= (List<UISelectItem>) UISelect.seleccione("TcManticPortalesDto", "herramienta", params, "herramienta", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS, "herramienta");
      this.attrs.put("herramientas", herramientas);
      if(!herramientas.isEmpty()) {
        herramientas.add(new UISelectItem("OTRA", "OTRA"));
        if(this.eaccion.equals(EAccion.AGREGAR)) 
			    this.portal.setHerramienta("-1");
        else
          if(this.eaccion.equals(EAccion.MODIFICAR)) 
            herramientas.remove(0);
      } // if  
      this.doLoadMarcas();
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
  }
  
  public void doLoadMarcas() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("herramienta", this.portal.getHerramienta());
      List<UISelectItem> marcas= (List<UISelectItem>) UISelect.seleccione("TcManticPortalesDto", "marca", params, "marca", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS, "marca");
      this.attrs.put("marcas", marcas);
      if(!marcas.isEmpty()) {
        marcas.add(new UISelectItem("OTRA", "OTRA"));
        if(this.eaccion.equals(EAccion.AGREGAR)) 
			    this.portal.setMarca("-1");
        else
          if(this.eaccion.equals(EAccion.MODIFICAR)) 
            marcas.remove(0);
      } // if  
      this.doLoadModelos();
		  if("OTRA".equals(this.portal.getHerramienta()))
        UIBackingUtilities.execute("janal.renovate('contenedorGrupos\\\\:otraHerramienta', {validaciones: 'requerido', mascara: 'alfanumerico', formatos: 'cambiar-mayusculas'});");			
		  else
        UIBackingUtilities.execute("janal.renovate('contenedorGrupos\\\\:otraHerramienta', {validaciones: 'libre', mascara: 'alfanumerico', formatos: 'cambiar-mayusculas'});");		
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
  }

  public void doLoadModelos() {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
    try {
			columns= new ArrayList<>();
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("herramienta", this.portal.getHerramienta());
			params.put("marca", this.portal.getMarca());
      List<UISelectItem> modelos= (List<UISelectItem>) UISelect.seleccione("TcManticPortalesDto", "modelo", params, "modelo", EFormatoDinamicos.MAYUSCULAS, Constantes.SQL_TODOS_REGISTROS, "modelo");
      this.attrs.put("modelos", modelos);
      if(!modelos.isEmpty()) {
        modelos.add(new UISelectItem("OTRO", "OTRO"));
        if(this.eaccion.equals(EAccion.AGREGAR)) 
			    this.portal.setModelo("-1");
        else
          if(this.eaccion.equals(EAccion.MODIFICAR)) 
            modelos.remove(0);
      } // if  
		  if("OTRA".equals(this.portal.getMarca()))
        UIBackingUtilities.execute("janal.renovate('contenedorGrupos\\\\:otraMarca', {validaciones: 'requerido', mascara: 'alfanumerico', formatos: 'cambiar-mayusculas'});");			
		  else
        UIBackingUtilities.execute("janal.renovate('contenedorGrupos\\\\:otraMarca', {validaciones: 'libre', mascara: 'alfanumerico', formatos: 'cambiar-mayusculas'});");		
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
  }
  
  public void doLoadOtroModelo() {
		if("OTRO".equals(this.portal.getModelo()))
      UIBackingUtilities.execute("janal.renovate('contenedorGrupos\\\\:otraModelo', {validaciones: 'requerido', mascara: 'alfanumerico', formatos: 'cambiar-mayusculas'});");			
		else
      UIBackingUtilities.execute("janal.renovate('contenedorGrupos\\\\:otraModelo', {validaciones: 'libre', mascara: 'alfanumerico', formatos: 'cambiar-mayusculas'});");		
  }
  
}
