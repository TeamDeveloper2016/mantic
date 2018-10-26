package mx.org.kaana.libs.facturama.services;

import mx.org.kaana.libs.facturama.models.Client;
import com.squareup.okhttp.OkHttpClient;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class ClientService extends CrudService<Client, Client> {

	public ClientService(OkHttpClient client) {
		super(client, "client");

		singleType = Client.class;
		multiType = new TypeToken<List<Client>>() {
		}.getType();
	}
}