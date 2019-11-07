package mx.org.kaana.mantic.facturas.comun;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.libs.pagina.UISelectEntity;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.correos.beans.Attachment;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import mx.org.kaana.mantic.correos.reglas.IBaseAttachment;
import mx.org.kaana.mantic.enums.EReportes;
import mx.org.kaana.mantic.facturas.beans.Correo;
import mx.org.kaana.mantic.facturas.reglas.Transferir;
import mx.org.kaana.mantic.ventas.comun.IBaseTicket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.context.RequestContext;

public abstract class FiltroFactura extends IBaseTicket {

	private static final Log LOG              = LogFactory.getLog(FiltroFactura.class);
	private static final long serialVersionUID= 2049728714303120753L;
	
	private List<Correo> correos;
	private List<Correo> selectedCorreos;	
	private Correo correo;
	protected Reporte reporte;
	
	public Reporte getReporte() {
		return reporte;
	}	// getReporte
	
	public List<Correo> getCorreos() {
		return correos;
	}

	public void setCorreos(List<Correo> correos) {
		this.correos = correos;
	}	
	
	public List<Correo> getSelectedCorreos() {
		return selectedCorreos;
	}

	public void setSelectedCorreos(List<Correo> selectedCorreos) {
		this.selectedCorreos = selectedCorreos;
	}	

	public Correo getCorreo() {
		return correo;
	}

	public void setCorreo(Correo correo) {
		this.correo = correo;
	}
	
	public void initBase(){
		this.correos= new ArrayList<>();
		this.selectedCorreos= new ArrayList<>();
	}
	
	public void doSendmail() {
		try {
			StringBuilder emails= new StringBuilder("");
			if(this.selectedCorreos!= null && !this.selectedCorreos.isEmpty()){
				for(Correo mail: this.selectedCorreos){
					if(!Cadena.isVacio(mail.getDescripcion()))
						emails.append(mail.getDescripcion()).append(", ");
				} // for
			} // if
			String idFacturama= ((Entity)this.attrs.get("seleccionado")).toString("idFacturama");
			if(emails.length()> 0 && !Cadena.isVacio(idFacturama)){
  	    CFDIFactory.getInstance().toSendMail(emails.substring(0, emails.length()- 2), idFacturama);
				JsfBase.addMessage("Reenviar factura", "Se realizo el reenvio de factura de forma correcta.");
			} // if
			else
				JsfBase.addMessage("Reenviar factura", "Es necesario seleccionar un correo electronico.");
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally{
			this.selectedCorreos= new ArrayList<>();
		} // finally
	}	// doSendmail
	
	public void doSendMail() {
		File factura= null;
		StringBuilder sb= new StringBuilder("");
		if(this.selectedCorreos!= null && !this.selectedCorreos.isEmpty()) {
			for(Correo mail: this.selectedCorreos) {
				if(!Cadena.isVacio(mail.getDescripcion()))
					sb.append(mail.getDescripcion()).append(", ");
			} // for
		} // if
		Map<String, Object> params= new HashMap<>();
		//String[] emails= {"jimenez76@yahoo.com", (sb.length()> 0? sb.substring(0, sb.length()- 2): "")};
		String[] emails= {(sb.length()> 0? sb.substring(0, sb.length()- 2): "")};
		List<Attachment> files= new ArrayList<>(); 
		try {
			Entity seleccionado= (Entity)this.attrs.get("seleccionado");
			params.put("header", "...");
			params.put("footer", "...");
			params.put("empresa", JsfBase.getAutentifica().getEmpresa().getNombre());
			params.put("tipo", "Factura");			
			params.put("razonSocial", seleccionado.toString("cliente"));
			params.put("correo", ECorreos.FACTURACION.getEmail());			
			factura= this.toXml(seleccionado.toLong("idFactura"));
			this.doReporte("FACTURAS_FICTICIAS_DETALLE", true);
			Attachment attachments= new Attachment(this.reporte.getNombre(), Boolean.FALSE);
			files.add(attachments);
			files.add(new Attachment(factura, Boolean.FALSE));
			files.add(new Attachment("logo", ECorreos.FACTURACION.getImages().concat("logo.png"), Boolean.TRUE));
			params.put("attach", attachments.getId());
			for (String item: emails) {
				try {
					if(!Cadena.isVacio(item)) {
					  IBaseAttachment notificar= new IBaseAttachment(ECorreos.FACTURACION, ECorreos.FACTURACION.getEmail(), item, "controlbonanza@gmail.com,jorge.alberto.vs.10@gmail.com", "Ferreteria Bonanza - Factura", params, files, ECorreos.FACTURACION.getAlias());
					  LOG.info("Enviando correo a la cuenta: "+ item);
					  notificar.send();
					} // if	
				} // try
				finally {
				  if(attachments.getFile().exists()) {
   	  	    LOG.info("Eliminando archivo temporal: "+ attachments.getAbsolute());
				    // user.getFile().delete();
				  } // if	
				} // finally	
			} // for
	  	LOG.info("Se envio el correo de forma exitosa");
			if(sb.length()> 0)
		    JsfBase.addMessage("Se envió el correo de forma exitosa.", ETipoMensaje.INFORMACION);
			else
		    JsfBase.addMessage("No se selecciono ningún correo, por favor verifiquelo e intente de nueva cuenta.", ETipoMensaje.ALERTA);
		} // try // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(files);
		} // finally
	} // doSendMail	
	
