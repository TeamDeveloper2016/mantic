package mx.org.kaana.libs.facturama.container;

import mx.org.kaana.libs.facturama.services.*;
import com.squareup.okhttp.OkHttpClient;

public class FacturamaApi {

	private ClientService _clients;
	private ProductService _products;
	private CatalogService _catalogs;
	private BranchOfficeService _branchOffices;
	private CfdiService _cfdis;

	/**
	 *
	 * @param user Nickname en la cuenta de Facturama (la mísma con la que puede
	 * ingresar a la WEB)
	 * @param password Contraseña de la cuenta de Facturama (la mísma con la que
	 * puede ingresar a la WEB)
	 * @param isDevelopment Bandera de modo de Pruebas o Producción (True = Modo
	 * de pruebas)
	 */
	public FacturamaApi(String user, String password, boolean isDevelopment) {
		String host = isDevelopment ? "apisandbox.facturama.mx" : "api.facturama.mx";
		String schema = isDevelopment ? "https" : "https";
		int port = isDevelopment ? 443 : 443;

		OkHttpClient httpClient = new OkHttpClient().setAuthenticator(new BasicAuthInterceptor(user, password));
		httpClient.interceptors().add(new BaseUrlInterceptor(schema, host, port));

		_clients = new ClientService(httpClient);
		_products = new ProductService(httpClient);
		_catalogs = new CatalogService(httpClient);
		_branchOffices = new BranchOfficeService(httpClient);
		_cfdis = new CfdiService(httpClient);
	}

	public ClientService Clients() {
		return _clients;
	}

	public ProductService Products() {
		return _products;
	}

	public CatalogService Catalogs() {
		return _catalogs;
	}

	public BranchOfficeService BranchOffices() {
		return _branchOffices;
	}

	public CfdiService Cfdis() {
		return _cfdis;
	}

}
