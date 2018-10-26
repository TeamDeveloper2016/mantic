package mx.org.kaana.libs.facturama.services.multiemisor;

import mx.org.kaana.libs.facturama.services.CrudService;
import mx.org.kaana.libs.facturama.models.Csd;
import com.squareup.okhttp.OkHttpClient;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class CsdService extends CrudService<Csd, Csd> {

	public CsdService(OkHttpClient client) {
		super(client, "api-lite/csds");

		singleType = Csd.class;
		multiType = new TypeToken<List<Csd>>() {
		}.getType();

	}

}
