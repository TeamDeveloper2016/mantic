package mx.org.kaana.libs.facturama.reglas;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.facturama.container.FacturamaApi;
import mx.org.kaana.libs.facturama.models.Address;
import mx.org.kaana.libs.facturama.models.Client;
import mx.org.kaana.libs.facturama.models.Product;
import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.request.ProductTax;
import mx.org.kaana.libs.facturama.models.request.Cfdi;
import mx.org.kaana.libs.facturama.models.request.CfdiType;
import mx.org.kaana.libs.facturama.models.request.Item;
import mx.org.kaana.libs.facturama.models.request.Receiver;
import mx.org.kaana.libs.facturama.models.request.Tax;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.models.response.CancelationStatus;
import mx.org.kaana.libs.facturama.services.CfdiService;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.libs.formato.Encriptar;
import mx.org.kaana.libs.formato.Numero;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.recurso.Configuracion;
import mx.org.kaana.mantic.facturas.beans.ArticuloFactura;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;
import mx.org.kaana.mantic.facturas.enums.EMotivoCancelacion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *@company MANTIC
 *@project KAANA (Sistema de seguimiento y control de proyectos)
 *@date 30/10/2018
 *@time 10:30:40 PM 
 *@author Team Developer 2018 <team.developer@gmail.com>
 */

public class CFDIFactory implements Serializable {
	
	private static final Log LOG=LogFactory.getLog(CFDIFactory.class);
	private static final long serialVersionUID =-5361573067043698091L;
  
  private static final String IMOX_CFDI         = "IMOX_CFDI";
  private static final String IMOX_SANDBOX      = "IMOX_SANDBOX";
  
  private static final String MANTIC_USER_PO    = "FERRBONANZA";
  private static final String MANTIC_PASSWORD_PO= "0112121a263933cf4341424a";
  private static final String MANTIC_USER_PU    = "FERRBONANZASANDBOX";
  private static final String MANTIC_PASSWORD_PU= "5d9e41ed12cd669a8d8ff019b948ff25c976a3";
  
  private static final String KALAN_USER_PO    = "KALAN_USER";
  private static final String KALAN_PASSWORD_PO= "f561d0729c44ec15060b";
  private static final String KALAN_USER_PU    = "KALANSANDBOX";
  private static final String KALAN_PASSWORD_PU= "2cde0123cb77e05fd03699ca0c23cc7dbb";
  
  private static final String TSAAK_USER_PO    = "TSAAK_USER";
  private static final String TSAAK_PASSWORD_PO= "5dc6b7629ab651f264a5";
  private static final String TSAAK_USER_PU    = "TSAAKSANDBOX";
  private static final String TSAAK_PASSWORD_PU= "dd1bcf7faf56c1bfb0d175e66097b06197";
  
  private static final String DESCRIPCION_IVA  = "IVA";
  private static final String CURRENCY         = "MXN";  
	
	private FacturamaApi facturama;
	
  private static CFDIFactory instance;
  private static Object mutex;
  /**
   * Inicialización de variable mutex
   */
  static {
    mutex = new Object();
  }
	
