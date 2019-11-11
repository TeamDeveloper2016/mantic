package mx.org.kaana.mantic.catalogos.cajas.backing;

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
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatCustomLazy;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.cajas.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticCajasDto;

@Named(value = "manticCatalogosCajasFiltro")
@ViewScoped
public class Filtro extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 7539514569874123596L;

  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("clave", "");
      this.attrs.put("nombre", "");        
      this.attrs.put("isMatriz", JsfBase.getAutentifica().getEmpresa().isMatriz());
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.attrs.put("isDeleted", isDeleted());
      this.attrs.put("sortOrder", "order by tc_mantic_cajas.clave");
			this.toLoadCatalog();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

  @Override
  public void doLoad() {
    List<Columna> columns     = null;
		Map<String, Object> params= this.toPrepare();
    try {
      columns = new ArrayList<>();
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));                 
      columns.add(new Columna("empresa", EFormatoDinamicos.MAYUSCULAS));                 
      columns.add(new Columna("nombreEmpresa", EFormatoDinamicos.MAYUSCULAS));                 
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));                    
      columns.add(new Columna("limite", EFormatoDinamicos.MONEDA_SAT_DECIMALES));   
      columns.add(new Columna("registro", EFormatoDinamicos.FECHA_CORTA));
      columns.add(new Columna("activa", EFormatoDinamicos.MAYUSCULAS));
      this.lazyModel = new FormatCustomLazy("VistaCierresCajasDto", "consultar", params, columns);
      this.attrs.put("isDeleted", isDeleted());
      UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    finally {
      Methods.clean(columns);
    } // finally		
  } // doLoad	
	
	private Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(this.attrs.get("clave")))
  		sb.append("(tc_mantic_cajas.clave like '%").append(this.attrs.get("clave")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("nombre")))
  		sb.append("(tc_mantic_cajas.nombre like '%").append(this.attrs.get("nombre")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	}
	
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
      this.attrs.put("sucursales", (List<UISelectEntity>) UIEntity.build("TcManticEmpresasDto", "empresas", params, columns));
    } // try
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    }// finally
	}	
	
  public String doAccion(String accion) {
    EAccion eaccion= null;
		try {
			eaccion= EAccion.valueOf(accion.toUpperCase());
			JsfBase.setFlashAttribute("accion", eaccion);		
			JsfBase.setFlashAttribute("idCaja", (eaccion.equals(EAccion.MODIFICAR) || eaccion.equals(EAccion.CONSULTAR)) ? ((Entity)this.attrs.get("seleccionado")).getKey() : -1L);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch
		return "accion".concat(Constantes.REDIRECIONAR);
  } // doAccion

  public void doEliminar() {
		Transaccion transaccion = null;
		Entity seleccionado     = null;
    TcManticCajasDto dto    = null;
		try {
			seleccionado= (Entity) this.attrs.get("seleccionado");	
      if(DaoFactory.getInstance().toField("TcManticCajasDto", "cuantas", this.attrs , "total").toInteger()>1){
        this.attrs.put("idCaja", seleccionado.getKey());       
        dto=(TcManticCajasDto) DaoFactory.getInstance().findFirst(TcManticCajasDto.class,"unica",this.attrs);
        transaccion= new Transaccion(dto ,seleccionado.getKey());
        if(transaccion.ejecutar(EAccion.ELIMINAR))
          JsfBase.addMessage("Eliminar Caja", "La caja se ha eliminado correctamente.", ETipoMensaje.INFORMACION);
        else
          JsfBase.addMessage("Eliminar Caja", "Ocurrió un error al eliminar la caja.", ETipoMensaje.ERROR);		
      }
      else
        JsfBase.addMessage("Eliminar Caja", "No puede existir menos de 1 caja por sucursal.", ETipoMensaje.ERROR);	
      this.attrs.put("isDeleted", isDeleted());
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch			
  } // doEliminar
  
  public boolean isDeleted() throws Exception{
    boolean regresar = false;
    try{
      regresar = (DaoFactory.getInstance().toField("TcManticCajasDto", "cuantas", this.attrs , "total").toInteger()>1);
    }
    catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);			
		} // catch		
    return regresar;
  } 
}
