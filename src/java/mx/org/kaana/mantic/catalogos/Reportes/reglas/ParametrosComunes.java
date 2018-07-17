package mx.org.kaana.mantic.catalogos.Reportes.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.reflection.Methods;

public final class ParametrosComunes implements Serializable{
  
  private Map<String, Object> parametrosComunes; 
  private Long idEmpresa;
  private Long idAlmacen;
  private Long idProveedor;
  private Long idCliente;

  public Map<String, Object> getParametrosComunes() {
    return parametrosComunes;
  }

  public void setParametrosComunes(Map<String, Object> parametrosComunes) {
    this.parametrosComunes = parametrosComunes;
  }

  public ParametrosComunes(Long idEmpresa) throws Exception {
    this.idEmpresa = idEmpresa;
    setParametrosComunes(toDatosEmpresa());
  }

  public ParametrosComunes(Long idEmpresa, Long idAlmacen, Long idProveedor, Long idCliente) throws Exception {
    this.idEmpresa   = idEmpresa;
    this.idAlmacen   = idAlmacen;
    this.idProveedor = idProveedor;
    this.idCliente   = idCliente;
    setParametrosComunes(toDatosEmpresa());
    if(this.idProveedor != -1L)
      toComplementarProveedor();
    if(this.idAlmacen != -1L)
      toComplementarAlmacen();
    if(this.idCliente != -1L)
      toComplementarCliente();
  }
  
