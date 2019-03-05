package mx.org.kaana.mantic.catalogos.reportes.reglas;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.libs.reflection.Methods;

public final class Parametros implements Serializable {

	private static final long serialVersionUID=-7778494802183548883L;
  
  private Map<String, Object> comunes; 
  private Long idEmpresa;
  private Long idAlmacen;
  private Long idAlmacenDestino;
  private Long idProveedor;
  private Long idCliente;

  public Parametros(Long idEmpresa) throws Exception {
    this.idEmpresa = idEmpresa;
    setComunes(toDatosEmpresa());
  }

  public Parametros(Long idEmpresa, Long idAlmacen, Long idAlmacenDestino) throws Exception {
    this.idEmpresa = idEmpresa;
    this.idAlmacen = idAlmacen;
    this.idAlmacenDestino = idAlmacenDestino;
    setComunes(toDatosEmpresa());
    if(this.idAlmacen != -1L)
      toComplementarAlmacen(this.idAlmacen, true);
    if(this.idAlmacenDestino != -1L)
      toComplementarAlmacen(this.idAlmacenDestino, false);
  }
  
  public Parametros(Long idEmpresa, Long idAlmacen, Long idProveedor, Long idCliente) throws Exception {
    this.idEmpresa   = idEmpresa;
    this.idAlmacen   = idAlmacen;
    this.idProveedor = idProveedor;
    this.idCliente   = idCliente;
    setComunes(toDatosEmpresa());
    if(this.idProveedor != -1L)
      toComplementarProveedor();
    if(this.idAlmacen != -1L)
      toComplementarAlmacen(this.idAlmacen, true);
    if(this.idCliente != -1L)
      toComplementarCliente();
  }
  
  public Map<String, Object> getComunes() {
    return comunes;
  }

