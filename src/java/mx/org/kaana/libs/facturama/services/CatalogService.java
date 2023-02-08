package mx.org.kaana.libs.facturama.services;

import mx.org.kaana.libs.facturama.models.response.catalogs.cfdi.ProductServices;
import mx.org.kaana.libs.facturama.models.response.catalogs.cfdi.Unit;
import mx.org.kaana.libs.facturama.models.response.catalogs.cfdi.UseCfdi;
import mx.org.kaana.libs.facturama.models.response.catalogs.cfdi.FiscalRegimen;
import mx.org.kaana.libs.facturama.models.response.catalogs.cfdi.Currency;
import mx.org.kaana.libs.facturama.models.response.catalogs.cfdi.NameCfdi;
import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.response.catalogs.Catalog;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.util.List;

public class CatalogService extends HttpService {

  public CatalogService(OkHttpClient client) {
    super(client, "catalogs");
    singleType = Catalog.class;
    multiType = new TypeToken<List<Catalog>>() {}.getType();
  }

  public List<Unit> Units() throws IOException, FacturamaException, Exception {
    return GetList("/units", new TypeToken<List<Unit>>() {}.getType());
  }

  public List<Unit> Units(String keyword) throws IOException, FacturamaException, Exception {
    return GetList("/units?keyword=" + keyword, new TypeToken<List<Unit>>() {}.getType());
  }

  public List<NameCfdi> NameIds() throws IOException, FacturamaException, Exception {
    return GetList("/NameIds", new TypeToken<List<NameCfdi>>() {}.getType());
  }

  public List<ProductServices> ProductsOrServices(String keyword) throws IOException, FacturamaException, Exception {
    return GetList("/ProductsOrServices?keyword=" + keyword, new TypeToken<List<ProductServices>>() {}.getType());
  }

  public List<Currency> Currencies() throws IOException, FacturamaException, Exception {
    return GetList("/currencies", new TypeToken<List<Currency>>() {}.getType());
  }

  public List<Currency> Currencies(String keyword) throws IOException, FacturamaException, Exception {
    return GetList("/currencies?keyword=" + keyword, new TypeToken<List<Currency>>() {}.getType());
  }

  public Currency Currency(String currencyCode) throws IOException, FacturamaException, Exception {
    List<Currency> lstCurrencies = Currencies();
    return lstCurrencies.stream().filter(p -> p.getValue().equals(currencyCode)).findFirst().get();
  }

  public List<Catalog> PaymentForms() throws IOException, FacturamaException, Exception {
    return GetList("/paymentforms");
  }

  /**
   * Métodos de pago, consulta del catálogo Referencia: https://apisandbox.facturama.mx/guias/catalogos/metodos-pago
   *
   * @return
   * @throws IOException
   * @throws FacturamaException
   * @throws Exception
   */
  public List<Catalog> PaymentMethods() throws IOException, FacturamaException, Exception {
    return GetList("/paymentmethods");
  }

  /**
   * Método de pago, consulta de un elemento especifico del catálogo, definido por el valor/código del mismo Valores posibles: PUE | PPD para una referencia completa de los valores posibles,
   * consulta: el catálogo PaymentMethods()
   *
   * @param paymentMethodCode
   * @return
   * @throws IOException
   * @throws FacturamaException
   * @throws Exception
   */
  public Catalog PaymentMethod(String paymentMethodCode) throws IOException, FacturamaException, Exception {
    return PaymentMethods().stream().filter(p -> p.getValue().equals(paymentMethodCode)).findFirst().get();
  }

  public List<UseCfdi> CfdiUses(String keyword) throws IOException, FacturamaException, Exception {
    return GetList("/CfdiUses?keyword=" + keyword, new TypeToken<List<UseCfdi>>() {}.getType());
  }

  public List<FiscalRegimen> FiscalRegimens() throws IOException, FacturamaException, Exception {
    return GetList("/FiscalRegimens", new TypeToken<List<FiscalRegimen>>() {}.getType());
  }

}