  private Map<String, Object> toDatosEmpresa() throws Exception {
    Map<String, Object>regresar = null;
		Map<String, Object>params   = null;
    Entity datosEmpresa         = null;
    try {
      regresar= new HashMap<>();	
      params= new HashMap<>();	
      params.put("idEmpresa", this.idEmpresa);	
      datosEmpresa = (Entity) DaoFactory.getInstance().toEntity("VistaInformacionEmpresas", "datosEmpresa", params);
      if(datosEmpresa != null){
        regresar.put("REPORTE_EMPRESA", datosEmpresa.toString("nombre"));
        regresar.put("REPORTE_EMPRESA_DIRECCION", datosEmpresa.toString("empresaDireccion"));
        regresar.put("REPORTE_EMPRESA_COLONIA", datosEmpresa.toString("colonia"));
        regresar.put("REPORTE_EMPRESA_CP", datosEmpresa.toString("codigoPostal"));
        regresar.put("REPORTE_EMPRESA_CONTACTO", datosEmpresa.toString("responsableEmpresa"));
        regresar.put("REPORTE_EMPRESA_TELEFONOS", datosEmpresa.toString("telefonosEmpresa"));
        regresar.put("REPORTE_EMPRESA_EMAILS", datosEmpresa.toString("emailsEmpresa"));
        regresar.put("REPORTE_EMPRESA_MUNICIPIO", datosEmpresa.toString("empresaRegion"));
        regresar.put("REPORTE_EMPRESA_RFC", datosEmpresa.toString("rfcEmpresa"));
        regresar.put("REPORTE_EMPRESA_CLAVE", datosEmpresa.toString("clave"));
      }
    } // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally {
			Methods.clean(params);
		} // finally
		return regresar;		
	}
  
  public void toComplementarAlmacen() throws Exception {
		Map<String, Object>params   = null;
    Entity datosAlmacen         = null;
		try {
      params= new HashMap<>();	
      params.put("idAlmacen", this.idAlmacen);	
			datosAlmacen = (Entity) DaoFactory.getInstance().toEntity("VistaInformacionEmpresas", "datosAlmacen", params);
      if(datosAlmacen != null){
        this.parametrosComunes.put("REPORTE_ALMACEN_DIRECCION", datosAlmacen.toString("almacenDireccion"));
        this.parametrosComunes.put("REPORTE_ALMACEN_COLONIA", datosAlmacen.toString("colonia"));
        this.parametrosComunes.put("REPORTE_ALMACEN_CP", datosAlmacen.toString("codigoPostal"));
        this.parametrosComunes.put("REPORTE_ALMACEN_CONTACTO", datosAlmacen.toString("responsableAlmacen"));
        this.parametrosComunes.put("REPORTE_ALMACEN_TELEFONOS", datosAlmacen.toString("telefonosAlmacen"));
        this.parametrosComunes.put("REPORTE_ALMACEN_EMAILS", datosAlmacen.toString("emailsAlmacen"));
        this.parametrosComunes.put("REPORTE_ALMACEN_MUNICIPIO", datosAlmacen.toString("almacenRegion"));
      }
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
    finally {
			Methods.clean(params);
		} // finally
	} // toEmpresa
  
  public void toComplementarProveedor() throws Exception {
		Map<String, Object>params   = null;
    Entity datosProveedor         = null;
		try {
      params= new HashMap<>();	
      params.put("idProveedor", this.idProveedor);	
			datosProveedor = (Entity) DaoFactory.getInstance().toEntity("VistaInformacionEmpresas", "datosProveedor", params);
      if(datosProveedor != null){
        this.parametrosComunes.put("REPORTE_PROVEEDOR", datosProveedor.toString("razonSocial"));
        this.parametrosComunes.put("REPORTE_PROVEEDOR_DIRECCION", datosProveedor.toString("proveedorDireccion"));
        this.parametrosComunes.put("REPORTE_PROVEEDOR_COLONIA", datosProveedor.toString("colonia"));
        this.parametrosComunes.put("REPORTE_PROVEEDOR_CP", datosProveedor.toString("codigoPostal"));
        this.parametrosComunes.put("REPORTE_PROVEEDOR_CONTACTO", datosProveedor.toString("agente"));
        this.parametrosComunes.put("REPORTE_PROVEEDOR_TELEFONOS", datosProveedor.toString("telefonosProveedor"));
        this.parametrosComunes.put("REPORTE_PROVEEDOR_EMAILS", datosProveedor.toString("emailsProveedor"));
        this.parametrosComunes.put("REPORTE_PROVEEDOR_MUNICIPIO", datosProveedor.toString("proveedorRegion"));
        this.parametrosComunes.put("REPORTE_PROVEEDOR_RFC", datosProveedor.toString("rfc"));
        this.parametrosComunes.put("REPORTE_PROVEEDOR_CLAVE", datosProveedor.toString("clave"));
      }
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
    finally {
			Methods.clean(params);
		} // finally
	} // toComplementarProveedor
  
  public void toComplementarCliente() throws Exception {
		Map<String, Object>params   = null;
    Entity datosProveedor         = null;
		try {
      params= new HashMap<>();	
      params.put("idCliente", this.idCliente);	
			datosProveedor = (Entity) DaoFactory.getInstance().toEntity("VistaInformacionEmpresas", "datosCliente", params);
      if(datosProveedor != null){
        this.parametrosComunes.put("REPORTE_CLIENTE", datosProveedor.toString("razonSocial"));
        this.parametrosComunes.put("REPORTE_CLIENTE_DIRECCION", datosProveedor.toString("clienteDireccion"));
        this.parametrosComunes.put("REPORTE_CLIENTE_COLONIA", datosProveedor.toString("colonia"));
        this.parametrosComunes.put("REPORTE_CLIENTE_CP", datosProveedor.toString("codigoPostal"));
        this.parametrosComunes.put("REPORTE_CLIENTE_CONTACTO", datosProveedor.toString("agente"));
        this.parametrosComunes.put("REPORTE_CLIENTE_TELEFONOS", datosProveedor.toString("telefonosCliente"));
        this.parametrosComunes.put("REPORTE_CLIENTE_EMAILS", datosProveedor.toString("emailsCliente"));
        this.parametrosComunes.put("REPORTE_CLIENTE_MUNICIPIO", datosProveedor.toString("clienteRegion"));
        this.parametrosComunes.put("REPORTE_CLIENTE_RFC", datosProveedor.toString("rfc"));
        this.parametrosComunes.put("REPORTE_CLIENTE_CLAVE", datosProveedor.toString("clave"));
      }
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
    finally {
			Methods.clean(params);
		} // finally
	} // toComplementarCliente

  @Override
  protected void finalize() throws Throwable {
    Methods.clean(this.parametrosComunes);
    super.finalize(); 
  }
  
}