  /**
   * Contructor default
   */
  private CFDIFactory() {		
    String user_po= MANTIC_USER_PO;
    String user_pu= MANTIC_USER_PU;
    Encriptar encriptar= new Encriptar();
    String password_po= (!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion())? System.getenv(IMOX_CFDI): encriptar.desencriptar(MANTIC_PASSWORD_PO);
    String password_pu= (!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion())? System.getenv(IMOX_SANDBOX): encriptar.desencriptar(MANTIC_PASSWORD_PU);
    switch(Configuracion.getInstance().getPropiedad("sistema.empresa.principal")) {
      case "mantic":
        user_po    = MANTIC_USER_PO;
        user_pu    = MANTIC_USER_PU;
        password_po= (!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion())? System.getenv(IMOX_CFDI): encriptar.desencriptar(MANTIC_PASSWORD_PO);
        password_pu= (!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion())? System.getenv(IMOX_SANDBOX): encriptar.desencriptar(MANTIC_PASSWORD_PU);
        break;
      case "kalan":
        user_po    = KALAN_USER_PO;
        user_pu    = KALAN_USER_PU;
        password_po= (!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion())? System.getenv(IMOX_CFDI): encriptar.desencriptar(KALAN_PASSWORD_PO);
        password_pu= (!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion())? System.getenv(IMOX_SANDBOX): encriptar.desencriptar(KALAN_PASSWORD_PU);
        break;
      case "tsaak":
        user_po    = TSAAK_USER_PO;
        user_pu    = TSAAK_USER_PU;
        password_po= (!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion())? System.getenv(IMOX_CFDI): encriptar.desencriptar(TSAAK_PASSWORD_PO);
        password_pu= (!Configuracion.getInstance().isEtapaDesarrollo() && !Configuracion.getInstance().isEtapaCapacitacion())? System.getenv(IMOX_SANDBOX): encriptar.desencriptar(TSAAK_PASSWORD_PU);
        break;
    } // swtich
    try {
      if (Configuracion.getInstance().isEtapaProduccion())
        this.facturama= new FacturamaApi(user_po, password_po, false);
      else 
        if (Configuracion.getInstance().isEtapaPruebas())
          this.facturama= new FacturamaApi(user_pu, password_pu, true);
        else
          this.facturama= new FacturamaApi(user_pu, password_pu, true);
    } // try 
    catch(Exception e) {
      LOG.error("NO SE PUDO CREAR LA CONEXION HACIA FACTURAMA");
      Error.mensaje(e);
    } // catch
    finally {
      encriptar= null;
    } // finally
  } // CFDIFactory

  /**
   * Devuelve la instancia de la clase.
   *
   * @return Instancia de la clase.
   */
  public static CFDIFactory getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        instance = new CFDIFactory();
      } // if
    } // if
    return instance;
  }

	public List<Client> getClients() throws FacturamaException, Exception {
		return this.facturama.Clients().List();
	}
	
	public List<Product> getProducts() throws FacturamaException, Exception {
		return this.facturama.Products().List();
	}

	public List<CfdiSearchResult> getCfdis() throws FacturamaException, Exception {
		return this.facturama.Cfdis().List();
	}
	
	public List<CfdiSearchResult> getCfdis(int folioStart, int folioEnd) throws FacturamaException, Exception {
		return this.facturama.Cfdis().List(
			folioStart, // folioStart
			folioEnd, // folioEnd
			null, // rfc
			null, // taxEntityName
			null, // dateStart
			null, // dateEnd
			null, // idBranch
			null, // serie
			CfdiService.CfdiStatus.Active, // status
			CfdiService.InvoiceType.Issued, // type
      null // orderNumber
		);
	}
	
	public List<CfdiSearchResult> getCfdis(String dateStart, String dateEnd) throws FacturamaException, Exception {
		return this.facturama.Cfdis().List(
			-1, // folioStart
			-1, // folioEnd
			null, // rfc
			null, // taxEntityName
			dateStart, // dateStart
			dateEnd, // dateEnd
			null, // idBranch
			null, // serie
			CfdiService.CfdiStatus.Active, // status
			CfdiService.InvoiceType.Issued, // type
      null // orderNumber
		);
	}
	
	public List<CfdiSearchResult> getCfdis(String rfc) throws FacturamaException, Exception {
		return this.facturama.Cfdis().ListFilterByRfc(rfc);
	}

	public int toCfdisSize() throws FacturamaException, Exception {
		return this.facturama.Cfdis().List().size();
	}

  public void download(String path, String name, String id) throws Exception {
		File result= new File(path);		
		if (!result.exists())
			result.mkdirs();
		result= new File(path.concat(name).concat(".").concat(EFormatos.PDF.name().toLowerCase()));
		if(!result.exists())
			this.facturama.Cfdis().SavePdf(path.concat(name).concat(".").concat(EFormatos.PDF.name().toLowerCase()), id);
		result= new File(path.concat(name).concat(".").concat(EFormatos.XML.name().toLowerCase()));
		if(!result.exists())
			this.facturama.Cfdis().SaveXml(path.concat(name).concat(".").concat(EFormatos.XML.name().toLowerCase()), id);
	}	

  public void downpdf(String path, String name, String id) throws Exception {
		File result= new File(path);		
		if (!result.exists())
			result.mkdirs();
    //if(!Objects.equals(path, null))
		//  result= new File(path.concat(name).concat(".").concat(EFormatos.PDF.name().toLowerCase()));
		this.facturama.Cfdis().SavePdf(path.concat(name).concat(".").concat(EFormatos.PDF.name().toLowerCase()), id);
	}	

