package mx.org.kaana.mantic.catalogos.clientes.reglas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.db.comun.sql.Entity;
import mx.org.kaana.kajool.db.comun.sql.Value;
import mx.org.kaana.kajool.enums.ETipoMensaje;
import mx.org.kaana.kajool.template.backing.Reporte;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.libs.reflection.Methods;
import mx.org.kaana.libs.wassenger.Bonanza;
import mx.org.kaana.mantic.catalogos.reportes.reglas.Parametros;
import mx.org.kaana.mantic.comun.ParametrosReporte;
import mx.org.kaana.mantic.correos.beans.Attachment;
import mx.org.kaana.mantic.correos.enums.ECorreos;
import mx.org.kaana.mantic.correos.reglas.IBaseAttachment;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.EReportes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 28/05/2021
 *@time 07:17:11 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class NotificaCliente implements Serializable {
  
  private static final Log LOG = LogFactory.getLog(NotificaCliente.class);
  private static final long serialVersionUID = -7421927886876449153L;
  
  private Session sesion;
  private Long idCliente;
  private Long idVenta;
  private Long idClientePagoControl;
  private String razonSocial;
  private String correos;
  private String celulares;
  private ECorreos correo;
  private EReportes reportes;
  private Reporte reporte;
  private Reporte voucher;
  private boolean notifica;
  private boolean existe;
  private boolean ticket;

  public NotificaCliente(Session sesion, Long idCliente, EReportes reportes, ECorreos correo) {
    this(sesion, idCliente, null, null, reportes, correo, false);
  }
  
  public NotificaCliente(Session sesion, Long idCliente, EReportes reportes, ECorreos correo, Long idVenta) {
    this(sesion, idCliente, null, null, reportes, correo, false, idVenta);
  }
  
  public NotificaCliente(Session sesion, Long idCliente, String razonSocial, String correos, EReportes reportes, ECorreos correo) {
    this(sesion, idCliente, razonSocial, correos, reportes, correo, false);
  }
  
  public NotificaCliente(Long idCliente, String razonSocial, String correos, EReportes reportes, ECorreos correo, boolean notifica) {
    this(null, idCliente, razonSocial, correos, reportes, correo, notifica);
  }
  
  public NotificaCliente(Long idCliente, String razonSocial, EReportes reportes, ECorreos correo, boolean notifica) {
    this(null, idCliente, razonSocial, null, reportes, correo, notifica);
  }
  
  public NotificaCliente(Session sesion, Long idCliente, String razonSocial, EReportes reportes, ECorreos correo) {
    this(sesion, idCliente, razonSocial, null, reportes, correo, false);
  }
  
  public NotificaCliente(Session sesion, Long idCliente, String razonSocial, EReportes reportes, ECorreos correo, boolean notifica) {
    this(sesion, idCliente, razonSocial, null, reportes, correo, notifica);
  }
  
  public NotificaCliente(Session sesion, Long idCliente, String razonSocial, String correos, EReportes reportes, ECorreos correo, boolean notifica) {
    this(sesion, idCliente, razonSocial, correos, reportes, correo, notifica, -1L);
  }
  
  public NotificaCliente(Session sesion, Long idCliente, String razonSocial, String correos, EReportes reportes, ECorreos correo, boolean notifica, Long idVenta) {
    this.sesion     = sesion;
    this.idCliente  = idCliente;
    this.razonSocial= razonSocial== null? this.toRazonSocial(): razonSocial;
    this.correos    = correos== null? this.toTipoContacto("correos"): correos;
    this.reportes   = reportes;
    this.correo     = correo;
    this.notifica   = notifica;
    this.idClientePagoControl= -1L;
    this.celulares  = this.toTipoContacto("celulares");
    this.idVenta    = idVenta;
    this.existe     = Boolean.FALSE;
    this.ticket     = Boolean.FALSE;
  }

  public void setIdClientePagoControl(Long idClientePagoControl) {
    this.idClientePagoControl = idClientePagoControl;
  }

  private void toReporteIndividal() throws Exception {
    Parametros comunes           = null;
		Map<String, Object>params    = null;
		Map<String, Object>parametros= null;
    this.existe                  = Boolean.FALSE;
		try{		
      params= new HashMap<>();
      params.put("idEmpresa", JsfBase.getAutentifica().getEmpresa().getSucursales());
      params.put("idCliente", this.idCliente);
      switch(this.reportes) {
        case CUENTAS_POR_COBRAR:
          params.put(Constantes.SQL_CONDICION, "tc_mantic_clientes_deudas.id_cliente= "+ this.idCliente+ " and tc_mantic_clientes_deudas.id_cliente_estatus in (1, 2)");
          params.put("sortOrder", "order by	tc_mantic_clientes.razon_social,tc_mantic_clientes_deudas.registro desc");
          break;
        case PAGOS_CUENTAS_POR_COBRAR:
          params.put("idClientePagoControl", this.idClientePagoControl);
          params.put("sortOrder", "order by	tc_mantic_clientes_pagos.registro");
          break;
      } // switch
      comunes     = new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa(), -1L, -1L, this.idCliente);
      parametros  = comunes.getComunes();
      this.reporte= new Reporte();	
      this.reporte.init();
      parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
      parametros.put("NOMBRE_REPORTE", this.reportes.getTitulo());
      parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));			
      this.reporte.toAsignarReporte(new ParametrosReporte(this.reportes, params, parametros));		
      if(this.sesion!= null)
        this.reporte.toProcess(this.sesion);			
      else 
        this.reporte.doAceptarSimple();			
      this.existe= !Cadena.isVacio(this.reporte.getNombre());
    } // try
    catch(Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);			
    } // catch	
  } // toReporteIndividal  
  
  private void toReporteTicket() throws Exception {
    Parametros comunes           = null;
		Map<String, Object>params    = new HashMap<>();
		Map<String, Object>parametros= null;
    TcManticVentasDto venta      = null; 
    EReportes individual         = EReportes.TICKET_VENTA;
    this.ticket                  = Boolean.FALSE;
		try {	 	
      if(this.sesion!= null)
        venta= (TcManticVentasDto)DaoFactory.getInstance().findById(sesion, TcManticVentasDto.class, this.idVenta);
      else
        venta= (TcManticVentasDto)DaoFactory.getInstance().findById(TcManticVentasDto.class, this.idVenta);
      params.put("idVenta", this.idVenta);
      params.put("sortOrder", "order by tc_mantic_ventas.id_empresa, tc_mantic_clientes.id_cliente, tc_mantic_ventas.ejercicio, tc_mantic_ventas.orden");
      comunes     = new Parametros(JsfBase.getAutentifica().getEmpresa().getIdEmpresa());
      parametros  = comunes.getComunes();
      this.voucher= new Reporte();	
      this.voucher.init();
			parametros.put("REPORTE_EMPRESA", JsfBase.getAutentifica().getEmpresa().getNombreCorto());
		  parametros.put("ENCUESTA", JsfBase.getAutentifica().getEmpresa().getNombre().toUpperCase());
			parametros.put("NOMBRE_REPORTE", individual.getTitulo());
			parametros.put("REPORTE_ICON", JsfBase.getRealPath("").concat("resources/iktan/icon/acciones/"));		
      Encriptar encriptado= new Encriptar();
      String codigo= encriptado.encriptar(Fecha.formatear(Fecha.CODIGO_SEGURIDAD, venta.getRegistro()));
			parametros.put("REPORTE_CODIGO_SEGURIDAD", codigo);			
      this.voucher.toAsignarReporte(new ParametrosReporte(individual, params, parametros));		
      if(this.sesion!= null)
        this.voucher.toProcess(this.sesion);			
      else 
        this.voucher.doAceptarSimple();			
      this.ticket= !Cadena.isVacio(this.reporte.getNombre());
    } // try
    catch(Exception e) {
      throw e;		
    } // catch	
		finally {
			Methods.clean(params);
		} // finally    
  } // toReporteTicket  
  
  private void toSendMailIndividual() {
		Map<String, Object> params= new HashMap<>();
		String[] emails= {(this.correos.length()> 0? this.correos.substring(0, this.correos.length()- 2): "")};
		List<Attachment> files= new ArrayList<>(); 
    Attachment documento  = null;
    Attachment comprobante= null;
		try {
			params.put("header", "...");
			params.put("footer", "...");
			params.put("empresa", JsfBase.getAutentifica().getEmpresa().getNombre());
			params.put("tipo", "Estado de Cuenta");
			params.put("razonSocial", this.razonSocial);
			params.put("correo", this.correo.getEmail());
			params.put("url", Configuracion.getInstance().getPropiedadServidor("sistema.dns"));
			this.toReporteIndividal();
			documento= new Attachment(this.reporte.getNombre(), Boolean.FALSE);
			files.add(documento);
      if(this.idVenta> 0L) {
  			this.toReporteTicket();
  			comprobante= new Attachment(this.voucher.getNombre(), Boolean.FALSE);
	  		files.add(comprobante);
      } // if
			files.add(new Attachment("logo", this.correo.getImages().concat("logo.png"), Boolean.TRUE));
			params.put("attach", documento.getId());
			for (String item: emails) {
				try {
					if(!Cadena.isVacio(item)) {
					  IBaseAttachment notificar= new IBaseAttachment(this.correo, this.correo.getEmail(), item, "controlbonanza@gmail.com", "Ferreteria Bonanza - Estado de cuenta", params, files);
					  LOG.info("Enviando correo a la cuenta: "+ item);
					  notificar.send();
					} // if	
				} // try
				finally {
				  if(documento.getFile().exists()) 
   	  	    LOG.info("Eliminando archivo temporal: "+ documento.getAbsolute());
				  if(comprobante!= null && comprobante.getFile().exists()) 
   	  	    LOG.info("Eliminando archivo temporal: "+ comprobante.getAbsolute());
				} // finally	
			} // for
	  	LOG.info("Se envio el correo de forma exitosa");
			if(this.correos.length()> 0)
		    addMessage("Se envió el correo de forma exitosa.", ETipoMensaje.INFORMACION);
			else
		    addMessage("No se selecciono ningún correo, por favor verifiquelo e intente de nueva cuenta.", ETipoMensaje.ALERTA);
		} // try // try
		catch(Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
		finally {
			Methods.clean(files);
		} // finally    
  }
  
  public void doSendMail() {
    if(!Cadena.isVacio(this.correos))
      this.toSendMailIndividual();
  }
  
  public void doSendWhatsup() {
    StringBuilder alias= new StringBuilder();
    try {
      if(!Cadena.isVacio(this.celulares)) {
        if(!this.existe)
          this.toReporteIndividal();
        alias.append(this.reporte.getAlias());
        if(this.idVenta> 0L) {
          if(!this.ticket)
            this.toReporteTicket();
          alias.append("\\n\\n*Ticket de venta:*\\nhttps://ferreteriabonanza.com/Temporal/Pdf/").append(this.voucher.getAlias());
        } // if  
        Bonanza notificar= new Bonanza(this.razonSocial, "celular", alias.toString(), "ticket", "fecha");
        String[] phones= this.celulares.substring(0, this.celulares.length()- 2).split("[,]");
        for (String phone: phones) {
          notificar.setCelular(phone, Boolean.TRUE);
          LOG.info("Enviando mensaje por whatsup al celular: "+ phone);
          switch(this.correo) {
            case DEVOLUCION:
              notificar.doSendDevolucion(sesion);
              break;
            case PAGOS:
              notificar.doSendPagoCuenta(sesion);
              break;
            case CREDITO:
              notificar.doSendDevolucion(sesion);
              break;
          } // switch
        } // for
      } // if  
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
  }
  
  private void addMessage(String descripcion, ETipoMensaje tipoMensaje) {
    if(this.notifica)     
	    JsfBase.addMessage(descripcion, tipoMensaje);
  }

  private String toTipoContacto(String tipo) {
    int count                 = 0;
    StringBuilder emails      = new StringBuilder();
    StringBuilder email       = new StringBuilder();
    List<Entity> items        = null;
    Map<String, Object> params= null;
    try {      
      params = new HashMap<>();      
      params.put("idCliente", this.idCliente);      
      if(this.sesion!= null)
        items= DaoFactory.getInstance().toEntitySet(this.sesion, "TrManticClienteTipoContactoDto", tipo, params);
      else
        items= DaoFactory.getInstance().toEntitySet("TrManticClienteTipoContactoDto", tipo, params);
      if(items!= null && !items.isEmpty())
        for (Entity item: items) {
          if(Objects.equals(item.toLong("idPreferido"), 1L))
            emails.append(item.toString("valor")).append(", ");
          if(count< 10)
            email.append(item.toString("valor")).append(", ");
          count++;
        } // for
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return emails.length()== 0? email.toString(): emails.toString();
  } 
  
  private String toRazonSocial() {
    String regresar= "";
    Value value    = null; 
    Map<String, Object> params= null;
    try {      
      params = new HashMap<>();      
      params.put("idCliente", this.idCliente);      
      if(this.sesion!= null)
        value= DaoFactory.getInstance().toField(this.sesion, "TcManticClientesDto", "key", params, "razonSocial");
      else
        value= DaoFactory.getInstance().toField("TcManticClientesDto", "key", params, "razonSocial");
      if(value!= null && value.getData()!= null)
        regresar= value.toString();
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);      
    } // catch	
    finally {
      Methods.clean(params);
    } // finally
    return regresar;
  } 
  
  public static void main(String ... args) {
    String[] phones= "1,2,3".split("[,]");
    for (String phone : phones) {
      LOG.info("Cadena: "+ phone);
    } // for
  }
  
}