	protected File toXml(Long idFactura) throws Exception{
		File regresar            = null;
		List<Entity> facturas    = null;
		Map<String, Object>params= null;
		try {
			params= new HashMap<>();
			params.put("idFactura", idFactura);
			facturas= DaoFactory.getInstance().toEntitySet("VistaFicticiasDto", "importados", params);
			for(Entity factura: facturas){
				if(factura.toLong("idTipoArchivo").equals(1L))
					regresar= new File(factura.toString("ruta").concat(factura.toString("nombre")));
				else
					this.attrs.put("nameFacturaPdf", factura.toString("nombre"));
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		finally{
			Methods.clean(params);
		} // finally
		return regresar;
	} // toXml
	
	protected void doReporte(String nombre, boolean email) throws Exception {
		Parametros comunes = null;
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
		EReportes reporteSeleccion   = null;
    Entity seleccionado          = null;
		try {		
      params= this.toPrepare();	
      seleccionado = ((Entity)this.attrs.get("seleccionado"));
			//recuperar el sello digital en caso de que la factura ya fue timbrada para que salga de forma correcta el reporte
			if(seleccionado.toString("idFacturama")!= null && seleccionado.toString("selloSat")== null) {
				Transferir transferir= null;
				try {
          transferir= new Transferir(seleccionado.toString("idFacturama"));
				  transferir.ejecutar(EAccion.PROCESAR);
				} // try
        catch(Exception e) {
					LOG.warn("La factura ["+ seleccionado.toLong("idFactura")+ "] presento un problema al recuperar el sello digital ["+ seleccionado.toString("idFacturama")+"]");
          Error.mensaje(e);
				} // catch
				finally {
					transferir= null;
				} // finally
			} // if
      //es importante este orden para los grupos en el reporte	
      params.put("sortOrder", "order by tc_mantic_ventas.id_empresa, tc_mantic_clientes.id_cliente, tc_mantic_ventas.ejercicio, tc_mantic_ventas.orden");
      reporteSeleccion= EReportes.valueOf(nombre);
      if(!reporteSeleccion.equals(EReportes.FACTURAS_FICTICIAS)) {
        params.put("idFicticia", seleccionado.getKey());
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(),-1L, -1L, seleccionado.toLong("idCliente"));
      } // if
      else
        comunes= new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      this.reporte= JsfBase.toReporte();	
      parametros= comunes.getComunes();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", reporteSeleccion.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			      			
			if(email) { 
				this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros), this.attrs.get("nameFacturaPdf").toString().replaceFirst(".pdf", ""));		
        this.reporte.doAceptarSimple();			
			} // if
			else {				
				this.reporte.toAsignarReporte(new ParametrosReporte(reporteSeleccion, params, parametros));		
				this.doVerificarReporte();
				this.reporte.setPrevisualizar(true);
				this.reporte.doAceptar();			
			} // else
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  } // doReporte
	
	public boolean doVerificarReporte() {
    boolean regresar = false;
		RequestContext rc= UIBackingUtilities.getCurrentInstance();
		if(this.reporte.getTotal()> 0L) {
			rc.execute("start(" + this.reporte.getTotal() + ")");	
      regresar = true;
    }
		else {
			rc.execute("generalHide();");		
			JsfBase.addMessage("Reporte", "No se encontraron registros para el reporte", ETipoMensaje.ERROR);
      regresar = false;
		} // else
    return regresar;
	} // doVerificarReporte	
	
