package mx.org.kaana.libs.facturama.main;


import java.io.IOException;
import mx.org.kaana.libs.facturama.container.FacturamaApi;
import mx.org.kaana.libs.facturama.services.*;
import mx.org.kaana.libs.facturama.models.*;
import mx.org.kaana.libs.facturama.models.request.ProductTax;
import mx.org.kaana.libs.facturama.models.request.CfdiType;
import mx.org.kaana.libs.facturama.models.request.Item;
import mx.org.kaana.libs.facturama.models.request.Receiver;
import mx.org.kaana.libs.facturama.models.request.Tax;
import mx.org.kaana.libs.facturama.models.response.catalogs.*;
import mx.org.kaana.libs.facturama.models.response.catalogs.cfdi.*;
import mx.org.kaana.libs.facturama.models.response.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.request.complements.Complements;
import mx.org.kaana.libs.facturama.models.request.Payment;
import mx.org.kaana.libs.facturama.models.request.RelatedDocument;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pepe
 */
public class SampleApiWeb {

    public static void principal() {        
        System.out.println( "Ejemplos de consumo de la FacturamaAPI, con el usuario de 'pruebas'" );
            
       try {     
            // Creación de la instancia
            FacturamaApi facturama = createApiInstance();           
        
            // Prueba de la funcionalidad básica del servicio de clientes
            //sampleClients(facturama);
            
            // Prueba de funcionalidad de crear un producto
            //sampleProducts(facturama);
            
            
            // Prueba de la funcionalidad básica del servicio de CFDI (crear factura)
            sampleCfdi(facturama);            
            
             
            
            
        } catch (FacturamaException ex) {                        
            // Se muestran los errores
            System.out.println( ex.getMessage() );
                        
            Map<String, String[]> messageDetail = ex.getModel().getDetails();
            messageDetail.entrySet().forEach((entry) -> {
                System.out.println(entry.getKey() + ": " +  String.join(",", entry.getValue() ));
            });                                   
            
        } catch (Exception ex) {
            System.out.println( "Error inesperado: " + ex.getMessage() );
        }
        
    }
    
    
    
     /**
    * Creación de una instancia del SDK con los datos del usuario de pruebas
    * Remplaza 'user' y 'password' por los de tu cuenta
    * isDevMode = true : ambiente de pruebas ( no se consumen folios, las facturas realzadas son apócrifas)
    * isDevMode = false : ambiente de producción ( SI se consumen folios, las facturas realzadas son TIMBRADAS por un "PAC" )
    */
    private static FacturamaApi createApiInstance(){
        String user = "pruebas";
        String password = "pruebas2011";
        Boolean isDevMode = true;
        
        return new FacturamaApi(user, password, isDevMode);
    }
    
    
    
    /**
     * Ejemplos de funcionalidad basica de los servicios de "Clientes" 
     * @param facturama Instancia de la api de facturama
     */
    private static void sampleClients( FacturamaApi facturama) throws IOException, FacturamaException, Exception{                  
         List<Client> lstClients = facturama.Clients().List();         
         Integer clientsBefore = lstClients.size();                       
        
         // Creacion de cliente
        Client newClient = sampleClientsCreate(facturama);                
         
         // El ciente obtenido (clientRetrived) seria el mismo que (newClientSaved)         
         String clientRetrivedId = newClient.getId();                 
         Client clientRetrived = facturama.Clients().Retrieve(clientRetrivedId);
         
         // Se modifica el RFC
         clientRetrived.setRfc("XAXX010101000");    
         
         // Edicion
         facturama.Clients().Update(clientRetrived,clientRetrived.getId() );         
         System.out.println( "Cliente Editado" );                          
                  
         // Eliminación
         facturama.Clients().Remove(clientRetrivedId);
         
         // Comparativa de la cantidad de clientes al inicio y final de la prueba,
         // para confirmar que se ha eliminado
         List<Client> lstClientsAfter = facturama.Clients().List();         
         Integer clientsAfter = lstClientsAfter.size();
         
         if(Objects.equals(clientsBefore, clientsAfter)){
             System.out.println( "Creación y eliminacion de cliente correcta!" );
         }else{
             System.out.println( "Creación y eliminacion de cliente erronea!" );
         }
                  
         System.out.println( "Fin del ejemplo de clientes" );
       
    }
    
