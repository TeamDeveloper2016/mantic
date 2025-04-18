package mx.org.kaana.libs.facturama.reglas;

import java.io.File;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.facturama.models.Client;
import mx.org.kaana.libs.facturama.models.Product;
import mx.org.kaana.libs.facturama.models.response.Cfdi;
import mx.org.kaana.libs.facturama.models.response.Complement;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturamaBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.enums.EEstatusFacturas;
import mx.org.kaana.mantic.facturas.beans.ArticuloFactura;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class TransaccionFactura extends IBaseTnx {

	private static final Log LOG                 = LogFactory.getLog(TransaccionFactura.class);
	private static final String REGISTRO_CLIENTE = "REGISTRO DE CLIENTE";
	private static final String REGISTRO_ARTICULO= "REGISTRO DE ARTICULO";
	private static final String REGISTRO_CFDI    = "REGISTRO DE CFDI";
	private ClienteFactura cliente;
	private ArticuloFactura articulo;	
	private List<ArticuloFactura> articulos;	
	private String idFacturamaRegistro;

	public TransaccionFactura() {		
	} // Transaccion

	public TransaccionFactura(ClienteFactura cliente) {
		this(cliente, null);
	} // Transaccion
	
	public TransaccionFactura(ArticuloFactura articulo) {
		this(null, articulo);
	} // Transaccion
	
	public TransaccionFactura(ClienteFactura cliente, ArticuloFactura articulo) {
		this.cliente = cliente;
		this.articulo= articulo;
	} // Transaccion
	
	public TransaccionFactura(List<ArticuloFactura> articulos, ClienteFactura cliente) {
		this.cliente  = cliente;
		this.articulos= articulos;
	} // Transaccion

	public void setCliente(ClienteFactura cliente) {
		this.cliente = cliente;
	} // setCliente

	public ClienteFactura getCliente() {
		return cliente;
	}	
	
	public void setArticulo(ArticuloFactura articulo) {
		this.articulo = articulo;
	}	// setArticulo	

	public void setArticulos(List<ArticuloFactura> articulos) {
		this.articulos = articulos;
	}		

	public String getIdFacturamaRegistro() {
		return idFacturamaRegistro;
	}	
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
		try {
			switch(accion) {
				case PROCESAR:
					regresar= this.procesarClientes(sesion);
					break;		
				case REPROCESAR:
					regresar= this.procesarArticulos(sesion);
					break;				
				case ACTIVAR:
					if(Cadena.isVacio(this.cliente.getIdFacturama()))
						regresar= this.procesarCliente(sesion);
					else
						regresar= this.updateCliente(sesion);
					break;
				case AGREGAR:
					if(Cadena.isVacio(this.articulo.getIdFacturama()))
						regresar= this.procesarArticulo(sesion);
					else
						regresar= this.updateArticulo(sesion);
					break;
				case MODIFICAR:
					regresar= this.updateCliente(sesion);
					break;
				case COMPLEMENTAR:
					regresar= this.updateArticulo(sesion);
					break;
				case TRANSFORMACION:
					regresar= this.generarCfdi(sesion);
					break;
			} // switch			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // ejecutar
	
	protected boolean removeCliente(Session sesion, String id) throws Exception{
		boolean regresar= true;
		try {
			CFDIFactory.getInstance().clientRemove(id);
		} // try
		catch (Exception e) {			
			this.registrarBitacora(sesion, -1L, e.getMessage().concat(" Eliminaci�n cliente facturama : ").concat(id));
			throw e;
		} // catch		
		return regresar;
	} // removeArticulo
	
	protected boolean updateCliente(Session sesion) throws Exception{
		boolean regresar= true;
		try {
      if(!Configuracion.getInstance().isEtapaProduccion())
        LOG.warn("Actualizar los datos del cliente en FACTURAMA ["+ this.cliente+ "]");			
      else
        CFDIFactory.getInstance().updateClient(this.cliente);			
		} // try
		catch (Exception e) {		
			this.registrarBitacora(sesion, this.cliente.getId(), e.getMessage());
			throw e;
		} // catch		
		return regresar;
	} // procesarCliente
	
	protected boolean procesarCliente(Session sesion) throws Exception{
		boolean regresar= false;
		String id       = null;
		try {
			id= CFDIFactory.getInstance().createClientId(this.cliente);
			if(isCorrectId(id))
				regresar= actualizarCliente(sesion, this.cliente.getId(), id);
			else				
				this.registrarBitacora(sesion, this.cliente.getId(), id, false);								
		} // try
		catch (Exception e) {		
			this.registrarBitacora(sesion, this.cliente.getId(), e.getMessage());
			throw e;
		} // catch		
		return regresar;
	} // procesarCliente
	
	protected boolean procesarClientes(Session sesion) throws Exception{
		boolean regresar              = true;
		CFDIGestor gestor             = null;
		List<ClienteFactura> clientes = null;
		List<Client> clientesFacturama= null;
		Client clientePivote          = null;		
		Long idBitacora               = null;
		String id                     = null;
		int index                     = -1;
		try {
			gestor= new CFDIGestor();
			clientes= gestor.toAllClientesFactura(sesion);
			if(!clientes.isEmpty()){
				clientesFacturama= CFDIFactory.getInstance().getClients();
				if(!clientesFacturama.isEmpty()){
					for(ClienteFactura recordCliente: clientes){
						if(recordCliente.getCorreo()!= null && !Cadena.isVacio(recordCliente.getCorreo())){
							idBitacora= recordCliente.getId();
							clientePivote= new Client(recordCliente.getRfc());
							index= clientesFacturama.indexOf(clientePivote);
							if(index== -1){
								id= CFDIFactory.getInstance().createClientId(recordCliente);
								if(isCorrectId(id))
									actualizarCliente(sesion, recordCliente.getId(), id);
								else
									this.registrarBitacora(sesion, recordCliente.getId(), id);								
							} // if
							//else
								//CFDIFactory.getInstance().updateClient(recordCliente);
						} // if
						else
							LOG.info("El cliente con rfc: " + recordCliente.getRfc() + " no cuenta con correo por lo que no fue publicado en facturama.");
					} // for
				} // if
			} // if
		} // try
		catch (Exception e) {			
			this.registrarBitacora(sesion, idBitacora, e.getMessage());								
			throw e;
		} // catch		
		return regresar;
	} // procesarClientes	
	
	protected boolean actualizarCliente(Session sesion, Long id, String idFacturama) throws Exception{
		TcManticClientesDto cliente= (TcManticClientesDto) DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, id);
		cliente.setIdFacturama(idFacturama);
		return DaoFactory.getInstance().update(sesion, cliente)>= 1L;
	} // actualizarCliente
	
	protected boolean registrarBitacora(Session sesion, Long id, String error) throws Exception{
		return registrarBitacora(sesion, id, error, true);
	} // registrarBitacora
	
	protected boolean registrarBitacora(Session sesion, Long id, String error, boolean cliente) throws Exception{		
		return registrarBitacora(sesion, id, error, cliente ? REGISTRO_CLIENTE : REGISTRO_ARTICULO);
	} // actualizarCliente
	
	protected boolean registrarBitacora(Session sesion, Long id, String error, String proceso) throws Exception{
		TcManticFacturamaBitacoraDto bitacora= new TcManticFacturamaBitacoraDto();
		bitacora.setIdKey(id);
		bitacora.setProceso(proceso);
		bitacora.setObservacion(error);
		bitacora.setCodigo("99");
		return DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
	} // actualizarCliente
	
	protected boolean isCorrectId(String id){
		boolean regresar= false;
		try {
			regresar= !Cadena.isVacio(id);
		} // try
		catch (Exception e) {			
			return false;
		} // catch		
		return regresar;
	} // isCorrectId

	protected boolean updateArticulo(Session sesion) throws Exception{
		boolean regresar= true;
		try {
			CFDIFactory.getInstance().updateProduct(this.articulo);
		} // try
		catch (Exception e) {			
			this.registrarBitacora(sesion, this.articulo.getId(), e.getMessage(), false);
			throw e;
		} // catch		
		return regresar;
	} // procesarCliente
	
	protected boolean removeArticulo(Session sesion, String id) throws Exception{
		boolean regresar= true;
		try {
			CFDIFactory.getInstance().productRemove(id);
		} // try
		catch (Exception e) {			
			this.registrarBitacora(sesion, -1L, e.getMessage().concat(" Eliminacion articulo : ").concat(id), false);
			throw e;
		} // catch		
		return regresar;
	} // removeArticulo
	
	protected boolean procesarArticulo(Session sesion) throws Exception{
		boolean regresar= false;
		String id       = null;
		try {
			id= CFDIFactory.getInstance().createProductId(this.articulo);
			if(isCorrectId(id))
				regresar= actualizarProducto(sesion, this.articulo.getId(), id);
			else
				this.registrarBitacora(sesion, this.articulo.getId(), id, false);								
		} // try
		catch (Exception e) {			
			this.registrarBitacora(sesion, this.articulo.getId(), e.getMessage(), false);
			throw e;
		} // catch		
		return regresar;
	} // procesarArticulo
	
	protected boolean procesarArticulos(Session sesion) throws Exception{
		boolean regresar                = true;
		CFDIGestor gestor               = null;
		List<ArticuloFactura> articulos = null;
		List<Product> articulosFacturama= null;
		Product articuloPivote          = null;		
		Long idBitacora                 = null;
		String id                       = null;
		int index                       = -1;
		try {
			gestor= new CFDIGestor();
			articulos= gestor.toAllArticulosFactura(sesion);
			int count= 0;
			if(!articulos.isEmpty()){
				articulosFacturama= CFDIFactory.getInstance().getProducts();
				if(!articulosFacturama.isEmpty()) {
					for(ArticuloFactura recordArticulo: articulos) {
						LOG.warn("Procesando "+ (count++)+ " de "+ articulos.size());
						idBitacora= recordArticulo.getId();
						articuloPivote= new Product();
						articuloPivote.setIdentificationNumber(recordArticulo.getIdentificador());
						index= articulosFacturama.indexOf(articuloPivote);
						if(index == -1){
							id= CFDIFactory.getInstance().createProductId(recordArticulo);
							if(isCorrectId(id))
								this.actualizarProducto(sesion, recordArticulo.getId(), id);
							else
								this.registrarBitacora(sesion, recordArticulo.getId(), id, false);								
						} // if
						else{
							CFDIFactory.getInstance().updateProduct(recordArticulo);						
							this.actualizarProducto(sesion, recordArticulo.getId(), id);
						} // else
					} // for
				} // if
			} // if
		} // try
		catch (Exception e) {			
			this.registrarBitacora(sesion, idBitacora, e.getMessage(), false);
			throw e;
		} // catch		
		return regresar;
	} // procesarClientes
	
	protected boolean actualizarProducto(Session sesion, Long id, String idFacturama) throws Exception{
		boolean regresar         = false;
		TcManticArticulosDto item= (TcManticArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, id);
		item.setIdFacturama(idFacturama);
		regresar= DaoFactory.getInstance().update(sesion, item)>= 1L;		
		return regresar;
	} // actualizarCliente
	
	public boolean generarCfdi(Session sesion) throws Exception {
		return generarCfdi(sesion, JsfBase.getAutentifica()== null? "1": JsfBase.getAutentifica().getEmpresa().getIdEmpresa().toString(), JsfBase.getIdUsuario());
	} // generarCfdi
	
	public boolean generarCfdi(Session sesion, String idEmpresa, Long idUsuario) throws Exception{
		boolean regresar= false;
		Cfdi cfdi       = null;
		try {
      //**ERROR** DETENER AQUI PARA EL CALCULO DE LOS CONCEPTOS PARA FACTURAMA
			cfdi= CFDIFactory.getInstance().createCfdi(this.cliente, this.articulos);
			if(this.isCorrectId(cfdi.getId())) {
				this.idFacturamaRegistro= cfdi.getId();
				regresar= this.actualizarFactura(sesion, this.cliente.getIdFactura(), cfdi, idUsuario);
				Calendar calendar= Fecha.toCalendar(cfdi.getDate().substring(0, 10), cfdi.getDate().substring(11, 19));				
				String path = Configuracion.getInstance().getPropiedadSistemaServidor("facturama").concat(idEmpresa).concat("/")+ calendar.get(Calendar.YEAR)+ "/"+ Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase()+"/"+ this.cliente.getRfc().concat("/");				
				CFDIFactory.getInstance().download(path, this.cliente.getRfc().concat("-").concat(cfdi.getFolio()), cfdi.getId());
				this.toUpdateData(sesion, cfdi, this.cliente.getIdFactura(), path, idUsuario);
				this.insertFiles(sesion, calendar, cfdi, path, this.cliente.getRfc(), this.cliente.getIdFactura(), idUsuario);
			} // if
      else {
				this.registrarBitacora(sesion, this.cliente.getIdFactura(), cfdi.getId(), REGISTRO_CFDI);
				this.actualizarFacturaAutomatico(sesion, this.cliente.getIdFactura(), idUsuario);
			} // else
		} // try
		catch (Exception e) {						
			if(this.existFactura(sesion)) {
				this.registrarBitacoraFactura(sesion, this.cliente.getIdFactura(), EEstatusFacturas.AUTOMATICO.getIdEstatusFactura(), "OCURRI� UN ERROR AL REALIZAR EL TIMBRADO AUTOMATICO.", idUsuario);		
				this.actualizarFacturaAutomatico(sesion, this.cliente.getIdFactura(), idUsuario, EEstatusFacturas.AUTOMATICO.getIdEstatusFactura());
			} // if
			throw e;
		} // catch
		return regresar;
	} // generarCfdi
	
	private boolean existFactura(Session sesion) {
		boolean regresar           = false;
		TcManticFacturasDto factura= null;
		try {
			factura= (TcManticFacturasDto) DaoFactory.getInstance().findById(sesion, TcManticFacturasDto.class, this.cliente.getIdFactura());
			regresar= factura!= null && factura.isValid();
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
			regresar= false;
		} // catch		
		return regresar;
	} // existFactura
	
	private void toUpdateData(Session sesion, Cfdi detail, Long idFactura, String path, Long idUsuario) throws Exception {
		TcManticFacturasDto factura= (TcManticFacturasDto)DaoFactory.getInstance().findById(sesion, TcManticFacturasDto.class, idFactura);
		try {
			if(factura!= null && factura.getSelloSat()== null && factura.getCadenaOriginal()== null) {
				LOG.warn("Actualizando datos de la factura ["+ detail.getFolio()+ "] del cliente ["+ this.cliente.getRfc()+ "] porque estaba incompleto el registro !");			
				this.registrarBitacoraFactura(sesion, idFactura, EEstatusFacturas.TIMBRADA.getIdEstatusFactura(), "ESTA FACTURA FUE RECUPERADA DE FACTURAMA", idUsuario);
				Complement complement = detail.getComplement();
				factura.setComentarios("ESTA FACTURA FUE RECUPERADA DE FACTURAMA !");
				factura.setSelloCfdi(complement.getTaxStamp().getCfdiSign());
				factura.setSelloSat(complement.getTaxStamp().getSatSign());
				factura.setCertificadoDigital(detail.getCertNumber());
				factura.setCertificadoSat(complement.getTaxStamp().getSatCertNumber());
				factura.setFolioFiscal(complement.getTaxStamp().getUuid());
				factura.setIdFacturaEstatus(EEstatusFacturas.TIMBRADA.getIdEstatusFactura());
				factura.setIntentos(factura.getIntentos()+1L);
				factura.setCadenaOriginal(this.toCadenaOriginal(path.concat(this.cliente.getRfc()).concat("-").concat(detail.getFolio()).concat(".").concat(EFormatos.XML.name().toLowerCase())));				
				DaoFactory.getInstance().update(sesion, factura);
			} // if
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
	} // toUpdateData
	
	private String toCadenaOriginal(String xml) {
		String regresar= null;
		try {
      LOG.warn("Generando cadena original: ".concat(Objects.equals(Configuracion.getInstance().getPropiedad("sistema.nivel.facturacion"), "4.0")? "cadenaoriginal_4_0.xslt": "cadenaoriginal_3_3.xslt"));
			StreamSource source       = new StreamSource(new File(xml));
			StreamSource stylesource  = new StreamSource(this.getClass().getResourceAsStream("/mx/org/kaana/mantic/libs/factura/".concat(Objects.equals(Configuracion.getInstance().getPropiedad("sistema.nivel.facturacion"), "4.0")? "cadenaoriginal_4_0.xslt": "cadenaoriginal_3_3.xslt")));
			TransformerFactory factory= TransformerFactory.newInstance();
			Transformer transformer   = factory.newTransformer(stylesource);
			StreamResult result       = new StreamResult(new StringWriter());
			transformer.transform(source, result);
			regresar= result.getWriter().toString();
		} // try
		catch(Exception e) {
			Error.mensaje(e);
		} // catch
		return regresar;
	} // toCadenaOriginal
	
	public void insertFiles(Session sesion, Calendar calendar, Cfdi cfdi, String path, String rfc, Long idFactura, Long idUsuario) throws Exception {
    LOG.warn("Insertando XML: ".concat(rfc.concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.XML.name().toLowerCase())));
  	TcManticFacturasArchivosDto xml= new TcManticFacturasArchivosDto(
			idFactura, 
			path, 
			null, 
			rfc.concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.XML.name().toLowerCase()), 
			new Long(calendar.get(Calendar.YEAR)),
			null, 
			-1L,
			0L,
			idUsuario,
			1L, // idTipoArchivo XML
			1L, // idPrincipal
			cfdi.getCfdiType()+ "|"+ cfdi.getPaymentMethod()+ "|"+ cfdi.getSerie(), // observaciones, 
			path.concat(rfc).concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.XML.name().toLowerCase()), 
			new Long(calendar.get(Calendar.MONTH)+ 1),
			"" // comentarios
		);
		DaoFactory.getInstance().insert(sesion, xml);
		TcManticFacturasArchivosDto pdf= new TcManticFacturasArchivosDto(
			idFactura, 
			path,
			null, 
			rfc.concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.PDF.name().toLowerCase()), 
			new Long(calendar.get(Calendar.YEAR)),
			null, 
			-1L,
			0L,
			idUsuario,
			2L, // idTipoArchivo PDF
			1L, // idPrincipal
			cfdi.getCfdiType()+ "|"+ cfdi.getPaymentMethod()+ "|"+ cfdi.getPaymentConditions(), // observaciones, 
			path.concat(rfc).concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.PDF.name().toLowerCase()), 
			new Long(calendar.get(Calendar.MONTH)+ 1), 
			"" // comentarios
		);
		DaoFactory.getInstance().insert(sesion, pdf);
	} // insertFiles	
	
	protected boolean actualizarFactura(Session sesion, Long id, Cfdi cfdi, Long idUsuario) throws Exception {
		boolean regresar           = false;
		TcManticFacturasDto factura= null;		
		factura= (TcManticFacturasDto) DaoFactory.getInstance().findById(sesion, TcManticFacturasDto.class, id);
		this.registrarBitacoraFactura(sesion, id, EEstatusFacturas.TIMBRADA.getIdEstatusFactura(), "TIMBRADO DE LA FACTURA", idUsuario);		
		factura.setIdFacturama(cfdi.getId());
		factura.setFolio(cfdi.getFolio());					
		factura.setTimbrado(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		factura.setIntentos(factura.getIntentos()+1L);
		factura.setIdFacturaEstatus(EEstatusFacturas.TIMBRADA.getIdEstatusFactura());
		regresar= DaoFactory.getInstance().update(sesion, factura)>= 1L;		
		return regresar;
	} // actualizarFactura
	
	public boolean actualizarFacturaAutomatico(Session sesion, Long id, Long idUsuario) throws Exception {		
		boolean regresar           = false;
		TcManticFacturasDto factura= null;	
    try {
      factura= (TcManticFacturasDto) DaoFactory.getInstance().findById(sesion, TcManticFacturasDto.class, id);
      this.registrarBitacoraFactura(sesion, id, EEstatusFacturas.AUTOMATICO.getIdEstatusFactura(), "ASIGNACION A FACTURACION AUTOMATICA", idUsuario);		
      factura.setIdFacturaEstatus(EEstatusFacturas.AUTOMATICO.getIdEstatusFactura());		
      factura.setIntentos(factura.getIntentos()+ 1L);
      regresar= DaoFactory.getInstance().update(sesion, factura)>= 1L;		
    } // try
    catch(Exception e) {
      throw e;
    } // catch
		return regresar;
	} 
	
	public boolean actualizarFacturaAutomatico(Session sesion, Long id, Long idUsuario, Long idEstatus) throws Exception {
    return this.actualizarFacturaAutomatico(sesion, id, idUsuario, idEstatus, 1L, null);
  }
  
	public boolean actualizarFacturaAutomatico(Session sesion, Long id, Long idUsuario, Long idEstatus, Long intentos, String comentario) throws Exception {
    boolean regresar= false;
    try {
      TcManticFacturasDto factura= (TcManticFacturasDto) DaoFactory.getInstance().findById(sesion, TcManticFacturasDto.class, id);
      if(Objects.equals(intentos, 10L))
        this.registrarBitacoraFactura(sesion, id, idEstatus, comentario, idUsuario);				
      else
        this.registrarBitacoraFactura(sesion, id, idEstatus, "ASIGNACION A FACTURACION AUTOMATICA", idUsuario);				
      factura.setIdFacturaEstatus(idEstatus);		
      factura.setIntentos(factura.getIntentos()+ intentos);
      regresar= DaoFactory.getInstance().update(sesion, factura)>= 1L;
    } // try
    catch(Exception e) {
      throw e;
    } // catch
		return regresar;
	} // actualizarFacturaAutomatico
	
	protected boolean registrarBitacoraFactura(Session sesion, Long idFactura, Long idFacturaEstatus, String justificacion) throws Exception{
		return registrarBitacoraFactura(sesion, idFactura, idFacturaEstatus, justificacion, JsfBase.getIdUsuario());
	} // registrarBitacoraFactura
	
	protected boolean registrarBitacoraFactura(Session sesion, Long idFactura, Long idFacturaEstatus, String justificacion, Long idUsuario) throws Exception{
		boolean regresar= false;
		TcManticFacturasBitacoraDto bitacora= null;
		try {
			bitacora= new TcManticFacturasBitacoraDto();
			bitacora.setIdFactura(idFactura);
			bitacora.setIdFacturaEstatus(idFacturaEstatus);
			bitacora.setIdUsuario(idUsuario);
			bitacora.setJustificacion(justificacion);
			regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // registrarBitacoraFactura
	
	protected void actualizarSaldoCatalogoCliente(Session sesion, Long idCliente, Double cantidad, boolean sumar) throws Exception{
		TcManticClientesDto cliente= null;
		try {
			cliente= (TcManticClientesDto) DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, idCliente);
			cliente.setSaldo(sumar ? (cliente.getSaldo() + cantidad) : (cliente.getSaldo() - cantidad));
			DaoFactory.getInstance().update(sesion, cliente);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
	} // actualizarSaldoCatalogoCliente
  
}
