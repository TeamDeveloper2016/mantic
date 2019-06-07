package mx.org.kaana.mantic.catalogos.empaques.backing;

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
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.articulos.reglas.Transaccion;
import mx.org.kaana.mantic.db.dto.TcManticEmpaquesDto;

@Named(value = "manticCatalogosEmpaquesAccion")
@ViewScoped
public class Accion extends IBaseAttribute implements Serializable {

	private static final long serialVersionUID = 6498389869799336830L;  
	
  @PostConstruct
  @Override
  protected void init() {
    try {
      this.attrs.put("accion", JsfBase.getFlashAttribute("accion"));
      this.attrs.put("idEmpaqueUnidadMedida", JsfBase.getFlashAttribute("idEmpaqueUnidadMedida"));
			this.attrs.put("empaqueExistente", true);
			this.attrs.put("unidadExistente", true);
			toLoadCatalog();
      doLoad();      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	private void toLoadCatalog() {
		List<Columna> columns     = null;
    Map<String, Object> params= null;
    try {
			params= new HashMap<>();
			columns= new ArrayList<>();			
			params.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
      columns.add(new Columna("clave", EFormatoDinamicos.MAYUSCULAS));
      columns.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
      this.attrs.put("empaques", (List<UISelectEntity>) UIEntity.build("TcManticEmpaquesDto", "row", params, columns));
			this.attrs.put("idEmpaque", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("empaques")));
      this.attrs.put("unidades", (List<UISelectEntity>) UIEntity.build("TcManticUnidadesMedidasDto", "row", params, columns));
			this.attrs.put("idUnidad", UIBackingUtilities.toFirstKeySelectEntity((List<UISelectEntity>)this.attrs.get("unidades")));
    } // try
    catch (Exception e) {
      throw e;
    } // catch   
    finally {
      Methods.clean(columns);
      Methods.clean(params);
    } // finally
	} // toLoadCatalog
	
  public void doLoad() {
    EAccion eaccion= null;    
    try {
      eaccion= (EAccion) this.attrs.get("accion");			
      this.attrs.put("nombreAccion", Cadena.letraCapital(eaccion.name()));
      switch (eaccion) {
        case AGREGAR:
          this.attrs.put("empaque", DaoFactory.getInstance().findById(TcManticEmpaquesDto.class, ((UISelectEntity)this.attrs.get("idEmpaque")).getKey()));
          this.attrs.put("unidad", DaoFactory.getInstance().findById(TcManticEmpaquesDto.class, ((UISelectEntity)this.attrs.get("idUnidad")).getKey()));
          break;
        case MODIFICAR:
        case CONSULTAR:                  
          break;
      } // switch
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

  public String doAceptar(String accion) {
    Transaccion transaccion= null;
    String regresar        = null;
		EAccion eaccion        = null;
    try {
			eaccion= EAccion.valueOf(accion.toUpperCase());      
      if (transaccion.ejecutar(eaccion)) {
        regresar = "filtro".concat(Constantes.REDIRECIONAR);
        JsfBase.addMessage("Se registro el artículo de forma correcta.", ETipoMensaje.INFORMACION);
      } // if
      else 
        JsfBase.addMessage("Ocurrió un error al registrar el artículo", ETipoMensaje.ERROR);      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch
    return regresar;
  } // doAccion

  public String doCancelar() {    
    return "filtro";
  } // doAccion
}
