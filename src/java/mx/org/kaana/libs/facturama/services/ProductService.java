package mx.org.kaana.libs.facturama.services;

import mx.org.kaana.libs.facturama.models.Product;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import java.util.List;

public class ProductService extends CrudService<Product, Product> {

	public ProductService(OkHttpClient client) {
		super(client, "product");

		singleType = Product.class;
		multiType = new TypeToken<List<Product>>() {
		}.getType();
	}
}
