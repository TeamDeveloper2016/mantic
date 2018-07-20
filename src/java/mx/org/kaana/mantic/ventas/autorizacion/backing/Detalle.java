package mx.org.kaana.mantic.ventas.autorizacion.backing;

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
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.reflection.Methods;

@Named(value= "manticVentasAutorizacionDetalle")
@ViewScoped
public class Detalle extends IBaseFilter implements Serializable {

	private static final long serialVersionUID = -7073204002310261565L;  		
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
      this.attrs.put("idVenta", JsfBase.getFlashAttribute("idVenta"));
			this.attrs.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
			loadDetalleVenta();
			doLoad();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	@Override
  public void doLoad() {
		List<Columna> campos= null;
    try {      
			campos= new ArrayList<>();
			this.lazyModel= new FormatLazyModel("TcManticVentasDetallesDto", "detalleVenta", this.attrs, campos);
			UIBackingUtilities.resetDataTable();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // doLoad

	private void loadDetalleVenta(){
		Entity venta             = null;
		Map<String, Object>params= null;
		List<Columna> campos     = null;
		try {
			params= new HashMap<>();			
			campos= new ArrayList<>();
			campos.add(new Columna("cliente", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			params.put("idVenta", this.attrs.get("idVenta"));
			venta= (Entity) DaoFactory.getInstance().toEntity("VistaVentasDto", "lazyVenta", params);			
			this.attrs.put("ventaDetalle", venta);			
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
	} // loadDetalleVenta
	
  public String doCancelar() {   
    return "filtro".concat(Constantes.REDIRECIONAR);
  } // doCancelar		
}