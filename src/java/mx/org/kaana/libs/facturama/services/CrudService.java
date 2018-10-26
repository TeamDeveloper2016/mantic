/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.org.kaana.libs.facturama.services;

import mx.org.kaana.libs.facturama.models.exception.FacturamaException;
import com.squareup.okhttp.OkHttpClient;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author user
 * @param <TI>
 * @param <TO>
 */
public class CrudService<TI, TO> extends HttpService<TI, TO> {

	public CrudService(OkHttpClient client, String url) {
		super(client, url);
	}

	public TO Retrieve(String id) throws IOException, FacturamaException, Exception {
		return super.Get(id);
	}

	public List<TO> List() throws IOException, FacturamaException, Exception {
		return super.GetList();
	}

	public TO Create(TI obj) throws IOException, FacturamaException, Exception {
		return super.Post(obj);
	}

	public TO Remove(String id) throws IOException, FacturamaException, Exception {
		return super.Delete(id);
	}

	public TO Update(TI model, String id) throws IOException, FacturamaException, Exception {
		return super.Put(model, id);
	}

}
