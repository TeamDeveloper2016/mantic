package mx.org.kaana.libs.facturama.services;

import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.response.*;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;

public class ProfileService extends HttpService<Profile, Profile> {

	public ProfileService(OkHttpClient client) {
		super(client, "profile");
	}

	public Profile Retrive() throws IOException, FacturamaException, Exception {
		return Get("");
	}
}
