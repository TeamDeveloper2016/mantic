package mx.org.kaana.libs.facturama.reglas;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.org.kaana.kajool.enums.EFormatos;
import mx.org.kaana.libs.facturama.container.FacturamaApi;
import mx.org.kaana.libs.facturama.models.Address;
import mx.org.kaana.libs.facturama.models.Client;
import mx.org.kaana.libs.facturama.models.Product;
import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.request.ProductTax;
import mx.org.kaana.libs.facturama.models.response.Cfdi;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.services.CfdiService;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.mantic.facturas.beans.ArticuloFactura;
import mx.org.kaana.mantic.facturas.beans.ClienteFactura;

/**
 *@company MANTIC
 *@project KAANA (Sistema de seguimiento y control de proyectos)
 *@date 30/10/2018
 *@time 10:30:40 PM 
 *@author Team Developer 2018 <team.developer@gmail.com>
 */

public class CFDIFactory implements Serializable {

	private static final long serialVersionUID =-5361573067043698091L;
  private static final String USER           = "FERRBONANZA";
  private static final String PASSWORD       = "ZABONAN2018";
  private static final String USER_PU        = "FERRBONANZASANDBOX";
  private static final String PASSWORD_PU    = "zabonan2018sandbox";
  private static final String DESCRIPCION_IVA= "IVA";
	private static final Boolean PRODUCTION    = true;
	
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
		if(this.PRODUCTION)
			this.facturama= new FacturamaApi(this.USER, this.PASSWORD, false);
		else
			this.facturama= new FacturamaApi(this.USER_PU, this.PASSWORD_PU, true);
  }

  /**
   * Devuelve la instancia de la clase.
   *
   * @return Instancia de la clase.
   */
  public static CFDIFactory getInstance() {
    synchronized (mutex) {
      if (instance == null) {
        instance = new CFDIFactory();
      }
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

	public boolean facturar(){
		boolean regresar= false;
		try {
			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch
		finally {
			
		} // finally
		return regresar;
	} // facturar
	
	public String createClientId(ClienteFactura detalleCliente) throws Exception {
		String regresar= null;
		Client cliente = null;
		try {
			cliente= loadCliente(detalleCliente);
			regresar= createClient(cliente).getId();
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
			cliente= loadCliente(detalleCliente);
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
			regresar= this.facturama.Clients().Create(client);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // createClient
	
	public Client updateClient(ClienteFactura detalleCliente) throws Exception {
		return updateClient(detalleCliente, detalleCliente.getIdFacturama());
	} // updateCliente
	
	public Client updateClient(ClienteFactura detalleCliente, String id) throws Exception{
		Client regresar= null;
		Client cliente = null;
		try {
			cliente= loadCliente(detalleCliente);
			regresar= this.facturama.Clients().Update(cliente, id);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // updateCliente
	
	public Client clientFindById(String id) throws Exception, Exception {
		Client regresar= null;
		try {
			regresar= this.facturama.Clients().Retrieve(id);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // clientFindById
	
	public Client clientRemove(String id) throws Exception {
		Client regresar= null;
		try {
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
			product= loadProduct(detalleArticulo);
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
			product= loadProduct(detalleArticulo);
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
		Product product = null;
		try {
			product= loadProduct(detalleArticulo);
			regresar= this.facturama.Products().Update(product, id);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch	
		return regresar;
	} // updateProduct
	
	public Product productFindById(String id) throws Exception {
		Product regresar= null;
		try {
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
			if(articulo.getIva() > 0D){
				taxes= new ArrayList();
				taxes.add(loadTaxes(articulo.getIva()));
				regresar.setTaxes(taxes);
			} // if
			regresar.setUnit(articulo.getUnidad());
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
	
	private ProductTax loadTaxes(Double iva) {
		ProductTax regresar= null;
		try {
			regresar= new ProductTax();
      regresar.setName(DESCRIPCION_IVA);
      regresar.setRate(iva);
      regresar.setIsRetention(false);
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // loadTaxes
			
	public Cfdi toCfdiDetail(String id) throws FacturamaException, Exception {
	  return this.facturama.Cfdis().Retrive(id);
	}
	
	public void toSendMail(String email, String id) throws Exception {
		this.facturama.Cfdis().SendEmail(email, CfdiService.InvoiceType.Issued, id);
	}	
}