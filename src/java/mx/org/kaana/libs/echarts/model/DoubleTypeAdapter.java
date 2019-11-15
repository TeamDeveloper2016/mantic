package mx.org.kaana.libs.echarts.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 *@company KAANA
 *@project KAJOOL (Control system polls)
 *@date 29/10/2019
 *@time 01:56:38 PM 
 *@author Team Developer 2016 <team.developer@kaana.org.mx>
 */

public class DoubleTypeAdapter  extends TypeAdapter<Double> {

	@Override
	public void write(JsonWriter writer, Double value) throws IOException {
    if (value == null) 
      writer.nullValue();
		else
			if(value== 0)
				writer.jsonValue("0 || '-'");
		  else
				writer.value(value);
	}

	@Override
	public Double read(JsonReader reader) throws IOException {
		Double regresar= null;
    if (reader.peek() == JsonToken.NULL) 
      reader.nextNull();
		else 
       regresar= reader.nextDouble();
     return regresar;
	}
	
}
