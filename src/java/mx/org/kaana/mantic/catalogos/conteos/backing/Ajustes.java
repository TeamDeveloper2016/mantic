package mx.org.kaana.mantic.catalogos.conteos.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ESql;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.almacenes.transferencias.beans.Umbral;

@Named(value = "manticCatalogosConteosAjustes")
@ViewScoped
public class Ajustes extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID=-3509185709407306573L;
  
  private Entity conteo;
  private List<Umbral> fuentes;
  
	public Entity getConteo() {
		return conteo;
	}

  public List<Umbral> getFuentes() {
    return fuentes;
  }

	@PostConstruct
	@Override
	protected void init() {
		try {
      this.attrs.put("idConteo", 1L);
      this.toLoadCatalogos();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
	} // init
  
  public void doLoad() {
    Map<String, Object> params    = new HashMap<>();
    UISelectEntity idAlmacen      = (UISelectEntity)this.attrs.get("idAlmacen");
    List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
    try {
      if(!Objects.equals(almacenes, null) && !almacenes.isEmpty()) {
        int index= almacenes.indexOf(idAlmacen);
        if(index>= 0)
          this.attrs.put("idAlmacen", almacenes.get(index));
      } // if  
			params.put("sucursales", this.attrs.get("idEmpresa"));
			params.put("idAlmacen", this.attrs.get("idAlmacen"));
			params.put("idProveedor", -1L);
      if(Cadena.isVacio(this.attrs.get("nombre")))
        params.put("codigoFaltante", "");
      else {
        String nombre= ((String)this.attrs.get("nombre")).replaceAll(Constantes.CLEAN_SQL, "").trim();
        params.put("codigoFaltante", nombre.toUpperCase());
      } // else
      this.fuentes = (List<Umbral>)DaoFactory.getInstance().toEntitySet(Umbral.class, "VistaOrdenesComprasDto", "faltantes", params);
      if(Objects.equals(this.fuentes, null))      
        this.fuentes= new ArrayList<>();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
		finally {
      Methods.clean(params);
    } // finally
  } 

	public String doAceptar() {
		Transaccion transaccion= null;
    String regresar        = null;
		try {
      if(this.toCheck()) {
        transaccion= new Transaccion(this.fuentes);
        if(transaccion.ejecutar(EAccion.REPROCESAR)) {
          JsfBase.addMessage("Se registró de forma correcta la información", ETipoMensaje.INFORMACION);
        } // if
        else
          JsfBase.addMessage("Ocurrió un error al registrar la información", ETipoMensaje.ERROR);
      } // if
      else 
        JsfBase.addMessage("No existe ninguna partida por actualizar", ETipoMensaje.ERROR);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return regresar;
	} 
  
  private Boolean toCheck() {
    Boolean regresar= Boolean.FALSE;
    for (Umbral item: this.fuentes) {
      if(!regresar)
        regresar= Objects.equals(item.getAction(), ESql.UPDATE);
    } // for
    return regresar;
  }
  
  public void doChangeMinimo(Umbral row) {
    row.setAction(ESql.UPDATE);
    row.setIdVerificado(1L);
  }
  
  public void doChangeMaximo(Umbral row) {
    row.setAction(ESql.UPDATE);
    row.setIdVerificado(1L);
  }

  public String toColor(Umbral row) {
    return Objects.equals(row.getAction(), ESql.UPDATE)? "janal-tr-yellow": "";
  }

	private void toLoadCatalogos() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			if(JsfBase.getAutentifica().getEmpresa().isMatriz())
        params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresaDepende());
			else
				params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      this.attrs.put("empresas", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
 			List<UISelectEntity> empresas= (List<UISelectEntity>)this.attrs.get("empresas");
			if(!Objects.equals(empresas, null) && !empresas.isEmpty()) 
				this.attrs.put("idEmpresa", UIBackingUtilities.toFirstKeySelectEntity(empresas));
      else
        this.attrs.put("idEmpresa", new UISelectEntity(-1L));
      this.doLoadAlmacenes();
   } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}
  
	public void doLoadAlmacenes() {
		List<Columna> columns     = new ArrayList<>();
    Map<String, Object> params= new HashMap<>();
    try {
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
			params.put("sucursales", this.attrs.get("idEmpresa"));
      this.attrs.put("almacenes", UIEntity.build("TcManticAlmacenesDto", "almacenes", params, columns));
 			List<UISelectEntity> almacenes= (List<UISelectEntity>)this.attrs.get("almacenes");
			if(!Objects.equals(almacenes, null) && !almacenes.isEmpty()) 
			  this.attrs.put("idAlmacen", UIBackingUtilities.toFirstKeySelectEntity(almacenes));
      else
        this.attrs.put("idAlmacen", new UISelectEntity(-1L));
      this.doLoad();
   } // try
    catch (Exception e) {
      Error.mensaje(e);
			JsfBase.addMessageError(e);
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	}

}

