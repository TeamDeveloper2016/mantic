package mx.org.kaana.libs.facturama.reglas;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import mx.org.kaana.kajool.db.comun.hibernate.DaoFactory;
import mx.org.kaana.kajool.enums.EAccion;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.kajool.reglas.IBaseTnx;
import mx.org.kaana.libs.facturama.models.Client;
import mx.org.kaana.libs.facturama.models.Product;
import mx.org.kaana.libs.facturama.models.response.Cfdi;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Fecha;
import mx.org.kaana.libs.pagina.JsfBase;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.db.dto.TcManticArticulosDto;
import mx.org.kaana.mantic.db.dto.TcManticClientesDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturamaBitacoraDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasArchivosDto;
import mx.org.kaana.mantic.db.dto.TcManticFacturasDto;
import mx.org.kaana.mantic.facturas.beans.ArticuloFactura;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

public class TransaccionFactura extends IBaseTnx{

	private static final Log LOG                 = LogFactory.getLog(TransaccionFactura.class);
	private static final String REGISTRO_CLIENTE = "REGISTRO DE CLIENTE";
	private static final String REGISTRO_ARTICULO= "REGISTRO DE ARTICULO";
	private static final String REGISTRO_CFDI    = "REGISTRO DE CFDI";
	private ClienteFactura cliente;
	private ArticuloFactura articulo;	
	private List<ArticuloFactura> articulos;	

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
	
