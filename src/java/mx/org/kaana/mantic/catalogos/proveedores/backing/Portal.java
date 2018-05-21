package mx.org.kaana.mantic.catalogos.proveedores.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.kajool.enums.EFormatoDinamicos;
import mx.org.kaana.kajool.reglas.comun.Columna;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.pagina.IBaseAttribute;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UIEntity;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.mantic.catalogos.proveedores.reglas.MotorBusqueda;
import mx.org.kaana.mantic.db.dto.TcManticProveedoresPortalesDto;

@Named(value = "manticCatalogosProveedoresPortal")
@ViewScoped
public class Portal extends IBaseAttribute implements Serializable {

  private static final long serialVersionUID = 8793667741599428879L;

  @PostConstruct
  @Override
  protected void init() {
    try {			
      this.attrs.put("codigo", "");
      this.attrs.put("sucursales", JsfBase.getAutentifica().getIdsSucursales());      
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init
	  
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
				this.attrs.put("proveedor", -1L);
				this.attrs.put("pagina", "");
				this.attrs.put("cuenta", "");
				this.attrs.put("contrasenia", "");
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
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch		
	} // doLoadPortal
}