    private static Client sampleClientsCreate(FacturamaApi facturama) throws IOException, FacturamaException, Exception{
        Client newClient = new Client();    
         
         Address clientAddress = new Address();
         clientAddress.setCountry("MEXICO");
         clientAddress.setExteriorNumber("1230");
         clientAddress.setInteriorNumber("B");
         clientAddress.setLocality("San Luis");                  
         clientAddress.setMunicipality("San Luis Potosí");         
         clientAddress.setNeighborhood("Lomas 4ta");
         clientAddress.setState("San Luis Potosí");
         clientAddress.setStreet("Cañada de Gomez");
         clientAddress.setZipCode("78220");
         
         newClient.setAddress(clientAddress);
         newClient.setCfdiUse("P01");
         newClient.setRfc("ESO1202108R2");
         newClient.setEmail("diego@facturama.com.mx");
         newClient.setName("Expresion en Software");
         
         
         return facturama.Clients().Create(newClient);
    }
    
    private static void sampleProducts( FacturamaApi facturama) throws IOException, FacturamaException, Exception{                  
         List<Product> lstProducts = facturama.Products().List();         
         Integer ProductsBefore = lstProducts.size();                       
        
         // Creacion de Producto
        Product newProduct = sampleProductCreate(facturama);                
         
         // El producto obtenido (ProductRetrived) seria el mismo que (newProductSaved)         
         String ProductRetrivedId = newProduct.getId();                 
         Product ProductRetrived = facturama.Products().Retrieve(ProductRetrivedId);
         
         // Se modifica el Precio
         ProductRetrived.setPrice(100.00);    
         
         // Edicion
         facturama.Products().Update(ProductRetrived,ProductRetrived.getId() );         
         System.out.println( "Producto Editado" );                          
                  
         // Eliminación
         facturama.Products().Remove(ProductRetrivedId);
         
         // Comparativa de la cantidad de Productos al inicio y final de la prueba,
         // para confirmar que se ha eliminado
         List<Product> lstProductsAfter = facturama.Products().List();         
         Integer ProductsAfter = lstProductsAfter.size();
         
         if(Objects.equals(ProductsBefore, ProductsAfter)){
             System.out.println( "Creación y eliminacion de Producto correcta!" );
         }else{
             System.out.println( "Creación y eliminacion de Producto erronea!" );
         }
                  
         System.out.println( "Fin del ejemplo de Productes" );
       
    }
    
    private static Product sampleProductCreate(FacturamaApi facturama) throws IOException, FacturamaException, Exception{
        
        Unit unit = facturama.Catalogs().Units("servicio").get(0);
        ProductServices prod = facturama.Catalogs().ProductsOrServices("desarrollo").get(0);
        
        Product product = new Product();
        List<ProductTax> taxes = new ArrayList();
        
        product.setUnit("Servicio");
        product.setUnitCode(unit.getValue());
        product.setIdentificationNumber("WEB003");
        product.setName("Sitio Web CMS");
        product.setDescription("Desarrollo e implementación de sitio web empleando un CMS");
        product.setPrice(6500.00);
        product.setCodeProdServ(prod.getValue());
        product.setCuentaPredial("123");

        ProductTax iva = new ProductTax();
        iva.setName("IVA");
        iva.setRate(0.16);
        iva.setIsRetention(false);

        /*ProductTax isr = new ProductTax();
        isr.setName("ISR");
        isr.setTotal(0.19);
        isr.setIsRetention(true);*/

        taxes.add(iva);
        //taxes.add(isr);

        product.setTaxes(taxes);
        
        product = facturama.Products().Create(product);
        System.out.println("Se creo exitosamente un producto con el id: " + product.getId());

        return product;
    }
    
