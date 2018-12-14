package mx.org.kaana.mantic.catalogos.listasprecios.backing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.listasprecios.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticListasPreciosDto;

@javax.inject.Named("manticCatalogosListaPreciosFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements java.io.Serializable
{
  private static final long serialVersionUID = 862453337895123687L;
  
  public Filtro() {}
  
  @PostConstruct
  protected void init(){
    try {
      attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
  }
  
  public void doLoadProveedores() {
    Map<String, Object> params = null;
    List<UISelectEntity> proveedores = null;
    try {
      params = new HashMap();
      params.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());
      proveedores = UIEntity.build("TcManticProveedoresDto", "sucursales", params);
      attrs.put("proveedores", proveedores);
      attrs.put("idProveedor", new UISelectEntity("-1"));
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
    finally {
      Methods.clean(params);
    }
  }
  
  public void doLoad(){
    List<Columna> campos = null;
    Map<String, Object> params = null;
    try {
      params = toPrepare();
      campos = new ArrayList();
      params.put("sortOrder", "order by tc_mantic_proveedores.razon_social");
      campos.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("observaciones", EFormatoDinamicos.MAYUSCULAS));
      campos.add(new Columna("registro", EFormatoDinamicos.FECHA_HORA));
      lazyModel = new FormatCustomLazy("VistaListasArchivosDto", "lazy", params, campos);
      mx.org.kaana.libs.pagina.UIBackingUtilities.resetDataTable();
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
    finally {
      Methods.clean(campos);
      Methods.clean(params);
    }
  }
  
  public String doAgregarListaProveedor() {
    JsfBase.setFlashAttribute("retorno", "filtro");
    JsfBase.setFlashAttribute("idListaPrecio", -1L);
    return "importar".concat("?faces-redirect=true");
  }
  
  public void doEliminar() {
    Transaccion transaccion = null;
    try {
      transaccion = new Transaccion(new TcManticListasPreciosDto(((Entity)attrs.get("seleccionado")).getKey()));
      transaccion.ejecutar(EAccion.ELIMINAR);
      JsfBase.addMessage("Eliminar lista de precios proveedor", "La lista de precios de el proveedor se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
    }
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    }
  }
  
  public String doImportar() {
    JsfBase.setFlashAttribute("retorno", "filtro");
    JsfBase.setFlashAttribute("idListaPrecio", ((Entity)attrs.get("seleccionado")).getKey());
    return "importar".concat(Constantes.REDIRECIONAR);
  }

	public String doBuscarArticulo() {
    JsfBase.setFlashAttribute("retorno", "filtro");
    return "articulos".concat(Constantes.REDIRECIONAR);
  }
  
  private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("clave")))
  		sb.append("tc_mantic_proveedores.rfc like '%").append(this.attrs.get("clave")).append("%'");
		if(!Cadena.isVacio(this.attrs.get("razonSocial")))
  		sb.append((!Cadena.isVacio(this.attrs.get("clave"))?" and ":" ").concat("tc_mantic_proveedores.razon_social like '%")).append(this.attrs.get("razonSocial")).append("%'");
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb);
		return regresar;
	}
  
}
