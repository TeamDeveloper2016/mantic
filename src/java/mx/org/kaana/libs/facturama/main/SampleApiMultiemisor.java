package mx.org.kaana.libs.facturama.main;

import mx.org.kaana.libs.facturama.container.FacturamaApiMultiemisor;
import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.Csd;
import mx.org.kaana.libs.facturama.models.request.CfdiType;
import mx.org.kaana.libs.facturama.models.request.Issuer;
import mx.org.kaana.libs.facturama.models.request.Item;
import mx.org.kaana.libs.facturama.models.request.Receiver;
import mx.org.kaana.libs.facturama.models.request.Tax;
import mx.org.kaana.libs.facturama.models.response.catalogs.Catalog;
import mx.org.kaana.libs.facturama.models.response.catalogs.cfdi.Currency;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import mx.org.kaana.libs.facturama.models.request.Payment;
import mx.org.kaana.libs.facturama.models.request.RelatedDocument;
import mx.org.kaana.libs.facturama.models.request.complements.Complements;
import mx.org.kaana.libs.facturama.models.request.complements.ine.IdContabilidad;
import mx.org.kaana.libs.facturama.models.request.complements.ine.Ine;
import mx.org.kaana.libs.facturama.models.request.complements.ine.IneEntidad;
import mx.org.kaana.libs.facturama.models.response.CancelationStatus;
import mx.org.kaana.libs.facturama.models.response.catalogs.cfdi.NameCfdi;

public class SampleApiMultiemisor {

  public static void principal() {
    System.out.println("Ejemplos de consumo de la FacturamaAPI Multiemisor, con el usuario de 'pruebas'");

    try {

      FacturamaApiMultiemisor facturama = createApiInstance();

      // Ejemplo de administración de CSDs (descomenta la linea para incluirlo en la ejecución)
      //sampleCsd(facturama);
      // Ejemplo de creación de CFDI ..3
      //sampleCfdi(facturama);
      // Ejemplo de creación de CFDI 4.0
      sampleCfdi40(facturama);

      //Test Cancelación
      //TestCancel(facturama);
      // Ejemplo de creación de "Complemento de Pago"
      //samplePaymentComplement(facturama);
    } catch (FacturamaException ex) {
      // Se muestran los errores
      System.out.println(ex.getMessage());

      Map<String, String[]> messageDetail = ex.getModel().getDetails();
      messageDetail.entrySet().forEach((entry) -> {
        System.out.println(entry.getKey() + ": " + String.join(",", entry.getValue()));
      });

    } catch (Exception ex) {
      System.out.println("Error inesperado: " + ex.getMessage());
    }

  }

  /*
     * Creación del Objeto Facturama Multiemisor
     * @return Objeto creado, listo para hacer las peticiones a la API
   */
  private static FacturamaApiMultiemisor createApiInstance() {
    String user = "pruebas";
    String password = "pruebas2011";
    Boolean isDevMode = true;       // true  = Sandbox, false = Productivo

    return new FacturamaApiMultiemisor(user, password, isDevMode);
  }

