package mx.org.kaana.mantic.ventas.facturas.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import mx.org.kaana.libs.Constantes;
import mx.org.kaana.libs.facturama.reglas.CFDIFactory;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Variables;
import mx.org.kaana.libs.pagina.UIBackingUtilities;
import mx.org.kaana.mantic.facturas.reglas.Transaccion;
import mx.org.kaana.mantic.catalogos.clientes.beans.ClienteTipoContacto;
import mx.org.kaana.mantic.catalogos.comun.MotorBusquedaCatalogos;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.db.dto.TcManticVentasDto;
import mx.org.kaana.mantic.enums.ETiposContactos;
import mx.org.kaana.mantic.facturas.beans.Correo;
import mx.org.kaana.mantic.inventarios.comun.IBaseImportar;
import mx.org.kaana.mantic.ventas.reglas.MotorBusqueda;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 7/05/2018
 *@time 03:29:13 PM
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

@Named(value= "manticVentasFacturasImportar")
@ViewScoped
public class Importar extends IBaseImportar implements Serializable {

	private static final Log LOG              = LogFactory.getLog(Importar.class);
  private static final long serialVersionUID= 327353488565639367L;	
	private Long idFactura;
	private Long idVenta;
	private List<Correo> correos;
	private List<Correo> selectedCorreos;	
	private Correo correo;

	public List<Correo> getCorreos() {
		return correos;
	}

	public List<Correo> getSelectedCorreos() {
		return selectedCorreos;
	}

	public void setSelectedCorreos(List<Correo> selectedCorreos) {
		this.selectedCorreos=selectedCorreos;
	}

	public Correo getCorreo() {
		return correo;
	}

	public void setCorreo(Correo correo) {
		this.correo=correo;
	}
	
	@PostConstruct
  @Override
  protected void init() {		
    try {
			if(JsfBase.getFlashAttribute("idFactura")== null || JsfBase.getFlashAttribute("idVenta")== null)
				UIBackingUtilities.execute("janal.isPostBack('cancelar')");
			else {
        this.idFactura = JsfBase.getFlashAttribute("idFactura")== null? -1L: (Long)JsfBase.getFlashAttribute("idFactura");
        this.idVenta= JsfBase.getFlashAttribute("idVenta")== null? -1L: (Long)JsfBase.getFlashAttribute("idVenta");
				this.attrs.put("factura", DaoFactory.getInstance().findById(TcManticFacturasDto.class, this.idFactura));
				this.attrs.put("venta", DaoFactory.getInstance().findById(TcManticVentasDto.class, this.idVenta));
				this.attrs.put("cliente", DaoFactory.getInstance().findById(TcManticClientesDto.class, ((TcManticVentasDto)this.attrs.get("venta")).getIdCliente()));
			} // if
			this.attrs.put("retorno", JsfBase.getFlashAttribute("retorno")== null? "filtro": JsfBase.getFlashAttribute("retorno"));
			doLoad();			
    } // try
    catch (Exception e) {
      Error.mensaje(e);
      JsfBase.addMessageError(e);
    } // catch		
  } // init

	@Override
	public void doLoad() {
		try {
			this.doLoadImportados("VistaFicticiasDto", "importados", Variables.toMap("idFactura~"+ this.idFactura)); 			
			this.attrs.put("formatos", Constantes.PATRON_IMPORTAR_FACTURA);
			this.attrs.put("xml", ""); 
			this.attrs.put("pdf", ""); 
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // doLoad
	
	public void doViewDocument() {
		this.doViewDocument(Configuracion.getInstance().getPropiedadSistemaServidor("facturama"));
	} // doViewDocument

	public void doViewFile() {
		this.doViewFile(Configuracion.getInstance().getPropiedadSistemaServidor("facturama"));
	} // doViewFile
	
  public String doCancelar() {   
  	JsfBase.setFlashAttribute("idFactura", this.idFactura);
    return ((String)this.attrs.get("retorno")).concat(Constantes.REDIRECIONAR);
  } // doCancelar
	
	public void doReenviar() {
		MotorBusquedaCatalogos motor      = null; 
		List<ClienteTipoContacto>contactos= null;
		try {
			motor= new MotorBusqueda(-1L, ((TcManticVentasDto)this.attrs.get("venta")).getIdCliente());
			contactos= motor.toClientesTipoContacto();
			this.correos= new ArrayList<>();
			for(ClienteTipoContacto contacto: contactos) {
				if(contacto.getIdTipoContacto().equals(ETiposContactos.CORREO.getKey()))
					this.correos.add(new Correo(contacto.getIdClienteTipoContacto(), contacto.getValor()));				
			} // for
			this.correos.add(new Correo(-1L, ""));
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	} // doReenviar
	
	public void doAgregarCorreo() {
		Transaccion transaccion= null;
		try {
			if(!Cadena.isVacio(this.correo.getDescripcion())){
				transaccion= new Transaccion(this.correo, ((TcManticVentasDto)this.attrs.get("venta")).getIdCliente());
				if(transaccion.ejecutar(EAccion.COMPLEMENTAR))
					JsfBase.addMessage("Se agregó el correo electronico correctamente !");
				else
					JsfBase.addMessage("Ocurrió un error al agregar el correo electronico");
			} // if
			else
				JsfBase.addMessage("Es necesario capturar un correo electronico !");
		} // try
		catch (Exception e) {
			JsfBase.addMessageError(e);
			Error.mensaje(e);			
		} // catch		
	} // doAgregarCorreo
	
	public void doSendmail() {
		StringBuilder emails= null;
		String idFacturama  = null;
		try {
			emails= new StringBuilder("");
			if(this.selectedCorreos!= null && !this.selectedCorreos.isEmpty()){
				for(Correo mail: this.selectedCorreos)
					emails.append(mail.getDescripcion()).append(", ");
			} // if
			idFacturama= ((TcManticFacturasDto)this.attrs.get("factura")).getIdFacturama();
			if(emails.length()> 0 && !Cadena.isVacio(idFacturama))
  	    CFDIFactory.getInstance().toSendMail(emails.substring(0, emails.length()- 2), idFacturama);
		} // try
		catch (Exception e) {
			Error.mensaje(e);
			JsfBase.addMessageError(e);
		} // catch
	}	// doSendmail
}