//	public mx.org.kaana.libs.facturama.models.response.Cfdi cfdiRemove(String id) throws Exception {
//		mx.org.kaana.libs.facturama.models.response.Cfdi regresar= null;
//		try {
//			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas())
//			  regresar= this.facturama.Cfdis().Remove(id);
//		} // try
//		catch (Exception e) {			
//			throw e;
//		} // catch		
//		return regresar;
//	} // cfdiRemove
  
	public CancelationStatus cfdiRemove(String id, String motivo, String idNuevo) throws Exception {
		CancelationStatus regresar= null;
		try {
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas())
			  regresar= this.facturama.Cfdis().Remove(id, "issued", motivo, idNuevo);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // cfdiRemove
	
	public CancelationStatus cfdiRemove(String id, String motivo) throws Exception {
		return cfdiRemove(id, motivo, "");
	} // cfdiRemove
	
	public CancelationStatus cfdiRemove(String id) throws Exception {
		return cfdiRemove(id, EMotivoCancelacion.CFDI_NO_SE_LLEVO_ACABO.getIdMotivoCancelacion());
	} // cfdiRemove
	
	public String createCfdiId(ClienteFactura encabezado, List<ArticuloFactura> detalle) throws Exception {
		String regresar= null;
		Cfdi cfdi      = null;
		try {
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo()) {
			  cfdi= this.loadCfdi(encabezado, detalle);
			  regresar= this.createCfdi(cfdi).getId();
			} // if	
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // createCfdiId	
	
	public mx.org.kaana.libs.facturama.models.response.Cfdi createCfdi(ClienteFactura encabezado, List<ArticuloFactura> detalle) throws Exception {
		mx.org.kaana.libs.facturama.models.response.Cfdi regresar= null;
		Cfdi cfdi= null;
		try {
		  cfdi= this.loadCfdi(encabezado, detalle);
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo()) 
			  regresar= this.createCfdi(cfdi);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // createCfdi
	
	public mx.org.kaana.libs.facturama.models.response.Cfdi createCfdi(Cfdi cfdi) throws Exception {
		mx.org.kaana.libs.facturama.models.response.Cfdi regresar= null;
		try { 
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo())
        if(Objects.equals(Configuracion.getInstance().getPropiedad("sistema.nivel.facturacion"), "4.0"))  
			    regresar= this.facturama.Cfdis().Create3(cfdi);
        else 
			    regresar= this.facturama.Cfdis().Create(cfdi);
		} // try
		catch (Exception e) {			
      LOG.error("Factura para: "+ cfdi.getReceiver().getName()+ " ["+ cfdi.getReceiver().getRfc()+ "] ["+ cfdi.getReceiver().getFiscalRegime()+ "] ["+ cfdi.getReceiver().getTaxZipCode()+ "]");
      LOG.error("Items: "+ cfdi.getItems());
			throw e;
		} // catch	
		return regresar;
	} 
	
	private Cfdi loadCfdi(ClienteFactura encabezado, List<ArticuloFactura> detalle) {		
		Cfdi regresar= null;
		try {
			regresar= new Cfdi();
			regresar.setCurrency(CURRENCY);
			regresar.setExpeditionPlace(encabezado.getCodigoPostal());
			regresar.setPaymentConditions(Cadena.isVacio(encabezado.getObservaciones()) ? null : encabezado.getObservaciones());
			regresar.setCfdiType(CfdiType.Ingreso.getValue());
			regresar.setPaymentForm(encabezado.getMedioPago());
			regresar.setPaymentMethod(encabezado.getMetodoPago());
			regresar.setReceiver(this.toReceiver(encabezado));
			regresar.setItems(this.detalleFactura(detalle));
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		return regresar;
	} // loadCfdi
	
	private Receiver toReceiver(ClienteFactura encabezado) {
		Receiver regresar= null;
		try {
			regresar= new Receiver();
			regresar.setRfc(encabezado.getRfc());
			regresar.setName(encabezado.getNombre().trim());
			regresar.setCfdiUse(encabezado.getUsoCfdi().substring(0, 3));
      if(Objects.equals(Configuracion.getInstance().getPropiedad("sistema.nivel.facturacion"), "4.0")) {
		    regresar.setFiscalRegime(encabezado.getRegimenFiscal());
        regresar.setTaxZipCode(encabezado.getCodigoPostalCliente());
      } // if  
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // toReceiver
	
	private List<Item> detalleFactura(List<ArticuloFactura> articulos) {
		List<Item> regresar= null;
		Item articulo      = null;
		try {
			regresar= new ArrayList<>();
			for(ArticuloFactura record: articulos) {
				// ESTE AJUSTE ES PARA QUITAR LOS PROBLEMAS DEL REDONDEO A DOS DECIMALES 
				double diferencia= Numero.toRedondear(record.getTotal()- (record.getImpuestos()+ record.getSubtotal()));
				record.setSubtotal(Numero.toRedondear(record.getSubtotal()+ diferencia));
				
				// SI AUN HACIENDO EL AJUSTE DEL SUBTOTAL CON EL IMPUESTO EXISTE UNA DIFERENCIA EN EL TOTAL ENTONCES AJUSTAR EL TOTAL
				//if((record.getImpuestos()+ record.getSubtotal())!= record.getTotal())
				//	record.setTotal(Numero.toRedondear(record.getImpuestos()+ record.getSubtotal()));
				
				articulo= new Item();
				articulo.setProductCode(record.getCodigoHacienda());
				articulo.setIdentificationNumber(record.getIdentificador());
				articulo.setDescription(record.getNombre());
				articulo.setUnit(Cadena.letraCapital(record.getUnidad()));
				articulo.setUnitCode(record.getCodigo());
				articulo.setUnitPrice(record.getPrecio());
				articulo.setQuantity(record.getCantidad());
				articulo.setSubtotal(record.getSubtotal());
				articulo.setTotal(record.getTotal());				
				articulo.setTaxes(this.toTaxArticulo(record));
        if(Objects.equals(Configuracion.getInstance().getPropiedad("sistema.nivel.facturacion"), "4.0"))
          articulo.setTaxObject(record.getCodigoImpuesto());
				regresar.add(articulo);
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;		
	} // detalleFactura
	
	private List<Tax> toTaxArticulo(ArticuloFactura articulo) {
		List<Tax> regresar= null;
		Tax taxArticulo   = null;
		try {
			regresar= new ArrayList<>();
			taxArticulo= new Tax();
			taxArticulo.setTotal(articulo.getImpuestos());
			taxArticulo.setName(DESCRIPCION_IVA);
			taxArticulo.setBase(articulo.getSubtotal());
			taxArticulo.setRate(articulo.getIva()> 1 ? articulo.getIva()/100 : articulo.getIva());
			taxArticulo.setIsRetention(false);
			regresar.add(taxArticulo);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;		
	} // detalleFactura
	
	public String createClientId(ClienteFactura detalleCliente) throws Exception {
		String regresar= null;
		Client cliente = null;
		try {
		  cliente= this.loadCliente(detalleCliente);
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo())
			  regresar= this.createClient(cliente).getId();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // createClient
	
	public Client createClient(ClienteFactura detalleCliente) throws Exception {
		Client regresar= null;
		Client cliente = null;
		try {
			cliente= this.loadCliente(detalleCliente);
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo())
			  regresar= createClient(cliente);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // createClient
	
	public Client createClient(Client client) throws Exception {
		Client regresar= null;
		try {
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo())
			  regresar= this.facturama.Clients().Create(client);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // createClient
	
	public Client updateClient(ClienteFactura detalleCliente) throws Exception {
		return this.updateClient(detalleCliente, detalleCliente.getIdFacturama());
	} // updateCliente
	
	public Client updateClient(ClienteFactura detalleCliente, String id) throws Exception {
		Client regresar= null;
		Client cliente = null;
		Client pivote  = null;
    Boolean clean  = Boolean.FALSE;
		try {
			cliente= this.loadCliente(detalleCliente);
			pivote = this.clientFindById(id);
      clean  = Objects.equals(pivote, null);
      if(clean)
        pivote= this.createClient(detalleCliente);
			pivote.setAddress(cliente.getAddress());
			pivote.setCfdiUse(cliente.getCfdiUse());
			pivote.setEmail(cliente.getEmail());
			pivote.setName(cliente.getName());
			pivote.setRfc(cliente.getRfc());
      pivote.setFiscalRegime(cliente.getFiscalRegime()); // CFDI 4.0
      pivote.setTaxZipCode(cliente.getTaxZipCode()); // CFDI 4.0
      if(!clean) {
			  if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo())
  			  regresar= this.facturama.Clients().Update(pivote, pivote.getId());
      } // if  
      else
        regresar= pivote;
		} // try
		catch (Exception e) {			
			LOG.error("Cliente: "+ detalleCliente);
			throw e;
		} // catch	
		return regresar;
	} // updateCliente
	
	public Client clientFindById(String id) throws Exception, Exception {
		Client regresar= null;
		try {
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo()) {
			  regresar= this.facturama.Clients().Retrieve(id);
        // SI ES NULL SE DEBE DE LIMPIAR ID DE FACTURAMA
        if(Objects.equals(regresar, null)) {
          
        } // if
      } // if
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // clientFindById
	
	public Client clientRemove(String id) throws Exception {
		Client regresar= null;
		try {
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas())
			  regresar= this.facturama.Clients().Remove(id);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // clientFindById
	
	private Client loadCliente(ClienteFactura cliente) {
		Client regresar= null;
		Address address= null;
		try {
			regresar= new Client();		
			address= loadAddress(cliente);
			regresar.setAddress(address);
			regresar.setCfdiUse(cliente.getTipoCfdi());
			regresar.setEmail(cliente.getCorreo());
			regresar.setName(cliente.getNombre());
			regresar.setRfc(cliente.getRfc());
      if(!Cadena.isVacio(cliente.getRegimenFiscal()))
        regresar.setFiscalRegime(cliente.getRegimenFiscal()); // CFDI 4.0
      if(!Cadena.isVacio(cliente.getCodigoPostal()))
        regresar.setTaxZipCode(cliente.getCodigoPostal()); // CFDI 4.0
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	}	// loadCliente
	
	private Address loadAddress(ClienteFactura cliente) {
		Address regresar= null;
		try {
			regresar= new Address();
			regresar.setCountry(cliente.getPais());
			regresar.setExteriorNumber(cliente.getNumeroExterior());
			regresar.setInteriorNumber(cliente.getNumeroInterior());
			regresar.setLocality(cliente.getLocalidad());
			regresar.setMunicipality(cliente.getMunicipio());
			regresar.setNeighborhood(cliente.getColonia());
			regresar.setState(cliente.getEstado());
			regresar.setStreet(cliente.getCalle());
			regresar.setZipCode(cliente.getCodigoPostal());
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadAddress
	
	public String createProductId(ArticuloFactura detalleArticulo) throws Exception {
		String regresar= null;
		Product product = null;
		try {
			product= this.loadProduct(detalleArticulo);
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo())
			  regresar= createProduct(product).getId();
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // createProduct
	
	public Product createProduct(ArticuloFactura detalleArticulo) throws Exception {
		Product regresar= null;
		Product product = null;
		try {
			product= this.loadProduct(detalleArticulo);
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo())
  			regresar= createProduct(product);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // createProduct
	
	public Product createProduct(Product product) throws Exception {
		Product regresar= null;
		try {			
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo())
			  regresar= this.facturama.Products().Create(product);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // createClient
	
	public Product updateProduct(ArticuloFactura detalleArticulo) throws Exception {
		return updateProduct(detalleArticulo, detalleArticulo.getIdFacturama());
	} // updateProduct
	
	public Product updateProduct(ArticuloFactura detalleArticulo, String id) throws Exception {
		Product regresar= null;
		Product pivote  = this.productFindById(id);
    try {
      if(pivote!= null) {
        LOG.error("SI ENTRO A REALIZAR LA ACTUALIZACIÓN DEL ARTICULO EN FACTURAMA");
        Product product= this.loadProduct(detalleArticulo);			
        pivote.getTaxes().clear();
        pivote.setTaxes(product.getTaxes());
        pivote.setUnit(Cadena.letraCapital(product.getUnit()));
        pivote.setUnitCode(product.getUnitCode());
        pivote.setIdentificationNumber(product.getIdentificationNumber());
        pivote.setName(product.getName());			
        pivote.setDescription(Cadena.isVacio(product.getDescription()) ? product.getName(): product.getDescription());						
        pivote.setPrice(product.getPrice());			
        pivote.setCodeProdServ(product.getCodeProdServ());
        pivote.setCuentaPredial(null);
        if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo())
          regresar= this.facturama.Products().Update(pivote, pivote.getId());
        LOG.error("SE ACTUALIZÓ DEL ARTICULO EN FACTURAMA");
      }	// if
      else
        throw new RuntimeException("El articulo no existe en facturama: "+ detalleArticulo);
    } // try
    catch(Exception e) {
      throw e;
    } // catch
		return regresar;
	} // updateProduct
	
	public Product productFindById(String id) throws Exception {
		Product regresar= null;
		try {
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas() || Configuracion.getInstance().isEtapaDesarrollo())
			  regresar= this.facturama.Products().Retrieve(id);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // clientFindById
	
	public Product productRemove(String id) throws Exception {
		Product regresar= null;
		try {
			if(Configuracion.getInstance().isEtapaProduccion() || Configuracion.getInstance().isEtapaPruebas())
			  regresar= this.facturama.Products().Remove(id);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // clientFindById
	
	private Product loadProduct(ArticuloFactura articulo) throws Exception {
		Product regresar      = null;
		List<ProductTax> taxes= null;
		try {
			regresar= new Product();					
			taxes   = this.loadTaxes(articulo.getIva());				
			regresar.setTaxes(taxes);			
			regresar.setUnit(Cadena.letraCapital(articulo.getUnidad()));
			regresar.setUnitCode(articulo.getCodigo());
			regresar.setIdentificationNumber(articulo.getIdentificador());
			regresar.setName(articulo.getNombre());			
			regresar.setDescription(Cadena.isVacio(articulo.getDescripcion()) ? articulo.getNombre() : articulo.getDescripcion());						
			regresar.setPrice(articulo.getPrecio());			
			regresar.setCodeProdServ(articulo.getCodigoHacienda());
		} // try
		catch (Exception e) {
			throw e;
		} // catch		
		return regresar;
	}	// loadProduct
	
	private List<ProductTax> loadTaxes(Double iva) {
		List<ProductTax> regresar= null;
		ProductTax tax           = null;
		try {
			regresar= new ArrayList<>();
			tax     = new ProductTax();
      tax.setName(DESCRIPCION_IVA);
      tax.setRate(iva> 1? (iva/100): iva);
      tax.setIsRetention(Boolean.FALSE);
      tax.setIsFederalTax(Boolean.TRUE);
			regresar.add(tax);			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadTaxes
			
	public mx.org.kaana.libs.facturama.models.response.Cfdi toCfdiDetail(String id) throws FacturamaException, Exception {
	  return this.facturama.Cfdis().Retrive(id);
	}
	
	public void toSendMail(String email, String id) throws Exception {
		this.facturama.Cfdis().SendEmail(email, CfdiService.InvoiceType.Issued, id);
	}
	
	public static void main(String ... args) throws Exception {
//		double subtotal= Numero.toRedondear(48.9071);
//		double impuestos= Numero.toRedondear(7.8251);
//		double total= Numero.toRedondear(56.7322);
//		double diferencia= Numero.toRedondear(total- (impuestos+ subtotal));
//		LOG.info("subtotal: "+ Numero.toRedondear(48.9071));
//		LOG.info("impuesto: "+ Numero.toRedondear(7.8251));
//		LOG.info("total: "+ Numero.toRedondear(56.7322)+ " calculado: "+ Numero.toRedondear(impuestos+ subtotal)+ " ajustado: "+ Numero.toRedondear(impuestos+ subtotal+ diferencia));
//		subtotal+= diferencia;
//		LOG.info("original: "+ Numero.toRedondear(48.9071)+ " diferencia: "+ diferencia+ "  subtotal: "+ subtotal+ " total: "+ Numero.toRedondear(impuestos+ subtotal));

    CFDIFactory.getInstance().downpdf("/home/ferreter/servicios/produccion/facturama/1/2020/ENERO/ADC190402MT8/", "ADC190402MT8-8781", "PgPhuWwpq0Hs85QOyP5vqg2");
		LOG.error("Ok.");
	} 	
}