  /*
     * Ejemplo de manejo de los Sellos Digitales CSD
     * - Eliminar
     * - Agregar
     * - Listar
     * - Mostrar CSD específico
     * 
     * @param facturama Objeto Facturama Multiemisor
     * @throws IOException
     * @throws FacturamaException
     * @throws Exception 
   */
  private static void sampleCsd(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {

    System.out.println("----- Inicio del ejemplo de CSD -----");

    System.out.println("Eliminando el Certificado para el RFC EKU9003173C9");

    facturama.Csd().Remove("EKU9003173C9");

    System.out.println("Agregando el Certificado para el RFC EKU9003173C9");
    sampleCsdCreate(facturama);

    // Listado de todos los Certificados que tiene cargados el usuario       
//        System.out.println( "Listado de todos los Certificados que tiene cargados el usuario" );
//        List<Csd> lstCsd = facturama.Csd().List();         
//       for(int i=0;i <lstCsd.size();i++)
//       {
//            Csd csdlist = lstCsd.get(i);
//            System.out.println();
//            System.out.println(csdlist.getCertificate());
//            System.out.println(csdlist.getPrivateKey());
//            System.out.println(csdlist.getPrivateKeyPassword());
//            System.out.println(csdlist.getRfc());                   
//       }
// 
//       // Mostrado del certificado especìfico para el RFC AAA010101AAA
//        System.out.println( "Mostrado del certificado especìfico para el RFC EKU9003173C9" );
//        System.out.println();
//        System.out.println(facturama.Csd().Retrieve("EKU9003173C9").getCertificate());
//        System.out.println(facturama.Csd().Retrieve("EKU9003173C9").getPrivateKey());
//        System.out.println(facturama.Csd().Retrieve("EKU9003173C9").getPrivateKeyPassword());
//        System.out.println(facturama.Csd().Retrieve("EKU9003173C9").getRfc());
//        
//        System.out.println();
//        System.out.println("ejemplo de CSD terminado");
  }

  private static Csd sampleCsdCreate(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {
    Csd newCsd = new Csd();
    newCsd.setCertificate("MIIFuzCCA6OgAwIBAgIUMzAwMDEwMDAwMDA0MDAwMDI0MzQwDQYJKoZIhvcNAQELBQAwggErMQ8wDQYDVQQDDAZBQyBVQVQxLjAsBgNVBAoMJVNFUlZJQ0lPIERFIEFETUlOSVNUUkFDSU9OIFRSSUJVVEFSSUExGjAYBgNVBAsMEVNBVC1JRVMgQXV0aG9yaXR5MSgwJgYJKoZIhvcNAQkBFhlvc2Nhci5tYXJ0aW5lekBzYXQuZ29iLm14MR0wGwYDVQQJDBQzcmEgY2VycmFkYSBkZSBjYWRpejEOMAwGA1UEEQwFMDYzNzAxCzAJBgNVBAYTAk1YMRkwFwYDVQQIDBBDSVVEQUQgREUgTUVYSUNPMREwDwYDVQQHDAhDT1lPQUNBTjERMA8GA1UELRMIMi41LjQuNDUxJTAjBgkqhkiG9w0BCQITFnJlc3BvbnNhYmxlOiBBQ0RNQS1TQVQwHhcNMTkwNjE3MTk0NDE0WhcNMjMwNjE3MTk0NDE0WjCB4jEnMCUGA1UEAxMeRVNDVUVMQSBLRU1QRVIgVVJHQVRFIFNBIERFIENWMScwJQYDVQQpEx5FU0NVRUxBIEtFTVBFUiBVUkdBVEUgU0EgREUgQ1YxJzAlBgNVBAoTHkVTQ1VFTEEgS0VNUEVSIFVSR0FURSBTQSBERSBDVjElMCMGA1UELRMcRUtVOTAwMzE3M0M5IC8gWElRQjg5MTExNlFFNDEeMBwGA1UEBRMVIC8gWElRQjg5MTExNk1HUk1aUjA1MR4wHAYDVQQLExVFc2N1ZWxhIEtlbXBlciBVcmdhdGUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCN0peKpgfOL75iYRv1fqq+oVYsLPVUR/GibYmGKc9InHFy5lYF6OTYjnIIvmkOdRobbGlCUxORX/tLsl8Ya9gm6Yo7hHnODRBIDup3GISFzB/96R9K/MzYQOcscMIoBDARaycnLvy7FlMvO7/rlVnsSARxZRO8Kz8Zkksj2zpeYpjZIya/369+oGqQk1cTRkHo59JvJ4Tfbk/3iIyf4H/Ini9nBe9cYWo0MnKob7DDt/vsdi5tA8mMtA953LapNyCZIDCRQQlUGNgDqY9/8F5mUvVgkcczsIgGdvf9vMQPSf3jjCiKj7j6ucxl1+FwJWmbvgNmiaUR/0q4m2rm78lFAgMBAAGjHTAbMAwGA1UdEwEB/wQCMAAwCwYDVR0PBAQDAgbAMA0GCSqGSIb3DQEBCwUAA4ICAQBcpj1TjT4jiinIujIdAlFzE6kRwYJCnDG08zSp4kSnShjxADGEXH2chehKMV0FY7c4njA5eDGdA/G2OCTPvF5rpeCZP5Dw504RZkYDl2suRz+wa1sNBVpbnBJEK0fQcN3IftBwsgNFdFhUtCyw3lus1SSJbPxjLHS6FcZZ51YSeIfcNXOAuTqdimusaXq15GrSrCOkM6n2jfj2sMJYM2HXaXJ6rGTEgYmhYdwxWtil6RfZB+fGQ/H9I9WLnl4KTZUS6C9+NLHh4FPDhSk19fpS2S/56aqgFoGAkXAYt9Fy5ECaPcULIfJ1DEbsXKyRdCv3JY89+0MNkOdaDnsemS2o5Gl08zI4iYtt3L40gAZ60NPh31kVLnYNsmvfNxYyKp+AeJtDHyW9w7ftM0Hoi+BuRmcAQSKFV3pk8j51la+jrRBrAUv8blbRcQ5BiZUwJzHFEKIwTsRGoRyEx96sNnB03n6GTwjIGz92SmLdNl95r9rkvp+2m4S6q1lPuXaFg7DGBrXWC8iyqeWE2iobdwIIuXPTMVqQb12m1dAkJVRO5NdHnP/MpqOvOgLqoZBNHGyBg4Gqm4sCJHCxA1c8Elfa2RQTCk0tAzllL4vOnI1GHkGJn65xokGsaU4B4D36xh7eWrfj4/pgWHmtoDAYa8wzSwo2GVCZOs+mtEgOQB91/g==");
    newCsd.setPrivateKey("MIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQIAgEAAoIBAQACAggAMBQGCCqGSIb3DQMHBAgwggS8AgEAMASCBMh4EHl7aNSCaMDA1VlRoXCZ5UUmqErAbucRFLOMmsAaFNkyWR0dXIAh0CMjE6NpQIMZhQ0HH/4tHgmwh4kCawGjIwERoG6/IH3mCt7u19J5+m6gUEGOJdEMXj976E5lKCd/EG6t6lCq66GE3rgux/nFmeQZvsjLlzPyhe2j+X81LrGudITTjDdgLI0EdbdV9CUJwWbibzrVxjuAVShRh07XPL/DiEw3Wk2+kdy4cfWmMvh0U55p0RKZopNkWuVVSvr3ai7ZNCwHZWDVqkUDpwDDGdyt0kYQ7qoKanIxv/A9wv6ekq0LQ/yLlOcelkxQeb8Glu4RXe+krRvrASw1eBAQ3mvNKpngwF8vtlyoil41PjHUOKALMJtNpywckRRYOk4703ylWIzTfdBlrZ6VmDBjdC5723G1HAx3R/x+o+08++RNiFaN06Ly5QbZZvjnealDfSKz1VKRHWeXggaW87rl4n0SOOWnvabKs4ZWRXTS0dhWK+KD/yYYQypTslDSXQrmyMkpc1Zcb4p9RTjodXxGCWdsR5i5+Ro/RiJvxWwwaO3YW6eaSavV0ROqANQ+A+GizMlxsVjl6G5Ooh6ORdA7jTNWmK44Icgyz6QFNh+J3NibxVK2GZxsQRi+N3HXeKYtq5SDXARA0BsaJQzYfDotA9LFgmFKg9jVhtcc1V3rtpaJ5sab8tdBTPPyN/XT8fA0GxlIX+hjLd3E9wB7qzNR6PZ84UKDxhCGWrLuIoSzuCbr+TD9UCJprsfTu8kr8Pur4rrxm7Zu1MsJRR9U5Ut+O9FZfw4SqGykyTGGh0v1gDG8esKpTW5MKNk9dRwDNHEmIF6tE6NeXDlzovf8VW6z9JA6AVUkgiFjDvLUY5MgyTqPB9RJNMSAZBzrkZgXyHlmFz2rvPqQGFbAtukjeRNS+nkVayLqfQnqpgthBvsgDUgFn03z0U2Svb094Q5XHMeQ4KM/nMWTEUC+8cybYhwVklJU7FBl9nzs66wkMZpViIrVWwSB2k9R1r/ZQcmeL+LR+WwgCtRs4It1rNVkxXwYHjsFM2Ce46TWhbVMF/h7Ap4lOTS15EHC8RvIBBcR2w1iJ+3pXiMeihArTELVnQsS31X3kxbBp3dGvLvW7PxDlwwdUQOXnMoimUCI/h0uPdSRULPAQHgSp9+TwqI0Uswb7cEiXnN8PySN5Tk109CYJjKqCxtuXu+oOeQV2I/0knQLd2zol+yIzNLj5a/HvyN+kOhIGi6TrFThuiVbbtnTtRM1CzKtFGuw5lYrwskkkvenoSLNY0N85QCU8ugjc3Bw4JZ9jNrDUaJ1Vb5/+1GQx/q/Dbxnl+FK6wMLjXy5JdFDeQyjBEBqndQxrs9cM5xBnl6AYs2Xymydafm2qK0cEDzwOPMpVcKU8sXS/AHvtgsn+rjMzW0wrQblWE0Ht/74GgfCj4diCDtzxQ0ggi6yJD+yhLZtVVqmKS3Gwnj9RxPLNfpgzPP01eYyBBi/W0RWTzcTb8iMxWX52MTU0oX9//4I7CAPXn0ZhpWAAIvUmkfjwfEModH7iwwaNtZFlT2rlzeshbP++UCEtqbwvveDRhmr5sMYkl+duEOca5156fcRy4tQ8Y3moNcKFKzHGMenShEIHz+W5KE=");
    newCsd.setPrivateKeyPassword("12345678a");
    newCsd.setRfc("EKU9003173C9");
    return facturama.Csd().Create(newCsd);

  }

  //Test CFDI 3.3
  private static void sampleCfdi(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {

    System.out.println("----- Inicio del ejemplo de CFDI -----");

    // Se obtiene la moneda con el valor "MXN"
    List<Currency> lstCurrencies = facturama.Catalogs().Currencies();
    Currency currency = lstCurrencies.stream().
            filter(p -> p.getValue().equals("MXN")).findFirst().get();

    // -------- Creacion del cfdi en su forma general (sin items / productos) asociados --------
    mx.org.kaana.libs.facturama.models.request.CfdiLite cfdi = createCfdi(facturama, currency);

    // -------- Agregar los items que lleva el cfdi ( para este ejemplo, se agregan con datos aleatorios) --------        
    cfdi = addItemsToCfdi(facturama, cfdi);

    // Se obtiene la factura recien creada
    mx.org.kaana.libs.facturama.models.response.Cfdi cfdiCreated = facturama.Cfdis().Create(cfdi);
    //com.Facturama.sdk_java.Models.Response.Cfdi cfdiCreated = facturama.Cfdis().Create3(cfdi); // CFDI 4.0 Disponible hasta el 30 de Junio 2022

    System.out.println("Se creó exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid()); // CFDI 3.3
    //System.out.println( "Se creó exitosamente el cfdi con el folio fiscal: " +  cfdiCreated.getComplement().getTaxStamp().getUuid() ); // cfdi 4.0 test

    // Descarga de los archivos de la factura
    //String filePath = "factura"+cfdiCreated.getComplement().getTaxStamp().getUuid();
    //facturama.Cfdis().SaveXml(filePath+".xml", cfdiCreated.getId());
    // Se elmina la factura recien creada
    CancelationStatus response = facturama.Cfdis().Remove(cfdiCreated.getId(), "02", "d8e34bab-5bd4-4788-bde2-1428dc469e10");

    System.out.println(response.getStatus());

    String strCanceled = "canceled";
    String strPending = "canceled";
    if (strCanceled.equals(response.getStatus())) {
      System.out.println("Se ha cancelado exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid());
    } else if (strPending.equals(response.getStatus())) {
      System.out.println("La factura está en proceso de cancelación, pueden pasar hasta 72 horas para que se considere cancelada.");
    } else {
      System.out.println("Algo ha pasado, que el CFDI no se ha podido cancelar. Revisa el mensaje: " + response.getMessage());
    }

    //El correo que se ingrese debe existir 
    // Consulta de cfdis mediante palabra clave o rfc
    //System.out.println( "Consulta de RFCs mediante RFC" );  
    //List<CfdiSearchResult> lstCfdiFilteredByKeyword = facturama.Cfdis().List("Expresion en Software");
    //List<CfdiSearchResult> lstCfdiFilteredByRfc = facturama.Cfdis().ListFilterByRfc("ESO1202108R2");                
    //System.out.println("Se obtiene la lista de facturas: " + lstCfdiFilteredByKeyword.size());
    //System.out.println("Se obtiene la lista de facturas por RFC: " + lstCfdiFilteredByRfc.size());
    System.out.println("----- Fin del ejemplo de CFDI -----");

  }

  //Test CFDI 4.0 Multiemisor
  private static void sampleCfdi40(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {

    System.out.println("----- Inicio del ejemplo de CFDI 4.0 -----");

    // Se obtiene la moneda con el valor "MXN"
    List<Currency> lstCurrencies = facturama.Catalogs().Currencies();
    Currency currency = lstCurrencies.stream().
            filter(p -> p.getValue().equals("MXN")).findFirst().get();

    // -------- Creacion del cfdi en su forma general (sin items / productos) asociados --------
    mx.org.kaana.libs.facturama.models.request.CfdiLite cfdi = createCfdi40(facturama, currency);

    // -------- Agregar los items que lleva el cfdi ( para este ejemplo, se agregan con datos aleatorios) --------        
    cfdi = addItemsToCfdi(facturama, cfdi);

    // Se obtiene la factura recien creada
    mx.org.kaana.libs.facturama.models.response.Cfdi cfdiCreated = facturama.Cfdis().Create3(cfdi);

    System.out.println("Se creó exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid());

    // Descarga de los archivos de la factura
    //String filePath = "factura"+cfdiCreated.getComplement().getTaxStamp().getUuid();
    //facturama.Cfdis().SaveXml(filePath+".xml", cfdiCreated.getId());
    // Se elmina la factura recien creada
    CancelationStatus response = facturama.Cfdis().Remove(cfdiCreated.getId(), "02", "");
    System.out.println(response.getStatus());

    String strCanceled = "canceled";
    String strPending = "canceled";
    if (strCanceled.equals(response.getStatus())) {
      System.out.println("Se ha cancelado exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid());
    } else if (strPending.equals(response.getStatus())) {
      System.out.println("La factura está en proceso de cancelación, pueden pasar hasta 72 horas para que se considere cancelada.");
    } else {
      System.out.println("Algo ha pasado, que el CFDI no se ha podido cancelar. Revisa el mensaje: " + response.getMessage());
    }

    //El correo que se ingrese debe existir 
    // Consulta de cfdis mediante palabra clave o rfc
    //System.out.println( "Consulta de RFCs mediante RFC" );  
    //List<CfdiSearchResult> lstCfdiFilteredByKeyword = facturama.Cfdis().List("Expresion en Software");
    //List<CfdiSearchResult> lstCfdiFilteredByRfc = facturama.Cfdis().ListFilterByRfc("ESO1202108R2");                
    //System.out.println("Se obtiene la lista de facturas: " + lstCfdiFilteredByKeyword.size());
    //System.out.println("Se obtiene la lista de facturas por RFC: " + lstCfdiFilteredByRfc.size());
    System.out.println("----- Fin del ejemplo de CFDI -----");

  }

  private static mx.org.kaana.libs.facturama.models.request.CfdiLite createCfdi(FacturamaApiMultiemisor facturama, Currency currency)
          throws IOException, FacturamaException, Exception {

    mx.org.kaana.libs.facturama.models.request.CfdiLite cfdi = new mx.org.kaana.libs.facturama.models.request.CfdiLite();

    // Método de pago       
    Catalog paymentMethod = facturama.Catalogs().PaymentMethods().stream().
            filter(p -> p.getName().equals("Pago en una sola exhibición")).findFirst().get();

    // Forma de pago
    Catalog paymentForm = facturama.Catalogs().PaymentForms().stream().
            filter(p -> p.getName().equals("Efectivo")).findFirst().get();

    // Cliente (se toma como cliente el "cliente generico", aquel que tiene el RFC genérico),
    // (como los clientes son exclusivos para cada usuario, se debe previamente dar de alta este cliente)
    // Lugar de expedición
    cfdi.setFolio("10");
    cfdi.setNameId(facturama.Catalogs().NameIds().get(1).getValue());
    cfdi.setCfdiType(CfdiType.Ingreso.getValue());
    cfdi.setPaymentForm(paymentForm.getValue());
    cfdi.setPaymentMethod(paymentMethod.getValue());
    cfdi.setCurrency(currency.getValue());

    // logo - Se especifica como una URL
    cfdi.setLogoUrl("https://www.ejemplos.co/wp-content/uploads/2015/11/Logo-Chanel.jpg");

    // Datos no fiscales (se muestran en el PDF)
    cfdi.setObservations("Este es un ejemplo de observaciones");
    cfdi.setOrderNumber("123321");
    cfdi.setPaymentBankName("BBVA");
    cfdi.setPaymentAccountNumber("6789");
    cfdi.setPaymentConditions("Condiciones");

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    cfdi.setDate(dateFormat.format(date));
    cfdi.setExpeditionPlace("78140");

    Receiver receiver = new Receiver();
    receiver.setCfdiUse("G03");
    receiver.setName("UNIVERSIDAD ROBOTICA ESPAÑOLA");
    receiver.setRfc("URE180429TM6");
    //receiver.setFiscalRegime("601"); // Valores para CFDI 4.0
    //receiver.setTaxZipCode("65000"); // Valores para CFDI 4.0

    Issuer issuer = new Issuer();
    issuer.setFiscalRegime(facturama.Catalogs().FiscalRegimens().get(0).getValue());
    issuer.setName("ESCUELA KEMPER URGATE");
    issuer.setRfc("EKU9003173C9");
    cfdi.setIssuer(issuer);
    cfdi.setReceiver(receiver);

    return cfdi;

  }

  //Llenado del CFDI 4.0
  private static mx.org.kaana.libs.facturama.models.request.CfdiLite createCfdi40(FacturamaApiMultiemisor facturama, Currency currency)
          throws IOException, FacturamaException, Exception {
    mx.org.kaana.libs.facturama.models.request.CfdiLite cfdi = new mx.org.kaana.libs.facturama.models.request.CfdiLite();

    cfdi.setFolio("100");
    cfdi.setNameId("1");
    cfdi.setCfdiType("I");
    cfdi.setPaymentForm("03");
    cfdi.setPaymentMethod("PUE");
    cfdi.setCurrency("MXN");

    // logo - Se especifica como una URL
    cfdi.setLogoUrl("https://www.ejemplos.co/wp-content/uploads/2015/11/Logo-Chanel.jpg");

    // Datos no fiscales (se muestran en el PDF)
    cfdi.setObservations("Este es un ejemplo de observaciones");
    cfdi.setOrderNumber("123321");
    cfdi.setPaymentBankName("BBVA");
    cfdi.setPaymentAccountNumber("6789");
    cfdi.setPaymentConditions("Condiciones");

    //cfdi = addIneComplement(facturama, cfdi);
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    cfdi.setDate(dateFormat.format(date));
    cfdi.setExpeditionPlace("78140");

    //Exportación
    cfdi.setExportation("01");

    //Nodo Informacion Global
//            GlobalInformation globalinformation=new GlobalInformation();
//            globalinformation.setPeriodicity("02");
//            globalinformation.setMonths("04");
//            globalinformation.setYear("2022");
//            cfdi.setGlobalInformation(globalinformation);
//            
    Receiver receiver = new Receiver();
    receiver.setRfc("XAXX010101000");
    receiver.setName("PUBLICO GENERAL");
    receiver.setCfdiUse("S01");
    receiver.setTaxZipCode("78140");
    receiver.setFiscalRegime("616");

    Issuer issuer = new Issuer();
    issuer.setFiscalRegime("601");
    issuer.setName("ESCUELA KEMPER URGATE");
    issuer.setRfc("EKU9003173C9");

    cfdi.setIssuer(issuer);
    cfdi.setReceiver(receiver);

    return cfdi;

  }

  private static mx.org.kaana.libs.facturama.models.request.CfdiLite addItemsToCfdi(FacturamaApiMultiemisor facturama,
          mx.org.kaana.libs.facturama.models.request.CfdiLite cfdi) throws IOException, FacturamaException, Exception {

    Double price = 100.00;
    Double quantity = 2.00;
    Double discount = 10.00;
    Currency currency = facturama.Catalogs().Currency("MXN");
    int decimals = (int) currency.getDecimals();
    Double numberOfDecimals = Math.pow(10, decimals);

    Double subtotal = Math.round((price * quantity) * numberOfDecimals) / numberOfDecimals;
    List<Item> lstItems = new ArrayList<>();
    Item item = new Item();
    //item.setProductCode(facturama.Catalogs().ProductsOrServices("desarrollo").get(0).getValue());
    item.setProductCode("84111506");
    item.setUnitCode(facturama.Catalogs().Units("pieza").get(1).getValue());
    item.setUnit("Libra");
    item.setDescription("Descripción del Producto");
    item.setIdentificationNumber("010101-56");
    item.setQuantity(quantity);
    item.setDiscount(Math.round(discount * numberOfDecimals) / numberOfDecimals);
    item.setUnitPrice(Math.round(price * numberOfDecimals) / numberOfDecimals);
    item.setSubtotal(subtotal);
    item.setTaxObject("02");

    //A cuenta de terceros
    /*
            ThirdPartyAccount thirdPartyAccount= new ThirdPartyAccount(); 
            thirdPartyAccount.setRfc("CACX7605101P8");
            thirdPartyAccount.setName("XOCHILT CASAS CHAVEZ");
            thirdPartyAccount.setFiscalRegime("616");
            thirdPartyAccount.setTaxZipCode("10740");
            item.setThirdPartyAccount(thirdPartyAccount);
            item.setCuentaPredial("12345");
     */
    //Número Pedimento
    /*
            ArrayList<String> numerosPedimento=new ArrayList<>();
            numerosPedimento.add("21  47  3807  8003832");
            numerosPedimento.add("21  47  3807  8003832");
            numerosPedimento.add("21  47  3807  8003832");
            item.setNumerosPedimento(numerosPedimento);
     */
    lstItems.add(item);

    item = addTaxesToItem(item, numberOfDecimals);

    cfdi.setItems(lstItems);

    return cfdi;
  }

  private static Item addTaxesToItem(Item item, Double numberOfDecimals) {

    List<Tax> lstTaxes = new ArrayList<>();              // Impuestos del item (del cfdi)

    Tax tax = new Tax();

    tax.setName("IVA");
    tax.setIsQuota(false);
    tax.setIsRetention(false);

    tax.setRate(0.160000d);
    tax.setBase(Math.round(item.getSubtotal() * numberOfDecimals) / numberOfDecimals);
    tax.setTotal(Math.round((/*cambie el baseAmount*/tax.getBase() * tax.getRate()) * numberOfDecimals) / numberOfDecimals);

    lstTaxes.add(tax);

    Double retentionsAmount = 0D;
    Double transfersAmount = 0D;

    // Asignación de los impuestos, en caso de que no se tengan, el campo va nulo
    if (!lstTaxes.isEmpty()) {
      item.setTaxes(lstTaxes);

      retentionsAmount = item.getTaxes().stream().filter(o -> o.getIsRetention()).mapToDouble(o -> o.getTotal()).sum();
      transfersAmount = item.getTaxes().stream().filter(o -> !o.getIsRetention()).mapToDouble(o -> o.getTotal()).sum();
    }

    // Calculo del subtotal
    item.setTotal(item.getSubtotal() - item.getDiscount() + transfersAmount - retentionsAmount);
    //item.setObjetoImp("02");// Nuevo elemento CFDI 4.0
    return item;

  }

  /*
     * Ejemplo de creación de un CFDI "complemento de pago"
     * Referencia: https://apisandbox.facturama.mx/guias/api-web/cfdi/complemento-pago
     * 
     * En virtud de que el complemento de pago, requiere ser asociado a un CFDI con el campo "PaymentMethod" = "PPD"
     * En este ejemplo se incluye la creacón de este CFDI, para posteriormente realizar el  "Complemento de pago" = "PUE"     
   */
  private static void samplePaymentComplement(FacturamaApiMultiemisor facturama)
          throws IOException, FacturamaException, Exception {

    System.out.println("----- Inicio del ejemplo samplePaymentComplement -----");

    System.out.println("Creación del CFDI Inicial (PPD)");
    // Cfdi Incial (debe ser "PPD")
    // -------- Creacion del cfdi en su forma general (sin items / productos) asociados --------
    mx.org.kaana.libs.facturama.models.request.CfdiLite cfdi = createModelCfdiGeneral(facturama);

    // -------- Agregar los items que lleva el cfdi ( para este ejemplo, se agregan con datos aleatorios) --------        
    cfdi = addItemsToCfdi(facturama, cfdi);
    cfdi.setFolio("11");
    cfdi.setPaymentMethod("PPD");                   // El método de pago del documento inicial debe ser "PPD"

    // logo - Se especifica como una URL
    cfdi.setLogoUrl("https://www.ejemplos.co/wp-content/uploads/2015/11/Logo-Chanel.jpg");

    // Se manda timbrar mediante Facturama
    mx.org.kaana.libs.facturama.models.response.Cfdi cfdiInicial = facturama.Cfdis().Create(cfdi);

    System.out.println("Se creó exitosamente el cfdi Inicial (PPD) con el folio fiscal: " + cfdiInicial.getComplement().getTaxStamp().getUuid());

    // Descarga de los archivos del documento inicial
    String filePath = "factura" + cfdiInicial.getComplement().getTaxStamp().getUuid();
    facturama.Cfdis().SavePdf(filePath + ".pdf", cfdiInicial.getId());
    facturama.Cfdis().SaveXml(filePath + ".xml", cfdiInicial.getId());

    // Complemento de pago (debe ser "PUE")        
    // Y no lleva "Items" solo especifica el "Complemento"
    System.out.println("Creación del complemento de Pago ");

    mx.org.kaana.libs.facturama.models.request.CfdiLite paymentComplementModel = createModelCfdiPaymentComplement(facturama, cfdiInicial);

    // Se manda timbrar el complemento de pago mediante Facturama
    mx.org.kaana.libs.facturama.models.response.Cfdi paymentComplement = facturama.Cfdis().Create(paymentComplementModel);

    System.out.println("Se creó exitosamente el complemento de pago con el folio fiscal: " + paymentComplement.getComplement().getTaxStamp().getUuid());

    // Descarga de los archivos del documento inicial
    String filePathPayment = "factura" + paymentComplement.getComplement().getTaxStamp().getUuid();
    facturama.Cfdis().SavePdf(filePath + ".pdf", paymentComplement.getId());
    facturama.Cfdis().SaveXml(filePath + ".xml", paymentComplement.getId());

    // Posibilidad de mandar  los cfdis por coreo ( el cfdiInical y complemento de pago)
    System.out.println(facturama.Cfdis().SendEmail("chucho@facturama.mx", mx.org.kaana.libs.facturama.services.multiemisor.CfdiService.InvoiceType.IssuedLite, cfdiInicial.getId()));
    System.out.println(facturama.Cfdis().SendEmail("chucho@facturama.mx", mx.org.kaana.libs.facturama.services.multiemisor.CfdiService.InvoiceType.IssuedLite, paymentComplement.getId()));

    System.out.println("----- Fin del ejemplo de samplePaymentComplement -----");

  }

  /*
     * Llenado del modelo de CFDI, de una forma general
     * - Se especifica: la moneda, método de pago, forma de pago, cliente, y lugar de expedición     
   */
  private static mx.org.kaana.libs.facturama.models.request.CfdiLite createModelCfdiGeneral(FacturamaApiMultiemisor facturama)
          throws IOException, FacturamaException, Exception {

    System.out.println("createModelCfdiGeneral");

    Currency currency = facturama.Catalogs().Currency("MXN");

    mx.org.kaana.libs.facturama.models.request.CfdiLite cfdi = new mx.org.kaana.libs.facturama.models.request.CfdiLite();
    // Lista del catálogo de nombres en el PDF
    NameCfdi nameForPdf = facturama.Catalogs().NameIds().get(0); // Nombre en el pdf: "Factura"

    // Método de pago                             
    Catalog paymentMethod = facturama.Catalogs().PaymentMethod("PUE");

    // Forma de pago
    Catalog paymentForm = facturama.Catalogs().PaymentForms().stream().
            filter(p -> p.getName().equals("Efectivo")).findFirst().get();

    cfdi.setNameId(nameForPdf.getValue());
    cfdi.setCfdiType(CfdiType.Ingreso.getValue());
    cfdi.setPaymentForm(paymentForm.getValue());
    cfdi.setPaymentMethod(paymentMethod.getValue());
    cfdi.setCurrency(currency.getValue());
    cfdi.setExpeditionPlace("78180");

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    cfdi.setDate(dateFormat.format(date));

    Receiver receiver = new Receiver();
    receiver.setCfdiUse(facturama.Catalogs().CfdiUses("AAA010101AAA").get(0).getValue());
    receiver.setName("Receptor de Ejemplo");
    receiver.setRfc("ESO1202108R2");

    cfdi.setReceiver(receiver);

    Issuer issuer = new Issuer();
    issuer.setFiscalRegime(facturama.Catalogs().FiscalRegimens().get(0).getValue());
    issuer.setName("Emisor de Ejemplo");
    issuer.setRfc("AAA010101AAA");
    cfdi.setIssuer(issuer);

    return cfdi;

  }

  /*
     * Modelo "Complemento de pago"
     * - Se especifica: la moneda, método de pago, forma de pago, cliente, y lugar de expedición     
   */
  private static mx.org.kaana.libs.facturama.models.request.CfdiLite
          createModelCfdiPaymentComplement(FacturamaApiMultiemisor facturama, mx.org.kaana.libs.facturama.models.response.Cfdi cfdiInicial)
          throws IOException, FacturamaException, Exception {

    System.out.println("createModelCfdiPaymentComplement");

    mx.org.kaana.libs.facturama.models.request.CfdiLite cfdi = new mx.org.kaana.libs.facturama.models.request.CfdiLite();

    // Lista del catálogo de nombres en el PDF
    NameCfdi nameForPdf = facturama.Catalogs().NameIds().get(13); // Nombre en el pdf: "Complemento de pago"

    // Forma de pago
    Catalog paymentForm = facturama.Catalogs().PaymentForms().stream().
            filter(p -> p.getName().equals("Efectivo")).findFirst().get();

    cfdi.setNameId(nameForPdf.getValue());
    cfdi.setCfdiType(CfdiType.Pago.getValue()); // "P"  (El comprobante es del tipo "Pago")         
    cfdi.setFolio("12");

    // logo - Se especifica como una URL
    cfdi.setLogoUrl("https://www.ejemplos.co/wp-content/uploads/2015/11/Logo-Chanel.jpg");

    Receiver receiver = new Receiver();
    receiver.setCfdiUse(facturama.Catalogs().CfdiUses("AAA010101AAA").get(0).getValue());
    receiver.setName("Receptor de Ejemplo");
    receiver.setRfc("ESO1202108R2");
    receiver.setCfdiUse("P01");
    cfdi.setReceiver(receiver);

    Issuer issuer = new Issuer();
    issuer.setFiscalRegime(facturama.Catalogs().FiscalRegimens().get(0).getValue());
    issuer.setName("Emisor de Ejemplo");
    issuer.setRfc("AAA010101AAA");
    cfdi.setIssuer(issuer);

    cfdi.setExpeditionPlace("78180");

    // Fecha y hora de expecidión del comprobante
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date cfdiDate = new Date();
    cfdi.setDate(dateFormat.format(cfdiDate));

    // Complemento de pago ---
    Complements complement = new Complements();

    // Pueden representarse más de un pago en un solo CFDI
    List<Payment> lstPagos = new ArrayList();
    Payment pago = new Payment();

    // Fecha y hora en que se registró el pago en el formato: "yyyy-MM-ddTHH:mm:ss" 
    // (la fecha del pago debe ser menor que la fecha en que se emite el CFDI)
    // Para este ejemplo, se considera que  el pago se realizó hace una hora            
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(cfdiDate);
    calendar.add(Calendar.HOUR_OF_DAY, -1);
    pago.setDate(dateFormat.format(calendar.getTime()));

    // Selección de la moneda del catálogo
    // La Moneda, puede ser diferente a la del documento inicial
    // (En el caso de que sea diferente, se debe colocar el tipo de cambio)
    List<Currency> lstCurrencies = facturama.Catalogs().Currencies();
    Currency currency = lstCurrencies.stream().
            filter(p -> p.getValue().equals("MXN")).findFirst().get();
    pago.setCurrency(currency.getValue());              // Moneda en que se realiza el pago

    // Monto del pago
    // Este monto se puede distribuir entre los documentos relacionados al pago            
    pago.setAmount(100.00);
    pago.setPaymentForm(paymentForm.getValue());

    // Documentos relacionados con el pago
    // En este ejemplo, los datos se obtiene el cfdiInicial, pero puedes colocar solo los datos
    // aun sin tener el "Objeto" del cfdi Inicial, ya que los valores son del tipo "String"
    List<RelatedDocument> lstRelatedDocuments = new ArrayList();
    RelatedDocument relatedDocument = new RelatedDocument();
    relatedDocument.setUuid(cfdiInicial.getComplement().getTaxStamp().getUuid()); // "27568D31-E579-442F-BA77-798CBF30BD7D"
    relatedDocument.setSerie(cfdiInicial.getSerie()); // "EA"
    relatedDocument.setFolio(cfdiInicial.getFolio()); // 34853
    relatedDocument.setCurrency(currency.getValue());
    relatedDocument.setPaymentMethod("PPD");            // Metodo de pago del CFDI Inicial
    relatedDocument.setPartialityNumber(1);
    relatedDocument.setPreviousBalanceAmount(100.00);
    relatedDocument.setAmountPaid(100.00);
    relatedDocument.setImpSaldoInsoluto(0.00);

    lstRelatedDocuments.add(relatedDocument);

    pago.setRelatedDocument(lstRelatedDocuments);

    lstPagos.add(pago);

    complement.setPayments(lstPagos);

    cfdi.setComplements(complement);

    return cfdi;

  }

  //Add Complement    
  private static mx.org.kaana.libs.facturama.models.request.CfdiLite addIneComplement(FacturamaApiMultiemisor facturama,
          mx.org.kaana.libs.facturama.models.request.CfdiLite cfdi) throws IOException, FacturamaException, Exception {

    Complements complement = new Complements();
    Ine ine = new Ine();

//        ine.setVersion("1.1");
//        ine.setTipoProceso("Ordinario");
//        ine.setTipoComite("EjecutivoNacional");
//        ine.setIdContabilidad("123456");
//        ine.setTipoComiteSpecified(true);
    ine.setVersion("1.1");
    ine.setTipoProceso("Precampaña");
    List<IneEntidad> lstentidad = new ArrayList();
    IneEntidad entidad = new IneEntidad();

    entidad.setAmbito("Federal");
    entidad.setClaveEntidad("AGU");

    List<IdContabilidad> lstContabilidad = new ArrayList<>();

    IdContabilidad idContabilidad = new IdContabilidad();
    idContabilidad.setIdContabilidad("123456");
    lstContabilidad.add(idContabilidad);

    entidad.setContabilidad(lstContabilidad);
    lstentidad.add(entidad);
    ine.setEntidad(lstentidad);

    complement.setIne(ine);
    cfdi.setComplements(complement);

    return cfdi;
  }

  private static void TestCancel(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {
    System.out.println("----- Inicio del ejemplo Test Cancelación-----");

    String Cfdi_Id = "";
    // Se elmina la factura recien creada
    CancelationStatus response = facturama.Cfdis().Remove(Cfdi_Id, "02", "");

    System.out.println(response.getStatus());

    String strCanceled = "canceled";
    String strPending = "canceled";
    if (strCanceled.equals(response.getStatus())) {
      System.out.println("Se ha cancelado exitosamente el cfdi con el folio fiscal: " + response.getUuid());
    } else if (strPending.equals(response.getStatus())) {
      System.out.println("La factura está en proceso de cancelación, pueden pasar hasta 72 horas para que se considere cancelada.");
    } else {
      System.out.println("Algo ha pasado, que el CFDI no se ha podido cancelar. Revisa el mensaje: " + response.getMessage());
    }

    System.out.println("----- Fin del ejemplo Test Cancelación -----");
  }

}