    /**
     * Ejemplo Operaciones con el servicio de CFDI (Facturas)     
    */
    private static void sampleCfdi( FacturamaApi facturama) throws IOException, FacturamaException, Exception{  
        
        System.out.println( "----- Inicio del ejemplo de CFDI -----" );      
        
        // Se obtiene la moneda con el valor "MXN"
        List<Currency> lstCurrencies = facturama.Catalogs().Currencies();                
        Currency currency = lstCurrencies.stream().
        filter(p -> p.getValue().equals("MXN")).findFirst().get();
        
        
        // -------- Creacion del cfdi en su forma general (sin items / productos) asociados --------
        mx.org.kaana.libs.facturama.models.request.Cfdi cfdi = createCfdi(facturama, currency);
                
        // -------- Agregar los items que lleva el cfdi ( para este ejemplo, se agregan con datos aleatorios) --------        
        cfdi = addItemsToCfdi(facturama, currency, cfdi);
        
        
        // Se obtiene la factura recien creada
        mx.org.kaana.libs.facturama.models.response.Cfdi cfdiCreated = facturama.Cfdis().Create(cfdi);

        System.out.println( "Se creó exitosamente el cfdi con el folio fiscal: " +  cfdiCreated.getComplement().getTaxStamp().getUuid() );
        
        // Descarga de los archivos de la factura
        String filePath = "factura"+cfdiCreated.getComplement().getTaxStamp().getUuid();
        facturama.Cfdis().SavePdf(filePath+".pdf", cfdiCreated.getId());
        facturama.Cfdis().SaveXml(filePath+".xml", cfdiCreated.getId());
        
        
        // Se elmina la factura recien creada
        facturama.Cfdis().Remove(cfdiCreated.getId());        
        System.out.println( "Se elminó exitosamente el cfdi con el folio fiscal: " +  cfdiCreated.getComplement().getTaxStamp().getUuid() );
        
        //El correo que se ingrese debe existir 
        System.out.println(facturama.Cfdis().SendEmail("example@.mx",CfdiService.InvoiceType.Issued, "7eo51BvzV-E16gBx3nnxfQ2"));
        // Consulta de cfdis mediante palabra clave o rfc
        List<CfdiSearchResult> lstCfdiFilteredByKeyword = facturama.Cfdis().List("Expresion en Software");
        List<CfdiSearchResult> lstCfdiFilteredByRfc = facturama.Cfdis().ListFilterByRfc("ESO1202108R2");                

        System.out.println("Se obtiene la lista de facturas: " + lstCfdiFilteredByKeyword.size());
        System.out.println("Se obtiene la lista de facturas por RFC: " + lstCfdiFilteredByRfc.size());
        
        System.out.println( "----- Fin del ejemplo de CFDI -----" );
        
    }
    
    
    /**
     * Crea el encabezado ()
     */    
    private static mx.org.kaana.libs.facturama.models.request.Cfdi createCfdi(FacturamaApi facturama, Currency currency) throws IOException, FacturamaException, Exception{
        
        mx.org.kaana.libs.facturama.models.request.Cfdi cfdi = new mx.org.kaana.libs.facturama.models.request.Cfdi();
            // Lista del catálogo de nombres en el PDF
            NameCfdi nameForPdf = facturama.Catalogs().NameIds().get(0); // Nombre en el pdf: "Factura"
                
            // Método de pago       
            Catalog paymentMethod = facturama.Catalogs().PaymentMethods().stream().
            filter(p -> p.getName().equals("Pago en una sola exhibición")).findFirst().get();                

            // Forma de pago
            Catalog paymentForm = facturama.Catalogs().PaymentForms().stream().
            filter(p -> p.getName().equals("Efectivo")).findFirst().get();                


            // Cliente (se toma como cliente el "cliente generico", aquel que tiene el RFC genérico),
            // (como los clientes son exclusivos para cada usuario, se debe previamente dar de alta este cliente)
            Client client = facturama.Clients().List().stream().
            filter(p -> p.getRfc().equals("XAXX010101000")).findFirst().get();                

            // Lugar de expedición
            BranchOffice branchOffice = facturama.BranchOffices().List().get(0);

            cfdi.setNameId(nameForPdf.getValue());
            cfdi.setCfdiType( CfdiType.Ingreso.getValue() );        
            cfdi.setPaymentForm( paymentForm.getValue() );
            cfdi.setPaymentMethod( paymentMethod.getValue() );
            cfdi.setCurrency(currency.getValue());
            cfdi.setExpeditionPlace(branchOffice.getAddress().getZipCode());

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();        
            cfdi.setDate(dateFormat.format(date));            

            Receiver  receiver = new Receiver();
            receiver.setCfdiUse(client.getCfdiUse());
            receiver.setName(client.getName());
            receiver.setRfc(client.getRfc());

            cfdi.setReceiver(receiver);           
            
            Complements complement = new Complements();
            List<Payment> lstPagos = new ArrayList();
            Payment pagos = new Payment();
            
            pagos.setDate("2018-04-05T05:00:00.000Z");
            pagos.setPaymentForm(paymentForm.getValue());
            pagos.setCurrency(currency.getValue());
            pagos.setAmount(100.00);
            
            List<RelatedDocument> lstrelatedDocument = new ArrayList();
            RelatedDocument relatedDocument = new RelatedDocument();
            relatedDocument.setUuid("27568D31-E579-442F-BA77-798CBF30BD7D");
            relatedDocument.setSerie("CB");
            relatedDocument.setFolio("34853");
            relatedDocument.setCurrency(currency.getValue());
            relatedDocument.setPaymentMethod("PUE");
            relatedDocument.setPartialityNumber(1);
            relatedDocument.setPreviousBalanceAmount(100.00);
            relatedDocument.setAmountPaid(100.00);
            
            lstrelatedDocument.add(relatedDocument);
            
            pagos.setRelatedDocument(lstrelatedDocument);
            
            lstPagos.add(pagos);
            
            complement.setPayments(lstPagos);
            
            cfdi.setComplements(complement);
            
       
        return cfdi;
                
    }
    
    
    private static mx.org.kaana.libs.facturama.models.request.Cfdi addItemsToCfdi(FacturamaApi facturama, Currency currency,
       mx.org.kaana.libs.facturama.models.request.Cfdi cfdi) throws IOException, FacturamaException, Exception{
        
        // Lista de todos los productos
        List<Product> lstProducts = facturama.Products().List();
        Random random = new Random();
        
        int nItems = (random.nextInt(lstProducts.size()) % 10) + 1;
        int decimals = (int) currency.getDecimals();
        
        
        // Lista de Items en el cfdi (los articulos a facturar)
        List<Item> lstItems = new ArrayList<>();
        
        // Creacion del CFDI 
        for( int i = lstProducts.size() - nItems; i < lstProducts.size() && i > 0; i++  ){
            
            Product product = lstProducts.get(i);   // Un producto cualquiera
            int quantity = random.nextInt(5) + 1;   // una cantidad aleatoria de elementos de este producto
            Double discount = product.getPrice () % ( product.getPrice() == 0 ? 1 : random.nextInt( (int) product.getPrice() ) );
            
            // Redondeo del precio del producto, de acuerdo a la moneda
            Double numberOfDecimals = Math.pow(10, decimals); 
            Double subTotal = Math.round( (product.getPrice() * quantity) * numberOfDecimals) / numberOfDecimals;
            
            
            // Llenado del item (que va en el cfdi)
            Item item = new Item();
            item.setProductCode(product.getCodeProdServ());
            item.setUnitCode(product.getUnitCode());
            item.setUnit(product.getUnit());
            item.setDescription(product.getDescription());
            item.setIdentificationNumber(product.getIdentificationNumber());
            item.setQuantity(quantity);
            item.setDiscount( Math.round( discount * numberOfDecimals) / numberOfDecimals );
            item.setUnitPrice(Math.round( product.getPrice() * numberOfDecimals) / numberOfDecimals);
            item.setSubtotal(subTotal);
            
            
            
            // ---- Llenado de los impuestos del item ----                                    
            item = addTaxesToItem(item, product, numberOfDecimals);
                        
            lstItems.add(item);                        
            
        }
        
        cfdi.setItems(lstItems);
        
        return cfdi;
    }
    
    
    /**
     * Se agregan los impuestos al Item (uno de los items del cfdi)
     * Se agregan todos los impuestos del producto, en el caso de que no se tengan impuestos, se debe colocar un valor nulo
     */
    private static Item addTaxesToItem(Item item, Product product, Double numberOfDecimals){
        
        List<ProductTax> lstProductTaxes = product.getTaxes(); // impuestos del producto
            List<Tax> lstTaxes = new ArrayList<>();              // Impuestos del item (del cfdi)
            
            Double baseAmount = Math.round( ( item.getSubtotal() - item.getDiscount()) * numberOfDecimals) / numberOfDecimals ;
            
            for(int j = 0; j < lstProductTaxes.size(); j++ ){
                
                ProductTax pTax = lstProductTaxes.get(j);
                
                Tax tax = new Tax();
                
                tax.setName(pTax.getName());
                tax.setIsQuota(pTax.getIsQuota());
                tax.setIsRetention(pTax.getIsRetention());
                if(pTax.getRate() == null)
                {
                    pTax.setRate(0d);
                }
                
                Double rate = pTax.getRate();
                Double rateRounded = (double) Math.round(rate  * 1000000) / 1000000;                
                tax.setRate( rateRounded );
                tax.setBase( Math.round(item.getSubtotal()  * numberOfDecimals) / numberOfDecimals );
                tax.setTotal( Math.round( (/*cambie el baseAmount*/tax.getBase() * pTax.getRate()) * numberOfDecimals) / numberOfDecimals );                
                
                lstTaxes.add(tax);
            }
                        
            
            Double retentionsAmount = 0D;
            Double transfersAmount = 0D;
            
            // Asignación de los impuestos, en caso de que no se tengan, el campo va nulo
            if(!lstTaxes.isEmpty()){
                item.setTaxes(lstTaxes);
                
                retentionsAmount = item.getTaxes().stream().filter(o -> o.getIsRetention()).mapToDouble(o -> o.getTotal()).sum();
                transfersAmount = item.getTaxes().stream().filter(o -> ! o.getIsRetention()).mapToDouble(o -> o.getTotal()).sum();
            }                        
            
            // Calculo del subtotal
            item.setTotal(item.getSubtotal() - item.getDiscount() + transfersAmount -  retentionsAmount);
            
            return item;
            
    }
}
