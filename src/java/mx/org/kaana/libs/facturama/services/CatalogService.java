package mx.org.kaana.libs.facturama.services;

import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.response.catalogs.Catalog;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.util.List;
import mx.org.kaana.libs.facturama.models.response.catalogs.cfdi.*;

public class CatalogService extends HttpService {

	public CatalogService(OkHttpClient client) {
		super(client, "catalogs");
		singleType = Catalog.class;
		multiType = new TypeToken<List<Catalog>>() {
		}.getType();

	}

	public List<Unit> Units() throws IOException, FacturamaException, Exception {
		return GetList("/units", new TypeToken<List<Unit>>() {
		}.getType());
	}

	public List<Unit> Units(String keyword) throws IOException, FacturamaException, Exception {
		return GetList("/units?keyword=" + keyword, new TypeToken<List<Unit>>() {
		}.getType());
	}

	public List<NameCfdi> NameIds() throws IOException, FacturamaException, Exception {
		return GetList("/NameIds", new TypeToken<List<NameCfdi>>() {
		}.getType());
	}

	public List<ProductServices> ProductsOrServices(String keyword) throws IOException, FacturamaException, Exception {
		return GetList("/ProductsOrServices?keyword=" + keyword, new TypeToken<List<ProductServices>>() {
		}.getType());
	}

	public List<Currency> Currencies() throws IOException, FacturamaException, Exception {
		return GetList("/currencies", new TypeToken<List<Currency>>() {
		}.getType());
	}

	public List<Currency> Currencies(String keyword) throws IOException, FacturamaException, Exception {
		return GetList("/currencies?keyword=" + keyword, new TypeToken<List<Currency>>() {
		}.getType());
	}

	public List<Catalog> PaymentForms() throws IOException, FacturamaException, Exception {
		return GetList("/paymentforms");
	}

	public List<Catalog> PaymentMethods() throws IOException, FacturamaException, Exception {
		return GetList("/paymentmethods");
	}

	public List<UseCfdi> CfdiUses(String keyword) throws IOException, FacturamaException, Exception {
		return GetList("/CfdiUses?keyword=" + keyword, new TypeToken<List<UseCfdi>>() {
		}.getType());

	}

	public List<FiscalRegimen> FiscalRegimens() throws IOException, FacturamaException, Exception {
		return GetList("/FiscalRegimens", new TypeToken<List<FiscalRegimen>>() {
		}.getType());

	}

}
