package mx.org.kaana.libs.facturama.main;

import mx.org.kaana.libs.facturama.container.FacturamaApiMultiemisor;
import mx.org.kaana.libs.facturama.models.BranchOffice;
import mx.org.kaana.libs.facturama.models.Client;
import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.Csd;
import mx.org.kaana.libs.facturama.models.request.CfdiType;
import mx.org.kaana.libs.facturama.models.request.Issuer;
import mx.org.kaana.libs.facturama.models.request.Item;
import mx.org.kaana.libs.facturama.models.request.Receiver;
import mx.org.kaana.libs.facturama.models.request.Tax;
import mx.org.kaana.libs.facturama.models.response.catalogs.Catalog;
import mx.org.kaana.libs.facturama.models.response.catalogs.cfdi.Currency;
import mx.org.kaana.libs.facturama.models.response.CfdiSearchResult;
import mx.org.kaana.libs.facturama.services.CfdiService;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Map;

public class SampleApiMultiemisor {

	public static void principal() {
		System.out.println("Ejemplos de consumo de la FacturamaAPI, con el usuario de 'pruebas'");

		try {

			FacturamaApiMultiemisor facturama = createApiInstance();

			sampleCsd(facturama);
			sampleCfdi(facturama);

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

	private static FacturamaApiMultiemisor createApiInstance() {
		String user = "pruebas";
		String password = "pruebas2011";
		Boolean isDevMode = true;

		return new FacturamaApiMultiemisor(user, password, isDevMode);
	}

	private static void sampleCsd(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {
		List<Csd> lstCsd = facturama.Csd().List();
		Integer csdBefore = lstCsd.size();

		facturama.Csd().Remove("AAA010101AAA");

		Csd newCsd = sampleCsdCreate(facturama);

		for (int i = 0; i < lstCsd.size(); i++) {
			Csd csdlist = lstCsd.get(i);
			System.out.println();
			System.out.println(csdlist.getCertificate());
			System.out.println(csdlist.getPrivateKey());
			System.out.println(csdlist.getPrivateKeyPassword());
			System.out.println(csdlist.getRfc());

		}

		System.out.println();

		System.out.println(facturama.Csd().Retrieve("AAA010101AAA").getCertificate());
		System.out.println(facturama.Csd().Retrieve("AAA010101AAA").getPrivateKey());
		System.out.println(facturama.Csd().Retrieve("AAA010101AAA").getPrivateKeyPassword());
		System.out.println(facturama.Csd().Retrieve("AAA010101AAA").getRfc());

		System.out.println();
		System.out.println("ejemplo terminado");

	}

	private static Csd sampleCsdCreate(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {
		Csd newCsd = new Csd();
		newCsd.setCertificate("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");
		newCsd.setPrivateKey("MIIFDjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQIAgEAAoIBAQACAggAMBQGCCqGSIb3DQMHBAgwggS9AgEAMASCBMh4EHl7aNSCaMDA1VlRoXCZ5UUmqErAbucRBAKNQXH8tz2zJ7hdZaOZx7PEfMiWh5Nh6e8G8kxY+GW4YCSbLxslkhBtfTR6v5JYv3vhgH7XzMCwJPOfX6gxeeCYZ4HTdDNAyBVCjTbJpqbo778ri33o+I4yx7zgMqA3mzVE61re6MPrGXh1YT/K9zZeEdmwvXQfPs9VnioKUhiswoMcJ3kc3FxGLrEAsjQqv/ZVOHPY3NrbcfpQUyprsCKv3rRdxkIRdMPY4eiA720mffzvDqyzeQ8xfwHTE8Xjunja4KXvW/mV7ItTH0vRXHc3HJQ0dNnyawXmbC1FiYbCVdswoYuVQmslvq3QEXUGwP3KYfxQzKatnU7nprkmsipPqPBqDrzqc6NSN/8rxIc5zTAL4bFul+CEKz9VybwdavgewEy7u3fPnKPN+y4HilNgmlbtS7seWpbIgVPA+woG2Ph5hsgREXZCjGKSRuI77/FLcI5CMrZR+FvbnaqG+gXDBTz2lWhK9pmWlVawT2pvfiHOLzYRf2YyuVbJ79D2EgbUKyp3kCQ6fddMzspPhD/pvLQizExeyIxImb/kQXs2mmtDnyFIsj4Hcn5wCcs+SDIj+FJnwRiKB6YfdzjIig/ZMfpgMpl0u69LX649uL318o+Hy3d5t3wxgSkTaJ5McKhWyh9x9vlHZhYyM6HArBNfP9cGF86M3GwAMHAiJQl9UevyKe6rlvAIDlop6l3M02m5hHUXUpPjz4j7inFXZzvSv0tFoSbEqGgno0Pa+0gWHqRwBEGLGEwHVfyEy+Of8g4+0jzo0jNPIcurA5xRh9HSRSAd3kdEhx75eeVL7lBdLjRUkbtRtg7nelSjqAX7tQZK6Awp5C/17W96+f/vtjB+Y+ZgrSUjnQDADnZCnapIrzHgE3ZanhGAtnMMl+o4aLd1+74inG4jht/GJB60raSQfYrDrM3kBs0oyfpbEk5TI8ISzRlRmejv+mqpTogJaAqhnLP7rAli3d4pRhUjbACn/xQSFKxl2OURdmnMlvlbb6pleXviJHRxzPPQ25NVdWvmCYWrDfAZYn8X1sABOdyrth38BfmAVsyyPATYFB+5cXuNIZkPz1swz3859iZWTn5JRfPEAGICu5G6w6nrgOLYM9UqOPmxofzEdiEPafLQ5orMxdSWF6+3mD2Yw/VP+B43B/oYehgfrYjBUJt2D04VU/v8XK1ZUVgX/Co0odcdcszAP+ljQ7UVhW+uxVMd2sEprwepPPjYT3HvdI6RBB94yYBWfkoCSo/jsrrRpw2DVEyvoDp/hOXKyt8Y/8UGLCxJUhhv5fEiezYnlUAmwAGjgZfzfAErx0gkQFBgNKglEA7jz0Dqc2Z92pGVGTyPtXqRsqX3IYX5WsZVUoJim0wI7+LNmKpu147ePC0G4Sf4AGoZyPWVXq2SZSPpN261pIKSoLEDeA8WIKj2U5JG2DMMYokV0bZ1TsabrwHvwsp3muLnaP8L+n2fBplbhAEE2buBXvsATixMGu57ZI5WKFLnHn4KIBrZzALCtGehfFbCsdf1nBR6aAt+BpWhhZki54fZTurgMr6zuC5hAaP4rExW+LCc3upHMW7R9DcHWaZuZIfwnVDImnAQ9UOsz+A=");
		newCsd.setPrivateKeyPassword("12345678a");
		newCsd.setRfc("AAA010101AAA");
		return facturama.Csd().Create(newCsd);

	}

	private static void sampleCfdi(FacturamaApiMultiemisor facturama) throws IOException, FacturamaException, Exception {

		System.out.println("----- Inicio del ejemplo de CFDI -----");

		// Se obtiene la moneda con el valor "MXN"
		List<Currency> lstCurrencies = facturama.Catalogs().Currencies();
		Currency currency = lstCurrencies.stream().
						filter(p -> p.getValue().equals("MXN")).findFirst().get();

		// -------- Creacion del cfdi en su forma general (sin items / productos) asociados --------
		mx.org.kaana.libs.facturama.models.request.CfdiLite cfdi = createCfdi(facturama, currency);

		// -------- Agregar los items que lleva el cfdi ( para este ejemplo, se agregan con datos aleatorios) --------        
		cfdi = addItemsToCfdi(facturama, currency, cfdi);

		// Se obtiene la factura recien creada
		mx.org.kaana.libs.facturama.models.response.Cfdi cfdiCreated = facturama.Cfdis().Create(cfdi);

		System.out.println("Se creó exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid());

		// Descarga de los archivos de la factura
		String filePath = "factura" + cfdiCreated.getComplement().getTaxStamp().getUuid();
		facturama.Cfdis().SaveXml(filePath + ".xml", cfdiCreated.getId());

		// Se elmina la factura recien creada
		facturama.Cfdis().Remove(cfdiCreated.getId());
		System.out.println("Se elminó exitosamente el cfdi con el folio fiscal: " + cfdiCreated.getComplement().getTaxStamp().getUuid());

		//El correo que se ingrese debe existir 
		// Consulta de cfdis mediante palabra clave o rfc
		List<CfdiSearchResult> lstCfdiFilteredByKeyword = facturama.Cfdis().List("Expresion en Software");
		List<CfdiSearchResult> lstCfdiFilteredByRfc = facturama.Cfdis().ListFilterByRfc("ESO1202108R2");

		System.out.println("Se obtiene la lista de facturas: " + lstCfdiFilteredByKeyword.size());
		System.out.println("Se obtiene la lista de facturas por RFC: " + lstCfdiFilteredByRfc.size());

		System.out.println("----- Fin del ejemplo de CFDI -----");

	}

	private static mx.org.kaana.libs.facturama.models.request.CfdiLite createCfdi(FacturamaApiMultiemisor facturama, Currency currency) throws IOException, FacturamaException, Exception {

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

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		cfdi.setDate(dateFormat.format(date));
		cfdi.setExpeditionPlace("78180");

		Receiver receiver = new Receiver();
		receiver.setCfdiUse(facturama.Catalogs().CfdiUses("AAA010101AAA").get(0).getValue());
		receiver.setName("Receptor de Ejemplo");
		receiver.setRfc("ESO1202108R2");

		Issuer issuer = new Issuer();
		issuer.setFiscalRegime(facturama.Catalogs().FiscalRegimens().get(0).getValue());
		issuer.setName("Emisor de Ejemplo");
		issuer.setRfc("AAA010101AAA");
		cfdi.setIssuer(issuer);
		cfdi.setReceiver(receiver);

		return cfdi;

	}

	private static mx.org.kaana.libs.facturama.models.request.CfdiLite addItemsToCfdi(FacturamaApiMultiemisor facturama, Currency currency,
					mx.org.kaana.libs.facturama.models.request.CfdiLite cfdi) throws IOException, FacturamaException, Exception {

		Double price = 100.00;
		Double quantity = 2.00;
		Double discount = 10.00;
		int decimals = (int) currency.getDecimals();
		Double numberOfDecimals = Math.pow(10, decimals);

		Double subtotal = Math.round((price * quantity) * numberOfDecimals) / numberOfDecimals;
		List<Item> lstItems = new ArrayList<>();
		Item item = new Item();
		item.setProductCode(facturama.Catalogs().ProductsOrServices("desarrollo").get(0).getValue());
		item.setUnitCode(facturama.Catalogs().Units("pieza").get(1).getValue());
		item.setUnit("Libra");
		item.setDescription("Descripción del Producto");
		item.setIdentificationNumber("010101-56");
		item.setQuantity(quantity);
		item.setDiscount(Math.round(discount * numberOfDecimals) / numberOfDecimals);
		item.setUnitPrice(Math.round(price * numberOfDecimals) / numberOfDecimals);
		item.setSubtotal(subtotal);
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

		return item;

	}
}