  public void setComunes(Map<String, Object> comunes) {
    this.comunes = comunes;
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
        regresar.put("REPORTE_EMPRESA",datosEmpresa.toString("nombre")!=null? datosEmpresa.toString("nombre"):" ");
        regresar.put("REPORTE_EMPRESA_DIRECCION", datosEmpresa.toString("empresaDireccion")!=null?datosEmpresa.toString("empresaDireccion"):" ");
        regresar.put("REPORTE_EMPRESA_COLONIA", datosEmpresa.toString("colonia")!=null?datosEmpresa.toString("colonia"):" ");
        regresar.put("REPORTE_EMPRESA_CP", datosEmpresa.toString("nombre")!=null?datosEmpresa.toString("codigoPostal"):" ");
        regresar.put("REPORTE_EMPRESA_CONTACTO", datosEmpresa.toString("codigoPostal")!=null?datosEmpresa.toString("responsableEmpresa"):" ");
        //regresar.put("REPORTE_EMPRESA_CONTACTO", "JOSE ANTONIO DAVALOS PADILLA");
        regresar.put("REPORTE_EMPRESA_TELEFONOS", datosEmpresa.toString("telefonosEmpresa")!=null?datosEmpresa.toString("telefonosEmpresa"):" ");
        regresar.put("REPORTE_EMPRESA_EMAILS", datosEmpresa.toString("emailsEmpresa")!=null?datosEmpresa.toString("emailsEmpresa"):" ");
        regresar.put("REPORTE_EMPRESA_MUNICIPIO", datosEmpresa.toString("empresaRegion")!=null?datosEmpresa.toString("empresaRegion"):" ");
        regresar.put("REPORTE_EMPRESA_RFC", datosEmpresa.toString("rfcEmpresa")!=null?datosEmpresa.toString("rfcEmpresa"):" ");
        //regresar.put("REPORTE_EMPRESA_RFC", "DAPA580118TK4");
        regresar.put("REPORTE_EMPRESA_CLAVE", datosEmpresa.toString("clave")!=null?datosEmpresa.toString("clave"):" ");
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
  
  public void toComplementarAlmacen(Long idKeyAlmacen, boolean isOrigen) throws Exception {
		Map<String, Object>params   = null;
    Entity datosAlmacen         = null;
		try {
      params= new HashMap<>();	
      params.put("idAlmacen", idKeyAlmacen);	
			datosAlmacen = (Entity) DaoFactory.getInstance().toEntity("VistaInformacionEmpresas", "datosAlmacen", params);
      if(datosAlmacen != null && isOrigen){
        this.comunes.put("REPORTE_ALMACEN_CLAVE", datosAlmacen.toString("clave")!=null? datosAlmacen.toString("clave"):" ");
        this.comunes.put("REPORTE_ALMACEN_DIRECCION", datosAlmacen.toString("almacenDireccion")!=null? datosAlmacen.toString("almacenDireccion"):" ");
        this.comunes.put("REPORTE_ALMACEN_COLONIA", datosAlmacen.toString("colonia")!=null? datosAlmacen.toString("colonia"):" ");
        this.comunes.put("REPORTE_ALMACEN_CP", datosAlmacen.toString("codigoPostal")!=null? datosAlmacen.toString("codigoPostal"):" ");
        this.comunes.put("REPORTE_ALMACEN_CONTACTO", datosAlmacen.toString("responsableAlmacen")!=null? datosAlmacen.toString("responsableAlmacen"):" ");
        this.comunes.put("REPORTE_ALMACEN_TELEFONOS", datosAlmacen.toString("telefonosAlmacen")!=null? datosAlmacen.toString("telefonosAlmacen"):" ");
        this.comunes.put("REPORTE_ALMACEN_EMAILS", datosAlmacen.toString("emailsAlmacen")!=null? datosAlmacen.toString("emailsAlmacen"):" ");
        this.comunes.put("REPORTE_ALMACEN_MUNICIPIO", datosAlmacen.toString("almacenRegion")!=null? datosAlmacen.toString("almacenRegion"):" ");
      } 
      else if(datosAlmacen != null && !isOrigen){
        this.comunes.put("REPORTE_ALMACEN_CLAVE_DESTINO", datosAlmacen.toString("clave")!=null? datosAlmacen.toString("clave"):" ");
        this.comunes.put("REPORTE_ALMACEN_DIRECCION_DESTINO", datosAlmacen.toString("almacenDireccion")!=null? datosAlmacen.toString("almacenDireccion"):" ");
        this.comunes.put("REPORTE_ALMACEN_COLONIA_DESTINO", datosAlmacen.toString("colonia")!=null? datosAlmacen.toString("colonia"):" ");
        this.comunes.put("REPORTE_ALMACEN_CP_DESTINO", datosAlmacen.toString("codigoPostal")!=null? datosAlmacen.toString("codigoPostal"):" ");
        this.comunes.put("REPORTE_ALMACEN_CONTACTO_DESTINO", datosAlmacen.toString("responsableAlmacen")!=null? datosAlmacen.toString("responsableAlmacen"):" ");
        this.comunes.put("REPORTE_ALMACEN_TELEFONOS_DESTINO", datosAlmacen.toString("telefonosAlmacen")!=null? datosAlmacen.toString("telefonosAlmacen"):" ");
        this.comunes.put("REPORTE_ALMACEN_EMAILS_DESTINO", datosAlmacen.toString("emailsAlmacen")!=null? datosAlmacen.toString("emailsAlmacen"):" ");
        this.comunes.put("REPORTE_ALMACEN_MUNICIPIO_DESTINO", datosAlmacen.toString("almacenRegion")!=null? datosAlmacen.toString("almacenRegion"):" ");
      }
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
    finally {
			Methods.clean(params);
		} // finally
	} // toComplementarAlmacen
  
  public void toComplementarProveedor() throws Exception {
		Map<String, Object>params   = null;
    Entity datosProveedor         = null;
		try {
      params= new HashMap<>();	
      params.put("idProveedor", this.idProveedor);	
			datosProveedor = (Entity) DaoFactory.getInstance().toEntity("VistaInformacionEmpresas", "datosProveedor", params);
      if(datosProveedor != null){
        this.comunes.put("REPORTE_PROVEEDOR", datosProveedor.toString("razonSocial")!=null? datosProveedor.toString("razonSocial"):" ");
        this.comunes.put("REPORTE_PROVEEDOR_DIRECCION", datosProveedor.toString("proveedorDireccion")!=null? datosProveedor.toString("proveedorDireccion"):" ");
        this.comunes.put("REPORTE_PROVEEDOR_COLONIA", datosProveedor.toString("colonia")!=null? datosProveedor.toString("colonia"):" ");
        this.comunes.put("REPORTE_PROVEEDOR_CP", datosProveedor.toString("codigoPostal")!=null? datosProveedor.toString("codigoPostal"):" ");
        this.comunes.put("REPORTE_PROVEEDOR_CONTACTO", datosProveedor.toString("agente")!=null? datosProveedor.toString("agente"):" ");
        this.comunes.put("REPORTE_PROVEEDOR_TELEFONOS", datosProveedor.toString("telefonosProveedor")!=null? datosProveedor.toString("telefonosProveedor"):" ");
        this.comunes.put("REPORTE_PROVEEDOR_EMAILS", datosProveedor.toString("emailsProveedor")!=null? datosProveedor.toString("emailsProveedor"):" ");
        this.comunes.put("REPORTE_PROVEEDOR_MUNICIPIO", datosProveedor.toString("proveedorRegion")!=null? datosProveedor.toString("proveedorRegion"):" ");
        this.comunes.put("REPORTE_PROVEEDOR_RFC", datosProveedor.toString("rfc")!=null? datosProveedor.toString("rfc"):" ");
        this.comunes.put("REPORTE_PROVEEDOR_CLAVE", datosProveedor.toString("clave")!=null? datosProveedor.toString("clave"):" ");
      }
		} // try // try
		catch (Exception e) {			
			throw e;
		} // catch	
    finally {
			Methods.clean(params);
		} // finally
	} // toComplementarProveedor
  
  public void toComplementarCliente() throws Exception {
		Map<String, Object>params   = null;
    Entity datosCiente          = null;
		try {
      params= new HashMap<>();	
      params.put("idCliente", this.idCliente);	
			datosCiente = (Entity) DaoFactory.getInstance().toEntity("VistaInformacionEmpresas", "datosCliente", params);
      if(datosCiente != null){
        this.comunes.put("REPORTE_CLIENTE", datosCiente.toString("razonSocial")!=null? datosCiente.toString("razonSocial"):" ");
        this.comunes.put("REPORTE_CLIENTE_DIRECCION", datosCiente.toString("clienteDireccion")!=null? datosCiente.toString("clienteDireccion"):" ");
        this.comunes.put("REPORTE_CLIENTE_COLONIA", datosCiente.toString("colonia")!=null? datosCiente.toString("colonia"):" ");
        this.comunes.put("REPORTE_CLIENTE_CP", datosCiente.toString("codigoPostal")!=null? datosCiente.toString("codigoPostal"):" ");
        this.comunes.put("REPORTE_CLIENTE_TELEFONOS", datosCiente.toString("telefonosCliente")!=null? datosCiente.toString("telefonosCliente"):" ");
        this.comunes.put("REPORTE_CLIENTE_EMAILS", datosCiente.toString("emailsCliente")!=null? datosCiente.toString("emailsCliente"):" ");
        this.comunes.put("REPORTE_CLIENTE_MUNICIPIO", datosCiente.toString("clienteRegion")!=null? datosCiente.toString("clienteRegion"):" ");
        this.comunes.put("REPORTE_CLIENTE_RFC", datosCiente.toString("rfc")!=null? datosCiente.toString("rfc"):" ");
        this.comunes.put("REPORTE_CLIENTE_CLAVE", datosCiente.toString("clave")!=null? datosCiente.toString("clave"):" ");
      }
		} // try // try
		catch (Exception e) {			
			throw e;
		} // catch	
    finally {
			Methods.clean(params);
		} // finally
	} // toComplementarCliente

  public void finalize() throws Throwable {
    Methods.clean(this.comunes);
    super.finalize(); 
  }
  
}
