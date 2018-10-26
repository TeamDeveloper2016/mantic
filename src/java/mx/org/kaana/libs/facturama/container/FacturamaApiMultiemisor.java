package mx.org.kaana.libs.facturama.container;

import mx.org.kaana.libs.facturama.services.CatalogService;
import mx.org.kaana.libs.facturama.services.multiemisor.*;
import com.squareup.okhttp.OkHttpClient;

public class FacturamaApiMultiemisor {

	private CsdService _csd;
	private CatalogService _catalogs;
	private CfdiService _cfdis;

	public FacturamaApiMultiemisor(String user, String password, boolean isDevelopment) {
		String host = isDevelopment ? "apisandbox.facturama.mx" : "api.facturama.mx";
		String schema = isDevelopment ? "https" : "https";
		int port = isDevelopment ? 443 : 443;

		OkHttpClient httpClient = new OkHttpClient().setAuthenticator(new BasicAuthInterceptor(user, password));
		httpClient.interceptors().add(new BaseUrlInterceptor(schema, host, port));

		_csd = new CsdService(httpClient);
		_catalogs = new CatalogService(httpClient);
		_cfdis = new CfdiService(httpClient);

	}

	public CsdService Csd() {
		return _csd;
	}

	public CatalogService Catalogs() {
		return _catalogs;
	}

	public Object Clients() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public CfdiService Cfdis() {
		return _cfdis;
	}

}