	protected Map<String, Object> toPrepare() {
	  Map<String, Object> regresar= new HashMap<>();	
		StringBuilder sb= new StringBuilder();
		if(!Cadena.isVacio(JsfBase.getParametro("codigo_input"))) 
	 	  sb.append("tc_mantic_ventas_detalles.codigo regexp '.*").append(JsfBase.getParametro("codigo_input").replaceAll(Constantes.CLEAN_SQL, "").replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");
		if(!Cadena.isVacio(JsfBase.getParametro("articulo_input")))
  		sb.append("(upper(tc_mantic_ventas_detalles.nombre) like upper('%").append(JsfBase.getParametro("articulo_input")).append("%')) and ");
		if(!Cadena.isVacio(this.attrs.get("razonSocial")) && !this.attrs.get("razonSocial").toString().equals("-1"))
			sb.append("tc_mantic_clientes.id_cliente = ").append(((Entity)this.attrs.get("razonSocial")).getKey()).append(" and ");					
		else if(!Cadena.isVacio(JsfBase.getParametro("razonSocial_input"))) 
			 	 sb.append("tc_mantic_clientes.razon_social regexp '.*").append(JsfBase.getParametro("razonSocial_input").replaceAll(Constantes.CLEAN_SQL, "").replaceAll("(,| |\\t)+", ".*.*")).append(".*' and ");
		if(!Cadena.isVacio(this.attrs.get("facturama")) && !this.attrs.get("facturama").toString().equals("-1")) {
			if(((Long)this.attrs.get("facturama"))== 1L)
  		  sb.append("(tc_mantic_facturas.folio is not null) and ");
			else
  		  sb.append("(tc_mantic_ventas.id_venta is null) and ");
		} // if
		if(!Cadena.isVacio(this.attrs.get("idFicticia")) && !this.attrs.get("idFicticia").toString().equals("-1"))
  		sb.append("(tc_mantic_ventas.id_venta=").append(this.attrs.get("idFicticia")).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("consecutivo")))
  		sb.append("(tc_mantic_ventas.ticket like '%").append(this.attrs.get("consecutivo")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("folio")))
  		sb.append("(tc_mantic_facturas.folio like '%").append(this.attrs.get("folio")).append("%') and ");
		if(!Cadena.isVacio(this.attrs.get("fechaInicio")))
		  sb.append("((date_format(tc_mantic_facturas.timbrado, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("') or (date_format(tc_mantic_facturas.cancelada, '%Y%m%d')>= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaInicio"))).append("')) and ");			
		if(!Cadena.isVacio(this.attrs.get("fechaTermino")))
		  sb.append("((date_format(tc_mantic_facturas.timbrado, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("') or (date_format(tc_mantic_facturas.cancelada, '%Y%m%d')<= '").append(Fecha.formatear(Fecha.FECHA_ESTANDAR, (Date)this.attrs.get("fechaTermino"))).append("')) and ");			
		if(!Cadena.isVacio(this.attrs.get("montoInicio")))
		  sb.append("(tc_mantic_ventas.total>= ").append((Double)this.attrs.get("montoInicio")).append(") and ");			
		if(!Cadena.isVacio(this.attrs.get("montoTermino")))
		  sb.append("(tc_mantic_ventas.total<= ").append((Double)this.attrs.get("montoTermino")).append(") and ");			
		if(!Cadena.isVacio(this.attrs.get("idTipoDocumento")) && !this.attrs.get("idTipoDocumento").toString().equals("-1"))
  		sb.append("(tc_mantic_ventas.id_tipo_documento=").append(this.attrs.get("idTipoDocumento")).append(") and ");
		if(this.attrs.get("idVentaEstatus")!= null && !((UISelectEntity) this.attrs.get("idVentaEstatus")).getKey().equals(-1L))
  		sb.append("(tc_mantic_ventas.id_venta_estatus= ").append(((UISelectEntity) this.attrs.get("idVentaEstatus")).getKey()).append(") and ");
		if(!Cadena.isVacio(this.attrs.get("idEmpresa")) && !this.attrs.get("idEmpresa").toString().equals("-1"))
		  regresar.put("idEmpresa", this.attrs.get("idEmpresa"));
		else
		  regresar.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
		if(sb.length()== 0)
		  regresar.put(Constantes.SQL_CONDICION, Constantes.SQL_VERDADERO);
		else	
		  regresar.put(Constantes.SQL_CONDICION, sb.substring(0, sb.length()- 4));
		return regresar;		
	} // toPrepare
	
	@Override
	protected void finalize() throws Throwable {
    super.finalize();
		Methods.clean(this.correos);
		Methods.clean(this.selectedCorreos);
	}	// finalize
}
