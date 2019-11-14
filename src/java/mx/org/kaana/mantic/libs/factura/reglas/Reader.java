package mx.org.kaana.mantic.libs.factura.reglas;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import mx.org.kaana.libs.formato.Error;
import mx.org.kaana.libs.formato.Cadena;
import mx.org.kaana.mantic.libs.factura.beans.ComprobanteFiscal;
import mx.org.kaana.mantic.libs.factura.beans.Concepto;
import mx.org.kaana.mantic.libs.factura.beans.Emisor;
import mx.org.kaana.mantic.libs.factura.beans.Impuesto;
import mx.org.kaana.mantic.libs.factura.beans.InformacionAduanera;
import mx.org.kaana.mantic.libs.factura.beans.Receptor;
import mx.org.kaana.mantic.libs.factura.beans.TimbreFiscalDigital;
import mx.org.kaana.mantic.libs.factura.beans.Traslado;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Reader implements Serializable{
	private static final Log LOG=LogFactory.getLog(Reader.class);
	private static final long serialVersionUID = -7129012131006642751L;
	private String fileName;

	public Reader(String fileName) {
		this.fileName = fileName;
	}	
			
	public ComprobanteFiscal execute() throws Exception{
		ComprobanteFiscal regresar= null;
		Document document         = null;
		InputStream inputStream   = null;
		DocumentBuilderFactory dbf= null;
		DocumentBuilder db        = null;
		try {        
			dbf= DocumentBuilderFactory.newInstance();
			dbf.setIgnoringComments(true);
			dbf.setIgnoringElementContentWhitespace(true);
      db = dbf.newDocumentBuilder();
			inputStream= new FileInputStream(new File(this.fileName));        
			document= db.parse(inputStream);
      document.getDocumentElement().normalize();					
			regresar= readHeader(document);			
			regresar.setEmisor(readEmisor(document));
			regresar.setReceptor(readReceptor(document));
			regresar.setConceptos(readConceptos(document));
			regresar.setImpuesto(readImpuesto(document));
			regresar.setTimbreFiscalDigital(readTimbreFiscal(document));
    } // try 
		catch (Exception e) {
			Error.mensaje(e);			
    } // catch	
		return regresar;
	} // procesar
	
	private ComprobanteFiscal readHeader(Document document){
		ComprobanteFiscal regresar= null;
		String nameAttr           = null;
		String valAttr            = null;
		NamedNodeMap mapAttrs     = null;
		try {
			regresar= new ComprobanteFiscal();
			mapAttrs= document.getDocumentElement().getAttributes();			
			for (int i = 0; i < mapAttrs.getLength(); i++){
				nameAttr= mapAttrs.item(i).getNodeName();
				valAttr= mapAttrs.item(i).getNodeValue();
				switch(nameAttr){
					case "xmlns:xsi":
						regresar.setXsi(valAttr);
						break;
					case "xsi:schemaLocation":
						regresar.setSchemaLocation(valAttr);
						break;
					case "Version":
						regresar.setVersion(valAttr);
						break;
					case "Folio":
						regresar.setFolio(valAttr);
						break;
					case "Fecha":
						regresar.setFecha(valAttr);
						break;
					case "Sello":
						regresar.setSello(valAttr);
						break;
					case "FormaPago":
						regresar.setFormaPago(valAttr);
						break;
					case "NoCertificado":
						regresar.setNoCertificado(valAttr);
						break;
					case "Certificado":
						regresar.setCertificado(valAttr);
						break;
					case "SubTotal":
						regresar.setSubTotal(valAttr);
						break;
					case "Moneda":
						regresar.setMoneda(valAttr);
						break;						
					case "TipoCambio":
						regresar.setTipoCambio(valAttr);
						break;
					case "Total":
						regresar.setTotal(valAttr);
						break;
					case "TipoDeComprobante":
						regresar.setTipoDeComprobante(valAttr);
						break;
					case "MetodoPago":
						regresar.setMetodoPago(valAttr);
						break;						
					case "LugarExpedicion":
						regresar.setLugarExpedicion(valAttr);
						break;
					case "xmlns:cfdi":
						regresar.setCfdi(valAttr);
						break;
				} // switch				
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // readHeader
	
	private Emisor readEmisor(Document document){
		Emisor regresar      = null;
		String nameAttr      = null;
		String valAttr       = null;
		NamedNodeMap mapAttrs= null;
		try {
			regresar= new Emisor();
			mapAttrs= document.getDocumentElement().getElementsByTagName("cfdi:Emisor").item(0).getAttributes();	
			for (int i = 0; i < mapAttrs.getLength(); i++){
				nameAttr= mapAttrs.item(i).getNodeName();
				valAttr= mapAttrs.item(i).getNodeValue();
				switch(nameAttr){
					case "Nombre":
						regresar.setNombre(valAttr);
					break;
					case "RegimenFiscal":
						regresar.setRegimenFiscal(valAttr);
					break;
					case "Rfc":
						regresar.setRfc(valAttr);
					break;
				} // switch
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		LOG.info("Reader.readEmisor: "+ regresar.getNombre());
		return regresar;
	} // readEmisor
	
	private Receptor readReceptor(Document document){
		Receptor regresar    = null;
		String nameAttr      = null;
		String valAttr       = null;
		NamedNodeMap mapAttrs= null;
		try {
			regresar= new Receptor();
			mapAttrs= document.getDocumentElement().getElementsByTagName("cfdi:Receptor").item(0).getAttributes();	
			for (int i = 0; i < mapAttrs.getLength(); i++){
				nameAttr= mapAttrs.item(i).getNodeName();
				valAttr= mapAttrs.item(i).getNodeValue();
				switch(nameAttr){
					case "Nombre":
						regresar.setNombre(valAttr);
					break;
					case "UsoCFDI":
						regresar.setUsoCfdi(valAttr);
					break;
					case "Rfc":
						regresar.setRfc(valAttr);
					break;
				} // switch
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		LOG.info("Reader.readReceptor: "+ regresar.getNombre());
		return regresar;
	} // readReceptor
	
	private List<Concepto> readConceptos(Document document){
		List<Concepto> regresar= null;
		Concepto concepto      = null;
		String nameAttr        = null;
		String valAttr         = null;
		NamedNodeMap mapAttrs  = null;
		NodeList conceptos     = null;
		Node node              = null;
		String nodeName        = null;
		try {
			regresar= new ArrayList<>();
			conceptos= document.getDocumentElement().getElementsByTagName("cfdi:Conceptos").item(0).getChildNodes();				
			for (int i = 0; i < conceptos.getLength(); i++){				
				node= conceptos.item(i)== null? conceptos.item(i).getNextSibling(): conceptos.item(i);
				if(node!= null) {
					nodeName= node.getNodeName();
					if(nodeName.equals("cfdi:Concepto")) {
						concepto= new Concepto();
						mapAttrs= node.getAttributes();
						for (int count = 0; count < mapAttrs.getLength(); count++){						
							nameAttr= mapAttrs.item(count).getNodeName();
							valAttr= mapAttrs.item(count).getNodeValue();
							switch(nameAttr){
								case "Cantidad":
									concepto.setCantidad(valAttr);
									break;
								case "ClaveProdServ":
									concepto.setClaveProdServ(valAttr!= null? valAttr.trim(): "");
									break;
								case "ClaveUnidad":
									concepto.setClaveUnidad(valAttr);
									break;
								case "Descripcion":
									concepto.setDescripcion(valAttr);
									break;
								case "Importe":
									concepto.setImporte(valAttr);
									break;
								case "NoIdentificacion":
									concepto.setNoIdentificacion(valAttr!= null? Cadena.eliminar(valAttr, ' '): valAttr);
									break;
								case "Unidad":
									concepto.setUnidad(valAttr);
									break;
								case "ValorUnitario":
									concepto.setValorUnitario(valAttr);
									break;
								case "Descuento":
									concepto.setDescuento(valAttr);
									break;
							} // switc						
						} // for		
        		LOG.info("Reader.readConceptos: "+ concepto.getDescripcion());
 						concepto.setTraslado(readTraslado(node.getFirstChild().getNodeName().equals("cfdi:Impuestos")? node.getFirstChild(): node.getFirstChild().getNextSibling()));
						concepto.setInformacionAduanera(readInformacionAduanera(node));
						if(regresar.indexOf(concepto)< 0)
						  regresar.add(concepto);
					} // if
				} // if
			} // for
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // readConceptos
	
	private Traslado readTraslado(Node impuestos) {
		Traslado regresar    = null;
		Node traslados       = null;
		Node traslado        = null;
		NamedNodeMap mapAttrs= null;
		String nameAttr      = null;
		String valAttr       = null;
		try {			
			regresar = new Traslado();
			traslados= impuestos.getFirstChild()== null? impuestos.getNextSibling(): impuestos.getFirstChild().getNodeName().equals("#text")? impuestos.getFirstChild().getNextSibling(): impuestos.getFirstChild();
			if(traslados.getNodeName().equals("cfdi:Traslados")){
				traslado= traslados.getFirstChild().getNodeName().equals("#text")? traslados.getFirstChild().getNextSibling(): traslados.getFirstChild();
				if(traslado.getNodeName().equals("cfdi:Traslado")){
					mapAttrs= traslado.getAttributes();
					for (int count = 0; count < mapAttrs.getLength(); count++){
						nameAttr= mapAttrs.item(count).getNodeName();
						valAttr= mapAttrs.item(count).getNodeValue();
						switch(nameAttr){
							case "Base":
								regresar.setBase(valAttr);
								break;
							case "Importe":
								regresar.setImporte(valAttr);
								break;
							case "Impuesto":
								regresar.setImpuesto(valAttr);
								break;
							case "TasaOCuota":
								regresar.setTasaCuota(valAttr);
								break;
							case "TipoFactor":
								regresar.setTipoFactor(valAttr);
								break;
						} // switch
					} // for
				} // if
			} // if
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
 		LOG.info("Reader.readTraslado: "+ regresar);
		return regresar;
	} // readTraslado
	
	private InformacionAduanera readInformacionAduanera(Node node){
		InformacionAduanera regresar= null;
		NodeList list               = null;
		Node pivote                 = null;
		NamedNodeMap mapAttrs       = null;
		String nameAttr             = null;
		String valAttr              = null;
		try {			
			regresar= new InformacionAduanera();
			pivote= node;
			list= pivote.getChildNodes();
			for(int i=0; i<list.getLength(); i++){
				if(list.item(i).getNodeName().equals("cfdi:InformacionAduanera")){
					mapAttrs= list.item(i).getAttributes();				
					for (int count = 0; count < mapAttrs.getLength(); count++){
						nameAttr= mapAttrs.item(count).getNodeName();
						valAttr= mapAttrs.item(count).getNodeValue();
						switch(nameAttr){
							case "NumeroPedimento":
								regresar.setNumeroPedimento(valAttr);
								break;
						} // switch
					} // for
				} // if
			} // for			
		} // try
		catch (Exception e) {		
			throw e;
		} // catch		
 		LOG.info("Reader.readInformacionAduanera: "+ regresar.getNumeroPedimento());
		return regresar;
	} // readInformacionAduanera
	
	private Impuesto readImpuesto(Document document){
		Impuesto regresar    = null;
		String nameAttr      = null;
		String valAttr       = null;
		NamedNodeMap mapAttrs= null;
		Node node            = null;
		NodeList list        = null;
		try {
			regresar= new Impuesto();
			list= document.getChildNodes().item(0).getChildNodes();
			for(int count =0; count< list.getLength(); count++){
				node= list.item(count);
				if(node.getNodeName().equals("cfdi:Impuestos")){
					mapAttrs= node.getAttributes();	
					for (int i = 0; i < mapAttrs.getLength(); i++){
						nameAttr= mapAttrs.item(i).getNodeName();
						valAttr= mapAttrs.item(i).getNodeValue();
						switch(nameAttr){
							case "TotalImpuestosTrasladados":
								regresar.setTotalImpuestosTrasladados(valAttr);
							break;										
						} // switch
					} // for
					regresar.setTraslado(readTraslado(node));
				} // if
			} // for			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
 		LOG.info("Reader.readImpuesto: "+ regresar.getTraslado());
		return regresar;
	} // readImpuesto
	
	private TimbreFiscalDigital readTimbreFiscal(Document document){
		TimbreFiscalDigital regresar= null;
		NamedNodeMap mapAttrs       = null;
		Node node                   = null;
		NodeList list               = null;
		try {
			regresar= new TimbreFiscalDigital();
			list= document.getChildNodes().item(0).getChildNodes();
			for(int count =0; count< list.getLength(); count++){
				node= list.item(count);
				if(node.getNodeName().equals("cfdi:Complemento")){
					mapAttrs= node.getFirstChild().getAttributes()!= null? node.getFirstChild().getAttributes(): node.getFirstChild().getNextSibling().getAttributes();					
					regresar.setFechaTimbrado(mapAttrs.getNamedItem("FechaTimbrado").getNodeValue());
					regresar.setNoCertificadoSat(mapAttrs.getNamedItem("NoCertificadoSAT").getNodeValue());
					regresar.setRfcProvCertif(mapAttrs.getNamedItem("RfcProvCertif").getNodeValue());
					regresar.setSchemaLocation(mapAttrs.getNamedItem("xsi:schemaLocation").getNodeValue());
					regresar.setSelloCfd(mapAttrs.getNamedItem("SelloCFD").getNodeValue());
					regresar.setSelloSat(mapAttrs.getNamedItem("SelloSAT").getNodeValue());
					regresar.setTfd(mapAttrs.getNamedItem("xmlns:tfd").getNodeValue());
					regresar.setUuid(mapAttrs.getNamedItem("UUID").getNodeValue());
					regresar.setVersion(mapAttrs.getNamedItem("Version").getNodeValue());
					if(mapAttrs.getNamedItem("xmlns:xsi")!= null)
					  regresar.setXsi(mapAttrs.getNamedItem("xmlns:xsi").getNodeValue());
				} // if
			} // for			
		} // try
		catch (Exception e) {			
			throw e;
		} // catch		
		return regresar;
	} // readTimbreFiscal	
}


