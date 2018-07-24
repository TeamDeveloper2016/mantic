package mx.org.kaana.mantic.catalogos.proveedores.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.kajool.reglas.comun.FormatLazyModel;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseFilter;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresPortalesDto;
import mx.org.kaana.mantic.enums.ETiposCuentas;

@Named(value = "manticCatalogosProveedoresPortal")
@ViewScoped
public class Portal extends IBaseFilter implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;
	private FormatLazyModel lazyModelServicios;

	public FormatLazyModel getLazyModelServicios() {
		return lazyModelServicios;
	}	
	
  @PostConstruct
  @Override
  protected void init() {
    try {			
      this.attrs.put("codigo", "");
      this.attrs.put("sucursales", JsfBase.getAutentifica().getEmpresa().getSucursales());      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
	  
	@Override
  public void doLoad() {
		List<UISelectEntity> proveedores= null;
		List<Columna>campos= null;
    try {
			if(!Cadena.isVacio(this.attrs.get("codigo").toString())){
				campos= new ArrayList<>();
				campos.add(new Columna("rfc", EFormatoDinamicos.MAYUSCULAS));
				campos.add(new Columna("razonSocial", EFormatoDinamicos.MAYUSCULAS));
				proveedores= UIEntity.build("VistaProveedoresDto", "portal", this.attrs, campos, Constantes.SQL_TODOS_REGISTROS);
				this.attrs.put("proveedores", proveedores);
				this.attrs.put("proveedor", UIBackingUtilities.toFirstKeySelectEntity(proveedores));
				doLoadPortal();				
			} // if
			else{
				this.attrs.put("proveedores", new ArrayList<>());
				this.attrs.put("proveedor", new UISelectEntity("-1"));
				this.attrs.put("pagina", "");
				this.attrs.put("cuenta", "");
				this.attrs.put("contrasenia", "");
				doLoadServicios();
				doLoadTransferencias();
			} // else
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch    
  } // doLoad  
	
	public void doLoadPortal(){
		MotorBusqueda motor= null;
		Entity proveedor= null;
		TcManticProveedoresPortalesDto portal= null;
		try {
			proveedor= (Entity) this.attrs.get("proveedor");
			motor= new MotorBusqueda(proveedor.getKey());
			portal= motor.toPortal();
			if(portal.isValid()){
				this.attrs.put("pagina", portal.getPagina());
				this.attrs.put("cuenta", portal.getCuenta());
				this.attrs.put("contrasenia", portal.getContrasenia());
			} // if
			else{
				this.attrs.put("pagina", "");
				this.attrs.put("cuenta", "");
				this.attrs.put("contrasenia", "");
			} // else
			doLoadServicios();
			doLoadTransferencias();
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doLoadPortal
	
	public void doLoadServicios(){
		loadBanca(ETiposCuentas.SERVICIOS.getKey());
		UIBackingUtilities.resetDataTable();
	} // doLoadServicios
	
	public void doLoadTransferencias(){
		loadBanca(ETiposCuentas.TRANSFERENCIAS.getKey());
		UIBackingUtilities.resetDataTable("tablaTransferencia");
	} // doLoadTransferencias
	
	private void loadBanca(Long idServicio){
		Map<String, Object>params= null;
		List<Columna>campos      = null;
		try {
			campos= new ArrayList<>();
			campos.add(new Columna("nombre", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("convenioCuenta", EFormatoDinamicos.MAYUSCULAS));
			campos.add(new Columna("clabeReferencia", EFormatoDinamicos.MAYUSCULAS));
			params= new HashMap<>();
			params.put("idProveedor", ((Entity) this.attrs.get("proveedor")).getKey());
			params.put("idTipoCuenta", idServicio);
			if(idServicio.equals(ETiposCuentas.SERVICIOS.getKey()))
				this.lazyModelServicios= new FormatLazyModel("VistaPortalProveedoresDto", "banca", params, campos);
			else	
				this.lazyModel= new FormatLazyModel("VistaPortalProveedoresDto", "banca", params, campos);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(params);
			Methods.clean(campos);
		} // finally		
	} // loadBanca
}
