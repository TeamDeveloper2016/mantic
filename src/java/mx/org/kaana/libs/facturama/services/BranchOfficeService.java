package mx.org.kaana.libs.facturama.services;

import mx.org.kaana.libs.facturama.models.BranchOffice;
import com.squareup.okhttp.OkHttpClient;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class BranchOfficeService extends CrudService<BranchOffice, BranchOffice> {

	public BranchOfficeService(OkHttpClient client) {
		super(client, "BranchOffice");

		singleType = BranchOffice.class;
		multiType = new TypeToken<List<BranchOffice>>() {
		}.getType();

	}
}
