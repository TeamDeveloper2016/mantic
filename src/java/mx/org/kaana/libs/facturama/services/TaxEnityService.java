package mx.org.kaana.libs.facturama.services;

import mx.org.kaana.libs.facturama.models.response.TaxEnity;
import mx.org.kaana.libs.facturama.models.*;
import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import mx.org.kaana.libs.facturama.models.request.*;
import com.google.gson.Gson;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;

public class TaxEnityService extends HttpService<mx.org.kaana.libs.facturama.models.request.TaxEnity, mx.org.kaana.libs.facturama.models.response.TaxEnity> {

	public TaxEnityService(OkHttpClient client) {
		super(client, "taxenity");
	}

	public TaxEnity Retrive() throws IOException, FacturamaException, Exception {
		return Get("");
	}

	public TaxEnity Update(mx.org.kaana.libs.facturama.models.request.TaxEnity model) throws IOException, Exception {
		return Put(model, "");
	}

	public boolean UploadImage(Image img) throws IOException, Exception {

		HttpUrl.Builder urlBuilder
						= HttpUrl.parse(baseUrl + "/UploadImage").newBuilder();

		String url = urlBuilder.build().toString();

		String jsonObj = new Gson().toJson(img);

		RequestBody body = RequestBody.create(
						MediaType.parse("application/json; charset=utf-8"),
						jsonObj);

		Request request = new Request.Builder()
						.url(url)
						.put(body)
						.build();

		Response response = Execute(request);
		return response.code() == 204;
	}

	public boolean UploadCsd(Csd csd) throws IOException, Exception {
		HttpUrl.Builder urlBuilder
						= HttpUrl.parse(baseUrl + "/UploadCsd").newBuilder();

		String url = urlBuilder.build().toString();

		String jsonObj = new Gson().toJson(csd);

		RequestBody body = RequestBody.create(
						MediaType.parse("application/json; charset=utf-8"),
						jsonObj);

		Request request = new Request.Builder()
						.url(url)
						.put(body)
						.build();

		Response response = Execute(request);

		return response.code() == 204;
	}

}
