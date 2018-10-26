package mx.org.kaana.libs.facturama.container;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

/**
 *
 * @author user
 */
public class BaseUrlInterceptor implements Interceptor {

	private String _host;
	private String _schema;
	private int _port;

	public String getBaseUrl() {
		return this._schema + "://" + this._host + ":" + this._port;
	}

	public BaseUrlInterceptor(String schema, String host, int port) {
		_schema = schema;
		_host = host;
		_port = port;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		String host = this._host;
		if (host != null) {
			HttpUrl newUrl = request.httpUrl().newBuilder()
							.scheme(this._schema)
							.host(this._host)
							.port(this._port)
							.build();

			request = request.newBuilder()
							.url(newUrl)
							.build();
		}
		return chain.proceed(request);
	}
}