	@Override
	protected boolean ejecutar(Session sesion, EAccion accion) throws Exception {		
		boolean regresar= false;
		try {
			switch(accion){
				case PROCESAR:
					regresar= procesarClientes(sesion);
					break;		
				case REPROCESAR:
					regresar= procesarArticulos(sesion);
					break;				
				case ACTIVAR:
					if(Cadena.isVacio(this.cliente.getIdFacturama()))
						regresar= procesarCliente(sesion);
					else
						regresar= updateCliente(sesion);
					break;
				case AGREGAR:
					regresar= procesarArticulo(sesion);
					break;
				case MODIFICAR:
					regresar= updateCliente(sesion);
					break;
				case COMPLEMENTAR:
					regresar= updateArticulo(sesion);
					break;
				case TRANSFORMACION:
					regresar= generarCfdi(sesion);
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
			registrarBitacora(sesion, id, e.getMessage());
			throw e;
		} // catch		
		return regresar;
	} // removeArticulo
	
	protected boolean updateCliente(Session sesion) throws Exception{
		boolean regresar= true;
		try {
			CFDIFactory.getInstance().updateClient(this.cliente);			
		} // try
		catch (Exception e) {		
			registrarBitacora(sesion, this.cliente.getId(), e.getMessage());
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
				registrarBitacora(sesion, this.cliente.getId(), id, false);								
		} // try
		catch (Exception e) {		
			registrarBitacora(sesion, this.cliente.getId(), e.getMessage());
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
		String idBitacora             = null;
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
									registrarBitacora(sesion, recordCliente.getId(), id);								
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
			registrarBitacora(sesion, idBitacora, e.getMessage());								
			throw e;
		} // catch		
		return regresar;
	} // procesarClientes	
	
	protected boolean actualizarCliente(Session sesion, String id, String idFacturama) throws Exception{
		boolean regresar           = false;
		TcManticClientesDto cliente= null;
		try {
			cliente= (TcManticClientesDto) DaoFactory.getInstance().findById(sesion, TcManticClientesDto.class, Long.valueOf(id));
			cliente.setIdFacturama(idFacturama);
			regresar= DaoFactory.getInstance().update(sesion, cliente)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // actualizarCliente
	
	protected boolean registrarBitacora(Session sesion, String id, String error) throws Exception{
		return registrarBitacora(sesion, id, error, true);
	} // registrarBitacora
	
	protected boolean registrarBitacora(Session sesion, String id, String error, boolean cliente) throws Exception{		
		return registrarBitacora(sesion, id, error, cliente ? REGISTRO_CLIENTE : REGISTRO_ARTICULO);
	} // actualizarCliente
	
	protected boolean registrarBitacora(Session sesion, String id, String error, String proceso) throws Exception{
		boolean regresar                     = false;		
		TcManticFacturamaBitacoraDto bitacora= null;
		try {
			bitacora= new TcManticFacturamaBitacoraDto();
			bitacora.setIdKey(Long.valueOf(id));
			bitacora.setProceso(proceso);
			bitacora.setObservacion(error);
			bitacora.setCodigo("99");
			regresar= DaoFactory.getInstance().insert(sesion, bitacora)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
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
			registrarBitacora(sesion, this.articulo.getId(), e.getMessage(), false);
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
			registrarBitacora(sesion, id, e.getMessage(), false);
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
				registrarBitacora(sesion, this.articulo.getId(), id, false);								
		} // try
		catch (Exception e) {			
			registrarBitacora(sesion, this.articulo.getId(), e.getMessage(), false);
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
		String idBitacora               = null;
		String id                       = null;
		int index                      = -1;
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
								actualizarProducto(sesion, recordArticulo.getId(), id);
							else
								registrarBitacora(sesion, recordArticulo.getId(), id, false);								
						} // if
						else{
							CFDIFactory.getInstance().updateProduct(recordArticulo);						
							actualizarProducto(sesion, recordArticulo.getId(), id);
						} // else
					} // for
				} // if
			} // if
		} // try
		catch (Exception e) {			
			registrarBitacora(sesion, idBitacora, e.getMessage(), false);
			throw e;
		} // catch		
		return regresar;
	} // procesarClientes
	
	protected boolean actualizarProducto(Session sesion, String id, String idFacturama) throws Exception{
		boolean regresar            = false;
		TcManticArticulosDto articulo= null;
		try {
			articulo= (TcManticArticulosDto) DaoFactory.getInstance().findById(sesion, TcManticArticulosDto.class, Long.valueOf(id));
			articulo.setIdFacturama(idFacturama);
			regresar= DaoFactory.getInstance().update(sesion, articulo)>= 1L;
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // actualizarCliente
	
	public boolean generarCfdi(Session sesion) throws Exception{
		boolean regresar= false;
		Cfdi cfdi       = null;
		try {
			cfdi= CFDIFactory.getInstance().createCfdi(this.cliente, this.articulos);
			if(isCorrectId(cfdi.getId())) {
				regresar= actualizarFactura(sesion, this.cliente.getIdFactura(), cfdi);
				Calendar calendar= Fecha.toCalendar(cfdi.getDate().substring(0, 10), cfdi.getDate().substring(11, 19));
				String path = Configuracion.getInstance().getPropiedadSistemaServidor("facturama")+ calendar.get(Calendar.YEAR)+ "/"+ Fecha.getNombreMes(calendar.get(Calendar.MONTH)).toUpperCase()+"/"+ this.cliente.getRfc().concat("/");
				CFDIFactory.getInstance().download(path, this.cliente.getRfc().concat("-").concat(cfdi.getFolio()), cfdi.getId());
				insertFiles(sesion, calendar, cfdi, path, this.cliente.getRfc(), Long.valueOf(this.cliente.getIdFactura()));
			} // if
			else
				registrarBitacora(sesion, this.cliente.getIdFactura(), cfdi.getId(), REGISTRO_CFDI);
		} // try
		catch (Exception e) {
			registrarBitacora(sesion, this.cliente.getId(), e.getMessage(), REGISTRO_CFDI);
			throw e;
		} // catch		
		return regresar;
	} // generarCfdi
	
	public void insertFiles(Session sesion, Calendar calendar, Cfdi cfdi, String path, String rfc, Long idFactura) throws Exception {
	TcManticFacturasArchivosDto xml= new TcManticFacturasArchivosDto(
			idFactura, 
			path, 
			null, 
			rfc.concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.XML.name().toLowerCase()), 
			new Long(calendar.get(Calendar.YEAR)),
			null, 
			-1L,
			0L,
			JsfBase.getIdUsuario(),
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
			JsfBase.getIdUsuario(),
			2L, // idTipoArchivo PDF
			1L, // idPrincipal
			cfdi.getCfdiType()+ "|"+ cfdi.getPaymentMethod()+ "|"+ cfdi.getPaymentConditions(), // observaciones, 
			path.concat(rfc).concat("-").concat(cfdi.getFolio()).concat(".").concat(EFormatos.PDF.name().toLowerCase()), 
			new Long(calendar.get(Calendar.MONTH)+ 1), 
			"" // comentarios
		);
		DaoFactory.getInstance().insert(sesion, pdf);
	}
	
	protected boolean actualizarFactura(Session sesion, String id, Cfdi cfdi) throws Exception {
		boolean regresar           = false;
		TcManticFacturasDto factura= null;
		try {
			factura= (TcManticFacturasDto) DaoFactory.getInstance().findById(sesion, TcManticFacturasDto.class, Long.valueOf(id));
			factura.setIdFacturama(cfdi.getId());
			factura.setFolio(cfdi.getFolio());			
			factura.setTimbrado(new Timestamp(Fecha.toCalendar(cfdi.getDate().substring(0, 10), cfdi.getDate().substring(11, 19)).getTimeInMillis()));
			regresar= DaoFactory.getInstance().update(sesion, factura)>= 1L;
		} // try
		catch (Exception e) {			
			Error.mensaje(e);
		} // catch		
		return regresar;
	} // actualizarCliente
}
