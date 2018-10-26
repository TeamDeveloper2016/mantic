package mx.org.kaana.libs.facturama.services.multiemisor;

import mx.org.kaana.libs.facturama.services.HttpService;
import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.response.*;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.net.URLEncoder;
import mx.org.kaana.libs.facturama.models.response.InovoiceFile;
import java.io.FileOutputStream;
import org.primefaces.util.Base64;

public class CfdiService extends HttpService { //<mx.org.kaana.libs.facturama.models.request.Cfdi,mx.org.kaana.libs.facturama.models.response.Cfdi>{

	private enum FileFormat {
		Xml, Pdf, Html
	}

	public enum InvoiceType {
		Issued, Received, Payroll
	}

	public enum CfdiStatus {
		All, Active, Cancel
	}

	public CfdiService(OkHttpClient client) {
		super(client, "");
		singleType = mx.org.kaana.libs.facturama.models.response.Cfdi.class;
		multiType = new TypeToken<List<mx.org.kaana.libs.facturama.models.response.Cfdi>>() {
		}.getType();
	}

	public mx.org.kaana.libs.facturama.models.response.Cfdi Create(mx.org.kaana.libs.facturama.models.request.CfdiLite model) throws IOException, FacturamaException, Exception {
		return (mx.org.kaana.libs.facturama.models.response.Cfdi) Post(model, "api-lite/2/cfdis");
	}

	public mx.org.kaana.libs.facturama.models.response.Cfdi Remove(String id) throws IOException, FacturamaException, Exception {
		if (id != null && !id.isEmpty()) {
			return (mx.org.kaana.libs.facturama.models.response.Cfdi) Delete("api-lite/cfdi/" + id + "?type=issuedLite");
		} else {
			throw new NullPointerException(singleType.getTypeName());
		}
	}

	public mx.org.kaana.libs.facturama.models.response.Cfdi Retrive(String id) throws IOException, FacturamaException, Exception {
		return (mx.org.kaana.libs.facturama.models.response.Cfdi) Get("cfdi/" + id + "?type=IssuedLite");
	}

	public mx.org.kaana.libs.facturama.models.response.Cfdi Retrive(String id, InvoiceType type) throws IOException, FacturamaException, Exception {
		return (mx.org.kaana.libs.facturama.models.response.Cfdi) Get("cfdi/" + id + "?type=" + type.toString());
	}

	public List<CfdiSearchResult> List(String keyword) throws IOException, FacturamaException, Exception {
		return this.List(keyword, CfdiStatus.Active);
	}

	public List<CfdiSearchResult> List(String keyword, CfdiStatus status) throws IOException, FacturamaException, Exception {
		return this.List(keyword, CfdiStatus.Active, InvoiceType.Issued);
	}

	public List<CfdiSearchResult> List(String keyword, CfdiStatus status, InvoiceType type) throws IOException, FacturamaException, Exception {
		keyword = URLEncoder.encode(keyword);

		String resource = "cfdi?type=" + type + "Lite&keyword=" + keyword + "&status=" + status;

		return GetList(resource, new TypeToken<List<mx.org.kaana.libs.facturama.models.response.CfdiSearchResult>>() {
		}.getType());
	}

	public List<CfdiSearchResult> List() throws IOException, FacturamaException, Exception {
		return this.List(-1, -1,
						null, null,
						"", "",
						"", "",
						CfdiStatus.Active, InvoiceType.Issued);
	}

	public List<CfdiSearchResult> ListFilterByRfc(String rfc) throws IOException, FacturamaException, Exception {
		return this.List(-1, -1,
						rfc, null,
						null, null,
						null, null,
						CfdiStatus.Active, InvoiceType.Issued);
	}

	public List<CfdiSearchResult> List(int folioStart, int folioEnd,
					String rfc, String taxEntityName,
					String dateStart, String dateEnd,
					String idBranch, String serie,
					CfdiStatus status, InvoiceType type) throws IOException, FacturamaException, Exception {

		String resource = "cfdi?type=" + type + "Lite&status=" + status;

		if (folioStart > -1) {
			resource += "&folioStart=" + folioStart;
		}

		if (folioEnd > -1) {
			resource += "&folioEnd=" + folioEnd;
		}

		if (rfc != null) {
			resource += "&rfc=" + rfc;
		}

		if (taxEntityName != null) {
			resource += "&taxEntityName=" + taxEntityName;
		}

		if (dateStart != null) {
			resource += "&dateStart=" + dateStart;
		}

		if (dateEnd != null) {
			resource += "&dateEnd=" + dateEnd;
		}

		if (idBranch != null) {
			resource += "&idBranch=" + idBranch;
		}

		if (serie != null) {
			resource += "&serie=" + serie;
		}

		return GetList(resource, new TypeToken<List<CfdiSearchResult>>() {
		}.getType());
	}

	/**
	 * -
	 * Obtiene un archivo referente a un CFDI del tipo "Issued"
	 *
	 * @param id Identificador del CFDI
	 * @param format Formato deseado ( pdf | html | xml )
	 * @return Archivo en cuestion
	 */
	public InovoiceFile GetFile(String id, FileFormat format) throws Exception {
		return GetFile(id, format, InvoiceType.Issued);
	}

	/**
	 * Obtiene un archivo referente a un CFDI
	 *
	 * @param id Identificador del CFDI
	 * @param format Formato deseado ( pdf | html | xml )
	 * @param type Tipo de comprobante ( payroll | received | issued )
	 * @return Archivo en cuestion
	 */
	public InovoiceFile GetFile(String id, FileFormat format, InvoiceType type) throws FacturamaException, Exception {

		String resource = "cfdi/" + format + "/" + type + "Lite/" + "/" + id;

		InovoiceFile file = (InovoiceFile) Get(resource, InovoiceFile.class);

		return file;
	}

	public void SaveXml(String filePath, String id) throws Exception {
		SaveXml(filePath, id, InvoiceType.Issued);
	}

	/**
	 * Guardada el XML de un CFDI en la ruta especificada
	 *
	 * @param filePath Ruta donde se va a guardar el PDF
	 * @param id Idenficador del CFDI
	 * @param type Tipo del comprobante (payroll | received | issued)
	 */
	public void SaveXml(String filePath, String id, InvoiceType type) throws Exception {
		InovoiceFile file = GetFile(id, FileFormat.Xml, type);

		FileOutputStream fos = new FileOutputStream(filePath);
		fos.write(Base64.decode(file.getContent()));
		fos.close();
	}

}
