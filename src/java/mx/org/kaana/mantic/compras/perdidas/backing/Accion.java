package mx.org.kaana.mantic.compras.perdidas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.procesos.acceso.beans.Faltante;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.db.dto.TcManticFaltantesDto;


@Named(value = "manticComprasPerdidasAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 327393488565639367L;

	private Faltante faltante;

	public Faltante getFaltante() {
		return faltante;
	}

	public void setFaltante(Faltante faltante) {
		this.faltante=faltante;
	}

	@PostConstruct
  @Override
  protected void init() {		
    try {
      if(JsfBase.getFlashAttribute("accion")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
			this.attrs.put("codigo", "");
			this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idFaltante", JsfBase.getFlashAttribute("idFaltante")== null? -1L: JsfBase.getFlashAttribute("idFaltante"));
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno"));
			this.doLoad();
			this.toLoadCatalog();	
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  public void doLoad() {
    EAccion eaccion= null;
    try {
      eaccion= (EAccion) this.attrs.get("accion");
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:											
    			this.faltante= new Faltante(JsfBase.getIdUsuario(), -1L, "", 1D, 1L, -1L, JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
          break;
        case MODIFICAR:					
        case CONSULTAR:					
          this.faltante= (Faltante)DaoFactory.getInstance().toEntity(Faltante.class, "VistaFaltantesDto", "igual", Variables.toMap("idFaltante~"+ (Long)this.attrs.get("idFaltante")));
          break;
      } // switch
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar() {  
    String regresar= null;
    try {			
      TcManticFaltantesDto existe= (TcManticFaltantesDto)DaoFactory.getInstance().findFirst(TcManticFaltantesDto.class, "existe", this.faltante.toMap());
			if(existe== null) {
				if(DaoFactory.getInstance().insert(this.faltante)> 0L) {
	  			JsfBase.addMessage("Agregado:", "El articulo fue agregado a la relación de faltantes. !", ETipoMensaje.INFORMACION);
  				this.faltante= new Faltante(JsfBase.getIdUsuario(), -1L, "", 1D, 1L, -1L, this.faltante.getIdEmpresa());
				} // if	
		  }	// if
			else {
				existe.setCantidad(existe.getCantidad()+ this.faltante.getCantidad());
				if(DaoFactory.getInstance().update(existe)> 0L) {
	  			JsfBase.addMessage("Agregado:", "El articulo fue actualizado en la relación de faltantes. !", ETipoMensaje.INFORMACION);
  				this.faltante= new Faltante(JsfBase.getIdUsuario(), -1L, "", 1D, 1L, -1L, this.faltante.getIdEmpresa());
				} // if	
			} // else	
			UIBackingUtilities.update("@(.faltantes)");
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {   
    return this.attrs.get("retorno")== null? "filtro": (String)this.attrs.get("retorno");
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
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.attrs.put("empresas", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
			List<UISelectEntity> empresas= (List<UISelectEntity>)this.attrs.get("empresas");
			if(empresas!= null && this.faltante!= null && this.faltante.getIdEmpresa()> 0) 
				this.faltante.setIkEmpresa(new UISelectEntity(new Entity(this.faltante.getIdEmpresa())));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}

	public List<UISelectEntity> doCompleteArticulo(String query) {
		List<Columna> columns     = null;
    Map<String, Object> params= new HashMap<>();
		boolean buscaPorCodigo    = false;
    try {
			columns= new ArrayList<>();
      columns.add(new Columna("propio", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idAlmacen", JsfBase.getAutentifica().getEmpresa().getIdAlmacen());
  		params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
  		params.put("idProveedor", this.attrs.get("proveedor")== null? new UISelectEntity(new Entity(-1L)): ((UISelectEntity)this.attrs.get("proveedor")).getKey());
			String search= new String(query); 
			if(!Cadena.isVacio(search)) {
  			search= search.replaceAll(Constantes.CLEAN_SQL, "").trim();
				buscaPorCodigo= search.startsWith(".");
				if(buscaPorCodigo)
					search= search.trim().substring(1);
				search= search.toUpperCase().replaceAll("(,| |\\t)+", ".*.*");
			} // if	
			else
				search= "WXYZ";
  		params.put("codigo", search);
			if(buscaPorCodigo)
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porCodigo", params, columns, 20L));
			else
        this.attrs.put("articulos", (List<UISelectEntity>) UIEntity.build("VistaOrdenesComprasDto", "porNombre", params, columns, 20L));
		} // try
	  catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
		return (List<UISelectEntity>)this.attrs.get("articulos");
	}	

  public void doReplaceFaltante() {
		try {
    	List<UISelectEntity> articulos= (List<UISelectEntity>)this.attrs.get("articulos");
	    UISelectEntity articulo= (UISelectEntity)this.attrs.get("articulo");
			if(articulo== null)
			  articulo= new UISelectEntity(new Entity(-1L));
			else
				if(articulos.indexOf(articulo)>= 0) 
					articulo= articulos.get(articulos.indexOf(articulo));
			  else
			    articulo= articulos.get(0);
			if(articulo.size()> 0) {
			  this.faltante.setIdArticulo(articulo.toLong("idArticulo"));
			  this.faltante.setCodigo(articulo.toString("propio"));
			  this.faltante.setNombre(articulo.toString("nombre"));
			} // if	
		} // try
	  catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
	}

}