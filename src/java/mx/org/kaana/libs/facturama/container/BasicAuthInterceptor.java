package mx.org.kaana.libs.facturama.container;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.Proxy;

public class BasicAuthInterceptor implements Authenticator {

	private String credentials;

	public BasicAuthInterceptor(String user, String password) {
		this.credentials = Credentials.basic(user, password);
	}

	@Override
	public Request authenticate(Proxy proxy, Response response) throws IOException {
		String credential = credentials;
		return response.request().newBuilder().header("Authorization", credential).build();
	}

	@Override
	public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
		return null;
	}
}
