package mx.org.kaana.libs.facturama.services;

import mx.org.kaana.libs.facturama.models.*;
import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.util.List;

public class SerieService extends HttpService<Serie, Serie> {

	public SerieService(OkHttpClient client) {
		super(client, "serie");
	}

	public List<Serie> List(String idBranchOffice) throws IOException, FacturamaException, Exception {
		return GetList(idBranchOffice);

	}

	public Serie Retrive(String idBranchOffice, String SerieName) throws IOException, FacturamaException, Exception {
		return Get("" + idBranchOffice + "/" + SerieName + "");
	}

	public Serie Update(Serie model) throws IOException, Exception {
		return Put(model, "" + model.getIdBranchOffice() + "/" + model.getName());
	}

	public Serie Remove(String idBranchOffice, String SerieName) throws IOException, Exception {
		return Delete("" + idBranchOffice + "/" + SerieName);
	}
}
