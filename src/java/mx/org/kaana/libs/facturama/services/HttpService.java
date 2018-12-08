package mx.org.kaana.libs.facturama.services;

import mx.org.kaana.libs.facturama.container.BaseUrlInterceptor;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.lang.reflect.Type;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import java.util.List;
import java.io.IOException;
import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.exception.ModelException;
import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;

public abstract class HttpService<TI, TO> {

	protected OkHttpClient httpClient;
	protected String baseUrl; // URL base (Test o Produccion)
	protected String relativeUrl;
	protected Type singleType, multiType;

	public HttpService(OkHttpClient client, String url) {
		httpClient = client;
		relativeUrl = url;
		httpClient.setConnectTimeout(90, TimeUnit.SECONDS);
		httpClient.setReadTimeout(90, TimeUnit.SECONDS);

		BaseUrlInterceptor interceptor = (BaseUrlInterceptor) httpClient.interceptors().get(0);
		this.baseUrl = interceptor.getBaseUrl();
	}

	protected final Response Execute(Request request) throws IOException, FacturamaException, Exception {
		Response response= null;
		StringBuilder sb = new StringBuilder();
		try {
			response = httpClient.newCall(request).execute();
			switch (response.code()) {
				case 400: // BadRequest                    
					String jsonData = response.body().string();
					ModelException exception = new Gson().fromJson(jsonData, ModelException.class);
					sb.append(exception.getMessage());
					for (String key : exception.getDetails().keySet()) {
						sb.append(key).append("= ");
						String[] msgs= exception.getDetails().get(key);
						for (String msg : msgs) {
  						sb.append(msg).append("|");
						} // for
					} // for 
					throw new FacturamaException(sb.toString(), exception);
				case 401: // Unauthorized
					throw new FacturamaException("No esta autorizado para realizar esta peticion, verifique su usuario y contrasenia y que su suscripcion se encuentre activa");
				case 500: // InternalServerError
					throw new Exception(response.message());
			} // switch
		} // try
		catch (ProtocolException ex) {
			throw new FacturamaException("No esta autorizado para realizar esta peticion, verifique su usuario y contrasenia y que su suscripcion se encuentre activa");
		}
		return response;
	}

	protected final TO Get() throws IOException, FacturamaException, Exception {
		return this.Get("");
	}

	protected final TO Get(String resource) throws IOException, FacturamaException, Exception {

		return this.Get(resource, singleType);
	}

	protected final TO Get(String resource, Type singleType) throws IOException, FacturamaException, Exception {
		HttpUrl.Builder urlBuilder
						= HttpUrl.parse(baseUrl + "/" + relativeUrl + "/" + resource).newBuilder();

		String url = urlBuilder.build().toString();

		Request request = new Request.Builder()
						.url(url)
						.build();

		Response response = Execute(request);
		String jsonData = response.body().string();

		TO object = new Gson().fromJson(jsonData, singleType);

		return object;
	}

	protected final List<TO> GetList() throws IOException, FacturamaException, Exception {
		return this.GetList("");
	}

	protected final List<TO> GetList(String resource) throws IOException, FacturamaException, Exception {

		return this.GetList(resource, this.multiType);

	}

	protected final List<TO> GetList(String resource, Type multiType) throws IOException, FacturamaException, Exception {

		HttpUrl.Builder urlBuilder
						= HttpUrl.parse(baseUrl + "/" + relativeUrl + resource).newBuilder();

		
		String url = urlBuilder.build().toString();

		Request request = new Request.Builder()
						.url(url)
						.build();

		Response response = Execute(request);
		String jsonData = response.body().string();

		List<TO> toList = new Gson().fromJson(jsonData, multiType);

		return toList;

	}

	protected final TO Post(TI obj) throws IOException, FacturamaException, Exception {
		return Post(obj, "");
	}

	protected final TO Post(TI obj, String urlParams) throws IOException, FacturamaException, Exception {

		if (obj == null) {
			throw new NullPointerException(singleType.getTypeName());
		}

		HttpUrl.Builder urlBuilder
						= HttpUrl.parse(baseUrl + "/" + relativeUrl + "/" + urlParams).newBuilder();

		String url = urlBuilder.build().toString();

		String jsonObj = new Gson().toJson(obj);

		RequestBody body = RequestBody.create(
						MediaType.parse("application/json; charset=utf-8"),
						jsonObj);

		Request request = new Request.Builder()
						.url(url)
						.post(body)
						.build();

		Response response = Execute(request);
		String jsonData = response.body().string();

		TO object = new Gson().fromJson(jsonData, singleType);

		return object;
	}

	protected final TO Put(TI obj, String id) throws IOException, FacturamaException, Exception {

		if (obj == null) {
			throw new NullPointerException(singleType.getTypeName());
		}

		HttpUrl.Builder urlBuilder
						= HttpUrl.parse(baseUrl + "/" + relativeUrl + "/" + id).newBuilder();

		String url = urlBuilder.build().toString();

		String jsonObj = new Gson().toJson(obj);

		RequestBody body = RequestBody.create(
						MediaType.parse("application/json; charset=utf-8"),
						jsonObj);

		Request request = new Request.Builder()
						.url(url)
						.put(body)
						.build();

		Response response = Execute(request);
		String jsonData = response.body().string();

		TO object = new Gson().fromJson(jsonData, singleType);

		return object;

	}

	protected final TO Delete(String resource) throws IOException, FacturamaException, Exception {

		HttpUrl.Builder urlBuilder
						= HttpUrl.parse(baseUrl + "/" + relativeUrl + "/" + resource).newBuilder();

		String url = urlBuilder.build().toString();

		Request request = new Request.Builder()
						.url(url)
						.delete()
						.build();

		Response response = Execute(request);
		String jsonData = response.body().string();

		TO object = new Gson().fromJson(jsonData, singleType);

		return object;

	}

}
